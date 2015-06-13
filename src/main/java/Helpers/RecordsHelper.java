package Helpers;


import android.content.res.Resources;
import android.util.SparseArray;
import com.graviton.Cuzdan.R;

/**
 * Created by Umut Seven on 13.6.2015.
 */
public class RecordsHelper {

    private SparseArray<String> records;

    public RecordsHelper(Resources res) {
        records = new SparseArray<String>();
        PopulateRecords(res);
    }

    public int GetIDFromName(String name) {
        for (int i = 0; i < records.size(); i++) {
            if (records.valueAt(i).equals(name)) {
                return records.keyAt(i);
            }
        }
        return -1;
    }

    public String GetNameFromID(int id) {
        return records.get(id);
    }

    private void PopulateRecords(Resources res) {
        //Gelirler
        records.put(101, res.getString(R.string.salary));
        records.put(102, res.getString(R.string.rent));
        records.put(103, res.getString(R.string.allowance));
        records.put(104, res.getString(R.string.loan));
        records.put(105, res.getString(R.string.interest));
        records.put(106, res.getString(R.string.games_of_chance));
        records.put(107, res.getString(R.string.receivables));
        records.put(108, res.getString(R.string.custom_category));

        records.put(10101, res.getString(R.string.salary1));
        records.put(10102, res.getString(R.string.salary2));

        records.put(10201, res.getString(R.string.rent1));

        records.put(10301, res.getString(R.string.allowance1));

        records.put(10401, res.getString(R.string.loan1));

        records.put(10501, res.getString(R.string.interest1));
        records.put(10502, res.getString(R.string.interest2));

        records.put(10601, res.getString(R.string.games1));
        records.put(10602, res.getString(R.string.games2));
        records.put(10603, res.getString(R.string.games3));
        records.put(10604, res.getString(R.string.games4));

        records.put(10701, res.getString(R.string.receivables1));

        //Giderler
        records.put(21, res.getString(R.string.tag_personal));
        records.put(22, res.getString(R.string.tag_home));

        records.put(2101, res.getString(R.string.food));
        records.put(2102, res.getString(R.string.cig));
        records.put(2103, res.getString(R.string.shop));
        records.put(2104, res.getString(R.string.health));
        records.put(2105, res.getString(R.string.ent));
        records.put(2106, res.getString(R.string.trans));
        records.put(2107, res.getString(R.string.exchange));
        records.put(2108, res.getString(R.string.install));
        records.put(2109, res.getString(R.string.debt));
        records.put(2110, res.getString(R.string.rent_expense));
        records.put(2111, res.getString(R.string.bills));
        records.put(2112, res.getString(R.string.pet));
        records.put(2113, res.getString(R.string.groceries));

        records.put(210101, res.getString(R.string.food1));
        records.put(210102, res.getString(R.string.food2));

        records.put(210201, res.getString(R.string.cig1));
        records.put(210202, res.getString(R.string.cig2));

        records.put(210301, res.getString(R.string.shop1));
        records.put(210302, res.getString(R.string.shop2));
        records.put(210303, res.getString(R.string.shop3));

        records.put(210401, res.getString(R.string.health1));
        records.put(210402, res.getString(R.string.health2));

        records.put(210501, res.getString(R.string.ent1));
        records.put(210502, res.getString(R.string.ent2));
        records.put(210503, res.getString(R.string.ent3));
        records.put(210504, res.getString(R.string.ent4));

        records.put(210601, res.getString(R.string.trans1));
        records.put(210602, res.getString(R.string.trans2));
        records.put(210603, res.getString(R.string.trans3));
        records.put(210604, res.getString(R.string.trans4));

        records.put(210701, res.getString(R.string.exchange1));

        records.put(210801, res.getString(R.string.install1));

        records.put(210901, res.getString(R.string.debt1));
        records.put(210902, res.getString(R.string.debt2));
        records.put(210903, res.getString(R.string.debt3));

        records.put(211001, res.getString(R.string.rent_expense1));

        records.put(211101, res.getString(R.string.bills1));
        records.put(211102, res.getString(R.string.bills2));
        records.put(211103, res.getString(R.string.bills3));
        records.put(211104, res.getString(R.string.bills4));
        records.put(211105, res.getString(R.string.bills5));
        records.put(211106, res.getString(R.string.bills6));
        records.put(211107, res.getString(R.string.bills7));

        records.put(211201, res.getString(R.string.pet1));
        records.put(211202, res.getString(R.string.pet2));
        records.put(211203, res.getString(R.string.pet3));
        records.put(211204, res.getString(R.string.pet4));

        records.put(211301, res.getString(R.string.groceries1));
        records.put(211302, res.getString(R.string.groceries2));
        records.put(211303, res.getString(R.string.groceries3));
        records.put(211304, res.getString(R.string.groceries4));
        records.put(211305, res.getString(R.string.groceries5));
        records.put(211306, res.getString(R.string.groceries6));
        records.put(211307, res.getString(R.string.groceries7));
    }


}
