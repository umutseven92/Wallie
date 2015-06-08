package Helpers;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.graviton.Cuzdan.R;

import java.util.List;

public class BudgetPageAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;

    public int Position;

    public BudgetPageAdapter(FragmentManager fm, List<Fragment> fragments, int pos, Context ctx) {
        super(fm);
        Position = pos;
        this.fragments = fragments;
        titles = new String[]{
                ctx.getResources().getString(R.string.budget_incomes),
                ctx.getResources().getString(R.string.budget_expenses),
                ctx.getResources().getString(R.string.budget_balance)};
    }

    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
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
