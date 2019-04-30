package com.rsun.provider.aggregator.struct;

/**
 * Created by yfyuan on 2017/1/19.
 */
public class ColumnIndex {

    private int index;
    private String aggType;
    private String name;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getAggType() {
        return aggType;
    }

    public void setAggType(String aggType) {
        this.aggType = aggType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
