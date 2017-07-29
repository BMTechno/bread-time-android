package com.bread.time.breadtime.fragmentsFeed;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bread.time.breadtime.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FornadaFragment extends Fragment {
    // Log tag
    private static final String TAG = MainFragment.class.getSimpleName();

    public static FornadaFragment newInstance() {
        return new FornadaFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fornada, container, false);

    }

}
