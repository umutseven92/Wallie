package Fragments;

import Helpers.Billing.IabHelper;
import Helpers.Billing.IabResult;
import Helpers.Billing.Purchase;
import Helpers.User;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.graviton.Cuzdan.Global;
import com.graviton.Cuzdan.R;
import com.graviton.Cuzdan.SplashActivity;
import org.json.JSONException;

import java.io.IOException;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Created by Umut Seven on 18.11.2014, for Graviton.
 */
public class AccountFragment extends Fragment {

    static User _user;
    TextView txtName, txtCurrency;
    Switch swcSaving, swcRem, swcStat;
    Button btnSav, btnRem, btnPro;
    int num;
    int refNum;
    private String processId = "";

    public static final AccountFragment newInstance() {
        AccountFragment f = new AccountFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.account_fragment, container, false);
        txtName = (TextView) v.findViewById(R.id.txtName);
        txtCurrency = (TextView) v.findViewById(R.id.txtAccountCurrency);
        swcSaving = (Switch) v.findViewById(R.id.swcAccountSaving);
        swcRem = (Switch) v.findViewById(R.id.swcAccountRem);
        swcStat = (Switch) v.findViewById(R.id.swcStatusNot);
        btnRem = (Button) v.findViewById(R.id.btnAccountRem);
        btnSav = (Button) v.findViewById(R.id.btnAccountSav);
        btnPro = (Button) v.findViewById(R.id.btnPro);

        _user = ((com.graviton.Cuzdan.Global) getActivity().getApplication()).GetUser();

        UpdateControls();

        btnRem.setText(String.valueOf(_user.GetRemNotHour()));
        btnSav.setText(String.valueOf(_user.GetSavingNotHour()));

        swcSaving.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    _user.GetBanker().ToggleNotifications();
                    _user.ToggleSavNotifications();

                    if (isChecked) {
                        btnSav.setEnabled(true);
                    } else {
                        btnSav.setEnabled(false);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        swcRem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    _user.GetBanker().ToggleRemNotifications();
                    _user.ToggleRemNotifications();

                    if (isChecked) {
                        btnRem.setEnabled(true);
                    } else {
                        btnRem.setEnabled(false);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        swcStat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    _user.GetBanker().ToggleStatusNotifications();
                    _user.ToggleStatusNotification(getActivity());
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        btnRem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickHour("rem", Integer.parseInt(btnRem.getText().toString()));
            }
        });

        btnSav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickHour("sav", Integer.parseInt(btnSav.getText().toString()));
            }
        });

        btnPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(getActivity())
                        .setIcon(R.drawable.ic_launcher)
                        .setTitle("Cüzdan Plus")
                        .setMessage("- Reklamları kaldırın.\n\n" +
                                "- Gelecekte çıkacak ve sadece Cüzdan Plus sahiplerine özel olacak fonksiyonlara hiçbir ekstra ücret ödemeden sahip olun.")
                        .setPositiveButton("Satın Al", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

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
                                processId = uId;

                                helper.launchPurchaseFlow(getActivity(), "cuzdan_pro", 8008135, purchaseListener, uId);
                            }

                        })
                        .setNegativeButton("Geri Dön", null)
                        .show();

            }
        });

        return v;
    }

    @Override
    public void onResume() {
        txtName.setText(_user.GetName() + " " + _user.GetLastName());
        txtCurrency.setText(_user.GetCurrency());

        UpdateControls();

        btnRem.setText(String.valueOf(_user.GetRemNotHour()));
        btnSav.setText(String.valueOf(_user.GetSavingNotHour()));

        super.onResume();
    }

    private void UpdateControls() {
        if (_user.GetNotifications().equals("true")) {
            swcSaving.setChecked(true);
            btnSav.setEnabled(true);
        } else if (_user.GetNotifications().equals("false")) {
            swcSaving.setChecked(false);
            btnSav.setEnabled(false);
        }

        if (_user.GetRemNotifications().equals("true")) {
            swcRem.setChecked(true);
            btnRem.setEnabled(true);
        } else if (_user.GetRemNotifications().equals("false")) {
            swcRem.setChecked(false);
            btnRem.setEnabled(false);
        }

        if (_user.GetStatusNotification().equals("true")) {
            swcStat.setChecked(true);
        } else if (_user.GetStatusNotification().equals("false")) {
            swcStat.setChecked(false);
        }

        if (_user.GetVersion() == User.Version.Pro) {
            btnPro.setVisibility(View.INVISIBLE);
        } else {
            btnPro.setVisibility(View.VISIBLE);
        }
    }

    private void PickHour(final String mode, int initial) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View accountView = layoutInflater.inflate(R.layout.number_dialog, null);
        alert.setView(accountView);

        final NumberPicker nmNum = (NumberPicker) accountView.findViewById(R.id.nmNum);
        nmNum.setMinValue(1);
        nmNum.setMaxValue(24);
        nmNum.setValue(initial);

        alert.setTitle("Birikim Saati").setPositiveButton("Seç", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mode.equals("rem")) {
                    refNum = nmNum.getValue();
                    _user.SetRemNotHour(refNum);
                    _user.UpdateRemNotification();
                    try {
                        _user.GetBanker().UpdateRemNotification(refNum);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    btnRem.setText(Integer.toString(refNum));
                } else {
                    num = nmNum.getValue();
                    _user.SetSavingNotHour(num);
                    _user.UpdateSavNotification();
                    try {
                        _user.GetBanker().UpdateNotification(num);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    btnSav.setText(Integer.toString(num));
                }
                dialog.dismiss();
            }
        }).setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        final AlertDialog dialog = alert.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        dialog.show();
    }

    IabHelper.OnIabPurchaseFinishedListener purchaseListener = new IabHelper.OnIabPurchaseFinishedListener() {
        @Override
        public void onIabPurchaseFinished(IabResult result, Purchase info) {
            _user.SetVersion(User.Version.Pro);
            try {
                _user.GetBanker().ToggleVersion();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Uygulamayı yeniden başlat
            Context context = getActivity().getApplicationContext();
            Intent mStartActivity = new Intent(context, SplashActivity.class);
            int mPendingIntentId = 123456;
            PendingIntent mPendingIntent = PendingIntent.getActivity(context, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
            System.exit(0);
        }


    };
}
