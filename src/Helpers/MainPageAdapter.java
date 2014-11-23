package Helpers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class MainPageAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;

    public MainPageAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }

    private final String[] titles = { "Gelirler", "Giderler", "Bakiye", "Birikim", "Hesap"};

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

}
