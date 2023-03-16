package cheetah.alphacapital.reportApp.getset;

import java.util.List;

public class GetAllCumulativeLeadsReportResponse{
	private Data data;
	private boolean success;
	private Object message;
	private String id;

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

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public static class Data{
		private LstSummaryData lstSummaryData;
		private String id;
		private List<LstEmpDataItem> lstEmpData;

		public void setLstSummaryData(LstSummaryData lstSummaryData){
			this.lstSummaryData = lstSummaryData;
		}

		public LstSummaryData getLstSummaryData(){
			return lstSummaryData;
		}

		public void setId(String id){
			this.id = id;
		}

		public String getId(){
			return id;
		}

		public void setLstEmpData(List<LstEmpDataItem> lstEmpData){
			this.lstEmpData = lstEmpData;
		}

		public List<LstEmpDataItem> getLstEmpData(){
			return lstEmpData;
		}
	}

	public static class LstEmpDataItem{
		private double NoOfClients;
		private double Conversion_LeadToClient;
		private double Leads_Avaliable;
		private double LeadsStillToBeConverted;
		private double First_Meeting;
		private double conversionMeetToClient;
		private int employeeId;
		private String EmployeeName;
		private String id;

		public void setNoOfClients(double noOfClients){
			this.NoOfClients = noOfClients;
		}

		public double getNoOfClients(){
			return NoOfClients;
		}

		public void setConversion_LeadToClient(double conversion_LeadToClient){
			this.Conversion_LeadToClient = conversion_LeadToClient;
		}

		public double getConversion_LeadToClient(){
			return Conversion_LeadToClient;
		}

		public void setLeads_Avaliable(double leads_Avaliable){
			this.Leads_Avaliable = leads_Avaliable;
		}

		public double getLeads_Avaliable(){
			return Leads_Avaliable;
		}

		public void setLeadsStillToBeConverted(double leadsStillToBeConverted){
			this.LeadsStillToBeConverted = leadsStillToBeConverted;
		}

		public double getLeadsStillToBeConverted(){
			return LeadsStillToBeConverted;
		}

		public void setFirst_Meeting(double first_Meeting){
			this.First_Meeting = first_Meeting;
		}

		public double getFirst_Meeting(){
			return First_Meeting;
		}

		public void setConversionMeetToClient(double conversionMeetToClient){
			this.conversionMeetToClient = conversionMeetToClient;
		}

		public double getConversionMeetToClient(){
			return conversionMeetToClient;
		}

		public void setEmployeeId(int employeeId){
			this.employeeId = employeeId;
		}

		public int getEmployeeId(){
			return employeeId;
		}

		public void setEmployeeName(String employeeName){
			this.EmployeeName = employeeName;
		}

		public String getEmployeeName(){
			return EmployeeName;
		}

		public void setId(String id){
			this.id = id;
		}

		public String getId(){
			return id;
		}
	}

	public static class LstSummaryData{
		private double noOfClients;
		private double conversionLeadToClient;
		private double leadsAvaliable;
		private double leadsStillToBeConverted;
		private String title;
		private double firstMeeting;
		private double conversionMeetToClient;
		private String id;

		public void setNoOfClients(double noOfClients){
			this.noOfClients = noOfClients;
		}

		public double getNoOfClients(){
			return noOfClients;
		}

		public void setConversionLeadToClient(double conversionLeadToClient){
			this.conversionLeadToClient = conversionLeadToClient;
		}

		public double getConversionLeadToClient(){
			return conversionLeadToClient;
		}

		public void setLeadsAvaliable(double leadsAvaliable){
			this.leadsAvaliable = leadsAvaliable;
		}

		public double getLeadsAvaliable(){
			return leadsAvaliable;
		}

		public void setLeadsStillToBeConverted(double leadsStillToBeConverted){
			this.leadsStillToBeConverted = leadsStillToBeConverted;
		}

		public double getLeadsStillToBeConverted(){
			return leadsStillToBeConverted;
		}

		public void setTitle(String title){
			this.title = title;
		}

		public String getTitle(){
			return title;
		}

		public void setFirstMeeting(double firstMeeting){
			this.firstMeeting = firstMeeting;
		}

		public double getFirstMeeting(){
			return firstMeeting;
		}

		public void setConversionMeetToClient(double conversionMeetToClient){
			this.conversionMeetToClient = conversionMeetToClient;
		}

		public double getConversionMeetToClient(){
			return conversionMeetToClient;
		}

		public void setId(String id){
			this.id = id;
		}

		public String getId(){
			return id;
		}
	}


}
