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
import android.util.SparseArray;
import com.graviton.Cuzdan.Global;
import com.graviton.Cuzdan.R;
import wizard.model.*;

import java.util.ArrayList;

public class IncomeWizardModel extends AbstractWizardModel {
    public IncomeWizardModel(Context context) {
        super(context);
    }

    @Override
    protected PageList onNewRootPageList() {

        BranchPage bPage = new BranchPage(this, mContext.getString(R.string.category));
        bPage.setRequired(true);

        SparseArray<String> incomes = ((Global) mContext.getApplicationContext()).recordsHelper.ReturnIncomes();

        SparseArray<String> incomeCategories = new SparseArray<String>();

        // Ana kategorileri al
        for (int i = 0; i < incomes.size(); i++) {
            if (Integer.toString(incomes.keyAt(i)).toCharArray().length == 3) {
                incomeCategories.put(incomes.keyAt(i), incomes.valueAt(i));
            }
        }

        // BranchPage'i olustur
        for (int i = 0; i < incomeCategories.size(); i++) {
            ArrayList<String> subCategories = new ArrayList<String>();

            for (int j = 0; j < incomes.size(); j++) {
                if (Integer.toString(incomes.keyAt(j)).toCharArray().length > 3 && Integer.toString(incomes.keyAt(j)).startsWith(Integer.toString(incomeCategories.keyAt(i)))) {
                    subCategories.add(incomes.valueAt(j));
                }
            }

            if (incomeCategories.valueAt(i).equals(mContext.getString(R.string.custom_category))) {
                bPage.addBranch(incomeCategories.valueAt(i), new BalanceCustomInfoPage(this, mContext.getString(R.string.enter_category), mContext).setRequired(true));
            } else {
                bPage.addBranch(incomeCategories.valueAt(i), new SingleFixedChoicePage(this, mContext.getString(R.string.subCategory)).setChoices(subCategories.toArray(new String[subCategories.size()])).setRequired(true));
            }
        }

        return new PageList(bPage, new BalanceInfoPage(this, mContext.getString(R.string.details), mContext).setRequired(true));
    }
}
