package Helpers;

import Helpers.Saving.Period;
import android.app.Application;
import android.content.Context;
import android.os.Environment;
import com.graviton.Cuzdan.Global;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Umut Seven on 12.11.2014, for Graviton.
 * <p/>
 * Tum gelirlerin ve giderlerin yonetildigi class.
 */
public class Banker implements Serializable {

    /**
     * @param incomes  Gelirin oldugu JSON arrayi
     * @param expenses Giderlerin oldugu JSON arrayi
     * @param filePath userConfig dosya yolu
     * @throws JSONException
     * @throws ParseException
     */
    public Banker(JSONArray incomes, JSONArray expenses, JSONArray savings, JSONArray incomeCustoms, JSONArray expenseCustoms, String filePath, Application app, String currency) throws Exception {
        _incomes = new ArrayList<Income>();
        _expenses = new ArrayList<Expense>();
        _savings = new ArrayList<Saving>();
        _incomeCustoms = new ArrayList<String>();
        _expenseCustoms = new ArrayList<String>();
        mainApp = app;
        _currency = currency;
        this.filePath = filePath;
        LoadBalance(incomes, expenses);
        LoadSavings(savings);
        LoadCustoms(incomeCustoms, expenseCustoms);
    }

    private Application mainApp;

    private ArrayList<Income> _incomes;

    private String filePath;

    private String _currency;

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


    private ArrayList<String> _expenseCustoms;

    public void SetExpenseCustoms(ArrayList<String> customs) {
        _expenseCustoms = customs;
    }

    public ArrayList<String> GetExpenseCustoms() throws IOException, JSONException {
        LoadCustoms(FetchIncomeCustomsData(), FetchExpenseCustomsData());
        return _expenseCustoms;
    }

    private ArrayList<String> _incomeCustoms;

    public void SetIncomeCustoms(ArrayList<String> customs) {
        _incomeCustoms = customs;
    }

    public ArrayList<String> GetIncomeCustoms() throws IOException, JSONException {
        LoadCustoms(FetchIncomeCustomsData(), FetchExpenseCustomsData());
        return _incomeCustoms;
    }

    private ArrayList<Saving> _savings;

    public void SetSavings(ArrayList<Saving> savings) {
        _savings = savings;
    }

    public ArrayList<Saving> GetSavings() throws Exception {
        LoadSavings(FetchSavingsData());
        return _savings;
    }

    public int GetSavingsCount() throws Exception {
        return GetSavings().size();
    }

    public Period GetPeriodFromTurkishString(String turkishPeriod) {
        Period per = null;

        if (turkishPeriod.equals("Gün")) {
            per = Period.Day;
        } else if (turkishPeriod.equals("Hafta")) {
            per = Period.Week;
        } else if (turkishPeriod.equals("Ay")) {
            per = Period.Month;
        } else if (turkishPeriod.equals("3 Ay")) {
            per = Period.ThreeMonths;
        } else if (turkishPeriod.equals("6 Ay")) {
            per = Period.SixMonths;
        } else if (turkishPeriod.equals("1 Yıl")) {
            per = Period.Year;
        } else if (turkishPeriod.equals("Özel")) {
            per = Period.Custom;
        }

        assert per != null;

        return per;
    }

    private String GetPeriodString(Period period) {
        String periodRep = "";

        switch (period) {
            case Day:
                periodRep = "day";
                break;
            case Week:
                periodRep = "week";
                break;
            case Month:
                periodRep = "month";
                break;
            case ThreeMonths:
                periodRep = "three_months";
                break;
            case SixMonths:
                periodRep = "six_months";
                break;
            case Year:
                periodRep = "year";
                break;
            case Custom:
                periodRep = "custom";
                break;
        }
        return periodRep;
    }


