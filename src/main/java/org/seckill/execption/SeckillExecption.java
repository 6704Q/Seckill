package org.seckill.execption;

/**
 * Created by admin on 2017/3/7.
 */
public class SeckillExecption extends RuntimeException {

    public SeckillExecption(String message) {
        super(message);
    }

    public SeckillExecption(String message, Throwable cause) {
        super(message, cause);
    }
}
