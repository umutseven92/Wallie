package Fragments;

import Helpers.*;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import com.example.Cuzdan.Global;
import com.example.Cuzdan.R;
import java.math.BigDecimal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class IncomeFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    static User _user;
    String mode = "day";
    View infView;
    Date dateBeingViewed;
    ImageButton leftArrow;
    ImageButton rightArrow;
    TextView txtIncomeDate;

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
        leftArrow = (ImageButton)infView.findViewById(R.id.imgLeftIncome);
        rightArrow = (ImageButton)infView.findViewById(R.id.imgRightIncome);
        txtIncomeDate = (TextView)infView.findViewById(R.id.txtIncomeDate);

        leftArrow.setOnClickListener(onLeftArrowClick);
        rightArrow.setOnClickListener(onRightArrowClick);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(infView.getContext(), R.array.dateArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDate.setAdapter(adapter);
        spnDate.setOnItemSelectedListener(this);

        _user = ((Global) getActivity().getApplication()).GetUser();
        dateBeingViewed = new Date();

        return infView;
    }

    OnClickListener onLeftArrowClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getLastDateIncomes();
        }
    };

    OnClickListener onRightArrowClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getNextDateIncomes();
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(position == 0)
        {
            mode = "day";
            LoadListView(dateBeingViewed, true);

        }
        else if(position == 1)
        {
            mode = "month";
            LoadListView(dateBeingViewed, false);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void getNextDateIncomes()
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateBeingViewed);

        if(mode == "day")
        {
            cal.add(Calendar.DATE,1);
            dateBeingViewed = cal.getTime();
            LoadListView(dateBeingViewed,true);

        }
        else if (mode == "month")
        {
            cal.add(Calendar.MONTH,1);
            dateBeingViewed = cal.getTime();
            LoadListView(dateBeingViewed,false);

        }
    }

    public void getLastDateIncomes()
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateBeingViewed);

        if(mode == "day")
        {
            cal.add(Calendar.DATE,-1);
            dateBeingViewed = cal.getTime();
            LoadListView(dateBeingViewed, true);
        }
        else if (mode == "month")
        {
            cal.add(Calendar.MONTH,-1);
            dateBeingViewed = cal.getTime();
            LoadListView(dateBeingViewed,false);

        }
    }

    public void LoadListView(Date date, boolean day)
    {
        ArrayList<Income> incomes;
        if(day)
        {
            incomes = _user.GetBanker().GetIncomesFromDay(date);
            UpdateDayText();
        }
        else
        {
            incomes = _user.GetBanker().GetIncomesFromMonth(date);
            UpdateMonthText();
        }
        BigDecimal total = BigDecimal.ZERO;

        for (int i = 0; i<incomes.size(); i++)
        {
            BigDecimal val = incomes.get(i).GetAmount();
            total = total.add(val);
        }
        ListView lv = (ListView)infView.findViewById(R.id.lstIncomes);
        TextView txtTotalIncome = (TextView)infView.findViewById(R.id.txtTotalIncome);
        lv.setAdapter(new IncomeListAdapter(this.getActivity(),incomes));

        txtTotalIncome.setText(total.toString() + " TL");
        txtTotalIncome.setTextColor(Color.GREEN);
    }




}
