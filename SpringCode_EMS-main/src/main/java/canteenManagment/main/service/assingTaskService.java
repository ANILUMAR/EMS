package canteenManagment.main.service;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import canteenManagment.main.DTO.assignTaskDTO;
import canteenManagment.main.UserEntity.AssignTask;
import canteenManagment.main.UserEntity.DeletedTask;
import canteenManagment.main.UserEntity.UserEntity;
import canteenManagment.main.userReposerity.DeletedTaskRepository;
import canteenManagment.main.userReposerity.UserRepository;
import canteenManagment.main.userReposerity.assignTaskRepository;
import org.springframework.beans.factory.annotation.Value;
import java.io.File;
import java.time.LocalDateTime;




@Service
public class assingTaskService {


@Value("${file.upload-dir}")
private String pathDirectory;

    @Autowired
    private assignTaskRepository assignTaskRepository;
     
    @Autowired
    private DeletedTaskRepository DeletedTaskRepository ;
    
    @Autowired
    private  UserRepository UserRepository ; 
    
    public AssignTask saveTask(assignTaskDTO dto) throws IOException {
        AssignTask task = new AssignTask();
        task.setProject(dto.getProject().toUpperCase());
        if(dto.getTaskTitle()!=null && !dto.getTaskTitle().equals("undefined")) {
        task.setTaskTitle(dto.getTaskTitle().toUpperCase());
        }else {
        	task.setTaskTitle("EMR".toUpperCase());
        }
        String username = dto.getAssignedTo().toUpperCase(); 
        UserEntity user = UserRepository.findById(Long.valueOf(username))
                        .orElseThrow(() -> new RuntimeException("User not found"));

        task.setAssignedTo(user);
        
        task.setStartDate(dto.getStartDate());
        task.setEndDate(dto.getEndDate());
        task.setPriority(dto.getPriority().toUpperCase());
        task.setModule(dto.getModule().toUpperCase());
        task.setSubModule(dto.getSubModule().toUpperCase());
        task.setScreen(dto.getScreen().toUpperCase());
        task.setDescription(dto.getDescription().toUpperCase());
        
        MultipartFile file = dto.getAttachment();
        File uploadPath = new File(pathDirectory);
        if (!uploadPath.exists()) {
            uploadPath.mkdirs(); 
        }
        String originalFilename = file.getOriginalFilename();
        File destFile = new File(uploadPath, originalFilename);
        file.transferTo(destFile);
         task .setAttachmentPath(originalFilename);
         
        return assignTaskRepository.save(task);
    }
    
    public boolean deleteTaskById(Long id) {
        if (assignTaskRepository.existsById(id)) {
        	assignTaskRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
    
    public boolean deleteTaskWithReason(Long taskId, String reason, String deletedBy) {
    	
        Optional<AssignTask> taskOpt = assignTaskRepository.findById(taskId);
        if (taskOpt.isPresent()) {
            AssignTask task = taskOpt.get();
            DeletedTask deleted = new DeletedTask();
            deleted.setTaskId(task.getId());
            deleted.setTaskName(task.getDescription());
            deleted.setReason(reason);
            deleted.setDeletedBy(deletedBy);
            deleted.setDeletedDate(LocalDateTime.now());
            DeletedTaskRepository.save(deleted);

            assignTaskRepository.deleteById(taskId);
            return true;
        }
        return false;
    }

    
    
}
