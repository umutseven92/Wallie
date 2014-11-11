package com.example.Cuzdan;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

    RelativeLayout lytAccounting;
    RelativeLayout lytSavings;
    ImageView imgAccArrow;
    ImageView imgSavArrow;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Accounting section
        lytAccounting = (RelativeLayout) findViewById(R.id.lytAccounting);
        imgAccArrow = (ImageView) findViewById(R.id.imgAccArrow);

        // Savings section
        lytSavings = (RelativeLayout) findViewById(R.id.lytSavings);
        lytSavings.setVisibility(View.GONE);
        imgSavArrow = (ImageView) findViewById(R.id.imgSavArrow);
    }

    public void ExpandAccounting(View v)
    {
        if(lytAccounting.isShown())
        {
            imgAccArrow.setImageResource(R.drawable.down);
            lytAccounting.setVisibility(View.GONE);
            FX.SlideUp(this,lytAccounting);
        }
        else
        {
            imgAccArrow.setImageResource(R.drawable.up);
            lytAccounting.setVisibility(View.VISIBLE);
            FX.SlideDown(this,lytAccounting);
        }

    }

    public void ExpandSavings(View v)
    {
        if(lytSavings.isShown())
        {
            imgSavArrow.setImageResource(R.drawable.down);
            lytSavings.setVisibility(View.GONE);
            FX.SlideUp(this,lytSavings);
        }
        else
        {
            imgSavArrow.setImageResource(R.drawable.up);
            lytSavings.setVisibility(View.VISIBLE);
            FX.SlideDown(this,lytSavings);
        }
    }



}
