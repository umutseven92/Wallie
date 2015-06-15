/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package wizard.model;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import com.graviton.Cuzdan.R;
import wizard.ui.BalanceInfoFragment;

import java.util.ArrayList;

public class BalanceInfoPage extends Page {
    public static final String AMOUNT_DATA_KEY = "amount";
    public static final String DESC_DATA_KEY = "description";
    private Context ctx;

    public BalanceInfoPage(ModelCallbacks callbacks, String title, Context context) {
        super(callbacks, title);
        ctx = context;
    }

    @Override
    public Fragment createFragment() {
        return BalanceInfoFragment.create(getKey());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        dest.add(new ReviewItem(ctx.getString(R.string.amount), mData.getString(AMOUNT_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem(ctx.getString(R.string.description), mData.getString(DESC_DATA_KEY), getKey(), -1));
    }

    @Override
    public boolean isCompleted() {
        return !TextUtils.isEmpty(mData.getString(AMOUNT_DATA_KEY));
    }
}
