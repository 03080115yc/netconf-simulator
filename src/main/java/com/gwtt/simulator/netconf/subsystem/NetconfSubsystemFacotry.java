package com.gwtt.simulator.netconf.subsystem;

import java.io.IOException;

import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.subsystem.SubsystemFactory;

public class NetconfSubsystemFacotry implements SubsystemFactory {

	@Override
	public String getName() {
		return "Netconf";
	}

	@Override
	public Command createSubsystem(ChannelSession channel) throws IOException {
		return new NetconfCommand();
	}

}
