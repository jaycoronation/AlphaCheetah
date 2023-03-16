package cheetah.alphacapital.reportApp.getset;

import java.util.ArrayList;
import java.util.List;

public class DARByEmployeeResponse {
    

    private String message ="";
    private boolean success =false;
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

        private int PageCount;
        private int RowNo;
        private int RecordCount;
        private int Id;
        private int employee_id;
        private int client_id;
        private int activity_type_id;
        private String Dar_message;
        private String RMName;
        private int TimeSpent;
        private int TimeSpent_Min;
        private String RemarksComment;
        private String ReportDate;
        private String created_date;
        private String first_name;
        private String last_name;
        private String c_first_name;
        private String c_last_name;
        private String activity_type_name;

        public int getPageCount() {
            return PageCount;
        }

        public void setPageCount(int PageCount) {
            this.PageCount = PageCount;
        }

        public int getRowNo() {
            return RowNo;
        }

        public void setRowNo(int RowNo) {
            this.RowNo = RowNo;
        }

        public int getRecordCount() {
            return RecordCount;
        }

        public void setRecordCount(int RecordCount) {
            this.RecordCount = RecordCount;
        }

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
    }
}
