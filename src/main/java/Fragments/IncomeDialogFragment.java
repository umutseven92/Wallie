package Fragments;

import Helpers.Banker;
import Helpers.DateFormatHelper;
import Helpers.Income;
import Helpers.IncomeLoadListener;
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

        String message = String.format("<b>%s</b> %s<br /><br /><b>%s</b> %s<br /><br /><b>%s</b> %s<br /><br /><b>%s</b> %s<br /><br /><b>%s</b> %s", "Tarih:", DateFormatHelper.GetDayText(income.GetDate()), "Kategori:", income.GetCategory(), "Alt Kategori:", income.GetSubCategory(), "Miktar:", income.GetAmount().toString() + " " + getString(R.string.currency), "Açıklama:", income.GetDescription());

        if (canDelete) {
            builder.setTitle("Detaylar")
                    .setMessage(Html.fromHtml(message))
                    .setPositiveButton("Sil", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                Banker banker = ((Global) getActivity().getApplication()).GetUser().GetBanker();
                                banker.DeleteIncome(income.GetID());
                                _listener.onDismissed();
                                dismiss();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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


    private IncomeLoadListener _listener;

    public void SetListener(IncomeLoadListener listener) {
        _listener = listener;
    }


}
