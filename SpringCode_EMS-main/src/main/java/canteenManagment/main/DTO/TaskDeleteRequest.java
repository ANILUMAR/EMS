package canteenManagment.main.DTO;

public class TaskDeleteRequest {
	 private Long taskId;
	    private String reason;
	    private String deletedBy;
		public Long getTaskId() {
			return taskId;
		}
		public void setTaskId(Long taskId) {
			this.taskId = taskId;
		}
		public String getReason() {
			return reason;
		}
		public void setReason(String reason) {
			this.reason = reason;
		}
		public String getDeletedBy() {
			return deletedBy;
		}
		public void setDeletedBy(String deletedBy) {
			this.deletedBy = deletedBy;
		}
	    
	    

}
