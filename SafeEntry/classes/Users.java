package classes;

import java.io.Serializable;

/**
 * Represents the Users of the TraceTogether application. Each object contains
 * id(ObjectId), name(String), NRIC(String), userType(String), password(String),
 * healthStatus(String) of User.
 */
@SuppressWarnings("serial")
public class Users implements Serializable {

	private String name, nric, healthStatus;

	/**
	 * Constructor used for creating in.
	 * 
	 * @param User's name, NRIC & Password. Sets default value of userType &
	 *               healthStatus.
	 */
	public Users(String name, String nric) {
		this.name = name;
		this.nric = nric;
		this.healthStatus = "Healthy";
	}

	public Users() {
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

	public String getHealthStatus() {
		return healthStatus;
	}

	public void setHealthStatus(String healthStatus) {
		this.healthStatus = healthStatus;
	}

}
