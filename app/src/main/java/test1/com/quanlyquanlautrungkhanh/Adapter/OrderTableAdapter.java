package test1.com.quanlyquanlautrungkhanh.Adapter;

import android.content.Context;
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

import test1.com.quanlyquanlautrungkhanh.Model.Table;
import test1.com.quanlyquanlautrungkhanh.R;

public class OrderTableAdapter extends FirebaseRecyclerAdapter<Table, OrderTableAdapter.OrderTableViewHoleder> {
    private Context mConText;
    private OrderTableAdapter.IClickOrderTableAdapter iClickOrderTableAdapter;

    public interface IClickOrderTableAdapter{
        void clickOpenPopupMenuOrderTable(Table mTable, int position, View mView);
    }

    public OrderTableAdapter(@NonNull FirebaseRecyclerOptions<Table> options, Context mConText, OrderTableAdapter.IClickOrderTableAdapter iClickOrderTableAdapter) {
        super(options);
        this.iClickOrderTableAdapter = iClickOrderTableAdapter;
        this.mConText = mConText;
    }
    @NonNull
    @Override
    public OrderTableAdapter.OrderTableViewHoleder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_table, parent, false);

        return new OrderTableAdapter.OrderTableViewHoleder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull OrderTableAdapter.OrderTableViewHoleder holder, int position, @NonNull Table model) {
        if (model == null){
            return;
        }

        String statusTable = model.getStatusTable();
        switch (statusTable){
            case "empty":{
                holder.tvStatusOrder_table.setText(mConText.getResources().getString(R.string.dang_trong));
                holder.imgStatusOrder_table.setImageResource(R.drawable.ic_table_off_64);
                holder.tvIdOrder_table.setText(String.valueOf(model.getIdTable()));
                holder.tvTypeOrder_table.setText(String.valueOf(model.getTypeTable()));

                holder.tvStatusOrder_table.setTextColor(ContextCompat.getColor(mConText, R.color.grey));
                holder.tvIdOrder_table.setTextColor(ContextCompat.getColor(mConText, R.color.grey));
                holder.tvTypeOrder_table.setTextColor(ContextCompat.getColor(mConText, R.color.grey));
                holder.tvLoaiBanOrder.setTextColor(ContextCompat.getColor(mConText, R.color.grey));
                break;
            }
            case "repair":{
                holder.tvStatusOrder_table.setText(mConText.getResources().getString(R.string.dang_bao_tri));
                holder.imgStatusOrder_table.setImageResource(R.drawable.ic_table_pause_64);
                holder.tvIdOrder_table.setText(String.valueOf(model.getIdTable()));
                holder.tvTypeOrder_table.setText(String.valueOf(model.getTypeTable()));

                holder.tvStatusOrder_table.setTextColor(ContextCompat.getColor(mConText, R.color.red));
                holder.tvIdOrder_table.setTextColor(ContextCompat.getColor(mConText, R.color.red));
                holder.tvTypeOrder_table.setTextColor(ContextCompat.getColor(mConText, R.color.red));
                holder.tvLoaiBanOrder.setTextColor(ContextCompat.getColor(mConText, R.color.red));
                break;
            }
            case "waiting":{
                holder.tvStatusOrder_table.setText(mConText.getResources().getString(R.string.da_dat_truoc));
                holder.imgStatusOrder_table.setImageResource(R.drawable.ic_table_waiting_64);
                holder.tvIdOrder_table.setText(String.valueOf(model.getIdTable()));
                holder.tvTypeOrder_table.setText(String.valueOf(model.getTypeTable()));

                holder.tvStatusOrder_table.setTextColor(ContextCompat.getColor(mConText, R.color.orange));
                holder.tvIdOrder_table.setTextColor(ContextCompat.getColor(mConText, R.color.orange));
                holder.tvTypeOrder_table.setTextColor(ContextCompat.getColor(mConText, R.color.orange));
                holder.tvLoaiBanOrder.setTextColor(ContextCompat.getColor(mConText, R.color.orange));
                break;
            }
            case "ordered":{
                holder.tvStatusOrder_table.setText(mConText.getResources().getString(R.string.dang_hoat_dong));
                holder.imgStatusOrder_table.setImageResource(R.drawable.ic_table_on_64);
                holder.tvIdOrder_table.setText(String.valueOf(model.getIdTable()));
                holder.tvTypeOrder_table.setText(String.valueOf(model.getTypeTable()));

                holder.tvStatusOrder_table.setTextColor(ContextCompat.getColor(mConText, R.color.neon_green));
                holder.tvIdOrder_table.setTextColor(ContextCompat.getColor(mConText, R.color.neon_green));
                holder.tvTypeOrder_table.setTextColor(ContextCompat.getColor(mConText, R.color.neon_green));
                holder.tvLoaiBanOrder.setTextColor(ContextCompat.getColor(mConText, R.color.neon_green));
                break;
            }

            case "pending":{
                holder.tvStatusOrder_table.setText(mConText.getResources().getString(R.string.dang_cho_thanh_toan));
                holder.imgStatusOrder_table.setImageResource(R.drawable.ic_table_pending_64);
                holder.tvIdOrder_table.setText(String.valueOf(model.getIdTable()));
                holder.tvTypeOrder_table.setText(String.valueOf(model.getTypeTable()));

                holder.tvStatusOrder_table.setTextColor(ContextCompat.getColor(mConText, R.color.yellow));
                holder.tvIdOrder_table.setTextColor(ContextCompat.getColor(mConText, R.color.yellow));
                holder.tvTypeOrder_table.setTextColor(ContextCompat.getColor(mConText, R.color.yellow));
                holder.tvLoaiBanOrder.setTextColor(ContextCompat.getColor(mConText, R.color.yellow));
                break;
            }
            default:{
                holder.tvStatusOrder_table.setText(mConText.getResources().getString(R.string.dang_trong));
                holder.imgStatusOrder_table.setImageResource(R.drawable.ic_table_off_64);
                holder.tvIdOrder_table.setText(String.valueOf(model.getIdTable()));
                holder.tvTypeOrder_table.setText(String.valueOf(model.getTypeTable()));

                holder.tvStatusOrder_table.setTextColor(ContextCompat.getColor(mConText, R.color.grey));
                holder.tvIdOrder_table.setTextColor(ContextCompat.getColor(mConText, R.color.grey));
                holder.tvTypeOrder_table.setTextColor(ContextCompat.getColor(mConText, R.color.grey));
                holder.tvLoaiBanOrder.setTextColor(ContextCompat.getColor(mConText, R.color.grey));
                break;
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickOrderTableAdapter.clickOpenPopupMenuOrderTable(model, position, holder.itemView);
            }
        });
    }

    public class OrderTableViewHoleder extends RecyclerView.ViewHolder{
        private ImageView imgStatusOrder_table;
        private TextView tvStatusOrder_table, tvTypeOrder_table, tvIdOrder_table, tvLoaiBanOrder;

        public OrderTableViewHoleder(@NonNull View itemView) {
            super(itemView);

            imgStatusOrder_table = itemView.findViewById(R.id.imgStatusOrder_table);
            tvStatusOrder_table = itemView.findViewById(R.id.tvStatusOrder_table);
            tvTypeOrder_table = itemView.findViewById(R.id.tvTypeOrder_table);
            tvIdOrder_table = itemView.findViewById(R.id.tvIdOrder_table);
            tvLoaiBanOrder = itemView.findViewById(R.id.tvLoaiBanOrder);
        }
    }
}