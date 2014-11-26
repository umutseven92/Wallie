package Helpers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;

import java.util.List;

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
