package com.pickle.pickleproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.pickle.pickleprojectmodel.Article;

import org.w3c.dom.Text;

/**
 * Created by admin on 11/13/2015.
 */
public class NewsfeedAdapter extends ArrayAdapter<Article> {
    private Context context;
    private int resource;
    private Article[] objects;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    public NewsfeedAdapter(Context context, int resource, Article[] objects) {

        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
        this.objects = objects;

        mRequestQueue = Volley.newRequestQueue(context);
        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);
            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }
            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        });
    }

    public View getView(final int position,
                        View convertView,
                        ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View row = inflater.inflate(resource, parent, false);
        TextView title = (TextView) row.findViewById(R.id.titleNewsfeed);
        TextView desc = (TextView) row.findViewById(R.id.descriptionNewsfeed);
        title.setText((CharSequence) objects[position].title);
        CircleImageView thumbnail = (CircleImageView) row.findViewById(R.id.thumbnailnews);
        thumbnail.setImageUrl(objects[position].getPhoto_url(), mImageLoader);
        if (objects[position].getContent().toString().length() < 23){
            desc.setText((CharSequence) objects[position].getContent());
        }
        else{
            desc.setText((CharSequence) objects[position].getContent().substring(0,23));
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
