package com.gyh.login.util;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gyh.login.ArticleIndex;
import com.gyh.login.Index;
import com.gyh.login.R;
import com.gyh.login.bottomsheet.BottomSheetBuilder;
import com.gyh.login.bottomsheet.BottomSheetItemClickListener;
import com.gyh.login.db.Article;

import java.util.List;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ViewHolder> {

    private  Context mContext;

    private List<Article> mArticleList;

    private BottomSheetDialog mBottomSheetDialog;
    private boolean mShowingGridDialog;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView mCardView;
        ImageView articleImage;
        TextView articleTitle;
        TextView articleDate;
        TextView articleInfo;
        TextView share;

        public ViewHolder(View view) {
            super(view);
            mCardView = (CardView) view.findViewById(R.id.article);
            articleImage = (ImageView) view.findViewById(R.id.public_item_pic);
            articleTitle = (TextView) view.findViewById(R.id.public_item_title);
            articleDate = (TextView) view.findViewById(R.id.public_item_time);
            articleInfo = (TextView) view.findViewById(R.id.public_item_info);
            share = (TextView) view.findViewById(R.id.public_item_share);
        }
    }

    public ArticlesAdapter(List<Article> articleList) {
        mArticleList = articleList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext ==  null) {
            mContext = parent.getContext();
        }

        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.article_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Article article = mArticleList.get(position);
        Glide.with(mContext).load(article.getImageId()).into(holder.articleImage);
        holder.articleTitle.setText(article.getTitle());
        holder.articleDate.setText(article.getDate());
        holder.articleInfo.setText(article.getInfo());

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ActivityOptions options =
                                ActivityOptions.makeSceneTransitionAnimation((Index) mContext,
                                        Pair.create((View) holder.articleImage, "image_transition"));

                        Intent intent = new Intent(mContext, ArticleIndex.class);
                        intent.putExtra("article", article);
                        mContext.startActivity(intent, options.toBundle());
                    }
                }, 200);
            }
        });

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onShowDialogGridClick();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mArticleList.size();
    }

    public void onShowDialogGridClick() {
        if (mBottomSheetDialog != null) {
            mBottomSheetDialog.dismiss();
        }
        mShowingGridDialog = true;
        mBottomSheetDialog = new BottomSheetBuilder(mContext, R.style.AppTheme_BottomSheetDialog)
                .setMode(BottomSheetBuilder.MODE_GRID)
                .setMenu(mContext.getResources().getBoolean(R.bool.tablet_landscape)
                        ? R.menu.menu_bottom_grid_tablet_sheet : R.menu.menu_bottom_grid_sheet)
                .expandOnStart(true)
                .setItemClickListener(new BottomSheetItemClickListener() {
                    @Override
                    public void onBottomSheetItemClick(MenuItem item) {
                        mShowingGridDialog = false;
                    }
                })
                .createDialog();

        mBottomSheetDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mShowingGridDialog = false;
            }
        });
        mBottomSheetDialog.show();
    }
}
