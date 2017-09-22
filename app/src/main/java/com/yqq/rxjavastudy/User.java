package com.yqq.rxjavastudy;

/**
 * Created by Administrator on 2017/9/21 0021.
 */

public class User {

    public User setmAge(String mAge) {
        this.mAge = mAge;
        return this;
    }

    public String mName;
    public String mAge;

    public User setmName(String mName) {
        this.mName = mName;
        return this;
    }

    @Override
    public String toString() {
        return "User{" +
                "mName='" + mName + '\'' +
                ", mAge='" + mAge + '\'' +
                '}';
    }

    //1对多的关系
    public User setmEducationBackgroud(EducationBackgroud mEducationBackgroud) {
        this.mEducationBackgroud = mEducationBackgroud;
        return this;
    }

    public EducationBackgroud mEducationBackgroud;



}
