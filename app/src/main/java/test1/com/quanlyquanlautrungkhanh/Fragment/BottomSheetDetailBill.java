package test1.com.quanlyquanlautrungkhanh.Fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;

import test1.com.quanlyquanlautrungkhanh.Adapter.BillAdapter;
import test1.com.quanlyquanlautrungkhanh.Adapter.BillFoodAdapter;
import test1.com.quanlyquanlautrungkhanh.Adapter.FoodBillAdapter;
import test1.com.quanlyquanlautrungkhanh.Model.Bill;
import test1.com.quanlyquanlautrungkhanh.Model.ItemFood;
import test1.com.quanlyquanlautrungkhanh.R;

public class BottomSheetDetailBill extends BottomSheetDialogFragment {
    private static final String KEY_GET_BILL_MODEL = "my_bill_model";
    private BillFoodAdapter mBillFoodAdapter;
    private Bill mBill;

    private DecimalFormat formatter;
    private Button btnQuit_detailBill, btnPayment__detailBill;
    private RecyclerView rcv_detailBill;
    private TextView tvTotalPrice_detailBill, tvId_detailBill, tvIdTable_detailBill, tvDateCreated_detailBill, tvStatus_detailBill;

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;
    private DatabaseReference ref;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference();
        formatter = new DecimalFormat("###,###,###");

        Bundle bundleReceive = getArguments();
        if (bundleReceive != null) {
            mBill = (Bill) bundleReceive.getSerializable(KEY_GET_BILL_MODEL);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_bottom_sheet_detail_bill, null);

        bottomSheetDialog.setContentView(view);

        anhXa(view);
        readListTableFromFirebase();
        setTextBill();
        addEvents();
        return bottomSheetDialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        mBillFoodAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mBillFoodAdapter.stopListening();
    }

    private void anhXa(View view) {
        rcv_detailBill = view.findViewById(R.id.rcv_detailBill);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext().getApplicationContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rcv_detailBill.setLayoutManager(linearLayoutManager);

        btnQuit_detailBill = view.findViewById(R.id.btnQuit_detailBill);
        btnPayment__detailBill = view.findViewById(R.id.btnPayment__detailBill);
        tvId_detailBill = view.findViewById(R.id.tvId_detailBill);
        tvIdTable_detailBill = view.findViewById(R.id.tvIdTable_detailBill);
        tvDateCreated_detailBill = view.findViewById(R.id.tvDateCreated_detailBill);
        tvStatus_detailBill = view.findViewById(R.id.tvStatus_detailBill);
        tvTotalPrice_detailBill = view.findViewById(R.id.tvTotalPrice_detailBill);
    }

    private void readListTableFromFirebase() {
        FirebaseRecyclerOptions<ItemFood> options = new FirebaseRecyclerOptions.Builder<ItemFood>()
                .setQuery(ref.child("bills").child(mBill.getIdBill()).child("list_ordered_food"), ItemFood.class)
                .setLifecycleOwner(this)
                .build();

        mBillFoodAdapter = new BillFoodAdapter(options, getContext().getApplicationContext(), new BillFoodAdapter.IClickItemBillFoodAdapter() {
            @Override
            public void iGetTotalBillFood(int intTotalPrice) {
                tvTotalPrice_detailBill.setText(formatter.format(intTotalPrice));
            }
        });

        rcv_detailBill.setAdapter(mBillFoodAdapter);
    }

    private void setTextBill() {
        tvId_detailBill.setText(mBill.getIdBill());
        tvIdTable_detailBill.setText(String.valueOf(mBill.getIdTableBill()));
        tvDateCreated_detailBill.setText(mBill.getDateCreatedBill());
        tvStatus_detailBill.setText(mBill.getStatusBill());
    }

    private void addEvents() {
        btnPayment__detailBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strTotalPrice = tvTotalPrice_detailBill.getText().toString();

                if (strTotalPrice.isEmpty()){
                    setTotalPriceOnFirebase(0);
                }else {
                    int totalPrice = Integer.parseInt(strTotalPrice.replace(".", ""));
                    setTotalPriceOnFirebase(totalPrice);
                }

                addDateFinishBill();
                changeStatusBill();
            }
        });

        btnQuit_detailBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void setTotalPriceOnFirebase(int totalPrice) {
        ref.child("bills").child(mBill.getIdBill()).child("totalpriceBill").setValue(totalPrice);
    }

    private void addDateFinishBill() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        String strDateFinishdBill = sdf.format(calendar.getTime());
        ref.child("bills").child(mBill.getIdBill()).child("datefinishBill").setValue(strDateFinishdBill, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
            }
        });
    }

    private void changeStatusBill() {
        ref.child("bills").child(mBill.getIdBill()).child("statusBill").setValue("finished", new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null){
                    moveBillToNewNode();
                    setDefaultIdBillTable();
                    setDefaultStatusTable();
                }else {
                    Toast.makeText(getContext(), getResources().getString(R.string.loi)+" : "+error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void moveBillToNewNode() {
        DatabaseReference refOld = FirebaseDatabase.getInstance().getReference("bills");
        DatabaseReference refNew = FirebaseDatabase.getInstance().getReference("completedBills");
        refOld.child(mBill.getIdBill()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                refNew.child(mBill.getIdBill()).setValue(snapshot.getValue() , new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        if (error == null){
                            refOld.child(mBill.getIdBill()).removeValue();
                            dismiss();
                        }else {
                            Toast.makeText(getContext(), getResources().getString(R.string.loi)+" : "+error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), getResources().getString(R.string.loi)+" : "+error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setDefaultStatusTable() {
        ref.child("tables").child(mBill.getKeyTable()).child("statusTable").setValue("empty", new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error != null){
                    Toast.makeText(getContext(), getResources().getString(R.string.loi)+" : "+error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setDefaultIdBillTable() {
        ref.child("tables").child(mBill.getKeyTable()).child("idBill").setValue("", new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error != null){
                    Toast.makeText(getContext(), getResources().getString(R.string.loi)+" : "+error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
