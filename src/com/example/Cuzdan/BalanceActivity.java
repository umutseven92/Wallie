package com.example.Cuzdan;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

/**
 * Created by Umut on 15.11.2014.
 */
public class BalanceActivity extends Activity implements ActionBar.TabListener{

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.balance);
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        Tab incomeTab = actionBar.newTab();
        incomeTab.setText("Gelirler");
        incomeTab.setTabListener(this);
        actionBar.addTab(incomeTab);

        Tab expenseTab = actionBar.newTab();
        expenseTab.setText("Giderler");
        expenseTab.setTabListener(this);
        actionBar.addTab(expenseTab);
    }


    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {

    }
}
