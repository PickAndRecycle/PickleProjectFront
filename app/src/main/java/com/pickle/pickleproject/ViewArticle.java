package com.pickle.pickleproject;

import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.pickle.pickleprojectmodel.*;
import com.pickle.pickleprojectmodel.Article;

public class ViewArticle extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_article);
        final Article article = (Article) getIntent().getSerializableExtra("object");
        TextView title = (TextView) findViewById(R.id.articleTitle);
        TextView content = (TextView) findViewById(R.id.articleContent);
        ImageView photo = (ImageView) findViewById(R.id.articlePhoto);
        title.setText(article.getTitle());
        content.setText(article.getContent());

        ImageButton backArticleButton = (ImageButton) findViewById(R.id.backArticleButton);

        backArticleButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

        
    }


}
