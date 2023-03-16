package cheetah.alphacapital.reportApp.getset;

import java.util.ArrayList;
import java.util.List;

public class TargetListResponse
{

    /**
     * $id : 1
     * message : null
     * success : true
     * data : [{"$id":"2","PageCount":0,"RowNo":1,"RecordCount":1,"id":3,"employee_id":3,"first_name":"Ravi","last_name":"Patel","year":2019,"meetings_exisiting":300,"meetings_new":60,"reference_client":60,"fresh_aum":25000000,"sip":2000000,"new_clients_converted":2,"self_acquired_aum":10000000,"Inflow_outflow":null,"summery_mail":null,"day_forward_mail":null,"created_date":"2019-01-16T16:52:32.263"}]
     */

    private String message ="";
    private boolean success =false;
    private List<DataBean> data = new ArrayList<>();

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


        private int id;
        private int employee_id;
        private String first_name;
        private String last_name;
        private int year;
        private long meetings_exisiting;
        private long meetings_new;
        private long reference_client;
        private long fresh_aum;
        private long sip;
        private long new_clients_converted;
        private long self_acquired_aum;
        private long Inflow_outflow;
        private int summery_mail;
        private int day_forward_mail;
        private String created_date;

        public int getId()
        {
            return id;
        }

        public void setId(int id)
        {
            this.id = id;
        }

        public int getEmployee_id()
        {
            return employee_id;
        }

        public void setEmployee_id(int employee_id)
        {
            this.employee_id = employee_id;
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

        public int getYear()
        {
            return year;
        }

        public void setYear(int year)
        {
            this.year = year;
        }

        public long getMeetings_exisiting()
        {
            return meetings_exisiting;
        }

        public void setMeetings_exisiting(long meetings_exisiting)
        {
            this.meetings_exisiting = meetings_exisiting;
        }

        public long getMeetings_new()
        {
            return meetings_new;
        }

        public void setMeetings_new(long meetings_new)
        {
            this.meetings_new = meetings_new;
        }

        public long getReference_client()
        {
            return reference_client;
        }

        public void setReference_client(long reference_client)
        {
            this.reference_client = reference_client;
        }

        public long getFresh_aum()
        {
            return fresh_aum;
        }

        public void setFresh_aum(long fresh_aum)
        {
            this.fresh_aum = fresh_aum;
        }

        public long getSip()
        {
            return sip;
        }

        public void setSip(long sip)
        {
            this.sip = sip;
        }

        public long getNew_clients_converted()
        {
            return new_clients_converted;
        }

        public void setNew_clients_converted(long new_clients_converted)
        {
            this.new_clients_converted = new_clients_converted;
        }

        public long getSelf_acquired_aum()
        {
            return self_acquired_aum;
        }

        public void setSelf_acquired_aum(long self_acquired_aum)
        {
            this.self_acquired_aum = self_acquired_aum;
        }

        public long getInflow_outflow()
        {
            return Inflow_outflow;
        }

        public void setInflow_outflow(long inflow_outflow)
        {
            Inflow_outflow = inflow_outflow;
        }

        public int getSummery_mail()
        {
            return summery_mail;
        }

        public void setSummery_mail(int summery_mail)
        {
            this.summery_mail = summery_mail;
        }

        public int getDay_forward_mail()
        {
            return day_forward_mail;
        }

        public void setDay_forward_mail(int day_forward_mail)
        {
            this.day_forward_mail = day_forward_mail;
        }

        public String getCreated_date()
        {
            return created_date;
        }

        public void setCreated_date(String created_date)
        {
            this.created_date = created_date;
        }
    }
}
