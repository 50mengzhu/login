package com.gyh.login.util;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gyh.login.R;
import com.gyh.login.db.Marker;

import java.util.List;

public class MarkersAdapter extends RecyclerView.Adapter<MarkersAdapter.ViewHolder> {

    private Context mContext;

    private List<Marker> mMarkers;

    static class ViewHolder extends RecyclerView.ViewHolder {
        FloatingActionButton mFab;
        TextView mTag;
        CardView mInfo;
        TextView mTitle;
        TextView mIntro;

        public ViewHolder(View view) {
            super(view);
//            mTag = (TextView) view.findViewById(R.id.marker_tag);
            mFab = (FloatingActionButton) view.findViewById(R.id.marker_fab);
            mInfo = (CardView) view.findViewById(R.id.marker_info);
            mTitle = (TextView) view.findViewById(R.id.marker_title);
            mIntro = (TextView) view.findViewById(R.id.marker_intro);
        }
    }

    public MarkersAdapter(List<Marker> markerList) {
        mMarkers = markerList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }

        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.marker_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Marker marker = mMarkers.get(position);
        holder.mTitle.setText(marker.getTitle());
//        holder.mMethod.setText(marker.getMethod());
//        holder.mTag.setText(marker.getTag());
//        holder.mTime.setText(marker.getTime());
        holder.mIntro.setText(marker.getSnippet());

        if (marker.getMethod().equals("自驾")) {
            holder.mFab.setImageResource(R.drawable.ic_marker_car);
        } else if (marker.getMethod().equals("公共交通")) {
            holder.mFab.setImageResource(R.drawable.ic_marker_bus);
        } else if (marker.getMethod().equals("骑行")) {
            holder.mFab.setImageResource(R.drawable.ic_marker_bike);
        } else if (marker.getMethod().equals("步行")) {
            holder.mFab.setImageResource(R.drawable.ic_marker_walk);
        }

        holder.mInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mMarkers.size();
    }
}
