package Fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.graviton.Cuzdan.R;

/**
 * Created by Umut on 23.1.2015.
 */
public class ExpenseGraphFragment extends Fragment{

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.expensegraphfragment, container,false);
        return v;
    }

}