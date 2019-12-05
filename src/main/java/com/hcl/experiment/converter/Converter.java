package com.hcl.experiment.converter;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class Converter {
	public static void main(String[] args) {

		/* XML to object */
		File file = new File(
				"/Users/cleoerikasoriano/Documents/calendar-server/src/main/java/com/hcl/experiment/converter/NewFile.xml");

		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Customer.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			Customer customer = (Customer) jaxbUnmarshaller.unmarshal(file);
			System.out.println(customer);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/* object to XML */
//		Customer customer = new Customer();
//		customer.setId(100);
//		customer.setName("mkyong");
//		customer.setAge(29);
//
//		try {
//
//			JAXBContext jaxbContext = JAXBContext.newInstance(Customer.class);
//			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
//
//			// output pretty printed
//			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//
//			jaxbMarshaller.marshal(customer, System.out);
//
//		} catch (JAXBException e) {
//			e.printStackTrace();
//		}

	}
}
