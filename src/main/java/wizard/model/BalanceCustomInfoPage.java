package wizard.model;

import android.support.v4.app.Fragment;
import android.text.TextUtils;
import wizard.ui.BalanceCustomInfoFragment;

import java.util.ArrayList;

/**
 * Created by Umut Seven on 22.3.2015, for Graviton.
 */
public class BalanceCustomInfoPage extends Page {

    public static final String CUST_CAT_DATA_KEY = "cust";

    public BalanceCustomInfoPage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
    }

    @Override
    public Fragment createFragment() {
        return BalanceCustomInfoFragment.create(getKey());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        dest.add(new ReviewItem("Özel Kategori", mData.getString(CUST_CAT_DATA_KEY), getKey(), -1));
    }

    @Override
    public boolean isCompleted() {
        return !TextUtils.isEmpty(mData.getString(CUST_CAT_DATA_KEY));
    }
}
