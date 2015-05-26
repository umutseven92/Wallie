package Helpers;

import android.nfc.Tag;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by Umut Seven on 12.11.2014, for Graviton.
 */
public class Expense extends Balance {


    /**
     * JSON ustunden giderin yaratildigi yer.
     *
     * @param jsonExpense Bilgilerin oldugu JSONObject
     * @throws JSONException
     * @throws ParseException
     */
    public Expense(JSONObject jsonExpense) throws JSONException, ParseException {

        this.SetID(jsonExpense.getString("id"));
        this.SetCategory(jsonExpense.getString("category"));
        this.SetSubCategory(jsonExpense.getString("subCategory"));
        this.SetAmount(new BigDecimal(jsonExpense.getString("amount")));
        this.SetDescription(jsonExpense.getString("desc"));

        Date d = new SimpleDateFormat("yyyy-MM-d").parse(jsonExpense.getString("date"));
        this.SetDate(d);

        String tag = jsonExpense.getString("tag");
        if (tag.equals("personal")) {
            this.SetTag(Tags.Personal);
        } else {
            this.SetTag(Tags.Home);
        }
    }

    /**
     * Direk elden gelir yaratildigi yer.
     *
     * @param category    Kategori
     * @param subCategory Alt kategori
     * @param amount      Miktar
     * @param desc        Aciklama
     * @param date        Tarih
     */
    public Expense(String category, String subCategory, BigDecimal amount, String desc, Date date, Tags tag) {
        this.GenerateID();
        this.SetCategory(category);
        this.SetSubCategory(subCategory);
        this.SetAmount(amount);
        this.SetDescription(desc);
        this.SetDate(date);
        this.SetTag(tag);
    }

    public enum Tags {
        Home,
        Personal
    }

    private Tags _tag;

    /**
     * Tag'i JSON olarak kaydederken kullanilan string'i aldigimiz yer.
     *
     * @return Tag'in string hali
     */
    public String GetStringTag() {
        if (_tag == Tags.Home) {
            return "home";
        } else {
            return "personal";
        }
    }

    /**
     * Tag'i kullaniciya gosterirken kullandigimiz string'i aldigimiz yer.
     *
     * @return Turkce tag string'i
     */
    public String GetTurkishStringTag() {
        if (_tag == Tags.Home) {
            return "Ev";
        } else {
            return "Kişisel";
        }
    }

    public void SetTag(Tags tag) {
        _tag = tag;
    }

    public Tags GetTag()
    {
        return _tag;
    }
}
