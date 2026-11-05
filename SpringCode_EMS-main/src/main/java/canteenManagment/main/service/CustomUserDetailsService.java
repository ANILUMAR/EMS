package canteenManagment.main.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import canteenManagment.main.UserEntity.UserEntity;
import canteenManagment.main.userReposerity.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    UserService userService;

    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
	
    		UserEntity user ;
    	 	user = userService.getUserByEmail(name);
    	 	if( user == null) {
    	 	user = ((Optional<UserEntity>) userRepository.findByEmail(name)).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    	 	}
        // The role drives every access rule in SecurityConfig and in the
        // controllers, so it has to become a granted authority.
        String role = (user.getRole() == null || user.getRole().isEmpty())
                ? "EMPLOYEE"
                : user.getRole().toUpperCase();
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }
    
    
   
}