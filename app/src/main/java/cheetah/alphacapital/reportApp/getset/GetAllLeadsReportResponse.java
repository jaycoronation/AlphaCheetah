package cheetah.alphacapital.reportApp.getset;

import java.util.List;

public class GetAllLeadsReportResponse{
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
		private List<LstSummaryDataItem> lstSummaryData;
		private String id;
		private List<LstEmpDataItem> lstEmpData;

		public void setLstSummaryData(List<LstSummaryDataItem> lstSummaryData){
			this.lstSummaryData = lstSummaryData;
		}

		public List<LstSummaryDataItem> getLstSummaryData(){
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

	public static class LstSummaryDataItem{
		private double estateAnalysisPPTToBeMade;
		private double openNSEAccountGetInvestmentExecuted;
		private double estateAnalysisPresentationToClient;
		private double consolidatedPortfolioReportToBeMailed;
		private double leadsAvaliable;
		private String title;
		private double sendEstateAnalysisPPTToClient;
		private double sendInvestmentPlan;
		private double onlineAccessOfPortfolio;
		private double fixZoomPhysicalMeeting;
		private double welcomeMail;
		private double presentBusinessModel;
		private double wayForwardDataGatheringMail;
		private String id;
		private double getClientServiceAgreementSigned;

		public void setEstateAnalysisPPTToBeMade(double estateAnalysisPPTToBeMade){
			this.estateAnalysisPPTToBeMade = estateAnalysisPPTToBeMade;
		}

		public double getEstateAnalysisPPTToBeMade(){
			return estateAnalysisPPTToBeMade;
		}

		public void setOpenNSEAccountGetInvestmentExecuted(double openNSEAccountGetInvestmentExecuted){
			this.openNSEAccountGetInvestmentExecuted = openNSEAccountGetInvestmentExecuted;
		}

		public double getOpenNSEAccountGetInvestmentExecuted(){
			return openNSEAccountGetInvestmentExecuted;
		}

		public void setEstateAnalysisPresentationToClient(double estateAnalysisPresentationToClient){
			this.estateAnalysisPresentationToClient = estateAnalysisPresentationToClient;
		}

		public double getEstateAnalysisPresentationToClient(){
			return estateAnalysisPresentationToClient;
		}

		public void setConsolidatedPortfolioReportToBeMailed(double consolidatedPortfolioReportToBeMailed){
			this.consolidatedPortfolioReportToBeMailed = consolidatedPortfolioReportToBeMailed;
		}

		public double getConsolidatedPortfolioReportToBeMailed(){
			return consolidatedPortfolioReportToBeMailed;
		}

		public void setLeadsAvaliable(double leadsAvaliable){
			this.leadsAvaliable = leadsAvaliable;
		}

		public double getLeadsAvaliable(){
			return leadsAvaliable;
		}

		public void setTitle(String title){
			this.title = title;
		}

		public String getTitle(){
			return title;
		}

		public void setSendEstateAnalysisPPTToClient(double sendEstateAnalysisPPTToClient){
			this.sendEstateAnalysisPPTToClient = sendEstateAnalysisPPTToClient;
		}

		public double getSendEstateAnalysisPPTToClient(){
			return sendEstateAnalysisPPTToClient;
		}

		public void setSendInvestmentPlan(double sendInvestmentPlan){
			this.sendInvestmentPlan = sendInvestmentPlan;
		}

		public double getSendInvestmentPlan(){
			return sendInvestmentPlan;
		}

		public void setOnlineAccessOfPortfolio(double onlineAccessOfPortfolio){
			this.onlineAccessOfPortfolio = onlineAccessOfPortfolio;
		}

		public double getOnlineAccessOfPortfolio(){
			return onlineAccessOfPortfolio;
		}

		public void setFixZoomPhysicalMeeting(double fixZoomPhysicalMeeting){
			this.fixZoomPhysicalMeeting = fixZoomPhysicalMeeting;
		}

		public double getFixZoomPhysicalMeeting(){
			return fixZoomPhysicalMeeting;
		}

		public void setWelcomeMail(double welcomeMail){
			this.welcomeMail = welcomeMail;
		}

		public double getWelcomeMail(){
			return welcomeMail;
		}

		public void setPresentBusinessModel(double presentBusinessModel){
			this.presentBusinessModel = presentBusinessModel;
		}

		public double getPresentBusinessModel(){
			return presentBusinessModel;
		}

		public void setWayForwardDataGatheringMail(double wayForwardDataGatheringMail){
			this.wayForwardDataGatheringMail = wayForwardDataGatheringMail;
		}

		public double getWayForwardDataGatheringMail(){
			return wayForwardDataGatheringMail;
		}

		public void setId(String id){
			this.id = id;
		}

		public String getId(){
			return id;
		}

		public void setGetClientServiceAgreementSigned(double getClientServiceAgreementSigned){
			this.getClientServiceAgreementSigned = getClientServiceAgreementSigned;
		}

		public double getGetClientServiceAgreementSigned(){
			return getClientServiceAgreementSigned;
		}
	}

	public static class LstEmpDataItem{
		private double Estate_Analysis_PPT_to_be_made;
		private double Open_NSE_Account_get_investment_executed;
		private double Estate_Analysis_Presentation_to_client;
		private double Consolidated_Portfolio_Report_to_be_mailed;
		private double Leads_Avaliable;
		private double Send_Estate_Analysis_PPT_to_Client;
		private double Send_Investment_Plan;
		private double Online_Access_of_Portfolio;
		private double FixZoom_Physical_Meeting;
		private double Welcome_Mail;
		private double Present_Business_Model;
		private int employeeId;
		private double Way_Forward_Data_Gathering_Mail;
		private String EmployeeName;
		private String id;
		private double Get_Client_Service_Agreement_Signed;

		public void setEstate_Analysis_PPT_to_be_made(double estate_Analysis_PPT_to_be_made){
			this.Estate_Analysis_PPT_to_be_made = estate_Analysis_PPT_to_be_made;
		}

		public double getEstate_Analysis_PPT_to_be_made(){
			return Estate_Analysis_PPT_to_be_made;
		}

		public void setOpen_NSE_Account_get_investment_executed(double open_NSE_Account_get_investment_executed){
			this.Open_NSE_Account_get_investment_executed = open_NSE_Account_get_investment_executed;
		}

		public double getOpen_NSE_Account_get_investment_executed(){
			return Open_NSE_Account_get_investment_executed;
		}

		public void setEstate_Analysis_Presentation_to_client(double estate_Analysis_Presentation_to_client){
			this.Estate_Analysis_Presentation_to_client = estate_Analysis_Presentation_to_client;
		}

		public double getEstate_Analysis_Presentation_to_client(){
			return Estate_Analysis_Presentation_to_client;
		}

		public void setConsolidated_Portfolio_Report_to_be_mailed(double consolidated_Portfolio_Report_to_be_mailed){
			this.Consolidated_Portfolio_Report_to_be_mailed = consolidated_Portfolio_Report_to_be_mailed;
		}

		public double getConsolidated_Portfolio_Report_to_be_mailed(){
			return Consolidated_Portfolio_Report_to_be_mailed;
		}

		public void setLeadsAvaliable(double leadsAvaliable){
			this.Leads_Avaliable = leadsAvaliable;
		}

		public double getLeadsAvaliable(){
			return Leads_Avaliable;
		}

		public void setSend_Estate_Analysis_PPT_to_Client(double send_Estate_Analysis_PPT_to_Client){
			this.Send_Estate_Analysis_PPT_to_Client = send_Estate_Analysis_PPT_to_Client;
		}

		public double getSend_Estate_Analysis_PPT_to_Client(){
			return Send_Estate_Analysis_PPT_to_Client;
		}

		public void setSend_Investment_Plan(double send_Investment_Plan){
			this.Send_Investment_Plan = send_Investment_Plan;
		}

		public double getSend_Investment_Plan(){
			return Send_Investment_Plan;
		}

		public void setOnline_Access_of_Portfolio(double online_Access_of_Portfolio){
			this.Online_Access_of_Portfolio = online_Access_of_Portfolio;
		}

		public double getOnline_Access_of_Portfolio(){
			return Online_Access_of_Portfolio;
		}

		public void setFixZoom_Physical_Meeting(double fixZoom_Physical_Meeting){
			this.FixZoom_Physical_Meeting = fixZoom_Physical_Meeting;
		}

		public double getFixZoom_Physical_Meeting(){
			return FixZoom_Physical_Meeting;
		}

		public void setWelcome_Mail(double welcome_Mail){
			this.Welcome_Mail = welcome_Mail;
		}

		public double getWelcome_Mail(){
			return Welcome_Mail;
		}

		public void setPresent_Business_Model(double present_Business_Model){
			this.Present_Business_Model = present_Business_Model;
		}

		public double getPresent_Business_Model(){
			return Present_Business_Model;
		}

		public void setEmployeeId(int employeeId){
			this.employeeId = employeeId;
		}

		public int getEmployeeId(){
			return employeeId;
		}

		public void setWay_Forward_Data_Gathering_Mail(double way_Forward_Data_Gathering_Mail){
			this.Way_Forward_Data_Gathering_Mail = way_Forward_Data_Gathering_Mail;
		}

		public double getWay_Forward_Data_Gathering_Mail(){
			return Way_Forward_Data_Gathering_Mail;
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

		public void setGet_Client_Service_Agreement_Signed(double get_Client_Service_Agreement_Signed){
			this.Get_Client_Service_Agreement_Signed = get_Client_Service_Agreement_Signed;
		}

		public double getGet_Client_Service_Agreement_Signed(){
			return Get_Client_Service_Agreement_Signed;
		}
	}

}
