package com.yt.bishe.service.impl;

import com.yt.bishe.dao.EchartsDao;
import com.yt.bishe.service.EchartsService;
import com.yt.bishe.utils.Echarts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class EchartsServiceImpl implements EchartsService {
    @Autowired
    private EchartsDao echartsDao;
    @Override
    public List<Echarts> getBooks() {
        return echartsDao.getBooks();
    }

    @Override
    public List<Echarts> getSalesInDay(){
//        List<Echarts> echarts = echartsDao.getSalesInDay();
//        Map map = new HashMap();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        Iterator<Echarts> iterator = echarts.iterator();
//        while (iterator.hasNext()){
//            Echarts es = iterator.next();
//            String date = sdf.format(es.getcName());
//            if (map.containsKey(date)){
//                int count = (int) es.getValue();
//                int v = (int) map.get(date);
//                map.put(date,v+count);
//            }else {
//                map.put(date,es.getValue());
//            }
//        }
//        return map;
        return echartsDao.getSalesInDay();
    }
}
