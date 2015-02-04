package Helpers;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by Umut Seven on 12.11.2014, for Graviton.
 */
public class Income extends Balance {

    // Create income via JSON
    public Income(JSONObject jsonIncome) throws JSONException, ParseException {

        this.SetID(jsonIncome.getString("id"));
        this.SetCategory(jsonIncome.getString("category"));
        this.SetSubCategory(jsonIncome.getString("subCategory"));
        this.SetAmount(new BigDecimal(jsonIncome.getDouble("amount")));
        this.SetDescription(jsonIncome.getString("desc"));
        Date d = new SimpleDateFormat("yyyy-MM-d").parse(jsonIncome.getString("date"));
        this.SetDate(d);
    }

    // Create income manually
    public Income(String category, String subCategory, BigDecimal amount, String desc, Date date) {
        this.GenerateID();
        this.SetCategory(category);
        this.SetSubCategory(subCategory);
        this.SetAmount(amount);
        this.SetDescription(desc);
        this.SetDate(date);

    }

}
