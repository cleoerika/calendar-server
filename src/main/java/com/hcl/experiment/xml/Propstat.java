package com.hcl.experiment.xml;

import javax.xml.bind.annotation.XmlElement;

public class Propstat {

	public Propstat(String status, String title, String cTag) {
		this.ctg = new Ctag(title, cTag);
		this.status = status;
	}

	@XmlElement(name = "status")
	private final String status;
	
	@XmlElement(name = "prop")
	private final Ctag ctg;
	
	
	
}
