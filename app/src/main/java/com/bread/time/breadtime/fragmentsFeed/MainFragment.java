package com.bread.time.breadtime.fragmentsFeed;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.bread.time.breadtime.DetailActivity;
import com.bread.time.breadtime.MainActivity;
import com.bread.time.breadtime.R;
import com.bread.time.breadtime.adapter.CustomListAdapter;
import com.bread.time.breadtime.app.AppController;
import com.bread.time.breadtime.model.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {

    private ProgressDialog pDialog;
    private List<Post> postList = new ArrayList<>();
    private CustomListAdapter adapter;
    // Log tag
    private static final String TAG = MainFragment.class.getSimpleName();
    //Swipe Refresh
    private SwipeRefreshLayout swipeRefreshLayout;
    // Json url
    private static final String url = "https://breadtime.herokuapp.com/exportarfeed/";
    // JSON Node names
    private static final String TAG_TITLE = "titulo";
    private static final String TAG_DATE = "data";
    private static final String TAG_CATEGORY = "categoria";
    private static final String TAG_AUTHOR = "autor";
    private static final String TAG_NAME = "first_name";
    private static final String TAG_LAST_NAME = "last_name";
    private static final String TAG_CONTENT = "conteudo";
    private static final String TAG_VIEWS = "contview";
    private static final String TAG_THUMBNAIL = "imagem";
    private static final String TAG_WEBSITE = "https://breadtime.herokuapp.com/";

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        //Call Json
        if(((MainActivity)getActivity()).isOnline()) {
            init(view);
        }else{
            Toast.makeText(getActivity(),"Não foi possível recuperar o feed." +
                    " Sem conexão com a internet.", Toast.LENGTH_LONG).show();
        }

        //Refresh Feed
        refreshFeed(view);

        return view;
    }

    public void init(View view){

        // Setting ListView Adapter
        ListView listView;
        listView = (ListView) view.findViewById(R.id.listViewPost);
        adapter = new CustomListAdapter(getActivity(), postList);
        listView.setAdapter(adapter);

        // Showing progress dialog before http request
        pDialog = new ProgressDialog(getActivity());
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.setCancelable(false);
        pDialog.show();
        pDialog.setContentView(R.layout.progress_dialog);

        // Clear json items before Swipe Refresh
        if(postList != null) {
            postList.clear();
        }
        // Creating volley request obj
        JsonArrayRequest postRequest = new JsonArrayRequest(url,
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Log.d(TAG, response.toString());
                    hidePDialog();

                    // Parsing json
                    for (int i = 0; i < response.length(); i++) {
                        try {

                            JSONObject obj = response.getJSONObject(i);
                            // JSONArray params = obj.getJSONArray(TAG_AUTHOR);
                            // JSONObject objAuthor = params.getJSONObject(i);
                            Post post = new Post();
                            post.setTitle(obj.getString(TAG_TITLE));
                            post.setDate(obj.getString(TAG_DATE));
                            post.setCategory(obj.getString(TAG_CATEGORY));
                            post.setAuthor(obj.getString(TAG_AUTHOR));
                            post.setContent(obj.getString(TAG_CONTENT));
                            post.setViews(obj.getInt(TAG_VIEWS));
                            post.setThumbnail(TAG_WEBSITE + obj.getString(TAG_THUMBNAIL));
                            // adding post to post array
                            postList.add(post);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    // notifying list adapter about data changes
                    // so that it renders the list view with updated data
                    adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NoConnectionError){
                            Toast.makeText(getContext(), "Não foi possível recuperar os posts." +
                                    " Sem internet.", Toast.LENGTH_LONG).show();
                        }}
            });

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(postRequest);

        //Starting Detail Fragment
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // getting values from selected ListItem
                String title = ((TextView) view.findViewById(R.id.titlePost)).getText().toString();
                String date = ((TextView) view.findViewById(R.id.datePost)).getText().toString();
                String category = ((TextView) view.findViewById(R.id.categoryPost)).getText().toString();
                String author = ((TextView) view.findViewById(R.id.authorPost)).getText().toString();
                String content = ((TextView) view.findViewById(R.id.contentPost)).getText().toString();
                String views = ((TextView) view.findViewById(R.id.viewsPost)).getText().toString();
                BitmapDrawable bd = (BitmapDrawable) ((NetworkImageView) view.findViewById(R.id.thumbnailPost))
                        .getDrawable();
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                bd.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, bs);
                byte[] thumbnail = bs.toByteArray();

                // Starting new intent
                Intent in = new Intent(getContext(), DetailActivity.class);
                in.putExtra(TAG_TITLE, title);
                in.putExtra(TAG_DATE, date);
                in.putExtra(TAG_CATEGORY, category);
                in.putExtra(TAG_AUTHOR, author);
                in.putExtra(TAG_CONTENT, content);
                in.putExtra(TAG_VIEWS, views);
                in.putExtra(TAG_THUMBNAIL, thumbnail);
                startActivity(in);
            }
        });

    }

    public void refreshFeed(final View view){
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        init(view);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

}

