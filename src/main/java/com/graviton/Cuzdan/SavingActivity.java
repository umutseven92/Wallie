package com.graviton.Cuzdan;

import Helpers.Banker;
import Helpers.Saving;
import Helpers.SavingsHelper;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.gson.Gson;
import org.json.JSONException;

import java.io.IOException;

/**
 * Created by Umut Seven on 14.2.2015, for Graviton.
 */
public class SavingActivity extends Activity{

    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.savings_detail);

        Intent in = getIntent();
        Bundle b = in.getExtras();
        final Saving saving = new Gson().fromJson(b.getString("saving"),Saving.class);

        final Banker banker = ((Global) getApplication()).GetUser().GetBanker();

        TextView txtName = (TextView)findViewById(R.id.txtSavingName);
        TextView txtAmount = (TextView)findViewById(R.id.txtSavingAmount);
        TextView txtPeriod = (TextView)findViewById(R.id.txtSavingPeriod);
        TextView txtDesc = (TextView)findViewById(R.id.txtSavingDesc);
        TextView txtRep  = (TextView)findViewById(R.id.txtSavingRep);
        ProgressBar pb = (ProgressBar)findViewById(R.id.pbSaving);
        Button btnDelete = (Button)findViewById(R.id.btnDeleteSaving);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    banker.DeleteSaving(saving.GetID(),getApplication());
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finish();
            }
        });

        pb.setProgress(75);


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
