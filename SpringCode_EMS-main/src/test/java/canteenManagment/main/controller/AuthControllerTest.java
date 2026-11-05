package canteenManagment.main.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import canteenManagment.main.UserEntity.UserEntity;
import canteenManagment.main.jwt.JwtUtil;
import canteenManagment.main.service.SmsService;
import canteenManagment.main.service.UserService;
import canteenManagment.main.userReposerity.UserRepository;

public class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private  UserService userService; 

    @Mock
    private SmsService smsService;

    @Mock
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authController = new AuthController(passwordEncoder, userRepository, authenticationManager, userService, smsService, null);
    }

    @Test
    public void testRegisterUser_Success() {
        UserEntity user = new UserEntity();
        user.setName("john");
        user.setPassword("password");

        when(userRepository.findByName("john")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("hashedPassword");

        ResponseEntity<?> response = authController.register(user, null, null);

        assertEquals(200, response.getStatusCodeValue());
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    public void testRegisterUser_AlreadyExists() {
        UserEntity user = new UserEntity();
        user.setName("john");

        when(userRepository.findByName("john")).thenReturn(Optional.of(new UserEntity()));

        ResponseEntity<?> response = authController.register(user, null, null);

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void testLogin_Success() {
        LoginRequest request = new LoginRequest("test@example.com", "password");

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        ResponseEntity<?> response = authController.login(request);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void testLogin_Failure() {
        LoginRequest request = new LoginRequest("wrong@example.com", "wrongpassword");

        when(authenticationManager.authenticate(any()))
            .thenThrow(new BadCredentialsException("Invalid credentials"));

        ResponseEntity<?> response = authController.login(request);
        assertEquals(401, response.getStatusCodeValue());
    }
}