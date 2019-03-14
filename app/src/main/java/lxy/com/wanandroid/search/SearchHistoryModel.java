package lxy.com.wanandroid.search;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Creator : lxy
 * date: 2019/3/14
 */

@Entity
public class SearchHistoryModel {

    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "sh_content")
    private String content;

    @Property(nameInDb = "sh_date")
    private String date;

    @Generated(hash = 494794436)
    public SearchHistoryModel(Long id, String content, String date) {
        this.id = id;
        this.content = content;
        this.date = date;
    }

    @Generated(hash = 2050687136)
    public SearchHistoryModel() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
