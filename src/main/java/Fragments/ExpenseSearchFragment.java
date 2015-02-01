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
 * Created by Umut Seven on 23.1.2015, for Graviton.
 */
public class ExpenseSearchFragment extends Fragment implements AdapterView.OnItemSelectedListener, ExpenseLoadListener {

    View v;
    Spinner spnSearchCategory,spnSearchSubCategory,spnSearchDate,spnSearchTags;
    ListView lv;
    Date dateBeingViewed;
    String mode = "month";
    TextView txtExpenseDate;
    ImageButton btnRight, btnLeft;
    static User _user;
    ExpenseDialogFragment dialog;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        v = inflater.inflate(R.layout.expense_search_fragment, container, false);
        dateBeingViewed = new Date();
        _user = ((Global) getActivity().getApplication()).GetUser();
        lv = (ListView)v.findViewById(R.id.lstSearchExpense);
        lv.setOnItemClickListener(onItemClickListener);

        dialog = new ExpenseDialogFragment();
        dialog.SetListener(this);

        txtExpenseDate = (TextView)v.findViewById(R.id.txtSearchExpenseDate);

        btnLeft = (ImageButton)v.findViewById(R.id.imgSearchLeft);
        btnRight = (ImageButton)v.findViewById(R.id.imgSearchRight);

        btnLeft.setOnClickListener(onLeftArrowClick);
        btnRight.setOnClickListener(onRightArrowClick);

        spnSearchTags = (Spinner)v.findViewById(R.id.spnExpenseTags);
        spnSearchCategory = (Spinner)v.findViewById(R.id.spnExpenseCategory);
        spnSearchSubCategory = (Spinner)v.findViewById(R.id.spnExpenseSubCategory);
        spnSearchDate = (Spinner)v.findViewById(R.id.spnSearchExpenseDate);
        spnSearchDate.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> tagAdapter = ArrayAdapter.createFromResource(v.getContext(), R.array.expenseTags, R.layout.cuzdan_spinner_item);
        tagAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSearchTags.setAdapter(tagAdapter);
        spnSearchTags.setOnItemSelectedListener(this);

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
                getLastDateExpenses();
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
                getNextDateExpenses();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    public void getNextDateExpenses() throws JSONException, ParseException, IOException
    {
        Date today = new Date();

        if(dateBeingViewed.getDay() == today.getDay() && dateBeingViewed.getMonth() == today.getMonth() && dateBeingViewed.getYear() == today.getYear() )
        {
            return;
        }

        Calendar cal = Calendar.getInstance();

        cal.setTime(dateBeingViewed);

        if(mode == "day")
        {
            cal.add(Calendar.DATE,1);
            dateBeingViewed = cal.getTime();
            LoadListView(spnSearchTags.getSelectedItem().toString(), spnSearchCategory.getSelectedItem().toString(), spnSearchSubCategory.getSelectedItem().toString() , true);
        }
        else if (mode == "month")
        {
            cal.add(Calendar.MONTH,1);
            dateBeingViewed = cal.getTime();
            LoadListView(spnSearchTags.getSelectedItem().toString(),spnSearchCategory.getSelectedItem().toString(), spnSearchSubCategory.getSelectedItem().toString() , false);
        }
    }

