package com.cowork.wiki.util;

import java.lang.reflect.Method;
import java.lang.reflect.Type;


public class SchameCopyUtil {

	
	public static Object simpleCopy(Object source, Class targetClazz){
		Class sourceClazz = source.getClass();
		Object target = null;
		
		if(source != null){
			try {
				target = targetClazz.newInstance();
				
				Method[] sourceMethods = sourceClazz.getDeclaredMethods();
				if(sourceMethods != null && sourceMethods.length > 0){
					for(Method sourceMethod : sourceMethods){
						String methodName = sourceMethod.getName();
						//遍历get方法
						if(methodName.startsWith("get")){
							String fieldName = methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
							Class returnType = sourceMethod.getReturnType();
							//这个是用来处理带泛型的的类型
							Type type = sourceMethod.getGenericReturnType();
							
							//从srouce类中获取到值
							Object value = sourceMethod.invoke(source, new Object[0]);
							
							if(value != null){
								//避免两个类中字段不一致的时候，因为找不到对应的set方法，报空指针
								
								//向target类中set值
								String setterMethodName = "set" + methodName.substring(3);
								Method targetSetterMethod = targetClazz.getMethod(setterMethodName, returnType);
								targetSetterMethod.invoke(target, value);
							}
						}
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return target;
	}
	
}
