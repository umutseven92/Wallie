package Helpers;

import java.math.BigDecimal;
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
}
