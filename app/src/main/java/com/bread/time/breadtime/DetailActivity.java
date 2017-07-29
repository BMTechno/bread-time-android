package com.bread.time.breadtime;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG_TITLE = "titulo";
    private static final String TAG_DATE = "data";
    private static final String TAG_CATEGORY = "categoria";
    private static final String TAG_AUTHOR = "autor";
    private static final String TAG_CONTENT = "conteudo";
    private static final String TAG_VIEWS = "contview";
    private static final String TAG_THUMBNAIL = "imagem";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // getting intent data
        Intent in = getIntent();

        // Get JSON values from previous intent
        String title = in.getStringExtra(TAG_TITLE);
        String date = in.getStringExtra(TAG_DATE);
        String category = in.getStringExtra(TAG_CATEGORY);
        String author = in.getStringExtra(TAG_AUTHOR);
        String content = in.getStringExtra(TAG_CONTENT);
        String views = in.getStringExtra(TAG_VIEWS);
        Bundle extras = getIntent().getExtras();
        byte[] b = extras.getByteArray(TAG_THUMBNAIL);
        if(b!=null) {
            Bitmap thumbnail = BitmapFactory.decodeByteArray(b, 0, b.length);

            // Displaying all values on the screen
            TextView titlePost = (TextView) findViewById(R.id.titlePostDetail);
            TextView datePost = (TextView) findViewById(R.id.datePostDetail);
            TextView viewsPost = (TextView) findViewById(R.id.viewsPostDetail);
            TextView categoryPost = (TextView) findViewById(R.id.categoryPostDetail);
            TextView authorPost = (TextView) findViewById(R.id.authorPostDetail);
            TextView contentPost = (TextView) findViewById(R.id.contentPostDetail);
            ImageView imgView = (ImageView) findViewById(R.id.thumbnailPostDetail);

            imgView.setImageBitmap(thumbnail);
            titlePost.setText(title);
            datePost.setText(date);
            viewsPost.setText(views);
            categoryPost.setText(category);
            authorPost.setText(author);
            contentPost.setText(content);
        }else{
            Toast.makeText(getApplicationContext(), "Imagem não encontrada.",
                    Toast.LENGTH_LONG).show();
        }

        // Top Home Button to go back to previous activity
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
