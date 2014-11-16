package com.example.Cuzdan;

import Helpers.User;
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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Umut on 15.11.2014.
 */
public class MainActivity extends FragmentActivity {

    MainPageAdapter pageAdapter;
    User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Bundle extra = getIntent().getExtras();
        if(extra != null)
        {
            user = (User)extra.getSerializable("user");
            IncomeFragment.SetUser(user);
        }


        final ViewPager pager = (ViewPager) findViewById(R.id.pager);
        PagerTitleStrip strip = (PagerTitleStrip) findViewById(R.id.pagerTitle);

        List<Fragment> fragments = getFragments();
        pageAdapter = new MainPageAdapter(getSupportFragmentManager(), fragments);

        pager.setAdapter(pageAdapter);
    }



    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();

        fList.add(IncomeFragment.newInstance());
        fList.add(ExpenseFragment.newInstance());
        fList.add(SavingsFragment.newInstance());

        return fList;

    }

}

