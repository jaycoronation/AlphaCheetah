package cheetah.alphacapital.network;

import cheetah.alphacapital.reportApp.fragment.LearningManualResponseModel;
import cheetah.alphacapital.reportApp.getset.AddCapturedLeadResponseModel;
import cheetah.alphacapital.reportApp.getset.AllLearningManualResponseModel;
import cheetah.alphacapital.reportApp.getset.AumEmployeeMonthlySummeryResponse;
import cheetah.alphacapital.reportApp.getset.AumEmployeeYearlySummeryResponse;
import cheetah.alphacapital.reportApp.getset.CapturedLeadsResponseModel;
import cheetah.alphacapital.reportApp.getset.ClientDetailByIdResponse;
import cheetah.alphacapital.reportApp.getset.CommentsGetSet;
import cheetah.alphacapital.reportApp.getset.DARSummaryResponseModel;
import cheetah.alphacapital.reportApp.getset.DeleteLearningModuleResponseModel;
import cheetah.alphacapital.reportApp.getset.EmployeeListResponse;
import cheetah.alphacapital.reportApp.getset.EmployeeTypeResponse;
import cheetah.alphacapital.reportApp.getset.GetAllCumulativeLeadsReportResponse;
import cheetah.alphacapital.reportApp.getset.GetAllDocumentResponseModel;
import cheetah.alphacapital.reportApp.getset.GetAllLeadsReportResponse;
import cheetah.alphacapital.reportApp.getset.GetStartEndYearResponseModel;
import cheetah.alphacapital.reportApp.getset.LeadsDeatilsResponseModel;
import cheetah.alphacapital.reportApp.getset.LeadsResponseModel;
import cheetah.alphacapital.reportApp.getset.MonthlyReportResponse;
import cheetah.alphacapital.reportApp.getset.NotificationResponseModel;
import cheetah.alphacapital.reportApp.getset.RMlistResponse;
import cheetah.alphacapital.reportApp.getset.SaveLeadsResponseModel;
import cheetah.alphacapital.reportApp.getset.SaveMonthlyReportResponse;
import cheetah.alphacapital.reportApp.getset.TaskDetailsResponse;
import cheetah.alphacapital.reportApp.getset.TaskReportResponseModel;
import cheetah.alphacapital.reportApp.getset.ToDoListResponse;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import cheetah.alphacapital.reportApp.getset.AUMListGetSet;
import cheetah.alphacapital.reportApp.getset.AUMReportResponse;
import cheetah.alphacapital.reportApp.getset.ActivityTypeResponse;
import cheetah.alphacapital.reportApp.getset.AddNoteResponse;
import cheetah.alphacapital.reportApp.getset.AllEmployeeResponse;
import cheetah.alphacapital.reportApp.getset.ClientListResponse;
import cheetah.alphacapital.reportApp.getset.CommentResponse;
import cheetah.alphacapital.reportApp.getset.CommonResponse;
import cheetah.alphacapital.reportApp.getset.DARByEmployeeResponse;
import cheetah.alphacapital.reportApp.getset.DARDetailsReportListResponse;
import cheetah.alphacapital.reportApp.getset.DARFilterResponse;
import cheetah.alphacapital.reportApp.getset.DARResponse;
import cheetah.alphacapital.reportApp.getset.EmpDARReportListResponse;
import cheetah.alphacapital.reportApp.getset.EmpDashboardResponse;
import cheetah.alphacapital.reportApp.getset.EmployeeDetailResponse;
import cheetah.alphacapital.reportApp.getset.MonthYearResponse;
import cheetah.alphacapital.reportApp.getset.NewClientListResponse;
import cheetah.alphacapital.reportApp.getset.NoteActionResponse;
import cheetah.alphacapital.reportApp.getset.NotesResponse;
import cheetah.alphacapital.reportApp.getset.SaveTaskResponse;
import cheetah.alphacapital.reportApp.getset.TargetListResponse;
import cheetah.alphacapital.reportApp.getset.WorkingDaysResponse;
import retrofit2.http.Part;

public interface ApiInterface
{
    @POST(ApiEndPoints.GET_ALL_CLIENTS)
    @FormUrlEncoded
    Call<ClientListResponse> getAllClientForEmployee(@Field("pageindex") Integer pageindex, @Field("pagesize") Integer pagesize, @Field("CurrentMonth") String CurrentMonth, @Field("CurrentYear") String CurrentYear, @Field("employeeid") String employeeid);

