package com.gwtt.simulator.netconf;

import java.io.IOException;

import com.gwtt.simulator.netconf.server.NetconfServer;

public class App {

	public static void main(String[] args) throws IOException {
		NetconfServer.createServer("0.0.0.0", 830).start();
	}
}
