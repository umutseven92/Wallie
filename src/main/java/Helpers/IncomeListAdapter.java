package Helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.TwoLineListItem;
import com.graviton.Cuzdan.Global;
import com.graviton.Cuzdan.R;

import java.util.ArrayList;

/**
 * Created by Umut Seven on 26.11.2014, for Graviton.
 */
public class IncomeListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Income> incomes;
    private String currency;

    public IncomeListAdapter(Context context, ArrayList<Income> incomes, String currency) {
        this.context = context;
        this.incomes = incomes;
        this.currency = currency;
    }

    @Override
    public int getCount() {
        return incomes.size();
    }

    @Override
    public Object getItem(int position) {
        return incomes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TwoLineListItem twoLineListItem;

        RecordsHelper recordsHelper = ((Global) context.getApplicationContext()).recordsHelper;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            twoLineListItem = (TwoLineListItem) inflater.inflate(
                    R.layout.main_list_item, null);
        } else {
            twoLineListItem = (TwoLineListItem) convertView;
        }

        TextView text1 = twoLineListItem.getText1();
        TextView text2 = twoLineListItem.getText2();

        String cat = incomes.get(position).GetCategory();
        String xz = context.getString(R.string.custom_category);
        Integer i = recordsHelper.GetIDFromName(xz);
        String s = Integer.toString(i);

        if (cat.equals(Integer.toString(recordsHelper.GetIDFromName(context.getString(R.string.custom_category))))) {
            text1.setText(incomes.get(position).GetSubCategory());
        } else {
            text1.setText(recordsHelper.GetNameFromID(Integer.parseInt(incomes.get(position).GetCategory())));
        }

        text2.setText(incomes.get(position).GetAmount().toString() + " " + currency);

        return twoLineListItem;
    }
}
