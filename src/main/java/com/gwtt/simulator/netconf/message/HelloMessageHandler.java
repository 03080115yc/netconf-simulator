package com.gwtt.simulator.netconf.message;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.gwtt.simulator.netconf.model.hello.Capabilities;
import com.gwtt.simulator.netconf.model.hello.Hello;
import com.gwtt.simulator.netconf.subsystem.NetconfClient;
import com.gwtt.simulator.netconf.utils.XstreamException;
import com.gwtt.simulator.netconf.utils.XstreamUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HelloMessageHandler extends NetconfWriter implements MessageHandler {

	@Override
	public boolean handle(NetconfClient client, String requestMsg) {
		boolean bo = false;
		try {
			Hello hello = XstreamUtil.fromXML(requestMsg, Hello.class);
			if (hello != null) {

				client.setCapabilities( hello.getCapabilities());

				Capabilities serverCapabilities = getServerCapabilities();
				hello = new Hello();
				hello.setSessionId(new Random().nextInt(1000));
				hello.setCapabilities(serverCapabilities);
				String reply = XstreamUtil.toXML(hello);

				writeMessage(client.getOutput(), new Capabilities(), reply);
				
				bo = true;
			}
		} catch (XstreamException e) {
			log.debug("check messsage is not hello");
		}
		return bo;
	}


	private Capabilities getServerCapabilities() {
		Capabilities capabilities = new Capabilities();

		List<String> capabilityList = new ArrayList<>();
		capabilityList.add(Capabilities.CAPABILITY_BASE_1_0);
		capabilityList.add(Capabilities.CAPABILITY_BASE_1_1);
		capabilityList.add(Capabilities.CAPABILITY_NOTIFICATION_1_0);

		capabilities.setCapability(capabilityList);
		return capabilities;
	}

}
