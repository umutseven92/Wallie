package wizard.model;

import android.support.v4.app.Fragment;
import android.text.TextUtils;
import wizard.ui.SavingCustomInfoFragment;

import java.util.ArrayList;

/**
 * Created by Umut Seven on 2.3.2015, for Graviton.
 */
public class SavingCustomInfoPage extends Page {

    public static final String AMOUNT_DATA_KEY = "amount";
    public static final String NAME_DATA_KEY = "name";
    public static final String REPEAT_DATA_KEY = "repeat";
    public static final String REPEAT_BOOL_KEY = "repeatBool";
    public static final String CUSTOM_DAY_KEY = "customDay";

    public SavingCustomInfoPage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
    }

    @Override
    public Fragment createFragment() {
        return SavingCustomInfoFragment.create(getKey());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        dest.add(new ReviewItem("Miktar", mData.getString(AMOUNT_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("İsim", mData.getString(NAME_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("Tekrarla", mData.getString(REPEAT_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("Gün Sayısı", mData.getString(CUSTOM_DAY_KEY), getKey(), -1));
    }

    @Override
    public boolean isCompleted() {
        return !TextUtils.isEmpty(mData.getString(AMOUNT_DATA_KEY)) && !TextUtils.isEmpty(mData.getString(NAME_DATA_KEY)) && !TextUtils.isEmpty(mData.getString(CUSTOM_DAY_KEY));
    }
}
