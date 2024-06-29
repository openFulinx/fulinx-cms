/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.service.file;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fulinx.spring.core.spring.exception.BusinessException;
import com.fulinx.spring.data.mysql.entity.TbFileEntity;
import com.fulinx.spring.service.file.dto.FileListResultDto;
import com.fulinx.spring.service.file.dto.FileQueryConditionDto;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface IFileService {
    /**
     * 新增文件
     *
     * @param fileName
     * @param fileContentType
     * @param fileExtensionName
     * @param path
     * @return
     * @throws BusinessException
     */
    Optional<TbFileEntity> create(String originalFileName, String fileName, String fileContentType, String fileExtensionName, String path, String fileUrl, String sha256) throws BusinessException;

    Optional<TbFileEntity> create(Integer userId, String originalFileName, String fileName, String fileContentType, String fileExtensionName, String path, String fileUrl, String sha256) throws BusinessException;

    /**
     * 删除文件
     *
     * @param fileIds
     * @return
     * @throws BusinessException
     */
    boolean remove(List<Integer> fileIds) throws BusinessException;

    /**
     * 锁表查
     *
     * @param id
     * @return
     */
    Optional<TbFileEntity> lockById(Serializable id);

    /**
     * 查询单个
     *
     * @param id
     * @return
     */
    Optional<FileListResultDto> getById(Integer id) throws BusinessException;

    /**
     * 查询列表
     *
     * @param fileQueryConditionDto
     * @return
     */
    List<FileListResultDto> list(FileQueryConditionDto fileQueryConditionDto);

    /**
     * 列表-分页
     *
     * @param fileQueryConditionDto
     * @param pageNumber
     * @param pageSize
     * @return
     */
    IPage<FileListResultDto> page(FileQueryConditionDto fileQueryConditionDto, int pageNumber, int pageSize);

}
