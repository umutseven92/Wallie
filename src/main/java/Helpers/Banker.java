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
 *
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

    /**
     * Gelir ve giderleri direk kaynaktan yukluyoruz.
     *
     * @return JSON olarak gelir ve giderler
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

    public JSONArray FetchIncomeData() throws IOException, JSONException {
        JSONObject jsonObject = FetchBalanceData();

        return jsonObject.getJSONObject("user").getJSONArray("incomes");
    }

    public JSONArray FetchExpenseData() throws IOException, JSONException {
        JSONObject jsonObject = FetchBalanceData();

        return jsonObject.getJSONObject("user").getJSONArray("expenses");
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
     * @param day   Gelirlerin istendigi gun
     * @return  Gune ait gelirler
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
     * @param day   Gelirlerin istendigi ay
     * @return  Aya ait gelirler
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
     * @param day   Giderlerin istendigi gun
     * @return  Gune ait giderler
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
     * @param day   Giderlerin istendigi ay
     * @return  Aya ait giderler
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
     * @param income    JSON hali istenen gelir
     * @return  JSON halinde gelir
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
     * @return  JSON halinde gider
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
     * @param app   Ana uygulama
     * @return  string halinde kullanici bilgileri
     */
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

    /**
     * Verilen stringi /data/com/graviton/Cuzdan'a yazar.
     *
     * @param app   Ana uygulama
     * @param infoToWrite   Yazilmasi istenen veri
     * @throws IOException
     */
    public void WriteUserInfo(Application app, String infoToWrite) throws IOException {
        FileOutputStream fileOutputStream = app.openFileOutput(((Global) app).GetFilePath(), Context.MODE_PRIVATE);
        fileOutputStream.write(infoToWrite.getBytes());
        fileOutputStream.close();

    }

    /**
     * Gelir ekleme metodu.
     *
     * @param income    Eklenicek gelir
     * @param app   Ana uygulama
     * @throws IOException
     * @throws JSONException
     */
    public void AddIncome(Income income, Application app) throws IOException, JSONException {

        JSONObject incomeToSave = CreateJSONIncome(income);

        String main = ReadUserInfo(app);

        JSONObject mainJSON = new JSONObject(main);
        JSONArray incomes = mainJSON.getJSONObject("user").getJSONArray("incomes");
        incomes.put(incomeToSave);
        WriteUserInfo(app, mainJSON.toString());
    }

    /**
     * Gider ekleme metodu.
     *
     * @param expense   Eklenicek gider
     * @param app   Ana uygulama
     * @throws JSONException
     * @throws IOException
     */
    public void AddExpense(Expense expense, Application app) throws JSONException, IOException {

        JSONObject expenseToSave = CreateJSONExpense(expense);

        String main = ReadUserInfo(app);
        JSONObject mainJSON = new JSONObject(main);
        JSONArray expenses = mainJSON.getJSONObject("user").getJSONArray("expenses");
        expenses.put(expenseToSave);

        WriteUserInfo(app, mainJSON.toString());
    }

    /**
     * Gelirlerin silindigi yer. Silme adimlari;
     * 1) Gelirlerin kopyasini al,
     * 2) Kullanici bilgilerini yeniden olustur,
     * 3) Silinecek gelir disindaki gelirleri ekle,
     * 4) Bilgileri birdaha yaz.
     *
     * tl:dr; userConfig'i bastan olusturuyoruz.
     *
     * @param id    Silinicek gelirin id'si
     * @param app   Ana uygulama
     * @throws JSONException
     * @throws IOException
     */
    public void DeleteIncome(String id, Application app) throws JSONException, IOException {

        String main = ReadUserInfo(app);
        JSONObject mainJSON = new JSONObject(main);
        JSONObject userJSON = mainJSON.getJSONObject("user");
        JSONArray incomes = userJSON.getJSONArray("incomes");
        JSONArray expenses = userJSON.getJSONArray("expenses");

        String userSettings = String.format("{\n" +
                "\t\"user\": {\n" +
                "\t\t\"userName\": \"%s\",\n" +
                "\t\t\"birthDate\": \"1992-08-05\",\n" +
                "\t\t\"name\": \"%s\",\n" +
                "\t\t\"lastName\": \"%s\",\n" +
                "\t\t\"city\": \"Istanbul\",\n" +
                "\t\t\"email\": \"umutseven92@gmail.com\",\n" +
                "\n" +
                "\t\t\"incomes\": [],\n" +
                "\t\t\"expenses\": []\n" +
                "\t}\n" +
                "}\n", userJSON.getString("userName"), userJSON.getString("name"), userJSON.getString("lastName"));

        JSONObject userInfo = new JSONObject(userSettings);
        JSONArray newIncomes = userInfo.getJSONObject("user").getJSONArray("incomes");
        JSONArray newExpenses = userInfo.getJSONObject("user").getJSONArray("expenses");

        for (int i = 0; i < incomes.length(); i++) {
            if (!incomes.getJSONObject(i).getString("id").equals(id)) {
                newIncomes.put(incomes.getJSONObject(i));
            }
        }

        for (int i = 0; i < expenses.length(); i++) {
            newExpenses.put(expenses.getJSONObject(i));
        }

        WriteUserInfo(app, userInfo.toString());

    }

    /**
     * Giderlerin silindigi yer. Silme adimlari;
     * 1) Giderlerin kopyasini al,
     * 2) Kullanici bilgilerini yeniden olustur,
     * 3) Silinecek gider disindaki giderleri ekle,
     * 4) Bilgileri birdaha yaz.
     *
     * tl:dr; userConfig'i bastan olusturuyoruz.
     *
     * @param id    Silinecek giderin id'si
     * @param app   Ana uygulama
     * @throws JSONException
     * @throws IOException
     */
    public void DeleteExpense(String id, Application app) throws JSONException, IOException {

        String main = ReadUserInfo(app);
        JSONObject mainJSON = new JSONObject(main);
        JSONObject userJSON = mainJSON.getJSONObject("user");
        JSONArray incomes = userJSON.getJSONArray("incomes");
        JSONArray expenses = userJSON.getJSONArray("expenses");

        String userSettings = String.format("{\n" +
                "\t\"user\": {\n" +
                "\t\t\"userName\": \"%s\",\n" +
                "\t\t\"birthDate\": \"1992-08-05\",\n" +
                "\t\t\"name\": \"%s\",\n" +
                "\t\t\"lastName\": \"%s\",\n" +
                "\t\t\"city\": \"Istanbul\",\n" +
                "\t\t\"email\": \"umutseven92@gmail.com\",\n" +
                "\n" +
                "\t\t\"incomes\": [],\n" +
                "\t\t\"expenses\": []\n" +
                "\t}\n" +
                "}\n", userJSON.getString("userName"), userJSON.getString("name"), userJSON.getString("lastName"));

        JSONObject userInfo = new JSONObject(userSettings);
        JSONArray newIncomes = userInfo.getJSONObject("user").getJSONArray("incomes");
        JSONArray newExpenses = userInfo.getJSONObject("user").getJSONArray("expenses");

        for (int i = 0; i < expenses.length(); i++) {
            if (!expenses.getJSONObject(i).getString("id").equals(id)) {
                newExpenses.put(expenses.getJSONObject(i));
            }
        }

        for (int i = 0; i < incomes.length(); i++) {
            newIncomes.put(incomes.getJSONObject(i));
        }

        WriteUserInfo(app, userInfo.toString());

    }

}
