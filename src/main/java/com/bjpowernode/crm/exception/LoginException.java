package com.bjpowernode.crm.exception;

/**
 * @author neo
 * @date 2021/2/19
 * @time 12:16
 */
public class LoginException extends Exception {

    public LoginException(){

    }

    public LoginException(String message){
        super(message);
    }
}
