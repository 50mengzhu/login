package com.gyh.login.searchHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arlib.floatingsearchview.util.Util;
import com.bumptech.glide.Glide;
import com.gyh.login.FounderIndex;
import com.gyh.login.R;
import com.gyh.login.RouteIndex;
import com.gyh.login.db.Route;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchResultsListAdapter extends RecyclerView.Adapter<SearchResultsListAdapter.ViewHolder> {

    private Context  mContext;

    private List<Route> mRoutes = new ArrayList<>();

    private int mItemWidth;
    private int mLastAnimatedItemPosition = -1;

    public void setItemWidth(int width) {
        mItemWidth = width;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
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

    public void swapData(List<Route> mNewRoutes) {
        mRoutes = mNewRoutes;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.all_route_item, parent, false);
        // 若是两列的需要计算来获取居中
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = mItemWidth;
        view.setLayoutParams(layoutParams);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Route route = mRoutes.get(position);

        Glide.with(mContext).load(route.getImageId()).into(holder.routeImage);
        holder.routeTitle.setText(route.getTitle());
        holder.routePrice.setText("￥" + route.getPrice());

        if (mLastAnimatedItemPosition < position) {
            animateItem(holder.itemView);
            mLastAnimatedItemPosition = position;
        }

        holder.mRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RouteIndex.class);
                intent.putExtra("route", route);
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
    }

    @Override
    public int getItemCount() {
        return mRoutes.size();
    }

    private void animateItem(View view) {
        view.setTranslationY(Util.getScreenHeight((Activity) view.getContext()));
        view.animate()
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator(3.f))
                .setDuration(700)
                .start();
    }
}
