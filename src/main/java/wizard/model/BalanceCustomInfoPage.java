package wizard.model;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import com.graviton.Cuzdan.R;
import wizard.ui.BalanceCustomInfoFragment;

import java.util.ArrayList;

/**
 * Created by Umut Seven on 22.3.2015, for Graviton.
 */
public class BalanceCustomInfoPage extends Page {

    public static final String CUST_CAT_DATA_KEY = "cust";
    private Context ctx;

    public BalanceCustomInfoPage(ModelCallbacks callbacks, String title, Context context) {
        super(callbacks, title);
        ctx = context;
    }

    @Override
    public Fragment createFragment() {
        return BalanceCustomInfoFragment.create(getKey());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {

        dest.add(new ReviewItem(ctx.getString(R.string.custom_category), mData.getString(CUST_CAT_DATA_KEY), getKey(), -1));
    }

    @Override
    public boolean isCompleted() {
        return !TextUtils.isEmpty(mData.getString(CUST_CAT_DATA_KEY));
    }
}
