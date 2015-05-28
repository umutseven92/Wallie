package Fragments;

import Helpers.Billing.IabHelper;
import Helpers.Billing.IabResult;
import Helpers.Billing.Purchase;
import Helpers.ErrorDialog;
import Helpers.User;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.graviton.Cuzdan.Global;
import com.graviton.Cuzdan.R;
import com.graviton.Cuzdan.SplashActivity;

import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Created by Umut Seven on 26.5.2015.
 */
public class PlusFragment extends Fragment {

    Button btnPro;
    User _user;

    public static final PlusFragment newInstance() {
        PlusFragment f = new PlusFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.plus_fragment, container, false);

        _user = ((com.graviton.Cuzdan.Global) getActivity().getApplication()).GetUser();
        btnPro = (Button) v.findViewById(R.id.btnPro);

        btnPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                IabHelper helper = ((Global) getActivity().getApplication()).iabHelper;

                String uId = "";

                Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
                Account[] accounts = AccountManager.get(getActivity().getApplicationContext()).getAccounts();
                for (Account account : accounts) {
                    if (emailPattern.matcher(account.name).matches()) {
                        String possibleEmail = account.name;
                        uId += possibleEmail + "+";
                    }
                }

                uId += UUID.randomUUID().toString().substring(0, 8);

                helper.launchPurchaseFlow(getActivity(), "cuzdan_pro", 8008135, purchaseListener, uId);

            }
        });
        return v;
    }

    @Override
    public void onResume() {

        if (_user.GetVersion() == User.Version.Free) {
            btnPro.setVisibility(View.VISIBLE);
        } else {
            btnPro.setVisibility(View.VISIBLE);
        }

        super.onResume();
    }

    IabHelper.OnIabPurchaseFinishedListener purchaseListener = new IabHelper.OnIabPurchaseFinishedListener() {
        @Override
        public void onIabPurchaseFinished(IabResult result, Purchase info) {
            if (result.isSuccess()) {
                _user.SetVersion(User.Version.Pro);
                try {
                    _user.GetBanker().ToggleVersion();
                } catch (Exception e) {
                    e.printStackTrace();
                    ErrorDialog.ShowErrorDialog(getActivity().getApplication(), e, "Cüzdan Plus'a geçilirken hata oluştu.", null);
                }

                // Uygulamayı yeniden başlat
                Context context = getActivity().getApplicationContext();
                Intent mStartActivity = new Intent(context, SplashActivity.class);
                int mPendingIntentId = 123456;
                PendingIntent mPendingIntent = PendingIntent.getActivity(context, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                getActivity().finish();

            }
        }


    };
}
