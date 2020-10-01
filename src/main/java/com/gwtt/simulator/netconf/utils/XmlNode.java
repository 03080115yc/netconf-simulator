package com.gwtt.simulator.netconf.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class XmlNode {

	private String name;
	private Map<String, String> attribute = new HashMap<>();
	private String text;
	private List<XmlNode> childList = new ArrayList<>();

	public boolean isLeaf() {
		return childList.isEmpty();
	}
	
	public boolean isCriteria() {
		return text != null; 
	}

}
