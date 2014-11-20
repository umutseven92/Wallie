package Helpers;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Umut on 12.11.2014.
 */
public class Balance implements Serializable {


    private String _category;

    public String GetCategory()
    {
        return _category;
    }

    public void SetCategory(String value)
    {
        _category = value;
    }

    private String _subCategory;

    public String GetSubCategory()
    {
        return _subCategory;
    }

    public void SetSubCategory(String value)
    {
        _subCategory = value;
    }

    private String _description;

    public String GetDescription()
    {
        return _description;
    }

    public void SetDescription(String value)
    {
        _description = value;
    }

    private BigDecimal _amount;

    public BigDecimal GetAmount()
    {
        return _amount;
    }

    public void SetAmount(BigDecimal value)
    {
        _amount = value;
    }

    private Date _incomeDate;

    public Date GetIncomeDate()
    {
        return _incomeDate;
    }

    public void SetIncomeDate(Date incomeDate)
    {
        _incomeDate = incomeDate;
    }
}
