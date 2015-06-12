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
public class IncomeDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Bundle bundle = getArguments();
        final Income income = new Gson().fromJson(bundle.getString("income"), Income.class);
        boolean canDelete = bundle.getBoolean("canDelete");
        User user = ((Global) getActivity().getApplication()).GetUser();
        String message = String.format("<b>%s</b> %s<br /><br /><b>%s</b> %s<br /><br /><b>%s</b> %s<br /><br /><b>%s</b> %s<br /><br /><b>%s</b> %s", getString(R.string.date), DateFormatHelper.GetDayText(income.GetDate()), getString(R.string.category), income.GetCategory(), getString(R.string.subCategory), income.GetSubCategory(), getString(R.string.amount), income.GetAmount().toString() + " " + user.GetCurrency(), getString(R.string.description), income.GetDescription());

        if (canDelete) {
            builder.setTitle(getString(R.string.details))
                    .setMessage(Html.fromHtml(message))
                    .setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {

                                User user = ((Global) getActivity().getApplication()).GetUser();
                                Banker banker = user.GetBanker();
                                banker.DeleteIncome(income.GetID());
                                _listener.onDismissed();
                                dismiss();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    })
                    .setNeutralButton(getString(R.string.repeat), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            User user = ((Global) getActivity().getApplication()).GetUser();
                            Banker banker = user.GetBanker();
                            try {
                                banker.RepeatIncome(income);
                                _listener.onDismissed();
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


    private IncomeLoadListener _listener;

    public void SetListener(IncomeLoadListener listener) {
        _listener = listener;
    }


}
