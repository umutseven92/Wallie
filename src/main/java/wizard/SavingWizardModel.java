package wizard;

import android.content.Context;
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
       /*
        return new PageList(
                new SingleFixedChoicePage(this, "Dönem").setChoices("Hafta", "Ay", "3 Ay", "6 Ay", "1 Yıl", "Özel").setRequired(true),
                new SavingInfoPage(this, "Detaylar").setRequired(true)
        ); */
        return new PageList(
            new BranchPage(this, "Dönem")
                    .addBranch("Hafta", new SavingInfoPage(this, "Detaylar").setRequired(true))
                    .addBranch("Ay", new SavingInfoPage(this, "Detaylar").setRequired(true))
                    .addBranch("3 Ay", new SavingInfoPage(this, "Detaylar").setRequired(true))
                    .addBranch("6 Ay", new SavingInfoPage(this, "Detaylar").setRequired(true))
                    .addBranch("1 Yıl", new SavingInfoPage(this, "Detaylar").setRequired(true))
                    .addBranch("Özel", new SavingCustomInfoPage(this, "Detaylar").setRequired(true))
        );
    }
}
