package test1.com.quanlyquanlautrungkhanh.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import de.hdodenhof.circleimageview.CircleImageView;
import test1.com.quanlyquanlautrungkhanh.Model.Food;
import test1.com.quanlyquanlautrungkhanh.Model.User;
import test1.com.quanlyquanlautrungkhanh.R;

public class FoodAdapter extends FirebaseRecyclerAdapter<Food, FoodAdapter.FoodViewHolder> {
    private DecimalFormat formatter;
    private IClickItemFoodAdapter iClickItemFoodAdapter;
    private Context mConText;

    public FoodAdapter(@NonNull FirebaseRecyclerOptions<Food> options, Context mConText, IClickItemFoodAdapter iClickItemFoodAdapter) {
        super(options);
        this.iClickItemFoodAdapter = iClickItemFoodAdapter;
        this.mConText = mConText;
    }

    public interface IClickItemFoodAdapter{
        void clickDeleteFood(Food model, int position);

        void clickChangeImgFood(Food model, int position);

        void clickEditFood(Food model, int position);
    }


    @Override
    protected void onBindViewHolder(@NonNull FoodAdapter.FoodViewHolder holder, int position, @NonNull Food model) {
        Picasso.get().load(model.getImgFood()).error(R.drawable.ic_food_default_64).into(holder.imgAvt_food);

        if (model.getStatusFood() == 1){
            holder.imgStatus_food.setImageResource(R.drawable.ic_status_on_24);
            holder.tvStatus_food.setText(mConText.getResources().getString(R.string.con));
            holder.tvAmount_food.setText(formatter.format(model.getAmountFood()));
            holder.tvName_food.setText(model.getNameFood());
            holder.tvPrice_food.setText(formatter.format(model.getPriceFood()));

            holder.tvStatus_food.setTextColor(ContextCompat.getColor(mConText, R.color.orange));
            holder.tvAmount_food.setTextColor(ContextCompat.getColor(mConText, R.color.orange));
            holder.tvName_food.setTextColor(ContextCompat.getColor(mConText, R.color.orange));
            holder.tvPrice_food.setTextColor(ContextCompat.getColor(mConText, R.color.orange));
            holder.tvNamePrice_food.setTextColor(ContextCompat.getColor(mConText, R.color.orange));
        }else if (model.getStatusFood() == 0 || model.getAmountFood() <= 0){
            holder.imgStatus_food.setImageResource(R.drawable.ic_status_off_24);
            holder.tvStatus_food.setText(mConText.getResources().getString(R.string.het));
            holder.tvAmount_food.setText(formatter.format(model.getAmountFood()));
            holder.tvName_food.setText(model.getNameFood());
            holder.tvPrice_food.setText(formatter.format(model.getPriceFood()));

            holder.tvStatus_food.setTextColor(ContextCompat.getColor(mConText, R.color.grey));
            holder.tvAmount_food.setTextColor(ContextCompat.getColor(mConText, R.color.grey));
            holder.tvName_food.setTextColor(ContextCompat.getColor(mConText, R.color.grey));
            holder.tvPrice_food.setTextColor(ContextCompat.getColor(mConText, R.color.grey));
            holder.tvNamePrice_food.setTextColor(ContextCompat.getColor(mConText, R.color.grey));
        }

        holder.layoutChangeImg_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemFoodAdapter.clickChangeImgFood(model, position);
            }
        });

        holder.layoutDelete_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemFoodAdapter.clickDeleteFood(model, position);
            }
        });

        holder.layoutEdit_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemFoodAdapter.clickEditFood(model, position);
            }
        });

    }

    @NonNull
    @Override
    public FoodAdapter.FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        formatter = new DecimalFormat("###,###,###");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(view);
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout layoutDelete_food, layoutEdit_food, layoutChangeImg_food;
        private TextView tvStatus_food, tvAmount_food, tvName_food, tvPrice_food, tvNamePrice_food;
        private ImageView imgStatus_food, imgAvt_food;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);

            imgStatus_food = itemView.findViewById(R.id.imgStatus_food);
            imgAvt_food = itemView.findViewById(R.id.imgAvt_food);
            tvStatus_food = itemView.findViewById(R.id.tvStatus_food);
            tvAmount_food = itemView.findViewById(R.id.tvAmount_food);
            tvName_food = itemView.findViewById(R.id.tvName_food);
            tvPrice_food = itemView.findViewById(R.id.tvPrice_food);
            tvNamePrice_food = itemView.findViewById(R.id.tvNamePrice_food);
            layoutEdit_food = itemView.findViewById(R.id.layoutEdit_food);
            layoutDelete_food = itemView.findViewById(R.id.layoutDelete_food);
            layoutChangeImg_food = itemView.findViewById(R.id.layoutChangeImg_food);

        }
    }
}

