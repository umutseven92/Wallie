package Helpers;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.graviton.Cuzdan.R;

import java.util.List;

/**
 * Created by Umut Seven on 14.4.2015, for Graviton.
 */
public class AboutPageAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;

    public int Position;

    public AboutPageAdapter(FragmentManager fm, List<Fragment> fragments, int pos, Context ctx) {
        super(fm);
        Position = pos;
        this.fragments = fragments;
        titles = new String[]{ctx.getResources().getString(R.string.about_about)};
    }

    @Override
    public Fragment getItem(int i) {
        return this.fragments.get(i);
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }

    private final String[] titles;

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
