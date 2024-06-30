package com.fulinx.web;

import com.fulinx.spring.data.mysql.dao.mapper.IUserDao;
import com.fulinx.spring.data.mysql.entity.TbSystemUserEntity;
import com.fulinx.spring.data.mysql.service.TbSystemUserEntityService;
import com.fulinx.spring.web.SpringWebApplication;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest(classes = SpringWebApplication.class)
@Slf4j
@ComponentScan({"com.fulinx"})
@MapperScan({"com.fulinx.spring.data.mysql.*.mapper"})
public class MybatisTests {

    private final IUserDao iUserDao;

    @Autowired
    public MybatisTests(IUserDao iUserDao) {
        this.iUserDao = iUserDao;
    }

    @Test
    void test() {
        iUserDao.lockById(1);
    }
}
