package org.choongang.global.config.containers.mybatis;

import org.choongang.global.config.DBConn;
import org.choongang.global.config.annotations.mybatis.MapperScan;

import java.util.Arrays;

@MapperScan({"org.choongang.member.mapper"})
public class MapperProvider {

    public static MapperProvider instance;

    private MapperProvider() {}

    public static MapperProvider getInstance() {
        if (instance == null) {
            instance = new MapperProvider();
        }
        return instance;
    }

    public <T> T getMapper(Class clz) {

        MapperScan mapperScan = getClass().getAnnotation(MapperScan.class);
        boolean isMapper = Arrays.stream(mapperScan.value()).anyMatch(s -> s.startsWith(clz.getPackageName()));

        if (isMapper) {
            return (T)DBConn.getSession().getMapper(clz);
        }

        return null;
    }
}
