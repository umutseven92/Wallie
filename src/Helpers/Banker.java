package Helpers;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;

import java.util.Date;

/**
 * Created by Umut on 12.11.2014.
 */
public class Banker implements Serializable {

    public Banker(JSONArray incomes, JSONArray expenses) throws JSONException, ParseException {
        _incomes = new ArrayList<Income>();
        _expenses = new ArrayList<Expense>();
        LoadBalance(incomes, expenses);
    }

    private ArrayList<Income> _incomes;

    public ArrayList<Income> GetIncomes()
    {
        return _incomes;
    }

    public void SetIncomes(ArrayList<Income> value)
    {
        _incomes = value;
    }

    private ArrayList<Expense> _expenses;

    public ArrayList<Expense> GetExpenses()
    {
        return _expenses;
    }

    public void SetExpenses(ArrayList<Expense> value)
    {
        _expenses = value;
    }

    // This is where expenses & incomes are loaded from users save file
    public void LoadBalance(JSONArray jsonIncomes, JSONArray jsonExpenses) throws JSONException, ParseException {

        LoadExpenses(jsonExpenses);
        LoadIncomes(jsonIncomes);

    }

    public void LoadExpenses(JSONArray jsonBalance) throws JSONException, ParseException {
        for(int i = 0; i<jsonBalance.length(); i++)
        {
            Expense expense = new Expense(jsonBalance.getJSONObject(i));
            _expenses.add(expense);
        }
    }

    public void LoadIncomes(JSONArray jsonBalance) throws JSONException, ParseException {
        for(int i = 0; i<jsonBalance.length(); i++)
        {
            Income income = new Income(jsonBalance.getJSONObject(i));
            _incomes.add(income);
        }

    }

    public BigDecimal GetTotalDayIncome(Date date)
    {
        BigDecimal total = BigDecimal.ZERO;
        for (Income i : _incomes)
        {
            if(i.GetDate().getDay() == date.getDay())
            {
                total = total.add(i.GetAmount());
            }

        }
        return total;
    }

    public BigDecimal GetTotalMonthIncome(Date date)
    {
        BigDecimal total = BigDecimal.ZERO;
        for (Income i : _incomes)
        {
            if(i.GetDate().getMonth() == date.getMonth())
            {
                total = total.add(i.GetAmount());
            }

        }
        return total;
    }

    public BigDecimal GetTotalDayExpense(Date date)
    {
        BigDecimal total = BigDecimal.ZERO;
        for (Expense i : _expenses)
        {
            if(i.GetDate().getDay() == date.getDay())
            {
                total = total.add(i.GetAmount());
            }

        }

        return total;
    }

    public BigDecimal GetTotalMonthExpense(Date date)
    {
        BigDecimal total = BigDecimal.ZERO;
        for (Expense i : _expenses)
        {
            if(i.GetDate().getMonth() == date.getMonth())
            {
                total = total.add(i.GetAmount());
            }

        }

        return total;
    }

    public BigDecimal GetBalance(Date date, boolean day)
    {
        BigDecimal total = null;
        if(day)
        {
            BigDecimal totalIncome = GetTotalDayIncome(date);
            BigDecimal totalExpense = GetTotalDayExpense(date);
            total = totalIncome.subtract(totalExpense);
        }
        else
        {
            BigDecimal totalIncome = GetTotalMonthIncome(date);
            BigDecimal totalExpense = GetTotalMonthExpense(date);
            total = totalIncome.subtract(totalExpense);
        }

        return total;
    }

    public ArrayList<Income> GetIncomesFromDay(Date day)
    {
        ArrayList<Income> specIncomes = new ArrayList<Income>();

        for (int i = 0;i < _incomes.size(); i++)
        {
            Income incomeToTest = _incomes.get(i);
            if(incomeToTest.GetDate().getDay() == day.getDay())
            {
                specIncomes.add(_incomes.get(i));
            }
        }

        return specIncomes;
    }

    public ArrayList<Income> GetIncomesFromMonth(Date day)
    {
        ArrayList<Income> specIncomes = new ArrayList<Income>();

        for (int i = 0;i < _incomes.size(); i++)
        {
            Income incomeToTest = _incomes.get(i);
            if(incomeToTest.GetDate().getMonth() == day.getMonth())
            {
                specIncomes.add(_incomes.get(i));
            }
        }

        return specIncomes;
    }

    public ArrayList<Expense> GetExpensesFromDay(Date day)
    {
        ArrayList<Expense> specExpenses = new ArrayList<Expense>();

        for (int i = 0;i < _expenses.size(); i++)
        {
            Expense expenseToTest = _expenses.get(i);
            if(expenseToTest.GetDate().getDay() == day.getDay())
            {
                specExpenses.add(_expenses.get(i));
            }
        }

        return specExpenses;
    }

    public ArrayList<Expense> GetExpensesFromMonth(Date day)
    {
        ArrayList<Expense> specExpenses = new ArrayList<Expense>();

        for (int i = 0;i < _expenses.size(); i++)
        {
            Expense expenseToTest = _expenses.get(i);
            if(expenseToTest.GetDate().getMonth() == day.getMonth())
            {
                specExpenses.add(_expenses.get(i));
            }
        }

        return specExpenses;
    }
}
