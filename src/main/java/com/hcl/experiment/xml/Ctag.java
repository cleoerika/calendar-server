package com.hcl.experiment.xml;

import javax.xml.bind.annotation.XmlElement;

public class Ctag {

	public Ctag(String title, String cTag) {
		this.displayname = title;
		this.getctag = cTag;
	}

	@XmlElement
	private final String displayname;
	
	@XmlElement(namespace = "http://calendarserver.org/ns/")
	private final String getctag;
	
}