    @POST(ApiEndPoints.GET_ALL_CLIENTS)
    @FormUrlEncoded
    Call<ClientListResponse> getAllClientForAdmin(@Field("pageindex") Integer pageindex, @Field("pagesize") Integer pagesize, @Field("Search") String Search);

    @POST(ApiEndPoints.MONTH_YEAR)
    Call<MonthYearResponse> getMonthYear();

    @POST(ApiEndPoints.DELETE_CLIENT)
    @FormUrlEncoded
    Call<CommonResponse> deleteClient(@Field("clientid") String clientid);

    @POST(ApiEndPoints.APPROVE_NEWADDED_CLIENT_BYEMPLOYEE)
    @FormUrlEncoded
    Call<CommonResponse> ApproveClientByEmployee(@Field("clientid") String clientid, @Field("status") String status);

    @POST(ApiEndPoints.Get_All_Employee)
    @FormUrlEncoded
    Call<AllEmployeeResponse> getAllEmployee(@Field("clientid") String clientid, @Field("pagesize") String pagesize, @Field("pageindex") String pageindex);

    @POST(ApiEndPoints.GET_CLIENT_DETAIL_BY_ID)
    @FormUrlEncoded
    Call<ClientDetailByIdResponse> getClientDetailById(@Field("employeeid") String employeeid, @Field("clientid") String clientid);

    @POST(ApiEndPoints.GET_AUM_LIST)
    @FormUrlEncoded
    Call<AUMListGetSet> getAllAumDetailByEmaployee(
            @Field("pageindex") String pageindex, @Field("pagesize") String pagesize, @Field("AUM_Month") String AUM_Month, @Field("AUM_Year") String AUM_Year, @Field("employee_id") String employee_id, @Field("client_id") String client_id
    );

    @POST(ApiEndPoints.GET_MONTH_WISE_DAR_REPORT_BY_EMPLOYEE)
    @FormUrlEncoded
    Call<DARResponse> getMonthWiseDarReportByEmployee(
            @Field("PageIndex") String pageindex, @Field("PageSize") String pagesize, @Field("CurrentMonth") String AUM_Month, @Field("CurrentYear") String AUM_Year, @Field("client_id") String client_id, @Field("employee_id") String employee_id
    );

    @POST(ApiEndPoints.ADD_NOTE)
    @FormUrlEncoded
    Call<NoteActionResponse> addNewNote(@Field("employee_id") String employeeid, @Field("client_id") String clientid, @Field("title") String title, @Field("description") String description);

    @POST(ApiEndPoints.UPDATE_NOTE)
    @FormUrlEncoded
    Call<NoteActionResponse> updateNote(@Field("employee_id") String employeeid, @Field("client_id") String clientid, @Field("title") String title, @Field("description") String description, @Field("id") String id);

    @POST(ApiEndPoints.DELETE_NOTE)
    @FormUrlEncoded
    Call<CommonResponse> deleteNote(@Field("id") String id);

    @POST(ApiEndPoints.ALL_WORKING_DAY)
    @FormUrlEncoded
    Call<WorkingDaysResponse> getAllMangeWorkingDay(@Field("month") String month, @Field("year") String year);


    @POST(ApiEndPoints.DELETE_WORKING_DAYS)
    @FormUrlEncoded
    Call<CommonResponse> deleteWorkingDays(@Field("id") String id);


    @POST(ApiEndPoints.GET_ALL_ACTIVITY_TYPE)
    Call<ActivityTypeResponse> getAllActivityType();

    @POST(ApiEndPoints.DELETE_ACTIVITY_TYPE)
    @FormUrlEncoded
    Call<CommonResponse> deleteActivityType(@Field("id") String id);


    @POST(ApiEndPoints.AUM_EMPLOYEE_YEARLY_SUMMERY)
    @FormUrlEncoded
    Call<AumEmployeeYearlySummeryResponse> getAumEmployeeYearlySummery(@Field("CurrentYear") String CurrentYear, @Field("employee_id") String employee_id);

