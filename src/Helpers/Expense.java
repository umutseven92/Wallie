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

    public Expense(String category, String subCategory, BigDecimal amount, String desc, Date expenseDate, Tags tag)
    {
        this.SetCategory(category);
        this.SetSubCategory(subCategory);
        this.SetAmount(amount);
        this.SetDescription(desc);
        this.SetDate(expenseDate);
        this.SetTag(tag);
    }

    public Expense(JSONObject jsonExpense) throws JSONException, ParseException {

        JSONObject expense = jsonExpense;
        this.SetCategory(expense.getString("category"));
        this.SetSubCategory(expense.getString("subCategory"));
        this.SetAmount(new BigDecimal(expense.getDouble("amount")));
        this.SetDescription(expense.getString("desc"));

        Date d = new SimpleDateFormat("yyyy-d-MM").parse(expense.getString("date"));
        this.SetDate(d);

        String tag = expense.getString("category");
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
