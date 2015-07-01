package wizard;

import android.content.Context;
import android.util.SparseArray;
import com.graviton.Cuzdan.Global;
import com.graviton.Cuzdan.R;
import wizard.model.*;

import java.util.ArrayList;


/**
 * Created by Umut Seven on 2.1.2015, for Graviton.
 */
public class ExpenseWizardModel extends AbstractWizardModel {
    public ExpenseWizardModel(Context context) {
        super(context);
    }

    @Override
    protected PageList onNewRootPageList() {

        BranchPage mainPage = new BranchPage(this, mContext.getString(R.string.expense_type));
        mainPage.setRequired(true);

        SparseArray<String> expenses = ((Global) mContext.getApplicationContext()).recordsHelper.ReturnExpenses();

        SparseArray<String> personalCategories = new SparseArray<String>();
        SparseArray<String> homeCategories = new SparseArray<String>();

        // Kategorileri al
        for (int i = 0; i < expenses.size(); i++) {
            if (Integer.toString(expenses.keyAt(i)).startsWith("21") && Integer.toString(expenses.keyAt(i)).toCharArray().length == 4) {
                // "Personal"
                personalCategories.put(expenses.keyAt(i), expenses.valueAt(i));

            } else if (Integer.toString(expenses.keyAt(i)).startsWith("22") && Integer.toString(expenses.keyAt(i)).toCharArray().length == 4) {
                // "Home"
                homeCategories.put(expenses.keyAt(i), expenses.valueAt(i));
            }
        }

        BranchPage personalBranch = new BranchPage(this, mContext.getString(R.string.category));
        personalBranch.setRequired(true);

        // Personal
        for (int i = 0; i < personalCategories.size(); i++) {
            ArrayList<String> personalSubCategories = new ArrayList<String>();

            for (int j = 0; j < expenses.size(); j++) {
                if (Integer.toString(expenses.keyAt(j)).toCharArray().length > 4 && Integer.toString(expenses.keyAt(j)).startsWith(Integer.toString(personalCategories.keyAt(i)))) {
                    personalSubCategories.add(expenses.valueAt(j));
                }
            }

            if (personalCategories.valueAt(i).equals(mContext.getString(R.string.custom_category_personal))) {
                personalBranch.addBranch(personalCategories.valueAt(i), new BalanceCustomInfoPage(this, mContext.getString(R.string.enter_category), mContext).setRequired(true));
            } else {
                personalBranch.addBranch(personalCategories.valueAt(i), new SingleFixedChoicePage(this, mContext.getString(R.string.subCategory)).setChoices(personalSubCategories.toArray(new String[personalSubCategories.size()])).setRequired(true));
            }
        }

        BranchPage homeBranch = new BranchPage(this, mContext.getString(R.string.category));
        homeBranch.setRequired(true);

        // Home
        for (int i = 0; i < homeCategories.size(); i++) {
            ArrayList<String> homeSubCategories = new ArrayList<String>();

            for (int j = 0; j < expenses.size(); j++) {
                if (Integer.toString(expenses.keyAt(j)).toCharArray().length > 4 && Integer.toString(expenses.keyAt(j)).startsWith(Integer.toString(homeCategories.keyAt(i)))) {
                    homeSubCategories.add(expenses.valueAt(j));
                }
            }

            if (homeCategories.valueAt(i).equals(mContext.getString(R.string.custom_category_home))) {
                homeBranch.addBranch(homeCategories.valueAt(i), new BalanceCustomInfoPage(this, mContext.getString(R.string.enter_category), mContext).setRequired(true));
            } else {
                homeBranch.addBranch(homeCategories.valueAt(i), new SingleFixedChoicePage(this, mContext.getString(R.string.subCategory)).setChoices(homeSubCategories.toArray(new String[homeSubCategories.size()])).setRequired(true));
            }
        }

        mainPage.addBranch(mContext.getString(R.string.tag_personal), personalBranch);
        mainPage.addBranch(mContext.getString(R.string.tag_home), homeBranch);

        return new PageList(mainPage, new BalanceInfoPage(this, mContext.getString(R.string.details), mContext).setRequired(true));
    }

}
