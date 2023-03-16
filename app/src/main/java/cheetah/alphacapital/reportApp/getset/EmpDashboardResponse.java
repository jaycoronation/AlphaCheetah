package cheetah.alphacapital.reportApp.getset;

public class EmpDashboardResponse
{
    private String message ="";
    private boolean success =false;
    private DataBean data = new DataBean();

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {

        private DashboardDataBean DashboardData = new DashboardDataBean();
        private DashboardDataForAdminBean DashboardDataForAdmin = new DashboardDataForAdminBean();

        public DashboardDataBean getDashboardData() {
            return DashboardData;
        }

        public void setDashboardData(DashboardDataBean DashboardData) {
            this.DashboardData = DashboardData;
        }

        public DashboardDataForAdminBean getDashboardDataForAdmin() {
            return DashboardDataForAdmin;
        }

        public void setDashboardDataForAdmin(DashboardDataForAdminBean DashboardDataForAdmin) {
            this.DashboardDataForAdmin = DashboardDataForAdmin;
        }

        public static class DashboardDataBean {

            private long Target_meetings_exisiting = 0;
            private long Target_meetings_new = 0;
            private long Target_reference_client = 0;
            private long Target_fresh_aum = 0;
            private long Target_sip = 0;
            private long Target_new_clients_converted = 0;
            private long Target_self_acquired_aum = 0;
            private long TotalClient = 0;
            private long MeetingWithClient = 0;
            private long Actual_Reference = 0;
            private long Actual_SIP = 0;
            private long Actual_fresh_aum = 0;
            private long Actual_fresh_aum_TillDate = 0;
            private long Actual_New_Meeting = 0;
            private long Actual_Existing_Meeting = 0;
            private long Actual_NewClientsConverted = 0;
            private long Actual_SelfAcquiredAUM = 0;
            private long OverallProgress = 0;
            private long Month_End_AUM = 0;
            private long Inflow_Outfolw = 0;
            private long summery_mail = 0;
            private long day_forward_mail = 0;

            public long getTarget_meetings_exisiting()
            {
                return Target_meetings_exisiting;
            }

            public void setTarget_meetings_exisiting(long target_meetings_exisiting)
            {
                Target_meetings_exisiting = target_meetings_exisiting;
            }

            public long getTarget_meetings_new()
            {
                return Target_meetings_new;
            }

            public void setTarget_meetings_new(long target_meetings_new)
            {
                Target_meetings_new = target_meetings_new;
            }

            public long getTarget_reference_client()
            {
                return Target_reference_client;
            }

            public void setTarget_reference_client(long target_reference_client)
            {
                Target_reference_client = target_reference_client;
            }

            public long getTarget_fresh_aum()
            {
                return Target_fresh_aum;
            }

            public void setTarget_fresh_aum(long target_fresh_aum)
            {
                Target_fresh_aum = target_fresh_aum;
            }

            public long getTarget_sip()
            {
                return Target_sip;
            }

            public void setTarget_sip(long target_sip)
            {
                Target_sip = target_sip;
            }

            public long getTarget_new_clients_converted()
            {
                return Target_new_clients_converted;
            }

            public void setTarget_new_clients_converted(long target_new_clients_converted)
            {
                Target_new_clients_converted = target_new_clients_converted;
            }

            public long getTarget_self_acquired_aum()
            {
                return Target_self_acquired_aum;
            }

            public void setTarget_self_acquired_aum(long target_self_acquired_aum)
            {
                Target_self_acquired_aum = target_self_acquired_aum;
            }

            public long getTotalClient()
            {
                return TotalClient;
            }

            public void setTotalClient(long totalClient)
            {
                TotalClient = totalClient;
            }

            public long getMeetingWithClient()
            {
                return MeetingWithClient;
            }

            public void setMeetingWithClient(long meetingWithClient)
            {
                MeetingWithClient = meetingWithClient;
            }

            public long getActual_Reference()
            {
                return Actual_Reference;
            }

            public void setActual_Reference(long actual_Reference)
            {
                Actual_Reference = actual_Reference;
            }

            public long getActual_SIP()
            {
                return Actual_SIP;
            }

            public void setActual_SIP(long actual_SIP)
            {
                Actual_SIP = actual_SIP;
            }

            public long getActual_fresh_aum()
            {
                return Actual_fresh_aum;
            }

            public void setActual_fresh_aum(long actual_fresh_aum)
            {
                Actual_fresh_aum = actual_fresh_aum;
            }

            public long getActual_fresh_aum_TillDate()
            {
                return Actual_fresh_aum_TillDate;
            }

            public void setActual_fresh_aum_TillDate(long actual_fresh_aum_TillDate)
            {
                Actual_fresh_aum_TillDate = actual_fresh_aum_TillDate;
            }

            public long getActual_New_Meeting()
            {
                return Actual_New_Meeting;
            }

            public void setActual_New_Meeting(long actual_New_Meeting)
            {
                Actual_New_Meeting = actual_New_Meeting;
            }

            public long getActual_Existing_Meeting()
            {
                return Actual_Existing_Meeting;
            }

            public void setActual_Existing_Meeting(long actual_Existing_Meeting)
            {
                Actual_Existing_Meeting = actual_Existing_Meeting;
            }

            public long getActual_NewClientsConverted()
            {
                return Actual_NewClientsConverted;
            }

            public void setActual_NewClientsConverted(long actual_NewClientsConverted)
            {
                Actual_NewClientsConverted = actual_NewClientsConverted;
            }

            public long getActual_SelfAcquiredAUM()
            {
                return Actual_SelfAcquiredAUM;
            }

            public void setActual_SelfAcquiredAUM(long actual_SelfAcquiredAUM)
            {
                Actual_SelfAcquiredAUM = actual_SelfAcquiredAUM;
            }

            public long getOverallProgress()
            {
                return OverallProgress;
            }

            public void setOverallProgress(long overallProgress)
            {
                OverallProgress = overallProgress;
            }

            public long getMonth_End_AUM()
            {
                return Month_End_AUM;
            }

            public void setMonth_End_AUM(long month_End_AUM)
            {
                Month_End_AUM = month_End_AUM;
            }

            public long getInflow_Outfolw()
            {
                return Inflow_Outfolw;
            }

            public void setInflow_Outfolw(long inflow_Outfolw)
            {
                Inflow_Outfolw = inflow_Outfolw;
            }

            public long getSummery_mail()
            {
                return summery_mail;
            }

            public void setSummery_mail(long summery_mail)
            {
                this.summery_mail = summery_mail;
            }

            public long getDay_forward_mail()
            {
                return day_forward_mail;
            }

            public void setDay_forward_mail(long day_forward_mail)
            {
                this.day_forward_mail = day_forward_mail;
            }
        }

        public static class DashboardDataForAdminBean {

            private long TotalTask;
            private long TotalCompletedTask;
            private long TotalEmployeeForAdmin;
            private long TotalClientForAdmin;
            private long TotalSchemeForAdmin;

            public long getTotalTask()
            {
                return TotalTask;
            }

            public void setTotalTask(long totalTask)
            {
                TotalTask = totalTask;
            }

            public long getTotalCompletedTask()
            {
                return TotalCompletedTask;
            }

            public void setTotalCompletedTask(long totalCompletedTask)
            {
                TotalCompletedTask = totalCompletedTask;
            }

            public long getTotalEmployeeForAdmin()
            {
                return TotalEmployeeForAdmin;
            }

            public void setTotalEmployeeForAdmin(long totalEmployeeForAdmin)
            {
                TotalEmployeeForAdmin = totalEmployeeForAdmin;
            }

            public long getTotalClientForAdmin()
            {
                return TotalClientForAdmin;
            }

            public void setTotalClientForAdmin(long totalClientForAdmin)
            {
                TotalClientForAdmin = totalClientForAdmin;
            }

            public long getTotalSchemeForAdmin()
            {
                return TotalSchemeForAdmin;
            }

            public void setTotalSchemeForAdmin(long totalSchemeForAdmin)
            {
                TotalSchemeForAdmin = totalSchemeForAdmin;
            }
        }
    }
}
