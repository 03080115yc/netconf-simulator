import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.alibaba.fastjson.JSON;
import com.gwtt.simulator.netconf.utils.XmlNode;

public class XpathTest {

	public static void main(String[] args) throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(false);
		DocumentBuilder db = dbf.newDocumentBuilder();
		InputStream input = XpathTest.class.getClassLoader().getResourceAsStream("example.xml");
		Document doc = db.parse(input);

		// 创建XPath对象
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();

		TransformerFactory tff = TransformerFactory.newInstance();
		Transformer tf = tff.newTransformer();

		// Node node = (Node) xpath.evaluate("/components", doc, XPathConstants.NODE);
		// tf.transform(new DOMSource(node), new StreamResult(out));
		// System.out.println(out.toString());
		//
		// System.out.println("=============================================");
		// NodeList nodeList = (NodeList) xpath.evaluate("/components/component", doc,
		// XPathConstants.NODESET);
		// for (int i = 0; i < nodeList.getLength(); i++) {
		// Node nodeItem = nodeList.item(i);
		// tf.transform(new DOMSource(nodeItem), new StreamResult(out));
		// System.out.println(out.toString());
		// }

		String xml = "<components xmlns=\"http://openconfig.net/yang/platform\">\r\n" + "  <component>\r\n"
				+ "      <name>FAN-1-X</name>\r\n" + "    <state/>\r\n" + "  </component>\r\n" + "</components>";
		InputStream byteInput = new ByteArrayInputStream(xml.getBytes());
		doc = db.parse(byteInput);
		XmlNode xmlNode = generateXmlNode(doc);
		System.out.println(JSON.toJSONString(xmlNode));
		List<String> list = generateXpath(xmlNode);
		for (String path : list) {
			System.out.println("path---------" + path);
			NodeList nodeList = (NodeList) xpath.evaluate(path, doc, XPathConstants.NODESET);
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node nodeItem = nodeList.item(i);
				StringWriter out = new StringWriter();
				tf.transform(new DOMSource(nodeItem), new StreamResult(out));
				System.out.println("path---------" + path + "======");
				System.out.println(out.toString());
				out.flush();
			}
		}

		// NodeList nodeList = (NodeList) xpath.evaluate("/components/component", doc,
		// XPathConstants.NODESET);
		// Node nodeItem = nodeList.item(0);
		// tf.transform(new DOMSource(nodeItem), new StreamResult(out));
		// System.out.println(out.toString());

		// for (int i = 0; i < nodeList.getLength(); i++) {
		// Node nodeItem = nodeList.item(i);
		// tf.transform(new DOMSource(nodeItem), new StreamResult(out));
		//
		// System.out.println(out.toString());
		// }
	}

	/**
	 * 将Dom对象转为自定义的XmlNode,目的是简化结构
	 * 
	 * @param node
	 * @return
	 */
	public static XmlNode generateXmlNode(Node node) {
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
	 * @param node
	 * @return
	 */
	public static List<String> generateXpath(XmlNode node) {
		List<String> result = new ArrayList<>();
		String nodeName = node.getName();
		nodeName = nodeName == null ? "" : nodeName;

		if (node.isLeaf()) {
				result.add(nodeName );
		} else {
			List<String> childPathList = new ArrayList<>();
			StringBuilder childCriteria = new StringBuilder();
			for (XmlNode child : node.getChildList()) {
				if(child.isCriteria()) {
					if(childCriteria.length() != 0) {
						childCriteria.append(" and ");
					}
					
					childCriteria.append(child.getName());
					childCriteria.append("=\"");
					childCriteria.append(child.getText());
					childCriteria.append("\"");
				}else {
					childPathList.addAll(generateXpath(child));
				}
			}
			if(childCriteria.length() > 0) {
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
