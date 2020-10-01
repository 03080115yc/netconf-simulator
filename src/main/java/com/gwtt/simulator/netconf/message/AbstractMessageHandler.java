package com.gwtt.simulator.netconf.message;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import com.gwtt.simulator.netconf.model.hello.Capabilities;
import com.gwtt.simulator.netconf.utils.Constants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractMessageHandler implements MessageHandler {

	protected void writeReply(OutputStream output, Capabilities capabilities, String body) {
		try {
			String message = formatMessageHeader(body);
			message = formatMessageChunked(message, capabilities);
			output.write(message.getBytes(Constants.MESSAGE_CHARSET));
			output.flush();
			log.debug("send reply \n{}", message);
		} catch (Exception e) {
			log.error("write reply err the message is {}", body, e);
		}

	}

	/**
	 * 加消息头
	 * 
	 * @param message
	 * @return
	 */
	protected String formatMessageHeader(String message) {
		if (!message.startsWith(MESSAGE_HEADER)) {
			if (message.startsWith(MESSAGE_NEWLINE)) {
				message = MESSAGE_HEADER + message;
			} else {
				message = MESSAGE_HEADER + MESSAGE_NEWLINE + message;
			}
		}
		return message;
	}

	/**
	 * 根据版本确定是否加块
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	protected String formatMessageChunked(String message, Capabilities capabilities)
			throws UnsupportedEncodingException {
		List<String> capabilityList = null;
		if (capabilities != null) {
			capabilityList = capabilities.getCapability();
		}
		if (capabilityList != null && capabilityList.contains(Capabilities.CAPABILITY_BASE_1_1)) {
			message = LF + HASH + message.getBytes(Constants.MESSAGE_CHARSET).length + LF + message + LF + HASH + HASH
					+ LF;
		} else {
			message = message + Constants.MESSAGE_END_MARK;
		}
		return message;
	}

}
