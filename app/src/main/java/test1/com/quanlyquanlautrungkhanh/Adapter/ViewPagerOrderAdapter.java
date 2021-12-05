package test1.com.quanlyquanlautrungkhanh.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import test1.com.quanlyquanlautrungkhanh.Fragment.ChartFragment;
import test1.com.quanlyquanlautrungkhanh.Fragment.ManagerFragment;
import test1.com.quanlyquanlautrungkhanh.Fragment.OrderTableFragment;
import test1.com.quanlyquanlautrungkhanh.Fragment.PaymentFragment;
import test1.com.quanlyquanlautrungkhanh.Fragment.RequirementFoodFragment;

public class ViewPagerOrderAdapter extends FragmentStateAdapter {
    private static final int NUM_PAGES = 2;

    public ViewPagerOrderAdapter(@NonNull FragmentActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new OrderTableFragment();

            case 1:
                return new RequirementFoodFragment();

            default:
                return new PaymentFragment();
        }
    }

    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }
}
