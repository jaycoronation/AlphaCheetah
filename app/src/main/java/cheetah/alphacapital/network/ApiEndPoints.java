package cheetah.alphacapital.network;

/**
 * Created by Kiran Patel on 23-Jan-19.
 */
public interface ApiEndPoints
{
    String GET_ALL_CLIENTS = "Client/GetAllClientForAdminListing";
    String MONTH_YEAR = "Common/GetAllYearAndMonth";
    String DELETE_CLIENT = "Client/DeleteClient";
    String APPROVE_NEWADDED_CLIENT_BYEMPLOYEE = "Client/ApproveNewAddedClientByEmployee";
    String Get_All_Employee = "Employee/GetAllEmployee";
    String GET_CLIENT_DETAIL_BY_ID = "Client/GetClientDetailById_v1";
    String GET_AUM_LIST = "Aum/GetAllAumDetailByEmaployee";
    String GET_MONTH_WISE_DAR_REPORT_BY_EMPLOYEE = "Task/GetMonthWiseDarReportByEmployee";
    String ALL_WORKING_DAY = "Aum/GetAllMangeWorkingDay";
    String DELETE_WORKING_DAYS = "Aum/DeleteMangeWorkingDaysById";
    String ADD_NOTE = "Client/AddNote";
    String UPDATE_NOTE = "Client/UpdateNote";
    String DELETE_NOTE = "Client/DeleteNote";
    String GET_ALL_ACTIVITY_TYPE = "Activity/GetAllActivityType";
    String DELETE_ACTIVITY_TYPE = "Activity/DeleteActivityTypeMasterById";
    String AUM_EMPLOYEE_YEARLY_SUMMERY = "Aum/AumEmployeeYearlySummery";
    String AUM_EMPLOYEE_MONTHLY_SUMMERY = "Aum/AumMonthlySummeryByClient";
    String GET_ALL_TASK = "Task/GetAllTask";
    String ADD_TASK = "Task/AddTask";
    String UPDATE_TASK = "Task/UpdateTask";
    String DELETE_TASK = "Task/DeleteTask";
    String PIN_TASK = "Task/PinTask";
    String GET_TASK_DETAILBYID = "Task/GetTaskDetailById";
    String GETALLTASK_MESSAGE_BYTASKID = "Task/GetAllTaskMessageByTaskId";
    String SAVE_TASK_MESSAGE = "Task/SaveTaskMessage";
    String MARK_AS_READ_MESSAGE = "Task/MarkAsReadMessage";

    ///kiran
    String GET_RM_LIST = "Employee/GetAllRMEmployeeList";
    String EMPLOYEE_TYPE = "Employee/GetAllEmployeeType";
    String GET_ALL_EMPLOYEE = "Employee/GetAllEmployee";
    String GET_DAR_REPORT_BY_EMPLOYEE = "Task/GetDarReportByEmployee";
    String GET_DASHBOARD_DATA = "Aum/getDashboardData";
    String GET_EMPLOYEE_DETAILS = "Employee/GetEmployeeDetailById";
    String AUM_REPORT_BY_EMPLOYEE = "Employee/GetAUMReportByEmaployee";
    String MONTH_WISE_DAR_REPORT_BY_EMPLOYEE = "Task/GetMonthWiseDarReportByEmployee";
    String ALL_CLIENT_BY_EMPLOYEE = "Client/GetAllAssignedClientByEmployee";
    String GET_DAR_FILTER = "Task/GetDarReportByType";
    String AUM_REPORT_YEAR_WISE = "Aum/AumEmployeeYearlySummery";
    String GET_EMPLOYEE_TARGET = "EmployeeTarget/getAllEmployeeTarget";
    String GET_ALL_NOTES = "Client/GetAllNotesByEmployeeAssign";

    //Jay
    String GET_LEADS_DETAILS = "Employee/LeadsTrackerByEmaployeeMonthWiseSummery";
    String GET_LEADS_LIST = "Employee/LeadsTrackerYearlySummery";
    String ADD_LEAD = "Employee/AddLeadsTrackerDetail";
    String UPDATE_LEAD = "Employee/UpdateLeadsTrackerDetail";
    String DELETE_LEAD = "Employee/DeleteLeadsTrackerDetail";

    String GET_LEARNING_MANUAL_CATEGORY = "Client/GetAllLearningManualCategory";
    String GET_ALL_LEARNING_MANUAL = "Client/GetAllLearningManualByEmployeeId";
    String DELETE_FILE = "Client/DeleteLearningManual";
    String ADD_LEARNING_MANNUAL = "Client/AddLearningManual";
    String UPDATE_LEARNING_MANUAL = "Client/UpdateLearningManual";

    String GET_TASK_REPORT = "Task/GetTaskReportByEmployee";
    String DAR_SUMMARAY = "Employee/GetDailyActivityReportEmployeeWise";

    String UPDATE_DEVICE_TOKEN = "Employee/UpdateDeviceToken";
    String NOTIFICATION_LIST = "Task/GetAllNotificationByUserId";
    String CLEAR_NOTIFICATION_LIST = "Task/DeleteAllNotificationByEmployeeId";

    String MONTHLY_REPORT = "Employee/MonthlyReportYearlySummery";
    String ADD_MONTHLY_REPORT = "Employee/AddMonthlyReportDetail";

    String GET_ALL_DOCUMENT = "Client/GetAllDocumentByEmployeeAssign";
    String ADD_DOCUMENT ="Client/AddDocument";

    String GET_START_END_YEAR = "aum/getAumStartAndLastInvestyearByClient";

    String GET_LEADS = "Lead/GetAllLeadDetail";
    String ADD_LEADS = "Lead/AddLead";
    String UPDATE_LEADS = "Lead/UpdateLeadStatus";
    String DELETE_LEADS = "Lead/DeleteLead";
    String UPDATE_LEAD_DATE = "Lead/UpdateLeadStatusDate";

    String LEAD_REPORT = "Lead/GetAllEMPLeadReport_AdminView";
    String LEAD_CUMULATIVE_REPORT = "Lead/GetAllEMPCumulativeData_AdminView";

    // String ADD_NOTE = "Client/AddNote";
    //String UPDATE_NOTE = "Client/UpdateNote";
    //String DELETE_NOTE = "Client/DeleteNote";
}
