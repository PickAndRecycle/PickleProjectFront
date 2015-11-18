package com.pickle.pickleproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

public class PickList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_list);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("UNUSED"));
        tabLayout.addTab(tabLayout.newTab().setText("GENERAL"));
        tabLayout.addTab(tabLayout.newTab().setText("RECYCLED"));
        tabLayout.addTab(tabLayout.newTab().setText("GREEN"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        ImageButton back = (ImageButton) findViewById(R.id.backHome);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeHome();
            }
        });

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void changeHome() {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
        this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

}
