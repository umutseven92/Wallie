package com.graviton.Cuzdan;

import Helpers.Saving;
import Helpers.SavingsHelper;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.gson.Gson;

/**
 * Created by Umut Seven on 14.2.2015, for Graviton.
 */
public class SavingActivity extends Activity{

    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.savings_detail);

        TextView txtName = (TextView)findViewById(R.id.txtSavingName);
        TextView txtAmount = (TextView)findViewById(R.id.txtSavingAmount);
        TextView txtPeriod = (TextView)findViewById(R.id.txtSavingPeriod);
        TextView txtDesc = (TextView)findViewById(R.id.txtSavingDesc);
        TextView txtRep  = (TextView)findViewById(R.id.txtSavingRep);
        ProgressBar pb = (ProgressBar)findViewById(R.id.pbSaving);
        pb.setProgress(75);

        Intent in = getIntent();
        Bundle b = in.getExtras();
        final Saving saving = new Gson().fromJson(b.getString("saving"),Saving.class);

        txtName.setText(saving.GetName());
        txtAmount.setText(saving.GetAmount().toString() +" TL (Günlük " + saving.GetDailyGoal().toString() + " TL)");
        txtPeriod.setText(SavingsHelper.GetPeriodString(saving.GetPeriod()) + " (" + saving.GetTotalDays(saving.GetPeriod()) + " Gün)");
        txtDesc.setText(saving.GetDescription());

        if(saving.GetRepeating())
        {
            txtRep.setText("Evet");
        }
        else
        {
            txtRep.setText("Hayır");
        }
    }

}
