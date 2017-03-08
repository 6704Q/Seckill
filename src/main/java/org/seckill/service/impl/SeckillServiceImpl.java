package org.seckill.service.impl;

import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.execption.RepeatKillException;
import org.seckill.execption.SeckillCloseException;
import org.seckill.execption.SeckillExecption;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2017/3/7.
 */
@Service
public class SeckillServiceImpl implements SeckillService {

    private Logger logger= LoggerFactory.getLogger(this.getClass());

    @Resource
    private SeckillDao seckillDao;

    @Resource
    private SuccessKilledDao successKilledDao;

    //MD5加密混淆
    private String slat="sdlfgjrejg985LLDSKGLKSDJ545412^%$&LKNSDF";

    /**
     * 查询所有秒杀商品
     *
     * @return
     */
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0,4);
    }

    /**
     * 查询单个秒杀商品
     *
     * @param seckillId 秒杀商品ID
     * @return
     */
    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    /**
     * 秒杀开启时输出接口地址
     * 否则输出系统时间与秒杀时间
     * @param seckillId
     */
    public Exposer exportSeckillUrl(long seckillId) {
        Seckill seckill = seckillDao.queryById(seckillId);
        if (seckill == null){
            return new Exposer(false,seckillId);
        }
        Date startTime=seckill.getStartTime();//秒杀开始时间
        Date endTime=seckill.getEndTime();//秒杀结束时间
        Date nowTime=new Date();//系统当前时间
        if (nowTime.getTime()<startTime.getTime() || nowTime.getTime()>endTime.getTime()){
            return new Exposer(false,seckillId,nowTime.getTime(),startTime.getTime(),endTime.getTime());
        }
        String md5=getMD5(seckillId);
        return new Exposer(true,md5,seckillId);
    }

    /**
     * MD5加密
     * @param seckillId
     * @return MD5
     */
    private String getMD5(long seckillId){
        String base=seckillId+"/"+slat;
        String md5= DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    /**
     * 执行秒杀
     *
     * @param seckillId
     * @param userPhone
     * @param md5
     */
    @Transactional
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillExecption, SeckillCloseException, RepeatKillException {
        if(md5 == null ||!md5.equals(getMD5(seckillId))){
            throw new SeckillExecption("seckill date rewrite");
        }
        //执行秒杀逻辑 减库存+记录秒杀行为
        Date nowTime = new Date();
        int updateCount = seckillDao.reduceNumber(seckillId,nowTime);
        try {
            //减库存
            if (updateCount <= 0){
                //没有更新到记录，秒杀结束
                throw new SeckillCloseException("Seckill is Closed");
            }else {
                //记录购买行为
                int insertCount=successKilledDao.insertSuccessKilled(seckillId,userPhone);
                //唯一;seckillId,userPhone
                if (insertCount <= 0){
                    //重复秒杀
                    throw new RepeatKillException("Seckill Repeated");
                }else {
                    //秒杀成功
                    SuccessKilled successKilled=successKilledDao.queryByIdWithSeckill(seckillId,userPhone);
                    return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS,successKilled);
                }
            }
        }catch (SeckillCloseException e1){
            throw e1;
        }catch (RepeatKillException e2){
            throw e2;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            throw new SeckillExecption("Seckill inner error:"+e.getMessage());
        }
    }
}
