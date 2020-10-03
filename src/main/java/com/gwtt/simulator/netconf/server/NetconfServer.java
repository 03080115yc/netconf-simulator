package com.gwtt.simulator.netconf.server;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import org.apache.sshd.server.ServerFactoryManager;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;

import com.gwtt.simulator.netconf.message.NotificationSender;
import com.gwtt.simulator.netconf.subsystem.NetconfSubsystemFacotry;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NetconfServer {

	private SshServer sshServer;

	public static NetconfServer createServer(String host, int port) {
		NetconfServer server = new NetconfServer();
		server.initNetconfServer(host, port);

		return server;
	}

	private void initNetconfServer(String host, int port) {
		log.info("start init netconf server");

		sshServer = SshServer.setUpDefaultServer();
		sshServer.setHost(host);
		sshServer.setPort(port);

		sshServer.getProperties().put(ServerFactoryManager.COMMAND_EXIT_TIMEOUT, TimeUnit.SECONDS.toMillis(10L));

		// 认证
		sshServer.setPasswordAuthenticator(new NetConfPasswordAuthenticator());
		// 秘钥生成
		sshServer.setKeyPairProvider(new SimpleGeneratorHostKeyProvider());
		// 设置netconf子系统
		sshServer.setSubsystemFactories(Collections.singletonList(new NetconfSubsystemFacotry()));

		log.debug("init netconf server over");
	}

	public void start() throws IOException {
		sshServer.start();
		NotificationSender.start();
	}

}
