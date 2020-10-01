package com.gwtt.simulator.netconf.message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.gwtt.simulator.netconf.model.hello.Capabilities;
import com.gwtt.simulator.netconf.model.rpc.Rpc;
import com.gwtt.simulator.netconf.model.rpc.RpcData;
import com.gwtt.simulator.netconf.model.rpc.RpcReply;
import com.gwtt.simulator.netconf.utils.XmlUtil;
import com.gwtt.simulator.netconf.utils.XpathUtil;
import com.gwtt.simulator.netconf.utils.XstreamException;
import com.gwtt.simulator.netconf.utils.XstreamUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RpcMessageHandler extends AbstractMessageHandler implements MessageHandler {

	private OutputStream output;

	public RpcMessageHandler(OutputStream output) {
		this.output = output;
	}

	@Override
	public boolean handle(Capabilities capabilities, String requestMsg) {
		boolean bo = false;
		try {
			Rpc rpc = XstreamUtil.fromXML(requestMsg, Rpc.class);
			if (rpc != null) {
				log.debug("this rpc is {}", rpc);

				String filterType = rpc.getGet().getFilter().getType();
				if ("subtree".equals(filterType) || filterType == null) {
					filterBySubtree(capabilities, rpc.getMessageId(), requestMsg);
				}
				bo = true;
			}
		} catch (XstreamException e) {
			log.debug("check messsage is not rpc");
		}
		return bo;
	}

	/**
	 * 入参为rpc整个xml
	 * 
	 * @param requestMsg
	 */
	private void filterBySubtree(Capabilities capabilities, Integer messageId, String requestMsg) {
		try {
			Document doc = XmlUtil.getDocumentByXml(requestMsg);
			Node filterNode = (Node) XpathUtil.evaluate("/rpc/get/filter/*", doc, XPathConstants.NODE);

			Document resultDoc = null;
			if (filterNode != null) {
				log.debug(filterNode.toString());
				NodeList filterResult = filterByFilterNode(filterNode);
				resultDoc = findDocument(filterResult);
			}
			sendGetReplay(capabilities, messageId, resultDoc);
		} catch (Exception e) {
			log.error("filter sub tree err", e);
		}
	}

	private NodeList filterByFilterNode(Node filterNode) throws Exception {
		String xpathExp = XpathUtil.generateXpathExpression(filterNode);
		log.debug("filter by xpath and the path is {}", xpathExp);

		InputStream input = this.getClass().getClassLoader().getResourceAsStream("example.xml");
		Document doc = XmlUtil.getDocumentByResource(input);

		return (NodeList) XpathUtil.evaluate(xpathExp, doc, XPathConstants.NODESET);
	}

	private Document findDocument(NodeList nodeList) {
		Node firstNode = nodeList.item(0);
		Node parentNode = firstNode.getParentNode();

		while (parentNode.hasChildNodes()) {
			Node child = parentNode.getFirstChild();
			parentNode.removeChild(child);
		}

		for (int i = 0; i < nodeList.getLength(); i++) {
			parentNode.appendChild(nodeList.item(i));
		}
		Document doc = findDocument(parentNode);

		return doc;
	}

	private Document findDocument(Node node) {
		Node parentNode = node.getParentNode();

		while (parentNode.hasChildNodes()) {
			Node child = parentNode.getFirstChild();
			parentNode.removeChild(child);
		}
		parentNode.appendChild(node);

		Document doc;
		if (parentNode.getNodeType() != Node.DOCUMENT_NODE) {
			doc = findDocument(parentNode);
		} else {
			doc = (Document) parentNode;
		}
		return doc;
	}

	private void sendGetReplay(Capabilities capabilities, Integer messageId, Document doc) {
		try {
			RpcReply reply = new RpcReply();
			reply.setData(new RpcData());
			reply.setMessageId(messageId);
			String replyXml = XstreamUtil.toXML(reply);

			if (doc != null) {
				Document replyDoc = XmlUtil.getDocumentByXml(replyXml);
				Node dataNode = (Node) XpathUtil.evaluate("/rpc-reply/data", replyDoc, XPathConstants.NODE);

				NodeList nodes = doc.getChildNodes();
				for (int i = 0; i < nodes.getLength(); i++) {
					Node node = nodes.item(i);
					node = replyDoc.importNode(node, true);
					dataNode.appendChild(node);
				}

				replyXml = XmlUtil.getXmlByDocument(replyDoc);
			}
			writeReply(output, capabilities, replyXml);
		} catch (XstreamException | TransformerException | XPathExpressionException | ParserConfigurationException
				| SAXException | IOException e) {
			log.error("send rpc get reply err", e);
		}
	}

}
