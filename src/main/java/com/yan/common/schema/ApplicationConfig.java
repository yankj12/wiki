package com.yan.common.schema;

import java.io.Serializable;

public class ApplicationConfig implements Serializable{

	private static final long serialVersionUID = 1L;

	private String syscode;
	
	private String cdn;

	public String getSyscode() {
		return syscode;
	}

	public void setSyscode(String syscode) {
		this.syscode = syscode;
	}

	public String getCdn() {
		return cdn;
	}

	public void setCdn(String cdn) {
		this.cdn = cdn;
	}
	
}
