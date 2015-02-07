package Helpers;

import android.app.Application;
import android.content.Context;
import com.graviton.Cuzdan.Global;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by Umut Seven on 12.11.2014, for Graviton.
 */
public class Banker implements Serializable {

    /**
     * @param incomes   Gelirin olduğu JSON arrayi
     * @param expenses  Giderlerin olduğu JSON arrayi
     * @param filePath  userConfig dosya yolu
     * @throws JSONException
     * @throws ParseException
     */
    public Banker(JSONArray incomes, JSONArray expenses, String filePath) throws JSONException, ParseException {
        _incomes = new ArrayList<Income>();
        _expenses = new ArrayList<Expense>();
        this.filePath = filePath;
        LoadBalance(incomes, expenses);
    }

    private ArrayList<Income> _incomes;

    private String filePath;

    public ArrayList<Income> GetIncomes() {
        return _incomes;
    }

    public void SetIncomes(ArrayList<Income> value) {
        _incomes = value;
    }

    private ArrayList<Expense> _expenses;

    public ArrayList<Expense> GetExpenses() {
        return _expenses;
    }

    public void SetExpenses(ArrayList<Expense> value) {
        _expenses = value;
    }

    private ArrayList<Saving> _savings;

    public void SetSavings(ArrayList<Saving> savings) {
        _savings = savings;
    }

    public ArrayList<Saving> GetSavings() {
        return _savings;
    }

    /**
     * Gelir ve giderlerin Banker listelerine yüklendiği yer
     *
     * @param jsonIncomes   Gelir JSON arrayi
     * @param jsonExpenses  Gider JSON arrayi
     * @throws JSONException
     * @throws ParseException
     */
    public void LoadBalance(JSONArray jsonIncomes, JSONArray jsonExpenses) throws JSONException, ParseException {
        LoadExpenses(jsonExpenses);
        LoadIncomes(jsonIncomes);
    }

    /**
     * JSON üstünden giderleri sıfırlanıp ArrayList'lere yüklendiği (veya yeniden yüklendiği) yer
     *
     * @param jsonBalance   Gider JSON arrayi
     * @throws JSONException
     * @throws ParseException
     */
    public void LoadExpenses(JSONArray jsonBalance) throws JSONException, ParseException {
        _expenses = new ArrayList<Expense>();
        for (int i = 0; i < jsonBalance.length(); i++) {
            Expense expense = new Expense(jsonBalance.getJSONObject(i));
            _expenses.add(expense);
        }
    }

    /**
     * JSON üstünden gelirlerin (yeniden) ArrayList'lere yüklendiği (veya yeniden yüklendiği) yer
     *
     * @param jsonBalance   Gelir JSON arrayi
     * @throws JSONException
     * @throws ParseException
     */
    public void LoadIncomes(JSONArray jsonBalance) throws JSONException, ParseException {
        _incomes = new ArrayList<Income>();
        for (int i = 0; i < jsonBalance.length(); i++) {
            Income income = new Income(jsonBalance.getJSONObject(i));
            _incomes.add(income);
        }

    }

