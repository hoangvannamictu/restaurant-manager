package test1.com.quanlyquanlautrungkhanh.Fragment;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import test1.com.quanlyquanlautrungkhanh.Adapter.OrderFoodAdapter;
import test1.com.quanlyquanlautrungkhanh.Model.Food;
import test1.com.quanlyquanlautrungkhanh.Model.ItemFood;
import test1.com.quanlyquanlautrungkhanh.R;

public class BottomSheetListFood extends BottomSheetDialogFragment {
    private static final String KEY_GET_ID_BILL = "my_id_bill";
    private OrderFoodAdapter mOrderFoodAdapter;
    private String strIdBill;

    private BottomSheetBehavior mBehavior;

    private ChipGroup chipGroup_orderFood;
    private android.widget.SearchView searchView_orderFood;
    private RecyclerView rcv_orderFood;

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

        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_bottom_sheet_list_food, null);

        LinearLayout linearLayout = view.findViewById(R.id.root);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
        params.height = getScreenHeight();
        linearLayout.setLayoutParams(params);


        bottomSheetDialog.setContentView(view);
        mBehavior = BottomSheetBehavior.from((View) view.getParent());

        anhXa(view);
        readListFoodFromFirebase("");
        searchViewQueryTextListener();
        addEvents();
        return bottomSheetDialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        mOrderFoodAdapter.startListening();
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void onStop() {
        super.onStop();
        mOrderFoodAdapter.stopListening();
    }

    private void anhXa(View view){
        rcv_orderFood = view.findViewById(R.id.rcv_orderFood);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext().getApplicationContext(), 2);
        rcv_orderFood.setLayoutManager(gridLayoutManager);

        chipGroup_orderFood = view.findViewById(R.id.chipGroup_orderFood);
        searchView_orderFood = view.findViewById(R.id.searchView_orderFood);

    }

    private void addEvents() {
        chipGroup_orderFood.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.chipFood:{
                        readListTagFoodFromFirebase(getResources().getString(R.string.tag_do_an));
                        break;
                    }

                    case R.id.chipDrink:{
                        readListTagFoodFromFirebase(getResources().getString(R.string.tag_do_uong));
                        break;
                    }

                    case R.id.chipFruit:{
                        readListTagFoodFromFirebase(getResources().getString(R.string.tag_hoa_qua));
                        break;
                    }
                    default:{
                        readListFoodFromFirebase("");
                        break;
                    }
                }
            }
        });
    }

    private void readListFoodFromFirebase(String s) {
        Query query = ref.child("foods").orderByChild("nameFood").startAt(s).endAt(s+"\uf8ff");
        FirebaseRecyclerOptions<Food> options = new FirebaseRecyclerOptions.Builder<Food>()
                .setQuery(query, Food.class)
                .setLifecycleOwner(this)
                .build();

        mOrderFoodAdapter = new OrderFoodAdapter(options, getContext().getApplicationContext(), new OrderFoodAdapter.IClickItemOrderFoodAdapter() {
            @Override
            public void clickOpenDialogChooseAmountFood(Food mFood, int position) {
                if (mFood.getStatusFood() == 0 || mFood.getAmountFood() <= 0){
                    Toast.makeText(getContext(), getResources().getString(R.string.het), Toast.LENGTH_SHORT).show();
                }else {
                    openDialogChooseAmountFood(mFood, position);
                }
            }
        });

        rcv_orderFood.setAdapter(mOrderFoodAdapter);
    }

    private void readListTagFoodFromFirebase(String s) {
        Query query = ref.child("foods").orderByChild("tagFood").startAt(s).endAt(s+"\uf8ff");
        FirebaseRecyclerOptions<Food> tagOptions = new FirebaseRecyclerOptions.Builder<Food>()
                .setQuery(query, Food.class)
                .setLifecycleOwner(this)
                .build();

        mOrderFoodAdapter = new OrderFoodAdapter(tagOptions, getContext().getApplicationContext(), new OrderFoodAdapter.IClickItemOrderFoodAdapter() {
            @Override
            public void clickOpenDialogChooseAmountFood(Food mFood, int position) {
                openDialogChooseAmountFood(mFood, position);
            }
        });

        rcv_orderFood.setAdapter(mOrderFoodAdapter);
    }

    private void openDialogChooseAmountFood(Food mFood, int position) {
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

        tvDialogAmount_orderFood.setText(String.valueOf(mFood.getAmountFood()));
        edtChoose_AmountFood.setText("0");

        btnPrevious_amountFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int amountFood = Integer.valueOf(edtChoose_AmountFood.getText().toString().trim());

                if (amountFood <= 0){
                    Toast.makeText(getContext(), getResources().getString(R.string.khongduoc_nhohon_hoac_bang_0), Toast.LENGTH_SHORT).show();
                }else {
                    amountFood--;
                    edtChoose_AmountFood.setText(String.valueOf(amountFood));
                }
            }
        });

        btnNext_amountFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int amountFood = Integer.valueOf(edtChoose_AmountFood.getText().toString().trim());
                if (amountFood >= mFood.getAmountFood()){
                    Toast.makeText(getContext(), getResources().getString(R.string.khongduoc_lonhon_soluong_dangco), Toast.LENGTH_SHORT).show();
                }else {
                    amountFood++;
                    edtChoose_AmountFood.setText(String.valueOf(amountFood));
                }

            }
        });

        btnAdd_amountFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = Integer.valueOf(edtChoose_AmountFood.getText().toString().trim());
                if (number <= 0){
                    Toast.makeText(getContext(), getResources().getString(R.string.khongduoc_nhohon_hoac_bang_0), Toast.LENGTH_SHORT).show();
                }else if (number > mFood.getAmountFood()){
                    Toast.makeText(getContext(), getResources().getString(R.string.khongduoc_lonhon_soluong_dangco), Toast.LENGTH_SHORT).show();
                }else {
                    String idItemFood = "bill_"+mFood.getIdFood();
                    ItemFood itemFood = new ItemFood(idItemFood, mFood.getNameFood(), number, mFood.getPriceFood(), mFood.getTagFood());

                    int amountFood = mFood.getAmountFood() - number;
                    mFood.setAmountFood(amountFood);
                    ref.child("foods").child(mFood.getIdFood()).child("amountFood").setValue(mFood.getAmountFood(), new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            if (error == null){
                                addAmountFoodOnFirebaseDatabase(itemFood);
                                dialog.dismiss();
                            }else {
                                Toast.makeText(getContext(), getResources().getString(R.string.loi)+" : "+error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
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

    private void addAmountFoodOnFirebaseDatabase(ItemFood itemFood) {
        DatabaseReference dbreference = FirebaseDatabase.getInstance().getReference("bills").child(strIdBill).child("list_ordered_food");
        Query query = dbreference.orderByKey().equalTo(itemFood.getIdFood());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        int amount = (int) snapshot.child("amountFood").getValue(Integer.class);
                        int amountFood = amount + itemFood.getAmountFood();

                        ref.child("bills").child(strIdBill).child("list_ordered_food").child(itemFood.getIdFood()).child("amountFood").setValue(amountFood, new DatabaseReference.CompletionListener() {
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
                }else{
                    ref.child("bills").child(strIdBill).child("list_ordered_food").child(itemFood.getIdFood()).setValue(itemFood, new DatabaseReference.CompletionListener() {
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

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), getResources().getString(R.string.loi)+" : "+databaseError, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchViewQueryTextListener() {
        searchView_orderFood.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                readListFoodFromFirebase(newText);
                return false;
            }
        });
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
}