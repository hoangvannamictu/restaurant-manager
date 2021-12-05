package test1.com.quanlyquanlautrungkhanh.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import test1.com.quanlyquanlautrungkhanh.AdminActivity;
import test1.com.quanlyquanlautrungkhanh.FoodActivity;
import test1.com.quanlyquanlautrungkhanh.Model.ItemManager;
import test1.com.quanlyquanlautrungkhanh.R;
import test1.com.quanlyquanlautrungkhanh.TableActivity;
import test1.com.quanlyquanlautrungkhanh.UserActivity;

public class ItemManagerAdapter extends RecyclerView.Adapter<ItemManagerAdapter.ItemViewHolder>{
    private Context mConText;
    private List<ItemManager> listItemManager;

    public ItemManagerAdapter(Context mConText) {
        this.mConText = mConText;
    }

    public void setData(List<ItemManager> list){
        this.listItemManager = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_manager, parent, false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ItemManager itemManager = listItemManager.get(position);
        if (itemManager == null){
            return;
        }

        holder.imgItemManager.setImageResource(itemManager.getResourceImage());
        holder.tvNameItem.setText(itemManager.getNameItem());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemManager.getNameItem().equalsIgnoreCase(mConText.getResources().getString(R.string.admin_capslock))){
                    Intent intent = new Intent(mConText, AdminActivity.class);
                    mConText.startActivity(intent);
                }else if(itemManager.getNameItem().equalsIgnoreCase(mConText.getResources().getString(R.string.nhan_vien_capslock))){
                    Intent intent = new Intent(mConText, UserActivity.class);
                    mConText.startActivity(intent);
                }else if(itemManager.getNameItem().equalsIgnoreCase(mConText.getResources().getString(R.string.mon_an_capslock))){
                    Intent intent = new Intent(mConText, FoodActivity.class);
                    mConText.startActivity(intent);
                }else if (itemManager.getNameItem().equalsIgnoreCase(mConText.getResources().getString(R.string.ban_capslock))){
                    Intent intent = new Intent(mConText, TableActivity.class);
                    mConText.startActivity(intent);
                }else if (itemManager.getNameItem().equalsIgnoreCase(mConText.getResources().getString(R.string.voucher_capslock))){

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (listItemManager != null){
            return listItemManager.size();
        }
        return 0;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgItemManager;
        private TextView tvNameItem;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            imgItemManager = itemView.findViewById(R.id.imgItemManager);
            tvNameItem = itemView.findViewById(R.id.tvNameItem);
        }
    }
}
