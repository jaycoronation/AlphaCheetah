package cheetah.alphacapital.reportApp.getset;

import java.util.List;

public class NotificationResponseModel{
	private List<DataItem> data;
	private boolean success;
	private Object message;
	private String $id;

	public void setData(List<DataItem> data){
		this.data = data;
	}

	public List<DataItem> getData(){
		return data;
	}

	public void setSuccess(boolean success){
		this.success = success;
	}

	public boolean isSuccess(){
		return success;
	}

	public void setMessage(Object message){
		this.message = message;
	}

	public Object getMessage(){
		return message;
	}

	public void set$id(String $id){
		this.$id = $id;
	}

	public String get$id(){
		return $id;
	}

	public static class DataItem{
		private int TaskId;
		private int RowNo;
		private String ActivityByUserLName;
		private String AddedDate;
		private boolean IsNotificationRead;
		private int ActivityByUserId;
		private int PageCount;
		private String NotificationMessage;
		private String ActivityByUserFName;
		private int UserId;
		private String NotificationType;
		private int id;
		private String UserFName;
		private int RecordCount;
		private String UserLName;
		private String $id;
		private Object TaskMessage;

		public void setTaskId(int taskId){
			this.TaskId = taskId;
		}

		public int getTaskId(){
			return TaskId;
		}

		public void setRowNo(int rowNo){
			this.RowNo = rowNo;
		}

		public int getRowNo(){
			return RowNo;
		}

		public void setActivityByUserLName(String activityByUserLName){
			this.ActivityByUserLName = activityByUserLName;
		}

		public String getActivityByUserLName(){
			return ActivityByUserLName;
		}

		public void setAddedDate(String addedDate){
			this.AddedDate = addedDate;
		}

		public String getAddedDate(){
			return AddedDate;
		}

		public void setIsNotificationRead(boolean isNotificationRead){
			this.IsNotificationRead = isNotificationRead;
		}

		public boolean isIsNotificationRead(){
			return IsNotificationRead;
		}

		public void setActivityByUserId(int activityByUserId){
			this.ActivityByUserId = activityByUserId;
		}

		public int getActivityByUserId(){
			return ActivityByUserId;
		}

		public void setPageCount(int pageCount){
			this.PageCount = pageCount;
		}

		public int getPageCount(){
			return PageCount;
		}

		public void setNotificationMessage(String notificationMessage){
			this.NotificationMessage = notificationMessage;
		}

		public String getNotificationMessage(){
			return NotificationMessage;
		}

		public void setActivityByUserFName(String activityByUserFName){
			this.ActivityByUserFName = activityByUserFName;
		}

		public String getActivityByUserFName(){
			return ActivityByUserFName;
		}

		public void setUserId(int userId){
			this.UserId = userId;
		}

		public int getUserId(){
			return UserId;
		}

		public void setNotificationType(String notificationType){
			this.NotificationType = notificationType;
		}

		public String getNotificationType(){
			return NotificationType;
		}

		public void set$id(String $id){
			this.$id = $id;
		}

		public String get$id(){
			return $id;
		}

		public void setUserFName(String userFName){
			this.UserFName = userFName;
		}

		public String getUserFName(){
			return UserFName;
		}

		public void setRecordCount(int recordCount){
			this.RecordCount = recordCount;
		}

		public int getRecordCount(){
			return RecordCount;
		}

		public void setUserLName(String userLName){
			this.UserLName = userLName;
		}

		public String getUserLName(){
			return UserLName;
		}

		public void setId(int id){
			this.id = id;
		}

		public int getId(){
			return id;
		}

		public void setTaskMessage(Object taskMessage){
			this.TaskMessage = taskMessage;
		}

		public Object getTaskMessage(){
			return TaskMessage;
		}
	}

}