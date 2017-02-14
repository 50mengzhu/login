package com.gyh.login.util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gyh.login.R;
import com.gyh.login.db.Route;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RoutesAdapter extends RecyclerView.Adapter<RoutesAdapter.ViewHolder> {

    private Context mContext;

    private List<Route> mRouteList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView routeImage;
        TextView routeTitle;
        TextView routePrice;
        CircleImageView founderImage;

        public ViewHolder(View view) {
            super(view);
            routeImage = (ImageView) view.findViewById(R.id.route_image);
            routeTitle = (TextView) view.findViewById(R.id.route_title);
            routePrice = (TextView) view.findViewById(R.id.route_price);
            founderImage = (CircleImageView) view.findViewById(R.id.route_user_image);
        }
    }

    public RoutesAdapter(List<Route> routeList) {
        mRouteList = routeList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.route_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Route route = mRouteList.get(position);
        Glide.with(mContext).load(route.getImageId()).into(holder.routeImage);
        holder.routeTitle.setText(route.getTitle());
        holder.routePrice.setText("￥" + route.getPrice());
    }

    @Override
    public int getItemCount() {
        return mRouteList.size();
    }
}
