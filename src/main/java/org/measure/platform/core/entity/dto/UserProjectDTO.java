package org.measure.platform.core.entity.dto;

public class UserProjectDTO {
	
	private String id; 
	private String login;
	private String first_name; 
	private String last_name; 
	private String email;
	private String role;
	
	public UserProjectDTO(String id, String login, String first_name, String last_name, String email, String role) {
		this.id = id;
		this.login = login;
		this.first_name = first_name;
		this.last_name = last_name;
		this.email = email;
		this.role = role;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
}
