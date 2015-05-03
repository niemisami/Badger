package niemisami.badger;

import android.os.IInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;


public class BadgeActivity extends FragmentActivity {

    private final String TAG = "BadgerActivity";

    //    ViewPager allows swipe right or left to view other badges
    private ViewPager mViewPager;
    private ArrayList<Badge> mBadges;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);
        mBadges = BadgeManager.get(this).getBadges();


        FragmentManager manager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(manager) {
            @Override
            public Fragment getItem(int position) {

                Badge badge = mBadges.get(position);
                return BadgeFragment.newInstance(badge.getId());
            }

            @Override
            public int getCount() {
                return mBadges.size();
            }
        });

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

//
        overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_bottom);

        UUID badgeId = (UUID)getIntent().getSerializableExtra(BadgeFragment.EXTRA_BADGE_ID);
        for (int i = 0; i < mBadges.size(); i++) {

            if (mBadges.get(i).getId().equals(badgeId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {

        FragmentManager manager = getSupportFragmentManager();
        BadgeFragment fragment = (BadgeFragment) manager.findFragmentById(R.id.fragmentContainer);
        if (fragment != null) {
            fragment.setWorkDoneAndExit();
        } else
            super.onBackPressed();


        overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_bottom);
    }
}
