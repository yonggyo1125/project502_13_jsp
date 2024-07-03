package org.choongang.file.mappers;

import org.choongang.global.config.DBConn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class MapperTest {

    private FileInfoMapper mapper;

    @BeforeEach
    void init() {
        mapper = DBConn.getSession().getMapper(FileInfoMapper.class);
    }

    @Test
    void registerTest() {
        String gid = UUID.randomUUID().toString();
        String location1 = "loc1";
        String location2 = "loc2";

        for (int i = 0; i < 10; i++) {

        }
    }
}
