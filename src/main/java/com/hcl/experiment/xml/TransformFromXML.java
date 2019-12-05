package com.hcl.experiment.xml;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class TransformFromXML {

	public static void main (String[] args) throws JAXBException {
		InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("testFile.xml");
		JAXBContext jaxbContext = JAXBContext.newInstance(Employee.class);
		
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		Employee cal = (Employee) jaxbUnmarshaller.unmarshal(inputStream);
		System.out.println(cal);
		
	}
}