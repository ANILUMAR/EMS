package canteenManagment.main.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import canteenManagment.main.DTO.UserDetailsDTO;
import canteenManagment.main.UserEntity.UserEntity;
import canteenManagment.main.security.AuthContext;
import canteenManagment.main.service.UserService;
import canteenManagment.main.userReposerity.UserRepository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;


@RestController
@RequestMapping("/api/UserProfile")

public class UserProfialsController {

	private final UserRepository userRepository;
	private final UserService userService;

	
	public UserProfialsController(UserRepository userRepository ,UserService userService) {
		this.userRepository = userRepository;
		this.userService    = userService;
		
	}
	 @GetMapping("/userdetails")
	    public ResponseEntity<UserDetailsDTO> getUserDetails(@RequestParam String username) {
	        UserDetailsDTO userDetails = userService.getUserDetailsByUsername(username);
	        if (userDetails != null) {
	            return ResponseEntity.ok(userDetails);
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	        }
	    }
	 
	 @GetMapping("/user")
	    public ResponseEntity<UserDetailsDTO> getUserDetails1(@RequestParam String username) {
	        UserDetailsDTO userDetails = userService.getUserDetailsByUsername(username);
	        if (userDetails != null) {
	            return ResponseEntity.ok(userDetails);
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	        }
	    }
	 	
	 @PutMapping("/update")
	    @CrossOrigin
	    public ResponseEntity<Map<String, String>> update(
	    		@RequestPart("details") UserDetailsDTO detailsDTO,
	    		@RequestPart(value = "profilePhoto", required = false) MultipartFile imageFile,
	    		org.springframework.security.core.Authentication authentication
	    		) 
	    {
	        Map<String, String> response = new HashMap<>();
	        try {
	            // Non-admins may only edit their own record and may not change
	            // their role, so a crafted request cannot promote an account.
	            if (!AuthContext.isAdmin(authentication)) {
	                detailsDTO.setEmail(AuthContext.email(authentication));
	                detailsDTO.setRole(null);
	            }
	            UserEntity updatedUser = userService.updateUser1(detailsDTO,imageFile);
	            // Optionally, send SMS notifications:
	            response.put("message", "User updated successfully");
	            return ResponseEntity.ok(response);
	        } catch (Exception e) {
	            response.put("error", "Update failed");
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	        }
	    }

}
