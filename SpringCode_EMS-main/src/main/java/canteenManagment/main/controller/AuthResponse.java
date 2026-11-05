package canteenManagment.main.controller;
import canteenManagment.main.UserEntity.UserEntity;


public class AuthResponse {
    private String token;
    final private UserEntity UserEntity;

    public AuthResponse(String token ,UserEntity UserEntity ) {
        this.token = token;
        this.UserEntity = UserEntity;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

	public UserEntity getUserEntity() {
		return UserEntity;
	}
    
}
