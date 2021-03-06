package com.payulatam.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.gigaspaces.annotation.pojo.SpaceClass;


/**
 * The persistent class for the customer database table.
 * 
 */
@Entity
@Table(name="customer")
@SpaceClass(persist=true)
public class Customer extends BaseEntity {
	
	private static final long serialVersionUID = 1L;

	private String address;

	private String name;

	private String phone;

	public Customer() {
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		return "Customer [address=" + address + ", name=" + name + ", phone=" + phone + "]";
	}
	
}