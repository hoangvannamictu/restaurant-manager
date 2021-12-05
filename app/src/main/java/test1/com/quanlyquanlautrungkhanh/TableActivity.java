package test1.com.quanlyquanlautrungkhanh;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import test1.com.quanlyquanlautrungkhanh.Adapter.TableAdapter;
import test1.com.quanlyquanlautrungkhanh.Adapter.UserAdapter;
import test1.com.quanlyquanlautrungkhanh.Model.Table;
import test1.com.quanlyquanlautrungkhanh.Model.User;

public class TableActivity extends AppCompatActivity {
    private RecyclerView rcv_table;
    private CardView cvAdd_table;
    private TableAdapter mTableAdapter;

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference();

        anhXa();
        readListUserFromFirebase();
        addEvents();
    }

    private void anhXa() {
        rcv_table = findViewById(R.id.rcv_table);
        cvAdd_table = findViewById(R.id.cvAdd_table);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rcv_table.setLayoutManager(gridLayoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mTableAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mTableAdapter.stopListening();
    }

    private void readListUserFromFirebase() {
        FirebaseRecyclerOptions<Table> options = new FirebaseRecyclerOptions.Builder<Table>()
                .setQuery(ref.child("tables"), Table.class)
                .setLifecycleOwner(this)
                .build();

        mTableAdapter = new TableAdapter(options, TableActivity.this, new TableAdapter.IClickTableAdapter() {
            @Override
            public void clickDeleteTable(Table model, int position) {
                openDialogDeleteTable(model, position);
            }

            @Override
            public void clickEditTable(Table model, int position) {
                openDialogEditTable(model, position);
            }
        });

        rcv_table.setAdapter(mTableAdapter);
    }

    private void addEvents() {
        cvAdd_table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(TableActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.layout_dialog_add_table);
                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

                Button btnAdd_table = dialog.findViewById(R.id.btnAdd_table);
                Button btnQuitDiaLogAdd_table = dialog.findViewById(R.id.btnQuitDiaLogAdd_table);
                Button btnAddChooseStatus_table = dialog.findViewById(R.id.btnAddChooseStatus_table);
                EditText edtAddId_table = dialog.findViewById(R.id.edtAddId_table);
                EditText edtAddType_table = dialog.findViewById(R.id.edtAddType_table);
                TextView tvAddStatus_table = dialog.findViewById(R.id.tvAddStatus_table);

                btnAddChooseStatus_table.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openMenuChooseStatusTable(tvAddStatus_table, btnAddChooseStatus_table);
                    }
                });

                btnAdd_table.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String strAddId_table = edtAddId_table.getText().toString();
                        String strAddType_table = edtAddType_table.getText().toString();
                        String strCurrentStatus_table = tvAddStatus_table.getText().toString();
                        String strAddStatus_table;
                        String strDangTrong = getResources().getString(R.string.dang_trong);
                        String strDangBaoTri = getResources().getString(R.string.dang_bao_tri);
                        String strDaDatTruoc = getResources().getString(R.string.da_dat_truoc);

                        Calendar calendar = Calendar.getInstance();
                        String strKey = "table" + calendar.getTimeInMillis();

                        if (strAddId_table.isEmpty() || strAddType_table.isEmpty() || strCurrentStatus_table.isEmpty()) {
                            Toast.makeText(TableActivity.this, getResources().getString(R.string.khong_de_trong), Toast.LENGTH_SHORT).show();
                        } else {
                            if (strCurrentStatus_table.equals(strDaDatTruoc)) {
                                strAddStatus_table = "waiting";
                            } else if (strCurrentStatus_table.equals(strDangBaoTri)) {
                                strAddStatus_table = "repair";
                            } else {
                                strAddStatus_table = "empty";
                            }

                            Table mTable = new Table(strKey, Integer.valueOf(strAddId_table), Integer.valueOf(strAddType_table), strAddStatus_table);
                            if (LoginActivity.isInternetConnection(TableActivity.this)) {
                                addTableOnFirebaseDatabase(mTable, strKey);
                            } else {
                                Toast.makeText(TableActivity.this, getResources().getString(R.string.khong_internet), Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();

                        }
                    }
                });

                btnQuitDiaLogAdd_table.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
    }

    private void openDialogEditTable(Table model, int position) {
        Dialog dialog = new Dialog(TableActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_edit_table);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        Button btnEdit_table = dialog.findViewById(R.id.btnEdit_table);
        Button btnQuitDiaLogEdit_table = dialog.findViewById(R.id.btnQuitDiaLogEdit_table);
        Button btnEditChooseStatus_table = dialog.findViewById(R.id.btnEditChooseStatus_table);
        EditText edtEditId_table = dialog.findViewById(R.id.edtEditId_table);
        EditText edtEditType_table = dialog.findViewById(R.id.edtEditType_table);
        TextView tvEditStatus_table = dialog.findViewById(R.id.tvEditStatus_table);

        edtEditId_table.setText(String.valueOf(model.getIdTable()));
        edtEditType_table.setText(String.valueOf(model.getTypeTable()));
        switch (model.getStatusTable()) {
            case "empty": {
                tvEditStatus_table.setText(getResources().getString(R.string.dang_trong));
                break;
            }

            case "repair": {
                tvEditStatus_table.setText(getResources().getString(R.string.dang_bao_tri));
                break;
            }

            case "waiting": {
                tvEditStatus_table.setText(getResources().getString(R.string.da_dat_truoc));
                break;
            }
        }

        btnEditChooseStatus_table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMenuChooseStatusTable(tvEditStatus_table, btnEditChooseStatus_table);
            }
        });

        btnEdit_table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strEditId_table = edtEditId_table.getText().toString();
                String strEditType_table = edtEditType_table.getText().toString();
                String strCurrentStatus_table = tvEditStatus_table.getText().toString();
                String strEditStatus_table;
                String strDangTrong = getResources().getString(R.string.dang_trong);
                String strDangBaoTri = getResources().getString(R.string.dang_bao_tri);
                String strDaDatTruoc = getResources().getString(R.string.da_dat_truoc);

                if (strEditId_table.isEmpty() || strEditType_table.isEmpty() || strCurrentStatus_table.isEmpty()) {
                    Toast.makeText(TableActivity.this, getResources().getString(R.string.khong_de_trong), Toast.LENGTH_SHORT).show();
                } else {
                    if (strCurrentStatus_table.equals(strDaDatTruoc)) {
                        strEditStatus_table = "waiting";
                    } else if (strCurrentStatus_table.equals(strDangBaoTri)) {
                        strEditStatus_table = "repair";
                    } else {
                        strEditStatus_table = "empty";
                    }

                    Table mTable = new Table(model.getKey(), Integer.valueOf(strEditId_table), Integer.valueOf(strEditType_table), strEditStatus_table);

                    if (LoginActivity.isInternetConnection(TableActivity.this)) {
                        editTableOnFirebaseDatabase(mTable);
                    } else {
                        Toast.makeText(TableActivity.this, getResources().getString(R.string.khong_internet), Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();

                }

            }
        });

        btnQuitDiaLogEdit_table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void openDialogDeleteTable(Table model, int position) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(TableActivity.this, R.style.AlertDialog);
        dialog.setTitle(getResources().getString(R.string.xoa_ban));
        dialog.setMessage(getResources().getString(R.string.ban_cochac_muon_xoakhong));

        dialog.setPositiveButton(getResources().getString(R.string.co), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteTableOnFirebaseDatabase(model.getKey());
            }
        });

        dialog.setNegativeButton(getResources().getString(R.string.khong), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void addTableOnFirebaseDatabase(Table mTable, String strKey) {
        ref.child("tables").child(strKey).setValue(mTable, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null) {
                    Toast.makeText(TableActivity.this, getResources().getString(R.string.them_ban_thanh_cong), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TableActivity.this, getResources().getString(R.string.loi) + " : " + error, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void deleteTableOnFirebaseDatabase(String strKey) {
        ref.child("tables").child(strKey).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null) {
                    Toast.makeText(TableActivity.this, getResources().getString(R.string.xoa_thanhcong), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TableActivity.this, getResources().getString(R.string.loi) + " : " + error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void editTableOnFirebaseDatabase(Table mTable) {
        ref.child("tables").child(mTable.getKey()).setValue(mTable, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null) {
                    Toast.makeText(TableActivity.this, getResources().getString(R.string.sua_ban_thanh_cong), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TableActivity.this, getResources().getString(R.string.loi) + " : " + error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openMenuChooseStatusTable(TextView tvAddStatus_table, Button btnAddChooseStatus_table) {
        PopupMenu popupMenu = new PopupMenu(TableActivity.this, btnAddChooseStatus_table);
        popupMenu.getMenuInflater().inflate(R.menu.menu_status_table, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menuStatusEmpty_table: {
                        tvAddStatus_table.setText(item.getTitle());
                        break;
                    }

                    case R.id.menuStatusRepair_table: {
                        tvAddStatus_table.setText(item.getTitle());
                        break;
                    }

                    case R.id.menuStatusWaiting_table: {
                        tvAddStatus_table.setText(item.getTitle());
                        break;
                    }
                }
                return false;
            }
        });
        popupMenu.show();
    }

}