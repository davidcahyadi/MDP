package com.codeculator.foodlook.admin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codeculator.foodlook.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminCrawlerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminCrawlerFragment extends Fragment {


    public AdminCrawlerFragment() {
        // Required empty public constructor
    }


    public static AdminCrawlerFragment newInstance(String param1, String param2) {
        AdminCrawlerFragment fragment = new AdminCrawlerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_crawler, container, false);
    }
}