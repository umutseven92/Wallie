package Fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.graviton.Cuzdan.R;

import java.util.jar.Attributes;

/**
 * Created by Umut on 14.1.2015.
 */
public class IncomeSearchFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    View v;
    Spinner spnSearchCategory,spnSearchSubCategory,spnSearchDate;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        v = inflater.inflate(R.layout.incomesearchfragment, null);

        spnSearchCategory = (Spinner)v.findViewById(R.id.spnSearchCategory);
        spnSearchSubCategory = (Spinner)v.findViewById(R.id.spnSearchSubCategory);
        spnSearchDate = (Spinner)v.findViewById(R.id.spnSearchDate);
        spnSearchDate.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(v.getContext(), R.array.incomeCategories, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSearchCategory.setAdapter(categoryAdapter);
        spnSearchCategory.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> dateAdapter = ArrayAdapter.createFromResource(v.getContext(), R.array.balanceDateArray, android.R.layout.simple_spinner_item);
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSearchDate.setAdapter(dateAdapter);
        spnSearchDate.setOnItemSelectedListener(this);

        int id = getResources().getIdentifier("subCategories","array",getActivity().getPackageName());
/*
        ArrayAdapter<CharSequence> subCategoryAdapter = ArrayAdapter.createFromResource(v.getContext(), id, android.R.layout.simple_spinner_item);
        subCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSearchSubCategory.setAdapter(subCategoryAdapter);
*/
        return v;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        int pID = parent.getId();
        int cID = spnSearchCategory.getId();

        if(pID == cID)
        {
            int a = 5;
        }
        if(parent.getId() == spnSearchSubCategory.getId())
        {
            int a = 5;
        }
        if(parent.getId() == spnSearchDate.getId())
        {
            int a = 5;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
