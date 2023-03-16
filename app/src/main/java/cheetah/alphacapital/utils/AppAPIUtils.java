package cheetah.alphacapital.utils;

public class AppAPIUtils
{
    public static String TOKEN_ID = "@alpha-capital-123-4$";

    //public static String BASE_URL = "http://192.168.50.52/AlphaCapitalReportApp/api/";
 //public static String BASE_URL = "http://demo1.coronation.in/AlphacapitalReportAppTest/api/";

    public static String BASE_URL = "http://demo1.coronation.in/AlphacapitalReportApp/api/";
    public static String ADD_MANGE_WORKING_DAYS = BASE_URL + "Aum/AddMangeWorkingDays";
    public static String UPDATE_MANGE_WORKING_DAYS = BASE_URL + "Aum/UpdateMangeWorkingDays";
    public static String ADD_ACTIVITY_TYPE = BASE_URL + "Activity/AddActivityTypeMaster";
    public static String UPDATE_ACTIVITY_TYPE = BASE_URL + "Activity/UpdateActivityTypeMaster";

    //Common Api
    public static String LOGIN = BASE_URL + "Common/Login";
    public static String CHANGE_PASSWORD = BASE_URL + "Common/ChangePassword";
    public static String SAVE_RESET_PASSWORD = BASE_URL + "Common/SaveResetPassword";
    public static String SEND_RESET_PASSWORD_MAIL = BASE_URL + "Common/SendResetPasswordMail";
    public static String GET_EMPLOYEE_DETAIL_BY_RESETTOKEN = BASE_URL + "Common/GetEmployeeDetailByResetToken";
    public static String GET_ALL_COUNTRY = BASE_URL + "Common/GetAllCountry";
    public static String Get_All_State = BASE_URL + "Common/GetAllStateByCountryId";
    public static String Get_All_City = BASE_URL + "Common/GetAllCityByStateId";

    //Client Api
    public static String GetAll_Assigned_Client_ByEmployee_For_Details = BASE_URL + "Client/GetAllAssignedClientByEmployee_V1";
    public static String GetAll_Assigned_Client_ByEmployee = BASE_URL + "Client/GetAllAssignedClientByEmployee";

    public static String ADD_CLIENT = BASE_URL + "Client/AddClient";
    public static String Map_Client_With_Employee = BASE_URL + "Client/MapClientWithEmployee";
    public static String MAP_CLIENT_WITH_SCHEME = BASE_URL + "Client/MapClientWithScheme";
    public static String APPROVE_NEWADDED_CLIENT_BYEMPLOYEE = BASE_URL + "Client/ApproveNewAddedClientByEmployee";
    public static String DELETE_CLIENT = BASE_URL + "Client/DeleteClient";

    //Employee
    public static String Get_All_Employee = BASE_URL + "Employee/GetAllEmployee";
    public static String UPDATE_EMPLOYEE = BASE_URL + "Employee/UpdateEmployee";
    public static String ADD_EMPLOYEE = BASE_URL + "Employee/AddEmployee";
    public static String DELETE_EMPLOYEE = BASE_URL + "Employee/DeleteEmployee";

    //Scheme
    public static String GET_ALL_SCHEME = BASE_URL + "Scheme/GetAllScheme";
    public static String ADD_SCHEME = BASE_URL + "Scheme/AddScheme";
    public static String UPDATE_SCHEME = BASE_URL + "Scheme/UpdateScheme";
    public static String DELETE_SCHEME = BASE_URL + "Scheme/DeleteScheme";
    public static String GETALL_SCHEME_TYPE = BASE_URL + "Scheme/GetAllSchemeType";

    //Activity
    public static String GET_ALLACTIVITY_STATUS = BASE_URL + "Activity/GetAllActivityStatus";
    public static String ADD_ACTIVITY = BASE_URL + "Activity/AddActivity";
    public static String UPDATE_ACTIVITY = BASE_URL + "Activity/UpdateActivity";
    public static String DELETE_ACTIVITY = BASE_URL + "Activity/DeleteActivity";
    public static String GET_ALL_ACTIVITY = BASE_URL + "Activity/GetAllActivity";
    public static String GET_ACTIVITYDETAIL_BYID = BASE_URL + "Activity/GetActivityDetailById";
    public static String GET_ALL_ACTIVITY_TYPE = BASE_URL + "Activity/GetAllActivityType";

    //EmployeeTarget
    public static String GET_ALL_EMPLOYEE_TARGET = BASE_URL + "EmployeeTarget/getAllEmployeeTarget";
    public static String UPDATE_EMPLOYEE_TARGET = BASE_URL + "EmployeeTarget/UpdateEmployeeTarget";
    public static String ADD_EMPLOYEE_TARGET = BASE_URL + "EmployeeTarget/AssignEmployeeTarget";

    //Dashboard
    public static String DASHBOARD_DATA = BASE_URL + "Aum/getDashboardData";
    public static String MONTH_YEAR = BASE_URL + "Common/GetAllYearAndMonth";

    //Task
    public static String GET_ALL_TASK = BASE_URL + "Task/GetAllTask";
    public static String GET_TASK_DETAILBYID = BASE_URL + "Task/GetTaskDetailById";
    public static String DELETE_TASK = BASE_URL + "Task/DeleteTask";
    public static String UPDATE_TASK = BASE_URL + "Task/UpdateTask";
    public static String ADD_TASK = BASE_URL + "Task/AddTask";
    public static String GET_ALL_TASKSTATUS = BASE_URL + "Task/GetAllTaskStatus";
    public static String PIN_TASK = BASE_URL + "Task/PinTask";
    public static String GETALLTASK_MESSAGE_BYTASKID = BASE_URL + "Task/GetAllTaskMessageByTaskId";
    public static String SAVE_TASK_MESSAGE = BASE_URL + "Task/SaveTaskMessage";
    public static String MARK_AS_READ_MESSAGE = BASE_URL + "Task/MarkAsReadMessage";
    public static String ADD_DAR_REPORT = BASE_URL + "Task/Add_DAR_Report";
    public static String UPDATE_DAR_REPORT = BASE_URL + "Task/Update_DAR_Report";
    public static String GET_DARREPORT_BYEMPLOYEE = BASE_URL + "Task/GetDarReportByEmployee";

    //AUM
    public static String ADD_AUM = BASE_URL + "Aum/AddAumDetail";
    public static String UPDATE_AUM = BASE_URL + "Aum/UpdateAumDetail";
    public static String GET_CLIENT_MEETING_BY_EMPLOYEE = BASE_URL + "Aum/GetClientMeetingCountByEmployee";
    public static String GET_AUM_LIST = BASE_URL + "Aum/GetAllAumDetailByEmaployee";
    public static String GET_SELF_ACQUIRED_ALUM_LIST = BASE_URL + "Aum/GetAllSelfAcquiredAUMByEmployee";
    public static String ADD_SELF_ACQUIRED_AUM = BASE_URL + "Aum/AddSelfAcquiredAUM";
    public static String UPDATE_SELF_ACQUIRED_AUM = BASE_URL + "Aum/UpdateSelfAcquiredAUM";
    public static String DELETE_SELF_ACQUIRED_AUM = BASE_URL + "Aum/AddSelfAcquiredAUM";

    //For Admin Login
    public static String GET_ALL_CLIENTS = BASE_URL + "Client/GetAllClientForAdminListing";
    public static String APPROVE_CLIENT = BASE_URL + "Client/ApproveNewAddedClientByEmployee";
    public static String UPDATE_CLIENT_DETAILS = BASE_URL + "Client/UpdateClient";
}
