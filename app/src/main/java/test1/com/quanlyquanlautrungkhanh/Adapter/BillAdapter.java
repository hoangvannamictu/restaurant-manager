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

import test1.com.quanlyquanlautrungkhanh.Model.Bill;
import test1.com.quanlyquanlautrungkhanh.Model.Table;
import test1.com.quanlyquanlautrungkhanh.R;

public class BillAdapter extends FirebaseRecyclerAdapter<Bill, BillAdapter.BillViewHoleder> {
    private Context mConText;
    private BillAdapter.IClickBillAdapter iClickBillAdapter;

    public interface IClickBillAdapter{
        void clickOpenBottomSheetDetailBill(Bill mBill);
    }

    public BillAdapter(@NonNull FirebaseRecyclerOptions<Bill> options, Context mConText, BillAdapter.IClickBillAdapter iClickBillAdapter) {
        super(options);
        this.iClickBillAdapter = iClickBillAdapter;
        this.mConText = mConText;
    }
    @NonNull
    @Override
    public BillAdapter.BillViewHoleder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill, parent, false);

        return new BillAdapter.BillViewHoleder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull BillAdapter.BillViewHoleder holder, int position, @NonNull Bill model) {
        if (model == null){
            return;
        }

        switch (model.getStatusBill()){
            case "unfinished":{
                holder.tvStatus_bill.setText(mConText.getResources().getString(R.string.chua_thanh_toan));
                holder.img_bill.setImageResource(R.drawable.ic_bill_unfinished_64);
                holder.tvIdTable_bill.setText(String.valueOf(model.getIdTableBill()));
                holder.tvDateCreated_bill.setText(String.valueOf(model.getDateCreatedBill()));

                holder.tvStatus_bill.setTextColor(ContextCompat.getColor(mConText, R.color.grey));
                holder.tvIdTable_bill.setTextColor(ContextCompat.getColor(mConText, R.color.grey));
                holder.tvDateCreated_bill.setTextColor(ContextCompat.getColor(mConText, R.color.grey));
                break;
            }case "pending":{
                holder.tvStatus_bill.setText(mConText.getResources().getString(R.string.cho_thanh_toan));
                holder.img_bill.setImageResource(R.drawable.ic_bill_pending_64);
                holder.tvIdTable_bill.setText(String.valueOf(model.getIdTableBill()));
                holder.tvDateCreated_bill.setText(String.valueOf(model.getDateCreatedBill()));

                holder.tvStatus_bill.setTextColor(ContextCompat.getColor(mConText, R.color.yellow));
                holder.tvIdTable_bill.setTextColor(ContextCompat.getColor(mConText, R.color.yellow));
                holder.tvDateCreated_bill.setTextColor(ContextCompat.getColor(mConText, R.color.yellow));
                break;
            }
            default:{
                holder.tvStatus_bill.setText(mConText.getResources().getString(R.string.chua_thanh_toan));
                holder.img_bill.setImageResource(R.drawable.ic_bill_unfinished_64);
                holder.tvIdTable_bill.setText(String.valueOf(model.getIdTableBill()));
                holder.tvDateCreated_bill.setText(String.valueOf(model.getDateCreatedBill()));

                holder.tvStatus_bill.setTextColor(ContextCompat.getColor(mConText, R.color.grey));
                holder.tvIdTable_bill.setTextColor(ContextCompat.getColor(mConText, R.color.grey));
                holder.tvDateCreated_bill.setTextColor(ContextCompat.getColor(mConText, R.color.grey));
                break;
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickBillAdapter.clickOpenBottomSheetDetailBill(model);
            }
        });
    }

    public class BillViewHoleder extends RecyclerView.ViewHolder{
        private ImageView img_bill;
        private TextView tvStatus_bill, tvDateCreated_bill, tvIdTable_bill;

        public BillViewHoleder(@NonNull View itemView) {
            super(itemView);

            img_bill = itemView.findViewById(R.id.img_bill);
            tvStatus_bill = itemView.findViewById(R.id.tvStatus_bill);
            tvDateCreated_bill = itemView.findViewById(R.id.tvDateCreated_bill);
            tvIdTable_bill = itemView.findViewById(R.id.tvIdTable_bill);
        }
    }
}