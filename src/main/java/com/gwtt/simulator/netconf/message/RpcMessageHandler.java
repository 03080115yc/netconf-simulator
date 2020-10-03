package com.gwtt.simulator.netconf.message;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.gwtt.simulator.netconf.model.rpc.Filter;
import com.gwtt.simulator.netconf.model.rpc.Rpc;
import com.gwtt.simulator.netconf.model.rpc.RpcData;
import com.gwtt.simulator.netconf.model.rpc.RpcReply;
import com.gwtt.simulator.netconf.subsystem.NetconfClient;
import com.gwtt.simulator.netconf.utils.XmlUtil;
import com.gwtt.simulator.netconf.utils.XpathUtil;
import com.gwtt.simulator.netconf.utils.XstreamException;
import com.gwtt.simulator.netconf.utils.XstreamUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RpcMessageHandler extends NetconfWriter implements MessageHandler {

	@Override
	public boolean handle(NetconfClient client, String requestMsg) {
		boolean bo = false;
		try {
			Rpc rpc = XstreamUtil.fromXML(requestMsg, Rpc.class);
			if (rpc != null) {
				log.debug("this rpc is {}", rpc);

				if (rpc.getGet() != null) {
					Filter filter = rpc.getGet().getFilter();
					if (filter == null) {
						filter = new Filter();
					}
					handleGet(client, rpc.getMessageId(), filter.getType(), requestMsg);
					bo = true;
				} else if (rpc.getCloseSession() != null) {
					handleCloseSession(client, rpc.getMessageId());
				} else if (rpc.getCreateSubscription() != null) {
					handleSubscription(client, rpc.getMessageId());
				}

				bo = true;
			}
		} catch (XstreamException e) {
			log.debug("check messsage is not rpc");
		}
		return bo;
	}

	private void handleSubscription(NetconfClient client, Integer messageId) {
		try {
			RpcReply reply = new RpcReply();
			reply.setMessageId(messageId);
			reply.setOk("");
			String replyXml = XstreamUtil.toXML(reply);
			writeMessage(client.getOutput(), client.getCapabilities(), replyXml);

			NotificationSender.subscription(client);
		} catch (XstreamException e) {
			log.error("send close session reply err", e);
		}

	}

	private void handleCloseSession(NetconfClient client, Integer messageId) {
		try {
			RpcReply reply = new RpcReply();
			reply.setMessageId(messageId);
			reply.setOk("");
			String replyXml = XstreamUtil.toXML(reply);
			writeMessage(client.getOutput(), client.getCapabilities(), replyXml);
		} catch (XstreamException e) {
			log.error("send close session reply err", e);
		}

	}

	private void handleGet(NetconfClient client, Integer messageId, String filterType, String requestMsg) {
		if ("subtree".equals(filterType) || filterType == null) {
			filterBySubtree(client, messageId, requestMsg);
		}
	}

	/**
	 * 入参为rpc整个xml
	 * 
	 * @param requestMsg
	 */
	private void filterBySubtree(NetconfClient client, Integer messageId, String requestMsg) {
		try {
			Document requestDoc = XmlUtil.getDocumentByXml(requestMsg);
			Node filterNode = (Node) XpathUtil.evaluate("/rpc/get/filter/*", requestDoc, XPathConstants.NODE);

			InputStream input = this.getClass().getClassLoader().getResourceAsStream("example.xml");
			Document doc = XmlUtil.getDocumentByResource(input);
			Document resultDoc = null;
			if (filterNode != null) {
				String xpathExp = XpathUtil.generateXpathExpression(filterNode);
				log.debug("filter by xpath and the path is {}", xpathExp);

				NodeList filterResult = (NodeList) XpathUtil.evaluate(xpathExp, doc, XPathConstants.NODESET);
				resultDoc = findDocument(filterResult);
			} else {
				resultDoc = doc;
			}
			sendGetReplay(client, messageId, resultDoc);
		} catch (Exception e) {
			log.error("filter sub tree err", e);
		}
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

	private void sendGetReplay(NetconfClient client, Integer messageId, Document doc) {
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
			writeMessage(client.getOutput(), client.getCapabilities(), replyXml);
		} catch (XstreamException | TransformerException | XPathExpressionException | ParserConfigurationException
				| SAXException | IOException e) {
			log.error("send rpc get reply err", e);
		}
	}

}
