package com.bjpowernode.crm.utils;

import java.util.UUID;

public class UUIDUtil {
	
	public static String getUUID(String loginPwd){
		
		return UUID.randomUUID().toString().replaceAll("-","");
		
	}
	
}
