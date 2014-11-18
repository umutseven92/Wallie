package Fragments;

import android.app.ListFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.Cuzdan.R;

import java.util.ArrayList;

public class IncomeFragment extends Fragment {

    public static final IncomeFragment newInstance()
    {
        IncomeFragment f = new IncomeFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.incomefragment, container, false);

        String[] myList = new String[] {"Hello","World","Foo","Bar"};

        ListView lv = (ListView)v.findViewById(R.id.lstIncomes);

        lv.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,myList));

        return v;
    }
}
