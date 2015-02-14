package wizard;

import android.content.Context;
import wizard.model.AbstractWizardModel;
import wizard.model.PageList;
import wizard.model.SavingInfoPage;
import wizard.model.SingleFixedChoicePage;

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
                new SingleFixedChoicePage(this, "Dönem").setChoices("Gün", "Hafta", "Ay", "3 Ay", "6 Ay", "1 Yıl", "Özel").setRequired(true),
                new SavingInfoPage(this, "Detaylar").setRequired(true)
        );
    }
}
