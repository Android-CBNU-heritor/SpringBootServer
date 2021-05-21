package com.billlog.rest.api.mapper;

import com.billlog.rest.api.model.file.FileManage;
import com.billlog.rest.api.model.file.FileUploadResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FileMapper {

    int insertFileManage(FileManage fileManage);

    int modifyFileManage(FileManage fileManage);

    int insertFiletoDb(FileUploadResponse fileUploadResponse);

    List<FileUploadResponse> getFilesByIdx(@Param("file_idx") int file_idx);
}

