package canteenManagment.main.security;

import java.util.List;

import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


	@Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

     private final JwtFilter jwtFilter;

     private final    AuthenticationEntryPoint unauthorizedHandler;

    public SecurityConfig(JwtFilter jwtFilter, AuthenticationEntryPoint unauthorizedHandler) {
        this.jwtFilter = jwtFilter;
        this.unauthorizedHandler = unauthorizedHandler;
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        AuthenticationEntryPoint customEntryPoint = null;
        http
        .csrf().disable()
        .cors() // 👈 Enable CORS
        .and()
            .authorizeRequests(auth -> auth
                // Public
                .antMatchers("/api/auth/register", "/api/auth/login", "/api/translation", "/attachments/**").permitAll()
                // Account administration is restricted to the admin role
                .antMatchers("/api/auth/registredEmployees", "/api/auth/delete").hasRole("ADMIN")
                // Only admins create work and read the assignee picker
                .antMatchers(HttpMethod.POST, "/api/tasks").hasRole("ADMIN")
                .antMatchers(
                        "/api/assigntasks/create",
                        "/api/assigntasks/getname",
                        "/api/assigntasks/getAllProjects",
                        "/api/assigntasks/getModuleDescription",
                        "/api/assigntasks/getprocessDescription",
                        "/api/assigntasks/getcreateDesc",
                        "/api/assigntasks/totalProjects"
                ).hasRole("ADMIN")
                // Everything else: any signed-in role, data scoped inside the controllers
                .anyRequest().authenticated()
            )
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(customEntryPoint)  // Return 401
            )
            .sessionManagement(sess -> sess
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // No server session
            );

       
		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    } 
    
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("usersByEmail");
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    
}