    /**
     * Gelir ve giderleri direk kaynaktan yüklüyoruz
     *
     * @return  JSON olarak gelir ve giderler
     * @throws IOException
     * @throws JSONException
     */
    public JSONObject FetchBalanceData() throws IOException, JSONException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(this.filePath));
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
            sb.append("\n");
        }
        return new JSONObject(sb.toString());
    }

    /**
     * @return  Ana gelir JSONObjecti
     * @throws IOException
     * @throws JSONException
     */
    public JSONArray FetchIncomeData() throws IOException, JSONException {
        JSONObject jsonObject = FetchBalanceData();

        return jsonObject.getJSONObject("user").getJSONArray("incomes");
    }

    /**
     * @return  Ana gider JSONObjecti
     * @throws IOException
     * @throws JSONException
     */
    public JSONArray FetchExpenseData() throws IOException, JSONException {
        JSONObject jsonObject = FetchBalanceData();

        return jsonObject.getJSONObject("user").getJSONArray("expenses");
    }


    /**
     * Belli bir güne ait olan gelirlerin toplam miktarını hesaplıyoruz
     *
     * @param date  Toplamı istediğimiz gün
     * @return  Gelir miktar toplamı
     */
    public BigDecimal GetTotalDayIncome(Date date) {
        BigDecimal total = BigDecimal.ZERO;
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date);

        for (Income i : _incomes) {
            cal1.setTime(i.GetDate());
            boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                    cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);

            if (sameDay) {
                total = total.add(i.GetAmount());
            }

        }
        return total;
    }

    /**
     * Belli bir aya ait olan gelirlerin toplam miktarını hesaplıyoruz
     *
     * @param date Toplamı istediğimiz ay
     * @return  Gelir miktarının toplamı
     */
    public BigDecimal GetTotalMonthIncome(Date date) {
        BigDecimal total = BigDecimal.ZERO;
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date);

        for (Income i : _incomes) {
            cal1.setTime(i.GetDate());
            boolean sameMonth = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                    cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);

            if (sameMonth) {
                total = total.add(i.GetAmount());
            }

        }
        return total;
    }

    /**
     * Belli bir güne ait olan giderleri toplam miktarını hesaplıyoruz
     *
     * @param date  Toplamı istediğimiz gün
     * @return  Gider miktar toplamı
     */
    public BigDecimal GetTotalDayExpense(Date date) {
        BigDecimal total = BigDecimal.ZERO;
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date);

        for (Expense i : _expenses) {
            cal1.setTime(i.GetDate());
            boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                    cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);

            if (sameDay) {
                total = total.add(i.GetAmount());
            }

        }

        return total;
    }

    /**
     * Belli bir aya ait olan giderlerin toplam miktarını hesaplıyoruz
     *
     * @param date Toplamı istediğimiz ay
     * @return  Gider miktarının toplamı
     */
    public BigDecimal GetTotalMonthExpense(Date date) {
        BigDecimal total = BigDecimal.ZERO;
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date);

        for (Expense i : _expenses) {
            cal1.setTime(i.GetDate());
            boolean sameMonth = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                    cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);

            if (sameMonth) {
                total = total.add(i.GetAmount());
            }

        }

        return total;
    }

    /**
     * Belli bir tarihe ait bakiyeyi (gelir - gider) hesapliyoruz
     *
     * @param date  Istenen tarih
     * @param day   Gün bakiyesi için true, ay için false
     * @return  Bakiye miktarı
     */
    public BigDecimal GetBalance(Date date, boolean day) {
        BigDecimal totalIncome;
        BigDecimal totalExpense;

        if (day) {
            totalIncome = GetTotalDayIncome(date);
            totalExpense = GetTotalDayExpense(date);
        } else {
            totalIncome = GetTotalMonthIncome(date);
            totalExpense = GetTotalMonthExpense(date);
        }

        return totalIncome.subtract(totalExpense);
    }

    public ArrayList<Income> GetIncomesFromDay(Date day) throws JSONException, ParseException, IOException {
        LoadIncomes(FetchIncomeData());
        ArrayList<Income> specIncomes = new ArrayList<Income>();
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(day);

        for (Income incomeToTest : _incomes) {
            cal1.setTime(incomeToTest.GetDate());
            boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                    cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);

            if (sameDay) {
                specIncomes.add(incomeToTest);
            }
        }

        return specIncomes;
    }

    public ArrayList<Income> GetIncomesFromMonth(Date day) throws JSONException, ParseException, IOException {
        LoadIncomes(FetchIncomeData());
        ArrayList<Income> specIncomes = new ArrayList<Income>();
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(day);

        for (Income incomeToTest : _incomes) {
            cal1.setTime(incomeToTest.GetDate());
            boolean sameMonth = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                    cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);

            if (sameMonth) {
                specIncomes.add(incomeToTest);
            }
        }

        return specIncomes;
    }

    public ArrayList<Expense> GetExpensesFromDay(Date day) throws JSONException, ParseException, IOException {
        LoadExpenses(FetchExpenseData());
        ArrayList<Expense> specExpenses = new ArrayList<Expense>();
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(day);

        for (Expense expenseToTest : _expenses) {
            cal1.setTime(expenseToTest.GetDate());
            boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                    cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);

            if (sameDay) {
                specExpenses.add(expenseToTest);
            }
        }

        return specExpenses;
    }

    public ArrayList<Expense> GetExpensesFromMonth(Date day) throws JSONException, ParseException, IOException {
        LoadExpenses(FetchExpenseData());
        ArrayList<Expense> specExpenses = new ArrayList<Expense>();
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(day);

        for (Expense expenseToTest : _expenses) {
            cal1.setTime(expenseToTest.GetDate());
            boolean sameMonth = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                    cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);

            if (sameMonth) {
                specExpenses.add(expenseToTest);
            }
        }

        return specExpenses;
    }

    public JSONObject CreateJSONIncome(Income income) throws JSONException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(income.GetDate());
        String date = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH);

        String json = String.format("{\n" +
                "\t\t\t\t\t\"id\": \"%s\", \n" +
                "\t\t\t\t\t\"category\": \"%s\",\n" +
                "\t\t\t\t\t\"subCategory\": \"%s\",\n" +
                "\t\t\t\t\t\"amount\": \"%s\",\n" +
                "\t\t\t\t\t\"desc\": \"%s\",\n" +
                "\t\t\t\t\t\"date\": \"%s\"\n" +
                "\t\t\t\t}", income.GetID(), income.GetCategory(), income.GetSubCategory(), income.GetAmount(), income.GetDescription(), date);

        return new JSONObject(json);
    }

    public JSONObject CreateJSONExpense(Expense expense) throws JSONException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(expense.GetDate());
        String date = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH);

        String json = String.format("{\n" +
                "\t\t\t\t\t\"id\": \"%s\", \n" +
                "\t\t\t\t\t\"category\": \"%s\",\n" +
                "\t\t\t\t\t\"subCategory\": \"%s\",\n" +
                "\t\t\t\t\t\"amount\": \"%s\",\n" +
                "\t\t\t\t\t\"tag\": \"%s\",\n" +
                "\t\t\t\t\t\"desc\": \"%s\",\n" +
                "\t\t\t\t\t\"date\": \"%s\"\n" +
                "\t\t\t\t}", expense.GetID(), expense.GetCategory(), expense.GetSubCategory(), expense.GetAmount(), expense.GetStringTag(), expense.GetDescription(), date);

        return new JSONObject(json);

    }

    private String ReadUserInfo(Application app) {
        StringBuffer datax = new StringBuffer("");

        try {

            FileInputStream fIn = app.openFileInput(((Global) app).GetFilePath());
            InputStreamReader isr = new InputStreamReader(fIn);
            BufferedReader buffreader = new BufferedReader(isr);

            String readString = buffreader.readLine();
            while (readString != null) {
                datax.append(readString);
                readString = buffreader.readLine();
            }

            isr.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return datax.toString();
    }

    public void WriteUserInfo(Application app, String infoToWrite) throws IOException {
        FileOutputStream fileOutputStream = app.openFileOutput(((Global) app).GetFilePath(), Context.MODE_PRIVATE);
        fileOutputStream.write(infoToWrite.getBytes());
        fileOutputStream.close();

    }

    public void AddIncome(Income income, Application app) throws IOException, JSONException {

        JSONObject incomeToSave = CreateJSONIncome(income);

        String main = ReadUserInfo(app);

        JSONObject mainJSON = new JSONObject(main);
        JSONArray incomes = mainJSON.getJSONObject("user").getJSONArray("incomes");
        incomes.put(incomeToSave);
        WriteUserInfo(app, mainJSON.toString());
    }

    public void AddExpense(Expense expense, Application app) throws JSONException, IOException {

        JSONObject expenseToSave = CreateJSONExpense(expense);

        String main = ReadUserInfo(app);
        JSONObject mainJSON = new JSONObject(main);
        JSONArray expenses = mainJSON.getJSONObject("user").getJSONArray("expenses");
        expenses.put(expenseToSave);

        WriteUserInfo(app, mainJSON.toString());
    }


}