    @POST(ApiEndPoints.AUM_EMPLOYEE_MONTHLY_SUMMERY)
    @FormUrlEncoded
    Call<AumEmployeeMonthlySummeryResponse> getAumMonthlySummeryByClient(@Field("CurrentYear") String CurrentYear, @Field("CurrentMonth") String CurrentMonth, @Field("employee_id") String employee_id);

    @POST(ApiEndPoints.GET_ALL_TASK)
    @FormUrlEncoded
    Call<ToDoListResponse> getAllTaskFromApi(
            @Field("TaskStatusId") String TaskStatusId, @Field("employeeid") String employeeid, @Field("flt_clientids") String flt_clientids, @Field("flt_employeeids") String flt_employeeids, @Field("fromdate") String fromdate, @Field("todate") String todate, @Field("pageindex") String pageindex, @Field("pagesize") String pagesize
    );

    @POST(ApiEndPoints.ADD_TASK)
    @FormUrlEncoded
    Call<SaveTaskResponse> addTask(
            @Field("employee_id") String employee_id, @Field("assign_employee_ids") String assign_employee_ids, @Field("task_status_id") String task_status_id, @Field("task_message") String task_message, @Field("due_date") String due_date,//15/10/2019 12:00 AM
            @Field("assign_client_ids") String assign_client_ids
    );

    @POST(ApiEndPoints.UPDATE_TASK)
    @FormUrlEncoded
    Call<SaveTaskResponse> updateTask(
            @Field("Id") String Id, @Field("employee_id") String employee_id, @Field("assign_employee_ids") String assign_employee_ids, @Field("task_status_id") String task_status_id, @Field("task_message") String task_message,//15/10/2019 12:00 AM
            @Field("due_date") String due_date, @Field("assign_client_ids") String assign_client_ids
    );

    @POST(ApiEndPoints.DELETE_TASK)
    @FormUrlEncoded
    Call<CommonResponse> deleteTask(
            @Field("task_id") String task_id, @Field("employee_id") String employee_id
    );

    @POST(ApiEndPoints.PIN_TASK)
    @FormUrlEncoded
    Call<SaveTaskResponse> pinTask(
            @Field("Id") String Id, @Field("task_id") String task_id, @Field("employee_id") String employee_id, @Field("IsPin") String IsPin
    );

    @POST(ApiEndPoints.SAVE_TASK_MESSAGE)
    @FormUrlEncoded
    Call<CommentResponse> saveTaskMessage(
            @Field("employee_id") String employee_id, @Field("task_id") String task_id, @Field("msg_txt") String msg_txt, @Field("FileExtension") String FileExtension, @Field("is_image") String is_image, @Field("img_url") String img_url
    );

    @POST(ApiEndPoints.MARK_AS_READ_MESSAGE)
    @FormUrlEncoded
    Call<CommonResponse> markAsReadMessage(@Field("task_id") String task_id, @Field("employee_id") String employee_id);

    @POST(ApiEndPoints.GET_TASK_DETAILBYID)
    @FormUrlEncoded
    Call<TaskDetailsResponse> getTaskDetailById(@Field("task_id") String task_id);

    @POST(ApiEndPoints.GETALLTASK_MESSAGE_BYTASKID)
    @FormUrlEncoded
    Call<CommentsGetSet> getAllTaskMessageByTaskId(@Field("task_id") String task_id);


    @POST(ApiEndPoints.GET_ALL_EMPLOYEE)
    @FormUrlEncoded
    Call<EmployeeListResponse> getAllEmployee(
            @Field("pageindex") Integer pageindex, @Field("pagesize") Integer pagesize, @Field("searchtext") String searchtext
    );

    @POST(ApiEndPoints.GET_DAR_REPORT_BY_EMPLOYEE)
    @FormUrlEncoded
    Call<DARByEmployeeResponse> getDARbyEmployee(
            @Field("PageIndex") String PageIndex, @Field("PageSize") String PageSize, @Field("employee_id") String employee_id
    );

    @POST(ApiEndPoints.GET_EMPLOYEE_DETAILS)
    @FormUrlEncoded
    Call<EmployeeDetailResponse> getEmployeeDetails(@Field("employeeid") String employeeid);

