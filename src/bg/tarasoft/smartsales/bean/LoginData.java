package bg.tarasoft.smartsales.bean;

public class LoginData {
	private String email, password;

	public LoginData(String string, String string2) {
		this.email = string;
		this.password = string2;
	}

	public LoginData() {
		// TODO Auto-generated constructor stub
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
