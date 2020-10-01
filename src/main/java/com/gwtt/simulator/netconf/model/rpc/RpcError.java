package com.gwtt.simulator.netconf.model.rpc;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@XStreamAlias("rpc-error")
public class RpcError {

	/**
	 * 参考{@link ErroType#getValue()}
	 */
	@XStreamAlias("error-type")
	private String errorType;
	
	/**
	 * 标识错误条件
	 */
	@XStreamAlias("error-tag")
	private String errorTag;
	
	@XStreamAlias("error-severity")
	private String errorSeverity;
	
	@XStreamAlias("error-message")
	private String errorMessage;
	
}
