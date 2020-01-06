package com.lxy.basemodel.network.model;

/**
 * Created by Administrator on 2020/1/3.
 */

public class CoinInfo {


    /**
     * level : 45
     * rank : 5
     * userId : 2
     * coinCount : 4500
     * username : x**oyang
     */
    private int level;
    private int rank;
    private int userId;
    private int coinCount;
    private String username;

    public void setLevel(int level) {
        this.level = level;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setCoinCount(int coinCount) {
        this.coinCount = coinCount;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getLevel() {
        return level;
    }

    public int getRank() {
        return rank;
    }

    public int getUserId() {
        return userId;
    }

    public int getCoinCount() {
        return coinCount;
    }

    public String getUsername() {
        return username;
    }
}
