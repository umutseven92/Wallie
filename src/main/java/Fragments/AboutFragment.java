package Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.graviton.Cuzdan.R;

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
        return v;
    }
}
