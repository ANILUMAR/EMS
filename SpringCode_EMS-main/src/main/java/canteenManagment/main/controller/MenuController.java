package canteenManagment.main.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import canteenManagment.main.UserEntity.MenuItem;
import canteenManagment.main.userReposerity.MenuRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Value;

@RestController
@RequestMapping("/api/menu")
public class MenuController {
	
	 @Value("${file.upload-dir}")
	    private String uploadDir; 
	@Autowired
    MenuRepository menuRepository;


	@GetMapping
	public List<MenuItem> getMenu() {
	    List<MenuItem> menuItems = menuRepository.findAll();
	    for (MenuItem item : menuItems) {
	        if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
                String fullPath = uploadDir + File.separator + new File(item.getImageUrl()).getName();

	            File imageFile = new File(fullPath);
	            if (imageFile.exists()) {
	                try {
	                    byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
	                    item.setImage(imageBytes);
	                } catch (IOException e) {
	                    e.printStackTrace(); 
	                }
	            }
	        }
	    }

	    return menuItems;
	}
    
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addMenuItem(@RequestPart("menuItem") String menuItemJson, @RequestPart("image") MultipartFile imageFile) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            MenuItem menuItem = objectMapper.readValue(menuItemJson, MenuItem.class);
            if (imageFile != null && !imageFile.isEmpty()) {
                String fileName =imageFile.getOriginalFilename();
                File saveFile = new File(uploadDir + File.separator + fileName);
                saveFile.getParentFile().mkdirs(); 
                imageFile.transferTo(saveFile);
                menuItem.setImageUrl("/images/" + fileName);
                menuItem.setImage(null);
            }
            menuRepository.save(menuItem);
            return ResponseEntity.ok("Menu item added successfully");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error saving menu item: " + e.getMessage());
        }
    }
	
    @PutMapping("/{id}")
    public ResponseEntity<MenuItem> updateMenuItem(@PathVariable Long id,  @RequestParam("menuItem") String menuItemJson,  @RequestParam("image") MultipartFile imageFile) {
        Optional<MenuItem> existingItemOpt = menuRepository.findById(id);
        if (existingItemOpt.isPresent()) {
            MenuItem existingItem = existingItemOpt.get();
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                MenuItem updatedMenuItem = objectMapper.readValue(menuItemJson, MenuItem.class);
                existingItem.setName(updatedMenuItem.getName());
                existingItem.setPrice(updatedMenuItem.getPrice());
                existingItem.setCategory(updatedMenuItem.getCategory());
                if (imageFile != null && !imageFile.isEmpty()) {
                    if (existingItem.getImageUrl() != null && !existingItem.getImageUrl().isEmpty()) {
                        File oldImageFile = new File(existingItem.getImageUrl());
                        if (oldImageFile.exists()) {
                            boolean deleted = oldImageFile.delete();
                            if (!deleted) {
                            	return ResponseEntity.notFound().build(); 
                            }
                        }
                    }
                    String pathDirect = uploadDir;
                    String uploadDir = pathDirect;  
                    File directory = new File(uploadDir);
                    if (!directory.exists()) {
                        directory.mkdirs(); 
                    }
                    String fileName =imageFile.getOriginalFilename();  // unique file name
                    File saveFile = new File(uploadDir + File.separator + fileName);  // path to save the file
                    try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(saveFile))) {
                        stream.write(imageFile.getBytes());
                    }
                    String newImageUrl ="/images/"+ fileName;  
                    existingItem.setImageUrl(newImageUrl);  
                }
                menuRepository.save(existingItem);
                return ResponseEntity.ok(existingItem); 

            } catch (IOException e) {
            	return ResponseEntity.notFound().build(); 
            }
        } else {
            return ResponseEntity.notFound().build(); 
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMenuItem(@PathVariable Long id) {
        Optional<MenuItem> menuItemOpt = menuRepository.findById(id);

        if (menuItemOpt.isPresent()) {
            MenuItem menuItem = menuItemOpt.get();
            String imagePath = menuItem.getImageUrl();
            
            if (imagePath != null && !imagePath.isEmpty()) {
                File imageFile = new File(imagePath);
                if (imageFile.exists()) {
                    boolean deleted = imageFile.delete();
                    if (!deleted) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Failed to delete image from path.");
                    }
                }
            }
            menuRepository.deleteById(id);
            return ResponseEntity.ok("Menu item and image deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Menu item not found.");
        }
    }
    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        MenuItem item = menuRepository.findById(id).orElseThrow();
        try {
            if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
                String fullPath = uploadDir + File.separator + new File(item.getImageUrl()).getName();
                File imageFile = new File(fullPath);
                if (imageFile.exists()) {
                    byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
                    String contentType = Files.probeContentType(imageFile.toPath());
                    return ResponseEntity
                            .ok()
                            .contentType(MediaType.parseMediaType(contentType != null ? contentType : "image/jpeg"))
                            .body(imageBytes);
                }
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND) .body(("Image not found for menu item with ID: " + id).getBytes());

        } catch (IOException e) {
            return ResponseEntity.status(500).build();
        }
    }

    
    
    
    
}