package canteenManagment.main.UserEntity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "task_tracker_tbl")

public class TaskTrackerTbl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   // Task ID (Unique Identifier)

    @Column(nullable = false)
    private String taskTitle;   // Task Title

    // Many tasks belong to one project
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;   // Project

    // Module → Master Data
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id")
    private MasterModuleDescriptionTbl module;

    // Submodule → Master Data
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submodule_id")
    private MasterSubModuleDescriptionTbl subModule;

    // Screen → Master Data
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "screen_id")
   private MasterScreensTbl screen;

    @Column(columnDefinition = "TEXT")
    private String description;   // Task details

    @Column(columnDefinition = "TEXT")
    private String stepsToReproduce;   // Steps / Scenarios

    private String attachmentPath;   // File path / Screenshot / Docs

    @Column(columnDefinition = "TEXT")
    private String comments;   // Communication between devs & testers

    // Who generated the task
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "generated_by")
    private UserEntity generatedBy;

    private LocalDateTime generatedDate;

    // Who tested the task
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tested_by")
    private UserEntity testedBy;

    private LocalDateTime testedDate;

    @Enumerated(EnumType.STRING)
    private TestingStatus Testing;   // PASSED, FAILED, IN_PROGRESS, BLOCKED

    private String testingRemarks;   // Remarks after testing

    // Assigned to which user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to")
    private UserEntity assignedTo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTaskTitle() {
		return taskTitle;
	}

	public void setTaskTitle(String taskTitle) {
		this.taskTitle = taskTitle;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStepsToReproduce() {
		return stepsToReproduce;
	}

	public void setStepsToReproduce(String stepsToReproduce) {
		this.stepsToReproduce = stepsToReproduce;
	}

	public String getAttachmentPath() {
		return attachmentPath;
	}

	public void setAttachmentPath(String attachmentPath) {
		this.attachmentPath = attachmentPath;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public UserEntity getGeneratedBy() {
		return generatedBy;
	}

	public void setGeneratedBy(UserEntity generatedBy) {
		this.generatedBy = generatedBy;
	}

	public LocalDateTime getGeneratedDate() {
		return generatedDate;
	}

	public void setGeneratedDate(LocalDateTime generatedDate) {
		this.generatedDate = generatedDate;
	}

	public UserEntity getTestedBy() {
		return testedBy;
	}

	public void setTestedBy(UserEntity testedBy) {
		this.testedBy = testedBy;
	}

	public LocalDateTime getTestedDate() {
		return testedDate;
	}

	public void setTestedDate(LocalDateTime testedDate) {
		this.testedDate = testedDate;
	}


	public String getTestingRemarks() {
		return testingRemarks;
	}

	public void setTestingRemarks(String testingRemarks) {
		this.testingRemarks = testingRemarks;
	}

	public UserEntity getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(UserEntity assignedTo) {
		this.assignedTo = assignedTo;
	}
    
    
}





