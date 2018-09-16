package com.yan.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;


public class SchemaDocumentUtil {

	public static Document schameToDocument(Object object, Class clazz){
		Document doc = new Document();
		
		//Class clazz = ZhiHuPeople.class;
		Method[] methods = clazz.getDeclaredMethods();
		if(methods != null && methods.length > 0){
			for(Method method : methods){
				String methodName = method.getName();
				//遍历get方法
				if(methodName.startsWith("get") || methodName.startsWith("is")){
					String fieldName = null;
					
					if(methodName.startsWith("get")) {
						fieldName = methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
					}else if(methodName.startsWith("is")) {
						fieldName = methodName.substring(2, 3).toLowerCase() + methodName.substring(3);
					}
					
					Class returnType = method.getReturnType();
					//这个是用来处理带泛型的的类型
					Type type = method.getGenericReturnType();
					
					Object value = null;
					try {
						value = method.invoke(object, new Object[0]);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						e.printStackTrace();
					}
					
					if(value != null){
						if("id".equals(fieldName)){
							if(!"".equals(value.toString().trim())){
								doc.append("_id", new ObjectId(value.toString()));
							}
						}else{
							if(returnType == List.class) {
//								if(type instanceof ParameterizedType){  
//					                ParameterizedType parameterizedType = (ParameterizedType) type;  
//					                //获取参数的类型  
//					                //System.out.println(parameterizedType.getRawType());  
//					                //获取参数的泛型列表  
//					                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();  
//					                Class type2 = (Class)actualTypeArguments[0];
//					                System.out.println(type2);  
//					            }
								List<Object> list = (List)value;
								List<Object> list2 = null;
								if(list != null) {
									list2 = new ArrayList<>();
									for(Object obj:list) {
										if(obj.getClass() == byte.class || obj.getClass() == Byte.class
												|| obj.getClass() == short.class || obj.getClass() == Short.class
												|| obj.getClass() == int.class || obj.getClass() == Integer.class
												|| obj.getClass() == long.class || obj.getClass() == Long.class
												
												|| obj.getClass() == float.class || obj.getClass() == Float.class
												|| obj.getClass() == double.class || obj.getClass() == Double.class
												
												|| obj.getClass() == char.class || obj.getClass() == Character.class
												|| obj.getClass() == String.class
												
												|| obj.getClass() == boolean.class || obj.getClass() == Boolean.class
												
												|| obj.getClass() == Date.class
												){
											list2.add(obj);
										}else{
											Class subClazz = obj.getClass();
											Document subDoc = schameToDocument(obj, subClazz);
											list2.add(subDoc);
										}
									}
								}
								doc.append(fieldName, list2);
							}else {
								if(value.getClass() == byte.class || value.getClass() == Byte.class
										|| value.getClass() == short.class || value.getClass() == Short.class
										|| value.getClass() == int.class || value.getClass() == Integer.class
										|| value.getClass() == long.class || value.getClass() == Long.class
										
										|| value.getClass() == float.class || value.getClass() == Float.class
										|| value.getClass() == double.class || value.getClass() == Double.class
										
										|| value.getClass() == char.class || value.getClass() == Character.class
										|| value.getClass() == String.class
										
										|| value.getClass() == boolean.class || value.getClass() == Boolean.class
										
										|| value.getClass() == Date.class
										){
									doc.append(fieldName, value);
								}else{
									Class subClazz = value.getClass();
									Document subDoc = schameToDocument(value, subClazz);
									doc.append(fieldName, subDoc);
								}
								
							}
							
						}
					}
					
				}
				
			}
		}
		
		return doc;
	}
	
	
	//可能应该遍历documen的key来组装对象的
	//现在采用的是遍历对象的属性，在document中查找下是否存在对应的value
	public static Object documentToSchame(Document document, Class clazz){
		Object object = null;
		//ZhiHuPeople zhiHuPeople = null; 
		if(document != null){
			//zhiHuPeople = new ZhiHuPeople();
			//Class clazz = ZhiHuPeople.class;
			try {
				object = clazz.newInstance();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Method[] methods = clazz.getDeclaredMethods();
			if(methods != null && methods.length > 0){
				for(Method method : methods){
					String methodName = method.getName();
					//遍历get方法
					if(methodName.startsWith("get") || methodName.startsWith("is")){
						String fieldName = null;
						
						if(methodName.startsWith("get")) {
							fieldName = methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
						}else if(methodName.startsWith("is")) {
							fieldName = methodName.substring(2, 3).toLowerCase() + methodName.substring(3);
						}
						
						Class returnType = method.getReturnType();
						
						String setterMethodName = null;
						if(methodName.startsWith("get")) {
							setterMethodName = "set" + methodName.substring(3);
						}else if(methodName.startsWith("is")) {
							setterMethodName = "set" + methodName.substring(2);
						}
						
						try {
							Method setterMethod = clazz.getMethod(setterMethodName, returnType);
							
							Object value = null;
							
							if("id".equals(fieldName)){
								value = document.get("_id");
								if(value != null){
									value = value.toString();
								}
								setterMethod.invoke(object, value);
							}else{
								value = document.get(fieldName);
								if(value != null){
									if(returnType == List.class) {
										
										if(value instanceof List || value instanceof ArrayList) {
											
											// 如果是List类型，得到其Generic的类型    
											
											//clazz.getField无法得到私有属性
											//getFields()：获得某个类的所有的公共（public）的字段，包括父类中的字段。 
											//getDeclaredFields()：获得某个类的所有声明的字段，即包括public、private和proteced，但是不包括父类的申明字段。
											Field field = clazz.getDeclaredField(fieldName);
											Type genericType = field.getGenericType();  
											//得到泛型里的class类型对象
											//设置一个默认值Object
											Class<?> genericClazz = Object.class;
											
											if(genericType instanceof ParameterizedType){     
									            ParameterizedType pt = (ParameterizedType) genericType;  
									            //得到泛型里的class类型对象    
									            genericClazz = (Class<?>)pt.getActualTypeArguments()[0];   
									        }
											
											
											//如果document中存的是ArrayList类型，那么不能使用BsonArray来转
											List<Object> list = (List)value;
											//因为前面已经判断过value非null了，此处不需要再次判断
											List<Object> list2 = new ArrayList<>();
											for(Object obj:list) {
												if(obj.getClass() == byte.class || obj.getClass() == Byte.class
														|| obj.getClass() == short.class || obj.getClass() == Short.class
														|| obj.getClass() == int.class || obj.getClass() == Integer.class
														|| obj.getClass() == long.class || obj.getClass() == Long.class
														
														|| obj.getClass() == float.class || obj.getClass() == Float.class
														|| obj.getClass() == double.class || obj.getClass() == Double.class
														
														|| obj.getClass() == char.class || obj.getClass() == Character.class
														|| obj.getClass() == String.class
														
														|| obj.getClass() == boolean.class || obj.getClass() == Boolean.class
														
														|| obj.getClass() == Date.class
														){
													list2.add(obj);
												}else{
													obj = documentToSchame((Document)obj, genericClazz);
													
													list2.add(obj);
												}
											}
											
											value = list2;
										}
//										else if (value instanceof BsonArray) {
//											BsonArray bsonArray = (BsonArray)value;
//											if(bsonArray != null) {
//												value = Arrays.asList(bsonArray.toArray());
//											}
//										}
									}
									
									// 只有value非null的时候，才有必要调用set方法
									if(value instanceof Document){
										value = documentToSchame((Document)value, returnType);
									}
									
									setterMethod.invoke(object, value);
								}
								
							}
							
							
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					}
					
				}
			}
		}
		
		return object;
	}
}
