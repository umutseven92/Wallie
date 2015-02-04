package Helpers;


import java.util.Date;

/**
 * Created by Umut Seven on 18.1.2015, for Graviton.
 */
public interface ExpenseLoadListener {
    public void onDismissed();

    public void onDateSelected(Date date);
}
