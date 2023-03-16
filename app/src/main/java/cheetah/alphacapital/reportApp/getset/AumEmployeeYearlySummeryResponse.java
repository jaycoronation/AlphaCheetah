package cheetah.alphacapital.reportApp.getset;

import java.util.ArrayList;

public class AumEmployeeYearlySummeryResponse {

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

    public static class DataBean {
        
        private SummeryBean Summery = new SummeryBean();
        private ArrayList<AumEmployeeYearlySummeryResultBean> AumEmployeeYearlySummeryResult = new ArrayList<AumEmployeeYearlySummeryResultBean>();

      
        public SummeryBean getSummery()
        {
            return Summery;
        }

        public void setSummery(SummeryBean Summery)
        {
            this.Summery = Summery;
        }

        public ArrayList<AumEmployeeYearlySummeryResultBean> getAumEmployeeYearlySummeryResult()
        {
            return AumEmployeeYearlySummeryResult;
        }

        public void setAumEmployeeYearlySummeryResult(ArrayList<AumEmployeeYearlySummeryResultBean> AumEmployeeYearlySummeryResult)
        {
            this.AumEmployeeYearlySummeryResult = AumEmployeeYearlySummeryResult;
        }

        public static class SummeryBean {
            
            private TotalBean Total = new TotalBean();
            private AverageBean Average = new AverageBean();
            private TargetBean Target = new TargetBean();
            private VarianceBean Variance = new VarianceBean();

            public TotalBean getTotal()
            {
                return Total;
            }

            public void setTotal(TotalBean Total)
            {
                this.Total = Total;
            }

            public AverageBean getAverage()
            {
                return Average;
            }

            public void setAverage(AverageBean Average)
            {
                this.Average = Average;
            }

            public TargetBean getTarget()
            {
                return Target;
            }

            public void setTarget(TargetBean Target)
            {
                this.Target = Target;
            }

            public VarianceBean getVariance()
            {
                return Variance;
            }

            public void setVariance(VarianceBean Variance)
            {
                this.Variance = Variance;
            }

            public static class TotalBean {
                private long MonthEndAUM= 0;
                private long SIP= 0;
                private long Inflow_Outfolw= 0;
                private long Meetings_Existing= 0;
                private long Meetings_New= 0;
                private long References= 0;
                private long New_Clients_Convered= 0;
                private long Self_Acquired_AUM= 0;
                private long summery_mail= 0;
                private long day_forward_mail= 0;
                private long DAR= 0;
                private long NewClientConverted= 0;
                private long SelfAcquiredAUM= 0;


                public long getMonthEndAUM()
                {
                    return MonthEndAUM;
                }

                public void setMonthEndAUM(long MonthEndAUM)
                {
                    this.MonthEndAUM = MonthEndAUM;
                }

                public long getSIP()
                {
                    return SIP;
                }

                public void setSIP(long SIP)
                {
                    this.SIP = SIP;
                }

                public long getInflow_Outfolw()
                {
                    return Inflow_Outfolw;
                }

                public void setInflow_Outfolw(long Inflow_Outfolw)
                {
                    this.Inflow_Outfolw = Inflow_Outfolw;
                }

                public long getMeetings_Existing()
                {
                    return Meetings_Existing;
                }

                public void setMeetings_Existing(long Meetings_Existing)
                {
                    this.Meetings_Existing = Meetings_Existing;
                }

                public long getMeetings_New()
                {
                    return Meetings_New;
                }

                public void setMeetings_New(long Meetings_New)
                {
                    this.Meetings_New = Meetings_New;
                }

                public long getReferences()
                {
                    return References;
                }

                public void setReferences(long References)
                {
                    this.References = References;
                }

                public long getNew_Clients_Convered()
                {
                    return New_Clients_Convered;
                }

                public void setNew_Clients_Convered(long New_Clients_Convered)
                {
                    this.New_Clients_Convered = New_Clients_Convered;
                }

                public long getSelf_Acquired_AUM()
                {
                    return Self_Acquired_AUM;
                }

                public void setSelf_Acquired_AUM(long Self_Acquired_AUM)
                {
                    this.Self_Acquired_AUM = Self_Acquired_AUM;
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

                public void setNewClientConverted(long NewClientConverted)
                {
                    this.NewClientConverted = NewClientConverted;
                }

                public long getSelfAcquiredAUM()
                {
                    return SelfAcquiredAUM;
                }

                public void setSelfAcquiredAUM(long SelfAcquiredAUM)
                {
                    this.SelfAcquiredAUM = SelfAcquiredAUM;
                }
            }

            public static class AverageBean {

