package com.fulinx.spring.data.mysql.service.impl;

import com.fulinx.spring.data.mysql.entity.TbCategoryEntity;
import com.fulinx.spring.data.mysql.entity.mapper.TbCategoryMapper;
import com.fulinx.spring.data.mysql.service.TbCategoryEntityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * Category Table 服务实现类
 * </p>
 *
 * @author fulinx
 * @since 2024-07-05
 */
@Service
public class TbCategoryEntityServiceImpl extends ServiceImpl<TbCategoryMapper, TbCategoryEntity> implements TbCategoryEntityService {

}
