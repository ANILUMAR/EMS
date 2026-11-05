package canteenManagment.main.service;


import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import canteenManagment.main.DTO.UserDetailsDTO;
import canteenManagment.main.UserEntity.UserEntity;
import canteenManagment.main.userReposerity.UserRepository;
import io.jsonwebtoken.io.IOException;

@Service
public class UserService {

    private final UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CacheManager cacheManager;
    
    @Value("${file.upload-dir1}")
    private String pathDirectory;

    public UserService(UserRepository userRepository, CacheManager cacheManager) {
        this.userRepository = userRepository;
        this.cacheManager=cacheManager;
    }

    @CachePut(value = "usersByEmail", key = "#result.email")
    public UserEntity saveUser(UserEntity user,MultipartFile imageFile) throws IllegalStateException, java.io.IOException {
    	java.util.Date utilDate = new java.util.Date();
		java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
		 String encryptedPassword = passwordEncoder.encode(user.getPassword());
		 user.setPassword(encryptedPassword);
    	 user.setGeneratedDate(sqlDate);
    	 user.setLowerName(user.getName().toLowerCase());
    	 user.setName(user.getName().toLowerCase());
    	// MultipartFile imageFile=user.getAttachment();
    	 
    	 if (imageFile != null && !imageFile.isEmpty()) {
    	        try {
    	        	
    	        	
    	            File dir = new File(pathDirectory);
    	            if (!dir.exists()) {
    	                dir.mkdirs();
    	            }
    	            
    	            String email = user.getEmail();
    	            String emailPrefix = (email != null && email.contains("@")) ? email.substring(0, email.indexOf('@')) : "user";

    	            // Get original file extension
    	            String originalFilename = imageFile.getOriginalFilename();
    	            String extension = "";
    	            if (originalFilename != null && originalFilename.contains(".")) {
    	                extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
    	            }

    	            // Create new filename: e.g., anilkumar.jpg
    	            //String fileName = emailPrefix + "_" + UUID.randomUUID() + extension;
    	            String fileName = emailPrefix+extension;

    	            // Save the file
    	            File destFile = new File(dir, fileName);
    	            imageFile.transferTo(destFile);

    	            // Save relative path or filename in DB
    	            user.setRegisterImage(fileName); 
    	            
    	            

    	        } catch (IOException e) {
    	            throw new RuntimeException("Failed to save image", e);
    	        }
    	    }
          
    	 
         UserEntity savedUser = userRepository.save(user);
         System.out.println("✅ User saved to database and cache with ID: " + savedUser.getId());
        return savedUser;
    }
    
    @Cacheable(value = "usersByEmail", key = "#email")
    public UserEntity getUserByEmail(String email) {
        System.out.println("🔍 Fetching user from DB for email: " + email);
        return userRepository.findByEmail(email).orElse(null);
    }
    
    @Cacheable(value = "users", key = "#id")
    public UserEntity getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    
    @Transactional
    @CacheEvict(value = "usersByEmail", key = "#email")
    public void deleteUserByEmail(String email) {
        userRepository.deleteByEmail(email);
        System.out.println(" User deleted from DB and cache for email: " + email);
    }
    
    
    public void checkCache(String email) {
        Cache cache = (Cache) cacheManager.getCache("usersByEmail");
        if (cache != null) {
            ValueWrapper wrapper = cache.get(email);
            if (wrapper != null) {
                System.out.println("✅ Found in cache: " + wrapper.get());
            } else {
                System.out.println("❌ Not found in cache.");
            }
        }
    }
    
    public ResponseEntity<List<UserEntity>> getAllEmployees() {
	    List<UserEntity> genders = userRepository.findAll();
	    return ResponseEntity.ok(genders);
	}
    
