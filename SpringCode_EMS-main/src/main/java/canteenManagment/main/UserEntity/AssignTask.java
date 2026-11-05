package canteenManagment.main.UserEntity;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "AssignTask")
public class AssignTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project", nullable = false)
    private String project;

    @Column(name = "task_title", nullable = false)
    private String taskTitle;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to", nullable = false)
    @JsonIgnore
    private UserEntity assignedTo;
    
  
    @Column(nullable = false)
    private Date startDate;

    @Column(nullable = false)
    private Date endDate;

    @Column(name = "priority", nullable = false)
    private String priority;

    @Column(name = "module", nullable = false)
    private String module;

    @Column(name = "sub_module", nullable = false)
    private String subModule;

    @Column(name = "screen", nullable = false)
    private String screen;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "attachment_path")
    private String attachmentPath;

    private String assignedBy;
    
  
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getPriority() {
		return priority;
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
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public UserEntity getAssignedTo() {
		return assignedTo;
	}


	public void setAssignedTo(UserEntity assignedTo) {
		this.assignedTo = assignedTo;
	}

	
    private String assignedToName; // just the username


	public String getAssignedToName() {
		return assignedToName;
	}


	public void setAssignedToName(String assignedToName) {
		this.assignedToName = assignedToName;
	}
	
	public String getAssignedBy() {
		return assignedBy;
	}
	public void setAssignedBy(String assignedBy) {
		this.assignedBy = assignedBy;
	}


	// Constructor
    public AssignTask() {}


	public AssignTask(Long id, String project, String taskTitle, UserEntity assignedTo, Date startDate, Date endDate,
			String priority, String module, String subModule, String screen, String description, String attachmentPath,
			String assignedToName) {
		super();
		this.id = id;
		this.project = project;
		this.taskTitle = taskTitle;
		this.assignedTo = assignedTo;
		this.startDate = startDate;
		this.endDate = endDate;
		this.priority = priority;
		this.module = module;
		this.subModule = subModule;
		this.screen = screen;
		this.description = description;
		this.attachmentPath = attachmentPath;
		this.assignedToName = assignedToName;
	}

    // Optional: Parameterized constructor, toString, equals, hashCode...
}