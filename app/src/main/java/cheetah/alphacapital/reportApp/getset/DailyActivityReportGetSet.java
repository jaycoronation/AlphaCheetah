package cheetah.alphacapital.reportApp.getset;

/**
 * Created by Ravi Patel on 08-03-2019.
 */
public class DailyActivityReportGetSet
{
    private int Id =0;
    private int employee_id=0;
    private int client_id=0;
    private int activity_type_id=0;
    private String Dar_message ="";
    private String RMName="";
    private int TimeSpent=0 ,TimeSpent_Min = 0;
    private String RemarksComment="";
    private String ReportDate="",ReportDateOrg="";
    private String created_date="";
    private String first_name="";
    private String last_name="";
    private String c_first_name="";
    private String c_last_name="";
    private String activity_type_name="";

    public String getReportDateOrg()
    {
        return ReportDateOrg;
    }

    public void setReportDateOrg(String reportDateOrg)
    {
        ReportDateOrg = reportDateOrg;
    }

    public int getTimeSpent_Min()
    {
        return TimeSpent_Min;
    }

    public void setTimeSpent_Min(int timeSpent_Min)
    {
        TimeSpent_Min = timeSpent_Min;
    }

    public int getId()
    {
        return Id;
    }

    public void setId(int Id)
    {
        this.Id = Id;
    }

    public int getEmployee_id()
    {
        return employee_id;
    }

    public void setEmployee_id(int employee_id)
    {
        this.employee_id = employee_id;
    }

    public int getClient_id()
    {
        return client_id;
    }

    public void setClient_id(int client_id)
    {
        this.client_id = client_id;
    }

    public int getActivity_type_id()
    {
        return activity_type_id;
    }

    public void setActivity_type_id(int activity_type_id)
    {
        this.activity_type_id = activity_type_id;
    }

    public String getDar_message()
    {
        return Dar_message;
    }

    public void setDar_message(String Dar_message)
    {
        this.Dar_message = Dar_message;
    }

    public String getRMName()
    {
        return RMName;
    }

    public void setRMName(String RMName)
    {
        this.RMName = RMName;
    }

    public int getTimeSpent()
    {
        return TimeSpent;
    }

    public void setTimeSpent(int TimeSpent)
    {
        this.TimeSpent = TimeSpent;
    }

    public String getRemarksComment()
    {
        return RemarksComment;
    }

    public void setRemarksComment(String RemarksComment)
    {
        this.RemarksComment = RemarksComment;
    }

    public String getReportDate()
    {
        return ReportDate;
    }

    public void setReportDate(String ReportDate)
    {
        this.ReportDate = ReportDate;
    }

    public String getCreated_date()
    {
        return created_date;
    }

    public void setCreated_date(String created_date)
    {
        this.created_date = created_date;
    }

    public String getFirst_name()
    {
        return first_name;
    }

    public void setFirst_name(String first_name)
    {
        this.first_name = first_name;
    }

    public String getLast_name()
    {
        return last_name;
    }

    public void setLast_name(String last_name)
    {
        this.last_name = last_name;
    }

    public String getC_first_name()
    {
        return c_first_name;
    }

    public void setC_first_name(String c_first_name)
    {
        this.c_first_name = c_first_name;
    }

    public String getC_last_name()
    {
        return c_last_name;
    }

    public void setC_last_name(String c_last_name)
    {
        this.c_last_name = c_last_name;
    }

    public String getActivity_type_name()
    {
        return activity_type_name;
    }

    public void setActivity_type_name(String activity_type_name)
    {
        this.activity_type_name = activity_type_name;
    }
}
