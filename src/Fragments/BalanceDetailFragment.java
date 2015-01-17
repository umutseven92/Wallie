package Fragments;

import Helpers.Balance;
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
public class BalanceDetailFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Bundle bundle = getArguments();
        Balance balance = new Gson().fromJson(bundle.getString("balance"), Balance.class);

        builder.setTitle( Html.fromHtml("<font color='#f44'>Detaylar</font>"))
                .setMessage(String.format("Kategori: %s\n\nAlt Kategori: %s\n\nAçıklama: %s",balance.GetCategory(),balance.GetSubCategory(),balance.GetDescription()))
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
