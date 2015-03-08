package Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.view.LayoutInflater;
import android.view.View;
import com.graviton.Cuzdan.R;

/**
 * Created by Umut Seven on 8.3.2015, for Graviton.
 */
public class AccountCreateDialogFragment extends DialogFragment
{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();

        View v = layoutInflater.inflate(R.layout.accout_create,null);
        builder.setView(v).setTitle("Merhaba!").setPositiveButton("Hm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                notify();
            }
        });
        return builder.create();
    }

}
