/**
 * 
 */
/**
 * @author cleoerikasoriano
 *
 */
@XmlSchema(
	    namespace = "DAV:",
	    elementFormDefault = XmlNsForm.QUALIFIED,
	    xmlns = {
	        @XmlNs(prefix="d", namespaceURI="DAV:"),
	        @XmlNs(prefix="cs", namespaceURI="http://calendarserver.org/ns/")
	    }
	) 
package com.hcl.experiment.xml;
import javax.xml.bind.annotation.*;