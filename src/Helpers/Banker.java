package Helpers;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Umut on 12.11.2014.
 */
public class Banker implements Serializable {

    public Banker()
    {
        _incomes = new ArrayList<Income>();
        _expenses = new ArrayList<Expense>();
    }

    public Banker(String loadLocation)
    {
        LoadBalance();
    }

    private List<Income> _incomes;

    public List<Income> GetIncomes()
    {
        return _incomes;
    }

    public void SetIncomes(List<Income> value)
    {
        _incomes = value;
    }

    private List<Expense> _expenses;

    public List<Expense> GetExpenses()
    {
        return _expenses;
    }

    public void SetExpenses(List<Expense> value)
    {
        _expenses = value;
    }

    public void LoadBalance()
    {
        // This is where expenses & incomes will be loaded from users save file
    }

    public void SaveBalance()
    {
        // This is where expenses & incomes will be saved to a location
    }

    public BigDecimal GetTotalIncome()
    {
        BigDecimal total = BigDecimal.ZERO;
        for (Income i : _incomes)
        {
           total = total.add(i.GetAmount());
        }

        return total;
    }

    public BigDecimal GetTotalExpense()
    {
        BigDecimal total = BigDecimal.ZERO;
        for (Expense i : _expenses)
        {
            total = total.add(i.GetAmount());
        }

        return total;
    }

    public BigDecimal GetBalance()
    {
        BigDecimal totalIncome = GetTotalIncome();
        BigDecimal totalExpense = GetTotalExpense();
        BigDecimal total = totalIncome.subtract(totalExpense);
        return total;
    }
}
