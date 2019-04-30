package com.rsun.provider.initialization;

import com.rsun.dao.HouseDao;
import com.rsun.dao.SystemDao;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InitSqliteTable implements InitializingBean {

    @Autowired
    private SystemDao systemDao;
    @Autowired
    private HouseDao houseDao;

    @Override
    public void afterPropertiesSet() {
        System.out.println("---------------------- INIT SQLITE START -----------------");
        if (systemDao.checkHouseInfoTable() < 1) {
            systemDao.createHouseInfoTable();
        }
        System.out.println("---------------------- INIT SQLITE END -----------------");
    }
}
