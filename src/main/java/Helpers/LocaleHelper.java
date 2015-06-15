package Helpers;

import android.content.Context;
import android.content.res.Configuration;
import com.graviton.Cuzdan.R;

import java.util.Locale;

/**
 * Created by Umut Seven on 15.4.2015, for Graviton.
 */
public class LocaleHelper {

    public static String GetRidOfTurkishCharacters(String turkishString) {
        String clean = "";

        for (int i = 0; i < turkishString.toCharArray().length; i++) {
            switch (turkishString.charAt(i)) {
                case 'ş':
                    clean += 's';
                    break;
                case 'Ş':
                    clean += 'S';
                    break;
                case 'ü':
                    clean += 'u';
                    break;
                case 'Ü':
                    clean += 'U';
                    break;
                case 'ç':
                    clean += 'c';
                    break;
                case 'Ç':
                    clean += 'C';
                    break;
                case 'Ö':
                    clean += 'O';
                    break;
                case 'Ğ':
                    clean += 'G';
                    break;
                case 'ö':
                    clean += 'o';
                    break;
                case 'ı':
                    clean += 'i';
                    break;
                case 'ğ':
                    clean += 'g';
                    break;
                case 'İ':
                    clean += 'I';
                    break;
                default:
                    clean += turkishString.charAt(i);
                    break;
            }
        }

        return clean;
    }

    public static void SetAppLocale(String local, Context context) {
        Locale locale = new Locale(local);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config,
                context.getResources().getDisplayMetrics());
    }

    public static String GetExpenseLocale(Context context, String tag) {
        if (tag.equals("home")) {
            return context.getResources().getString(R.string.tag_home);
        } else {
            return context.getResources().getString(R.string.tag_personal);
        }
    }

}
