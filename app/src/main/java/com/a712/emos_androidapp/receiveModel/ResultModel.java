package com.a712.emos_androidapp.receiveModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/17.
 */

public class ResultModel<T> {
    /**
     * 是否成功
     */
    private boolean flag = false;
    /**
     * 详细信息
     */
    private String info;
    /**
     * 返回结果条数
     */
    private int total = 0;

    private String select = null;

    /**
     * 返回结果详细信息
     */
    private List<T> rows = new ArrayList<T>();


    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getSelect() {
        return select;
    }

    public void setSelect(String select) {
        this.select = select;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
