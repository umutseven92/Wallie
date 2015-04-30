package Helpers;

import com.google.android.gms.ads.AdRequest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Umut Seven on 29.4.2015.
 */
public class AdHelper {

    public static AdRequest RequestAd(String deviceId) {
        return new AdRequest.Builder()
                .build();
    }

    public static String md5(final String s) throws NoSuchAlgorithmException {
        // Create MD5 Hash
        MessageDigest digest = java.security.MessageDigest
                .getInstance("MD5");
        digest.update(s.getBytes());
        byte messageDigest[] = digest.digest();

        // Create Hex String
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < messageDigest.length; i++) {
            String h = Integer.toHexString(0xFF & messageDigest[i]);
            while (h.length() < 2)
                h = "0" + h;
            hexString.append(h);
        }
        return hexString.toString();

    }
}
