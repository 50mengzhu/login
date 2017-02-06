package com.gyh.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gyh.login.db.Ad;
import com.gyh.login.util.AdLab;

import java.util.List;

public class Index extends AppCompatActivity {

    private RecyclerView mAdRecyclerView;
    private AdAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        mAdRecyclerView = (RecyclerView) findViewById(R.id.ad_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mAdRecyclerView.setLayoutManager(linearLayoutManager);

        updateAds();

    }

    private class AdHolder extends RecyclerView.ViewHolder {

        public ImageView mAdImageView;

        public AdHolder(View itemView) {
            super(itemView);

            mAdImageView = (ImageView) itemView;
        }
    }

    private class AdAdapter extends RecyclerView.Adapter<AdHolder> {

        private List<Ad> mAds;

        public AdAdapter(List<Ad> ads) {
            mAds = ads;
        }

        @Override
        public AdHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(Index.this);
            View view = layoutInflater.inflate(R.layout.ad_item, parent, false);
            return new AdHolder(view);
        }

        @Override
        public void onBindViewHolder(AdHolder holder, int position) {
            Ad ad = mAds.get(position);
            holder.mAdImageView.setImageResource(ad.getImageId());
        }

        @Override
        public int getItemCount() {
            return mAds.size();
        }
    }

    private void updateAds() {
        AdLab adLab = AdLab.get(Index.this);
        List<Ad> ads = adLab.getAds();

        mAdapter = new AdAdapter(ads);
        mAdRecyclerView.setAdapter(mAdapter);
    }
}
