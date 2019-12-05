package com.hcl.experiment.xml;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "employee")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Employee implements Serializable {
 
    private static final long serialVersionUID = 1L;
     
    private Integer id;
    private String firstName;
    private String lastName;
    private Department department;
     
    public Employee() {
        super();
    }
 
    public Employee(int id, String fName, String lName, Department department) {
        super();
        this.id = id;
        this.firstName = fName;
        this.lastName = lName;
        this.department = department;
    }
 
    @Override
    public String toString() {
        return "Employee [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", department="+ department + "]";
    }

	public Integer getId() {
		return id;
	}

	@XmlAttribute
	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	@XmlElement
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	@XmlElement
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Department getDepartment() {
		return department;
	}

	@XmlElement
	public void setDepartment(Department department) {
		this.department = department;
	}
}