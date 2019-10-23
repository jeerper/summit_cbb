package com.summit.exception;

/**
 * @author yt
 */
public class EsException extends Exception {
    /**
     * 无参构造方法
     */
    public EsException() {

        super();
    }

    /**
     * 有参的构造方法
     *
     * @param message
     */
    public EsException(String message) {
        super(message);

    }

    /**
     * 用指定的详细信息和原因构造一个新的异常
     *
     * @param message
     * @param cause
     */
    public EsException(String message, Throwable cause) {

        super(message, cause);
    }

    /**
     * 用指定原因构造一个新的异常
     *
     * @param cause
     */
    public EsException(Throwable cause) {

        super(cause);
    }
}
