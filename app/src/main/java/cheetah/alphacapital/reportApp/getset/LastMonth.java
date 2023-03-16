package cheetah.alphacapital.reportApp.getset;

public class LastMonth{
	private double workingHours;
	private String id;
	private double workingDays;

	public void setWorkingHours(double workingHours){
		this.workingHours = workingHours;
	}

	public double getWorkingHours(){
		return workingHours;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setWorkingDays(double workingDays){
		this.workingDays = workingDays;
	}

	public double getWorkingDays(){
		return workingDays;
	}
}
