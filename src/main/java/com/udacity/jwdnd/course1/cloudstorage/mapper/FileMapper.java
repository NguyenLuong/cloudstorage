package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    @Select("SELECT * FROM FILES WHERE userid = #{userid}")
    List<File> getFileWithUserId(Integer userid);

    @Select("SELECT * FROM FILES WHERE fileId = #{fileId}")
    File getFileWithFileId(Integer fileId);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) VALUES (#{filename}, #{contenttype}, #{filesize}, #{userid}, #{filedata})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insertFile(File file);

    @Delete("DELETE FROM FILES WHERE fileId = #{fileId}")
    int deleteFile(int fileId);

    @Select("SELECT count(*) FROM FILES WHERE filename = #{filename} AND userid = #{userid}")
    int checkFileIsExist(String filename, Integer userid);
}
