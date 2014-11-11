package com.example.Cuzdan;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by Umut on 12.11.2014.
 */
public class FX {

    public static void SlideDown(Context ctx, View v)
    {
        Animation a = AnimationUtils.loadAnimation(ctx,R.anim.slide_down);
        if (a!=null)
        {
            a.reset();
            if(v!= null)
            {
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }

    public static void SlideUp(Context ctx, View v)
    {
        Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_up);
        if (a!=null)
        {
            a.reset();
            if(v!= null)
            {
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }
}
