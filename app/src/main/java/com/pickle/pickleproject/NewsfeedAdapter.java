package com.pickle.pickleproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pickle.pickleprojectmodel.Newsfeed;

/**
 * Created by admin on 11/13/2015.
 */
public class NewsfeedAdapter extends ArrayAdapter<Newsfeed> {
    private Context context;
    private int resource;
    private Newsfeed[] objects;
    public NewsfeedAdapter(Context context, int resource, Newsfeed[] objects) {

        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }
    public View getView(final int position,
                        View convertView,
                        ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View row = inflater.inflate(resource, parent, false);
        TextView title = (TextView) row.findViewById(R.id.titleNewsfeed);
        TextView desc = (TextView) row.findViewById(R.id.descriptionNewsfeed);
        title.setText((CharSequence) objects[position].title);
        if(objects[position].content.length() <66){
        desc.setText((CharSequence) objects[position].content);}
        else{
            desc.setText((CharSequence) objects[position].content.substring(0,60)+"...");
        }
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //row.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                Intent intent = new Intent(context, ViewArticle.class);
                intent.putExtra("object", objects[position]);
                context.startActivity(intent);
            }
        });
        return row;
    }


}
