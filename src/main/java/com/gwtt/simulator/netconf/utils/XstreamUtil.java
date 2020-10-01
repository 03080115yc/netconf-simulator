package com.gwtt.simulator.netconf.utils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

public class XstreamUtil {

	public static String toXML(Object obj) throws XstreamException {
		try {
			XStream xstream = new XStream();
			xstream.autodetectAnnotations(true);
			return xstream.toXML(obj);
		} catch (Exception ex) {
			throw new XstreamException(ex);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T fromXML(String xml, Class<T> cls) throws XstreamException {
		try {
			XStream xstream = new XStream();
			// 解决Security framework of XStream not initialized, XStream is probably
			// vulnerable.
			XStream.setupDefaultSecurity(xstream);
			xstream.allowTypesByRegExp(new String[]{".*"});
			xstream.processAnnotations(cls);
			xstream.ignoreUnknownElements();

			return (T) xstream.fromXML(xml);
		} catch (Exception ex) {
			throw new XstreamException(ex);
		}
	}

}
