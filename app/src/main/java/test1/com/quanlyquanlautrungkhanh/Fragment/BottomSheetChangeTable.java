package test1.com.quanlyquanlautrungkhanh.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import test1.com.quanlyquanlautrungkhanh.Adapter.ChangeTableAdapter;
import test1.com.quanlyquanlautrungkhanh.Model.Bill;
import test1.com.quanlyquanlautrungkhanh.Model.ItemFood;
import test1.com.quanlyquanlautrungkhanh.Model.Table;
import test1.com.quanlyquanlautrungkhanh.R;
import test1.com.quanlyquanlautrungkhanh.UserActivity;

public class BottomSheetChangeTable extends BottomSheetDialogFragment {
    private static final String KEY_GET_TABLE_MODEL = "my_table_model";
    private ChangeTableAdapter mChangeTableAdapter;
    private Table tableCurrent;
    private String strKeyNewTable;
    private String statusCurrent = "empty";

    private BottomSheetBehavior mBehavior;

    private ImageButton btnDone_changeTable;
    private RecyclerView rcv_changeTable;
    private TextView tvIdTableOld_changeTable, tvIdTableNew_changeTable;
    private CardView cardViewNew_changeTable;

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;
    private DatabaseReference ref;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference();

        Bundle bundleReceive = getArguments();
        if (bundleReceive != null) {
            tableCurrent = (Table) bundleReceive.getSerializable(KEY_GET_TABLE_MODEL);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_bottom_sheet_change_table, null);

        LinearLayout linearLayout = view.findViewById(R.id.root_changeTable);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
        params.height = getScreenHeight();
        linearLayout.setLayoutParams(params);


        bottomSheetDialog.setContentView(view);
        mBehavior = BottomSheetBehavior.from((View) view.getParent());

        anhXa(view);
        readListTableFromFirebase(statusCurrent);
        addEvents();
        return bottomSheetDialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        mChangeTableAdapter.startListening();
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void onStop() {
        super.onStop();
        mChangeTableAdapter.stopListening();
    }

    private void anhXa(View view) {
        rcv_changeTable = view.findViewById(R.id.rcv_changeTable);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext().getApplicationContext(), 2);
        rcv_changeTable.setLayoutManager(gridLayoutManager);

        btnDone_changeTable = view.findViewById(R.id.btnDone_changeTable);
        tvIdTableOld_changeTable = view.findViewById(R.id.tvIdTableOld_changeTable);
        tvIdTableNew_changeTable = view.findViewById(R.id.tvIdTableNew_changeTable);
        cardViewNew_changeTable = view.findViewById(R.id.cardViewNew_changeTable);

        tvIdTableOld_changeTable.setText(String.valueOf(tableCurrent.getIdTable()));

    }

    private void readListTableFromFirebase(String s) {
        Query query = ref.child("tables").orderByChild("statusTable").startAt(s).endAt(s+"\uf8ff");
        FirebaseRecyclerOptions<Table> options = new FirebaseRecyclerOptions.Builder<Table>()
                .setQuery(query, Table.class)
                .setLifecycleOwner(this)
                .build();

        mChangeTableAdapter = new ChangeTableAdapter(options, getContext().getApplicationContext(), new ChangeTableAdapter.IClickChangeTableAdapter() {
            @Override
            public void clickChooseTable(Table mTable) {
                setTextViewNewTable(mTable);
            }
        });
        rcv_changeTable.setAdapter(mChangeTableAdapter);
    }

    private void addEvents() {
        btnDone_changeTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strNewIdTable = tvIdTableNew_changeTable.getText().toString();

                if (strNewIdTable.isEmpty()) {
                    Toast.makeText(getContext(), getResources().getString(R.string.ban_chua_chon_ban), Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext(), R.style.AlertDialog);
                    dialog.setTitle(getResources().getString(R.string.thay_doi));
                    dialog.setMessage(getResources().getString(R.string.ban_cochac_muon_thaydoi_khong));

                    dialog.setPositiveButton(getResources().getString(R.string.co), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            changeTableOnFirebaseDatabase(strNewIdTable);
                            dialog.dismiss();
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
            }
        });
    }

    private void changeTableOnFirebaseDatabase(String strNewIdTable) {
        DatabaseReference dbreference = FirebaseDatabase.getInstance().getReference("bills");
        Query query = dbreference.orderByChild("idBill").equalTo(tableCurrent.getIdBill());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String strNewIdBill = setNewIdBill(tableCurrent.getIdBill(), strNewIdTable);
                        Bill mBill = snapshot.getValue(Bill.class);
                        mBill.setIdBill(strNewIdBill);
                        mBill.setIdTableBill(Integer.parseInt(strNewIdTable));

                        DatabaseReference refBills = FirebaseDatabase.getInstance().getReference("bills");

                        refBills.child(tableCurrent.getIdBill()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                refBills.child(strNewIdBill)
                                        .setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                if (databaseError == null) {
                                                    refBills.child(strNewIdBill).child("idBill").setValue(mBill.getIdBill());
                                                    refBills.child(strNewIdBill).child("idTableBill").setValue(mBill.getIdTableBill());
                                                    refBills.child(strNewIdBill).child("keyTable").setValue(strKeyNewTable);
                                                    refBills.child(tableCurrent.getIdBill()).setValue(null);

                                                    settingOldTable();
                                                    settingNewTable(mBill);
                                                } else {
                                                    Log.e("B", "onComplete: failure:" + databaseError.getMessage() + ": "
                                                            + databaseError.getDetails());
                                                }
                                            }
                                        });
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.e("C", "onCancelled: " + databaseError.getMessage() + ": "
                                        + databaseError.getDetails());
                            }
                        });
                    }
                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), getResources().getString(R.string.loi) + " : " + databaseError, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void settingNewTable(Bill mBill) {
        ref.child("tables").child(strKeyNewTable).child("idBill").setValue(mBill.getIdBill(), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null) {
                    dismiss();
                    Toast.makeText(getContext(), getResources().getString(R.string.sua_thanhcong), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), getResources().getString(R.string.loi) + " : " + error, Toast.LENGTH_SHORT).show();
                }
            }
        });

        ref.child("tables").child(strKeyNewTable).child("statusTable").setValue("ordered", new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null) {
                    Toast.makeText(getContext(), getResources().getString(R.string.doi_ban_thanh_cong), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), getResources().getString(R.string.loi) + " : " + error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void settingOldTable() {
        tableCurrent.setIdBill("");
        tableCurrent.setStatusTable("empty");
        ref.child("tables").child(tableCurrent.getKey()).setValue(tableCurrent, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null) {
                    return;
                } else {
                    Toast.makeText(getContext(), getResources().getString(R.string.loi) + " : " + error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setTextViewNewTable(Table mTable) {
        if (tableCurrent.getIdTable() == mTable.getIdTable()) {
            Toast.makeText(getContext(), getResources().getString(R.string.ban_khong_duoc_trung_nhau), Toast.LENGTH_SHORT).show();
            return;
        } else {
            cardViewNew_changeTable.setVisibility(View.VISIBLE);
            tvIdTableNew_changeTable.setText(String.valueOf(mTable.getIdTable()));
            strKeyNewTable = mTable.getKey();
        }

    }

    private String setNewIdBill(String strOld, String strNew) {
        Calendar calendar = Calendar.getInstance();
        String[] strArr = strOld.split("_");
        String str = strArr[0] + "_" + strNew + "_" + strArr[2];
        return str;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
}