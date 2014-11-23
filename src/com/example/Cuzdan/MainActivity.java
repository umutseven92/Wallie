package com.example.Cuzdan;

import Fragments.*;
import Helpers.MainPageAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

        List<Fragment> fragments = getFragments();
        pageAdapter = new MainPageAdapter(getSupportFragmentManager(), fragments);

        pager.setAdapter(pageAdapter);
    }

    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();

        fList.add(IncomeFragment.newInstance());
        fList.add(ExpenseFragment.newInstance());
        fList.add(BalanceFragment.newInstance());
        fList.add(SavingsFragment.newInstance());
        fList.add(AccountFragment.newInstance());

        return fList;

    }

}

