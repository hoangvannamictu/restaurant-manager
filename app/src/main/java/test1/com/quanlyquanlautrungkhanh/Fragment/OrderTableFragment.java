package test1.com.quanlyquanlautrungkhanh.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import test1.com.quanlyquanlautrungkhanh.Adapter.OrderTableAdapter;
import test1.com.quanlyquanlautrungkhanh.Model.Bill;
import test1.com.quanlyquanlautrungkhanh.Model.Table;
import test1.com.quanlyquanlautrungkhanh.R;

public class OrderTableFragment extends Fragment {
    private RecyclerView rcvOrder_table;
    private OrderTableAdapter mOrderTableAdapter;

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;
    private DatabaseReference ref;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_table, container, false);
        anhXa(view);
        readListTableFromFirebase();
        addEvents();
        return view;
    }

    private void anhXa(View view) {
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference();
        rcvOrder_table = view.findViewById(R.id.rcvOrder_table);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext().getApplicationContext(), 2);
        rcvOrder_table.setLayoutManager(gridLayoutManager);
    }

    @Override
    public void onStart() {
        super.onStart();
        mOrderTableAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mOrderTableAdapter.stopListening();
    }

    private void readListTableFromFirebase() {
        FirebaseRecyclerOptions<Table> options = new FirebaseRecyclerOptions.Builder<Table>()
                .setQuery(ref.child("tables"), Table.class)
                .setLifecycleOwner(this)
                .build();

        mOrderTableAdapter = new OrderTableAdapter(options , getContext().getApplicationContext(), new OrderTableAdapter.IClickOrderTableAdapter() {

            @Override
            public void clickOpenPopupMenuOrderTable(Table mTable, int position, View mView) {
               openPopupMenuOrderTable(mTable, position, mView);
            }
        });

        rcvOrder_table.setAdapter(mOrderTableAdapter);
    }

    private void addEvents() {

    }

    private void openPopupMenuOrderTable(Table mTable, int position, View mView) {
        PopupMenu popupMenu = new PopupMenu(getContext().getApplicationContext(), mView);
        popupMenu.getMenuInflater().inflate(R.menu.menu_orders, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_popup_payment: {
                        if (mTable.getStatusTable().equals("ordered")){
                            openDialogConfirmPayment(mTable);
                        }else if (mTable.getStatusTable().equals("pending")){
                            Toast.makeText(getContext(), getResources().getString(R.string.dang_cho_thanh_toan), Toast.LENGTH_SHORT).show();
                            break;
                        } else {
                            Toast.makeText(getContext(), getResources().getString(R.string.ban_chua_dat_ban), Toast.LENGTH_SHORT).show();
                            break;
                        }
                        break;
                    }

                    case R.id.menu_popup_order_table: {
                        if (mTable.getStatusTable().equals("pending")){
                            Toast.makeText(getContext(), getResources().getString(R.string.dang_cho_thanh_toan), Toast.LENGTH_SHORT).show();
                            break;
                        } else if (mTable.getStatusTable().equals("ordered") || mTable.getStatusTable().equals("repair" ) || mTable.getStatusTable().equals("pending")){
                            Toast.makeText(getContext().getApplicationContext(), getResources().getString(R.string.khongthe_datban_do_dangsua_hoac_dang_hoatdong), Toast.LENGTH_SHORT).show();
                            break;
                        }
                        turnOnModeActiveTable(mTable, mView);
                        break;
                    }

                    case R.id.menu_popup_order_food: {
                        if (mTable.getStatusTable().equals("ordered")){
                            openBottomSheetListFood(mTable.getIdBill());
                        }else if (mTable.getStatusTable().equals("pending")){
                            Toast.makeText(getContext(), getResources().getString(R.string.dang_cho_thanh_toan), Toast.LENGTH_SHORT).show();
                            break;
                        } else {
                            Toast.makeText(getContext().getApplicationContext(), getResources().getString(R.string.khongthe_datmon_do_chua_datban), Toast.LENGTH_SHORT).show();
                            break;
                        }
                        break;
                    }

                    case R.id.menu_popup_change_table: {
                        if (mTable.getStatusTable().equals("ordered")){
                            openBottomSheetChangeTable(mTable);
                        }else if (mTable.getStatusTable().equals("pending")){
                            Toast.makeText(getContext(), getResources().getString(R.string.dang_cho_thanh_toan), Toast.LENGTH_SHORT).show();
                            break;
                        }else {
                            Toast.makeText(getContext(), getResources().getString(R.string.ban_chua_dat_ban), Toast.LENGTH_SHORT).show();
                            break;
                        }
                        break;
                    }

                    case R.id.menu_popup_edit_food: {
                        if (mTable.getStatusTable().equals("ordered")){
                            openBottomSheetEditFoodBill(mTable.getIdBill());
                        }else if (mTable.getStatusTable().equals("pending")){
                            Toast.makeText(getContext(), getResources().getString(R.string.dang_cho_thanh_toan), Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getContext(), getResources().getString(R.string.ban_chua_dat_ban), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                }
                return false;
            }
        });
        popupMenu.show();
    }

    private void openDialogConfirmPayment(Table mTable) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext(), R.style.AlertDialog);
        dialog.setTitle(getResources().getString(R.string.thay_doi));
        dialog.setMessage(getResources().getString(R.string.ban_cochac_muon_thaydoi_khong));

        dialog.setPositiveButton(getResources().getString(R.string.co), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                turnOnModePendingTable(mTable);
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

    private void turnOnModePendingTable(Table mTable) {
        ref.child("tables").child(mTable.getKey()).child("statusTable").setValue("pending", new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null) {
                } else {
                    Toast.makeText(getContext(), getResources().getString(R.string.loi) + " : " + error, Toast.LENGTH_SHORT).show();
                }
            }
        });

        ref.child("bills").child(mTable.getIdBill()).child("statusBill").setValue("pending", new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null) {
                    Toast.makeText(getContext(), getResources().getString(R.string.da_chuyen_sang_cho_thanh_toan), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), getResources().getString(R.string.loi) + " : " + error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openBottomSheetChangeTable(Table mTable) {
        BottomSheetChangeTable mBottomSheetChangeTable = new BottomSheetChangeTable();
        Bundle mBundle = new Bundle();
        mBundle.putSerializable("my_table_model", mTable);

        mBottomSheetChangeTable.setArguments(mBundle);
        mBottomSheetChangeTable.show(getChildFragmentManager(), mBottomSheetChangeTable.getTag());
    }

    private void openBottomSheetEditFoodBill(String idBill) {
        BottomSheetEditFoodBill mBottomSheetEditFoodBill = new BottomSheetEditFoodBill();
        Bundle mBundle = new Bundle();
        mBundle.putString("my_id_bill_2", idBill);

        mBottomSheetEditFoodBill.setArguments(mBundle);
        mBottomSheetEditFoodBill.show(getChildFragmentManager(), mBottomSheetEditFoodBill.getTag());
    }

    private void openBottomSheetListFood(String idBill) {
        BottomSheetListFood mBottomSheetListFoodFragment = new BottomSheetListFood();
        Bundle mBundle = new Bundle();
        mBundle.putString("my_id_bill", idBill);

        mBottomSheetListFoodFragment.setArguments(mBundle);
        mBottomSheetListFoodFragment.show(getChildFragmentManager(), mBottomSheetListFoodFragment.getTag());
    }

    private Bill createBill(Table mTable) {
        Calendar calendar = Calendar.getInstance();
        String strIdBill = "bill_"+mTable.getIdTable()+"_"+calendar.getTimeInMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        String strDateCreatedBill = sdf.format(calendar.getTime());

        Bill mBill = new Bill(strIdBill, mTable.getKey(), mTable.getIdTable(), strDateCreatedBill, "unfinished");

        return mBill;
    }

    private void turnOnModeActiveTable(Table mTable, View mView) {
        Bill mBill = createBill(mTable);

        mTable.setIdBill(mBill.getIdBill());
        mTable.setStatusTable("ordered");

        ref.child("tables").child(mTable.getKey()).setValue(mTable, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null){
                    addBillOnFirebaseDatabase(mBill);
                    Toast.makeText(getContext().getApplicationContext(), getResources().getString(R.string.dat_ban_thanh_cong), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext().getApplicationContext(), getResources().getString(R.string.loi)+" : "+error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addBillOnFirebaseDatabase(Bill mBill) {
        ref.child("bills").child(mBill.getIdBill()).setValue(mBill, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null){
                    Toast.makeText(getContext().getApplicationContext(), getResources().getString(R.string.tao_hoa_don_thanh_cong), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext().getApplicationContext(), getResources().getString(R.string.loi)+" : "+error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
