package org.seckill.web;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.dto.SeckillResult;
import org.seckill.entity.Seckill;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.execption.RepeatKillException;
import org.seckill.execption.SeckillCloseException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2017/3/9.
 */
@Controller
@RequestMapping("/seckill")//模块
public class SeckillController {

    private Logger logger= LoggerFactory.getLogger(this.getClass());

    @Resource
    private SeckillService seckillService;

    /**
     * 秒杀列表页面
     * @param model
     * @return
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public  String list(Model model){
        List<Seckill> list=seckillService.getSeckillList();
        model.addAttribute("list",list);
        return "list";
    }

    /**
     * 秒杀详情页
     * @param seckillId 秒杀商品ID
     * @param model
     * @return
     */
    @RequestMapping(value = "/{seckillId}/detail",method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, Model model){
        if (seckillId == null){
            return "redirect:/seckill/list";
        }
        Seckill seckill=seckillService.getById(seckillId);
        if (seckill == null){
            return "forwad:/seckill/list";
        }
        model.addAttribute("seckill",seckill);
        return "detail";
    }

    @RequestMapping(value = "/{seckillId}/exposer",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId){
        SeckillResult<Exposer> result=null;
        try {
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            result=new SeckillResult<Exposer>(true,exposer);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            result=new SeckillResult<Exposer>(false,e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/{seckillId}/{md5}/execution",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId, @PathVariable("md5") String md5,
                                                   @CookieValue(value = "killPhone",required = false) Long killPhone){
        if (killPhone == null){
            return new SeckillResult<SeckillExecution>(false,"手机号码为空！");
        }
        try {
            SeckillExecution seckillExecution = seckillService.executeSeckill(seckillId,killPhone,md5);
            return new SeckillResult<SeckillExecution>(true,seckillExecution);
        }catch (RepeatKillException e){
            SeckillExecution seckillExecution=new SeckillExecution(seckillId, SeckillStateEnum.REPEAT_KILL);
            return new SeckillResult<SeckillExecution>(false,seckillExecution);
        }catch (SeckillCloseException e){
            SeckillExecution seckillExecution=new SeckillExecution(seckillId, SeckillStateEnum.END);
            return new SeckillResult<SeckillExecution>(false,seckillExecution);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            SeckillExecution seckillExecution=new SeckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
            return new SeckillResult<SeckillExecution>(false,seckillExecution);
        }

    }


    /**
     * 获取服务器当前时间
     * @return
     */
    @RequestMapping(value = "/now/time",method = RequestMethod.GET)
    public SeckillResult<Long> getNowTime(){
        Date time=new Date();
        return new SeckillResult<Long>(true,time.getTime());
    }

}
