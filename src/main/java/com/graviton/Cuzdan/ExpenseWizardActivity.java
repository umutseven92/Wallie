package com.graviton.Cuzdan;

import Helpers.*;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import org.json.JSONException;
import wizard.ExpenseWizardModel;
import wizard.model.*;
import wizard.ui.PageFragmentCallbacks;
import wizard.ui.ReviewFragment;
import wizard.ui.StepPagerStrip;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * Created by Umut Seven on 2.1.2015, for Graviton.
 */
public class ExpenseWizardActivity extends FragmentActivity implements PageFragmentCallbacks, ReviewFragment.Callbacks, ModelCallbacks {
    private ViewPager mPager;
    private MyPagerAdapter mPagerAdapter;
    private boolean mEditingAfterReview;
    private AbstractWizardModel mWizardModel;
    private boolean mConsumePageSelectedEvent;
    private Button mNextButton;
    private Button mPrevButton;
    private List<Page> mCurrentPageSequence;
    private StepPagerStrip mStepPagerStrip;
    private ExpenseAddListener _addListener;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar().setTitle(getString(R.string.add_expense));
        mWizardModel = new ExpenseWizardModel(this);

        if (savedInstanceState != null) {
            mWizardModel.load(savedInstanceState.getBundle("model"));
        }

