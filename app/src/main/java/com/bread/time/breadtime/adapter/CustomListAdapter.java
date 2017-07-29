package com.bread.time.breadtime.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.bread.time.breadtime.R;
import com.bread.time.breadtime.app.AppController;
import com.bread.time.breadtime.model.Post;

import java.util.List;

public class CustomListAdapter extends BaseAdapter{

    private Activity activity;
    private LayoutInflater inflater;
    private List<Post> postItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomListAdapter(Activity activity, List<Post> postItems) {
        this.activity = activity;
        this.postItems = postItems;
    }

    @Override
    public int getCount() {
        return postItems.size();
    }

    @Override
    public Object getItem(int location) {
        return postItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.post_item, parent, false);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnailPost);
        TextView title = (TextView) convertView.findViewById(R.id.titlePost);
        TextView date = (TextView) convertView.findViewById(R.id.datePost);
        TextView views = (TextView) convertView.findViewById(R.id.viewsPost);
        TextView category = (TextView) convertView.findViewById(R.id.categoryPost);
        TextView author = (TextView) convertView.findViewById(R.id.authorPost);
        TextView content = (TextView) convertView.findViewById(R.id.contentPost);

        // getting post data for the row
        Post p = postItems.get(position);
        // thumbnail image
        thumbNail.setImageUrl(p.getThumbnail(), imageLoader);
        // title
        title.setText(p.getTitle());
        // date
        date.setText(p.getDate());
        // views
        views.setText(String.valueOf(p.getViews()));
        // category
        category.setText(p.getCategory());
        // author
        author.setText(p.getAuthor());
        // content
        content.setText(p.getContent());

        return convertView;
    }
}
