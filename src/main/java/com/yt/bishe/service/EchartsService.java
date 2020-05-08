package com.yt.bishe.service;

import com.yt.bishe.utils.Echarts;

import java.util.List;
import java.util.Map;

public interface EchartsService {
    List<Echarts> getBooks();

    List<Echarts> getSalesInDay();
}
