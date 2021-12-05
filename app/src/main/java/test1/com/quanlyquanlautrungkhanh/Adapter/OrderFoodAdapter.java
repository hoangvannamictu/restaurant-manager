package test1.com.quanlyquanlautrungkhanh.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import test1.com.quanlyquanlautrungkhanh.Model.Food;
import test1.com.quanlyquanlautrungkhanh.Model.Table;
import test1.com.quanlyquanlautrungkhanh.R;

public class OrderFoodAdapter extends FirebaseRecyclerAdapter<Food, OrderFoodAdapter.OrderFoodViewHolder>{
    private DecimalFormat formatter;
    private OrderFoodAdapter.IClickItemOrderFoodAdapter iClickItemOrderFoodAdapter;
    private Context mConText;


    public OrderFoodAdapter(@NonNull FirebaseRecyclerOptions<Food> options, Context mConText, OrderFoodAdapter.IClickItemOrderFoodAdapter iClickItemOrderFoodAdapter) {
        super(options);
        this.iClickItemOrderFoodAdapter = iClickItemOrderFoodAdapter;
        this.mConText = mConText;
    }

    public interface IClickItemOrderFoodAdapter{
        void clickOpenDialogChooseAmountFood(Food mFood, int position);
    }


    @Override
    protected void onBindViewHolder(@NonNull OrderFoodAdapter.OrderFoodViewHolder holder, int position, @NonNull Food model) {
        Picasso.get().load(model.getImgFood()).error(R.drawable.ic_food_default_64).into(holder.imgAvt_orderFood);

        if (model.getStatusFood() == 0 || model.getAmountFood() <= 0){
            holder.imgStatus_orderFood.setImageResource(R.drawable.ic_status_off_24);
            holder.tvStatus_orderFood.setText(mConText.getResources().getString(R.string.het));
            holder.tvAmount_orderFood.setText(formatter.format(model.getAmountFood()));
            holder.tvName_orderFood.setText(model.getNameFood());
            holder.tvPrice_orderFood.setText(formatter.format(model.getPriceFood()));

            holder.tvStatus_orderFood.setTextColor(ContextCompat.getColor(mConText, R.color.grey));
            holder.tvAmount_orderFood.setTextColor(ContextCompat.getColor(mConText, R.color.grey));
            holder.tvName_orderFood.setTextColor(ContextCompat.getColor(mConText, R.color.grey));
            holder.tvPrice_orderFood.setTextColor(ContextCompat.getColor(mConText, R.color.grey));
            holder.tvNamePrice_orderFood.setTextColor(ContextCompat.getColor(mConText, R.color.grey));
        }else {
            holder.imgStatus_orderFood.setImageResource(R.drawable.ic_status_on_24);
            holder.tvStatus_orderFood.setText(mConText.getResources().getString(R.string.con));
            holder.tvAmount_orderFood.setText(formatter.format(model.getAmountFood()));
            holder.tvName_orderFood.setText(model.getNameFood());
            holder.tvPrice_orderFood.setText(formatter.format(model.getPriceFood()));

            holder.tvStatus_orderFood.setTextColor(ContextCompat.getColor(mConText, R.color.orange));
            holder.tvAmount_orderFood.setTextColor(ContextCompat.getColor(mConText, R.color.orange));
            holder.tvName_orderFood.setTextColor(ContextCompat.getColor(mConText, R.color.orange));
            holder.tvPrice_orderFood.setTextColor(ContextCompat.getColor(mConText, R.color.orange));
            holder.tvNamePrice_orderFood.setTextColor(ContextCompat.getColor(mConText, R.color.orange));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemOrderFoodAdapter.clickOpenDialogChooseAmountFood(model, position);
            }
        });
    }

    @NonNull
    @Override
    public OrderFoodAdapter.OrderFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        formatter = new DecimalFormat("###,###,###");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_food, parent, false);
        return new OrderFoodAdapter.OrderFoodViewHolder(view);
    }

    public class OrderFoodViewHolder extends RecyclerView.ViewHolder {
        private TextView tvStatus_orderFood, tvAmount_orderFood, tvName_orderFood, tvPrice_orderFood, tvNamePrice_orderFood;
        private ImageView imgStatus_orderFood, imgAvt_orderFood;

        public OrderFoodViewHolder(@NonNull View itemView) {
            super(itemView);

            imgStatus_orderFood = itemView.findViewById(R.id.imgStatus_orderFood);
            imgAvt_orderFood = itemView.findViewById(R.id.imgAvt_orderFood);
            tvStatus_orderFood = itemView.findViewById(R.id.tvStatus_orderFood);
            tvAmount_orderFood = itemView.findViewById(R.id.tvAmount_orderFood);
            tvName_orderFood = itemView.findViewById(R.id.tvName_orderFood);
            tvPrice_orderFood = itemView.findViewById(R.id.tvPrice_orderFood);
            tvNamePrice_orderFood = itemView.findViewById(R.id.tvNamePrice_orderFood);

        }
    }
}