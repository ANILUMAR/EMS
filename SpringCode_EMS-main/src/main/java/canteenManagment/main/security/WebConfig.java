package canteenManagment.main.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {
	
	@Value("${file.upload-dir}")
	private String pathDirectory;
	
	@Value("${file.upload-dir1}")
	private String pathDirectory1;
	
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
        	@Override
        	public void addCorsMappings(CorsRegistry registry) {
        	    registry.addMapping("/**")
        	        .allowedOriginPatterns("*")
        	        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
        	        .allowedHeaders("*")
        	        .allowCredentials(true);
        	}
        	@Override
	        public void addResourceHandlers(ResourceHandlerRegistry registry) {
        		  String fullPath = "file:///" + pathDirectory.replace("\\", "/");
        		  String fullPath1 = "file:///" + pathDirectory1.replace("\\", "/");
        		  System.out.println("==========="+fullPath);
	            registry.addResourceHandler("/attachments/**")
	                    .addResourceLocations(fullPath,fullPath1);
	          
	        }
        	
        	

        };
    }
}
