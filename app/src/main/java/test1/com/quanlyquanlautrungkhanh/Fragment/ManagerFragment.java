package test1.com.quanlyquanlautrungkhanh.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import test1.com.quanlyquanlautrungkhanh.Adapter.ItemManagerAdapter;
import test1.com.quanlyquanlautrungkhanh.Model.ItemManager;
import test1.com.quanlyquanlautrungkhanh.R;

public class ManagerFragment extends Fragment {
    private RecyclerView rcvManager;
    private ItemManagerAdapter adapter;
    private List<ItemManager> listItemManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manager, container, false);

        rcvManager= view.findViewById(R.id.rcvManager);
        adapter = new ItemManagerAdapter(getContext());

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        rcvManager.setLayoutManager(gridLayoutManager);

        adapter.setData(getListItemManager());
        rcvManager.setAdapter(adapter);

        return view;
    }

    private List<ItemManager> getListItemManager() {
        listItemManager = new ArrayList<>();

        listItemManager.add(new ItemManager(R.drawable.ic_admin_128, getResources().getString(R.string.admin_capslock)));
        listItemManager.add(new ItemManager(R.drawable.ic_user_128,getResources().getString(R.string.nhan_vien_capslock)));
        listItemManager.add(new ItemManager(R.drawable.ic_food_128,getResources().getString(R.string.mon_an_capslock)));
        listItemManager.add(new ItemManager(R.drawable.ic_table_128,getResources().getString(R.string.ban_capslock)));
        listItemManager.add(new ItemManager(R.drawable.ic_voucher_128,getResources().getString(R.string.voucher_capslock)));

        return listItemManager;
    }
}
