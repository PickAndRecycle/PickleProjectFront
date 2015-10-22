package com.pickle.pickleproject;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by danieldeem on 10/22/2015.
 */
public class MontserratButton extends Button {

    public MontserratButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-Regular.otf"));


    }
}
