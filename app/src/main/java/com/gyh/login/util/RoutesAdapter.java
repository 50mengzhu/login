package com.gyh.login.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gyh.login.FounderIndex;
import com.gyh.login.R;
import com.gyh.login.RouteIndex;
import com.gyh.login.db.Route;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RoutesAdapter extends RecyclerView.Adapter<RoutesAdapter.ViewHolder> {

    private Context mContext;

    private List<Route> mRouteList;

    private int flag = 0;
    private int mItemWidth;

    public void setItemWidth(int width) {
        mItemWidth = width;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView routeImage;
        TextView routeTitle;
        TextView routePrice;
        CircleImageView founderImage;
        RelativeLayout mRoute;

        public ViewHolder(View view) {
            super(view);
            routeImage = (ImageView) view.findViewById(R.id.route_image);
            routeTitle = (TextView) view.findViewById(R.id.route_title);
            routePrice = (TextView) view.findViewById(R.id.route_price);
            founderImage = (CircleImageView) view.findViewById(R.id.route_user_image);
            mRoute = (RelativeLayout) view.findViewById(R.id.route);
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

        if (flag == 0) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.route_item, parent, false);

            return new ViewHolder(view);
        } else if (flag == 1 || flag == 2) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.all_route_item, parent, false);
            // 若是两列的需要计算来获取居中
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.width = mItemWidth;
            view.setLayoutParams(layoutParams);
            return new ViewHolder(view);
        }

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.route_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Route route = mRouteList.get(position);

        Glide.with(mContext).load(route.getImageId()).into(holder.routeImage);
        holder.routeTitle.setText(route.getTitle());
        holder.routePrice.setText("￥" + route.getPrice());
        holder.mRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RouteIndex.class);
                intent.putExtra("route", route.getId());
                mContext.startActivity(intent);
                ((Activity)mContext).overridePendingTransition(R.anim.in_right, R.anim.out_left);
            }
        });
        holder.founderImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FounderIndex.class);
                intent.putExtra("user", route.getFounder());
                intent.putExtra("flag", 1);
                mContext.startActivity(intent);
                ((Activity)mContext).overridePendingTransition(R.anim.in_right, R.anim.out_left);
            }
        });

        if (flag == 2) {
            holder.founderImage.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mRouteList.size();
    }
}
