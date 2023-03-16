package cheetah.alphacapital.reportApp.getset;

import java.util.ArrayList;
import java.util.ArrayList;

public class AumEmployeeMonthlySummeryResponse
{
    private String message ="";
    private boolean success =false;
    private DataBean data = new DataBean();

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public boolean isSuccess()
    {
        return success;
    }

    public void setSuccess(boolean success)
    {
        this.success = success;
    }

    public DataBean getData()
    {
        return data;
    }

    public void setData(DataBean data)
    {
        this.data = data;
    }

    public static class DataBean
    {
        
        private ArrayList<AumEmployeeYearlySummeryResultBean> AumEmployeeYearlySummeryResult = new ArrayList<AumEmployeeYearlySummeryResultBean>();

        public ArrayList<AumEmployeeYearlySummeryResultBean> getAumEmployeeYearlySummeryResult()
        {
            return AumEmployeeYearlySummeryResult;
        }

        public void setAumEmployeeYearlySummeryResult(ArrayList<AumEmployeeYearlySummeryResultBean> AumEmployeeYearlySummeryResult)
        {
            this.AumEmployeeYearlySummeryResult = AumEmployeeYearlySummeryResult;
        }

        public static class AumEmployeeYearlySummeryResultBean
        {
            private int Id = 0;
            private long Month_End_AUM =0;
            private long sip=0;
            private long Inflow_Outfolw=0;
            private long Meetings_Existing=0;
            private long Meetings_New=0;
            private long References=0;
            private long New_Clients_Convered=0;
            private long Self_Acquired_AUM=0;
            private int month=0;
            private String full_month="";
            private long summery_mail=0;
            private long day_forward_mail=0;
            private long DAR=0;
            private long NewClientConverted=0;
            private long LastYearTotalMeeting=0;
            private long max_month_aum=0;
            private long SelfAcquiredAUM=0;
            private long start_invest_year=0;

            public long getLast_invest_year()
            {
                return last_invest_year;
            }

            public void setLast_invest_year(long last_invest_year)
            {
                this.last_invest_year = last_invest_year;
            }

            public long getStart_invest_year()
            {
                return start_invest_year;
            }

            public void setStart_invest_year(long start_invest_year)
            {
                this.start_invest_year = start_invest_year;
            }

            private long last_invest_year=0;
            private String client_name ="";
            private String client_id = "";

            public long getMax_month_aum()
            {
                return max_month_aum;
            }

            public void setMax_month_aum(long max_month_aum)
            {
                this.max_month_aum = max_month_aum;
            }

            private String employee_id ="";


            public long getLastYearTotalMeeting()
            {
                return LastYearTotalMeeting;
            }

            public void setLastYearTotalMeeting(long lastYearTotalMeeting)
            {
                LastYearTotalMeeting = lastYearTotalMeeting;
            }



            public int getId()
            {
                return Id;
            }

            public void setId(int id)
            {
                Id = id;
            }

            public long getMonth_End_AUM()
            {
                return Month_End_AUM;
            }

            public void setMonth_End_AUM(long month_End_AUM)
            {
                Month_End_AUM = month_End_AUM;
            }

            public long getSip()
            {
                return sip;
            }

            public void setSip(long sip)
            {
                this.sip = sip;
            }

            public long getInflow_Outfolw()
            {
                return Inflow_Outfolw;
            }

            public void setInflow_Outfolw(long inflow_Outfolw)
            {
                Inflow_Outfolw = inflow_Outfolw;
            }

            public long getMeetings_Existing()
            {
                return Meetings_Existing;
            }

            public void setMeetings_Existing(long meetings_Existing)
            {
                Meetings_Existing = meetings_Existing;
            }

            public long getMeetings_New()
            {
                return Meetings_New;
            }

            public void setMeetings_New(long meetings_New)
            {
                Meetings_New = meetings_New;
            }

            public long getReferences()
            {
                return References;
            }

            public void setReferences(long references)
            {
                References = references;
            }

            public long getNew_Clients_Convered()
            {
                return New_Clients_Convered;
            }

            public void setNew_Clients_Convered(long new_Clients_Convered)
            {
                New_Clients_Convered = new_Clients_Convered;
            }

            public long getSelf_Acquired_AUM()
            {
                return Self_Acquired_AUM;
            }

            public void setSelf_Acquired_AUM(long self_Acquired_AUM)
            {
                Self_Acquired_AUM = self_Acquired_AUM;
            }

            public int getMonth()
            {
                return month;
            }

            public void setMonth(int month)
            {
                this.month = month;
            }

            public String getFull_month()
            {
                return full_month;
            }

            public void setFull_month(String full_month)
            {
                this.full_month = full_month;
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

            public long getDAR()
            {
                return DAR;
            }

            public void setDAR(long DAR)
            {
                this.DAR = DAR;
            }

            public long getNewClientConverted()
            {
                return NewClientConverted;
            }

            public void setNewClientConverted(long newClientConverted)
            {
                NewClientConverted = newClientConverted;
            }

            public long getSelfAcquiredAUM()
            {
                return SelfAcquiredAUM;
            }

            public void setSelfAcquiredAUM(long selfAcquiredAUM)
            {
                SelfAcquiredAUM = selfAcquiredAUM;
            }

            public String getClient_name()
            {
                return client_name;
            }

            public void setClient_name(String client_name)
            {
                this.client_name = client_name;
            }

            public String getClient_id()
            {
                return client_id;
            }

            public void setClient_id(String client_id)
            {
                this.client_id = client_id;
            }

            public String getEmployee_id()
            {
                return employee_id;
            }

            public void setEmployee_id(String employee_id)
            {
                this.employee_id = employee_id;
            }
        }
    }
}
