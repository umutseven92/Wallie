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

package wizard;

import android.content.Context;
import com.graviton.Cuzdan.R;
import wizard.model.*;

public class IncomeWizardModel extends AbstractWizardModel {
    public IncomeWizardModel(Context context) {
        super(context);
    }

    @Override
    protected PageList onNewRootPageList() {

        return new PageList(
                new BranchPage(this, mContext.getResources().getString(R.string.category))
                        .addBranch(mContext.getResources().getString(R.string.salary), new SingleFixedChoicePage(this, mContext.getResources().getString(R.string.subCategory)).setChoices(mContext.getResources().getString(R.string.salary1), mContext.getResources().getString(R.string.salary2)).setRequired(true))
                        .addBranch(mContext.getResources().getString(R.string.rent), new SingleFixedChoicePage(this, mContext.getResources().getString(R.string.subCategory)).setChoices(mContext.getResources().getString(R.string.rent1)).setRequired(true))
                        .addBranch(mContext.getResources().getString(R.string.allowance), new SingleFixedChoicePage(this, mContext.getResources().getString(R.string.subCategory)).setChoices(mContext.getResources().getString(R.string.allowance1)).setRequired(true))
                        .addBranch(mContext.getResources().getString(R.string.loan), new SingleFixedChoicePage(this, mContext.getResources().getString(R.string.subCategory)).setChoices(mContext.getResources().getString(R.string.loan1)).setRequired(true))
                        .addBranch(mContext.getResources().getString(R.string.interest), new SingleFixedChoicePage(this, mContext.getResources().getString(R.string.subCategory)).setChoices(mContext.getResources().getString(R.string.interest1), mContext.getResources().getString(R.string.interest2)).setRequired(true))
                        .addBranch(mContext.getResources().getString(R.string.games_of_chance), new SingleFixedChoicePage(this, mContext.getResources().getString(R.string.subCategory)).setChoices(mContext.getResources().getString(R.string.games1), mContext.getResources().getString(R.string.games2), mContext.getString(R.string.games3), mContext.getString(R.string.games4)).setRequired(true))
                        .addBranch(mContext.getResources().getString(R.string.receivables), new SingleFixedChoicePage(this, mContext.getResources().getString(R.string.subCategory)).setChoices(mContext.getResources().getString(R.string.receivables1)).setRequired(true))
                        .addBranch(mContext.getResources().getString(R.string.custom_category), new BalanceCustomInfoPage(this, mContext.getResources().getString(R.string.enter_category)).setRequired(true)).setRequired(true),
                new BalanceInfoPage(this, mContext.getResources().getString(R.string.details)).setRequired(true));
    }
}
