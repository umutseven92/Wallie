package wizard.model;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import com.graviton.Cuzdan.R;
import wizard.ui.SavingInfoFragment;

import java.util.ArrayList;

/**
 * Created by Umut Seven on 11.2.2015, for Graviton.
 */
public class SavingInfoPage extends Page {

    public static final String AMOUNT_DATA_KEY = "amount";
    public static final String NAME_DATA_KEY = "name";
    public static final String REPEAT_DATA_KEY = "repeat";
    public static final String REPEAT_BOOL_KEY = "repeatBool";

    private Context ctx;

    public SavingInfoPage(ModelCallbacks callbacks, String title, Context context) {
        super(callbacks, title);
        ctx = context;
    }

    @Override
    public Fragment createFragment() {
        return SavingInfoFragment.create(getKey());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        dest.add(new ReviewItem(ctx.getString(R.string.amount), mData.getString(AMOUNT_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem(ctx.getString(R.string.savings_name), mData.getString(NAME_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem(ctx.getString(R.string.repeat), mData.getString(REPEAT_DATA_KEY), getKey(), -1));
    }

    @Override
    public boolean isCompleted() {
        return !TextUtils.isEmpty(mData.getString(AMOUNT_DATA_KEY)) && !TextUtils.isEmpty(mData.getString(NAME_DATA_KEY));
    }

}
