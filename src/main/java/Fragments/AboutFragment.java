package Fragments;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.graviton.Cuzdan.R;

/**
 * Created by Umut Seven on 14.4.2015, for Graviton.
 */
public class AboutFragment extends Fragment {

    int rsCount = 0;
    int rsLength = 1000;
    ImageView rob;

    public static final AboutFragment newInstance() {
        AboutFragment f = new AboutFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.about_fragment, container, false);

        TextView txtVersion = (TextView) v.findViewById(R.id.txtAboutVersion);
        ImageView imgLogo = (ImageView) v.findViewById(R.id.imgLogo);
        rob = (ImageView) v.findViewById(R.id.imgRb);
        rob.setVisibility(View.INVISIBLE);

        txtVersion.setText(R.string.version);

        imgLogo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                if (rsCount > 2) {
                    rsCount = 0;
                }

                MediaPlayer mp = SetSchneider();
                rsCount++;

                rob.setVisibility(View.VISIBLE);
                mp.start();
                rob.postDelayed(new Runnable() {
                    public void run() {
                        rob.setVisibility(View.INVISIBLE);
                    }
                }, rsLength);
                return true;
            }
        });

        return v;
    }

    private MediaPlayer SetSchneider() {
        switch (rsCount) {
            case 0:
                rsLength = 2600;
                rob.setImageResource(R.drawable.rb1);
                return MediaPlayer.create(getActivity().getApplicationContext(), R.raw.rs1);
            case 1:
                rsLength = 2800;
                rob.setImageResource(R.drawable.rb2);
                return MediaPlayer.create(getActivity().getApplicationContext(), R.raw.rs2);
            case 2:
                rsLength = 5300;
                rob.setImageResource(R.drawable.rb3);
                return MediaPlayer.create(getActivity().getApplicationContext(), R.raw.rs3);
            default:
                rsLength = 2600;
                rob.setImageResource(R.drawable.rb1);
                return MediaPlayer.create(getActivity().getApplicationContext(), R.raw.rs1);
        }
    }

}
