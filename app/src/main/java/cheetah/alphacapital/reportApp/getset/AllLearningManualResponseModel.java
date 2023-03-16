package cheetah.alphacapital.reportApp.getset;

import java.util.List;

public class AllLearningManualResponseModel{
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
		private String employee_f_name = "";
		private String LearningManualCategory = "";
		private int employee_id = 0;
		private int LearningManualCategoryId = 0;
		private String link = "";
		private int id;
		private String created_date = "";
		private String employee_l_name = "";
		private String title = "";
		private String $id = "";

		public void setEmployee_f_name(String employee_f_name){
			this.employee_f_name = employee_f_name;
		}

		public String getEmployee_f_name(){
			return employee_f_name;
		}

		public void setLearningManualCategory(String learningManualCategory){
			this.LearningManualCategory = learningManualCategory;
		}

		public String getLearningManualCategory(){
			return LearningManualCategory;
		}

		public void setEmployee_id(int employee_id){
			this.employee_id = employee_id;
		}

		public int getEmployee_id(){
			return employee_id;
		}

		public void setLearningManualCategoryId(int learningManualCategoryId){
			this.LearningManualCategoryId = learningManualCategoryId;
		}

		public int getLearningManualCategoryId(){
			return LearningManualCategoryId;
		}

		public void setLink(String link){
			this.link = link;
		}

		public String getLink(){
			return link;
		}

		public void set$id(int id){
			this.id = id;
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

		public void setEmployee_l_name(String employee_l_name){
			this.employee_l_name = employee_l_name;
		}

		public String getEmployee_l_name(){
			return employee_l_name;
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
	}

}