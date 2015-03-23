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
import wizard.model.*;

public class IncomeWizardModel extends AbstractWizardModel {
    public IncomeWizardModel(Context context) {
        super(context);
    }

    @Override
    protected PageList onNewRootPageList() {
        return new PageList(
                new BranchPage(this, "Kategori")
                        .addBranch("Maaş", new SingleFixedChoicePage(this, "Alt Kategori").setChoices("Maaş Ödemesi", "Prim Ödemesi").setRequired(true))
                        .addBranch("Kira", new SingleFixedChoicePage(this, "Alt Kategori").setChoices("Kira Ödemesi").setRequired(true))
                        .addBranch("Harçlık", new SingleFixedChoicePage(this, "Alt Kategori").setChoices("Harçlık Ödemesi").setRequired(true))
                        .addBranch("Faiz Ödemesi", new SingleFixedChoicePage(this, "Alt Kategori").setChoices("Banka Faiz Ödemesi", "Borç Faiz Ödemesi").setRequired(true))
                        .addBranch("Şans Oyunları", new SingleFixedChoicePage(this, "Alt Kategori").setChoices("Piyango", "İddia", "Kumar Oyunları", "Diğer").setRequired(true))
                        .addBranch("Alacaklar", new SingleFixedChoicePage(this, "Alt Kategori").setChoices("Alacaklar Tahsilatı").setRequired(true))
                        .addBranch("Özel Kategori", new BalanceCustomInfoPage(this, "Kategori Girin").setRequired(true)).setRequired(true),
                new BalanceInfoPage(this, "Detaylar").setRequired(true));
    }
}
