package test1.com.quanlyquanlautrungkhanh.Fragment;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import test1.com.quanlyquanlautrungkhanh.Adapter.FoodBillAdapter;
import test1.com.quanlyquanlautrungkhanh.Adapter.OrderFoodAdapter;
import test1.com.quanlyquanlautrungkhanh.Model.Food;
import test1.com.quanlyquanlautrungkhanh.Model.ItemFood;
import test1.com.quanlyquanlautrungkhanh.R;

public class BottomSheetEditFoodBill extends BottomSheetDialogFragment {
    private static final String KEY_GET_ID_BILL = "my_id_bill_2";
    private FoodBillAdapter mFoodBillAdapter;
    private String strIdBill;

    private BottomSheetBehavior mBehavior;

    private ChipGroup chipGroup_foodBill;
    private android.widget.SearchView searchView_foodBill;
    private RecyclerView rcv_foodBill;

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;
    private DatabaseReference ref;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference();

        Bundle bundleReceive = getArguments();
        if (bundleReceive != null){
            strIdBill = bundleReceive.getString(KEY_GET_ID_BILL);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_bottom_sheet_edit_food_bill, null);

        LinearLayout linearLayout = view.findViewById(R.id.root_foodBill);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
        params.height = getScreenHeight();
        linearLayout.setLayoutParams(params);


        bottomSheetDialog.setContentView(view);
        mBehavior = BottomSheetBehavior.from((View) view.getParent());

        anhXa(view);
        readListFoodBillFromFirebase("");
        searchViewQueryTextListener();
        addEvents();
        return bottomSheetDialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        mFoodBillAdapter.startListening();
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void onStop() {
        super.onStop();
        mFoodBillAdapter.stopListening();
    }

    private void anhXa(View view){
        rcv_foodBill = view.findViewById(R.id.rcv_foodBill);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext().getApplicationContext(), 1);
        rcv_foodBill.setLayoutManager(gridLayoutManager);

