package lxy.com.wanandroid.officeAccount;

import java.util.List;

import lxy.com.wanandroid.home.model.ArticleModel;

/**
 * Created by Administrator on 2019/12/6.
 */

public class OfficeAccountArticleModel {
    private int curPage;
    private int offset;
    private boolean over;
    private int pageCount;
    private int size;
    private int total;

    private List<ArticleModel> datas;

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public boolean isOver() {
        return over;
    }

    public void setOver(boolean over) {
        this.over = over;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ArticleModel> getDatas() {
        return datas;
    }

    public void setDatas(List<ArticleModel> datas) {
        this.datas = datas;
    }

}
