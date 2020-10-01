package com.gwtt.simulator.netconf.server;

import org.apache.sshd.server.auth.AsyncAuthException;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.auth.password.PasswordChangeRequiredException;
import org.apache.sshd.server.session.ServerSession;

public class NetConfPasswordAuthenticator implements PasswordAuthenticator{

	private static final String DEFAULT_USERNAME = "admin";
	private static final String DEFAULT_PASSWORD = "admin";
	
	private String username = DEFAULT_USERNAME;
	private String password = DEFAULT_PASSWORD;
	
	public NetConfPasswordAuthenticator() {
		
	}
	
	public NetConfPasswordAuthenticator(String username, String password) {
		this.username = username;
		this.password = password;
	}

	@Override
	public boolean authenticate(String username, String password, ServerSession session)
			throws PasswordChangeRequiredException, AsyncAuthException {
//		return this.username.equals(username) && this.password.equals(password);
		return true;
	}

}
