package Fragments;

import Helpers.Income;
import Helpers.IncomeListAdapter;
import Helpers.IncomeLoadListener;
import Helpers.User;
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
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.jar.Attributes;

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
        v = inflater.inflate(R.layout.incomesearchfragment, container, false);
        dateBeingViewed = new Date();
        _user = ((Global) getActivity().getApplication()).GetUser();

        lv = (ListView)v.findViewById(R.id.lstSearchIncomes);
        lv.setOnItemClickListener(onItemClickListener);
        dialog = new IncomeDialogFragment();
        dialog.SetListener(this);

        txtIncomeDate = (TextView)v.findViewById(R.id.txtSearchDate);

        spnSearchCategory = (Spinner)v.findViewById(R.id.spnSearchCategory);
        spnSearchSubCategory = (Spinner)v.findViewById(R.id.spnSearchSubCategory);
        spnSearchDate = (Spinner)v.findViewById(R.id.spnSearchDate);
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

                if(mode == "month")
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
                else if(mode == "day")
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
                if(mode == "month")
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
                else if(mode == "day")
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

            case R.id.spnSearchDate:
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

            UpdateDayText();
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

            UpdateMonthText();
        }
        BigDecimal total = BigDecimal.ZERO;

        for (Income income : cleanedIncomes) {
            BigDecimal val = income.GetAmount();
            total = total.add(val);
        }
        lv.setAdapter(new IncomeListAdapter(this.getActivity(), cleanedIncomes));

        TextView txtTotalIncome = (TextView)v.findViewById(R.id.txtSearchTotal);

        txtTotalIncome.setText(total.toString() + "  TL");
        txtTotalIncome.setTextColor(Color.parseColor("#216C2A"));

    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            IncomeListAdapter adapter = (IncomeListAdapter)lv.getAdapter();
            Income inc = (Income)adapter.getItem(position);

            Bundle bundle = new Bundle();
            bundle.putString("income", new Gson().toJson(inc));
            dialog.setArguments(bundle);
            dialog.show(getActivity().getFragmentManager(), "dialog");
        }
    };


    public void UpdateDayText()
    {
        Format formatter = new SimpleDateFormat("dd/MM/yyyy");
        txtIncomeDate.setText(formatter.format(dateBeingViewed));
    }

    public void UpdateMonthText()
    {
        Format formatter = new SimpleDateFormat("MM");
        String[] months = getResources().getStringArray(R.array.turkishMonths);
        String month = "m";

        switch (Integer.parseInt(formatter.format(dateBeingViewed)))
        {
            case 1:
                month = months[0];
                break;
            case 2:
                month = months[1];
                break;
            case 3:
                month = months[2];
                break;
            case 4:
                month = months[3];
                break;
            case 5:
                month = months[4];
                break;
            case 6:
                month = months[5];
                break;
            case 7:
                month = months[6];
                break;
            case 8:
                month = months[7];
                break;
            case 9:
                month = months[8];
                break;
            case 10:
                month = months[9];
                break;
            case 11:
                month = months[10];
                break;
            case 12:
                month = months[11];
                break;

        }

        Format formatterYear = new SimpleDateFormat("yyyy");
        month += " " + formatterYear.format(dateBeingViewed);
        txtIncomeDate.setText(month);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onDismissed() {
        if(mode == "month")
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
        else if(mode == "day")
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
