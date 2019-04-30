package com.rsun.dao;

import com.rsun.dto.HouseInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HouseDao {
    List<HouseInfo> getHouseList(@Param("idx") String idx);

    int insertHouses(@Param("list") List<HouseInfo> list);
}
