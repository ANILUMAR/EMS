package canteenManagment.main.controller;

import java.util.List; // ✅ Import this
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.criteria.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import canteenManagment.main.DTO.TaskDeleteRequest;
import canteenManagment.main.DTO.assignTaskDTO;
import canteenManagment.main.UserEntity.AssignTask;
import canteenManagment.main.UserEntity.MasterProjectTbl;
import canteenManagment.main.UserEntity.UserEntity;
import canteenManagment.main.UserEntity.UserIdName;
import canteenManagment.main.service.assingTaskService; 
import canteenManagment.main.security.AuthContext;
import canteenManagment.main.userReposerity.MasterAssignmentRepository; 
import canteenManagment.main.userReposerity.UserRepository;
import canteenManagment.main.userReposerity.MasterProjectRepository;
import canteenManagment.main.userReposerity.assignTaskRepository;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;




@RestController
@RequestMapping("/api/assigntasks")
public class AssignTaskController {
	  private final assingTaskService assingTaskService;
	  private final MasterAssignmentRepository  MasterAssignmentRepository ;
	  private final UserRepository UserRepository; 
	  private final MasterProjectRepository MasterProjectRepository;
	  private final assignTaskRepository assignTaskRepository;
	  

	    @Autowired
	    public AssignTaskController(assingTaskService assingTaskService,MasterAssignmentRepository  MasterAssignmentRepository,UserRepository UserRepository, MasterProjectRepository MasterProjectRepository,assignTaskRepository assignTaskRepository) {
	        this.assingTaskService = assingTaskService;
			this.MasterAssignmentRepository = MasterAssignmentRepository;
			this.UserRepository =UserRepository;
			this.MasterProjectRepository = MasterProjectRepository;
			this.assignTaskRepository= assignTaskRepository;
	    }

		@PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
		public ResponseEntity<?> createTask(@ModelAttribute assignTaskDTO assignTaskDTO) {
			try {
				AssignTask savedTask = assingTaskService.saveTask(assignTaskDTO);
				return ResponseEntity.status(HttpStatus.CREATED).body(savedTask);
			} catch (Exception e) {
				e.printStackTrace();
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Task creation failed.");
			}
		}
		@GetMapping("/getAssignTask")
		public List<AssignTask> getAssignTaskList(@RequestParam(required = false) String email,
				org.springframework.security.core.Authentication authentication) {
		    // An admin may look up any teammate's board; everyone else is always
		    // pinned to their own email regardless of what the request asks for.
		    boolean admin = AuthContext.isAdmin(authentication);
		    boolean explicitEmail = email != null && !email.isEmpty() && !email.equalsIgnoreCase("null");
		    String lookup = (admin && explicitEmail) ? email : AuthContext.email(authentication);

		    UserEntity user = UserRepository.findByEmail(lookup)
		        .orElseThrow(() -> new RuntimeException("User not found with email: " + lookup));

		    List<AssignTask> tasks = user.getRole() != null && user.getRole().equalsIgnoreCase("ADMIN")
		        	? assignTaskRepository.findAll()
		        	: assignTaskRepository.findByAssignedTo_Id(user.getId());

		    return tasks.stream().map(task -> new AssignTask(
		            task.getId(),
		            task.getProject(),
		            task.getTaskTitle(),
		            null,
		            task.getStartDate(),
		            task.getEndDate(),
		            task.getPriority(),
		            task.getModule(),
		            task.getSubModule(),
		            task.getScreen(),
		            task.getDescription(),
		            task.getAttachmentPath(),
		            task.getAssignedTo() != null ? task.getAssignedTo().getName() : null
		    )).collect(Collectors.toList());
		}


		
		/*@GetMapping("/attachments/{filename:.+}")
		public ResponseEntity<Resource> getImage(@PathVariable String filename) throws IOException {
		    Path path = (Path) Paths.get("C:/Users/anilkumar.s/Documents/canteenManagment/CanteenImages/").resolve(filename).normalize();
		    Resource resource = new UrlResource(((java.nio.file.Path) path).toUri());

		    if (((Resource) resource).exists()) {
		        return ResponseEntity.ok()
		            .contentType(MediaType.IMAGE_PNG) 
		            .body(resource);
		    } else {
		        return ResponseEntity.notFound().build();
		    }
		}*/
		@GetMapping("/getprocessDescription")
		public List<String> getprocessDescription() {
			return MasterAssignmentRepository.findAllDistinctprocessDescription();
		}

		@GetMapping("/getModuleDescription")
		public List<String> getAllDistinctModuleDescriptions() {
			return MasterAssignmentRepository.findAllDistinctModuleDescriptions();
		}

		@GetMapping("/getcreateDesc")
		public List<String> getcreateDesc() {
			return MasterAssignmentRepository.findAllDistinctcreateDesc();
		}

		/*@GetMapping("/getname")
		public List<String> getname() {
			return UserRepository.findAllDistinctname();
		}*/
		
		@GetMapping("/getname")
		public List<UserIdName> getname() {
		    return UserRepository.findAllDistinctIdAndName();
		}

		@GetMapping("/getAllProjects")
		public List<MasterProjectTbl> getAllProjects() {
			return MasterProjectRepository.findAll();
		}
		
		@GetMapping("/totalProjects")
		public ResponseEntity<Integer> getAllProjectsCount() {
			Integer count = MasterProjectRepository.getAllProjectsCount();
	        return ResponseEntity.ok(count);
			
		}
		
		@PostMapping("/delete")
		public ResponseEntity<String> deleteTaskWithReason(@RequestBody TaskDeleteRequest request) {
		    boolean deleted = assingTaskService.deleteTaskWithReason(request.getTaskId(), request.getReason(), request.getDeletedBy());
		    if (deleted) {
		        return ResponseEntity.ok("Task deleted successfully.");
		    } else {
		        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found.");
		    }
		}

		
		

		
	}
