package com.graviton.Cuzdan;

import Fragments.IncomeGraphFragment;
import Fragments.IncomeSearchFragment;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


/**
 * Created by Umut on 14.1.2015.
 */
public class IncomeStatsActivity  extends FragmentActivity implements ActionBar.TabListener{

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActionBar();

        actionBar.setTitle("Gelirler");
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setStackedBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.cuzdan_red)));

        actionBar.addTab(actionBar.newTab().setText("Gelir Arama").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("Grafikler").setTabListener(this));
    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {

        // pos 0 -> Gelir arama, pos 1 -> Grafikler
        int pos = tab.getPosition();

        if(pos == 0)
        {
            ft.replace(android.R.id.content,new IncomeSearchFragment());

        }
        else if(pos == 1)
        {
            ft.replace(android.R.id.content,new IncomeGraphFragment());

        }

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }
}
