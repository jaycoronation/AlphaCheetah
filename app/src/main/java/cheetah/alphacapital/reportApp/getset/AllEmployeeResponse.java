package cheetah.alphacapital.reportApp.getset;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class AllEmployeeResponse implements Parcelable,Serializable
{
    private String message ="";
    private boolean success =false;
    private DataBean data = new DataBean();

    public AllEmployeeResponse()
    {
    }

    protected AllEmployeeResponse(Parcel in)
    {
        message = in.readString();
        success = in.readByte() != 0;
        data = in.readParcelable(DataBean.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(message);
        dest.writeByte((byte) (success ? 1 : 0));
        dest.writeParcelable(data, flags);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    public static final Creator<AllEmployeeResponse> CREATOR = new Creator<AllEmployeeResponse>()
    {
        @Override
        public AllEmployeeResponse createFromParcel(Parcel in)
        {
            return new AllEmployeeResponse(in);
        }

        @Override
        public AllEmployeeResponse[] newArray(int size)
        {
            return new AllEmployeeResponse[size];
        }
    };

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

    public static class DataBean implements Parcelable,Serializable
    {
       
        private ArrayList<AllEmployeeBean> AllEmployee = new ArrayList<AllEmployeeBean>();
        private ArrayList<ClientAssignedEmployeeBean> ClientAssignedEmployee = new ArrayList<ClientAssignedEmployeeBean>();

        public DataBean()
        {
        }

        protected DataBean(Parcel in)
        {
            AllEmployee = in.createTypedArrayList(AllEmployeeBean.CREATOR);
            ClientAssignedEmployee = in.createTypedArrayList(ClientAssignedEmployeeBean.CREATOR);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags)
        {
            dest.writeTypedList(AllEmployee);
            dest.writeTypedList(ClientAssignedEmployee);
        }

        @Override
        public int describeContents()
        {
            return 0;
        }

        public static final Creator<DataBean> CREATOR = new Creator<DataBean>()
        {
            @Override
            public DataBean createFromParcel(Parcel in)
            {
                return new DataBean(in);
            }

            @Override
            public DataBean[] newArray(int size)
            {
                return new DataBean[size];
            }
        };

        public ArrayList<AllEmployeeBean> getAllEmployee()
        {
            return AllEmployee;
        }

        public void setAllEmployee(ArrayList<AllEmployeeBean> AllEmployee)
        {
            this.AllEmployee = AllEmployee;
        }

        public ArrayList<ClientAssignedEmployeeBean> getClientAssignedEmployee()
        {
            return ClientAssignedEmployee;
        }

        public void setClientAssignedEmployee(ArrayList<ClientAssignedEmployeeBean> ClientAssignedEmployee)
        {
            this.ClientAssignedEmployee = ClientAssignedEmployee;
        }

        public static class AllEmployeeBean implements Parcelable,Serializable
        {
            private int id =0;
            private String first_name ="";
            private String last_name="";
            private String fullname="";
            private String contact_no="";
            private String emp_type="";
            private String email="";
            private String password="";
            private String address="";
            private int country_id=0;
            private String country_name="";
            private int state_id =0;
            private String state_name="";
            private int city_id=0;
            private String city_name="";
            private boolean is_admin =false;
            private boolean is_active =false;
            private boolean is_primary =false;
            private String created_date="";
            private int today_activity_count =0;
            private int yesterday_activity_count =0;
            private int day_before_yesterday_activity_count =0;
            private boolean is_selected = false;

            public AllEmployeeBean()
            {
            }

            protected AllEmployeeBean(Parcel in)
            {
                id = in.readInt();
                first_name = in.readString();
                last_name = in.readString();
                fullname = in.readString();
                contact_no = in.readString();
                emp_type = in.readString();
                email = in.readString();
                password = in.readString();
                address = in.readString();
                country_id = in.readInt();
                country_name = in.readString();
                state_id = in.readInt();
                state_name = in.readString();
                city_id = in.readInt();
                city_name = in.readString();
                is_admin = in.readByte() != 0;
                is_active = in.readByte() != 0;
                is_primary = in.readByte() != 0;
                created_date = in.readString();
                today_activity_count = in.readInt();
                yesterday_activity_count = in.readInt();
                day_before_yesterday_activity_count = in.readInt();
                is_selected = in.readByte() != 0;
            }

            public boolean isIs_primary() {
                return is_primary;
            }

            public void setIs_primary(boolean is_primary) {
                this.is_primary = is_primary;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags)
            {
                dest.writeInt(id);
                dest.writeString(first_name);
                dest.writeString(last_name);
                dest.writeString(fullname);
                dest.writeString(contact_no);
                dest.writeString(emp_type);
                dest.writeString(email);
                dest.writeString(password);
                dest.writeString(address);
                dest.writeInt(country_id);
                dest.writeString(country_name);
                dest.writeInt(state_id);
                dest.writeString(state_name);
                dest.writeInt(city_id);
                dest.writeString(city_name);
                dest.writeByte((byte) (is_admin ? 1 : 0));
                dest.writeByte((byte) (is_active ? 1 : 0));
                dest.writeByte((byte) (is_primary ? 1 : 0));
                dest.writeString(created_date);
                dest.writeInt(today_activity_count);
                dest.writeInt(yesterday_activity_count);
                dest.writeInt(day_before_yesterday_activity_count);
                dest.writeByte((byte) (is_selected ? 1 : 0));
            }

            @Override
            public int describeContents()
            {
                return 0;
            }

            public static final Creator<AllEmployeeBean> CREATOR = new Creator<AllEmployeeBean>()
            {
                @Override
                public AllEmployeeBean createFromParcel(Parcel in)
                {
                    return new AllEmployeeBean(in);
                }

                @Override
                public AllEmployeeBean[] newArray(int size)
                {
                    return new AllEmployeeBean[size];
                }
            };

            public String getFullname()
            {
                return fullname;
            }

            public void setFullname(String fullname)
            {
                this.fullname = fullname;
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

            public String getContact_no()
            {
                return contact_no;
            }

            public void setContact_no(String contact_no)
            {
                this.contact_no = contact_no;
            }

            public String getEmp_type()
            {
                return emp_type;
            }

            public void setEmp_type(String emp_type)
            {
                this.emp_type = emp_type;
            }

            public String getEmail()
            {
                return email;
            }

            public void setEmail(String email)
            {
                this.email = email;
            }

            public String getPassword()
            {
                return password;
            }

            public void setPassword(String password)
            {
                this.password = password;
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

            public boolean isIs_admin()
            {
                return is_admin;
            }

            public void setIs_admin(boolean is_admin)
            {
                this.is_admin = is_admin;
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

            public int getToday_activity_count()
            {
                return today_activity_count;
            }

            public void setToday_activity_count(int today_activity_count)
            {
                this.today_activity_count = today_activity_count;
            }

            public int getYesterday_activity_count()
            {
                return yesterday_activity_count;
            }

            public void setYesterday_activity_count(int yesterday_activity_count)
            {
                this.yesterday_activity_count = yesterday_activity_count;
            }

            public int getDay_before_yesterday_activity_count()
            {
                return day_before_yesterday_activity_count;
            }

            public void setDay_before_yesterday_activity_count(int day_before_yesterday_activity_count)
            {
                this.day_before_yesterday_activity_count = day_before_yesterday_activity_count;
            }

            public boolean isIs_selected()
            {
                return is_selected;
            }

            public void setIs_selected(boolean is_selected)
            {
                this.is_selected = is_selected;
            }
        }

        public static class ClientAssignedEmployeeBean implements Parcelable, Serializable
        {
            private int id =0;
            private String first_name="";
            private String last_name="";
            private String contact_no="";
            private String email="";
            private String address="";
            private int country_id =0;
            private String country_name="";
            private int state_id=0;
            private String state_name="";
            private int city_id =0;
            private String city_name ="";
            private boolean is_active =false;
            private boolean is_primary =false;
            private String created_date="";

            public ClientAssignedEmployeeBean()
            {
            }

            protected ClientAssignedEmployeeBean(Parcel in)
            {
                id = in.readInt();
                first_name = in.readString();
                last_name = in.readString();
                contact_no = in.readString();
                email = in.readString();
                address = in.readString();
                country_id = in.readInt();
                country_name = in.readString();
                state_id = in.readInt();
                state_name = in.readString();
                city_id = in.readInt();
                city_name = in.readString();
                is_active = in.readByte() != 0;
                is_primary = in.readByte() != 0;
                created_date = in.readString();
            }

            @Override
            public void writeToParcel(Parcel dest, int flags)
            {
                dest.writeInt(id);
                dest.writeString(first_name);
                dest.writeString(last_name);
                dest.writeString(contact_no);
                dest.writeString(email);
                dest.writeString(address);
                dest.writeInt(country_id);
                dest.writeString(country_name);
                dest.writeInt(state_id);
                dest.writeString(state_name);
                dest.writeInt(city_id);
                dest.writeString(city_name);
                dest.writeByte((byte) (is_active ? 1 : 0));
                dest.writeByte((byte) (is_primary ? 1 : 0));
                dest.writeString(created_date);
            }

            @Override
            public int describeContents()
            {
                return 0;
            }

            public static final Creator<ClientAssignedEmployeeBean> CREATOR = new Creator<ClientAssignedEmployeeBean>()
            {
                @Override
                public ClientAssignedEmployeeBean createFromParcel(Parcel in)
                {
                    return new ClientAssignedEmployeeBean(in);
                }

                @Override
                public ClientAssignedEmployeeBean[] newArray(int size)
                {
                    return new ClientAssignedEmployeeBean[size];
                }
            };

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

            public boolean isIs_primary() {
                return is_primary;
            }

            public void setIs_primary(boolean is_primary) {
                this.is_primary = is_primary;
            }

            public String getCity_name()
            {
                return city_name;
            }

            public void setCity_name(String city_name)
            {
                this.city_name = city_name;
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
        }
    }
}
