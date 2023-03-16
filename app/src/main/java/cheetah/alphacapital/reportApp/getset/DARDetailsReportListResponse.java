package cheetah.alphacapital.reportApp.getset;

import java.util.ArrayList;
import java.util.List;

public class DARDetailsReportListResponse
{

    /**
     * $id : 1
     * message : null
     * success : true
     * data : [{"$id":"2","PageCount":0,"RowNo":1,"RecordCount":2,"Id":127,"employee_id":3,"client_id":21,"activity_type_id":6,"Dar_message":"Testing","RMName":"Ravi Patel","TimeSpent":20,"TimeSpent_Min":14,"RemarksComment":"Testing","ReportDate":"2019-06-06T11:55:42.327","created_date":"2019-06-07T11:55:42.343","first_name":"Ravi","last_name":"Patel","c_first_name":"mayur","c_last_name":"patel","activity_type_name":"FATCA and KYC for all"},{"$id":"3","PageCount":0,"RowNo":2,"RecordCount":2,"Id":128,"employee_id":3,"client_id":3,"activity_type_id":2,"Dar_message":"Testing","RMName":"Ravi Patel","TimeSpent":3,"TimeSpent_Min":20,"RemarksComment":"Testing","ReportDate":"2019-06-07T11:55:42.327","created_date":"2019-06-07T11:55:42.343","first_name":"Ravi","last_name":"Patel","c_first_name":"Ravi","c_last_name":"Patel","activity_type_name":"Exisiting Meeting"}]
     */

    private String message ="";
    private boolean success = false;
    private List<DataBean> data = new ArrayList<DataBean>();

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {

        private int Id = 0;
        private int employee_id = 0;
        private int client_id = 0;
        private int activity_type_id = 0;
        private String Dar_message = "";
        private String RMName = "";
        private int TimeSpent = 0;
        private int TimeSpent_Min = 0;
        private String RemarksComment = "";
        private String ReportDate = "";
        private String created_date = "";
        private String first_name = "";
        private String last_name = "";
        private String c_first_name = "";
        private String c_last_name = "";
        private String activity_type_name = "";
        private int reportDate_T_Y_D = 1;

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public int getEmployee_id() {
            return employee_id;
        }

        public void setEmployee_id(int employee_id) {
            this.employee_id = employee_id;
        }

        public int getClient_id() {
            return client_id;
        }

        public void setClient_id(int client_id) {
            this.client_id = client_id;
        }

        public int getActivity_type_id() {
            return activity_type_id;
        }

        public void setActivity_type_id(int activity_type_id) {
            this.activity_type_id = activity_type_id;
        }

        public String getDar_message() {
            return Dar_message;
        }

        public void setDar_message(String Dar_message) {
            this.Dar_message = Dar_message;
        }

        public String getRMName() {
            return RMName;
        }

        public void setRMName(String RMName) {
            this.RMName = RMName;
        }

        public int getTimeSpent() {
            return TimeSpent;
        }

        public void setTimeSpent(int TimeSpent) {
            this.TimeSpent = TimeSpent;
        }

        public int getTimeSpent_Min() {
            return TimeSpent_Min;
        }

        public void setTimeSpent_Min(int TimeSpent_Min) {
            this.TimeSpent_Min = TimeSpent_Min;
        }

        public String getRemarksComment() {
            return RemarksComment;
        }

        public void setRemarksComment(String RemarksComment) {
            this.RemarksComment = RemarksComment;
        }

        public String getReportDate() {
            return ReportDate;
        }

        public void setReportDate(String ReportDate) {
            this.ReportDate = ReportDate;
        }

        public String getCreated_date() {
            return created_date;
        }

        public void setCreated_date(String created_date) {
            this.created_date = created_date;
        }

        public String getFirst_name() {
            return first_name;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public void setLast_name(String last_name) {
            this.last_name = last_name;
        }

        public String getC_first_name() {
            return c_first_name;
        }

        public void setC_first_name(String c_first_name) {
            this.c_first_name = c_first_name;
        }

        public String getC_last_name() {
            return c_last_name;
        }

        public void setC_last_name(String c_last_name) {
            this.c_last_name = c_last_name;
        }

        public String getActivity_type_name() {
            return activity_type_name;
        }

        public void setActivity_type_name(String activity_type_name) {
            this.activity_type_name = activity_type_name;
        }

        public int getReportDate_T_Y_D() {
            return reportDate_T_Y_D;
        }

        public void setReportDate_T_Y_D(int reportDate_T_Y_D) {
            this.reportDate_T_Y_D = reportDate_T_Y_D;
        }
    }
}