    public void getLastDateExpenses() throws JSONException, ParseException, IOException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateBeingViewed);

        if(mode == "day")
        {
            cal.add(Calendar.DATE,-1);
            dateBeingViewed = cal.getTime();
            LoadListView(spnSearchTags.getSelectedItem().toString(),spnSearchCategory.getSelectedItem().toString(), spnSearchSubCategory.getSelectedItem().toString() , true);
        }
        else if (mode == "month")
        {
            cal.add(Calendar.MONTH,-1);
            dateBeingViewed = cal.getTime();
            LoadListView(spnSearchTags.getSelectedItem().toString(),spnSearchCategory.getSelectedItem().toString(), spnSearchSubCategory.getSelectedItem().toString() , false);
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId())
        {
            case R.id.spnExpenseTags:
                String tag = parent.getSelectedItem().toString();
                int catID = getResources().getIdentifier(tag,"array",getActivity().getPackageName());

                ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(v.getContext(), catID, R.layout.cuzdan_spinner_item);
                categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnSearchCategory.setAdapter(categoryAdapter);

                String item2 = spnSearchCategory.getSelectedItem().toString();
                if(item2.contains(" "))
                {
                    item2 = item2.substring(0,item2.indexOf(" ")) + "Gider" + spnSearchTags.getSelectedItem().toString();
                }
                else
                {
                    item2 += "Gider" + spnSearchTags.getSelectedItem().toString() ;
                }
                int subID2 = getResources().getIdentifier(item2,"array",getActivity().getPackageName());

                ArrayAdapter<CharSequence> subCategoryAdapter2 = ArrayAdapter.createFromResource(v.getContext(), subID2, R.layout.cuzdan_spinner_item);
                subCategoryAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnSearchSubCategory.setAdapter(subCategoryAdapter2);


                if(mode == "month")
                {
                    try {
                        LoadListView(parent.getItemAtPosition(position).toString(),spnSearchCategory.getSelectedItem().toString(),spnSearchSubCategory.getSelectedItem().toString(), false);
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
                        LoadListView(parent.getItemAtPosition(position).toString(),spnSearchCategory.getSelectedItem().toString(),spnSearchSubCategory.getSelectedItem().toString(), true);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                break;

            case R.id.spnExpenseCategory:
                String item = parent.getItemAtPosition(position).toString();
                if(item.contains(" "))
                {
                    item = item.substring(0,item.indexOf(" ")) + "Gider" + spnSearchTags.getSelectedItem().toString();
                }
                else
                {
                    item += "Gider" + spnSearchTags.getSelectedItem().toString() ;
                }
                int subID = getResources().getIdentifier(item,"array",getActivity().getPackageName());

                ArrayAdapter<CharSequence> subCategoryAdapter = ArrayAdapter.createFromResource(v.getContext(), subID, R.layout.cuzdan_spinner_item);
                subCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnSearchSubCategory.setAdapter(subCategoryAdapter);

                if(mode == "month")
                {
                    try {
                        LoadListView(spnSearchTags.getSelectedItem().toString(), parent.getItemAtPosition(position).toString(), spnSearchSubCategory.getSelectedItem().toString(), false);
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
                        LoadListView(spnSearchTags.getSelectedItem().toString(),parent.getItemAtPosition(position).toString(),spnSearchSubCategory.getSelectedItem().toString(), true);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                break;

            case R.id.spnExpenseSubCategory:
                if(mode == "month")
                {
                    try {
                        LoadListView(spnSearchTags.getSelectedItem().toString(),spnSearchCategory.getSelectedItem().toString(),parent.getItemAtPosition(position).toString(), false);
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
                        LoadListView(spnSearchTags.getSelectedItem().toString(),spnSearchCategory.getSelectedItem().toString(),parent.getItemAtPosition(position).toString(), true);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                break;

            case R.id.spnSearchExpenseDate:
                if(parent.getItemAtPosition(position).toString().equals("Ay"))
                {
                    mode = "month";
                    try {
                        LoadListView(spnSearchTags.getSelectedItem().toString(), spnSearchCategory.getSelectedItem().toString(), spnSearchSubCategory.getSelectedItem().toString(), false);
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
                        LoadListView(spnSearchTags.getSelectedItem().toString(), spnSearchCategory.getSelectedItem().toString(), spnSearchSubCategory.getSelectedItem().toString(), true);
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

    private void LoadListView(String tag, String category, String subCategory, boolean day) throws ParseException, IOException, JSONException
    {
        ArrayList<Expense> expenses;
        ArrayList<Expense> cleanedExpenses = new ArrayList<Expense>();

        if(day)
        {
            expenses = _user.GetBanker().GetExpensesFromDay(dateBeingViewed);

            for (Expense expense: expenses)
            {
                if (expense.GetCategory().equals(category) && expense.GetSubCategory().equals(subCategory) && expense.GetTurkishStringTag().equals(tag)) {
                    cleanedExpenses.add(expense);
                }
            }

            txtExpenseDate.setText(DateFormatHelper.GetDayText(dateBeingViewed));
        }
        else
        {
            expenses = _user.GetBanker().GetExpensesFromMonth(dateBeingViewed);

            for (Expense expense: expenses)
            {
                if (expense.GetCategory().equals(category) && expense.GetSubCategory().equals(subCategory) && expense.GetTurkishStringTag().equals(tag)) {
                    cleanedExpenses.add(expense);
                }
            }

            txtExpenseDate.setText(DateFormatHelper.GetMonthText(dateBeingViewed, getResources()));
        }

        BigDecimal total = BigDecimal.ZERO;

        for (Expense expense: cleanedExpenses) {
            BigDecimal val = expense.GetAmount();
            total = total.add(val);
        }
        lv.setAdapter(new ExpenseListAdapter(this.getActivity(), cleanedExpenses));

        TextView txtTotalExpense = (TextView)v.findViewById(R.id.txtSearchExpenseTotal);

        txtTotalExpense.setText(total.toString() + "  TL");
        txtTotalExpense.setTextColor(Color.RED);
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ExpenseListAdapter adapter = (ExpenseListAdapter)lv.getAdapter();
            Expense exp = (Expense)adapter.getItem(position);

            Bundle bundle = new Bundle();
            bundle.putString("expense", new Gson().toJson(exp));
            dialog.setArguments(bundle);
            dialog.show(getActivity().getFragmentManager(), "dialog");
        }
    };


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onDismissed() {
        if(mode == "month")
        {
            try {
                LoadListView(spnSearchTags.getSelectedItem().toString(),spnSearchCategory.getSelectedItem().toString(),spnSearchSubCategory.getSelectedItem().toString(), false);
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
                LoadListView(spnSearchTags.getSelectedItem().toString(),spnSearchCategory.getSelectedItem().toString(),spnSearchSubCategory.getSelectedItem().toString(), true);
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
