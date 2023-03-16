package cheetah.alphacapital.reportApp.fragment;

import java.util.List;

public class LearningManualResponseModel{
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
		private boolean is_deleted;
		private int id;
		private String created_date;
		private String title;
		private String $id;
		private String timestamp;

		public void setIsDeleted(boolean isDeleted){
			this.is_deleted = isDeleted;
		}

		public boolean isIsDeleted(){
			return is_deleted;
		}

		public void set$id(int $id){
			this.id = $id;
		}

		public int get$id(){
			return id;
		}

		public void setCreated_date(String created_date){
			this.created_date = created_date;
		}

		public String getCreated_date(){
			return created_date;
		}

		public void setTitle(String title){
			this.title = title;
		}

		public String getTitle(){
			return title;
		}

		public void setId(String id){
			this.$id = id;
		}

		public String getId(){
			return $id;
		}

		public void setTimestamp(String timestamp){
			this.timestamp = timestamp;
		}

		public String getTimestamp(){
			return timestamp;
		}
	}

}