    public UserDetailsDTO getUserDetailsByUsername(String username) {
        Optional<UserEntity> userOpt = userRepository.findByEmail(username);
        if (userOpt.isPresent()) {
        	UserEntity user = userOpt.get();
            return new UserDetailsDTO(
                user.getName(),
                user.getEmail(),
                user.getName(),
                user.getRole(),
                user.getRegisterImage(),
                user.getPhoneNumber(),
                user.getAddress()
            );
        }
        return null;
    }
    
    public Integer getAllEmployeesCount()
    {
    	return userRepository.getAllEmployeesCount();
    }
    
    
    @CachePut(value = "usersByEmail", key = "#result.email")
    public UserEntity updateUser(
            Long userId,
            UserEntity userUpdates,
            MultipartFile imageFile
    ) throws IllegalStateException, IOException, java.io.IOException {
    	
        UserEntity existingUser = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Update fields as needed:
        if (userUpdates.getName() != null) {
            existingUser.setName(userUpdates.getName().toLowerCase());
            existingUser.setLowerName(userUpdates.getName().toLowerCase());
        }

        if (userUpdates.getRole() != null) {
            existingUser.setRole(userUpdates.getRole());
        }

        if (userUpdates.getAddress() != null) {
            existingUser.setAddress(userUpdates.getAddress());
        }

        if (userUpdates.getPhoneNumber() != null) {
            existingUser.setPhoneNumber(userUpdates.getPhoneNumber());
        }

        
        if (userUpdates.getPassword() != null && !userUpdates.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userUpdates.getPassword()));
        }

        // Handle image file
        if (imageFile != null && !imageFile.isEmpty()) {
            File dir = new File(pathDirectory);
            if (!dir.exists()) dir.mkdirs();

            String email = existingUser.getEmail();
            String emailPrefix = email != null && email.contains("@")
                ? email.substring(0, email.indexOf('@'))
                : "user";
            String originalFilename = imageFile.getOriginalFilename();
            String extension = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf('.'))
                : "";

            String fileName = emailPrefix + extension;
            File destFile = new File(dir, fileName);
            imageFile.transferTo(destFile);
            existingUser.setRegisterImage(fileName);
        }

        UserEntity updatedUser = userRepository.save(existingUser);
        System.out.println("✅ User updated: ID " + updatedUser.getId());
        return updatedUser;
    }
    
    @CachePut(value = "usersByEmail", key = "#result.email")
    public UserEntity updateUser1(UserDetailsDTO userUpdates,MultipartFile imageFile) throws IllegalStateException, IOException, java.io.IOException {
    	
        UserEntity existingUser = userRepository.findByEmail(userUpdates.getEmail())
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Update fields as needed:
        if (userUpdates.getFullName() != null) {
            existingUser.setName(userUpdates.getFullName().toLowerCase());
            existingUser.setLowerName(userUpdates.getFullName().toLowerCase());
        }

        if (userUpdates.getRole() != null) {
            existingUser.setRole(userUpdates.getRole());
        }

        if (userUpdates.getAddress() != null) {
            existingUser.setAddress(userUpdates.getAddress());
        }

        if (userUpdates.getPhoneNumber() != null) {
            existingUser.setPhoneNumber(userUpdates.getPhoneNumber());
        }

        
        /*if (userUpdates.getPassword() != null && !userUpdates.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userUpdates.getPassword()));
        }*/

        // Handle image file
        if (imageFile != null && !imageFile.isEmpty()) {
            File dir = new File(pathDirectory);
            if (!dir.exists()) dir.mkdirs();

            String email = existingUser.getEmail();
            String emailPrefix = email != null && email.contains("@")
                ? email.substring(0, email.indexOf('@'))
                : "user";
            String originalFilename = imageFile.getOriginalFilename();
            String extension = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf('.'))
                : "";

            String fileName = emailPrefix + extension;
            File destFile = new File(dir, fileName);
            imageFile.transferTo(destFile);
            existingUser.setRegisterImage(fileName);
        }

        UserEntity updatedUser = userRepository.save(existingUser);
        System.out.println("✅ User updated: ID " + updatedUser.getId());
        return updatedUser;
    }
}
    
   
