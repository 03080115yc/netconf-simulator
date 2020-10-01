package com.gwtt.simulator.netconf.subsystem;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;
import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.command.Command;

import com.gwtt.simulator.netconf.message.HelloMessageHandler;
import com.gwtt.simulator.netconf.message.RpcMessageHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * netconf子系统
 * 
 * @author yangchao
 *
 */
@Slf4j
public class NetconfCommand implements Command {

	private InputStream in;
	private OutputStream out;
	private OutputStream err;
	private NetconfProcessor processor;

	@Override
	public void start(ChannelSession channel, Environment env) throws IOException {
		log.debug("start command and session is {}, env is {}", channel, env);
		processor = new NetconfProcessor(in, out, err);
		processor.registerHandler(new HelloMessageHandler(out));
		processor.registerHandler(new RpcMessageHandler(out));
		
		new Thread(processor, "Netconf processor").start();
	}

	@Override
	public void destroy(ChannelSession channel) throws Exception {
		log.debug("destroy command and session is {}", channel);
		processor.stop();
	}

	@Override
	public void setInputStream(InputStream in) {
		log.debug("set command input stream");
		this.in = in;
	}

	@Override
	public void setOutputStream(OutputStream out) {
		log.debug("set command output stream");
		this.out = out;
	}

	@Override
	public void setErrorStream(OutputStream err) {
		log.debug("set command err stream");
		this.err = err;
	}

	@Override
	public void setExitCallback(ExitCallback callback) {
		// TODO Auto-generated method stub

	}

}
