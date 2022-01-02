package com.codeculator.foodlook.adapter.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codeculator.foodlook.R;
import com.codeculator.foodlook.admin.AdminCrawlerFragment;
import com.codeculator.foodlook.databinding.FragmentAdminCrawlerBinding;
import com.codeculator.foodlook.local.StoreCallback;
import com.codeculator.foodlook.local.UpdateCrawler;
import com.codeculator.foodlook.model.Crawler;
import com.codeculator.foodlook.services.RetrofitApi;
import com.codeculator.foodlook.services.response.BasicResponse;
import com.codeculator.foodlook.services.response.CrawlResponse;

import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminCrawlerListAdapter extends RecyclerView.Adapter<AdminCrawlerListAdapter.CrawlerHolder> {
    ArrayList<Crawler> crawlers;
    AdminCrawlerFragment fragment;

    public AdminCrawlerListAdapter(ArrayList<Crawler> crawlers, AdminCrawlerFragment fragment){
        this.crawlers = crawlers;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public AdminCrawlerListAdapter.CrawlerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_component_crawler_list,parent,false);
        return new CrawlerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminCrawlerListAdapter.CrawlerHolder holder, int position) {
        holder.bind(crawlers.get(position));
    }

    @Override
    public int getItemCount() {
        return crawlers.size();
    }

    public class CrawlerHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvUpdate;
        Button btnCrawl;

        public CrawlerHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.name);
            tvUpdate = itemView.findViewById(R.id.updateAt);
            btnCrawl = itemView.findViewById(R.id.btnCrawl);
        }

        public void bind(Crawler crawler){
            tvName.setText(crawler.getName());
            tvUpdate.setText(crawler.getUpdated_at());
            btnCrawl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragment.showLoading();
                    Call<CrawlResponse> call = RetrofitApi.getInstance().getAdminService().crawl(crawler.getId());
                    call.enqueue(new Callback<CrawlResponse>() {
                        @Override
                        public void onResponse(Call<CrawlResponse> call, Response<CrawlResponse> response) {
                            System.out.println(response.raw());
                            fragment.hideLoading();
                            crawler.setUpdated_at(new Date().toString());
                            new UpdateCrawler(fragment.getActivity(), new StoreCallback() {
                                @Override
                                public void preProcess() {

                                }

                                @Override
                                public void postProcess() {

                                }
                            }).execute(crawler);
                            Toast.makeText(fragment.getActivity(), "Success Crawling from : "+ crawler.getName() + " with total : " + response.body().getNewRecipes() + " recipes.", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<CrawlResponse> call, Throwable t) {

                        }
                    });
                    fragment.createData();
                }
            });
        }
    }
}
