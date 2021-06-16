package classes;

import java.io.Serializable;

/**
 * Represents the check-in/check-out Transaction Records of User. Each object
 * contains id(ObjectId), name(String), NRIC(String), location(String),
 * checkInTime(String) of User, checkOutTime(String) of User.
 */

public class Transactions implements Serializable {

	private String name, nric, location, type, checkInTime, checkOutTime;

	public Transactions(String name, String nric, String location) {
		this.name = name;
		this.nric = nric;
		this.location = location;
	}

	public Transactions(String nric, String name, String type, String location, String checkOutTime, String checkInTime) {
		this.name = name;
		this.nric = nric;
		this.location = location;
		this.type=type;
		this.checkInTime=checkInTime;
		this.checkOutTime=checkInTime;
	}
	
	public Transactions() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNric() {
		return nric;
	}

	public void setNric(String nric) {
		this.nric = nric;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCheckInTime() {
		return checkInTime;
	}

	public void setCheckInTime(String checkInTime) {
		this.checkInTime = checkInTime;
	}

	public String getCheckOutTime() {
		return checkOutTime;
	}

	public void setCheckOutTime(String checkOutTime) {
		this.checkOutTime = checkOutTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
