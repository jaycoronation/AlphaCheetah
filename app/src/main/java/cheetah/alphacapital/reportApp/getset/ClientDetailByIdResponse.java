package cheetah.alphacapital.reportApp.getset;

import java.util.ArrayList;
import java.util.List;

public class ClientDetailByIdResponse
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

        private ClientDetailBean ClientDetail = new ClientDetailBean();
        private List<NotesBean> Notes = new ArrayList<NotesBean>();

        public ClientDetailBean getClientDetail()
        {
            return ClientDetail;
        }

        public void setClientDetail(ClientDetailBean ClientDetail)
        {
            this.ClientDetail = ClientDetail;
        }

        public List<NotesBean> getNotes()
        {
            return Notes;
        }

        public void setNotes(List<NotesBean> Notes)
        {
            this.Notes = Notes;
        }

        public static class ClientDetailBean
        {
            private int id =0;
            private String first_name ="";
            private String last_name ="";
            private String organization ="";
            private String contact_no ="";
            private String email ="";
            private String address ="";
            private int country_id =0;
            private String country_name ="";
            private int state_id=0;
            private String state_name ="";
            private int city_id=0;
            private String city_name ="";
            private boolean is_approved_by_admin=false;
            private boolean is_active =false;
            private String created_date ="";
            private long TotalMeeting=0;
            private long Month_End_AUM=0;
            private long Inflow_outflow =0;
            private long SIP =0;
            private long TotalPunchingHour = 0;
            private String TotalPunchingHour_new ="";
            private List<LstCollaborativeEmployeeBean> lstCollaborativeEmployee = new ArrayList<LstCollaborativeEmployeeBean>();

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

            public void setTotalMeeting(int TotalMeeting)
            {
                this.TotalMeeting = TotalMeeting;
            }

            public long getMonth_End_AUM()
            {
                return Month_End_AUM;
            }

            public void setMonth_End_AUM(int Month_End_AUM)
            {
                this.Month_End_AUM = Month_End_AUM;
            }

            public long getInflow_outflow()
            {
                return Inflow_outflow;
            }

            public void setInflow_outflow(int Inflow_outflow)
            {
                this.Inflow_outflow = Inflow_outflow;
            }

            public long getSIP()
            {
                return SIP;
            }

            public void setSIP(int SIP)
            {
                this.SIP = SIP;
            }

            public long getTotalPunchingHour()
            {
                return TotalPunchingHour;
            }

            public void setTotalPunchingHour(int TotalPunchingHour)
            {
                this.TotalPunchingHour = TotalPunchingHour;
            }

            public String getTotalPunchingHour_new()
            {
                return TotalPunchingHour_new;
            }

            public void setTotalPunchingHour_new(String TotalPunchingHour_new)
            {
                this.TotalPunchingHour_new = TotalPunchingHour_new;
            }

            public List<LstCollaborativeEmployeeBean> getLstCollaborativeEmployee()
            {
                return lstCollaborativeEmployee;
            }

            public void setLstCollaborativeEmployee(List<LstCollaborativeEmployeeBean> lstCollaborativeEmployee)
            {
                this.lstCollaborativeEmployee = lstCollaborativeEmployee;
            }

            public static class LstCollaborativeEmployeeBean
            {


                private String first_name ="";
                private String last_name ="";

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
            }
        }

        public static class NotesBean
        {
            private int id =0;
            private int client_id =0;
            private int employee_id =0;
            private int employee_id_assign =0;
            private String title  ="";
            private String description ="";
            private String created_date ="";
            private String updated_date ="";
            private String employee_f_name ="";
            private String employee_l_name ="";

            public int getId()
            {
                return id;
            }

            public void setId(int id)
            {
                this.id = id;
            }

            public int getClient_id()
            {
                return client_id;
            }

            public void setClient_id(int client_id)
            {
                this.client_id = client_id;
            }

            public int getEmployee_id()
            {
                return employee_id;
            }

            public void setEmployee_id(int employee_id)
            {
                this.employee_id = employee_id;
            }

            public int getEmployee_id_assign()
            {
                return employee_id_assign;
            }

            public void setEmployee_id_assign(int employee_id_assign)
            {
                this.employee_id_assign = employee_id_assign;
            }

            public String getTitle()
            {
                return title;
            }

            public void setTitle(String title)
            {
                this.title = title;
            }

            public String getDescription()
            {
                return description;
            }

            public void setDescription(String description)
            {
                this.description = description;
            }

            public String getCreated_date()
            {
                return created_date;
            }

            public void setCreated_date(String created_date)
            {
                this.created_date = created_date;
            }

            public String getUpdated_date()
            {
                return updated_date;
            }

            public void setUpdated_date(String updated_date)
            {
                this.updated_date = updated_date;
            }

            public String getEmployee_f_name()
            {
                return employee_f_name;
            }

            public void setEmployee_f_name(String employee_f_name)
            {
                this.employee_f_name = employee_f_name;
            }

            public String getEmployee_l_name()
            {
                return employee_l_name;
            }

            public void setEmployee_l_name(String employee_l_name)
            {
                this.employee_l_name = employee_l_name;
            }
        }
    }
}
