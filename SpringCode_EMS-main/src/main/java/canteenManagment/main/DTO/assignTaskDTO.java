package canteenManagment.main.DTO;

import java.time.LocalDate;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import lombok.NonNull;

public class assignTaskDTO {

	    private String project;
	    private String taskTitle;
	    private String assignedTo;
	    @NonNull
	    @DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date startDate;

	 
	    
	    @NonNull
	    private String priority;
	    private String module;
	    private String subModule;
	    private String screen;
	    private String description;
	    private String attachmentPath; // or MultipartFile if uploading
	    @DateTimeFormat(pattern = "yyyy-MM-dd")
		private Date endDate;
	    
	    private MultipartFile attachment; 

	    
		public String getProject() {
			return project;
		}
		public void setProject(String project) {
			this.project = project;
		}
		public String getTaskTitle() {
			return taskTitle;
		}
		public void setTaskTitle(String taskTitle) {
			this.taskTitle = taskTitle;
		}
		public String getAssignedTo() {
			return assignedTo;
		}
		public void setAssignedTo(String assignedTo) {
			this.assignedTo = assignedTo;
		}
		
		
		public Date getEndDate() {
			return endDate;
		}
		public void setEndDate(Date endDate) {
			this.endDate = endDate;
		}
		public String getPriority() {
			return priority;
		}
		public Date getStartDate() {
			return startDate;
		}
		public void setStartDate(Date startDate) {
			this.startDate = startDate;
		}
		public void setPriority(String priority) {
			this.priority = priority;
		}
		public String getModule() {
			return module;
		}
		public void setModule(String module) {
			this.module = module;
		}
		public String getSubModule() {
			return subModule;
		}
		public void setSubModule(String subModule) {
			this.subModule = subModule;
		}
		public String getScreen() {
			return screen;
		}
		public void setScreen(String screen) {
			this.screen = screen;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getAttachmentPath() {
			return attachmentPath;
		}
		public void setAttachmentPath(String attachmentPath) {
			this.attachmentPath = attachmentPath;
		}
		public MultipartFile getAttachment() {
			return attachment;
		}
		public void setAttachment(MultipartFile attachment) {
			this.attachment = attachment;
		}

	   
	


}