    /**
     * Birikimi alip uygun JSONa dokuyoruz.
     *
     * @param saving JSON olacak birikim
     * @return JSON olarak birikim
     * @throws JSONException
     */
    public JSONObject CreateJSONSaving(Saving saving) throws JSONException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(saving.GetDate());
        String date = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH);

        String json = String.format("{\n" +
                "\t\t\t\t\t\"id\": \"%s\", \n" +
                "\t\t\t\t\t\"name\": \"%s\",\n" +
                "\t\t\t\t\t\"amount\": \"%s\",\n" +
                "\t\t\t\t\t\"period\": \"%s\",\n" +
                "\t\t\t\t\t\"date\": \"%s\",\n" +
                "\t\t\t\t\t\"customDays\": \"%s\",\n" +
                "\t\t\t\t\t\"repeating\": \"%s\"\n" +
                "\t\t\t\t}", saving.GetID(), saving.GetName(), saving.GetAmount(), GetPeriodString(saving.GetPeriod()), date, saving.GetCustomDays(), saving.GetRepeating());

        return new JSONObject(json);
    }

    public void LoadCustoms(JSONArray incomeCustom, JSONArray expenseCustom) throws JSONException {
        _incomeCustoms = new ArrayList<String>();
        _expenseCustoms = new ArrayList<String>();

        for (int i = 0; i < incomeCustom.length(); i++) {
            _incomeCustoms.add(incomeCustom.getString(i));
        }

        for (int i = 0; i < expenseCustom.length(); i++) {
            _expenseCustoms.add(expenseCustom.getString(i));
        }
    }

    /**
     * Gelir ve giderlerin Banker listelerine yuklendigi yer.
     *
     * @param jsonIncomes  Gelir JSON arrayi
     * @param jsonExpenses Gider JSON arrayi
     * @throws JSONException
     * @throws ParseException
     */
    public void LoadBalance(JSONArray jsonIncomes, JSONArray jsonExpenses) throws JSONException, ParseException {
        LoadExpenses(jsonExpenses);
        LoadIncomes(jsonIncomes);
    }

    /**
     * JSON ustunden giderlerin sifirlanip ArrayList'e yeniden yuklendiqi yer.
     *
     * @param jsonBalance Gider JSON arrayi
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
     * JSON ustunden birikimlerin sifirlanip ArrayList'e yeniden yuklendigi yer.
     *
     * @param jsonSavings Birikim JSON arrayi
     * @throws JSONException
     */
    public void LoadSavings(JSONArray jsonSavings) throws Exception {
        _savings = new ArrayList<Saving>();
        for (int i = 0; i < jsonSavings.length(); i++) {
            Saving saving = new Saving(jsonSavings.getJSONObject(i), _currency);
            _savings.add(saving);
        }
        CheckSavings();
    }

    /**
     * JSON ustunden gelirlerin sifirlanip ArrayList'e yeniden yuklendiqi yer.
     *
     * @param jsonBalance Gelir JSON arrayi
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

    public JSONArray FetchSavingsData() throws JSONException, IOException {
        JSONObject jsonObject = FetchUserData();

        return jsonObject.getJSONObject("user").getJSONArray("savings");
    }

    /**
     * Kullanici bilgilerini direk kaynaktan yukluyoruz.
     *
     * @return JSON olarak kullanici bilgileri
     * @throws IOException
     * @throws JSONException
     */
    public JSONObject FetchUserData() throws IOException, JSONException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(this.filePath));
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
            sb.append("\n");
        }
        return new JSONObject(sb.toString());
    }

    public JSONArray FetchIncomeData() throws IOException, JSONException {
        JSONObject jsonObject = FetchUserData();

        return jsonObject.getJSONObject("user").getJSONArray("incomes");
    }

    public JSONArray FetchExpenseData() throws IOException, JSONException {
        JSONObject jsonObject = FetchUserData();

        return jsonObject.getJSONObject("user").getJSONArray("expenses");
    }

    public JSONArray FetchIncomeCustomsData() throws IOException, JSONException {
        JSONObject jsonObject = FetchUserData();
        return jsonObject.getJSONObject("user").getJSONArray("incomeCustoms");
    }

    public JSONArray FetchExpenseCustomsData() throws IOException, JSONException {
        JSONObject jsonObject = FetchUserData();
        return jsonObject.getJSONObject("user").getJSONArray("expenseCustoms");
    }

    /**
     * Belli bir güne ait olan gelirlerin toplam miktarini hesapliyoruz.
     *
     * @param date Toplami istedigimiz gun
     * @return Gelir miktar toplami
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
     * Belli bir aya ait olan gelirlerin toplam miktarini hesapliyoruz.
     *
     * @param date Toplami istedigimiz ay
     * @return Gelir miktarinin toplami
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
     * Belli bir güne ait olan giderleri toplam miktarini hesapliyoruz
     *
     * @param date Toplami istedigimiz gün
     * @return Gider miktar toplami
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
     * Belli bir aya ait olan giderlerin toplam miktarini hesapliyoruz
     *
     * @param date Toplami istedigimiz ay
     * @return Gider miktarinin toplami
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
     * @param date Istenen tarih
     * @param day  Gün bakiyesi icin true, ay icin false
     * @return Bakiye miktari
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


    /**
     * Belli bir gune air gelirleri (kaynaktan) dondurur.
     *
     * @param day Gelirlerin istendigi gun
     * @return Gune ait gelirler
     * @throws JSONException
     * @throws ParseException
     * @throws IOException
     */
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

    /**
     * Belli bir aya air gelirleri (kaynaktan) dondurur.
     *
     * @param day Gelirlerin istendigi ay
     * @return Aya ait gelirler
     * @throws JSONException
     * @throws ParseException
     * @throws IOException
     */
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

    /**
     * Belli bir gune air giderleri (kaynaktan) dondurur.
     *
     * @param day Giderlerin istendigi gun
     * @return Gune ait giderler
     * @throws JSONException
     * @throws ParseException
     * @throws IOException
     */
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

    /**
     * Belli bir aya air giderleri (kaynaktan) dondurur.
     *
     * @param day Giderlerin istendigi ay
     * @return Aya ait giderler
     * @throws JSONException
     * @throws ParseException
     * @throws IOException
     */
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

    /**
     * Gelir alip JSON haline cevirir.
     *
     * @param income JSON hali istenen gelir
     * @return JSON halinde gelir
     * @throws JSONException
     */
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

    /**
     * Gider alip JSON haline cevirir.
     *
     * @param expense JSON hali istenen gider
     * @return JSON halinde gider
     * @throws JSONException
     */
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


    /**
     * /data/com/graviton/Cuzdan'daki kullanici bilgilerini dondurur.
     *
     * @return string halinde kullanici bilgileri
     */
    private String ReadUserInfo() {
        StringBuffer datax = new StringBuffer("");

        try {

            FileInputStream fIn = mainApp.openFileInput(((Global) mainApp).GetFilePath());
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

    /**
     * Verilen stringi /data/com/graviton/Cuzdan'a yazar.
     *
     * @param infoToWrite Yazilmasi istenen veri
     * @throws java.io.IOException
     */
    public void WriteUserInfo(String infoToWrite) throws IOException {
        FileOutputStream fileOutputStream = mainApp.openFileOutput(((Global) mainApp).GetFilePath(), Context.MODE_PRIVATE);
        fileOutputStream.write(infoToWrite.getBytes());
        fileOutputStream.close();

    }


    public String BackupUserData() throws JSONException, IOException {
        String mainInfo = ReadUserInfo();

        String main = android.util.Base64.encodeToString(mainInfo.getBytes(), android.util.Base64.DEFAULT);

        String state = android.os.Environment.getExternalStorageState();

        if (android.os.Environment.MEDIA_MOUNTED.equals(state)) {
            File file = new File(Environment.getExternalStorageDirectory() + "/Documents");
            boolean isPresent = true;

            if (!file.exists()) {
                isPresent = file.mkdir();
            }

            if (isPresent) {
                File backupFile = new File(file.getAbsolutePath(), "cuzdanBackup");

                FileOutputStream fileOutputStream = new FileOutputStream(backupFile);
                fileOutputStream.write(main.getBytes());
                fileOutputStream.close();

                return "SUCCESS";

            }

        }

        return "FAILURE";
    }

    public void ToggleStatusNotifications() throws JSONException, IOException {
        String main = ReadUserInfo();
        JSONObject mainJSON = new JSONObject(main);

        if (mainJSON.getJSONObject("user").getString("statusNot").equals("true")) {
            main = main.replaceFirst("\"statusNot\":\"true\"", "\"statusNot\":\"false\"");
        } else if (mainJSON.getJSONObject("user").getString("statusNot").equals("false")) {
            main = main.replaceFirst("\"statusNot\":\"false\"", "\"statusNot\":\"true\"");
        }
        JSONObject jsonToWrite = new JSONObject(main);
        WriteUserInfo(jsonToWrite.toString());
    }

    public void ToggleNotifications() throws JSONException, IOException {
        String main = ReadUserInfo();
        JSONObject mainJSON = new JSONObject(main);
        if (mainJSON.getJSONObject("user").getString("notifications").equals("true")) {
            main = main.replaceFirst("\"notifications\":\"true\"", "\"notifications\":\"false\"");
        } else if (mainJSON.getJSONObject("user").getString("notifications").equals("false")) {
            main = main.replaceFirst("\"notifications\":\"false\"", "\"notifications\":\"true\"");
        }
        JSONObject jsonToWrite = new JSONObject(main);
        WriteUserInfo(jsonToWrite.toString());
    }

    public void ToggleVersion() throws JSONException, IOException {
        String main = ReadUserInfo();
        JSONObject mainJson = new JSONObject(main);
        if (mainJson.getJSONObject("user").getString("pro").equals("true")) {
            main = main.replaceFirst("\"pro\":\"false\"", "\"pro\":\"true\"");
        } else if (mainJson.getJSONObject("user").getString("pro").equals("false")) {
            main = main.replaceFirst("\"pro\":\"false\"", "\"pro\":\"true\"");
        }
        JSONObject jsonToWrite = new JSONObject(main);
        WriteUserInfo(jsonToWrite.toString());
    }

    public void UpdateNotification(int newHour) throws JSONException, IOException {
        String main = ReadUserInfo();
        JSONObject mainJSON = new JSONObject(main);

        main = main.replaceFirst("\"savNotHour\":\"" + mainJSON.getJSONObject("user").getString("savNotHour") + "\"", "\"savNotHour\":\"" + newHour + "\"");
        JSONObject jsonToWrite = new JSONObject(main);
        WriteUserInfo(jsonToWrite.toString());
    }

    public void UpdateRemNotification(int newHour) throws JSONException, IOException {
        String main = ReadUserInfo();
        JSONObject mainJSON = new JSONObject(main);

        main = main.replaceFirst("\"remNotHour\":\"" + mainJSON.getJSONObject("user").getString("remNotHour") + "\"", "\"remNotHour\":\"" + newHour + "\"");
        JSONObject jsonToWrite = new JSONObject(main);
        WriteUserInfo(jsonToWrite.toString());
    }

    public void ToggleRemNotifications() throws JSONException, IOException {
        String main = ReadUserInfo();
        JSONObject mainJSON = new JSONObject(main);
        if (mainJSON.getJSONObject("user").getString("remNotifications").equals("true")) {
            main = main.replaceFirst("\"remNotifications\":\"true\"", "\"remNotifications\":\"false\"");
        } else if (mainJSON.getJSONObject("user").getString("remNotifications").equals("false")) {
            main = main.replaceFirst("\"remNotifications\":\"false\"", "\"remNotifications\":\"true\"");
        }
        JSONObject jsonToWrite = new JSONObject(main);
        WriteUserInfo(jsonToWrite.toString());
    }

    public void AddIncomeCustom(String custom) throws IOException, JSONException {
        String main = ReadUserInfo();
        JSONObject mainJSON = new JSONObject(main);
        JSONArray incomeCustoms = mainJSON.getJSONObject("user").getJSONArray("incomeCustoms");
        incomeCustoms.put(custom);
        WriteUserInfo(mainJSON.toString());
    }

    public void AddExpenseCustom(String custom) throws IOException, JSONException {
        String main = ReadUserInfo();
        JSONObject mainJSON = new JSONObject(main);
        JSONArray expenseCustoms = mainJSON.getJSONObject("user").getJSONArray("expenseCustoms");
        expenseCustoms.put(custom);
        WriteUserInfo(mainJSON.toString());
    }

    /**
     * Gelir ekleme metodu.
     *
     * @param income Eklenicek gelir
     * @throws IOException
     * @throws JSONException
     */
    public void AddIncome(Income income) throws JSONException, IOException {

        JSONObject incomeToSave = CreateJSONIncome(income);

        String main = ReadUserInfo();

        JSONObject mainJSON = new JSONObject(main);
        JSONArray incomes = mainJSON.getJSONObject("user").getJSONArray("incomes");
        incomes.put(incomeToSave);
        WriteUserInfo(mainJSON.toString());
    }

    public void RepeatIncome(Income income) throws IOException, JSONException {
        Income newIncome = new Income(income.GetCategory(),income.GetSubCategory(),income.GetAmount(),income.GetDescription(), new Date());
        AddIncome(newIncome);
    }

    /**
     * Birikim ekleme metodu.
     *
     * @param saving Eklenicek birikim
     * @throws JSONException
     */
    public void AddSaving(Saving saving) throws JSONException, IOException {

        JSONObject savingToSave = CreateJSONSaving(saving);

        String main = ReadUserInfo();

        JSONObject mainJSON = new JSONObject(main);
        JSONArray savings = mainJSON.getJSONObject("user").getJSONArray("savings");
        savings.put(savingToSave);
        WriteUserInfo(mainJSON.toString());

    }

    public void DeleteSaving(String id) throws JSONException, IOException {
        String main = ReadUserInfo();
        JSONObject mainJSON = new JSONObject(main);
        JSONObject userJSON = mainJSON.getJSONObject("user");
        JSONArray incomes = userJSON.getJSONArray("incomes");
        JSONArray expenses = userJSON.getJSONArray("expenses");
        JSONArray savings = userJSON.getJSONArray("savings");
        JSONArray incomeCustoms = userJSON.getJSONArray("incomeCustoms");
        JSONArray expenseCustoms = userJSON.getJSONArray("expenseCustoms");

        JSONObject userInfo = JSONHelper.CreateStartingJSON(userJSON.getString("name"), userJSON.getString("lastName"), userJSON.getString("currency"), userJSON.getString("pro"), userJSON.getString("notifications"), userJSON.getString("remNotifications"), userJSON.getString("savNotHour"), userJSON.getString("remNotHour"), userJSON.getString("statusNot"));

        JSONArray newIncomes = userInfo.getJSONObject("user").getJSONArray("incomes");
        JSONArray newExpenses = userInfo.getJSONObject("user").getJSONArray("expenses");
        JSONArray newSavings = userInfo.getJSONObject("user").getJSONArray("savings");
        JSONArray newIncomeCustoms = userInfo.getJSONObject("user").getJSONArray("incomeCustoms");
        JSONArray newExpenseCustoms = userInfo.getJSONObject("user").getJSONArray("expenseCustoms");

        for (int i = 0; i < incomes.length(); i++) {
            newIncomes.put(incomes.getJSONObject(i));
        }

        for (int i = 0; i < expenses.length(); i++) {
            newExpenses.put(expenses.getJSONObject(i));
        }

        for (int i = 0; i < savings.length(); i++) {
            if (!savings.getJSONObject(i).getString("id").equals(id)) {
                newSavings.put(savings.getJSONObject(i));
            }
        }

        for (int i = 0; i < incomeCustoms.length(); i++) {
            newIncomeCustoms.put(incomeCustoms.get(i));
        }

        for (int i = 0; i < expenseCustoms.length(); i++) {
            newExpenseCustoms.put(expenseCustoms.get(i));
        }

        WriteUserInfo(userInfo.toString());
    }


    /**
     * Gider ekleme metodu.
     *
     * @param expense Eklenicek gider
     * @throws JSONException
     * @throws IOException
     */
    public void AddExpense(Expense expense) throws JSONException, IOException {

        JSONObject expenseToSave = CreateJSONExpense(expense);

        String main = ReadUserInfo();
        JSONObject mainJSON = new JSONObject(main);
        JSONArray expenses = mainJSON.getJSONObject("user").getJSONArray("expenses");
        expenses.put(expenseToSave);

        WriteUserInfo(mainJSON.toString());
    }

    public void RepeatExpense(Expense expense) throws IOException, JSONException {
        Expense newExpense = new Expense(expense.GetCategory(),expense.GetSubCategory(),expense.GetAmount(),expense.GetDescription(), new Date(), expense.GetTag());
        AddExpense(newExpense);
    }

    /**
     * Gelirlerin silindigi yer. Silme adimlari;
     * 1) Gelirlerin kopyasini al,
     * 2) Kullanici bilgilerini yeniden olustur,
     * 3) Silinecek gelir disindaki gelirleri ekle,
     * 4) Bilgileri birdaha yaz.
     * <p/>
     * tl:dr; userConfig'i bastan olusturuyoruz.
     *
     * @param id Silinicek gelirin id'si
     * @throws org.json.JSONException
     * @throws java.io.IOException
     */
    public void DeleteIncome(String id) throws JSONException, IOException {

        String main = ReadUserInfo();
        JSONObject mainJSON = new JSONObject(main);
        JSONObject userJSON = mainJSON.getJSONObject("user");
        JSONArray incomes = userJSON.getJSONArray("incomes");
        JSONArray expenses = userJSON.getJSONArray("expenses");
        JSONArray savings = userJSON.getJSONArray("savings");
        JSONArray incomeCustoms = userJSON.getJSONArray("incomeCustoms");
        JSONArray expenseCustoms = userJSON.getJSONArray("expenseCustoms");

        JSONObject userInfo = JSONHelper.CreateStartingJSON(userJSON.getString("name"), userJSON.getString("lastName"), userJSON.getString("currency"), userJSON.getString("pro"), userJSON.getString("notifications"), userJSON.getString("remNotifications"), userJSON.getString("savNotHour"), userJSON.getString("remNotHour"), userJSON.getString("statusNot"));
        JSONArray newIncomes = userInfo.getJSONObject("user").getJSONArray("incomes");
        JSONArray newExpenses = userInfo.getJSONObject("user").getJSONArray("expenses");
        JSONArray newSavings = userInfo.getJSONObject("user").getJSONArray("savings");
        JSONArray newIncomeCustoms = userInfo.getJSONObject("user").getJSONArray("incomeCustoms");
        JSONArray newExpenseCustoms = userInfo.getJSONObject("user").getJSONArray("expenseCustoms");

        for (int i = 0; i < incomes.length(); i++) {
            if (!incomes.getJSONObject(i).getString("id").equals(id)) {
                newIncomes.put(incomes.getJSONObject(i));
            }
        }

        for (int i = 0; i < expenses.length(); i++) {
            newExpenses.put(expenses.getJSONObject(i));
        }

        for (int i = 0; i < savings.length(); i++) {
            newSavings.put(savings.getJSONObject(i));
        }

        for (int i = 0; i < incomeCustoms.length(); i++) {
            newIncomeCustoms.put(incomeCustoms.get(i));
        }

        for (int i = 0; i < expenseCustoms.length(); i++) {
            newExpenseCustoms.put(expenseCustoms.get(i));
        }

        WriteUserInfo(userInfo.toString());

    }

    /**
     * Giderlerin silindigi yer. Silme adimlari;
     * 1) Giderlerin kopyasini al,
     * 2) Kullanici bilgilerini yeniden olustur,
     * 3) Silinecek gider disindaki giderleri ekle,
     * 4) Bilgileri birdaha yaz.
     * <p/>
     * tl:dr; userConfig'i bastan olusturuyoruz.
     *
     * @param id Silinecek giderin id'si
     * @throws org.json.JSONException
     * @throws java.io.IOException
     */
    public void DeleteExpense(String id) throws JSONException, IOException {

        String main = ReadUserInfo();
        JSONObject mainJSON = new JSONObject(main);
        JSONObject userJSON = mainJSON.getJSONObject("user");
        JSONArray incomes = userJSON.getJSONArray("incomes");
        JSONArray expenses = userJSON.getJSONArray("expenses");
        JSONArray savings = userJSON.getJSONArray("savings");
        JSONArray incomeCustoms = userJSON.getJSONArray("incomeCustoms");
        JSONArray expenseCustoms = userJSON.getJSONArray("expenseCustoms");

        JSONObject userInfo = JSONHelper.CreateStartingJSON(userJSON.getString("name"), userJSON.getString("lastName"), userJSON.getString("currency"), userJSON.getString("pro"), userJSON.getString("notifications"), userJSON.getString("remNotifications"), userJSON.getString("savNotHour"), userJSON.getString("remNotHour"), userJSON.getString("statusNot"));
        JSONArray newIncomes = userInfo.getJSONObject("user").getJSONArray("incomes");
        JSONArray newExpenses = userInfo.getJSONObject("user").getJSONArray("expenses");
        JSONArray newSavings = userInfo.getJSONObject("user").getJSONArray("savings");
        JSONArray newIncomeCustoms = userInfo.getJSONObject("user").getJSONArray("incomeCustoms");
        JSONArray newExpenseCustoms = userInfo.getJSONObject("user").getJSONArray("expenseCustoms");

        for (int i = 0; i < expenses.length(); i++) {
            if (!expenses.getJSONObject(i).getString("id").equals(id)) {
                newExpenses.put(expenses.getJSONObject(i));
            }
        }

        for (int i = 0; i < incomes.length(); i++) {
            newIncomes.put(incomes.getJSONObject(i));
        }

        for (int i = 0; i < savings.length(); i++) {
            newSavings.put(savings.getJSONObject(i));
        }

        for (int i = 0; i < incomeCustoms.length(); i++) {
            newIncomeCustoms.put(incomeCustoms.get(i));
        }

        for (int i = 0; i < expenseCustoms.length(); i++) {
            newExpenseCustoms.put(expenseCustoms.get(i));
        }

        WriteUserInfo(userInfo.toString());

    }

    public BigDecimal GetTotalSavingLimit() {
        BigDecimal main = BigDecimal.ZERO;
        BigDecimal temp;

        boolean first = true;

        for (Saving s : _savings) {
            if (first) {
                main = s.GetDailyLimit();
                first = false;
            }

            temp = s.GetDailyLimit();
            if (temp.compareTo(main) == -1) {
                main = temp;
            }

        }
        return main;
    }

    public void CheckSavings() throws Exception {
        Date today = new Date();
        Calendar calToday = Calendar.getInstance();
        calToday.setTime(today);

        ArrayList<Saving> savingsToDelete = new ArrayList<Saving>();

        for (Saving s : _savings) {
            Date savingDay = s.GetDate();
            Calendar calSaving = Calendar.getInstance();
            calSaving.setTime(savingDay);

            int daysPast = calToday.get(Calendar.DAY_OF_MONTH) - calSaving.get(Calendar.DAY_OF_MONTH);

            if (daysPast < 0) {
                throw new Exception("Gecen gunler 0'dan kucuk.");
            }

            int remainingDays = s.GetDays(s.GetPeriod()) - daysPast;

            s.SetRemainingDays(remainingDays);
            s.SetDailyLimit(this.GetBalance(new Date(), false));

            if (remainingDays <= 0) {
                // Birikim tamamlandi
                int compProgress = s.GetProgress().compareTo(s.GetAmount());

                // Birikim silinecek
                savingsToDelete.add(s);

                if (compProgress == 0 || compProgress == 1) {
                    // Birikim amacina ulasti
                } else {
                    // Birikim basarisiz
                }

                if (s.GetRepeating()) {
                    // Birikim tekrar edilecek
                    Saving savingToRepeat = new Saving(s.GetName(), s.GetAmount(), s.GetDate(), s.GetPeriod(), s.GetRepeating(), ((Global) mainApp).GetUser().GetCurrency());
                    AddSaving(savingToRepeat);
                }

            } else {

                for (int i = 0; i < daysPast; i++) {
                    Calendar calSav = Calendar.getInstance();
                    calSav.setTime(s.GetDate());
                    calSav.add(Calendar.DAY_OF_MONTH, i);

                    BigDecimal gb = GetBalance(calSav.getTime(), false);
                    BigDecimal dl = s.GetDailyLimit();

                    int comp = gb.compareTo(dl);

                    if (comp == 0 || comp == 1) {
                        // Bu gun icinde  limit asilmamis, yani birikim dogru yolda
                        s.SetDailyProgress();
                    } else if (comp == -1) {
                        // Bu gun icinde limit asilmis

                    }
                }

            }

        }

        // Birikimlerin silinmesi
        for (Saving s : savingsToDelete) {
            DeleteSaving(s.GetID());
        }
    }

}
