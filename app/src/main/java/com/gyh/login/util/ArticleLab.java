package com.gyh.login.util;

import android.content.Context;

import com.gyh.login.R;
import com.gyh.login.db.Article;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ArticleLab {
    private static ArticleLab sArticleLab;

    private List<Article> mArticles;

    public static ArticleLab get(Context context) {
        if (sArticleLab == null) {
            sArticleLab = new ArticleLab(context);
        }
        return sArticleLab;
    }

    private ArticleLab(Context context) {
        mArticles = new ArrayList<>();
        Article one = new Article(R.drawable.article_1, "最美海滩", "世界上最美的十大海滩 \n再不去就老了，带上心爱的他她，说走就走", "二月 21");
        Article two = new Article(R.drawable.article_2, "现代建筑", "堪比鬼斧神工 \n看看建筑师眼中的几何与我们都有哪些不同", "二月 11");
        mArticles.add(one);
        mArticles.add(two);
    }

    public List<Article> getArticles() {
        return mArticles;
    }

    public Article getArticle(UUID id) {
        for (Article article : mArticles) {
            if (article.getId().equals(id)) {
                return article;
            }
        }
        return null;
    }
}