    @POST(ApiEndPoints.GET_DASHBOARD_DATA)
    @FormUrlEncoded
    Call<EmpDashboardResponse> getDashboardResponse(
            @Field("employee_id") String employeeid, @Field("CurrentMonth") String CurrentMonth, @Field("CurrentYear") String CurrentYear
    );

    @POST(ApiEndPoints.AUM_REPORT_BY_EMPLOYEE)
    @FormUrlEncoded
    Call<EmpDARReportListResponse> getAUMByEmployee(
            @Field("employeeid") String employeeid, @Field("year") String year
    );

    @POST(ApiEndPoints.MONTH_WISE_DAR_REPORT_BY_EMPLOYEE)
    @FormUrlEncoded
    Call<DARDetailsReportListResponse> getMonthWiseDARReportByEmployee(
            @Field("CurrentMonth") String CurrentMonth, @Field("CurrentYear") String CurrentYear, @Field("PageIndex") String PageIndex, @Field("PageSize") String PageSize, @Field("client_id") String client_id, @Field("employee_id") String employee_id
    );

    @POST(ApiEndPoints.ALL_CLIENT_BY_EMPLOYEE)
    @FormUrlEncoded
    Call<NewClientListResponse> getAllClientByEmployee_new(
            @Field("PageIndex") String PageIndex, @Field("PageSize") String PageSize, @Field("employeeid") String employeeid
    );

    @POST(ApiEndPoints.GET_DAR_FILTER)
    @FormUrlEncoded
    Call<DARFilterResponse> getDARFilters(
            @Field("PageIndex") String PageIndex, @Field("PageSize") String PageSize, @Field("CurrentMonth") String CurrentMonth, @Field("CurrentYear") String CurrentYear, @Field("client_id") String client_id, @Field("employee_id") String employee_id
    );

    @POST(ApiEndPoints.AUM_REPORT_YEAR_WISE)
    @FormUrlEncoded
    Call<AUMReportResponse> getAUMYearWise(
            @Field("CurrentYear") String CurrentYear, @Field("employee_id") String employee_id
    );

    @POST(ApiEndPoints.GET_EMPLOYEE_TARGET)
    @FormUrlEncoded
    Call<TargetListResponse> getEmployeeTargetList(
            @Field("PageIndex") String PageIndex, @Field("PageSize") String PageSize, @Field("AUM_Year") String AUM_Year, @Field("employee_id") String employee_id
    );

    @POST(ApiEndPoints.GET_ALL_NOTES)
    @FormUrlEncoded
    Call<NotesResponse> getAllNotesForEmployee(
            @Field("employee_id_assign") String employee_id_assign
    );

    @POST(ApiEndPoints.ADD_NOTE)
    @FormUrlEncoded
    Call<AddNoteResponse> addNote(
            @Field("client_id") String client_id, @Field("description") String description, @Field("employee_id") String employee_id, @Field("employee_id_assign") String employee_id_assign, @Field("title") String title
    );

    @POST(ApiEndPoints.UPDATE_NOTE)
    @FormUrlEncoded
    Call<AddNoteResponse> updateNote(
            @Field("id") String id, @Field("client_id") String client_id, @Field("description") String description, @Field("employee_id") String employee_id, @Field("employee_id_assign") String employee_id_assign, @Field("title") String title
    );

    @POST(ApiEndPoints.EMPLOYEE_TYPE)
    Call<EmployeeTypeResponse> getEmployeeType();

    @POST(ApiEndPoints.GET_RM_LIST)
    Call<RMlistResponse> getRMList();

    @POST(ApiEndPoints.GET_LEADS_LIST)
    @FormUrlEncoded
    Call<LeadsResponseModel> getLeadsList(
            @Field("CurrentYear") String CurrentYear, @Field("pageindex") Integer pageindex, @Field("pagesize") Integer pagesize, @Field("employee_id") String employee_id
    );

    @POST(ApiEndPoints.GET_LEADS_DETAILS)
    @FormUrlEncoded
    Call<LeadsDeatilsResponseModel> getLeadsDetails(
            @Field("CurrentYear") String CurrentYear, @Field("month") String month, @Field("pageindex") Integer pageindex, @Field("pagesize") Integer pagesize, @Field("employee_id") String employee_id
    );

