package cn.hlj.ems.orm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class PropertyFilter {
	private String propertyName;
	private Object propertyValue;

	public enum MatchType {
		EQ, GT, GE, LT, LE, LIKE
	}

	private MatchType matchType;

	public enum PropertyType {
		I(Integer.class), F(Float.class), S(String.class), D(Date.class);

		@SuppressWarnings("rawtypes")
		private Class propertyType;

		@SuppressWarnings("rawtypes")
		private PropertyType(Class propertyType) {
			this.propertyType = propertyType;
		}

		@SuppressWarnings("rawtypes")
		public Class getPropertyType() {
			return propertyType;
		}
	}

	@SuppressWarnings("rawtypes")
	private Class propertyType;

	@SuppressWarnings("rawtypes")
	public PropertyFilter(String propertyName, Object propertyValue, MatchType matchType, Class propertyType) {
		super();
		this.propertyName = propertyName;
		this.propertyValue = propertyValue;
		this.matchType = matchType;
		this.propertyType = propertyType;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public Object getPropertyValue() {
		return propertyValue;
	}

	public MatchType getMatchType() {
		return matchType;
	}

	@SuppressWarnings("rawtypes")
	public Class getPropertyType() {
		return propertyType;
	}

	// 把请求参数转为 PropertyFilter 的集合
	@SuppressWarnings("rawtypes")
	public static List<PropertyFilter> parseParamsToPropertyFilters(Map<String, Object> params) {
		List<PropertyFilter> filters = new ArrayList<>();

		for (Map.Entry<String, Object> me : params.entrySet()) {
			// value即为propertyValue
			Object propertyValue = me.getValue();
			if (propertyValue == null || "".equals(propertyValue.toString().trim())) {
				continue;
			}

			// 需要解析 key 来得到 propertyName, matchType, propertyName;
			String key = me.getKey(); // EQI_loginName, GTD_birth

			String str1 = StringUtils.substringBefore(key, "_");// EQI,GTD

			String matchTypeStr = StringUtils.substring(str1, 0, str1.length() - 1);// EQ,GT
			MatchType matchType = Enum.valueOf(MatchType.class, matchTypeStr);

			String propertyTypeStr = StringUtils.substring(str1, str1.length() - 1);// I,D
			Class propertyType = Enum.valueOf(PropertyType.class, propertyTypeStr).getPropertyType();

			String propertyName = StringUtils.substringAfter(key, "_");

			PropertyFilter filter = new PropertyFilter(propertyName, propertyValue, matchType, propertyType);
			filters.add(filter);
		}

		return filters;
	}

	@Override
	public String toString() {
		return "PropertyFilter [propertyName=" + propertyName + ", propertyValue=" + propertyValue + ", matchType="
				+ matchType + ", propertyType=" + propertyType + "]";
	}

}
