package Helpers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class BudgetPageAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;

    public int Position;

    public BudgetPageAdapter(FragmentManager fm, List<Fragment> fragments, int pos) {
        super(fm);
        Position = pos;
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

    private final String[] titles = {"Gelirler", "Giderler", "Bakiye"};

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

}
