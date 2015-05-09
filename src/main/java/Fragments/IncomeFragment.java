package Fragments;

import Helpers.*;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ActionViewTarget;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.gson.Gson;
import com.graviton.Cuzdan.*;
import org.json.JSONException;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class IncomeFragment extends Fragment implements AdapterView.OnItemSelectedListener, IncomeLoadListener, OnShowcaseEventListener, IncomeAddListener {

    static User _user;
    String mode = "day";
    View infView;
    Date dateBeingViewed;
    ImageButton btnLeftArrow, btnRightArrow, btnAddIncome, btnIncomeStats, btnCalendar;
    TextView txtIncomeDate;
    ListView lv;
    IncomeDialogFragment dialog;
    DatePickerFragment datePickerFragment;
    private int tutorialCount = 0;
    private InterstitialAd ad;
    private boolean first = false;
    String deviceId = null;

    public static final IncomeFragment newInstance() {
        IncomeFragment f = new IncomeFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        first = ((Global) this.getActivity().getApplication()).GetFirst();

        infView = inflater.inflate(R.layout.income_fragment, container, false);


        Spinner spnDate = (Spinner) infView.findViewById(R.id.spnDateIncome);
        btnLeftArrow = (ImageButton) infView.findViewById(R.id.imgLeftIncome);
        btnRightArrow = (ImageButton) infView.findViewById(R.id.imgRightIncome);
        btnCalendar = (ImageButton) infView.findViewById(R.id.btnCalendar);

        txtIncomeDate = (TextView) infView.findViewById(R.id.txtIncomeDate);
        btnAddIncome = (ImageButton) infView.findViewById(R.id.btnAddIncome);
        btnIncomeStats = (ImageButton) infView.findViewById(R.id.btnIncomeStats);
        lv = (ListView) infView.findViewById(R.id.lstIncomes);

        datePickerFragment = new DatePickerFragment();
        datePickerFragment.SetIncomeListener(this);

        dialog = new IncomeDialogFragment();
        dialog.SetListener(this);

        ((Global) this.getActivity().getApplication()).incomeAddListener = this;

        btnCalendar.setOnClickListener(onCalendarClick);
        btnLeftArrow.setOnClickListener(onLeftArrowClick);
        btnRightArrow.setOnClickListener(onRightArrowClick);
        btnAddIncome.setOnClickListener(onIncomeClick);
        btnIncomeStats.setOnClickListener(onIncomeStatsClick);

        lv.setOnItemClickListener(onItemClickListener);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(infView.getContext(), R.array.dateArray, R.layout.cuzdan_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDate.setAdapter(adapter);
        spnDate.setOnItemSelectedListener(this);

        _user = ((Global) getActivity().getApplication()).GetUser();
        dateBeingViewed = new Date();

        if (first) {
            // Tutorial
            // Bitince,
            ((Global) this.getActivity().getApplication()).SetFirst(false);
            new ShowcaseView.Builder(getActivity(), true)
                    .setTarget(new ViewTarget(infView.findViewById(R.id.btnAddIncome)))
                    .setContentText("Buradan gelir ekleyebilirsiniz.")
                    .setStyle(R.style.ShowcaseView_Cuzdan_Next)
                    .hasManualPosition(true)
                    .xPostion(150)
                    .yPostion(850)
                    .setShowcaseEventListener(this).build();
        }

        if (_user.GetVersion() == User.Version.Free) {
            // Reklamlar
            ad = new InterstitialAd(this.getActivity());
            ad.setAdUnitId(getString(R.string.incomeAdKey));

            deviceId = getString(R.string.testDeviceId);

            AdRequest adRequest = AdHelper.RequestAd(deviceId);
            ad.loadAd(adRequest);
        }

        return infView;
    }


    @Override
    public void onResume() {

        if (mode.equals("day")) {
            try {
                LoadListView(dateBeingViewed, true);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (mode.equals("month")) {
            try {
                LoadListView(dateBeingViewed, false);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (_user.GetVersion() == User.Version.Free) {
            if (!ad.isLoaded()) {
                AdRequest adRequest = AdHelper.RequestAd(deviceId);
                ad.loadAd(adRequest);
            }
        }


        super.onResume();

    }

    OnClickListener onCalendarClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            datePickerFragment.SetMode("income");
            datePickerFragment.show(getActivity().getFragmentManager(), "datepicker");
        }
    };

    OnClickListener onIncomeStatsClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent incomeStatsIntent = new Intent(getActivity(), IncomeStatsActivity.class);
            getActivity().startActivity(incomeStatsIntent);
        }
    };

    OnClickListener onLeftArrowClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                getLastDateIncomes();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    OnClickListener onRightArrowClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                getNextDateIncomes();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    OnClickListener onIncomeClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent incomeWizardIntent = new Intent(getActivity(), IncomeWizardActivity.class);
            getActivity().startActivity(incomeWizardIntent);
        }
    };


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Date today = new Date();
        if (position == 0) {
            mode = "day";
            try {
                if (dateBeingViewed.getMonth() == today.getMonth()) {
                    dateBeingViewed.setDate(today.getDate());
                }
                LoadListView(dateBeingViewed, true);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (position == 1) {
            mode = "month";
            try {
                dateBeingViewed.setDate(1);
                LoadListView(dateBeingViewed, false);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void getNextDateIncomes() throws JSONException, ParseException, IOException {
        Date today = new Date();
        Calendar calToday = Calendar.getInstance();
        calToday.setTime(today);

        Calendar cal = Calendar.getInstance();
        cal.setTime(dateBeingViewed);

        int calDayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        int calTodayDayOfMonth = calToday.get(Calendar.DAY_OF_MONTH);

        int calMonth = cal.get(Calendar.MONTH);
        int calTodayMonth = calToday.get(Calendar.MONTH);

        int calYear = cal.get(Calendar.YEAR);
        int calTodayYear = calToday.get(Calendar.YEAR);

        if (mode.equals("month")) {
            if (calMonth == calTodayMonth && calYear == calTodayYear) {
                return;
            }
        } else if (mode.equals("day")) {
            if (calDayOfMonth == calTodayDayOfMonth && calMonth == calTodayMonth && calYear == calTodayYear) {
                return;
            }

        }

        if (mode.equals("day")) {
            cal.add(Calendar.DATE, 1);
            dateBeingViewed = cal.getTime();
            LoadListView(dateBeingViewed, true);

        } else if (mode.equals("month")) {
            cal.add(Calendar.MONTH, 1);
            dateBeingViewed = cal.getTime();
            LoadListView(dateBeingViewed, false);

        }
    }

    public void getLastDateIncomes() throws JSONException, ParseException, IOException {

        Calendar cal = Calendar.getInstance();
        cal.setTime(dateBeingViewed);

        if (mode.equals("day")) {
            cal.add(Calendar.DATE, -1);
            dateBeingViewed = cal.getTime();
            LoadListView(dateBeingViewed, true);
        } else if (mode.equals("month")) {
            cal.add(Calendar.MONTH, -1);
            dateBeingViewed = cal.getTime();
            LoadListView(dateBeingViewed, false);

        }
    }

    public void LoadListView(Date date, boolean day) throws JSONException, ParseException, IOException {

        ArrayList<Income> incomes;
        if (day) {
            incomes = _user.GetBanker().GetIncomesFromDay(date);
            txtIncomeDate.setText(DateFormatHelper.GetDayText(date));
        } else {
            incomes = _user.GetBanker().GetIncomesFromMonth(date);
            txtIncomeDate.setText(DateFormatHelper.GetMonthText(date, getResources()));
        }
        BigDecimal total = BigDecimal.ZERO;

        for (Income income : incomes) {
            BigDecimal val = income.GetAmount();
            total = total.add(val);
        }
        lv.setAdapter(new IncomeListAdapter(this.getActivity(), incomes, _user.GetCurrency()));

        TextView txtTotalIncome = (TextView) infView.findViewById(R.id.txtTotalIncome);

        txtTotalIncome.setText(total.toString() + " " + _user.GetCurrency());
        txtTotalIncome.setTextColor(getResources().getColor(R.color.cuzdan_green));

        if (_user.GetStatusNotification().equals("true")) {
            NotificationHelper.SetPermaNotification(getActivity(), _user.GetBanker().GetBalance(new Date(), true), _user.GetCurrency());
        }
        try {
            WidgetHelper.UpdateInfoWithUser(getActivity(), _user);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            IncomeListAdapter adapter = (IncomeListAdapter) lv.getAdapter();
            Income inc = (Income) adapter.getItem(position);

            Bundle bundle = new Bundle();
            bundle.putBoolean("canDelete", true);
            bundle.putString("income", new Gson().toJson(inc));

            dialog.setArguments(bundle);
            dialog.show(getActivity().getFragmentManager(), "dialog");
        }
    };

    @Override
    public void onDismissed() {

        if (mode.equals("day")) {
            try {
                LoadListView(dateBeingViewed, true);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (mode.equals("month")) {
            try {
                LoadListView(dateBeingViewed, false);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Deprecated
    public boolean IsInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com"); //You can replace it with your name

            if (ipAddr.equals("")) {
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            return false;
        }

    }


    @Override
    public void onDateSelected(Date date) {
        dateBeingViewed = date;

        if (mode.equals("day")) {
            try {
                LoadListView(dateBeingViewed, true);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (mode.equals("month")) {
            try {
                LoadListView(dateBeingViewed, false);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onShowcaseViewHide(ShowcaseView showcaseView) {
        switch (tutorialCount) {
            case 0:
                new ShowcaseView.Builder(getActivity(), true)
                        .setTarget(new ViewTarget(infView.findViewById(R.id.spnDateIncome)))
                        .setContentText("Buradan kayıtlarınız gün veya ay olarak listeleyebilir, önceki ve sonraki tarihlere geçiş yapabilirsiniz.")
                        .setStyle(R.style.ShowcaseView_Cuzdan_Next)
                        .hasManualPosition(true)
                        .yPostion(530)
                        .xPostion(70)
                        .setShowcaseEventListener(this)
                        .build();
                tutorialCount++;
                break;
            case 1:
                new ShowcaseView.Builder(getActivity(), true)
                        .setTarget(new ViewTarget(infView.findViewById(R.id.btnCalendar)))
                        .setContentText("Buradan belirli bir tarihteki kayıtları listeleyebilirsiniz.")
                        .setStyle(R.style.ShowcaseView_Cuzdan_Next)
                        .hasManualPosition(true)
                        .xPostion(140)
                        .yPostion(800)
                        .setShowcaseEventListener(this)
                        .build();
                tutorialCount++;
                break;
            case 2:
                new ShowcaseView.Builder(getActivity(), true)
                        .setTarget(new ViewTarget(infView.findViewById(R.id.btnIncomeStats)))
                        .setContentText("Buradan kayıtlarınızı detaylı olarak inceleyebilirsiniz.")
                        .setStyle(R.style.ShowcaseView_Cuzdan_Next)
                        .hasManualPosition(true)
                        .xPostion(80)
                        .yPostion(820)
                        .setShowcaseEventListener(this)
                        .build();
                tutorialCount++;
                break;
            case 3:
                new ShowcaseView.Builder(getActivity(), true)
                        .setTarget(new ViewTarget(infView.findViewById(R.id.txtTutRight)))
                        .setContentText("Sekmeler arasında geçiş yapmak için sağa veya sola kaydırın.")
                        .setStyle(R.style.ShowcaseView_Cuzdan_Next)
                        .hasManualPosition(true)
                        .xPostion(80)
                        .yPostion(620)
                        .setShowcaseEventListener(this)
                        .build();
                tutorialCount++;
                break;

            case 4:
                new ShowcaseView.Builder(getActivity(), true)
                        .setTarget(new ActionViewTarget(this.getActivity(), ActionViewTarget.Type.HOME))
                        .setContentText("Menüye buradan ulaşabilirsiniz.")
                        .setStyle(R.style.ShowcaseView_Cuzdan)
                        .hasManualPosition(false)
                        .setShowcaseEventListener(this)
                        .build();
                tutorialCount++;
                break;
            default:
                break;
        }
    }

    @Override
    public void onShowcaseViewDidHide(ShowcaseView showcaseView) {

    }

    @Override
    public void onShowcaseViewShow(ShowcaseView showcaseView) {

    }

    @Override
    public void onAdded() {

        if (_user.GetVersion() == User.Version.Free) {
            if (!ad.isLoaded()) {
                AdRequest adRequest = AdHelper.RequestAd(deviceId);
                ad.loadAd(adRequest);
            }

            ad.show();
        }
    }
}
