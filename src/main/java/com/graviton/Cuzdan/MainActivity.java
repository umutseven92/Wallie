package com.graviton.Cuzdan;

import Fragments.*;
import Helpers.*;
import Helpers.Billing.IabHelper;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Umut Seven on 15.11.2014, for Graviton.
 */
public class MainActivity extends FragmentActivity {

    BudgetPageAdapter budgetPageAdapter;
    SavingsPageAdapter savingsPageAdapter;
    SettingsPageAdapter settingsPageAdapter;
    AboutPageAdapter aboutPageAdapter;
    PlusPageAdapter plusPageAdapter;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;
    private CharSequence drawerTitle, title;
    private String[] menuArray;
    ViewPager pager;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        pager = (ViewPager) findViewById(R.id.pager);

        User user = ((Global)getApplication()).GetUser();

        title = drawerTitle = getTitle();

        if(user.GetVersion() == User.Version.Pro)
        {
            menuArray = getResources().getStringArray(R.array.plusMenuArray);
        }
        else
        {
            menuArray = getResources().getStringArray(R.array.menuArray);
        }

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);

        PagerTabStrip t = (PagerTabStrip) findViewById(R.id.pagerTitle);
        t.setTextColor(getResources().getColor(R.color.cuzdan_red));
        t.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        t.setTabIndicatorColor(getResources().getColor(R.color.cuzdan_red));

        // set a custom shadow that overlays the main content when the drawer opens
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        // set up the drawer's list view with items and click listener
        drawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, menuArray));

        drawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        drawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(title);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(drawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);

        List<Fragment> accountFragments = GetAccountFragments();
        List<Fragment> savingsFragments = GetSavingsFragments();
        List<Fragment> settingsFragments = GetSettingsFragments();
        List<Fragment> aboutFragments = GetAboutFragments();
        List<Fragment> plusFragments = GetPlusFragments();

        budgetPageAdapter = new BudgetPageAdapter(getSupportFragmentManager(), accountFragments, 0);
        savingsPageAdapter = new SavingsPageAdapter(getSupportFragmentManager(), savingsFragments, 1);
        settingsPageAdapter = new SettingsPageAdapter(getSupportFragmentManager(), settingsFragments, 2);
        aboutPageAdapter = new AboutPageAdapter(getSupportFragmentManager(), aboutFragments, 3);
        plusPageAdapter = new PlusPageAdapter(getSupportFragmentManager(), plusFragments, 4);

        if (savedInstanceState == null) {
            selectItem(0);
        }

    }


    private List<Fragment> GetAccountFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();

        fList.add(IncomeFragment.newInstance());

        fList.add(ExpenseFragment.newInstance());
        fList.add(BalanceFragment.newInstance());

        return fList;
    }

    private List<Fragment> GetSavingsFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();

        fList.add(SavingsFragment.newInstance());

        return fList;
    }

    private List<Fragment> GetSettingsFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();

        fList.add(AccountFragment.newInstance());

        return fList;
    }

    private List<Fragment> GetAboutFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();

        fList.add(AboutFragment.newInstance());

        return fList;
    }

    private List<Fragment> GetPlusFragments()
    {
        List<Fragment> fList = new ArrayList<Fragment>();
        fList.add(PlusFragment.newInstance());

        return fList;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    // When I wrote this, only God and I understood what I was doing
    // Now, God only knows
    //                    -Karl Weierstrass
    private void selectItem(int position) {

        if (getSupportFragmentManager().getFragments() != null) {
            for (int i = 0; i < getSupportFragmentManager().getFragments().size(); i++) {
                Fragment f = getSupportFragmentManager().getFragments().get(i);
                if (f != null) {
                    getSupportFragmentManager().beginTransaction().disallowAddToBackStack().remove(getSupportFragmentManager().getFragments().get(i)).commit();
                }
            }
            getSupportFragmentManager().executePendingTransactions();
        }

        switch (position) {
            case 0:
                List<Fragment> accountFragments = GetAccountFragments();
                budgetPageAdapter = new BudgetPageAdapter(getSupportFragmentManager(), accountFragments, 0);
                pager.setAdapter(budgetPageAdapter);
                break;
            case 1:
                List<Fragment> savingsFragments = GetSavingsFragments();
                savingsPageAdapter = new SavingsPageAdapter(getSupportFragmentManager(), savingsFragments, 1);
                pager.setAdapter(savingsPageAdapter);
                break;
            case 2:
                List<Fragment> settingsFragments = GetSettingsFragments();
                settingsPageAdapter = new SettingsPageAdapter(getSupportFragmentManager(), settingsFragments, 2);
                pager.setAdapter(settingsPageAdapter);
                break;
            case 3:
                List<Fragment> aboutFragments = GetAboutFragments();
                aboutPageAdapter = new AboutPageAdapter(getSupportFragmentManager(), aboutFragments, 3);
                pager.setAdapter(aboutPageAdapter);
                break;
            case 4:
                List<Fragment> plusFragments = GetPlusFragments();
                plusPageAdapter = new PlusPageAdapter(getSupportFragmentManager(), plusFragments, 3);
                pager.setAdapter(plusPageAdapter);
                break;
            default:
                break;
        }

        drawerList.setItemChecked(position, true);
        setTitle(menuArray[position]);
        drawerLayout.closeDrawer(drawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        this.title = title;
        getActionBar().setTitle(this.title);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IabHelper helper = ((Global) getApplication()).iabHelper;

        // Pass on the activity result to the helper for handling
        if (!helper.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            Log.d("IABHELPER", "onActivityResult handled by IABUtil.");
        }
    }

    @Override
    public void onBackPressed() {


        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("Çıkış")
                .setMessage("Çıkmak istediğinizden emin misiniz?")
                .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((Global) getApplication()).iabHelper.dispose();
                        User user = ((Global) getApplication()).GetUser();
                        if (user.GetVersion() == User.Version.Pro && user.GetAutoBackup().equals("true")) {
                            try {
                                user.GetBanker().BackupUserData();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        finish();
                    }

                })
                .setNegativeButton("Hayır", null)
                .show();
    }
}


