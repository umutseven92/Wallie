package Fragments;

import Helpers.Banker;
import Helpers.DateFormatHelper;
import Helpers.Expense;
import Helpers.ExpenseLoadListener;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import com.google.gson.Gson;
import com.graviton.Cuzdan.Global;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;


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

        String message = String.format("<b>%s</b> %s<br /><br /><b>%s</b> %s<br /><br /><b>%s</b> %s<br /><br /><b>%s</b> %s<br /><br /><b>%s</b> %s<br /><br /><b>%s</b> %s","Tarih:", DateFormatHelper.GetDayText(expense.GetDate()),"Gider Türü:", expense.GetTurkishStringTag(),"Kategori:", expense.GetCategory(), "Alt Kategori:", expense.GetSubCategory(), "Miktar:", expense.GetAmount().toString(),"Açıklama:", expense.GetDescription());

        if (canDelete) {
            builder.setTitle("Detaylar")
                    .setMessage(Html.fromHtml(message))
                    .setPositiveButton("Sil", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Banker banker = ((Global) getActivity().getApplication()).GetUser().GetBanker();
                            try {
                                banker.DeleteExpense(expense.GetID(), getActivity().getApplication());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            _listener.onDismissed();
                            if (_secondListener != null) {
                                _secondListener.onDismissed();
                            }
                            dismiss();

                        }
                    }).setNegativeButton("Geri", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dismiss();
                }
            });

        } else {
            builder.setTitle("Detaylar")
                    .setMessage(Html.fromHtml(message))
                    .setNegativeButton("Geri", new DialogInterface.OnClickListener() {
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
