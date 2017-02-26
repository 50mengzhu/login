package com.gyh.login;

import android.animation.Animator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gyh.login.bottomsheet.BottomSheetBuilder;
import com.gyh.login.bottomsheet.BottomSheetItemClickListener;
import com.gyh.login.db.Article;

import de.hdodenhof.circleimageview.CircleImageView;

public class ArticleIndex extends AppCompatActivity implements BottomSheetItemClickListener {

    public static final String STATE_GRID = "state_grid";
    private BottomSheetDialog mBottomSheetDialog;
    private BottomSheetBehavior mBehavior;
    private boolean mShowingGridDialog;

    // 控制ToolBar的变量
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;

    private static final int ALPHA_ANIMATIONS_DURATION = 200;

    private boolean mIsTheTitleContainerVisible = true;

    private Toolbar mToolbar;
    private AppBarLayout mAppBarLayout;
    private ImageView mArticlePic;
    private TextView mArticleTitle;
    private TextView mArticleDate;
    private TextView mArticleAuthor;
    private CircleImageView mArticleAuthorImage;
    private TextView mArticleContent;
    private FloatingActionButton mFloatingActionButton;

    @Override
    public void onBackPressed() {
        mFloatingActionButton.animate().scaleX(0).scaleY(0).setDuration(200).setListener(new CustomTransitionListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                supportFinishAfterTransition();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_index);

        mToolbar = (Toolbar) findViewById(R.id.article_main_toolbar);
        mToolbar.setTitle("");

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mAppBarLayout = (AppBarLayout) findViewById(R.id.article_appbar);
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.article_fab);
        mArticlePic = (ImageView) findViewById(R.id.article_pic);
        mArticleTitle = (TextView) findViewById(R.id.article_title);
        mArticleDate = (TextView) findViewById(R.id.article_date);
        mArticleAuthor = (TextView) findViewById(R.id.article_author);
        mArticleAuthorImage = (CircleImageView) findViewById(R.id.article_author_img);
        mArticleContent = (TextView) findViewById(R.id.article_content);

        // AppBar的监听
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int maxScroll = appBarLayout.getTotalScrollRange();
                float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;
                handleAlphaOnTitle(percentage);
            }
        });


        final Article article = getIntent().getParcelableExtra("article");
        Glide.with(this).load(article.getImageId()).into(mArticlePic);
        mArticleTitle.setText(article.getTitle());
        mArticleDate.setText(article.getDate());
        mArticleAuthor.setText(article.getWriter().getName());

        if (savedInstanceState == null) {
            mFloatingActionButton.setScaleX(0);
            mFloatingActionButton.setScaleY(0);
            getWindow().getEnterTransition().addListener(new CustomTransitionListener() {
                @Override
                public void onTransitionEnd(Transition transition) {
                    getWindow().getEnterTransition().removeListener(this);
                    mFloatingActionButton.animate().scaleX(1).scaleY(1);
                }
            });
        }


        // 分享按钮
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onShowDialogGridClick();
            }
        });

        mArticleAuthorImage = (CircleImageView) findViewById(R.id.article_author_img);
        mArticleAuthorImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ArticleIndex.this, UserIndex.class);
                intent.putExtra("user", article.getWriter());
                intent.putExtra("flag", 1);
                startActivity(intent);
            }
        });
    }

    // 展示底部分享框
    public void onShowDialogGridClick() {
        if (mBottomSheetDialog != null) {
            mBottomSheetDialog.dismiss();
        }
        mShowingGridDialog = true;
        mBottomSheetDialog = new BottomSheetBuilder(this, R.style.AppTheme_BottomSheetDialog)
                .setMode(BottomSheetBuilder.MODE_GRID)
                .setAppBarLayout(mAppBarLayout)
                .setMenu(getResources().getBoolean(R.bool.tablet_landscape)
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


    @Override
    public void onBottomSheetItemClick(MenuItem item) {
        mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    protected void onDestroy() {
        // Avoid leaked windows
        if (mBottomSheetDialog != null) {
            mBottomSheetDialog.dismiss();
        }
        super.onDestroy();
    }


    // 控制FAB的显示
    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(mFloatingActionButton, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }
        } else {
            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mFloatingActionButton, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    // 设置渐变的动画
    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    // 返回上一个界面
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 自定义的一个监听器。
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    class CustomTransitionListener implements Transition.TransitionListener, Animator.AnimatorListener {

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {

        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }

        @Override
        public void onTransitionStart(Transition transition) {

        }

        @Override
        public void onTransitionEnd(Transition transition) {

        }

        @Override
        public void onTransitionCancel(Transition transition) {

        }

        @Override
        public void onTransitionPause(Transition transition) {

        }

        @Override
        public void onTransitionResume(Transition transition) {

        }
    }
}
