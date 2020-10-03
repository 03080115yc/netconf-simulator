package com.gwtt.simulator.netconf.subsystem;

import java.io.InputStream;
import java.io.OutputStream;

import com.gwtt.simulator.netconf.model.hello.Capabilities;

import lombok.Data;

@Data
public class NetconfClient {

	private InputStream input;
	private OutputStream output;
	private Capabilities capabilities;
	
}
