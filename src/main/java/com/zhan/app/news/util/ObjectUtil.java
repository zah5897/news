package com.zhan.app.news.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import com.zhan.app.annotation.Id;
import com.zhan.app.annotation.IgnoreColumn;
import com.zhan.app.annotation.Json;
import com.zhan.app.annotation.Strategy;
import com.zhan.app.annotation.Table;

/**
 * @author cuisuqiang
 * @version 1.0
 * @说明 对象操纵高级方法
 * @since
 */
public class ObjectUtil {
	/**
	 * 返回一个对象的属性和属性值
	 */
	public static Map<String, String> getProperty(Object entityName) {
		Map<String, String> map = new HashMap<String, String>();
		Class<? extends Object> c = entityName.getClass();
		// 获得对象属性
		List<Field> allFields = new ArrayList<Field>();
		getClassField(c, allFields);
		for (Field f : allFields) {
			f.setAccessible(true);

			// 忽略的字段
			IgnoreColumn ignore = f.getAnnotation(IgnoreColumn.class);
			if (ignore != null) {
				continue;
			}
			// 主键递增忽略的字段
			Id id = f.getAnnotation(Id.class);
			if (id != null) {
				// uuid主键
				if (id.strategy() == Strategy.UUID) {
					String uuid = UUIDUtil.getUUID();
					map.put(f.getName(), uuid);
					try {
						invokeSetMethod(entityName, f.getName(), new Object[] { uuid }, String.class);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				continue;
			}

			Object result = null;
			try {
				result = invokeGetMethod(entityName, f.getName(), null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (result != null) {
				if (result instanceof Date) {// 日期类型
					map.put(f.getName(), DateTimeUtil.parse((Date) result));
				} else if (result.getClass().isEnum()) { // 枚举类型
					try {
						map.put(f.getName(), String.valueOf(invokeOrdinalValue(result)));
					} catch (Exception e) {
						map.put(f.getName(), String.valueOf(Integer.MAX_VALUE));
					}
				} else if (result instanceof Collection<?>) {
					// 主键递增忽略的字段
					Json json = f.getAnnotation(Json.class);
					if (json != null) {
						try {
							map.put(f.getName(), JSONUtil.writeValueAsString(invokeOrdinalValue(result)));
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} else {
					map.put(f.getName(), result.toString());
				}
			} else {
				map.put(f.getName(), "");
			}
		}
		return map;
	}

	private static List<Field> getClassField(Class<? extends Object> clazz, List<Field> allFields) {
		if (clazz.getName().equals(Object.class.getName())) {
			return allFields;
		}
		Field fields[] = clazz.getDeclaredFields();

		for (Field f : fields) {
			if (!allFields.contains(f)) {
				allFields.add(f);
			}
		}
		return getClassField(clazz.getSuperclass(), allFields);
	}

	/**
	 * 获得对象属性的值
	 */
	private static Object invokeGetMethod(Object owner, String methodName, Object[] args) throws Exception {
		Class<? extends Object> ownerClass = owner.getClass();
		methodName = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
		Method method = null;
		try {
			method = ownerClass.getMethod("get" + methodName);
		} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
			return " can't find 'get" + methodName + "' method";
		}
		return method.invoke(owner);
	}

	public static void invokeSetMethod(Object owner, String methodName, Object[] args, Class paramClass)
			throws Exception {
		Class<? extends Object> ownerClass = owner.getClass();
		methodName = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
		Method method = null;
		method = ownerClass.getMethod("set" + methodName, paramClass);
		method.invoke(owner, args);
	}

	public static int invokeOrdinalValue(Object owner) throws Exception {
		Method method = owner.getClass().getMethod("ordinal");
		return (int) method.invoke(owner);
	}

	public static String getObjTableName(Class<?> c) {
		String tableName=null;
		Table table = c.getAnnotation(Table.class);
		if (table != null) {
			tableName=table.name();
		}
		if(!TextUtils.isEmpty(tableName)) {
			return tableName;
		}
		if (TextUtils.isEmpty(tableName)) {
			tableName = "t_" + c.getSimpleName();
		}
		return tableName;
	}

	public static Field getIdentity(Class<?> c) {
		List<Field> allFields = new ArrayList<Field>();
		getClassField(c, allFields);
		for (Field f : allFields) {
			f.setAccessible(true);
			Id autoIncrement = f.getAnnotation(Id.class);
			if (autoIncrement != null && autoIncrement.strategy() == Strategy.IDENTITY) { // 说明存在主键递增字段
				return f;
			}
		}
		return null;
	}

	// 获取枚举字段及枚举对应的class
	public static Map<String, Class<?>> getEnumFields(Object obj) {
		List<Field> allFields = new ArrayList<Field>();
		getClassField(obj.getClass(), allFields);
		Map<String, Class<?>> enumFields = new HashMap<String, Class<?>>();
		for (Field f : allFields) {
			f.setAccessible(true);
			if (f.getClass().isEnum()) {
				enumFields.put(f.getName(), f.getClass());
			}
		}
		return enumFields;
	}
}