    @POST(ApiEndPoints.ADD_LEAD)
    @FormUrlEncoded
    Call<SaveLeadsResponseModel> getSaveLead(
            @Field("AUM_Month") Integer AUM_Month, @Field("AUM_Year") Integer AUM_Year, @Field("Category") String Category, @Field("Category_id") String Category_id, @Field("ClientName") String ClientName, @Field("ReferenceDate") String ReferenceDate, @Field("ReferenceFrom") String ReferenceFrom, @Field("Status") String Status, @Field("employee_id") Integer employee_id, @Field("$id") String $id, @Field("id") String id, @Field("Id") Integer Id, @Field("created_date") String created_date
    );

    @POST(ApiEndPoints.UPDATE_LEAD)
    @FormUrlEncoded
    Call<SaveLeadsResponseModel> getUpdateLead(
            @Field("AUM_Month") Integer AUM_Month, @Field("AUM_Year") Integer AUM_Year, @Field("Category") String Category, @Field("Category_id") String Category_id, @Field("ClientName") String ClientName, @Field("ReferenceDate") String ReferenceDate, @Field("ReferenceFrom") String ReferenceFrom, @Field("Status") String Status, @Field("employee_id") Integer employee_id, @Field("$id") String $id, @Field("id") String id, @Field("Id") Integer Id, @Field("created_date") String created_date
    );

    @POST(ApiEndPoints.DELETE_LEAD)
    @FormUrlEncoded
    Call<SaveLeadsResponseModel> deleteLeadsAPI(@Field("id") Integer id);

    @POST(ApiEndPoints.GET_LEARNING_MANUAL_CATEGORY)
    @FormUrlEncoded
    Call<LearningManualResponseModel> getLearningManualsCategory(@Field("employee_id") Integer employee_id);

    @POST(ApiEndPoints.GET_ALL_LEARNING_MANUAL)
    @FormUrlEncoded
    Call<AllLearningManualResponseModel> getAllLearningManual(@Field("LearningManualCategoryId") String LearningManualCategoryId);

    @POST(ApiEndPoints.DELETE_FILE)
    @FormUrlEncoded
    Call<DeleteLearningModuleResponseModel> deleteFileAPI(@Field("id") Integer id);

    @POST(ApiEndPoints.ADD_LEARNING_MANNUAL)
    @FormUrlEncoded
    Call<DeleteLearningModuleResponseModel> getAddLearningManualApi(
            @Field("LearningManualCategoryId") String LearningManualCategoryId, @Field("employee_id") Integer employee_id, @Field("file") String file, @Field("link") String link, @Field("title") String title
    );

    @POST(ApiEndPoints.GET_TASK_REPORT)
    @FormUrlEncoded
    Call<TaskReportResponseModel> getTaskReportAPI(@Field("clientid") Integer clientid, @Field("employee_id") Integer employee_id);

    @POST(ApiEndPoints.UPDATE_LEARNING_MANUAL)
    @FormUrlEncoded
    Call<DeleteLearningModuleResponseModel> getUpdateLearningMaualAPI(
            @Field("LearningManualCategoryId") Integer LearningManualCategoryId,
            @Field("employee_id") Integer employee_id,
            @Field("id") Integer id,
            @Field("link") String link,
            @Field("title") String title
    );

    @POST(ApiEndPoints.DAR_SUMMARAY)
    @FormUrlEncoded
    Call<DARSummaryResponseModel> getDARSummaryAPI(@Field("clientid") Integer clientid, @Field("employee_id") Integer employee_id);

    @POST(ApiEndPoints.UPDATE_DEVICE_TOKEN)
    @FormUrlEncoded
    Call<CommentResponse> updateDeviceToken(@Field("employeeid") Integer user_id,@Field("token_id") String token, @Field("is_android_device") String is_android_device);

    @POST(ApiEndPoints.NOTIFICATION_LIST)
    @FormUrlEncoded
    Call<NotificationResponseModel> getNotificationList(@Field("employee_id") Integer employee_id, @Field("pageindex") Integer pageindex, @Field("pagesize") Integer pagesize);

    @POST(ApiEndPoints.CLEAR_NOTIFICATION_LIST)
    @FormUrlEncoded
    Call<CommentResponse> clearNotificationList(@Field("employee_id") Integer employee_id);

