package com.wzt.reggie.common;


/**
 * 实现：工具类，BaseContext包装ThreadLocal，为公共字段填充提供功能
 *
 * 同一线程间进行交互的类
 * 在同一线程中传递id的值
 *
 * 什么是 Threadlocal?Threadlocal井不是一个 Thread,而是 Thread的局部变量。
 * 当使用 Threadlocalt维护变量时, Threadloca为每个使用该变量的线程提供独立的变量副本,
 * 所以每一个线程都可以独立地改变自己的副本,
 * 而不会影响其它线程所对应的副本Threadlocal为每个线程提供单独一份存储空间,
 * 具有线程隔离的效果,只有在线程内才能获取到对应的值,线程外则不能访问
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setThreadLocalId(Long id) {
       threadLocal.set(id);
    }

    public static Long getThreadLocalId() {
        return threadLocal.get();
    }


}
