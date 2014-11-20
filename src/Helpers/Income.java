package Helpers;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Umut on 12.11.2014.
 */
public class Income extends Balance {

    public Income(String category, String subCategory, BigDecimal amount, String desc, Date incomeDate)
    {
        this.SetCategory(category);
        this.SetSubCategory(subCategory);
        this.SetAmount(amount);
        this.SetDescription(desc);
        this.SetIncomeDate(incomeDate);
    }
}
