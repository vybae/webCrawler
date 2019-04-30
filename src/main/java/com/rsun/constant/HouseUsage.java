package com.rsun.constant;

import org.springframework.beans.factory.annotation.Value;

public enum HouseUsage {
    /*
    <option value="-1" selected="selected">不限</option>
    <option value="1">住宅</option>
    <option value="101">低层住宅</option>
    <option value="102">多层住宅</option>
    <option value="103">小高层住宅</option>
    <option value="104">高层住宅</option>
    <option value="105">别墅</option>
    <option value="2">商业</option>
    <option value="3">办公</option>
    <option value="4">车库</option>
    <option value="5">厂房仓库</option>
    <option value="6">其它</option>
    <option value="7">酒店式公寓</option>
     */
    NO_LIMIT,
    RESIDENCE,
    LOW_RESIDENCE,
    MULTI_RESIDENCE,
    MID_RESIDENCE,
    HIGH_RESIDENCE,
    VILLA,
    BUSINESS,
    OFFICE,
    CARPORT,
    WAREHOUSE,
    OTHER,
    HOTEL;

    @Value("NO_LIMIT")
    private String no_limit;
    @Value("RESIDENCE")
    private String residence;
    @Value("LOW_RESIDENCE")
    private String low_residence;
    @Value("MULTI_RESIDENCE")
    private String multi_residence;
    @Value("MID_RESIDENCE")
    private String mid_residence;
    @Value("HIGH_RESIDENCE")
    private String high_residence;
    @Value("VILLA")
    private String villa;
    @Value("BUSINESS")
    private String business;
    @Value("OFFICE")
    private String office;
    @Value("CARPORT")
    private String carport;
    @Value("WAREHOUSE")
    private String warehouse;
    @Value("OTHER")
    private String other;
    @Value("HOTEL")
    private String hotel;

    public String Value() {
        switch (this) {
            case RESIDENCE:
                return residence;
            case LOW_RESIDENCE:
                return low_residence;
            case MULTI_RESIDENCE:
                return multi_residence;
            case MID_RESIDENCE:
                return mid_residence;
            case HIGH_RESIDENCE:
                return high_residence;
            case VILLA:
                return  villa;
            case BUSINESS:
                return business;
            case OFFICE:
                return office;
            case CARPORT:
                return carport;
            case WAREHOUSE:
                return warehouse;
            case OTHER:
                return other;
            case HOTEL:
                return hotel;
            case NO_LIMIT:
            default:
                return no_limit;
        }
    }

}
