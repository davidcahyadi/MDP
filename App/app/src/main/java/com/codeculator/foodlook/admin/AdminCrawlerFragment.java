package com.codeculator.foodlook.admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codeculator.foodlook.R;
import com.codeculator.foodlook.adapter.admin.AdminCrawlerListAdapter;
import com.codeculator.foodlook.databinding.FragmentAdminCrawlerBinding;
import com.codeculator.foodlook.databinding.FragmentRecipeDetailBinding;
import com.codeculator.foodlook.local.AddCrawler;
import com.codeculator.foodlook.local.FetchCallback;
import com.codeculator.foodlook.local.LoadCrawler;
import com.codeculator.foodlook.local.StoreCallback;
import com.codeculator.foodlook.model.Crawler;
import com.codeculator.foodlook.model.User;

import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminCrawlerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminCrawlerFragment extends Fragment {
    FragmentAdminCrawlerBinding binding;
    ArrayList<Crawler> crawlers;
    AdminCrawlerListAdapter adapter;

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
        binding = FragmentAdminCrawlerBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.crawlerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        hideLoading();
        createData();

        adapter = new AdminCrawlerListAdapter(crawlers,this);
        binding.crawlerRecyclerView.setAdapter(adapter);

        binding.adminBtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AdminHomeActivity) getActivity()).logoutFromAdmin();
            }
        });
    }

    public void createData(){
        crawlers = new ArrayList<>();

        new LoadCrawler(getActivity(), new FetchCallback<ArrayList<Crawler>>() {
            @Override
            public void preProcess() {

            }

            @Override
            public void postProcess(ArrayList<Crawler> data) {
                if(data.size() == 0){
                    crawlers.add(new Crawler(1,"All Recipes"));
                    crawlers.add(new Crawler(2,"Asian Food Network"));
                    for (Crawler crawler :  crawlers) {
                        crawler.setUpdated_at("Not Crawled Yet");
                        new AddCrawler(getActivity(), new StoreCallback() {
                            @Override
                            public void preProcess() {

                            }

                            @Override
                            public void postProcess() {

                            }
                        }).execute(crawler);
                    }
                }
                else{
                    crawlers.clear();
                    crawlers.addAll(data);
                }
                adapter.notifyDataSetChanged();

            }
        }).execute();
    }



    public void showLoading(){
        binding.loadingLayer.setVisibility(View.VISIBLE);
    }

    public void hideLoading(){
        binding.loadingLayer.setVisibility(View.INVISIBLE);
    }
}