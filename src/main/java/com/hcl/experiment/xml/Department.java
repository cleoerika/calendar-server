package com.hcl.experiment.xml;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Department implements Serializable {
     
	private static final long serialVersionUID = 1L;
	
	Integer id;
    String name;
     
    public Department() {
        super();
    }
 
    public Department(Integer id, String name) {
        super();
        this.id = id;
        this.name = name;
    }
     
    @Override
    public String toString() {
        return "Department [id=" + id + ", name=" + name + "]";
    }

	public Integer getId() {
		return id;
	}
	
	@XmlAttribute
	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	@XmlElement
	public void setName(String name) {
		this.name = name;
	}
}
