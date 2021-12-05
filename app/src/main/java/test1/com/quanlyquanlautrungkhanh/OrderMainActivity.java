package test1.com.quanlyquanlautrungkhanh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import test1.com.quanlyquanlautrungkhanh.Adapter.ViewPagerManagerAdapter;
import test1.com.quanlyquanlautrungkhanh.Adapter.ViewPagerOrderAdapter;

public class OrderMainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView_order;
    private ViewPager2 viewPager2_order;
    private ViewPagerOrderAdapter viewPagerOrderAdapter;

    private FragmentStateAdapter pagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        anhXa();
        addEvents();
    }

    private void addEvents() {
        viewPager2_order.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                switch (position){
                    case 0:
                        bottomNavigationView_order.getMenu().findItem(R.id.menu_order_table).setChecked(true);
                        break;

                    case 1:
                        bottomNavigationView_order.getMenu().findItem(R.id.menu_requirement_food).setChecked(true);
                        break;

                }
            }
        });

        bottomNavigationView_order.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_order_table:
                        viewPager2_order.setCurrentItem(0);
                        break;

                    case R.id.menu_requirement_food:
                        viewPager2_order.setCurrentItem(1);
                        break;
                }
                return true;
            }
        });
    }

    private void anhXa() {
        bottomNavigationView_order = findViewById(R.id.bottomNavigationView_order);
        viewPager2_order = findViewById(R.id.viewPager2_order);

        viewPagerOrderAdapter = new ViewPagerOrderAdapter(this);
        viewPager2_order.setAdapter(viewPagerOrderAdapter);
    }


    @Override
    public void onBackPressed() {
        if (viewPager2_order.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            viewPager2_order.setCurrentItem(viewPager2_order.getCurrentItem() - 1);
        }
    }
}