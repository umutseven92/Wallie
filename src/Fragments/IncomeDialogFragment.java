package Fragments;

import Helpers.Income;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import com.google.gson.Gson;

/**
 * Created by Umut on 17.1.2015.
 */
public class IncomeDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Bundle bundle = getArguments();
        Income income = new Gson().fromJson(bundle.getString("income"), Income.class);

        builder.setTitle( Html.fromHtml("Detaylar"))
                .setMessage(String.format("Kategori: %s\n\nAlt Kategori: %s\n\nAçıklama: %s",income.GetCategory(),income.GetSubCategory(),income.GetDescription()))
                .setPositiveButton("Sil", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setNegativeButton("Geri", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }
}
