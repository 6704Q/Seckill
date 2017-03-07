package org.seckill.service;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.execption.RepeatKillException;
import org.seckill.execption.SeckillCloseException;
import org.seckill.execption.SeckillExecption;

import java.util.List;

/**
 * 业务接口：站在“使用者”角度设计接口
 * Created by admin on 2017/3/7.
 */
public interface SeckillService {

    /**
     * 查询所有秒杀商品
     * @return
     */
    List<Seckill> getSeckillList();

    /**
     * 查询单个秒杀商品
     * @param seckillId 秒杀商品ID
     * @return
     */
    Seckill getById(long seckillId);

    /**
     * 秒杀开启时输出接口地址
     * 否则输出系统时间与秒杀时间
     * @param seckillId
     */
    Exposer exportSeckillUrl(long seckillId);

    /**
     * 执行秒杀
     * @param seckillId
     * @param userPhone
     * @param md5
     */
    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws SeckillExecption,SeckillCloseException,RepeatKillException;

}
