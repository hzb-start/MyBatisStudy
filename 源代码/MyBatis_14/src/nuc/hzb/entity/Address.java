package nuc.hzb.entity;

import java.io.Serializable;

public class Address implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String homeAddress;
	private String schoolAddress;
	
	public Address() {}
	
	public String getHomeAddress() {
		return homeAddress;
	}
	
	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}
	
	public String getSchoolAddress() {
		return schoolAddress;
	}
	
	public void setSchoolAddress(String schoolAddress) {
		this.schoolAddress = schoolAddress;
	}

	@Override
	public String toString() {
		return "Address [homeAddress=" + homeAddress + ", schoolAddress=" + schoolAddress + "]";
	}
	
}
