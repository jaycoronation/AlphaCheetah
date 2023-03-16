package cheetah.alphacapital.reportApp.getset;

import java.util.ArrayList;
import java.util.ArrayList;

public class NewClientListResponse
{

    /**
     * $id : 1
     * message : null
     * success : true
     * data : {"$id":"2","AllClientByEmployee":[{"$id":"3","RowNo":1,"RecordCount":626,"id":31,"first_name":"Anurag","last_name":"Khandelia","organization":"Self Emplyoed","contact_no":"9971258822","email":"khandeliaanurag@gmail.com","address":"A-3803 OBEROI EXQUISITE GOREGAON MUMBAI-400063","country_id":1,"country_name":"India","state_id":22,"state_name":"Maharashtra","city_id":2707,"city_name":"Mumbai","is_approved_by_admin":false,"is_active":true,"created_date":"2019-07-29T16:22:09.107","TotalMeeting":0,"Month_End_AUM":0,"Inflow_outflow":0,"SIP":0,"TotalPunchingHour":0,"TotalPunchingHour_new":"0.0"},{"$id":"13","RowNo":2,"RecordCount":626,"id":32,"first_name":"Aman","last_name":"Singhal","organization":"Nomura","contact_no":"9820495372","email":"aman.iima@gmail.com","address":"E, 1402 Ashok Gardens Tokershi Jivraj Road Sewree Mumbai-400015 Maharashtra India","country_id":1,"country_name":"India","state_id":22,"state_name":"Maharashtra","city_id":2707,"city_name":"Mumbai","is_approved_by_admin":true,"is_active":true,"created_date":"2019-07-29T17:07:19.717","TotalMeeting":0,"Month_End_AUM":0,"Inflow_outflow":0,"SIP":0,"TotalPunchingHour":0,"TotalPunchingHour_new":"0.0"}]}
     */

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


        private ArrayList<AllClientByEmployeeBean> AllClientByEmployee = new ArrayList<AllClientByEmployeeBean>();

        public ArrayList<AllClientByEmployeeBean> getAllClientByEmployee() {
            return AllClientByEmployee;
        }

        public void setAllClientByEmployee(ArrayList<AllClientByEmployeeBean> AllClientByEmployee) {
            this.AllClientByEmployee = AllClientByEmployee;
        }

        public static class AllClientByEmployeeBean {

            private int RowNo = 0;
            private int RecordCount =0;
            private int id =0;
            private String first_name ="";
            private String last_name="";
            private String organization="";
            private String contact_no="";
            private String email="";
            private String address="";
            private int country_id =0;
            private String country_name ="";
            private int state_id =0;
            private String state_name="";
            private int city_id =0;
            private String city_name="";
            private boolean is_approved_by_admin =false;
            private boolean is_active =false;
            private String created_date ="";
            private long TotalMeeting =0;
            private long Month_End_AUM =0;
            private long Inflow_outflow =0;
            private long SIP =0;
            private long TotalPunchingHour =0;
            private String TotalPunchingHour_new ="";
            private boolean isSelected =false;

            public int getRowNo()
            {
                return RowNo;
            }

            public void setRowNo(int rowNo)
            {
                RowNo = rowNo;
            }

            public int getRecordCount()
            {
                return RecordCount;
            }

            public void setRecordCount(int recordCount)
            {
                RecordCount = recordCount;
            }

            public int getId()
            {
                return id;
            }

            public void setId(int id)
            {
                this.id = id;
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

            public String getOrganization()
            {
                return organization;
            }

            public void setOrganization(String organization)
            {
                this.organization = organization;
            }

            public String getContact_no()
            {
                return contact_no;
            }

            public void setContact_no(String contact_no)
            {
                this.contact_no = contact_no;
            }

            public String getEmail()
            {
                return email;
            }

            public void setEmail(String email)
            {
                this.email = email;
            }

            public String getAddress()
            {
                return address;
            }

            public void setAddress(String address)
            {
                this.address = address;
            }

            public int getCountry_id()
            {
                return country_id;
            }

            public void setCountry_id(int country_id)
            {
                this.country_id = country_id;
            }

            public String getCountry_name()
            {
                return country_name;
            }

            public void setCountry_name(String country_name)
            {
                this.country_name = country_name;
            }

            public int getState_id()
            {
                return state_id;
            }

            public void setState_id(int state_id)
            {
                this.state_id = state_id;
            }

            public String getState_name()
            {
                return state_name;
            }

            public void setState_name(String state_name)
            {
                this.state_name = state_name;
            }

            public int getCity_id()
            {
                return city_id;
            }

            public void setCity_id(int city_id)
            {
                this.city_id = city_id;
            }

            public String getCity_name()
            {
                return city_name;
            }

            public void setCity_name(String city_name)
            {
                this.city_name = city_name;
            }

            public boolean isIs_approved_by_admin()
            {
                return is_approved_by_admin;
            }

            public void setIs_approved_by_admin(boolean is_approved_by_admin)
            {
                this.is_approved_by_admin = is_approved_by_admin;
            }

            public boolean isIs_active()
            {
                return is_active;
            }

            public void setIs_active(boolean is_active)
            {
                this.is_active = is_active;
            }

            public String getCreated_date()
            {
                return created_date;
            }

            public void setCreated_date(String created_date)
            {
                this.created_date = created_date;
            }

            public long getTotalMeeting()
            {
                return TotalMeeting;
            }

            public void setTotalMeeting(long totalMeeting)
            {
                TotalMeeting = totalMeeting;
            }

            public long getMonth_End_AUM()
            {
                return Month_End_AUM;
            }

            public void setMonth_End_AUM(long month_End_AUM)
            {
                Month_End_AUM = month_End_AUM;
            }

            public long getInflow_outflow()
            {
                return Inflow_outflow;
            }

            public void setInflow_outflow(long inflow_outflow)
            {
                Inflow_outflow = inflow_outflow;
            }

            public long getSIP()
            {
                return SIP;
            }

            public void setSIP(long SIP)
            {
                this.SIP = SIP;
            }

            public long getTotalPunchingHour()
            {
                return TotalPunchingHour;
            }

            public void setTotalPunchingHour(long totalPunchingHour)
            {
                TotalPunchingHour = totalPunchingHour;
            }

            public String getTotalPunchingHour_new()
            {
                return TotalPunchingHour_new;
            }

            public void setTotalPunchingHour_new(String totalPunchingHour_new)
            {
                TotalPunchingHour_new = totalPunchingHour_new;
            }

            public boolean isSelected()
            {
                return isSelected;
            }

            public void setSelected(boolean selected)
            {
                isSelected = selected;
            }
        }
    }
}
