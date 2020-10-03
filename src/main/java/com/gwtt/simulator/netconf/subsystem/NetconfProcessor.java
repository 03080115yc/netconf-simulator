package com.gwtt.simulator.netconf.subsystem;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.gwtt.simulator.netconf.message.HelloMessageHandler;
import com.gwtt.simulator.netconf.message.MessageHandler;
import com.gwtt.simulator.netconf.model.hello.Capabilities;
import com.gwtt.simulator.netconf.utils.Constants;

import lombok.extern.slf4j.Slf4j;

/**
 * netconf协议处理器，从流中读取并相应请求
 * 
 * @author yangchao
 *
 */
@Slf4j
public class NetconfProcessor implements Runnable {

	
	private NetconfClient client;

	private boolean active = true;
	private List<MessageHandler> handlerList = new ArrayList<>();
	private Capabilities clientCapabilities;

	public NetconfProcessor(NetconfClient client) {
		this.client = client;
	}

	@Override
	public void run() {
		StringBuilder msg = new StringBuilder();
		BufferedReader bufferReader = null;
		try {
			bufferReader = new BufferedReader(new InputStreamReader(client.getInput(), Constants.MESSAGE_CHARSET));
		} catch (UnsupportedEncodingException e1) {
			log.error("generate bufferreader err", e1);
		}

		NetconfMessageState state = NetconfMessageState.NO_MATCHING_PATTERN;
		while (active) {
			try {
				int cInt = bufferReader.read();
				state = state.evaluateChar((char) cInt);
				msg.append((char) cInt);

				if (state == NetconfMessageState.END_PATTERN) {
					log.debug("hanle message....{}", msg);
					String body = msg.toString().replace(Constants.MESSAGE_END_MARK, "");
					handleMessage(body);
					msg.delete(0, msg.length());
				} else if (state == NetconfMessageState.END_CHUNKED_PATTERN) {
					log.debug("hanle message....{}", msg);
					String body =  msg.toString().replaceAll(Constants.MSGLEN_REGEX_PATTERN, "");
					body = body.replaceAll(Constants.CHUNKED_END_REGEX_PATTERN, "");
					handleMessage(body);
					msg.delete(0, msg.length());
				}

			} catch (Exception e) {
				log.error("read line from input err", e);
			}
		}
	}

	public void stop() {
		log.debug("stop netconf processor");
		this.active = false;
	}

	private void handleMessage(String requestMsg) {
		boolean bo = false;
		for (int i = 0; !bo && i < handlerList.size(); i++) {
			MessageHandler handler = handlerList.get(i);
			bo = handler.handle(client, requestMsg);
//			if (bo && handler instanceof HelloMessageHandler) {
//				clientCapabilities = ((HelloMessageHandler) handler).getClientCapabilities();
//			}
		}

		if (!bo) {
			log.error("cant find handler by message: {}", requestMsg);
		}
	}

	public void registerHandler(MessageHandler handler) {
		handlerList.add(handler);
	}

}
