package com.gwtt.simulator.netconf.message;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.gwtt.simulator.netconf.model.hello.Capabilities;
import com.gwtt.simulator.netconf.model.hello.Hello;
import com.gwtt.simulator.netconf.utils.XstreamException;
import com.gwtt.simulator.netconf.utils.XstreamUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HelloMessageHandler extends AbstractMessageHandler implements MessageHandler {

	private OutputStream output;
	private Capabilities clientCapabilities;

	public HelloMessageHandler(OutputStream output) {
		this.output = output;
	}

	@Override
	public boolean handle(Capabilities clientCapabilities, String requestMsg) {
		boolean bo = false;
		try {
			Hello hello = XstreamUtil.fromXML(requestMsg, Hello.class);
			if (hello != null) {

				this.clientCapabilities = hello.getCapabilities();

				Capabilities serverCapabilities = getServerCapabilities();
				hello = new Hello();
				hello.setSessionId(new Random().nextInt(1000));
				hello.setCapabilities(serverCapabilities);
				String reply = XstreamUtil.toXML(hello);

				writeReply(output, new Capabilities(), reply);
				
				bo = true;
			}
		} catch (XstreamException e) {
			log.debug("check messsage is not hello");
		}
		return bo;
	}

	public Capabilities getClientCapabilities() {
		return clientCapabilities;
	}

	public Capabilities getServerCapabilities() {
		Capabilities capabilities = new Capabilities();

		List<String> capabilityList = new ArrayList<>();
		capabilityList.add(Capabilities.CAPABILITY_BASE_1_0);
		capabilityList.add(Capabilities.CAPABILITY_BASE_1_1);

		capabilities.setCapability(capabilityList);
		return capabilities;
	}

}