    @POST(ApiEndPoints.MONTHLY_REPORT)
    @FormUrlEncoded
    Call<MonthlyReportResponse> getMonthlyReport(@Field("CurrentYear") String CurrentYear,
                                                 @Field("employee_id") Integer employee_id,
                                                 @Field("pageindex") Integer pageindex,
                                                 @Field("pagesize") Integer pagesize);

    @POST(ApiEndPoints.ADD_MONTHLY_REPORT)
    @FormUrlEncoded
    Call<SaveMonthlyReportResponse> addMonthlyReport(
            @Field("AUM_Month") Integer AUM_Month,
            @Field("AUM_Year") Integer AUM_Year,
            @Field("Attendance") Integer Attendance,
            @Field("DAR") Integer DAR,
            @Field("KnowledgeSessions") Integer KnowledgeSessions,
            @Field("LastDateOfPortfolio") String LastDateOfPortfolio,
            @Field("NumberOfPortfolios") Integer NumberOfPortfolios,
            @Field("SelfAcquiredAUM") Integer SelfAcquiredAUM,
            @Field("employee_id") Integer employee_id
    );

    @POST(ApiEndPoints.GET_ALL_DOCUMENT)
    @FormUrlEncoded
    Call<GetAllDocumentResponseModel> getAllDocument(
            @Field("clientid") Integer clientid,
            @Field("employee_id_assign") Integer employee_id_assign
    );

    @Multipart
    @POST(ApiEndPoints.ADD_DOCUMENT)
    Call<CommentResponse> addDocument(
            @Part MultipartBody.Part client_id,
            @Part MultipartBody.Part employee_id,
            @Part MultipartBody.Part employee_id_assign,
            @Part MultipartBody.Part title,
            @Part MultipartBody.Part file,
            @Part MultipartBody.Part from_app
    );

    @POST(ApiEndPoints.GET_START_END_YEAR)
    @FormUrlEncoded
    Call<GetStartEndYearResponseModel> getStartEndYear(
            @Field("AUM_Month") Integer AUM_Month,
            @Field("AUM_Year") Integer AUM_Year,
            @Field("client_id") Integer client_id
    );

    @POST(ApiEndPoints.GET_LEADS)
    @FormUrlEncoded
    Call<CapturedLeadsResponseModel> getCaputuredLeads(
            @Field("getCaputuredLeads") Integer getCaputuredLeads,
            @Field("pageindex") Integer pageindex,
            @Field("pagesize") Integer pagesize
    );

    @POST(ApiEndPoints.ADD_LEADS)
    @FormUrlEncoded
    Call<AddCapturedLeadResponseModel> addLeads(
            @Field("id") Integer id,
            @Field("employee_id") Integer employee_id,
            @Field("rm_employee_id") Integer rm_employee_id,
            @Field("lead_email") String lead_email,
            @Field("lead_mobile") String lead_mobile,
            @Field("lead_name") String lead_name,
            @Field("lead_source") String lead_source,
            @Field("name_who_gave_lead") String name_who_gave_lead,
            @Field("lead_date") String lead_date
    );


    @POST(ApiEndPoints.UPDATE_LEADS)
    @FormUrlEncoded
    Call<CommonResponse> updateLeads(
            @Field("lead_status_id") Integer lead_status_id,
            @Field("lead_id") String lead_id
    );

    @POST(ApiEndPoints.LEAD_REPORT)
    @FormUrlEncoded
    Call<GetAllLeadsReportResponse> GetAllEMPLeadReport(
            @Field("employee_id") Integer employee_id
    );

    @POST(ApiEndPoints.DELETE_LEADS)
    @FormUrlEncoded
    Call<AddCapturedLeadResponseModel> deleteLead(
            @Field("employee_id") Integer employee_id,
            @Field("id") Integer id
    );

    @POST(ApiEndPoints.LEAD_CUMULATIVE_REPORT)
    @FormUrlEncoded
    Call<GetAllCumulativeLeadsReportResponse> GetCumulativeLeadReport(
            @Field("employee_id") Integer employee_id,
            @Field("year") Integer year,
            @Field("month") Integer month);

    @POST(ApiEndPoints.UPDATE_LEAD_DATE)
    @FormUrlEncoded
    Call<CommonResponse> updateLeadDate(
            @Field("lead_status_id") Integer lead_status_id,
            @Field("lead_date") String lead_date);

}
