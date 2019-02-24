package lxy.com.wanandroid.home;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

import lxy.com.wanandroid.home.model.BannerModel;

/**
 * @author : lxy
 * date: 2019/2/8
 */

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        BannerModel.DataBean dataBean = (BannerModel.DataBean)path;
        Glide.with(context).load(dataBean.getImagePath()).into(imageView);
    }
}
