package com.bjpowernode.crm.settings.test;

import com.bjpowernode.crm.utils.DateTimeUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author neo
 * @date 2021/2/19
 * @time 12:20
 */
public class TestMain {
    public static void main(String[] args) {

        // 验证失效时间
        // 失效时间
//        String expireTime = "2021-02-19 13:25:16";
        // 当前系统时间
        /*
        String currentTime = DateTimeUtil.getSysTime();
        int count = expireTime.compareTo(currentTime);
        System.out.println(count);
         */

        // 验证锁定状态
//        String lockState = "0";
//        if("0".equals(lockState)){
//            System.out.println("账号已锁定");
//        }
//

        // 验证IP地址
//        String ip = "127.0.0.5";
        // 允许访问的ip地址群
        /*
        String allowIp = "192.168.0.1,127.0.0.1";
         if (allowIp.contains(ip)){
             System.out.println("允许");
         }else{
             System.out.println("受限");
         }

         */
    }
}
