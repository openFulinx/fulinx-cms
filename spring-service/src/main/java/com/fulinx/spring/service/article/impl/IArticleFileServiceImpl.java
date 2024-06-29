/*
 * Copyright (c) Minong Tech. 2023.
 */

package com.fulinx.spring.service.article.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fulinx.spring.core.data.enums.RecordRemoveStatusEnum;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.fulinx.spring.core.spring.exception.BusinessException;
import com.fulinx.spring.core.utils.MiscUtils;
import com.fulinx.spring.data.mysql.dao.mapper.IArticleFileDao;
import com.fulinx.spring.data.mysql.dao.podo.article.ArticleFileListConditionPo;
import com.fulinx.spring.data.mysql.dao.podo.article.ArticleFileListResultDo;
import com.fulinx.spring.data.mysql.entity.TbArticleFileRelationEntity;
import com.fulinx.spring.data.mysql.entity.TbFileEntity;
import com.fulinx.spring.data.mysql.service.TbArticleFileRelationEntityService;
import com.fulinx.spring.service.article.IArticleFileService;
import com.fulinx.spring.service.article.IArticleService;
import com.fulinx.spring.service.article.dto.ArticleFileListResultDto;
import com.fulinx.spring.service.article.dto.ArticleFileQueryConditionDto;
import com.fulinx.spring.service.enums.ErrorMessageEnum;
import com.fulinx.spring.service.file.IFileService;
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
public class IArticleFileServiceImpl implements IArticleFileService {

    private final TbArticleFileRelationEntityService tbArticleFileRelationEntityService;

    private final IArticleService iArticleService;

    private final IFileService iFileService;

    private final IArticleFileDao iArticleFileDao;

    @Lazy
    @Autowired
    public IArticleFileServiceImpl(TbArticleFileRelationEntityService tbArticleFileRelationEntityService, IArticleService iArticleService, IFileService iFileService, IArticleFileDao iArticleFileDao) {
        this.tbArticleFileRelationEntityService = tbArticleFileRelationEntityService;
        this.iArticleService = iArticleService;
        this.iFileService = iFileService;
        this.iArticleFileDao = iArticleFileDao;
    }


    /**
     * @param articleId
     * @param fileId
     * @return
     * @throws BusinessException
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Optional<TbArticleFileRelationEntity> create(Integer articleId, Integer fileId) throws BusinessException {
        // 检查文章是否存在
        iArticleService.lockById(articleId).orElseThrow(() -> {
            log.debug("新增文章文件失败，失败原因，文章不存在，articleId = {}", articleId);
            return new BusinessException(ErrorMessageEnum.ARTICLE_NOT_EXISTS.getMessage(), ErrorMessageEnum.ARTICLE_NOT_EXISTS.getIndex());
        });

        // 检查文件是否存在

        iFileService.lockById(fileId).orElseThrow(() -> {
            log.debug("新增文章文件失败，失败原因，文件不存在，fileId = {}", fileId);
            return new BusinessException(ErrorMessageEnum.ARTICLE_NOT_EXISTS.getMessage(), ErrorMessageEnum.ARTICLE_NOT_EXISTS.getIndex());
        });

        TbArticleFileRelationEntity tbArticleFileRelationEntity = new TbArticleFileRelationEntity();
        tbArticleFileRelationEntity.setArticleId(articleId);
        tbArticleFileRelationEntity.setFileId(fileId);
        boolean isOk = tbArticleFileRelationEntityService.save(tbArticleFileRelationEntity);
        return Optional.ofNullable(isOk ? tbArticleFileRelationEntity : null);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Boolean remove(List<Integer> ids) throws BusinessException {
        for (Integer id : ids) {
            TbArticleFileRelationEntity tbArticleFileRelationEntity = this.lockById(id).orElseThrow(() -> {
                log.debug("删除文章文件失败，失败原因，文章文件不存在，id = {}", id);
                return new BusinessException(ErrorMessageEnum.ARTICLE_FILE_NOT_EXISTS.getMessage(), ErrorMessageEnum.ARTICLE_FILE_NOT_EXISTS.getIndex());
            });
            tbArticleFileRelationEntity.setIsDelete(RecordRemoveStatusEnum._LOGICALLY_DELETED.getIndex());
            tbArticleFileRelationEntityService.removeById(tbArticleFileRelationEntity);
        }
        return true;
    }

    /**
     * 锁表查询单条记录
     *
     * @param id
     * @return
     */
    @Override
    public Optional<TbArticleFileRelationEntity> lockById(Serializable id) {
        return Optional.ofNullable(iArticleFileDao.lockById(id));
    }

    /**
     * 获取单条记录
     *
     * @param id
     * @return
     */
    @Override
    public Optional<ArticleFileListResultDto> getById(Integer id) throws BusinessException {
        this.lockById(id).orElseThrow(() -> {
            log.warn("查看文件失败，文件不存在，id = {}", id);
            return new BusinessException(ErrorMessageEnum.ARTICLE_FILE_NOT_EXISTS.getMessage(), ErrorMessageEnum.ARTICLE_FILE_NOT_EXISTS.getIndex());
        });
        ArticleFileQueryConditionDto articleFileQueryConditionDto = new ArticleFileQueryConditionDto();
        articleFileQueryConditionDto.setId(id);
        List<ArticleFileListResultDto> list = this.list(articleFileQueryConditionDto);
        return Optional.ofNullable(list.size() > 0 ? list.get(0) : null);
    }

    /**
     * 列表查询
     *
     * @param articleFileQueryConditionDto
     * @return
     */
    @Override
    public List<ArticleFileListResultDto> list(ArticleFileQueryConditionDto articleFileQueryConditionDto) {
        ArticleFileListConditionPo articleFileListConditionPo = MiscUtils.copyProperties(articleFileQueryConditionDto, ArticleFileListConditionPo.class);
        List<ArticleFileListResultDo> list = iArticleFileDao.list(articleFileListConditionPo);
        return MiscUtils.copyList(list, ArticleFileListResultDto.class);
    }

    /**
     * 列表带分页查询
     *
     * @param articleFileQueryConditionDto
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @Override
    public IPage<ArticleFileListResultDto> page(ArticleFileQueryConditionDto articleFileQueryConditionDto, int pageNumber, int pageSize) {
        Page<ArticleFileListResultDo> page = PageHelper.startPage(pageNumber, pageSize);
        PageHelper.orderBy(String.format("%s desc", TbFileEntity.ID));
        List<ArticleFileListResultDto> articleFileListResultDtoList = list(articleFileQueryConditionDto);
        IPage<ArticleFileListResultDto> articleFileListResultDtoIPage = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>();
        articleFileListResultDtoIPage.setTotal(page.getTotal());
        articleFileListResultDtoIPage.setRecords(articleFileListResultDtoList);
        return articleFileListResultDtoIPage;
    }
}
