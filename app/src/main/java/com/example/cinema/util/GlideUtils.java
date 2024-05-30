package com.example.cinema.util;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.cinema.R;

public class GlideUtils {

    /**
     * Load an image from a URL into an ImageView as a banner.
     *
     * @param url       The URL of the image.
     * @param imageView The ImageView to load the image into.
     */
    public static void loadUrlBanner(String url, ImageView imageView) {
        if (imageView == null) {
            return;
        }
        if (StringUtil.isEmpty(url)) {
            imageView.setImageResource(R.drawable.img_no_image);
            return;
        }
        Glide.with(imageView.getContext())
                .load(url)
                .error(R.drawable.img_no_image)
                .dontAnimate()
                .into(imageView);
    }

    /**
     * Load an image from a URL into an ImageView.
     *
     * @param url       The URL of the image.
     * @param imageView The ImageView to load the image into.
     */
    public static void loadUrl(String url, ImageView imageView) {
        if (imageView == null) {
            return;
        }
        if (StringUtil.isEmpty(url)) {
            imageView.setImageResource(R.drawable.img_no_available);
            return;
        }
        Glide.with(imageView.getContext())
                .load(url)
                .error(R.drawable.img_no_available)
                .dontAnimate()
                .into(imageView);
    }
}
