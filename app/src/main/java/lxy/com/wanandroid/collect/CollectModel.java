package lxy.com.wanandroid.collect;

import java.util.List;

/**
 * Creator : lxy
 * date: 2019/3/3
 */

public class CollectModel {


    /**
     * data : {"curPage":1,"datas":[{"author":"jia635","chapterId":26,"chapterName":"基础UI控件","courseId":13,"desc":"","envelopePic":"","id":9668,"link":"https://blog.csdn.net/jia635/article/details/52387658","niceDate":"2018-04-26","origin":"","originId":2874,"publishTime":1524735560000,"title":"为什么Dialog不能用Application的Context","userId":3825,"visible":0,"zan":0},{"author":"wustor","chapterId":171,"chapterName":"binder","courseId":13,"desc":"","envelopePic":"","id":7337,"link":"http://www.jianshu.com/p/1174c362d090","niceDate":"2018-04-08","origin":"","originId":2775,"publishTime":1523176723000,"title":"Binder学习(三)通过AIDL分析Binder通信流程","userId":3825,"visible":0,"zan":0},{"author":"请叫我大苏","chapterId":60,"chapterName":"Android Studio相关","courseId":13,"desc":"","envelopePic":"","id":7336,"link":"https://www.jianshu.com/p/68fd5373effc","niceDate":"2018-04-08","origin":"","originId":2796,"publishTime":1523176717000,"title":"分享两个提高效率的AndroidStudio小技巧","userId":3825,"visible":0,"zan":0},{"author":"Jensen_czx","chapterId":78,"chapterName":"性能优化","courseId":13,"desc":"","envelopePic":"","id":7057,"link":"https://www.jianshu.com/p/02d32ef00337","niceDate":"2018-04-04","origin":"","originId":2729,"publishTime":1522830924000,"title":"Android 资源加载机制剖析","userId":3825,"visible":0,"zan":0},{"author":"鸿洋","chapterId":61,"chapterName":"Android测试相关","courseId":13,"desc":"","envelopePic":"","id":6361,"link":"http://www.wanandroid.com/blog/show/2085","niceDate":"2018-03-26","origin":"","originId":2490,"publishTime":1522044413000,"title":"必知必会 | Android 测试相关的方方面面都在这儿","userId":3825,"visible":0,"zan":0}],"offset":0,"over":true,"pageCount":1,"size":20,"total":5}
     * errorCode : 0
     * errorMsg :
     */

    private DataBean data;
    private int errorCode;
    private String errorMsg;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public static class DataBean {
        /**
         * curPage : 1
         * datas : [{"author":"jia635","chapterId":26,"chapterName":"基础UI控件","courseId":13,"desc":"","envelopePic":"","id":9668,"link":"https://blog.csdn.net/jia635/article/details/52387658","niceDate":"2018-04-26","origin":"","originId":2874,"publishTime":1524735560000,"title":"为什么Dialog不能用Application的Context","userId":3825,"visible":0,"zan":0},{"author":"wustor","chapterId":171,"chapterName":"binder","courseId":13,"desc":"","envelopePic":"","id":7337,"link":"http://www.jianshu.com/p/1174c362d090","niceDate":"2018-04-08","origin":"","originId":2775,"publishTime":1523176723000,"title":"Binder学习(三)通过AIDL分析Binder通信流程","userId":3825,"visible":0,"zan":0},{"author":"请叫我大苏","chapterId":60,"chapterName":"Android Studio相关","courseId":13,"desc":"","envelopePic":"","id":7336,"link":"https://www.jianshu.com/p/68fd5373effc","niceDate":"2018-04-08","origin":"","originId":2796,"publishTime":1523176717000,"title":"分享两个提高效率的AndroidStudio小技巧","userId":3825,"visible":0,"zan":0},{"author":"Jensen_czx","chapterId":78,"chapterName":"性能优化","courseId":13,"desc":"","envelopePic":"","id":7057,"link":"https://www.jianshu.com/p/02d32ef00337","niceDate":"2018-04-04","origin":"","originId":2729,"publishTime":1522830924000,"title":"Android 资源加载机制剖析","userId":3825,"visible":0,"zan":0},{"author":"鸿洋","chapterId":61,"chapterName":"Android测试相关","courseId":13,"desc":"","envelopePic":"","id":6361,"link":"http://www.wanandroid.com/blog/show/2085","niceDate":"2018-03-26","origin":"","originId":2490,"publishTime":1522044413000,"title":"必知必会 | Android 测试相关的方方面面都在这儿","userId":3825,"visible":0,"zan":0}]
         * offset : 0
         * over : true
         * pageCount : 1
         * size : 20
         * total : 5
         */

        private int curPage;
        private int offset;
        private boolean over;
        private int pageCount;
        private int size;
        private int total;
        private List<DatasBean> datas;

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

        public List<DatasBean> getDatas() {
            return datas;
        }

        public void setDatas(List<DatasBean> datas) {
            this.datas = datas;
        }

        public static class DatasBean {
            /**
             * author : jia635
             * chapterId : 26
             * chapterName : 基础UI控件
             * courseId : 13
             * desc :
             * envelopePic :
             * id : 9668
             * link : https://blog.csdn.net/jia635/article/details/52387658
             * niceDate : 2018-04-26
             * origin :
             * originId : 2874
             * publishTime : 1524735560000
             * title : 为什么Dialog不能用Application的Context
             * userId : 3825
             * visible : 0
             * zan : 0
             */

            private String author;
            private int chapterId;
            private String chapterName;
            private int courseId;
            private String desc;
            private String envelopePic;
            private int id;
            private String link;
            private String niceDate;
            private String origin;
            private int originId;
            private long publishTime;
            private String title;
            private int userId;
            private int visible;
            private int zan;

            public String getAuthor() {
                return author;
            }

            public void setAuthor(String author) {
                this.author = author;
            }

            public int getChapterId() {
                return chapterId;
            }

            public void setChapterId(int chapterId) {
                this.chapterId = chapterId;
            }

            public String getChapterName() {
                return chapterName;
            }

            public void setChapterName(String chapterName) {
                this.chapterName = chapterName;
            }

            public int getCourseId() {
                return courseId;
            }

            public void setCourseId(int courseId) {
                this.courseId = courseId;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public String getEnvelopePic() {
                return envelopePic;
            }

            public void setEnvelopePic(String envelopePic) {
                this.envelopePic = envelopePic;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getLink() {
                return link;
            }

            public void setLink(String link) {
                this.link = link;
            }

            public String getNiceDate() {
                return niceDate;
            }

            public void setNiceDate(String niceDate) {
                this.niceDate = niceDate;
            }

            public String getOrigin() {
                return origin;
            }

            public void setOrigin(String origin) {
                this.origin = origin;
            }

            public int getOriginId() {
                return originId;
            }

            public void setOriginId(int originId) {
                this.originId = originId;
            }

            public long getPublishTime() {
                return publishTime;
            }

            public void setPublishTime(long publishTime) {
                this.publishTime = publishTime;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public int getVisible() {
                return visible;
            }

            public void setVisible(int visible) {
                this.visible = visible;
            }

            public int getZan() {
                return zan;
            }

            public void setZan(int zan) {
                this.zan = zan;
            }
        }
    }
}
