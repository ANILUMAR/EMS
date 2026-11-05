package canteenManagment.main.DTO;

import org.springframework.web.multipart.MultipartFile;

public class UserDetailsDTO {
	  private String username;
	    private String email;
	    private String fullName;
	    private String role;
	    private String registerImage;
	    private String address; 
	    private String phoneNumber;
	    
	    
		
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public String getPhoneNumber() {
			return phoneNumber;
		}
		public void setPhoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
		}
		public String getRegisterImage() {
			return registerImage;
		}
		public void setRegisterImage(String registerImage) {
			this.registerImage = registerImage;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getFullName() {
			return fullName;
		}
		public void setFullName(String fullName) {
			this.fullName = fullName;
		}
		
	    
		   public String getRole() {
			return role;
		}
		public void setRole(String role) {
			this.role = role;
		}
		public UserDetailsDTO() {}

		    public UserDetailsDTO(String username, String email, String fullName , String role,String registerImage) {
		        this.username = username;
		        this.email = email;
		        this.fullName = fullName;
		        this.role     =  role;
		        this.registerImage = registerImage;
		    }
		    public UserDetailsDTO(String username, String email, String fullName , String role,String registerImage,String phoneNumber,String address) {
		        this.username = username;
		        this.email = email;
		        this.fullName = fullName;
		        this.role     =  role;
		        this.registerImage = registerImage;
		        this.phoneNumber = phoneNumber;
		        this.address = address;
		    }

}
