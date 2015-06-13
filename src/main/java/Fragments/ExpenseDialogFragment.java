package Fragments;

import Helpers.*;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import com.google.gson.Gson;
import com.graviton.Cuzdan.Global;
import com.graviton.Cuzdan.R;
import org.json.JSONException;

import java.io.IOException;


/**
 * Created by Umut Seven on 17.1.2015, for Graviton.
 */
public class ExpenseDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Bundle bundle = getArguments();
        final Expense expense = new Gson().fromJson(bundle.getString("expense"), Expense.class);
        boolean canDelete = bundle.getBoolean("canDelete");
        User user = ((Global) getActivity().getApplication()).GetUser();
        String message = String.format("<b>%s</b> %s<br /><br /><b>%s</b> %s<br /><br /><b>%s</b> %s<br /><br /><b>%s</b> %s<br /><br /><b>%s</b> %s<br /><br /><b>%s</b> %s", getString(R.string.date), DateFormatHelper.GetDayText(expense.GetDate()), getString(R.string.expense_type) + ":", expense.GetTurkishStringTag(), getString(R.string.category), expense.GetCategory(), getString(R.string.subCategory), expense.GetSubCategory(), getString(R.string.amount), expense.GetAmount().toString() + " " + user.GetCurrency(), getString(R.string.description), expense.GetDescription());

        if (canDelete) {
            builder.setTitle(getString(R.string.details))
                    .setMessage(Html.fromHtml(message))
                    .setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            User user = ((Global) getActivity().getApplication()).GetUser();
                            Banker banker = user.GetBanker();

                            try {
                                banker.DeleteExpense(expense.GetID());

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            _listener.onDismissed();
                            if (_secondListener != null) {
                                _secondListener.onDismissed();
                            }
                            dismiss();

                        }
                    })
                    .setNeutralButton(getString(R.string.repeat), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            User user = ((Global) getActivity().getApplication()).GetUser();
                            Banker banker = user.GetBanker();
                            try {
                                banker.RepeatExpense(expense);
                                _listener.onDismissed();
                                if (_secondListener != null) {
                                    _secondListener.onDismissed();
                                }
                                dismiss();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    })
                    .setNegativeButton(getString(R.string.back), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dismiss();
                        }
                    });

        } else {
            builder.setTitle(getString(R.string.details))
                    .setMessage(Html.fromHtml(message))
                    .setNegativeButton(getString(R.string.back), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dismiss();
                        }
                    });
        }

        return builder.create();
    }

    private ExpenseLoadListener _listener;
    private ExpenseLoadListener _secondListener;

    public void SetListener(ExpenseLoadListener listener) {
        _listener = listener;
    }

    public void SetSecondListener(ExpenseLoadListener listener) {
        _secondListener = listener;
    }
}
