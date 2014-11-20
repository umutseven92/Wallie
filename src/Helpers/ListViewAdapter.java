package Helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.TwoLineListItem;

import java.util.ArrayList;

/**
 * Created by Umut on 20.11.2014.
 */
public class ListViewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Balance> balances;

    public ListViewAdapter(Context context, ArrayList<Balance> balances) {
        this.context = context;
        this.balances = balances;
    }

    @Override
    public int getCount() {
        return balances.size();
    }

    @Override
    public Object getItem(int position) {
        return balances.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TwoLineListItem twoLineListItem;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            twoLineListItem = (TwoLineListItem) inflater.inflate(
                    android.R.layout.simple_list_item_2, null);
        } else {
            twoLineListItem = (TwoLineListItem) convertView;
        }

        TextView text1 = twoLineListItem.getText1();
        TextView text2 = twoLineListItem.getText2();

        text1.setText(balances.get(position).GetCategory() + "/" + balances.get(position).GetSubCategory() + "/" + balances.get(position).GetDescription());
        text2.setText(balances.get(position).GetAmount().toString() + " TL");

        return twoLineListItem;
    }
}

