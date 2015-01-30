package Fragments;

import Helpers.*;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import com.google.gson.Gson;
import com.graviton.Cuzdan.Global;
import com.graviton.Cuzdan.IncomeStatsActivity;
import com.graviton.Cuzdan.IncomeWizardActivity;
import com.graviton.Cuzdan.R;
import org.json.JSONException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class IncomeFragment extends Fragment implements AdapterView.OnItemSelectedListener, IncomeLoadListener {

    static User _user;
    String mode = "day";
    View infView;
    Date dateBeingViewed;
    ImageButton btnLeftArrow, btnRightArrow, btnAddIncome, btnIncomeStats;
    TextView txtIncomeDate;
    ListView lv;
    IncomeDialogFragment dialog;

    public static final IncomeFragment newInstance()
    {
        IncomeFragment f = new IncomeFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        infView = inflater.inflate(R.layout.incomefragment, container, false);

        Spinner spnDate = (Spinner)infView.findViewById(R.id.spnDateIncome);
        btnLeftArrow = (ImageButton)infView.findViewById(R.id.imgLeftIncome);
        btnRightArrow = (ImageButton)infView.findViewById(R.id.imgRightIncome);

        txtIncomeDate = (TextView)infView.findViewById(R.id.txtIncomeDate);
        btnAddIncome = (ImageButton)infView.findViewById(R.id.btnAddIncome);
        btnIncomeStats = (ImageButton)infView.findViewById(R.id.btnIncomeStats);
        lv = (ListView)infView.findViewById(R.id.lstIncomes);

        dialog = new IncomeDialogFragment();
        dialog.SetListener(this);

        btnLeftArrow.setOnClickListener(onLeftArrowClick);
        btnRightArrow.setOnClickListener(onRightArrowClick);
        btnAddIncome.setOnClickListener(onIncomeClick);
        btnIncomeStats.setOnClickListener(onIncomeStatsClick);
        lv.setOnItemClickListener(onItemClickListener);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(infView.getContext(), R.array.dateArray, R.layout.cuzdanspinneritem);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDate.setAdapter(adapter);
        spnDate.setOnItemSelectedListener(this);

        _user = ((Global) getActivity().getApplication()).GetUser();
        dateBeingViewed = new Date();

        return infView;
    }

    @Override
    public void onResume(){

        if(mode == "day")
        {
            try {
                LoadListView(dateBeingViewed, true);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(mode == "month")
        {
            try {
                LoadListView(dateBeingViewed, false);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onResume();

    }

    OnClickListener onIncomeStatsClick = new View.OnClickListener(){
        @Override
        public void onClick(View v)
        {
            Intent incomeStatsIntent = new Intent(getActivity(), IncomeStatsActivity.class);
            getActivity().startActivity(incomeStatsIntent);
        }
    };

    OnClickListener onLeftArrowClick = new View.OnClickListener() {
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

    OnClickListener onRightArrowClick = new View.OnClickListener() {
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

    OnClickListener onIncomeClick = new View.OnClickListener(){
        @Override
        public void onClick(View v)
        {
            Intent incomeWizardIntent = new Intent(getActivity(), IncomeWizardActivity.class);
            getActivity().startActivity(incomeWizardIntent);
        }
    };


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(position == 0)
        {
            mode = "day";
            try {
                LoadListView(dateBeingViewed, true);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else if(position == 1)
        {
            mode = "month";
            try {
                LoadListView(dateBeingViewed, false);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

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
            LoadListView(dateBeingViewed,true);

        }
        else if (mode.equals("month"))
        {
            cal.add(Calendar.MONTH,1);
            dateBeingViewed = cal.getTime();
            LoadListView(dateBeingViewed,false);

        }
    }

    public void getLastDateIncomes() throws JSONException, ParseException, IOException {

        Calendar cal = Calendar.getInstance();
        cal.setTime(dateBeingViewed);

        if(mode.equals("day"))
        {
            cal.add(Calendar.DATE,-1);
            dateBeingViewed = cal.getTime();
            LoadListView(dateBeingViewed, true);
        }
        else if (mode.equals("month"))
        {
            cal.add(Calendar.MONTH,-1);
            dateBeingViewed = cal.getTime();
            LoadListView(dateBeingViewed,false);

        }
    }

    public void LoadListView(Date date, boolean day) throws JSONException, ParseException, IOException {


        ArrayList<Income> incomes;
        if(day)
        {
            incomes = _user.GetBanker().GetIncomesFromDay(date);
            txtIncomeDate.setText(DateFormatHelper.GetDayText(date));
        }
        else
        {
            incomes = _user.GetBanker().GetIncomesFromMonth(date);
            txtIncomeDate.setText(DateFormatHelper.GetMonthText(date, getResources()));
        }
        BigDecimal total = BigDecimal.ZERO;

        for (int i = 0; i<incomes.size(); i++)
        {
            BigDecimal val = incomes.get(i).GetAmount();
            total = total.add(val);
        }
        lv.setAdapter(new IncomeListAdapter(this.getActivity(),incomes));

        TextView txtTotalIncome = (TextView)infView.findViewById(R.id.txtTotalIncome);

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

    @Override
    public void onDismissed() {

        if (mode.equals("day"))
        {
            try {
                LoadListView(dateBeingViewed, true);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (mode.equals("month"))
        {
            try {
                LoadListView(dateBeingViewed, false);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
