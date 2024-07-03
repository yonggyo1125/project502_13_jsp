package org.choongang.file.mappers;

import org.apache.ibatis.session.SqlSession;
import org.choongang.file.entities.FileInfo;
import org.choongang.global.config.DBConn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

public class MapperTest {

    private FileInfoMapper mapper;
    private SqlSession session;
    @BeforeEach
    void init() {
        session = DBConn.getSession();


        mapper = session.getMapper(FileInfoMapper.class);
    }

    @Test
    void registerTest() {
        String gid = UUID.randomUUID().toString();
        String location1 = "loc1";
        String location2 = "loc2";

        for (int i = 0; i < 10; i++) {
            FileInfo item = FileInfo.builder()
                    .gid(gid)
                    .location(i % 2 == 0 ? location1 : location2)
                    .fileName("파일" + i + ".png")
                    .extension(".png")
                    .contentType("image/png")
                    .done(i % 2)
                    .build();
            mapper.register(item);
            session.clearCache();
        }
    }

    @Test
    void getListTest() {
        String gid = "6f117ccd-5a0d-4224-a2ce-40aa1161a525";
        List<FileInfo> items = mapper.getList(gid, "loc1");
        items.forEach(System.out::println);
    }

    @Test
    void updateDoneTest() {
        String gid = "6f117ccd-5a0d-4224-a2ce-40aa1161a525";
        mapper.updateDone(gid);
    }

    @Test
    void deleteTest() {
        mapper.delete(15L);
    }

    @Test
    void deletesTest() {
        String gid = "6f117ccd-5a0d-4224-a2ce-40aa1161a525";
        String location = "loc2";
        mapper.deletes(gid, location);
    }
}
