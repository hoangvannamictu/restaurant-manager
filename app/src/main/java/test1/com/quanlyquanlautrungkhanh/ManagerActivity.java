package test1.com.quanlyquanlautrungkhanh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import test1.com.quanlyquanlautrungkhanh.Adapter.ViewPagerManagerAdapter;

public class ManagerActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView_manager;
    private ViewPager2 viewPager2_manager;
    private ViewPagerManagerAdapter viewPagerManagerAdapter;

    private FragmentStateAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_manager);

        anhXa();
        addEvents();
    }

    private void addEvents() {
        viewPager2_manager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                switch (position){
                    case 0:
                        bottomNavigationView_manager.getMenu().findItem(R.id.menu_payment).setChecked(true);
                        break;

                    case 1:
                        bottomNavigationView_manager.getMenu().findItem(R.id.menu_chart).setChecked(true);
                        break;

                    case 2:
                        bottomNavigationView_manager.getMenu().findItem(R.id.menu_manager).setChecked(true);
                        break;
                }
            }
        });

        bottomNavigationView_manager.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_payment:
                        viewPager2_manager.setCurrentItem(0);
                        break;

                    case R.id.menu_chart:
                        viewPager2_manager.setCurrentItem(1);
                        break;

                    case R.id.menu_manager:
                        viewPager2_manager.setCurrentItem(2);
                        break;
                }
                return true;
            }
        });
    }

    private void anhXa() {
        bottomNavigationView_manager = findViewById(R.id.bottomNavigationView_manager);
        viewPager2_manager = findViewById(R.id.viewPager2_manager);

        viewPagerManagerAdapter = new ViewPagerManagerAdapter(this);
        viewPager2_manager.setAdapter(viewPagerManagerAdapter);
    }


    @Override
    public void onBackPressed() {
        if (viewPager2_manager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            viewPager2_manager.setCurrentItem(viewPager2_manager.getCurrentItem() - 1);
        }
    }
}