package com.graviton.Cuzdan;

import Helpers.NotificationHelper;
import Helpers.User;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.Date;
import java.util.jar.JarException;

/**
 * Created by Umut Seven on 3.4.2015, for Graviton.
 */
public class OnBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {

            try {

                User user = null;
                String fileName = context.getString(R.string.configVersion);

                File file = new File(context.getFilesDir(), fileName);
                if (!file.exists()) {
                    return;
                }
                StringBuilder sb = new StringBuilder();
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                    sb.append("\n");
                }

                user = new User(new JSONObject(sb.toString()), file.getAbsolutePath(), null);

                NotificationHelper.SetPermaNotification(context,user.GetBanker().GetBalance(new Date(),true),user.GetCurrency());
            }

             catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
