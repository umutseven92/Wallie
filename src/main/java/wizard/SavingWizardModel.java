package wizard;

import android.content.Context;
import com.graviton.Cuzdan.R;
import wizard.model.*;

/**
 * Created by Umut Seven on 11.2.2015, for Graviton.
 */
public class SavingWizardModel extends AbstractWizardModel {

    public SavingWizardModel(Context context) {
        super(context);
    }

    @Override
    protected PageList onNewRootPageList() {
        return new PageList(
                new BranchPage(this, mContext.getString(R.string.savings_period))
                        .addBranch(mContext.getString(R.string.week), new SavingInfoPage(this, mContext.getString(R.string.details), mContext).setRequired(true))
                        .addBranch(mContext.getString(R.string.month), new SavingInfoPage(this, mContext.getString(R.string.details), mContext).setRequired(true))
                        .addBranch(mContext.getString(R.string.three_months), new SavingInfoPage(this, mContext.getString(R.string.details), mContext).setRequired(true))
                        .addBranch(mContext.getString(R.string.six_months), new SavingInfoPage(this, mContext.getString(R.string.details), mContext).setRequired(true))
                        .addBranch(mContext.getString(R.string.year), new SavingInfoPage(this, mContext.getString(R.string.details), mContext).setRequired(true))
                        .addBranch(mContext.getString(R.string.custom), new SavingCustomInfoPage(this, mContext.getString(R.string.details), mContext).setRequired(true)).setRequired(true)
        );
    }
}
