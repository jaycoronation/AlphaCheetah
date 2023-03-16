package cheetah.alphacapital.reportApp.getset;

import java.util.List;

public class DARSummaryResponseModel{
	private Data data;
	private boolean success;
	private Object message;
	private String $id;

	public void setData(Data data){
		this.data = data;
	}

	public Data getData(){
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

	public static class Data{
		private Average average;
		private List<DataItem> data;
		private String $id;

		public void setAverage(Average average){
			this.average = average;
		}

		public Average getAverage(){
			return average;
		}

		public void setData(List<DataItem> data){
			this.data = data;
		}

		public List<DataItem> getData(){
			return data;
		}

		public void set$id(String $id){
			this.$id = $id;
		}

		public String get$id(){
			return $id;
		}
	}

	public class Average{
		private CurrentMonthDarAverage PastYearMonthDARAverage;
		private CurrentMonthDarAverage CurrentMonthDarAverage;
		private CurrentMonthDarAverage LastMonthDarAverage;
		private String id;

		public void setPastYearMonthDARAverage(CurrentMonthDarAverage pastYearMonthDARAverage){
			this.PastYearMonthDARAverage = pastYearMonthDARAverage;
		}

		public CurrentMonthDarAverage getPastYearMonthDARAverage(){
			return PastYearMonthDARAverage;
		}

		public void setCurrentMonthDarAverage(CurrentMonthDarAverage currentMonthDarAverage){
			this.CurrentMonthDarAverage = currentMonthDarAverage;
		}

		public CurrentMonthDarAverage getCurrentMonthDarAverage(){
			return CurrentMonthDarAverage;
		}

		public void setLastMonthDarAverage(CurrentMonthDarAverage lastMonthDarAverage){
			this.LastMonthDarAverage = lastMonthDarAverage;
		}

		public CurrentMonthDarAverage getLastMonthDarAverage(){
			return LastMonthDarAverage;
		}

		public void setId(String id){
			this.id = id;
		}

		public String getId(){
			return id;
		}
	}

	public class CurrentMonthDarAverage{
		private double WorkingHourAverage;
		private double totalWorkingDay;
		private double totalDay;
		private double WorkingDayAverage;
		private String $id;

		public double getTotalWorkingDay()
		{
			return totalWorkingDay;
		}

		public void setTotalWorkingDay(double totalWorkingDay)
		{
			this.totalWorkingDay = totalWorkingDay;
		}

		public double getTotalDay()
		{
			return totalDay;
		}

		public void setTotalDay(double totalDay)
		{
			this.totalDay = totalDay;
		}

		public void setWorkingHourAverage(double workingHourAverage){
			this.WorkingHourAverage = workingHourAverage;
		}

		public double getWorkingHourAverage(){
			return WorkingHourAverage;
		}

		public void setWorkingDayAverage(double workingDayAverage){
			this.WorkingDayAverage = workingDayAverage;
		}

		public double getWorkingDayAverage(){
			return WorkingDayAverage;
		}

		public void set$id(String $id){
			this.$id = $id;
		}

		public String get$id(){
			return $id;
		}
	}

	public class DataItem{
		private int employee_id;
		private CurrentMonth LastMonth;
		private String employee_name;
		private CurrentMonth PastYearMonthDAR;
		private CurrentMonth CurrentMonth;
		private String $id;

		public void setEmployee_id(int employee_id){
			this.employee_id = employee_id;
		}

		public int getEmployee_id(){
			return employee_id;
		}

		public void setLastMonth(CurrentMonth lastMonth){
			this.LastMonth = lastMonth;
		}

		public CurrentMonth getLastMonth(){
			return LastMonth;
		}

		public void setEmployee_name(String employee_name){
			this.employee_name = employee_name;
		}

		public String getEmployee_name(){
			return employee_name;
		}

		public void setPastYearMonthDAR(CurrentMonth pastYearMonthDAR){
			this.PastYearMonthDAR = pastYearMonthDAR;
		}

		public CurrentMonth getPastYearMonthDAR(){
			return PastYearMonthDAR;
		}

		public void setCurrentMonth(CurrentMonth currentMonth){
			this.CurrentMonth = currentMonth;
		}

		public CurrentMonth getCurrentMonth(){
			return CurrentMonth;
		}

		public void set$id(String $id){
			this.$id = $id;
		}

		public String get$id(){
			return $id;
		}
	}

	public class CurrentMonth{
		private double WorkingHours;
		private double totalWorkingDay;
		private double totalDay;
		private String $id;
		private double WorkingDays;
		private double totalWorkingHour;
		private double totalHour;

		public double getTotalWorkingHour()
		{
			return totalWorkingHour;
		}

		public void setTotalWorkingHour(double totalWorkingHour)
		{
			this.totalWorkingHour = totalWorkingHour;
		}

		public double getTotalHour()
		{
			return totalHour;
		}

		public void setTotalHour(double totalHour)
		{
			this.totalHour = totalHour;
		}

		public double getTotalWorkingDay()
		{
			return totalWorkingDay;
		}

		public void setTotalWorkingDay(double totalWorkingDay)
		{
			this.totalWorkingDay = totalWorkingDay;
		}

		public double getTotalDay()
		{
			return totalDay;
		}

		public void setTotalDay(double totalDay)
		{
			this.totalDay = totalDay;
		}

		public void setWorkingHours(double workingHours){
			this.WorkingHours = workingHours;
		}

		public double getWorkingHours(){
			return WorkingHours;
		}

		public void set$id(String $id){
			this.$id = $id;
		}

		public String get$id(){
			return $id;
		}

		public void setWorkingDays(double workingDays){
			this.WorkingDays = workingDays;
		}

		public double getWorkingDays(){
			return WorkingDays;
		}
	}



}
