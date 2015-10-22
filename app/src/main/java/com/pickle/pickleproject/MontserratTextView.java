package com.pickle.pickleproject;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by danieldeem on 10/21/2015.
 */
public class MontserratTextView extends TextView {

    public MontserratTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/Montserrat-Regular.otf"));


    }

}
