package com.muxi.workbench.ui.home.model;

import android.net.Uri;

import com.muxi.workbench.R;

import java.util.ArrayList;
import java.util.List;

public class BannerBean {
    private int imageRes;
    private Uri imageUri;
    private String imageTitle;

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public String getImageTitle() {
        return imageTitle;
    }

    public void setImageTitle(String imageTitle) {
        this.imageTitle = imageTitle;
    }


    public static List<BannerBean> getDefaultBanners() {
        List<BannerBean> banners = new ArrayList<>();
        BannerBean banner = new BannerBean();
        banner.setImageRes(R.drawable.banner);
        banners.add(banner);
        return banners;
    }
}
