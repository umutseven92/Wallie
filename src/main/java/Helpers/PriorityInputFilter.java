package Helpers;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * Created by Umut Seven on 19.2.2015, for Graviton.
 */
public class PriorityInputFilter implements InputFilter{

    private int _maxValue;

    @Deprecated
    public PriorityInputFilter(int maxValue)
    {
        this._maxValue = maxValue;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

        return null;
    }
}
