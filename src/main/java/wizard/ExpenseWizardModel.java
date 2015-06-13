package wizard;

import android.content.Context;
import com.graviton.Cuzdan.R;
import wizard.model.*;


/**
 * Created by Umut Seven on 2.1.2015, for Graviton.
 */
public class ExpenseWizardModel extends AbstractWizardModel {
    public ExpenseWizardModel(Context context) {
        super(context);
    }

    @Override
    protected PageList onNewRootPageList() {


        return new PageList(
                new BranchPage(this, mContext.getString(R.string.expense_type))
                        .addBranch(mContext.getString(R.string.tag_personal), new BranchPage(this, mContext.getString(R.string.category))
                                        .addBranch(mContext.getString(R.string.food), new SingleFixedChoicePage(this, mContext.getString(R.string.subCategory)).setChoices(mContext.getString(R.string.food1), mContext.getString(R.string.food2)).setRequired(true))
                                        .addBranch(mContext.getString(R.string.cig), new SingleFixedChoicePage(this, mContext.getString(R.string.subCategory)).setChoices(mContext.getString(R.string.cig1), mContext.getString(R.string.cig2)).setRequired(true))
                                        .addBranch(mContext.getString(R.string.shop), new SingleFixedChoicePage(this, mContext.getString(R.string.subCategory)).setChoices(mContext.getString(R.string.shop1), mContext.getString(R.string.shop2), mContext.getString(R.string.shop3)).setRequired(true))
                                        .addBranch(mContext.getString(R.string.health), new SingleFixedChoicePage(this, mContext.getString(R.string.subCategory)).setChoices(mContext.getString(R.string.health1), mContext.getString(R.string.health2)).setRequired(true))
                                        .addBranch(mContext.getString(R.string.ent), new SingleFixedChoicePage(this, mContext.getString(R.string.subCategory)).setChoices(mContext.getString(R.string.ent1), mContext.getString(R.string.ent2), mContext.getString(R.string.ent3), mContext.getString(R.string.ent4)).setRequired(true))
                                        .addBranch(mContext.getString(R.string.trans), new SingleFixedChoicePage(this, mContext.getString(R.string.subCategory)).setChoices(mContext.getString(R.string.trans1), mContext.getString(R.string.trans2), mContext.getString(R.string.trans3), mContext.getString(R.string.trans4)).setRequired(true))
                                        .addBranch(mContext.getString(R.string.exchange), new SingleFixedChoicePage(this, mContext.getString(R.string.subCategory)).setChoices(mContext.getString(R.string.exchange1)).setRequired(true))
                                        .addBranch(mContext.getString(R.string.install), new SingleFixedChoicePage(this, mContext.getString(R.string.subCategory)).setChoices(mContext.getString(R.string.install1)).setRequired(true))
                                        .addBranch(mContext.getString(R.string.debt), new SingleFixedChoicePage(this, mContext.getString(R.string.subCategory)).setChoices(mContext.getString(R.string.debt1), mContext.getString(R.string.debt2), mContext.getString(R.string.debt3)).setRequired(true))
                                        .addBranch(mContext.getString(R.string.custom_category_personal), new BalanceCustomInfoPage(this, mContext.getString(R.string.enter_category)).setRequired(true))
                        )
                        .addBranch(mContext.getString(R.string.tag_home), new BranchPage(this, mContext.getString(R.string.category))
                                        .addBranch(mContext.getString(R.string.rent_expense), new SingleFixedChoicePage(this, mContext.getString(R.string.subCategory)).setChoices(mContext.getString(R.string.rent_expense1)).setRequired(true))
                                        .addBranch(mContext.getString(R.string.bills), new SingleFixedChoicePage(this, mContext.getString(R.string.subCategory)).setChoices(mContext.getString(R.string.bills1), mContext.getString(R.string.bills2), mContext.getString(R.string.bills3), mContext.getString(R.string.bills4), mContext.getString(R.string.bills5), mContext.getString(R.string.bills6), mContext.getString(R.string.bills7)).setRequired(true))
                                        .addBranch(mContext.getString(R.string.pet), new SingleFixedChoicePage(this, mContext.getString(R.string.subCategory)).setChoices(mContext.getString(R.string.pet1), mContext.getString(R.string.pet2), mContext.getString(R.string.pet3), mContext.getString(R.string.pet4)).setRequired(true))
                                        .addBranch(mContext.getString(R.string.groceries), new SingleFixedChoicePage(this, mContext.getString(R.string.subCategory)).setChoices(mContext.getString(R.string.groceries1), mContext.getString(R.string.groceries2), mContext.getString(R.string.groceries3), mContext.getString(R.string.groceries4), mContext.getString(R.string.groceries5), mContext.getString(R.string.groceries6), mContext.getString(R.string.groceries7)).setRequired(true))
                                        .addBranch(mContext.getString(R.string.custom_category_home), new BalanceCustomInfoPage(this, mContext.getString(R.string.enter_category)).setRequired(true))
                        ).setRequired(true),
                new BalanceInfoPage(this, mContext.getString(R.string.details)).setRequired(true));
    }

}