        chipGroup_foodBill = view.findViewById(R.id.chipGroup_foodBill);
        searchView_foodBill = view.findViewById(R.id.searchView_foodBill);

    }

    private void addEvents() {
        chipGroup_foodBill.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.chipFood_foodBill:{
                        readListTagFoodBillFromFirebase(getResources().getString(R.string.tag_do_an));
                        break;
                    }

                    case R.id.chipDrink_foodBill:{
                        readListTagFoodBillFromFirebase(getResources().getString(R.string.tag_do_uong));
                        break;
                    }

                    case R.id.chipFruit_foodBill:{
                        readListTagFoodBillFromFirebase(getResources().getString(R.string.tag_hoa_qua));
                        break;
                    }
                    default:{
                        readListFoodBillFromFirebase("");
                        break;
                    }
                }
            }
        });
    }

    private void readListFoodBillFromFirebase(String s) {
        Query query = ref.child("bills").child(strIdBill).child("list_ordered_food").orderByChild("nameFood").startAt(s).endAt(s+"\uf8ff");
        FirebaseRecyclerOptions<ItemFood> options = new FirebaseRecyclerOptions.Builder<ItemFood>()
                .setQuery(query, ItemFood.class)
                .setLifecycleOwner(this)
                .build();

        mFoodBillAdapter = new FoodBillAdapter(options, getContext().getApplicationContext(), new FoodBillAdapter.IClickItemFoodBillAdapter() {
            @Override
            public void clickOpenDialogEditFoodBill(ItemFood mItemFood, int position) {
                openDialogEditFoodBill(mItemFood);
            }
        });

        rcv_foodBill.setAdapter(mFoodBillAdapter);
    }

    private void readListTagFoodBillFromFirebase(String s) {
        Query query = ref.child("bills").child(strIdBill).child("list_ordered_food").orderByChild("tagFood").startAt(s).endAt(s+"\uf8ff");
        FirebaseRecyclerOptions<ItemFood> tagOptions = new FirebaseRecyclerOptions.Builder<ItemFood>()
                .setQuery(query, ItemFood.class)
                .setLifecycleOwner(this)
                .build();

        mFoodBillAdapter = new FoodBillAdapter(tagOptions, getContext().getApplicationContext(), new FoodBillAdapter.IClickItemFoodBillAdapter() {
            @Override
            public void clickOpenDialogEditFoodBill(ItemFood mItemFood, int position) {
                openDialogEditFoodBill(mItemFood);
            }
        });

        rcv_foodBill.setAdapter(mFoodBillAdapter);
    }

    private void openDialogEditFoodBill(ItemFood mItemFood) {
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_choose_amount_food);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        ImageButton btnPrevious_amountFood =dialog.findViewById(R.id.btnPrevious_amountFood);
        ImageButton btnNext_amountFood =dialog.findViewById(R.id.btnNext_amountFood);
        Button btnAdd_amountFood =dialog.findViewById(R.id.btnAdd_amountFood);
        Button btnQuitDiaLog_amountFood =dialog.findViewById(R.id.btnQuitDiaLog_amountFood);
        EditText edtChoose_AmountFood = dialog.findViewById(R.id.edtChoose_AmountFood);
        TextView tvDialogAmount_orderFood = dialog.findViewById(R.id.tvDialogAmount_orderFood);
        TextView tvAmountTitle_orderfood = dialog.findViewById(R.id.tvAmountTitle_orderfood);
        TextView titleDialog_orderFood = dialog.findViewById(R.id.titleDialog_orderFood);

        titleDialog_orderFood.setText(getResources().getString(R.string.sua_so_luong));
        btnAdd_amountFood.setText(getResources().getString(R.string.thay_doi));
        tvAmountTitle_orderfood.setVisibility(View.INVISIBLE);
        tvDialogAmount_orderFood.setVisibility(View.INVISIBLE);
        btnNext_amountFood.setVisibility(View.INVISIBLE);
        edtChoose_AmountFood.setText(String.valueOf(mItemFood.getAmountFood()));

        btnPrevious_amountFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int amountFood = Integer.valueOf(edtChoose_AmountFood.getText().toString().trim());

                if (amountFood <= 0){
                    Toast.makeText(getContext(), getResources().getString(R.string.khongduoc_nhohon_hoac_bang_0), Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    amountFood--;
                    edtChoose_AmountFood.setText(String.valueOf(amountFood));
                }
            }
        });

        btnAdd_amountFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int amountFood = Integer.valueOf(edtChoose_AmountFood.getText().toString().trim());

                if (amountFood < 0){
                    Toast.makeText(getContext(), getResources().getString(R.string.khongduoc_nhohon_0), Toast.LENGTH_SHORT).show();
                }else if (amountFood > mItemFood.getAmountFood()){
                    Toast.makeText(getContext(), getResources().getString(R.string.khongduoc_lonhon_soluong_dangco), Toast.LENGTH_SHORT).show();
                }else {
                    mItemFood.setAmountFood(amountFood);
                    editFoodBillOnFirebaseDatabase(mItemFood);
                    dialog.dismiss();
                }

            }
        });

        btnQuitDiaLog_amountFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void editFoodBillOnFirebaseDatabase(ItemFood mItemFood) {
        DatabaseReference dbreference = FirebaseDatabase.getInstance().getReference("bills").child(strIdBill).child("list_ordered_food");
        Query query = dbreference.orderByKey().equalTo(mItemFood.getIdFood());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        ref.child("bills").child(strIdBill).child("list_ordered_food").child(mItemFood.getIdFood()).child("amountFood").setValue(mItemFood.getAmountFood(), new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                if (error == null){
                                    Toast.makeText(getContext(), getResources().getString(R.string.them_mon_thanh_cong), Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(getContext(), getResources().getString(R.string.loi)+" : "+error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), getResources().getString(R.string.loi)+" : "+databaseError, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchViewQueryTextListener() {
        searchView_foodBill.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                readListFoodBillFromFirebase(newText);
                return false;
            }
        });
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
}