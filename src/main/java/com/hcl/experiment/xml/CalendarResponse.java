package com.hcl.experiment.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = { "propstat", "href"})
public class CalendarResponse {

	public CalendarResponse(String url, String status, String title, String cTag) {
		this.href = url;
		this.propstat = new Propstat(status, title, cTag);
	}

	@XmlElement(name = "href")
	private final String href ;
	
	@XmlElement(name = "propstat")
	private final Propstat propstat;
	
	
}
