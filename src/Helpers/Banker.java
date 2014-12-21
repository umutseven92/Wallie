package Helpers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
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

    public JSONObject GetIncomeJSON(Income income) throws JSONException {
        String incomeFormat = String.format("{\n" +
                        "\t\t\t\t\t\"category\": \"%s\",\n" +
                        "\t\t\t\t\t\"subCategory\": \"%s\",\n" +
                        "\t\t\t\t\t\"amount\": \"%s\",\n" +
                        "\t\t\t\t\t\"tag\": \"%s\",\n" +
                        "\t\t\t\t\t\"desc\": \"%s\",\n" +
                        "\t\t\t\t\t\"date\": \"%s\"\n" +
                        "\t\t\t\t}",income.GetCategory(),income.GetSubCategory(),income.GetAmount(),income.GetStringTag(),income.GetDescription(),income.GetDate());

        JSONObject incomeJSON = new JSONObject(incomeFormat);
        return incomeJSON;
    }

    public BigDecimal GetTotalDayIncome(Date date)
    {
        BigDecimal total = BigDecimal.ZERO;
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date);

        for (Income i : _incomes)
        {
            cal1.setTime(i.GetDate());
            boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                    cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);

            if(sameDay)
            {
                total = total.add(i.GetAmount());
            }

        }
        return total;
    }

    public BigDecimal GetTotalMonthIncome(Date date)
    {
        BigDecimal total = BigDecimal.ZERO;
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date);

        for (Income i : _incomes)
        {
            cal1.setTime(i.GetDate());
            boolean sameMonth = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                    cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);

            if(sameMonth)
            {
                total = total.add(i.GetAmount());
            }

        }
        return total;
    }

    public BigDecimal GetTotalDayExpense(Date date)
    {
        BigDecimal total = BigDecimal.ZERO;
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date);

        for (Expense i : _expenses)
        {
            cal1.setTime(i.GetDate());
            boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                    cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);

            if(sameDay)
            {
                total = total.add(i.GetAmount());
            }

        }

        return total;
    }

    public BigDecimal GetTotalMonthExpense(Date date)
    {
        BigDecimal total = BigDecimal.ZERO;
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date);

        for (Expense i : _expenses)
        {
            cal1.setTime(i.GetDate());
            boolean sameMonth = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                    cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);

            if(sameMonth)
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
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(day);

        for (int i = 0;i < _incomes.size(); i++)
        {
            Income incomeToTest = _incomes.get(i);
            cal1.setTime(incomeToTest.GetDate());
            boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                    cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);

            if(sameDay)
            {
                specIncomes.add(_incomes.get(i));
            }
        }

        return specIncomes;
    }

    public ArrayList<Income> GetIncomesFromMonth(Date day)
    {
        ArrayList<Income> specIncomes = new ArrayList<Income>();
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(day);

        for (int i = 0;i < _incomes.size(); i++)
        {
            Income incomeToTest = _incomes.get(i);
            cal1.setTime(incomeToTest.GetDate());
            boolean sameMonth = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                    cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);

            if(sameMonth)
            {
                specIncomes.add(_incomes.get(i));
            }
        }

        return specIncomes;
    }

    public ArrayList<Expense> GetExpensesFromDay(Date day)
    {
        ArrayList<Expense> specExpenses = new ArrayList<Expense>();
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(day);

        for (int i = 0;i < _expenses.size(); i++)
        {
            Expense expenseToTest = _expenses.get(i);
            cal1.setTime(expenseToTest.GetDate());
            boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                    cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);

            if(sameDay)
            {
                specExpenses.add(_expenses.get(i));
            }
        }

        return specExpenses;
    }

    public ArrayList<Expense> GetExpensesFromMonth(Date day)
    {
        ArrayList<Expense> specExpenses = new ArrayList<Expense>();
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(day);

        for (int i = 0;i < _expenses.size(); i++)
        {
            Expense expenseToTest = _expenses.get(i);
            cal1.setTime(expenseToTest.GetDate());
            boolean sameMonth = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                    cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);

            if(sameMonth)
            {
                specExpenses.add(_expenses.get(i));
            }
        }

        return specExpenses;
    }
}
