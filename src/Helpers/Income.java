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
public class Income extends Balance {

    public Income(String category, String subCategory, BigDecimal amount, String desc, Date incomeDate, Tags tag)
    {
        this.SetCategory(category);
        this.SetSubCategory(subCategory);
        this.SetAmount(amount);
        this.SetDescription(desc);
        this.SetDate(incomeDate);
        this.SetTag(tag);

    }

    public Income(JSONObject jsonIncome) throws JSONException, ParseException {
        JSONObject income = jsonIncome.getJSONObject("income");
        this.SetCategory(income.getString("category"));
        this.SetSubCategory(income.getString("subCategory"));
        this.SetAmount(new BigDecimal(income.getDouble("amount")));
        this.SetDescription(income.getString("desc"));
        Date d = new SimpleDateFormat("yyyy-d-MM").parse(income.getString("date"));
        this.SetDate(d);
        String tag = income.getString("category");
        if(tag == "personal")
        {
            this.SetTag(Tags.Personal);
        }
        else
        {
            this.SetTag(Tags.Home);
        }
    }
}
