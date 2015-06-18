package Fragments;

import Helpers.LocaleHelper;
import Helpers.User;
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
import android.widget.*;
import com.graviton.Cuzdan.R;
import com.graviton.Cuzdan.SplashActivity;
import org.json.JSONException;

import java.io.IOException;

/**
 * Created by Umut Seven on 18.11.2014, for Graviton.
 */
public class AccountFragment extends Fragment {

    static User _user;
    TextView txtName, txtCurrency;
    Switch swcSaving, swcRem, swcStat, swcAutoBackup;
    Button btnSav, btnRem, btnBackup;
    ImageView imgBackup;
    ProgressBar pbar;
    RelativeLayout lytPro;
    Spinner spnLanguage;
    int num;
    int refNum;
    boolean first = true;

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
        btnBackup = (Button) v.findViewById(R.id.btnBackup);
        imgBackup = (ImageView) v.findViewById(R.id.imgBackup);
        pbar = (ProgressBar) v.findViewById(R.id.progressBar);
        swcAutoBackup = (Switch) v.findViewById(R.id.swcAutoBackup);
        spnLanguage = (Spinner) v.findViewById(R.id.spnLanguage);
        lytPro = (RelativeLayout) v.findViewById(R.id.lytProOptions);

        _user = ((com.graviton.Cuzdan.Global) getActivity().getApplication()).GetUser();

        UpdateControls();

        btnRem.setText(String.valueOf(_user.GetRemNotHour()));
        btnSav.setText(String.valueOf(_user.GetSavingNotHour()));

        spnLanguage.setAdapter(ArrayAdapter.createFromResource(v.getContext(), R.array.languages, android.R.layout.simple_spinner_dropdown_item));


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

        swcAutoBackup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                try {
                    _user.GetBanker().ToggleAutoBackup();
                    _user.ToggleAutoBackup();
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


        btnBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    pbar.setVisibility(View.INVISIBLE);

                    String result = _user.GetBanker().BackupUserData();
                    imgBackup.setVisibility(View.VISIBLE);
                    if (result.equals("SUCCESS")) {
                        imgBackup.setImageResource(R.drawable.tick);
                    } else if (result.equals("FAILURE")) {
                        imgBackup.setImageResource(R.drawable.cross);
                    }
                    pbar.setVisibility(View.INVISIBLE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        spnLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (first) {
                    first = false;
                    return;
                }
                if (i == 0) {
                    LocaleHelper.SetAppLocale("en", getActivity().getBaseContext());
                    _user.SetLocale("en");
                    try {
                        _user.GetBanker().ChangeUserLocale("en");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    LocaleHelper.SetAppLocale("tr", getActivity().getBaseContext());
                    _user.SetLocale("tr");

                    try {
                        _user.GetBanker().ChangeUserLocale("tr");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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

        if (_user.GetVersion() == User.Version.Free) {
            lytPro.setVisibility(View.GONE);
        } else {
            lytPro.setVisibility(View.VISIBLE);

            if (_user.GetAutoBackup().equals("true")) {
                swcAutoBackup.setChecked(true);
            } else {
                swcAutoBackup.setChecked(false);
            }
        }

        if (_user.GetLocale().equals("en")) {
            spnLanguage.setSelection(0);
        } else {
            spnLanguage.setSelection(1);
        }

        imgBackup.setVisibility(View.INVISIBLE);
        pbar.setVisibility(View.INVISIBLE);


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

}
