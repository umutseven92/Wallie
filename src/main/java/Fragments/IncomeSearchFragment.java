package Fragments;

import Helpers.*;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
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
 * Created by Umut on 14.1.2015.
 */
public class IncomeSearchFragment extends Fragment implements AdapterView.OnItemSelectedListener, IncomeLoadListener{

    View v;
    Spinner spnSearchCategory,spnSearchSubCategory,spnSearchDate;
    ListView lv;
    Date dateBeingViewed;
    String mode = "month";
    TextView txtIncomeDate;
    ImageButton btnRight, btnLeft;
    static User _user;
    IncomeDialogFragment dialog;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        v = inflater.inflate(R.layout.income_search_fragment, container, false);
        dateBeingViewed = new Date();
        _user = ((Global) getActivity().getApplication()).GetUser();

        lv = (ListView)v.findViewById(R.id.lstSearchIncomes);
        lv.setOnItemClickListener(onItemClickListener);
        dialog = new IncomeDialogFragment();
        dialog.SetListener(this);

        txtIncomeDate = (TextView)v.findViewById(R.id.txtSearchIncomeDate);

        btnLeft = (ImageButton)v.findViewById(R.id.imgSearchLeftIncome);
        btnRight = (ImageButton)v.findViewById(R.id.imgSearchRightIncome);

        btnLeft.setOnClickListener(onLeftArrowClick);
        btnRight.setOnClickListener(onRightArrowClick);

        spnSearchCategory = (Spinner)v.findViewById(R.id.spnSearchCategory);
        spnSearchSubCategory = (Spinner)v.findViewById(R.id.spnSearchSubCategory);
        spnSearchDate = (Spinner)v.findViewById(R.id.spnSearchIncomeDate);
        spnSearchDate.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(v.getContext(), R.array.incomeCategories, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSearchCategory.setAdapter(categoryAdapter);
        spnSearchCategory.setOnItemSelectedListener(this);

        spnSearchSubCategory.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> dateAdapter = ArrayAdapter.createFromResource(v.getContext(), R.array.balanceDateArray, android.R.layout.simple_spinner_item);
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

    public void getNextDateIncomes() throws JSONException, ParseException, IOException
    {
        Date today = new Date();

        if(dateBeingViewed.getDay() == today.getDay() && dateBeingViewed.getMonth() == today.getMonth() && dateBeingViewed.getYear() == today.getYear() )
        {
            return;
        }

        Calendar cal = Calendar.getInstance();

        cal.setTime(dateBeingViewed);

        if(mode.equals("day"))
        {
            cal.add(Calendar.DATE,1);
            dateBeingViewed = cal.getTime();
            LoadListView(spnSearchCategory.getSelectedItem().toString(), spnSearchSubCategory.getSelectedItem().toString() , true);
        }
        else if (mode.equals("month"))
        {
            cal.add(Calendar.MONTH,1);
            dateBeingViewed = cal.getTime();
            LoadListView(spnSearchCategory.getSelectedItem().toString(), spnSearchSubCategory.getSelectedItem().toString() , false);
        }
    }

    public void getLastDateIncomes() throws JSONException, ParseException, IOException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateBeingViewed);

        if(mode.equals("day"))
        {
            cal.add(Calendar.DATE,-1);
            dateBeingViewed = cal.getTime();
            LoadListView(spnSearchCategory.getSelectedItem().toString(), spnSearchSubCategory.getSelectedItem().toString() , true);
        }
        else if (mode.equals("month"))
        {
            cal.add(Calendar.MONTH,-1);
            dateBeingViewed = cal.getTime();
            LoadListView(spnSearchCategory.getSelectedItem().toString(), spnSearchSubCategory.getSelectedItem().toString() , false);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId())
        {
            case R.id.spnSearchCategory:
                String item = parent.getItemAtPosition(position).toString();
                if(item.contains(" "))
                {
                    item = item.substring(0,item.indexOf(" ")) + "Gelir";
                }
                else
                {
                    item += "Gelir";
                }
                int subID = getResources().getIdentifier(item,"array",getActivity().getPackageName());

                ArrayAdapter<CharSequence> subCategoryAdapter = ArrayAdapter.createFromResource(v.getContext(), subID, android.R.layout.simple_spinner_item);
                subCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnSearchSubCategory.setAdapter(subCategoryAdapter);

                if(mode.equals("month"))
                {
                    try {
                        LoadListView(parent.getItemAtPosition(position).toString(),spnSearchSubCategory.getSelectedItem().toString(), false);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if(mode.equals("day"))
                {
                    try {
                        LoadListView(parent.getItemAtPosition(position).toString(),spnSearchSubCategory.getSelectedItem().toString(), true);
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
                if(mode.equals("month"))
                {
                    try {
                        LoadListView(spnSearchCategory.getSelectedItem().toString(), spnSearchSubCategory.getSelectedItem().toString(), false);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if(mode.equals("day"))
                {
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
                break;

            case R.id.spnSearchIncomeDate:
                if(parent.getItemAtPosition(position).toString().equals("Ay"))
                {
                    mode = "month";
                    try {
                        LoadListView(spnSearchCategory.getSelectedItem().toString(),spnSearchSubCategory.getSelectedItem().toString(),false);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if(parent.getItemAtPosition(position).toString().equals("Gün"))
                {
                    mode = "day";
                    try {
                        LoadListView(spnSearchCategory.getSelectedItem().toString(),spnSearchSubCategory.getSelectedItem().toString(),true);
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

        if(day)
        {
            incomes = _user.GetBanker().GetIncomesFromDay(dateBeingViewed);

            for (Income income : incomes)
            {
                if (income.GetCategory().equals(category) && income.GetSubCategory().equals(subCategory)) {
                    cleanedIncomes.add(income);
                }
            }

            txtIncomeDate.setText(DateFormatHelper.GetDayText(dateBeingViewed));
        }
        else
        {
            incomes = _user.GetBanker().GetIncomesFromMonth(dateBeingViewed);

            for (Income income : incomes)
            {
                if (income.GetCategory().equals(category) && income.GetSubCategory().equals(subCategory)) {
                    cleanedIncomes.add(income);
                }
            }

            txtIncomeDate.setText(DateFormatHelper.GetMonthText(dateBeingViewed, getResources()));
        }
        BigDecimal total = BigDecimal.ZERO;

        for (Income income : cleanedIncomes) {
            BigDecimal val = income.GetAmount();
            total = total.add(val);
        }
        lv.setAdapter(new IncomeListAdapter(this.getActivity(), cleanedIncomes));

        TextView txtTotalIncome = (TextView)v.findViewById(R.id.txtSearchIncomeTotal);

        txtTotalIncome.setText(total.toString() + "  TL");
        txtTotalIncome.setTextColor(Color.parseColor("#216C2A"));

    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            IncomeListAdapter adapter = (IncomeListAdapter)lv.getAdapter();
            Income inc = (Income)adapter.getItem(position);

            Bundle bundle = new Bundle();
            bundle.putBoolean("canDelete", true);

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
        if(mode.equals("month"))
        {
            try {
                LoadListView(spnSearchCategory.getSelectedItem().toString(), spnSearchSubCategory.getSelectedItem().toString(), false);
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(mode.equals("day"))
        {
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