        mWizardModel.registerListener(this);

        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);
        mStepPagerStrip = (StepPagerStrip) findViewById(R.id.strip);
        mStepPagerStrip.setOnPageSelectedListener(new StepPagerStrip.OnPageSelectedListener() {
            @Override
            public void onPageStripSelected(int position) {
                position = Math.min(mPagerAdapter.getCount() - 1, position);
                if (mPager.getCurrentItem() != position) {
                    mPager.setCurrentItem(position);
                }
            }
        });

        mNextButton = (Button) findViewById(R.id.next_button);
        mPrevButton = (Button) findViewById(R.id.prev_button);

        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mStepPagerStrip.setCurrentPage(position);

                if (mConsumePageSelectedEvent) {
                    mConsumePageSelectedEvent = false;
                    return;
                }

                mEditingAfterReview = false;
                updateBottomBar();
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    GoForwardOnePage();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPager.setCurrentItem(mPager.getCurrentItem() - 1);
            }
        });

        SetListener(((Global) this.getApplication()).expenseAddListener);

        onPageTreeChanged();
        updateBottomBar();

    }

    private void SetListener(ExpenseAddListener listener) {
        _addListener = listener;
    }

    private void GoForwardOnePage() throws IOException, JSONException {
        if (mPager.getCurrentItem() == mCurrentPageSequence.size()) {
            String tag = mWizardModel.findByKey(getString(R.string.expense_type)).getData().getString(Page.SIMPLE_DATA_KEY);
            Expense.Tags expenseTag;

            if (tag.equals(getString(R.string.tag_personal))) {
                expenseTag = Expense.Tags.Personal;
            } else {
                expenseTag = Expense.Tags.Home;
            }

            User user = ((Global) getApplication()).GetUser();
            RecordsHelper records = ((Global) getApplication()).recordsHelper;

            Banker banker = user.GetBanker();
            String category = mWizardModel.findByKey(tag + ":" + getString(R.string.category)).getData().getString(Page.SIMPLE_DATA_KEY);

            String subCategory;
            try {
                subCategory = mWizardModel.findByKey(category + ":" + getString(R.string.subCategory)).getData().getString(Page.SIMPLE_DATA_KEY);
            } catch (NullPointerException ex) {

                subCategory = mWizardModel.findByKey(getString(R.string.custom_category_home) + ":" + getString(R.string.enter_category)).getData().getString(BalanceCustomInfoPage.CUST_CAT_DATA_KEY);
                if (subCategory == null) {
                    subCategory = mWizardModel.findByKey(getString(R.string.custom_category_personal) + ":" + getString(R.string.enter_category)).getData().getString(BalanceCustomInfoPage.CUST_CAT_DATA_KEY);
                }
                if (!(banker.GetExpenseCustoms().contains(subCategory))) {
                    banker.AddExpenseCustom(subCategory);
                }
            }
            BigDecimal amount = new BigDecimal(mWizardModel.findByKey(getString(R.string.details)).getData().getString(BalanceInfoPage.AMOUNT_DATA_KEY));
            String description = mWizardModel.findByKey(getString(R.string.details)).getData().getString(BalanceInfoPage.DESC_DATA_KEY);

            String catID = Integer.toString(records.GetIDFromName(category));

            String subCatID = "";
            if (category.equals(getString(R.string.custom_category_personal)) || category.equals(getString(R.string.custom_category_home))) {
                subCatID = subCategory;
            } else {
                subCatID = Integer.toString(records.GetIDFromName(subCategory));
            }

            Expense expense = new Expense(catID, subCatID, amount, description, new Date(), expenseTag);

            try {
                banker.AddExpense(expense);
            } catch (Exception e) {
                e.printStackTrace();
            }
            _addListener.onAdded();
            finish();
        } else {
            if (mEditingAfterReview) {
                mPager.setCurrentItem(mPagerAdapter.getCount() - 1);
            } else {
                mPager.setCurrentItem(mPager.getCurrentItem() + 1);
            }
        }

    }


    private void updateBottomBar() {
        int position = mPager.getCurrentItem();
        if (position == mCurrentPageSequence.size()) {
            mNextButton.setText(R.string.finish_expense);
            mNextButton.setBackgroundResource(R.drawable.finish_background);
            mNextButton.setTextAppearance(this, R.style.TextAppearanceFinish);
        } else {
            mNextButton.setText(mEditingAfterReview
                    ? R.string.review
                    : R.string.next);
            mNextButton.setBackgroundResource(R.drawable.selectable_item_background);
            TypedValue v = new TypedValue();
            getTheme().resolveAttribute(android.R.attr.textAppearanceMedium, v, true);
            mNextButton.setTextAppearance(this, v.resourceId);
            mNextButton.setEnabled(position != mPagerAdapter.getCutOffPage());
        }

        mPrevButton.setVisibility(position <= 0 ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWizardModel.unregisterListener(this);
    }

    @Override
    public AbstractWizardModel onGetModel() {
        return mWizardModel;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle("model", mWizardModel.save());
    }

    @Override
    public void onEditScreenAfterReview(String key) {
        for (int i = mCurrentPageSequence.size() - 1; i >= 0; i--) {
            if (mCurrentPageSequence.get(i).getKey().equals(key)) {
                mConsumePageSelectedEvent = true;
                mEditingAfterReview = true;
                mPager.setCurrentItem(i);
                updateBottomBar();
                break;
            }
        }

    }

    @Override
    public void onPageDataChanged(Page page) {
        if (page.isRequired()) {
            if (recalculateCutOffPage()) {
                mPagerAdapter.notifyDataSetChanged();
                updateBottomBar();
            }
        }

    }

    @Override
    public void onPageTreeChanged() {
        mCurrentPageSequence = mWizardModel.getCurrentPageSequence();
        recalculateCutOffPage();
        mStepPagerStrip.setPageCount(mCurrentPageSequence.size() + 1); // + 1 = review step
        mPagerAdapter.notifyDataSetChanged();
        updateBottomBar();
    }

    @Override
    public void onOptionClicked() {
        try {
            GoForwardOnePage();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Page onGetPage(String key) {
        return mWizardModel.findByKey(key);
    }

    private boolean recalculateCutOffPage() {
        // Cut off the pager adapter at first required page that isn't completed
        int cutOffPage = mCurrentPageSequence.size() + 1;
        for (int i = 0; i < mCurrentPageSequence.size(); i++) {
            Page page = mCurrentPageSequence.get(i);
            if (page.isRequired() && !page.isCompleted()) {
                cutOffPage = i;
                break;
            }
        }

        if (mPagerAdapter.getCutOffPage() != cutOffPage) {
            mPagerAdapter.setCutOffPage(cutOffPage);
            return true;
        }

        return false;
    }

    public class MyPagerAdapter extends FragmentStatePagerAdapter {
        private int mCutOffPage;
        private Fragment mPrimaryItem;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            if (i >= mCurrentPageSequence.size()) {
                return new ReviewFragment();
            }

            return mCurrentPageSequence.get(i).createFragment();
        }

        @Override
        public int getItemPosition(Object object) {
            // TODO: be smarter about this
            if (object == mPrimaryItem) {
                // Re-use the current fragment (its position never changes)
                return POSITION_UNCHANGED;
            }

            return POSITION_NONE;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            mPrimaryItem = (Fragment) object;
        }

        @Override
        public int getCount() {
            if (mCurrentPageSequence == null) {
                return 0;
            }
            return Math.min(mCutOffPage + 1, mCurrentPageSequence.size() + 1);
        }

        public void setCutOffPage(int cutOffPage) {
            if (cutOffPage < 0) {
                cutOffPage = Integer.MAX_VALUE;
            }
            mCutOffPage = cutOffPage;
        }

        public int getCutOffPage() {
            return mCutOffPage;
        }
    }


}
