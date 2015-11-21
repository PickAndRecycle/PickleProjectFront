package com.pickle.pickleproject;

import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.LruCache;
import android.view.View;
import android.widget.ImageButton;;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.pickle.pickleprojectmodel.*;
import com.pickle.pickleprojectmodel.Article;

public class ViewArticle extends AppCompatActivity {
    private RequestQueue mRequestQueue, mQueue;
    private ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_article);

        mRequestQueue = Volley.newRequestQueue(this);
        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);
            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }
            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }
        });

        final Article article = (Article) getIntent().getSerializableExtra("object");
        TextView title = (TextView) findViewById(R.id.articleTitle);
        TextView content = (TextView) findViewById(R.id.articleContent);
        title.setText(article.getTitle());
        content.setText(article.getContent());

        NetworkImageView articlePhoto = (NetworkImageView) findViewById(R.id.articlePhoto);
        articlePhoto.setImageUrl(article.getPhoto_url(), mImageLoader);
;
        ImageButton backArticleButton = (ImageButton) findViewById(R.id.backArticleButton);

        backArticleButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

        
    }


}
