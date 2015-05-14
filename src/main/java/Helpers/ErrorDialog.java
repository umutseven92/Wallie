package Helpers;

import Helpers.Billing.IabHelper;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import com.graviton.Cuzdan.Global;

/**
 * Created by Umut Seven on 13.5.2015.
 */
public class ErrorDialog {

    public static void ShowErrorDialog(final Application _application, Exception ex, String simple, String detail) {

        String message;

        if (ex == null && detail != null) {
            message = "Bir hata oluştu. Hata:\n" + simple + "\nDetaylar:\n" + detail + "\nLütfen bu hata mesajınız support@graviton.io adresine gönderin.";
        } else if (ex != null && detail == null) {
            message = "Bir hata oluştu. Hata:\n" + simple + "\nDetaylar:\n" + ex.getMessage() + "\nLütfen bu hata mesajınız support@graviton.io adresine gönderin.";
        } else {
            message = "Bir hata oluştu.";
        }

        try {

            new AlertDialog.Builder(_application.getApplicationContext())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Hata")
                    .setMessage(message)
                    .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            IabHelper iab = ((Global) _application).iabHelper;

                            if (iab != null) {
                                iab.dispose();
                            }
                        }

                    })
                    .show();
        } catch (Exception e) {
            return;
        }

    }
}
