package wizard.ui;

import Helpers.DecimalDigitsInputFilter;
import Helpers.PriorityInputFilter;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import wizard.model.SavingInfoPage;
import com.graviton.Cuzdan.R;


/**
 * Created by Umut Seven on 11.2.2015, for Graviton.
 */
public class SavingInfoFragment extends Fragment {
    private static final String ARG_KEY = "key";

    private PageFragmentCallbacks mCallbacks;
    private String mKey;
    private SavingInfoPage mPage;
    private TextView mAmountView;
    private TextView mNameView;
    private CheckBox mCheckbox;
    private TextView mPriorityView;

    public static SavingInfoFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        SavingInfoFragment fragment = new SavingInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPage = (SavingInfoPage) mCallbacks.onGetPage(mKey);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.add_saving_fragment_info, container, false);
        ((TextView) rootView.findViewById(android.R.id.title)).setText(mPage.getTitle());

        mAmountView = ((TextView) rootView.findViewById(R.id.saving_amount));
        mAmountView.setText(mPage.getData().getString(SavingInfoPage.AMOUNT_DATA_KEY));
        mAmountView.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(2)});

        mNameView = ((TextView) rootView.findViewById(R.id.saving_name));
        mNameView.setText(mPage.getData().getString(SavingInfoPage.NAME_DATA_KEY));

        mCheckbox = ((CheckBox) rootView.findViewById(R.id.chkRepeating));
        mCheckbox.setText(mPage.getData().getString(SavingInfoPage.REPEAT_DATA_KEY));

        mPriorityView = ((TextView) rootView.findViewById(R.id.txtPriority));
        mPriorityView.setText(mPage.getData().getString(SavingInfoPage.REPEAT_DATA_KEY));
        //mPriorityView.setFilters(new InputFilter[]{new PriorityInputFilter(10)});

        mPage.getData().putBoolean(SavingInfoPage.REPEAT_BOOL_KEY, false);
        mPage.getData().putString(SavingInfoPage.REPEAT_DATA_KEY, "Hayır");
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof PageFragmentCallbacks)) {
            throw new ClassCastException("Activity must implement PageFragmentCallbacks");
        }

        mCallbacks = (PageFragmentCallbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAmountView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(SavingInfoPage.AMOUNT_DATA_KEY,
                        (editable != null) ? editable.toString() : null);

                mPage.notifyDataChanged();
            }
        });

        mNameView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(SavingInfoPage.NAME_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });

        mPriorityView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(SavingInfoPage.PRIORITY_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });

        mCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPage.getData().putBoolean(SavingInfoPage.REPEAT_BOOL_KEY, true);
                    mPage.getData().putString(SavingInfoPage.REPEAT_DATA_KEY, "Evet");
                } else {
                    mPage.getData().putBoolean(SavingInfoPage.REPEAT_BOOL_KEY, false);
                    mPage.getData().putString(SavingInfoPage.REPEAT_DATA_KEY, "Hayır");
                }
                mPage.notifyDataChanged();
            }
        });
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);

        // In a future update to the support library, this should override setUserVisibleHint
        // instead of setMenuVisibility.
        if (mAmountView != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            if (!menuVisible) {
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }
        }
    }

}
