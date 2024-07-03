package org.choongang.file.mappers;

import org.apache.ibatis.annotations.Param;
import org.choongang.file.entities.FileInfo;

import java.util.List;

public interface FileInfoMapper {
    List<FileInfo> getList(@Param("gid") String gid, @Param("location") String location);
    List<FileInfo> getListDone(@Param("gid") String gid, @Param("location") String location);
    List<FileInfo> getListUnDone(@Param("gid") String gid, @Param("location") String location);
    FileInfo get(long seq);
    int register(FileInfo fileInfo);
    int delete(long seq);
    int deletes(@Param("gid") String gid, @Param("location") String location);
    int updateDone(String gid);
}
