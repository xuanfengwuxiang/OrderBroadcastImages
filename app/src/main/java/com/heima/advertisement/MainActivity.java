package com.heima.advertisement;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity {
    public static final int SHOW_NEXT_PAGE = 1;
    @InjectView(R.id.view_pager)
    ViewPager viewPager;
    @InjectView(R.id.tv_desc)
    TextView tvDesc;
    @InjectView(R.id.ll_dots)
    LinearLayout llDots;
    private int[] imageResIds = {
            R.drawable.a,
            R.drawable.b,
            R.drawable.c,
            R.drawable.d,
            R.drawable.e,
    };

    private String[] descs = {
            "巩俐不低俗，我就不能低俗",
            "扑树又回来啦！再唱经典老歌引万人大合唱",
            "揭秘北京电影如何升级",
            "乐视网TV版大派送",
            "热血屌丝的反杀",
    };

    Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case SHOW_NEXT_PAGE:
                    showNextPage();
                    break;
            }
        }
    };



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        ViewPager view_pager = (ViewPager) findViewById(R.id.view_pager);
        view_pager.setAdapter(new BannerAdapter(imageResIds));
        initeDots();
        changeDescAndDot(0);

        view_pager.setOnPageChangeListener(mOnPageChangeListener);
        view_pager.setCurrentItem(view_pager.getAdapter().getCount()/2);
        handler.sendEmptyMessageDelayed(SHOW_NEXT_PAGE,3000);
        view_pager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        handler.removeCallbacksAndMessages(null);

                        break;
                    case MotionEvent.ACTION_UP:
                        handler.sendEmptyMessageDelayed(SHOW_NEXT_PAGE, 3000);
                        break;
                }
                return false;
            }


        });
    }



    private class BannerAdapter extends PagerAdapter {
        private int[] imageResIds;

        public BannerAdapter(int[] imageResIds) {
            this.imageResIds = imageResIds;

        }

        @Override
        public int getCount() {
            return imageResIds.length * 10000 * 100;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(container.getContext());
            imageView.setBackgroundResource(imageResIds[position%imageResIds.length]);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }//适配器

    private void initeDots() {
        for (int i = 0;i<imageResIds.length;i++) {
            View dot = new View(this);
            dot.setBackgroundResource(R.drawable.selector_dot);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dp2px(5),dp2px(5));
            params.leftMargin = i==0?0:5;
            dot.setLayoutParams(params);
            llDots.addView(dot);
        }
    }

    private void changeDescAndDot(int position) {
        position = position%imageResIds.length;
        tvDesc.setText(descs[position]);
        for (int i =0;i<llDots.getChildCount();i++){
            llDots.getChildAt(i).setSelected(i==position);
        }
    }


    public int dp2px(int dp) {
        float density = getResources().getDisplayMetrics().density;	// 获取手机屏幕的密度
        return (int) (dp * density + 0.5f);	// 加0.5是为了四舍五入
    }

    ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            changeDescAndDot(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    private void showNextPage() {
        viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
        handler.sendEmptyMessageDelayed(SHOW_NEXT_PAGE, 3000);
    }


    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
