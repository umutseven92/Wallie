package Helpers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;


/**
 * Created by Umut Seven on 29.11.2014, for Graviton.
 */
public class SettingsPageAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;

    public int Position;

    public SettingsPageAdapter(FragmentManager fm, List<Fragment> fragments, int pos)
    {
        super(fm);
        Position = pos;
        this.fragments = fragments;
    }
    @Override
    public Fragment getItem(int i) {
        return this.fragments.get(i);
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }

    private final String[] titles = { "Hesap" };

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
