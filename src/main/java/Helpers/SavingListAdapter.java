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
 * Created by Umut Seven on 14.2.2015, for Graviton.
 */
public class SavingListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Saving> savings;

    public SavingListAdapter(Context context, ArrayList<Saving> savings) {
        this.context = context;
        this.savings = savings;
    }

    @Override
    public int getCount() {
        return savings.size();
    }

    @Override
    public Object getItem(int position) {
        return savings.get(position);
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

        text1.setText(savings.get(position).GetName());
        text2.setText(savings.get(position).GetAmount().toString() + " TL");

        return twoLineListItem;

    }
}
