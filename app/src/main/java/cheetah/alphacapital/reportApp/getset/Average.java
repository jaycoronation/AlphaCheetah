package cheetah.alphacapital.reportApp.getset;

public class Average{
	private double attendance;
	private double selfAcquiredAUM;
	private double numberOfPortfolios;
	private double dAR;
	private double knowledgeSessions;
	private String id;

	public void setAttendance(double attendance){
		this.attendance = attendance;
	}

	public double getAttendance(){
		return attendance;
	}

	public void setSelfAcquiredAUM(double selfAcquiredAUM){
		this.selfAcquiredAUM = selfAcquiredAUM;
	}

	public double getSelfAcquiredAUM(){
		return selfAcquiredAUM;
	}

	public void setNumberOfPortfolios(double numberOfPortfolios){
		this.numberOfPortfolios = numberOfPortfolios;
	}

	public double getNumberOfPortfolios(){
		return numberOfPortfolios;
	}

	public void setDAR(double dAR){
		this.dAR = dAR;
	}

	public double getDAR(){
		return dAR;
	}

	public void setKnowledgeSessions(double knowledgeSessions){
		this.knowledgeSessions = knowledgeSessions;
	}

	public double getKnowledgeSessions(){
		return knowledgeSessions;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}
}
