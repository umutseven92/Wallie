package Helpers;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;


/**
 * Created by Umut Seven on 12.11.2014, for Graviton.
 * <p/>
 * Gelir ve giderin ortak bilgilerinin oldugu class. En buyuk fark giderdeki TAG ozelligi.
 * Kolay aktarilabilmesi icin Serializable.
 */
public class Balance implements Serializable {

    private String _id;

    public void GenerateID() {
        _id = UUID.randomUUID().toString();
    }

    public void SetID(String id) {
        _id = id;
    }

    public String GetID() {
        return _id;
    }

    private String _category;

    public String GetCategory() {
        return _category;
    }

    public void SetCategory(String value) {
        _category = value;
    }

    private String _subCategory;

    public String GetSubCategory() {
        return _subCategory;
    }

    public void SetSubCategory(String value) {
        _subCategory = value;
    }

    private String _description;

    public String GetDescription() {
        return _description;
    }

    public void SetDescription(String value) {
        _description = value;
    }

    private BigDecimal _amount;

    public BigDecimal GetAmount() {
        return _amount.setScale(2, BigDecimal.ROUND_DOWN);
    }

    public void SetAmount(BigDecimal value) {
        _amount = value;
    }

    private Date _date;

    public Date GetDate() {
        return _date;
    }

    public void SetDate(Date date) {
        _date = date;
    }

}
