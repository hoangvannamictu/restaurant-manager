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

import test1.com.quanlyquanlautrungkhanh.Model.Table;
import test1.com.quanlyquanlautrungkhanh.R;

public class TableAdapter extends FirebaseRecyclerAdapter<Table, TableAdapter.TableViewHoleder> {
    private Context mConText;
    private IClickTableAdapter iClickTableAdapter;

    public interface IClickTableAdapter{
        void clickDeleteTable(Table model, int position);

        void clickEditTable(Table model, int position);

    }

    public TableAdapter(@NonNull FirebaseRecyclerOptions<Table> options, Context mConText, IClickTableAdapter iClickTableAdapter) {
        super(options);
        this.iClickTableAdapter = iClickTableAdapter;
        this.mConText = mConText;
    }
    @NonNull
    @Override
    public TableAdapter.TableViewHoleder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_table, parent, false);

        return new TableAdapter.TableViewHoleder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull TableViewHoleder holder, int position, @NonNull Table model) {
        if (model == null){
            return;
        }

        String statusTable = model.getStatusTable();
        switch (statusTable){
            case "empty":{
                holder.tvStatus_table.setText(mConText.getResources().getString(R.string.dang_trong));
                holder.imgStatus_table.setImageResource(R.drawable.ic_table_off_64);
                holder.tvId_table.setText(String.valueOf(model.getIdTable()));
                holder.tvType_table.setText(String.valueOf(model.getTypeTable()));

                holder.tvStatus_table.setTextColor(ContextCompat.getColor(mConText, R.color.grey));
                holder.tvId_table.setTextColor(ContextCompat.getColor(mConText, R.color.grey));
                holder.tvType_table.setTextColor(ContextCompat.getColor(mConText, R.color.grey));
                holder.tvLoaiBan.setTextColor(ContextCompat.getColor(mConText, R.color.grey));
                break;
            }
            case "repair":{
                holder.tvStatus_table.setText(mConText.getResources().getString(R.string.dang_bao_tri));
                holder.imgStatus_table.setImageResource(R.drawable.ic_table_pause_64);
                holder.tvId_table.setText(String.valueOf(model.getIdTable()));
                holder.tvType_table.setText(String.valueOf(model.getTypeTable()));

                holder.tvStatus_table.setTextColor(ContextCompat.getColor(mConText, R.color.red));
                holder.tvId_table.setTextColor(ContextCompat.getColor(mConText, R.color.red));
                holder.tvType_table.setTextColor(ContextCompat.getColor(mConText, R.color.red));
                holder.tvLoaiBan.setTextColor(ContextCompat.getColor(mConText, R.color.red));
                break;
            }
            case "waiting":{
                holder.tvStatus_table.setText(mConText.getResources().getString(R.string.da_dat_truoc));
                holder.imgStatus_table.setImageResource(R.drawable.ic_table_waiting_64);
                holder.tvId_table.setText(String.valueOf(model.getIdTable()));
                holder.tvType_table.setText(String.valueOf(model.getTypeTable()));

                holder.tvStatus_table.setTextColor(ContextCompat.getColor(mConText, R.color.orange));
                holder.tvId_table.setTextColor(ContextCompat.getColor(mConText, R.color.orange));
                holder.tvType_table.setTextColor(ContextCompat.getColor(mConText, R.color.orange));
                holder.tvLoaiBan.setTextColor(ContextCompat.getColor(mConText, R.color.orange));
                break;
            }
            case "ordered":{
                holder.tvStatus_table.setText(mConText.getResources().getString(R.string.dang_hoat_dong));
                holder.imgStatus_table.setImageResource(R.drawable.ic_table_on_64);
                holder.tvId_table.setText(String.valueOf(model.getIdTable()));
                holder.tvType_table.setText(String.valueOf(model.getTypeTable()));

                holder.tvStatus_table.setTextColor(ContextCompat.getColor(mConText, R.color.neon_green));
                holder.tvId_table.setTextColor(ContextCompat.getColor(mConText, R.color.neon_green));
                holder.tvType_table.setTextColor(ContextCompat.getColor(mConText, R.color.neon_green));
                holder.tvLoaiBan.setTextColor(ContextCompat.getColor(mConText, R.color.neon_green));
                break;
            }
            case "pending":{
                holder.tvStatus_table.setText(mConText.getResources().getString(R.string.dang_cho_thanh_toan));
                holder.imgStatus_table.setImageResource(R.drawable.ic_table_pending_64);
                holder.tvId_table.setText(String.valueOf(model.getIdTable()));
                holder.tvType_table.setText(String.valueOf(model.getTypeTable()));

                holder.tvStatus_table.setTextColor(ContextCompat.getColor(mConText, R.color.yellow));
                holder.tvId_table.setTextColor(ContextCompat.getColor(mConText, R.color.yellow));
                holder.tvType_table.setTextColor(ContextCompat.getColor(mConText, R.color.yellow));
                holder.tvLoaiBan.setTextColor(ContextCompat.getColor(mConText, R.color.yellow));
                break;
            }
            default:{
                holder.tvStatus_table.setText(mConText.getResources().getString(R.string.dang_trong));
                holder.imgStatus_table.setImageResource(R.drawable.ic_table_off_64);
                holder.tvId_table.setText(String.valueOf(model.getIdTable()));
                holder.tvType_table.setText(String.valueOf(model.getTypeTable()));

                holder.tvStatus_table.setTextColor(ContextCompat.getColor(mConText, R.color.grey));
                holder.tvId_table.setTextColor(ContextCompat.getColor(mConText, R.color.grey));
                holder.tvType_table.setTextColor(ContextCompat.getColor(mConText, R.color.grey));
                holder.tvLoaiBan.setTextColor(ContextCompat.getColor(mConText, R.color.grey));
                break;
            }
        }

        holder.layoutEdit_table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickTableAdapter.clickEditTable(model, position);
            }
        });

        holder.layoutDelete_table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickTableAdapter.clickDeleteTable(model, position);
            }
        });

    }

    public class TableViewHoleder extends RecyclerView.ViewHolder{
        private LinearLayout layoutEdit_table, layoutDelete_table;
        private ImageView imgStatus_table;
        private TextView tvStatus_table, tvType_table, tvId_table, tvLoaiBan;

        public TableViewHoleder(@NonNull View itemView) {
            super(itemView);

            layoutEdit_table = itemView.findViewById(R.id.layoutEdit_table);
            layoutDelete_table = itemView.findViewById(R.id.layoutDelete_table);
            imgStatus_table = itemView.findViewById(R.id.imgStatus_table);
            tvStatus_table = itemView.findViewById(R.id.tvStatus_table);
            tvType_table = itemView.findViewById(R.id.tvType_table);
            tvId_table = itemView.findViewById(R.id.tvId_table);
            tvLoaiBan = itemView.findViewById(R.id.tvLoaiBan);
        }
    }
}
