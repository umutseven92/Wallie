package com.example.Cuzdan;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Umut on 15.11.2014.
 */
public class MainActivity extends FragmentActivity {

    MainPageAdapter pageAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final ViewPager pager = (ViewPager) findViewById(R.id.pager);
        PagerTitleStrip strip = (PagerTitleStrip) findViewById(R.id.pagerTitle);

        List<Fragment> fragments = getFragments();
        pageAdapter = new MainPageAdapter(getSupportFragmentManager(), fragments);

        pager.setAdapter(pageAdapter);

    }

    class MainPageAdapter extends FragmentPagerAdapter {
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

        private final String[] titles = { "Gelirler", "Giderler", "Birikim"};

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

    }

    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();

        fList.add(IncomeFragment.newInstance());
        fList.add(ExpenseFragment.newInstance());
        fList.add(SavingsFragment.newInstance());

        return fList;

    }

    public static class IncomeFragment extends Fragment {

        public static final IncomeFragment newInstance()
        {
            IncomeFragment f = new IncomeFragment();
            return f;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.incomefragment, container, false);
            return v;
        }
    }

    public static class ExpenseFragment extends Fragment {

        public static final ExpenseFragment newInstance()
        {
            ExpenseFragment f = new ExpenseFragment();
            return f;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.expensefragment, container, false);
            return v;
        }
    }

    public static class SavingsFragment extends Fragment {

        public static final SavingsFragment newInstance()
        {
            SavingsFragment f = new SavingsFragment();
            return f;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.savingsfragment, container, false);
            return v;
        }
    }

}
