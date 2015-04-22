package wizard;

import android.content.Context;
import wizard.model.*;


/**
 * Created by Umut Seven on 2.1.2015, for Graviton.
 */
public class ExpenseWizardModel extends AbstractWizardModel {
    public ExpenseWizardModel(Context context) {
        super(context);
    }

    @Override
    protected PageList onNewRootPageList() {
        return new PageList(
                new BranchPage(this, "Gider Türü")
                        .addBranch("Kişisel", new BranchPage(this, "Kategori")
                                        .addBranch("Yiyecek & İçecek", new SingleFixedChoicePage(this, "Alt Kategori").setChoices("Alkollü İçecek", "Gıda & Alkolsüz İçecek").setRequired(true))
                                        .addBranch("Sigara & Tütün", new SingleFixedChoicePage(this, "Alt Kategori").setChoices("Sigara", "Tütün").setRequired(true))
                                        .addBranch("Alışveriş", new SingleFixedChoicePage(this, "Alt Kategori").setChoices("Giyim & Ayakkabı", "Teknolojik Aletler", "Diğer").setRequired(true))
                                        .addBranch("Sağlık", new SingleFixedChoicePage(this, "Alt Kategori").setChoices("Hastane Masrafları", "İlaç").setRequired(true))
                                        .addBranch("Eğlence & Sosyal Aktiviteler", new SingleFixedChoicePage(this, "Alt Kategori").setChoices("Gazete, Dergi & Kitap", "Oyun", "Sinema, Tiyatro & Konser", "Diğer").setRequired(true))
                                        .addBranch("Ulaşım", new SingleFixedChoicePage(this, "Alt Kategori").setChoices("Akbil", "Yakıt", "Diğer").setRequired(true))
                                        .addBranch("Döviz", new SingleFixedChoicePage(this, "Alt Kategori").setChoices("Döviz Alımı").setRequired(true))
                                        .addBranch("Taksit", new SingleFixedChoicePage(this, "Alt Kategori").setChoices("Taksit Ödemesi").setRequired(true))
                                        .addBranch("Borçlar", new SingleFixedChoicePage(this, "Alt Kategori").setChoices("Borç Ödemesi", "Banka Faiz Borcu", "Diğer").setRequired(true))
                                        .addBranch("Özel Kategori (Kişisel)", new BalanceCustomInfoPage(this, "Kategori Girin").setRequired(true))
                        )
                        .addBranch("Ev", new BranchPage(this, "Kategori")
                                        .addBranch("Kira", new SingleFixedChoicePage(this, "Alt Kategori").setChoices("Ev Kirası").setRequired(true))
                                        .addBranch("Faturalar", new SingleFixedChoicePage(this, "Alt Kategori").setChoices("Elektrik", "Su", "Doğalgaz", "Telefon", "İnternet", "Kanal Abonelikleri", "Diğer").setRequired(true))
                                        .addBranch("Evcil Hayvan", new SingleFixedChoicePage(this, "Alt Kategori").setChoices("Evcil Hayvan", "Veteriner", "Mama", "Diğer").setRequired(true))
                                        .addBranch("Alışveriş", new SingleFixedChoicePage(this, "Alt Kategori").setChoices("Temizlik Malzemeleri", "Yiyecek & Alkolsüz İçecek", "Alkollü İçecek", "Sigara & Tütün", "Beyaz Eşya", "Mobilya", "Diğer").setRequired(true))
                                        .addBranch("Özel Kategori (Ev)", new BalanceCustomInfoPage(this, "Kategori Girin").setRequired(true))
                        ).setRequired(true),
                new BalanceInfoPage(this, "Detaylar").setRequired(true));
    }

}
