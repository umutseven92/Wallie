package Fragments;

import Helpers.ExpenseLoadListener;
import Helpers.IncomeLoadListener;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Umut Seven on 1.2.2015, for Graviton.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dg = new DatePickerDialog(getActivity(), this, year, month, day);
        dg.setTitle("Takvim");
        dg.getDatePicker().setMaxDate(new Date().getTime());
        return dg;
    }

    private IncomeLoadListener _incomeListener;
    private ExpenseLoadListener _expenseListener;
    private String mode = "income";

    public void SetIncomeListener(IncomeLoadListener listener) {
        _incomeListener = listener;
    }

    public void SetExpenseListener(ExpenseLoadListener listener) {
        _expenseListener = listener;
    }

    public void SetMode(String mode) {
        this.mode = mode;
    }

    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, monthOfYear, dayOfMonth);
        if (mode.equals("income")) {
            _incomeListener.onDateSelected(cal.getTime());
        } else if (mode.equals("expense")) {
            _expenseListener.onDateSelected(cal.getTime());
        }
    }
}
