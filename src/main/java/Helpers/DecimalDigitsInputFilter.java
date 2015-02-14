package Helpers;

import android.text.InputFilter;
import android.text.Spanned;


/**
 * Created by Umut Seven on 22.1.2015, for Graviton.
 * <p/>
 * Kullanici miktar girerken noktadan sonra en fazla 2 rakam girebilmeli.
 * Ayrica miktar noktadan sonrasi 12 karakter olmali (nokta dahil).
 * Limiti bu class ustunden sagliyoruz.
 */
public class DecimalDigitsInputFilter implements InputFilter {

    private final int decimalDigits;

    public DecimalDigitsInputFilter(int decimalDigits) {
        this.decimalDigits = decimalDigits;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

        int dotPos = -1;
        int len = dest.length();

        if (len >= 12) {
            return "";
        }
        for (int i = 0; i < len; i++) {
            char c = dest.charAt(i);
            if (c == '.' || c == ',') {
                dotPos = i;
                break;
            }
        }

        if (dotPos >= 0) {

            // protects against many dots
            if (source.equals(".") || source.equals(",")) {
                return "";
            }
            // if the text is entered before the dot
            if (dend <= dotPos) {
                return null;
            }
            if (len - dotPos > decimalDigits) {
                return "";
            }
        }

        return null;
    }
}
