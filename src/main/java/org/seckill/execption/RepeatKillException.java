package org.seckill.execption;

/**
 * Created by admin on 2017/3/7.
 */
public class RepeatKillException extends RuntimeException{

    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