                private long MonthEndAUM= 0;
                private long SIP= 0;
                private long Inflow_Outfolw= 0;
                private long Meetings_Existing= 0;
                private long Meetings_New= 0;
                private long References= 0;
                private long New_Clients_Convered= 0;
                private long Self_Acquired_AUM= 0;
                private long summery_mail= 0;
                private long day_forward_mail= 0;
                private long DAR= 0;
                private long NewClientConverted= 0;
                private long SelfAcquiredAUM= 0;



                public long getMonthEndAUM()
                {
                    return MonthEndAUM;
                }

                public void setMonthEndAUM(long MonthEndAUM)
                {
                    this.MonthEndAUM = MonthEndAUM;
                }

                public long getSIP()
                {
                    return SIP;
                }

                public void setSIP(long SIP)
                {
                    this.SIP = SIP;
                }

                public long getInflow_Outfolw()
                {
                    return Inflow_Outfolw;
                }

                public void setInflow_Outfolw(long Inflow_Outfolw)
                {
                    this.Inflow_Outfolw = Inflow_Outfolw;
                }

                public long getMeetings_Existing()
                {
                    return Meetings_Existing;
                }

                public void setMeetings_Existing(long Meetings_Existing)
                {
                    this.Meetings_Existing = Meetings_Existing;
                }

                public long getMeetings_New()
                {
                    return Meetings_New;
                }

                public void setMeetings_New(long Meetings_New)
                {
                    this.Meetings_New = Meetings_New;
                }

                public long getReferences()
                {
                    return References;
                }

                public void setReferences(long References)
                {
                    this.References = References;
                }

                public long getNew_Clients_Convered()
                {
                    return New_Clients_Convered;
                }

                public void setNew_Clients_Convered(long New_Clients_Convered)
                {
                    this.New_Clients_Convered = New_Clients_Convered;
                }

                public long getSelf_Acquired_AUM()
                {
                    return Self_Acquired_AUM;
                }

                public void setSelf_Acquired_AUM(long Self_Acquired_AUM)
                {
                    this.Self_Acquired_AUM = Self_Acquired_AUM;
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

                public void setNewClientConverted(long NewClientConverted)
                {
                    this.NewClientConverted = NewClientConverted;
                }

                public long getSelfAcquiredAUM()
                {
                    return SelfAcquiredAUM;
                }

                public void setSelfAcquiredAUM(long SelfAcquiredAUM)
                {
                    this.SelfAcquiredAUM = SelfAcquiredAUM;
                }
            }

            public static class TargetBean {

                private long MonthEndAUM= 0;
                private long SIP= 0;
                private long Inflow_Outfolw= 0;
                private long Meetings_Existing= 0;
                private long Meetings_New= 0;
                private long References= 0;
                private long New_Clients_Convered= 0;
                private long Self_Acquired_AUM= 0;
                private long summery_mail= 0;
                private long day_forward_mail= 0;



                public long getMonthEndAUM()
                {
                    return MonthEndAUM;
                }

                public void setMonthEndAUM(long MonthEndAUM)
                {
                    this.MonthEndAUM = MonthEndAUM;
                }

                public long getSIP()
                {
                    return SIP;
                }

                public void setSIP(long SIP)
                {
                    this.SIP = SIP;
                }

                public long getInflow_Outfolw()
                {
                    return Inflow_Outfolw;
                }

                public void setInflow_Outfolw(long Inflow_Outfolw)
                {
                    this.Inflow_Outfolw = Inflow_Outfolw;
                }

                public long getMeetings_Existing()
                {
                    return Meetings_Existing;
                }

                public void setMeetings_Existing(long Meetings_Existing)
                {
                    this.Meetings_Existing = Meetings_Existing;
                }

                public long getMeetings_New()
                {
                    return Meetings_New;
                }

                public void setMeetings_New(long Meetings_New)
                {
                    this.Meetings_New = Meetings_New;
                }

                public long getReferences()
                {
                    return References;
                }

                public void setReferences(long References)
                {
                    this.References = References;
                }

                public long getNew_Clients_Convered()
                {
                    return New_Clients_Convered;
                }

                public void setNew_Clients_Convered(long New_Clients_Convered)
                {
                    this.New_Clients_Convered = New_Clients_Convered;
                }

                public long getSelf_Acquired_AUM()
                {
                    return Self_Acquired_AUM;
                }

