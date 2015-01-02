package Helpers;

import org.json.JSONException;
import org.json.JSONObject;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Umut on 12.11.2014.
 */
public class Expense extends Balance {

    public Expense(JSONObject jsonExpense) throws JSONException, ParseException {

        this.SetCategory(jsonExpense.getString("category"));
        this.SetSubCategory(jsonExpense.getString("subCategory"));
        this.SetAmount(new BigDecimal(jsonExpense.getDouble("amount")));
        this.SetDescription(jsonExpense.getString("desc"));

        Date d = new SimpleDateFormat("yyyy-MM-d").parse(jsonExpense.getString("date"));
        this.SetDate(d);

        String tag = jsonExpense.getString("category");
        if(tag == "personal")
        {
            this.SetTag(Tags.Personal);
        }
        else
        {
            this.SetTag(Tags.Home);
        }
    }

    public Expense(String category, String subCategory, BigDecimal amount, String desc, Date date, Tags tag)
    {
        this.SetCategory(category);
        this.SetSubCategory(subCategory);
        this.SetAmount(amount);
        this.SetDescription(desc);
        this.SetDate(date);
        this.SetTag(tag);
    }
}
