package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.Seckill;

import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2017/3/1.
 */
public interface SeckillDao {

    /**
     * 减库存
     * @param seckillId 商品ID
     * @param killTime 操作时间
     * @return
     */
    int reduceNumber(@Param("seckillId") long seckillId,@Param("killTime") Date killTime);


    /**
     * 根据ID获取秒杀商品
     * @param seckillId 商品ID
     * @return
     */
    Seckill queryById(long seckillId);


    /**
     * 根据偏移量获取秒杀对象列表
     * @param offset
     * @param limit
     * @return
     */
    List<Seckill> queryAll(@Param("offset") int offset,@Param("limit") int limit);

}
