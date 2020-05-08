package com.yt.bishe.dao;

import com.yt.bishe.utils.Echarts;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface EchartsDao {
    List<Echarts> getBooks();

    List<Echarts> getSalesInDay();
}
