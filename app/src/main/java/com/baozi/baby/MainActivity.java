package com.baozi.baby;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.KeyEvent;
import android.widget.Toast;
import com.baozi.baby.base.BaseActivity;
import com.baozi.baby.fragment.HomeFragment;
import com.baozi.baby.util.ActivityManager;
import com.baozi.baby.view.NoScrollViewPager;
import com.jpeng.jptabbar.JPTabBar;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import butterknife.BindView;
import rx.Observable;
import rx.Observer;
import rx.Subscription;

/**
 * Created by qiong.liao on 2018/4/13.
 */

public class MainActivity extends BaseActivity {

    @BindView(R.id.view_pager)
    NoScrollViewPager viewPager;
    @BindView(R.id.tabbar)
    JPTabBar tabbar;

    private List<Fragment> fragments = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        tabbar.setTitles(R.string.penguin_continent, R.string.auction, R.string.my)
                .setNormalIcons(R.mipmap.snake, R.mipmap.snake, R.mipmap.snake)
                .setSelectedIcons(R.mipmap.snake_red, R.mipmap.snake_red, R.mipmap.snake_red)
                .generate();
        viewPager.setNoScroll(true);
        viewPager.setOffscreenPageLimit(3);
        fragments.add(HomeFragment.newInstance());
        fragments.add(HomeFragment.newInstance());
        fragments.add(HomeFragment.newInstance());
        viewPager.setAdapter(new ContentAdapter(getSupportFragmentManager()));
        tabbar.setContainer(viewPager);
    }

    /**
     * 内容页的适配器
     */
    private class ContentAdapter extends FragmentPagerAdapter {

        public ContentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private Subscription subscribe;
    private static boolean isExit = false;

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            subscribe = Observable.timer(2, TimeUnit.SECONDS).subscribe(new Observer<Long>() {
                @Override
                public void onCompleted() {
                    isExit = false;
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(Long aLong) {

                }
            });
        } else {
            subscribe.unsubscribe();
            subscribe = null;
            ActivityManager activitymanager = ActivityManager.getInstance();
            activitymanager.clearAll();
            System.exit(0);
        }
    }

}
