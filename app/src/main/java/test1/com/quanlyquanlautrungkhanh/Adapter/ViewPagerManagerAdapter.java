package test1.com.quanlyquanlautrungkhanh.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import test1.com.quanlyquanlautrungkhanh.Fragment.ManagerFragment;
import test1.com.quanlyquanlautrungkhanh.Fragment.PaymentFragment;
import test1.com.quanlyquanlautrungkhanh.Fragment.ChartFragment;

public class ViewPagerManagerAdapter extends  FragmentStateAdapter {
    private static final int NUM_PAGES = 3;

    public ViewPagerManagerAdapter(@NonNull FragmentActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new PaymentFragment();

            case 1:
                return new ChartFragment();

            case 2:
                return new ManagerFragment();

            default:
                return new PaymentFragment();
        }
    }

    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }
}
