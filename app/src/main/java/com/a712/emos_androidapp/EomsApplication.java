package com.a712.emos_androidapp;

import android.app.Application;

import com.a712.emos_androidapp.utils.SystemParams;

/**
 * Created by Lenovo on 2017/6/23.
 */

public class EomsApplication extends Application {

    private String selectBeginDate;
    private String selectEndDate;

    public String getSelectBeginDate() {
        return selectBeginDate;
    }

    public void setSelectBeginDate(String selectBeginDate) {
        this.selectBeginDate = selectBeginDate;
    }

    public String getSelectEndDate() {
        return selectEndDate;
    }

    public void setSelectEndDate(String selectEndDate) {
        this.selectEndDate = selectEndDate;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setSelectBeginDate("");
        setSelectEndDate("");
        SystemParams.init(this);
    }
}
