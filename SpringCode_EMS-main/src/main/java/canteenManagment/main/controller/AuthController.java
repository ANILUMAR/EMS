package canteenManagment.main.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import canteenManagment.main.UserEntity.Translation;
import canteenManagment.main.UserEntity.UserEntity;
import canteenManagment.main.jwt.JwtUtil;
import canteenManagment.main.security.AuthContext;
import canteenManagment.main.service.SmsService;
import canteenManagment.main.service.UserService;
import canteenManagment.main.userReposerity.TranslationRepository;
import canteenManagment.main.userReposerity.UserRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	 
	private final UserRepository userRepository;
	private final AuthenticationManager authenticationManager;
	private final PasswordEncoder passwordEncoder;
	private final UserService userService;
	private final SmsService smsService;
	private final TranslationRepository translationRepository;
    
    public AuthController(PasswordEncoder passwordEncoder, UserRepository userRepository , AuthenticationManager authenticationManager , UserService userService,SmsService smsService,TranslationRepository translationRepository) {
        this.userRepository = userRepository;
        this.authenticationManager= authenticationManager;
		this.passwordEncoder = passwordEncoder;
		this.userService = userService; 
		this.smsService = smsService;
		this.translationRepository= translationRepository;
    }
    private final JwtUtil jwtUtil = new JwtUtil();
	
    @PostMapping("/register")
    @CrossOrigin
	 public ResponseEntity<Map<String, String>> register(@RequestPart("user") UserEntity user,@RequestPart("file") MultipartFile file,  @RequestParam(defaultValue = "en") String lang) {
        Map<String, String> response = new HashMap<>();
        try {
            // The register page is public, so it may never mint an administrator:
            // only an existing admin can promote an account from the profile screen.
            String requestedRole = user.getRole() == null ? "" : user.getRole().toUpperCase();
            if (!"EMPLOYEE".equals(requestedRole) && !"TESTER".equals(requestedRole)) {
                user.setRole("EMPLOYEE");
            }
            userService.saveUser(user,file);
            List<Translation> translations = translationRepository.findByScreenAndLang("register", lang);
            Map<String, String> translationMap = translations.stream()
                .collect(Collectors.toMap(Translation::getKey, Translation::getValue));
            String body = translationMap.getOrDefault("register.success.sms",  "Hello " + user.getName() + ",\n\nYour registration was successful.\n\nThank you,\nCanteen Management Team");
            smsService.sendSms(user.getPhoneNumber(), body);
            response.put("message", translationMap.getOrDefault("register.success.api", 
                    "User registered and SMS sent successfully"));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", "Registration failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, String>> deleteUser(@RequestParam String email) {
        Map<String, String> response = new HashMap<>();
        try {
            userService.deleteUserByEmail(email);
            response.put("message", "User deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Failed to delete user");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
             SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwtToken = jwtUtil.generateToken(request.getEmail());
            userRepository.findByEmail(request.getEmail());
            UserEntity user = userRepository.findByEmail(request.getEmail()).orElseThrow();
            AuthResponse response = new AuthResponse(jwtToken, user);  
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }   
    
    @GetMapping("/check-cache")
    public void checkUserCache(@RequestParam String email) {
        userService.checkCache(email);
    }
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request, Authentication authentication) {
        // Self-service reset is fine; resetting somebody else's password (and so
        // taking over their account) stays an admin-only operation.
        if (!AuthContext.isAdmin(authentication)
                && !AuthContext.email(authentication).equalsIgnoreCase(String.valueOf(request.getEmail()))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only reset your own password");
        }

        Optional<UserEntity> optionalUser = userRepository.findByEmail(request.getEmail());

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found");
        }

        UserEntity user = optionalUser.get(); 
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.ok("Password has been reset successfully.");
    }
    
    @GetMapping("/registredEmployees")
	public ResponseEntity<List<UserEntity>> getAllEmployees() {
	    return (ResponseEntity<List<UserEntity>>) userService.getAllEmployees();
	}
    
    @GetMapping("/totalEmployees")
	public ResponseEntity<Integer> getAllEmployeesCount() {
    	Integer count = userService.getAllEmployeesCount();
        return ResponseEntity.ok(count);
	}
    
    
    @PutMapping("/users/{id}")
    @CrossOrigin
    public ResponseEntity<Map<String, String>> update(
            @PathVariable Long id,
            @RequestPart("user") UserEntity user,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) 
    {
        Map<String, String> response = new HashMap<>();
        try {
            UserEntity updatedUser = userService.updateUser(id, user, file);
            // Optionally, send SMS notifications:
            response.put("message", "User updated successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Update failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


}