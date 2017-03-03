package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by admin on 2017/3/2.
 */

/**
 * junit启动时加载springioc容器
 */
@RunWith(SpringJUnit4ClassRunner.class)
/**
 * 告诉junit spring配置文件位置
 */
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {

    //注入dao依赖
    @Resource
    private SeckillDao seckillDao;

    @Test
    public void queryById() throws Exception {
            long id=1000;
            Seckill seckill=seckillDao.queryById(id);
            SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println(sd.format(seckill.getCreateTime()));
            System.out.println(seckill.toString());
    }

    @Test
    public void queryAll() throws Exception {
        List<Seckill> seckilllist=seckillDao.queryAll(0,100);
        for (Seckill sc : seckilllist){
            System.out.println(sc.toString());
        }
    }

    @Test
    public void reduceNumber() throws Exception {
        Date killTime=new Date();
        int updateCount=seckillDao.reduceNumber(1000,killTime);
        System.out.println("updateCount="+updateCount);

    }



}