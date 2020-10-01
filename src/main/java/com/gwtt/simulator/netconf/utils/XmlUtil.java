package com.gwtt.simulator.netconf.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlUtil {

	public static Document getDocumentByXml(String xml) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource input = new InputSource(new StringReader(xml));
		return builder.parse(input);
	}
	
	public static Document getDocumentByResource(InputStream input) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder.parse(input);
	}
	
	public static String getXmlByDocument(Document doc) throws TransformerException {
		TransformerFactory tff = TransformerFactory.newInstance();
		Transformer tf = tff.newTransformer();
		StringWriter out = new StringWriter();	
		tf.transform(new DOMSource(doc), new StreamResult(out));
		
		String result = out.toString();
		//暂时通过字符串匹配删除xml头
		String match  = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>";
		if(result.startsWith(match)) {
			result = result.substring(match.length());
		}
		
		return result;
	}


}