                public void setSelf_Acquired_AUM(long Self_Acquired_AUM)
                {
                    this.Self_Acquired_AUM = Self_Acquired_AUM;
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

            public static class VarianceBean {
                private long MonthEndAUM= 0;
                private long SIP= 0;
                private long Inflow_Outfolw= 0;
                private long Meetings_Existing= 0;
                private long Meetings_New= 0;
                private long References= 0;
                private long New_Clients_Convered= 0;
                private long Self_Acquired_AUM= 0;
                private long summery_mail= 0;
                private long day_forward_mail= 0;

                public long getMonthEndAUM()
                {
                    return MonthEndAUM;
                }

                public void setMonthEndAUM(long MonthEndAUM)
                {
                    this.MonthEndAUM = MonthEndAUM;
                }

                public long getSIP()
                {
                    return SIP;
                }

                public void setSIP(long SIP)
                {
                    this.SIP = SIP;
                }

                public long getInflow_Outfolw()
                {
                    return Inflow_Outfolw;
                }

                public void setInflow_Outfolw(long Inflow_Outfolw)
                {
                    this.Inflow_Outfolw = Inflow_Outfolw;
                }

                public long getMeetings_Existing()
                {
                    return Meetings_Existing;
                }

                public void setMeetings_Existing(long Meetings_Existing)
                {
                    this.Meetings_Existing = Meetings_Existing;
                }

                public long getMeetings_New()
                {
                    return Meetings_New;
                }

                public void setMeetings_New(long Meetings_New)
                {
                    this.Meetings_New = Meetings_New;
                }

                public long getReferences()
                {
                    return References;
                }

                public void setReferences(long References)
                {
                    this.References = References;
                }

                public long getNew_Clients_Convered()
                {
                    return New_Clients_Convered;
                }

                public void setNew_Clients_Convered(long New_Clients_Convered)
                {
                    this.New_Clients_Convered = New_Clients_Convered;
                }

                public long getSelf_Acquired_AUM()
                {
                    return Self_Acquired_AUM;
                }

                public void setSelf_Acquired_AUM(long Self_Acquired_AUM)
                {
                    this.Self_Acquired_AUM = Self_Acquired_AUM;
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
        }

        public static class AumEmployeeYearlySummeryResultBean {

            private long Month_End_AUM = 0;
            private long sip= 0;
            private long Meetings_Existing= 0;
            private long Meetings_New= 0;
            private long References= 0;
            private long New_Clients_Convered= 0;
            private long Self_Acquired_AUM= 0;
            private long month= 0;
            private String full_month ="";
            private long Inflow_Outfolw= 0;
            private long summery_mail= 0;
            private long day_forward_mail= 0;
            private long DAR= 0;
            private long NewClientConverted= 0;
            private long SelfAcquiredAUM= 0;

            public long getMonth_End_AUM()
            {
                return Month_End_AUM;
            }

            public void setMonth_End_AUM(long Month_End_AUM)
            {
                this.Month_End_AUM = Month_End_AUM;
            }

            public long getSip()
            {
                return sip;
            }

            public void setSip(long sip)
            {
                this.sip = sip;
            }

            public long getMeetings_Existing()
            {
                return Meetings_Existing;
            }

            public void setMeetings_Existing(long Meetings_Existing)
            {
                this.Meetings_Existing = Meetings_Existing;
            }

            public long getMeetings_New()
            {
                return Meetings_New;
            }

            public void setMeetings_New(long Meetings_New)
            {
                this.Meetings_New = Meetings_New;
            }

            public long getReferences()
            {
                return References;
            }

            public void setReferences(long References)
            {
                this.References = References;
            }

            public long getNew_Clients_Convered()
            {
                return New_Clients_Convered;
            }

            public void setNew_Clients_Convered(long New_Clients_Convered)
            {
                this.New_Clients_Convered = New_Clients_Convered;
            }

            public long getSelf_Acquired_AUM()
            {
                return Self_Acquired_AUM;
            }

            public void setSelf_Acquired_AUM(long Self_Acquired_AUM)
            {
                this.Self_Acquired_AUM = Self_Acquired_AUM;
            }

            public long getMonth()
            {
                return month;
            }

            public void setMonth(long month)
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

            public long getInflow_Outfolw()
            {
                return Inflow_Outfolw;
            }

            public void setInflow_Outfolw(long Inflow_Outfolw)
            {
                this.Inflow_Outfolw = Inflow_Outfolw;
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

            public void setNewClientConverted(long NewClientConverted)
            {
                this.NewClientConverted = NewClientConverted;
            }

            public long getSelfAcquiredAUM()
            {
                return SelfAcquiredAUM;
            }

            public void setSelfAcquiredAUM(long SelfAcquiredAUM)
            {
                this.SelfAcquiredAUM = SelfAcquiredAUM;
            }
        }
    }
}
