package com.lxy.basemodel.network.model;

/**
 * Created by Administrator on 2020/1/3.
 */

public class ShareModel<T> {
    private CoinInfo coinInfo;

    private PageModel<T> pageModel;

    public CoinInfo getCoinInfo() {
        return coinInfo;
    }

    public void setCoinInfo(CoinInfo coinInfo) {
        this.coinInfo = coinInfo;
    }

    public PageModel<T> getPageModel() {
        return pageModel;
    }

    public void setPageModel(PageModel<T> pageModel) {
        this.pageModel = pageModel;
    }
}
