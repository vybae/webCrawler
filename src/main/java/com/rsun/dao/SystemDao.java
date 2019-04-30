package com.rsun.dao;

import org.springframework.stereotype.Repository;

@Repository
public interface SystemDao {

    int checkHouseInfoTable();

    void createHouseInfoTable();
}
