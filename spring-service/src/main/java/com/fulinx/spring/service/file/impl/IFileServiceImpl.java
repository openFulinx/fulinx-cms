/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.service.file.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fulinx.spring.core.data.enums.RecordRemoveStatusEnum;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.fulinx.spring.core.spring.exception.BusinessException;
import com.fulinx.spring.core.utils.MiscUtils;
import com.fulinx.spring.data.mysql.dao.mapper.IFileDao;
import com.fulinx.spring.data.mysql.dao.podo.file.FileListConditionPo;
import com.fulinx.spring.data.mysql.dao.podo.file.FileListResultDo;
import com.fulinx.spring.data.mysql.entity.TbFileEntity;
import com.fulinx.spring.data.mysql.service.TbFileEntityService;
import com.fulinx.spring.service.enums.ErrorMessageEnum;
import com.fulinx.spring.service.file.IFileService;
import com.fulinx.spring.service.file.dto.FileListResultDto;
import com.fulinx.spring.service.file.dto.FileQueryConditionDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class IFileServiceImpl implements IFileService {
    private final TbFileEntityService tbFileEntityService;

    private final IFileDao iFileDao;

    @Lazy
    @Autowired
    public IFileServiceImpl(TbFileEntityService tbFileEntityService, IFileDao iFileDao) {
        this.tbFileEntityService = tbFileEntityService;
        this.iFileDao = iFileDao;
    }


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
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Optional<TbFileEntity> create(String originalFileName, String fileName, String fileContentType, String fileExtensionName, String path, String fileUrl, String sha256) throws BusinessException {
        TbFileEntity tbFileEntity = new TbFileEntity();
        setFileEntity(originalFileName, fileName, fileContentType, fileExtensionName, path, fileUrl, sha256, tbFileEntity);
        boolean isOk = tbFileEntityService.save(tbFileEntity);
        return Optional.ofNullable(isOk ? tbFileEntity : null);
    }

    @Override
    public Optional<TbFileEntity> create(Integer userId, String originalFileName, String fileName, String fileContentType, String fileExtensionName, String path, String fileUrl, String sha256) throws BusinessException {
        TbFileEntity tbFileEntity = new TbFileEntity();
        setFileEntity(originalFileName, fileName, fileContentType, fileExtensionName, path, fileUrl, sha256, tbFileEntity);
        boolean isOk = tbFileEntityService.save(tbFileEntity);
        return Optional.ofNullable(isOk ? tbFileEntity : null);
    }


    /**
     * 删除文件
     *
     * @param ids
     * @return
     * @throws BusinessException
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean remove(List<Integer> ids) throws BusinessException {
        for (Integer id : ids) {
            TbFileEntity tbFileEntity = this.lockById(id).orElseThrow(() -> {
                log.warn("删除文件失败，文件不存在，id = {}", id);
                return new BusinessException(ErrorMessageEnum.FILE_NOT_EXISTS.getMessage(), ErrorMessageEnum.FILE_NOT_EXISTS.getIndex());
            });
            tbFileEntity.setIsDelete(RecordRemoveStatusEnum._LOGICALLY_DELETED.getIndex());
            tbFileEntityService.removeById(id);
        }
        return true;
    }


    /**
     * 锁表查
     *
     * @param id
     * @return
     */
    @Override
    public Optional<TbFileEntity> lockById(Serializable id) {
        return Optional.ofNullable(iFileDao.lockById(id));
    }

    /**
     * 查询单个
     *
     * @param id
     * @return
     */
    @Override
    public Optional<FileListResultDto> getById(Integer id) throws BusinessException {
        this.lockById(id).orElseThrow(() -> {
            log.warn("查看文件失败，文件不存在，id = {}", id);
            return new BusinessException(ErrorMessageEnum.FILE_NOT_EXISTS.getMessage(), ErrorMessageEnum.FILE_NOT_EXISTS.getIndex());
        });
        FileQueryConditionDto fileQueryConditionDto = new FileQueryConditionDto();
        fileQueryConditionDto.setId(id);
        IPage<FileListResultDto> fileQueryResultDtoIPage = this.page(fileQueryConditionDto, 1, 1);
        return Optional.ofNullable(fileQueryResultDtoIPage.getTotal() == 0 ? null : fileQueryResultDtoIPage.getRecords().get(0));
    }

    /**
     * 查询列表
     *
     * @param fileQueryConditionDto
     * @return
     */
    @Override
    public List<FileListResultDto> list(FileQueryConditionDto fileQueryConditionDto) {
        FileListConditionPo fileListConditionPo = MiscUtils.copyProperties(fileQueryConditionDto, FileListConditionPo.class);
        List<FileListResultDo> fileListResultDoList = iFileDao.list(fileListConditionPo);
        return MiscUtils.copyList(fileListResultDoList, FileListResultDto.class);
    }

    /**
     * 列表-分页
     *
     * @param fileQueryConditionDto
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @Override
    public IPage<FileListResultDto> page(FileQueryConditionDto fileQueryConditionDto, int pageNumber, int pageSize) {
        Page<FileListResultDo> page = PageHelper.startPage(pageNumber, pageSize);
        PageHelper.orderBy(String.format("%s desc", TbFileEntity.ID));
        List<FileListResultDto> fileQueryResultDtoList = list(fileQueryConditionDto);
        IPage<FileListResultDto> fileQueryResultDtoIPage = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>();
        fileQueryResultDtoIPage.setTotal(page.getTotal());
        fileQueryResultDtoIPage.setRecords(fileQueryResultDtoList);
        return fileQueryResultDtoIPage;
    }

    /**
     * @param fileName
     * @param fileContentType
     * @param fileExtensionName
     * @param path
     * @param tbFileEntity
     */
    private void setFileEntity(String originalFileName, String fileName, String fileContentType, String fileExtensionName, String path, String fileUrl, String sha256, TbFileEntity tbFileEntity) {
        tbFileEntity.setOriginalFileName(originalFileName);
        tbFileEntity.setFileName(fileName);
        tbFileEntity.setFileContentType(fileContentType);
        tbFileEntity.setFileExtensionName(fileExtensionName);
        tbFileEntity.setPath(path);
        tbFileEntity.setFileUrl(fileUrl);
        tbFileEntity.setSha256(sha256);
    }
}
