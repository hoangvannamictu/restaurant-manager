package test1.com.quanlyquanlautrungkhanh.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.text.DecimalFormat;

import test1.com.quanlyquanlautrungkhanh.Model.Food;
import test1.com.quanlyquanlautrungkhanh.Model.ItemFood;
import test1.com.quanlyquanlautrungkhanh.R;

public class FoodBillAdapter extends FirebaseRecyclerAdapter<ItemFood, FoodBillAdapter.FoodBillViewHolder> {
    private DecimalFormat formatter;
    private FoodBillAdapter.IClickItemFoodBillAdapter iClickItemFoodBillAdapter;
    private Context mConText;


    public FoodBillAdapter(@NonNull FirebaseRecyclerOptions<ItemFood> options, Context mConText, FoodBillAdapter.IClickItemFoodBillAdapter iClickItemFoodBillAdapter) {
        super(options);
        this.iClickItemFoodBillAdapter = iClickItemFoodBillAdapter;
        this.mConText = mConText;
    }

    public interface IClickItemFoodBillAdapter{
        void clickOpenDialogEditFoodBill(ItemFood mItemFood, int position);
    }

    @Override
    protected void onBindViewHolder(@NonNull FoodBillAdapter.FoodBillViewHolder holder, int position, @NonNull ItemFood model) {
        holder.tvName_foodBill.setText(model.getNameFood());
        holder.tvPrice_foodBill.setText(formatter.format(model.getPriceFood()));
        holder.tvAmount_FoodBill.setText(formatter.format(model.getAmountFood()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemFoodBillAdapter.clickOpenDialogEditFoodBill(model, position);
            }
        });
    }

    @NonNull
    @Override
    public FoodBillAdapter.FoodBillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        formatter = new DecimalFormat("###,###,###");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_edit_food_bill, parent, false);
        return new FoodBillAdapter.FoodBillViewHolder(view);
    }

    public class FoodBillViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName_foodBill, tvPrice_foodBill, tvAmount_FoodBill;

        public FoodBillViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName_foodBill = itemView.findViewById(R.id.tvName_foodBill);
            tvPrice_foodBill = itemView.findViewById(R.id.tvPrice_foodBill);
            tvAmount_FoodBill = itemView.findViewById(R.id.tvAmount_FoodBill);
        }
    }
}