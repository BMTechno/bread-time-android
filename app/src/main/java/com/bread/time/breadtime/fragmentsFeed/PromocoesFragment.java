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
public class PromocoesFragment extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;

    public PromocoesFragment() {
        // Required empty public constructor
    }

    public static PromocoesFragment newInstance() {
        PromocoesFragment fragment = new PromocoesFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_promocoes, container, false);

        return view;
    }

}
