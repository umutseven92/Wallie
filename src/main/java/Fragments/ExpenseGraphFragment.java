package Fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.graviton.Cuzdan.R;

/**
 * Created by Umut Seven on 27.1.2015, for Graviton.
 */
public class ExpenseGraphFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.expense_graph_fragment, container, false);

        return v;
    }

}
