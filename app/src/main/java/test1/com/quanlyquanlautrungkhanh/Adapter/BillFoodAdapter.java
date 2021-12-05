package test1.com.quanlyquanlautrungkhanh.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import test1.com.quanlyquanlautrungkhanh.Model.Bill;
import test1.com.quanlyquanlautrungkhanh.Model.ItemFood;
import test1.com.quanlyquanlautrungkhanh.R;

public class BillFoodAdapter extends FirebaseRecyclerAdapter<ItemFood, BillFoodAdapter.BillFoodViewHolder> {
    private DecimalFormat formatter;
    private BillFoodAdapter.IClickItemBillFoodAdapter iClickItemBillFoodAdapter;
    private Context mConText;
    private int intTotalPrice = 0;


    public BillFoodAdapter(@NonNull FirebaseRecyclerOptions<ItemFood> options, Context mConText, BillFoodAdapter.IClickItemBillFoodAdapter iClickItemBillFoodAdapter) {
        super(options);
        this.iClickItemBillFoodAdapter = iClickItemBillFoodAdapter;
        this.mConText = mConText;
    }

    public interface IClickItemBillFoodAdapter {
        void iGetTotalBillFood(int intTotalPrice);

    }

    @Override
    protected void onBindViewHolder(@NonNull BillFoodAdapter.BillFoodViewHolder holder, int position, @NonNull ItemFood model) {
        int totalPriceFood = model.getAmountFood() * model.getPriceFood();
        intTotalPrice += totalPriceFood;
        iClickItemBillFoodAdapter.iGetTotalBillFood(intTotalPrice);

        holder.tvNameFood_detailBill.setText(model.getNameFood());
        holder.tvPriceFood__detailBill.setText(formatter.format(model.getPriceFood()));
        holder.tvAmountFood_detailBill.setText(formatter.format(model.getAmountFood()));
        holder.tvTotalPriceFood_detailBill.setText(formatter.format(totalPriceFood));
    }

    @NonNull
    @Override
    public BillFoodAdapter.BillFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        formatter = new DecimalFormat("###,###,###");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill_food, parent, false);
        return new BillFoodAdapter.BillFoodViewHolder(view);
    }

    public class BillFoodViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNameFood_detailBill, tvPriceFood__detailBill, tvAmountFood_detailBill, tvTotalPriceFood_detailBill;

        public BillFoodViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNameFood_detailBill = itemView.findViewById(R.id.tvNameFood_detailBill);
            tvPriceFood__detailBill = itemView.findViewById(R.id.tvPriceFood__detailBill);
            tvAmountFood_detailBill = itemView.findViewById(R.id.tvAmountFood_detailBill);
            tvTotalPriceFood_detailBill = itemView.findViewById(R.id.tvTotalPriceFood_detailBill);
        }
    }
}