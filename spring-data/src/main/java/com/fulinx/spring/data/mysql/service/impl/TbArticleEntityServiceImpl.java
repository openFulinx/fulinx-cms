package com.fulinx.spring.data.mysql.service.impl;

import com.fulinx.spring.data.mysql.entity.TbArticleEntity;
import com.fulinx.spring.data.mysql.entity.mapper.TbArticleMapper;
import com.fulinx.spring.data.mysql.service.TbArticleEntityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * Article Table 服务实现类
 * </p>
 *
 * @author fulinx
 * @since 2024-07-01
 */
@Service
public class TbArticleEntityServiceImpl extends ServiceImpl<TbArticleMapper, TbArticleEntity> implements TbArticleEntityService {

}
