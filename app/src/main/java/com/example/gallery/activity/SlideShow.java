package com.example.gallery.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.gallery.R;
import com.example.gallery.adapter.SlideShowAdapter;
import com.example.gallery.model.Item;

import java.util.ArrayList;

public class SlideShow extends BaseActivity {
    private ViewPager2 viewPager2;
    private Handler sliderHandler = new Handler();
    int i = 0;

    @Override
    public void onBackPressed() {
        MainActivity.loadAllFiles();
        MainActivity.refesh();
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 2000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseActivity.changeLanguage(MainActivity.language, this);
        BaseActivity.changeTheme(MainActivity.light, this);
        setContentView(R.layout.activity_slide_show);
        viewPager2 = findViewById(R.id.viewPagerImageSlideshow);

        ArrayList<Item> sliderItems = new ArrayList<>();
        if (MainActivity.mainMode)
            sliderItems = MainActivity.items;
        else
            sliderItems = MainActivity.curAlbum.getImages();
        viewPager2.setAdapter(new SlideShowAdapter(sliderItems, viewPager2, this));
        i = getIntent().getIntExtra("number", 0);
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        viewPager2.setCurrentItem(i);
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 2000);
            }
        });
    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };
}