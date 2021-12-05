package test1.com.quanlyquanlautrungkhanh.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import test1.com.quanlyquanlautrungkhanh.Adapter.BillAdapter;
import test1.com.quanlyquanlautrungkhanh.Adapter.OrderFoodAdapter;
import test1.com.quanlyquanlautrungkhanh.Adapter.OrderTableAdapter;
import test1.com.quanlyquanlautrungkhanh.Model.Bill;
import test1.com.quanlyquanlautrungkhanh.Model.Food;
import test1.com.quanlyquanlautrungkhanh.R;

public class PaymentFragment extends Fragment {
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;
    private DatabaseReference ref;

    private ChipGroup chipGroup_bill;
    private RecyclerView rcv_bill;
    private BillAdapter mBillAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment, container, false);
        anhXa(view);
        readListBillFromFirebase("");
        addEvents();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mBillAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mBillAdapter.stopListening();
    }

    private void anhXa(View view) {
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference();
        rcv_bill = view.findViewById(R.id.rcv_bill);
        chipGroup_bill = view.findViewById(R.id.chipGroup_bill);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext().getApplicationContext(), 2);
        rcv_bill.setLayoutManager(gridLayoutManager);
    }

    private void readListBillFromFirebase(String s) {
        Query query = ref.child("bills").orderByChild("statusBill").startAt(s).endAt(s+"\uf8ff");
        FirebaseRecyclerOptions<Bill> options = new FirebaseRecyclerOptions.Builder<Bill>()
                .setQuery(query, Bill.class)
                .setLifecycleOwner(this)
                .build();

        mBillAdapter = new BillAdapter(options, getContext().getApplicationContext(), new BillAdapter.IClickBillAdapter() {
            @Override
            public void clickOpenBottomSheetDetailBill(Bill mBill) {
                openBottomSheetDetailBill(mBill);
            }
        });

        rcv_bill.setAdapter(mBillAdapter);
    }

    private void openBottomSheetDetailBill(Bill mBill) {
        BottomSheetDetailBill mBottomSheetDetailBill = new BottomSheetDetailBill();
        Bundle mBundle = new Bundle();
        mBundle.putSerializable("my_bill_model", mBill);

        mBottomSheetDetailBill.setArguments(mBundle);
        mBottomSheetDetailBill.show(getChildFragmentManager(), mBottomSheetDetailBill.getTag());
    }

    private void addEvents() {
        chipGroup_bill.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.chipUnfinished_bill:{
                        readListBillFromFirebase(getResources().getString(R.string.trang_thai_chua_hoan_thanh));
                        break;
                    }

                    case R.id.chipPending_bill:{
                        readListBillFromFirebase(getResources().getString(R.string.trang_thai_cho_xy_ly));
                        break;
                    }
                    default:{
                        readListBillFromFirebase("");
                        break;
                    }
                }
            }
        });
    }
}
