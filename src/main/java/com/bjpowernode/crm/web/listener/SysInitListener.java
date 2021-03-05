package com.bjpowernode.crm.web.listener;

import com.bjpowernode.crm.settings.domain.DicType;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.service.DicService;
import com.bjpowernode.crm.settings.service.impl.DicServiceImpl;
import com.bjpowernode.crm.utils.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author neo
 * @date 2021/3/3
 * @time 20:58
 */
public class SysInitListener implements ServletContextListener {

    /*

        该方法是用来监听上下文域对象的方法，当服务器启动的时候，上下文
        与对象被创建完毕后，马上执行该方法。

         event：该参数能够取得监听的对象
            监听的是什么对象，就可以通过该参数取得什么对象
     */
    @Override
    public void contextInitialized(ServletContextEvent event) {

        System.out.println("application 上下文对象被创建了");
        ServletContext application = event.getServletContext();

        // 取数据字典，并将数据字典存储到application域对象中
        /*

            对于数据字典一定要分门别类的保存，按照typeCode进行分类

         */
        DicService ds = (DicService) ServiceFactory.getService(new DicServiceImpl());

        /*

            listener需要从业务从获取的数据是7个List(因为每一个List是一类的数据字典值)
            故我们在业务层需要将7个List封装在一个Map中
                Map<String, List<DicValue>> map = new HashMap<>();
                map.put("key", value)
                ....

            map中的key应该是唯一不可重复的,故我从后台查询处Dic_type类型名即是  tbl_dic_type的code字段
            每一个key是一种typeCode，而每一个typeCode可以找到多个DicValue对象

         */

        Map<String, List<DicValue>> map = ds.getAll();
        // 将map解析为上下文域对象中保存的键值对儿
        Set<String> set = map.keySet();
//        System.out.println("==============" + set);
        for(String key : set){
//            System.out.println("------------------------>" + map.get(key));
            application.setAttribute(key + "List", map.get(key));

        }


    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {

    }
}
