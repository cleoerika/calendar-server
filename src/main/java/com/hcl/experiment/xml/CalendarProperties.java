package com.hcl.experiment.xml;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.core.JsonProcessingException;

@XmlRootElement(name = "multistatus")
public class CalendarProperties {

	public static void main(String[] args) throws JsonProcessingException, JAXBException {
		CalendarProperties c = new CalendarProperties("/calendars/djohndoe", "HTTP/1.1 200 OK", "HOme sweet calend",
				"22333");
		JAXBContext jc = JAXBContext.newInstance(CalendarProperties.class);
		Marshaller m = jc.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
		m.marshal(c, System.out);
	}

	@XmlElement(name = "response")
	private final CalendarResponse cr;

	public CalendarProperties() {
		this.cr = new CalendarResponse(null, null, null, null);
	}

	public CalendarProperties(final String url, final String status, final String title, final String cTag) {
		this.cr = new CalendarResponse(url, status, title, cTag);
	}

}
