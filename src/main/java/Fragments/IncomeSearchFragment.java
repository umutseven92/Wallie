package Fragments;

import Helpers.*;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.google.gson.Gson;
import com.graviton.Cuzdan.Global;
import com.graviton.Cuzdan.R;
import org.json.JSONException;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by Umut Seven on 14.1.2015, for Graviton.
 */
public class IncomeSearchFragment extends Fragment implements AdapterView.OnItemSelectedListener, IncomeLoadListener {

    View v;
    Spinner spnSearchCategory, spnSearchSubCategory, spnSearchDate;
    ListView lv;
    Date dateBeingViewed;
    String mode = "month";
    TextView txtIncomeDate;
    ImageButton btnRight, btnLeft, btnCalendar;
    static User _user;
    IncomeDialogFragment dialog;
    DatePickerFragment datePickerFragment;
    RecordsHelper rec;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.income_search_fragment, container, false);
        dateBeingViewed = new Date();
        _user = ((Global) getActivity().getApplication()).GetUser();
        rec = ((Global) getActivity().getApplication()).recordsHelper;

        lv = (ListView) v.findViewById(R.id.lstSearchIncomes);
        lv.setOnItemClickListener(onItemClickListener);
        dialog = new IncomeDialogFragment();
        dialog.SetListener(this);

        txtIncomeDate = (TextView) v.findViewById(R.id.txtSearchIncomeDate);
        datePickerFragment = new DatePickerFragment();
        datePickerFragment.SetIncomeListener(this);

        btnLeft = (ImageButton) v.findViewById(R.id.imgSearchLeftIncome);
        btnRight = (ImageButton) v.findViewById(R.id.imgSearchRightIncome);
        btnCalendar = (ImageButton) v.findViewById(R.id.btnIncomeSearchCalendar);

        btnLeft.setOnClickListener(onLeftArrowClick);
        btnRight.setOnClickListener(onRightArrowClick);
        btnCalendar.setOnClickListener(onCalendarClick);

        spnSearchCategory = (Spinner) v.findViewById(R.id.spnSearchCategory);
        spnSearchSubCategory = (Spinner) v.findViewById(R.id.spnSearchSubCategory);
        spnSearchDate = (Spinner) v.findViewById(R.id.spnSearchIncomeDate);
        spnSearchDate.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(v.getContext(), R.array.incomeCategories, R.layout.cuzdan_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSearchCategory.setAdapter(categoryAdapter);
        spnSearchCategory.setOnItemSelectedListener(this);

        spnSearchSubCategory.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> dateAdapter = ArrayAdapter.createFromResource(v.getContext(), R.array.balanceDateArray, R.layout.cuzdan_spinner_item);
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSearchDate.setAdapter(dateAdapter);
        spnSearchDate.setOnItemSelectedListener(this);

        return v;
    }

    View.OnClickListener onLeftArrowClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                getLastDateIncomes();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    View.OnClickListener onRightArrowClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                getNextDateIncomes();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    View.OnClickListener onCalendarClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            datePickerFragment.SetMode("income");
            datePickerFragment.show(getActivity().getFragmentManager(), "datepicker");
        }
    };

    public void getNextDateIncomes() throws JSONException, ParseException, IOException {

        if (spnSearchSubCategory.getSelectedItem() == null) {
            return;
        }

        Date today = new Date();
        Calendar calToday = Calendar.getInstance();
        calToday.setTime(today);

        Calendar cal = Calendar.getInstance();
        cal.setTime(dateBeingViewed);

        int calDayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        int calTodayDayOfMonth = calToday.get(Calendar.DAY_OF_MONTH);

        int calMonth = cal.get(Calendar.MONTH);
        int calTodayMonth = calToday.get(Calendar.MONTH);

        int calYear = cal.get(Calendar.YEAR);
        int calTodayYear = calToday.get(Calendar.YEAR);

        if (mode.equals("month")) {
            if (calMonth == calTodayMonth && calYear == calTodayYear) {
                return;
            }
        } else if (mode.equals("day")) {
            if (calDayOfMonth == calTodayDayOfMonth && calMonth == calTodayMonth && calYear == calTodayYear) {
                return;
            }

        }

        if (mode.equals("day")) {
            cal.add(Calendar.DATE, 1);
            dateBeingViewed = cal.getTime();
            LoadListView(spnSearchCategory.getSelectedItem().toString(), spnSearchSubCategory.getSelectedItem().toString(), true);
        } else if (mode.equals("month")) {
            cal.add(Calendar.MONTH, 1);
            dateBeingViewed = cal.getTime();
            LoadListView(spnSearchCategory.getSelectedItem().toString(), spnSearchSubCategory.getSelectedItem().toString(), false);
        }
    }

    public void getLastDateIncomes() throws JSONException, ParseException, IOException {

        if (spnSearchSubCategory.getSelectedItem() == null) {
            return;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(dateBeingViewed);

        if (mode.equals("day")) {
            cal.add(Calendar.DATE, -1);
            dateBeingViewed = cal.getTime();
            LoadListView(spnSearchCategory.getSelectedItem().toString(), spnSearchSubCategory.getSelectedItem().toString(), true);
        } else if (mode.equals("month")) {
            cal.add(Calendar.MONTH, -1);
            dateBeingViewed = cal.getTime();
            LoadListView(spnSearchCategory.getSelectedItem().toString(), spnSearchSubCategory.getSelectedItem().toString(), false);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {
            case R.id.spnSearchCategory:
                String item = parent.getItemAtPosition(position).toString();
                if (!item.equals(getString(R.string.custom_category))) {
                    if (item.contains(" ")) {
                        item = item.substring(0, item.indexOf(" ")) + getString(R.string.income_search_add);
                    } else {
                        item += getString(R.string.income_search_add);
                    }

                    int subID = getResources().getIdentifier(item, "array", getActivity().getPackageName());

                    // Android v4.4'ler ve sonrasinda resource nameler turkce karakter iceremez(mis).
                    // Bu yuzden getIdentifier() cagirmadan once tum turkce karakterlerden arındırıyoruz.
                    if (subID == 0) {
                        String newItem = LocaleHelper.GetRidOfTurkishCharacters(item);
                        subID = getResources().getIdentifier(newItem, "array", getActivity().getPackageName());
                    }

                    ArrayAdapter<CharSequence> subCategoryAdapter = ArrayAdapter.createFromResource(v.getContext(), subID, R.layout.cuzdan_spinner_item);
                    subCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnSearchSubCategory.setAdapter(subCategoryAdapter);
                } else {
                    try {
                        if (_user.GetBanker().GetIncomeCustoms().size() == 0) {
                            ArrayAdapter<String> emptyAdapter = new ArrayAdapter<String>(v.getContext(), R.layout.cuzdan_spinner_item, new ArrayList<String>());
                            emptyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spnSearchSubCategory.setAdapter(emptyAdapter);
                            return;
                        }
                        ArrayAdapter<String> customCategoryAdapter = new ArrayAdapter<String>(v.getContext(), R.layout.cuzdan_spinner_item, _user.GetBanker().GetIncomeCustoms());
                        customCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spnSearchSubCategory.setAdapter(customCategoryAdapter);

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (mode.equals("month")) {
                    try {
                        LoadListView(parent.getItemAtPosition(position).toString(), spnSearchSubCategory.getSelectedItem().toString(), false);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (mode.equals("day")) {
                    try {
                        LoadListView(parent.getItemAtPosition(position).toString(), spnSearchSubCategory.getSelectedItem().toString(), true);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case R.id.spnSearchSubCategory:

                if (mode.equals("month")) {
                    try {
                        if (spnSearchSubCategory.getSelectedItem().toString().equals(getString(R.string.all))) {
                            LoadListView(spnSearchCategory.getSelectedItem().toString(), null, false);
                        } else {
                            LoadListView(spnSearchCategory.getSelectedItem().toString(), spnSearchSubCategory.getSelectedItem().toString(), false);
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (mode.equals("day")) {
                    try {

                        if (spnSearchSubCategory.getSelectedItem().toString().equals(getString(R.string.all))) {
                            LoadListView(spnSearchCategory.getSelectedItem().toString(), null, true);
                        } else {
                            LoadListView(spnSearchCategory.getSelectedItem().toString(), spnSearchSubCategory.getSelectedItem().toString(), true);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                break;

            case R.id.spnSearchIncomeDate:
                Date today = new Date();

                if (parent.getItemAtPosition(position).toString().equals(getString(R.string.search_month))) {
                    mode = "month";
                    try {
                        dateBeingViewed.setDate(1);
                        LoadListView(spnSearchCategory.getSelectedItem().toString(), spnSearchSubCategory.getSelectedItem().toString(), false);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (parent.getItemAtPosition(position).toString().equals(getString(R.string.search_day))) {
                    mode = "day";
                    try {
                        if (dateBeingViewed.getMonth() == today.getMonth()) {
                            dateBeingViewed.setDate(today.getDate());
                        }

                        LoadListView(spnSearchCategory.getSelectedItem().toString(), spnSearchSubCategory.getSelectedItem().toString(), true);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;

            default:
                break;
        }

    }

    private void LoadListView(String category, String subCategory, boolean day) throws ParseException, IOException, JSONException {

        ArrayList<Income> incomes;
        ArrayList<Income> cleanedIncomes = new ArrayList<Income>();

        if (day) {
            incomes = _user.GetBanker().GetIncomesFromDay(dateBeingViewed);

            if (subCategory == null) {
                for (Income income : incomes) {
                    if (income.GetCategory().equals(Integer.toString(rec.GetIDFromName(category)))) {
                        cleanedIncomes.add(income);
                    }
                }
            } else {
                for (Income income : incomes) {
                    if (!income.GetCategory().equals(Integer.toString(rec.GetIDFromName(getString(R.string.custom_category))))) {
                        if (income.GetCategory().equals(Integer.toString(rec.GetIDFromName(category))) && income.GetSubCategory().equals(Integer.toString(rec.GetIDFromName(subCategory)))) {
                            cleanedIncomes.add(income);
                        }
                    } else {
                        if (income.GetCategory().equals(Integer.toString(rec.GetIDFromName(category))) && income.GetSubCategory().equals(subCategory)) {
                            cleanedIncomes.add(income);
                        }
                    }
                }
            }

            txtIncomeDate.setText(DateFormatHelper.GetDayText(dateBeingViewed));
        } else {
            incomes = _user.GetBanker().GetIncomesFromMonth(dateBeingViewed);

            if (subCategory == null) {
                for (Income income : incomes) {
                    if (income.GetCategory().equals(Integer.toString(rec.GetIDFromName(category)))) {
                        cleanedIncomes.add(income);
                    }
                }
            } else {
                for (Income income : incomes) {

                    if (!income.GetCategory().equals(Integer.toString(rec.GetIDFromName(getString(R.string.custom_category))))) {
                        if (income.GetCategory().equals(Integer.toString(rec.GetIDFromName(category))) && income.GetSubCategory().equals(Integer.toString(rec.GetIDFromName(subCategory)))) {
                            cleanedIncomes.add(income);
                        }
                    } else {
                        if (income.GetCategory().equals(Integer.toString(rec.GetIDFromName(category))) && income.GetSubCategory().equals(subCategory)) {
                            cleanedIncomes.add(income);
                        }
                    }
                }
            }

            txtIncomeDate.setText(DateFormatHelper.GetMonthText(dateBeingViewed, getResources()));
        }
        BigDecimal total = BigDecimal.ZERO;

        for (Income income : cleanedIncomes) {
            BigDecimal val = income.GetAmount();
            total = total.add(val);
        }
        lv.setAdapter(new IncomeListAdapter(this.getActivity(), cleanedIncomes, _user.GetCurrency()));

        TextView txtTotalIncome = (TextView) v.findViewById(R.id.txtSearchIncomeTotal);

        txtTotalIncome.setText(total.toString() + " " + _user.GetCurrency());
        txtTotalIncome.setTextColor(Color.parseColor("#216C2A"));

    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            IncomeListAdapter adapter = (IncomeListAdapter) lv.getAdapter();
            Income inc = (Income) adapter.getItem(position);

            Bundle bundle = new Bundle();
            bundle.putBoolean("canDelete", false);

            bundle.putString("income", new Gson().toJson(inc));
            dialog.setArguments(bundle);
            dialog.show(getActivity().getFragmentManager(), "dialog");
        }
    };


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onDismissed() {
        if (mode.equals("month")) {
            try {
                LoadListView(spnSearchCategory.getSelectedItem().toString(), spnSearchSubCategory.getSelectedItem().toString(), false);
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (mode.equals("day")) {
            try {
                LoadListView(spnSearchCategory.getSelectedItem().toString(), spnSearchSubCategory.getSelectedItem().toString(), true);
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onDateSelected(Date date) {
        dateBeingViewed = date;
        if (mode.equals("month")) {
            try {
                LoadListView(spnSearchCategory.getSelectedItem().toString(), spnSearchSubCategory.getSelectedItem().toString(), false);
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (mode.equals("day")) {
            try {
                LoadListView(spnSearchCategory.getSelectedItem().toString(), spnSearchSubCategory.getSelectedItem().toString(), true);
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


}
