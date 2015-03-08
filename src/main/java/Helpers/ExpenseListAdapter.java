package Helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.TwoLineListItem;
import com.graviton.Cuzdan.R;

import java.util.ArrayList;


/**
 * Created by Umut Seven on 20.11.2014, for Graviton.
 */
public class ExpenseListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Expense> expenses;

    public ExpenseListAdapter(Context context, ArrayList<Expense> expenses) {
        this.context = context;
        this.expenses = expenses;
    }

    @Override
    public int getCount() {
        return expenses.size();
    }

    @Override
    public Object getItem(int position) {
        return expenses.get(position);
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
                    R.layout.main_list_item, null);
        } else {
            twoLineListItem = (TwoLineListItem) convertView;
        }

        TextView text1 = twoLineListItem.getText1();
        TextView text2 = twoLineListItem.getText2();

        text1.setText(expenses.get(position).GetCategory());
        text2.setText(expenses.get(position).GetAmount().toString() + " "+ context.getString(R.string.currency));

        return twoLineListItem;
    }
}

