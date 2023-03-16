package cheetah.alphacapital.reportApp.getset;

public class LastMonthDarAverage{
	private double workingHourAverage;
	private double workingDayAverage;
	private String id;

	public void setWorkingHourAverage(double workingHourAverage){
		this.workingHourAverage = workingHourAverage;
	}

	public double getWorkingHourAverage(){
		return workingHourAverage;
	}

	public void setWorkingDayAverage(double workingDayAverage){
		this.workingDayAverage = workingDayAverage;
	}

	public double getWorkingDayAverage(){
		return workingDayAverage;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}
}
