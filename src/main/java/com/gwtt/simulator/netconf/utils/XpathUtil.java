package com.gwtt.simulator.netconf.utils;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XpathUtil {

	public static Object evaluate(String expression, Object item, QName returnType) throws XPathExpressionException{
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		
		return xpath.evaluate(expression, item, returnType);
	}
	
	
	public static String generateXpathExpression(Node doc) {
		XmlNode xmlNode = generateXmlNode(doc);
		List<String> exps = generateXpath(xmlNode);
		StringBuilder sb = new StringBuilder();
		for (String exp : exps) {
			if (sb.length() != 0) {
				sb.append("|");
			}
			if(!exp.startsWith("/")) {
				sb.append("/");
			}
			sb.append(exp);
		}
		return sb.toString();
	}

	/**
	 * 将Dom对象转为自定义的XmlNode,目的是简化结构
	 * 
	 * @param node
	 * @return
	 */
	private static XmlNode generateXmlNode(Node node) {
		XmlNode xmlNode = null;

		if (node.getNodeType() == Node.ELEMENT_NODE || node.getNodeType() == Node.DOCUMENT_NODE) {

			xmlNode = new XmlNode();

			NamedNodeMap attrs = node.getAttributes();
			if (attrs != null) {
				for (int i = 0; i < attrs.getLength(); i++) {
					Node attr = attrs.item(i);
					xmlNode.getAttribute().put(attr.getNodeName(), attr.getTextContent());
				}
			}

			if (node.getNodeType() == Node.ELEMENT_NODE) {
				xmlNode.setName(node.getNodeName());
			}

			NodeList childNodes = node.getChildNodes();

			if (childNodes.getLength() == 1) {
				Node child = node.getFirstChild();
				if (child.getNodeType() == Node.TEXT_NODE) {
					xmlNode.setText(child.getTextContent());
				}
			}

			for (int i = 0; i < childNodes.getLength(); i++) {
				Node child = childNodes.item(i);
				XmlNode childXmlNode = generateXmlNode(child);
				if (childXmlNode != null) {
					xmlNode.getChildList().add(childXmlNode);
				}
			}

		}

		return xmlNode;
	}

	/**
	 * 根据XmlNode生成一组Xpath表达式，多条之间为或的关系
	 * 
	 * @param node
	 * @return
	 */
	public static List<String> generateXpath(XmlNode node) {
		List<String> result = new ArrayList<>();
		String nodeName = node.getName();
		nodeName = nodeName == null ? "" : nodeName;

		if (node.isLeaf()) {
			result.add(nodeName);
		} else {
			List<String> childPathList = new ArrayList<>();
			StringBuilder childCriteria = new StringBuilder();
			for (XmlNode child : node.getChildList()) {
				if (child.isCriteria()) {
					if (childCriteria.length() != 0) {
						childCriteria.append(" and ");
					}

					childCriteria.append(child.getName());
					childCriteria.append("=\"");
					childCriteria.append(child.getText());
					childCriteria.append("\"");
				} else {
					childPathList.addAll(generateXpath(child));
				}
			}
			if (childCriteria.length() > 0) {
				childCriteria.insert(0, "[");
				childCriteria.append("]");
				nodeName = nodeName + childCriteria.toString();
			}
			for (String childPath : childPathList) {
				result.add(nodeName + "/" + childPath);
			}
		}

		return result;
	}

}
