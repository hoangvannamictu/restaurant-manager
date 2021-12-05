package test1.com.quanlyquanlautrungkhanh.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

import test1.com.quanlyquanlautrungkhanh.Model.Food;
import test1.com.quanlyquanlautrungkhanh.Model.Table;
import test1.com.quanlyquanlautrungkhanh.R;

public class ChangeTableAdapter extends FirebaseRecyclerAdapter<Table, ChangeTableAdapter.ChangeTableViewHoleder> {
    private Context mConText;
    private ChangeTableAdapter.IClickChangeTableAdapter iClickChangeTableAdapter;

    public interface IClickChangeTableAdapter{
        void clickChooseTable(Table mTable);
    }

    public ChangeTableAdapter(@NonNull FirebaseRecyclerOptions<Table> options, Context mConText, ChangeTableAdapter.IClickChangeTableAdapter iClickChangeTableAdapter) {
        super(options);
        this.iClickChangeTableAdapter = iClickChangeTableAdapter;
        this.mConText = mConText;
    }
    @NonNull
    @Override
    public ChangeTableAdapter.ChangeTableViewHoleder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_table, parent, false);

        return new ChangeTableAdapter.ChangeTableViewHoleder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ChangeTableAdapter.ChangeTableViewHoleder holder, int position, @NonNull Table model) {
        if (model == null || model.getStatusTable().equals("repair") || model.getStatusTable().equals("waiting") || model.getStatusTable().equals("ordered") || model.getStatusTable().equals("pending")){
            return;
        }
        if (model.getStatusTable().equals("empty")){
            holder.tvStatusOrder_table.setText(mConText.getResources().getString(R.string.dang_trong));
            holder.imgStatusOrder_table.setImageResource(R.drawable.ic_table_off_64);
            holder.tvIdOrder_table.setText(String.valueOf(model.getIdTable()));
            holder.tvTypeOrder_table.setText(String.valueOf(model.getTypeTable()));

            holder.tvStatusOrder_table.setTextColor(ContextCompat.getColor(mConText, R.color.grey));
            holder.tvIdOrder_table.setTextColor(ContextCompat.getColor(mConText, R.color.grey));
            holder.tvTypeOrder_table.setTextColor(ContextCompat.getColor(mConText, R.color.grey));
            holder.tvLoaiBanOrder.setTextColor(ContextCompat.getColor(mConText, R.color.grey));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickChangeTableAdapter.clickChooseTable(model);
            }
        });

    }

    public class ChangeTableViewHoleder extends RecyclerView.ViewHolder{
        private ImageView imgStatusOrder_table;
        private TextView tvStatusOrder_table, tvTypeOrder_table, tvIdOrder_table, tvLoaiBanOrder;

        public ChangeTableViewHoleder(@NonNull View itemView) {
            super(itemView);

            imgStatusOrder_table = itemView.findViewById(R.id.imgStatusOrder_table);
            tvStatusOrder_table = itemView.findViewById(R.id.tvStatusOrder_table);
            tvTypeOrder_table = itemView.findViewById(R.id.tvTypeOrder_table);
            tvIdOrder_table = itemView.findViewById(R.id.tvIdOrder_table);
            tvLoaiBanOrder = itemView.findViewById(R.id.tvLoaiBanOrder);
        }
    }
}