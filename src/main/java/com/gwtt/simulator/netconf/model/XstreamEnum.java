package com.gwtt.simulator.netconf.model;

public interface XstreamEnum {

	public String getValue();

	public static <T extends XstreamEnum> T getEnum(String value, Class<T>  cls) {
		T[] enums = cls.getEnumConstants();
		for (T t : enums) {
			if(t.getValue().equals(value)) {
				return t;
			}
		}
		return null;
	}
}
