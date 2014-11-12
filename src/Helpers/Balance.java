package Helpers;

import java.math.BigDecimal;

/**
 * Created by Umut on 12.11.2014.
 */
public class Balance {

    private String _category;

    public String getCategory()
    {
        return _category;
    }

    public void setCategory(String value)
    {
        _category = value;
    }

    private String _subCategory;

    public String getSubCategory()
    {
        return _subCategory;
    }

    public void setSubCategory(String value)
    {
        _subCategory = value;
    }

    private String _description;

    public String getDescription()
    {
        return _description;
    }

    public void setDescription(String value)
    {
        _description = value;
    }

    private BigDecimal _amount;

    public BigDecimal getAmount()
    {
        return _amount;
    }

    public void setAmount(BigDecimal value)
    {
        _amount = value;
    }
}
