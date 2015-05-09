package Fragments;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.graviton.Cuzdan.R;
import com.graviton.Cuzdan.SplashActivity;

/**
 * Created by Umut Seven on 14.4.2015, for Graviton.
 */
public class AboutFragment extends Fragment {

    public static final AboutFragment newInstance() {
        AboutFragment f = new AboutFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.about_fragment, container, false);

        TextView txtVersion = (TextView) v.findViewById(R.id.txtAboutVersion);
        txtVersion.setText(R.string.version);

        txtVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(getActivity())
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setTitle("Restart")
                        .setMessage("Device will restart.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Context context = getActivity().getApplicationContext();
                                Intent mStartActivity = new Intent(context, SplashActivity.class);
                                int mPendingIntentId = 123456;
                                PendingIntent mPendingIntent = PendingIntent.getActivity(context, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
                                AlarmManager mgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                                System.exit(0);
                            }

                        })
                        .show();
            }
        });

        return v;
    }
}
