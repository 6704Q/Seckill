package org.seckill.execption;

/**
 * Created by admin on 2017/3/7.
 */
public class SeckillCloseException extends RuntimeException {

    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}
