package cheetah.alphacapital.reportApp.getset;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Ravi Patel on 29-01-2019.
 */
public class AssignedClientGetSetOld implements Parcelable, Serializable
{
    private int id = 0;
    private String first_name = "";
    private String last_name = "";
    private String fullname="";
    private String name ="";
    private String organization = "";
    private String contact_no = "";
    private String email = "",password ="";
    private String address = "";
    private int country_id = 0;
    private String country_name = "";
    private int state_id = 0;
    private String state_name = "";
    private int city_id = 0;
    private String city_name = "";
    private boolean is_approved_by_admin = false;
    private boolean is_active = false, is_selected = false,is_admin =false;
    private String created_date = "",employeeName="",TotalPunchingHour_new="";
    private long totalMeeting = 0, AUM = 0, SIP = 0,Month_End_AUM=0,Inflow_outflow=0;
    private int TotalPunchingHour = 0;
    private ArrayList<CommonGetSet> listCollaborativeEmployee = new ArrayList<CommonGetSet>();

    public AssignedClientGetSetOld()
    {
    }

    protected AssignedClientGetSetOld(Parcel in)
    {
        id = in.readInt();
        first_name = in.readString();
        last_name = in.readString();
        fullname = in.readString();
        name = in.readString();
        organization = in.readString();
        contact_no = in.readString();
        email = in.readString();
        password = in.readString();
        address = in.readString();
        country_id = in.readInt();
        country_name = in.readString();
        state_id = in.readInt();
        state_name = in.readString();
        city_id = in.readInt();
        city_name = in.readString();
        is_approved_by_admin = in.readByte() != 0;
        is_active = in.readByte() != 0;
        is_selected = in.readByte() != 0;
        is_admin = in.readByte() != 0;
        created_date = in.readString();
        employeeName = in.readString();
        TotalPunchingHour_new = in.readString();
        totalMeeting = in.readLong();
        AUM = in.readLong();
        SIP = in.readLong();
        Month_End_AUM = in.readLong();
        Inflow_outflow = in.readLong();
        TotalPunchingHour = in.readInt();
        listCollaborativeEmployee = in.createTypedArrayList(CommonGetSet.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(id);
        dest.writeString(first_name);
        dest.writeString(last_name);
        dest.writeString(fullname);
        dest.writeString(name);
        dest.writeString(organization);
        dest.writeString(contact_no);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(address);
        dest.writeInt(country_id);
        dest.writeString(country_name);
        dest.writeInt(state_id);
        dest.writeString(state_name);
        dest.writeInt(city_id);
        dest.writeString(city_name);
        dest.writeByte((byte) (is_approved_by_admin ? 1 : 0));
        dest.writeByte((byte) (is_active ? 1 : 0));
        dest.writeByte((byte) (is_selected ? 1 : 0));
        dest.writeByte((byte) (is_admin ? 1 : 0));
        dest.writeString(created_date);
        dest.writeString(employeeName);
        dest.writeString(TotalPunchingHour_new);
        dest.writeLong(totalMeeting);
        dest.writeLong(AUM);
        dest.writeLong(SIP);
        dest.writeLong(Month_End_AUM);
        dest.writeLong(Inflow_outflow);
        dest.writeInt(TotalPunchingHour);
        dest.writeTypedList(listCollaborativeEmployee);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    public static final Creator<AssignedClientGetSetOld> CREATOR = new Creator<AssignedClientGetSetOld>() {
        @Override
        public AssignedClientGetSetOld createFromParcel(Parcel in)
        {
            return new AssignedClientGetSetOld(in);
        }

        @Override
        public AssignedClientGetSetOld[] newArray(int size)
        {
            return new AssignedClientGetSetOld[size];
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

    public String getFullname()
    {
        return fullname;
    }

    public void setFullname(String fullname)
    {
        this.fullname = fullname;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
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

    public boolean isIs_selected()
    {
        return is_selected;
    }

    public void setIs_selected(boolean is_selected)
    {
        this.is_selected = is_selected;
    }

    public boolean isIs_admin()
    {
        return is_admin;
    }

    public void setIs_admin(boolean is_admin)
    {
        this.is_admin = is_admin;
    }

    public String getCreated_date()
    {
        return created_date;
    }

    public void setCreated_date(String created_date)
    {
        this.created_date = created_date;
    }

    public String getEmployeeName()
    {
        return employeeName;
    }

    public void setEmployeeName(String employeeName)
    {
        this.employeeName = employeeName;
    }

    public String getTotalPunchingHour_new()
    {
        return TotalPunchingHour_new;
    }

    public void setTotalPunchingHour_new(String totalPunchingHour_new)
    {
        TotalPunchingHour_new = totalPunchingHour_new;
    }

    public long getTotalMeeting()
    {
        return totalMeeting;
    }

    public void setTotalMeeting(long totalMeeting)
    {
        this.totalMeeting = totalMeeting;
    }

    public long getAUM()
    {
        return AUM;
    }

    public void setAUM(long AUM)
    {
        this.AUM = AUM;
    }

    public long getSIP()
    {
        return SIP;
    }

    public void setSIP(long SIP)
    {
        this.SIP = SIP;
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

    public int getTotalPunchingHour()
    {
        return TotalPunchingHour;
    }

    public void setTotalPunchingHour(int totalPunchingHour)
    {
        TotalPunchingHour = totalPunchingHour;
    }

    public ArrayList<CommonGetSet> getListCollaborativeEmployee()
    {
        return listCollaborativeEmployee;
    }

    public void setListCollaborativeEmployee(ArrayList<CommonGetSet> listCollaborativeEmployee)
    {
        this.listCollaborativeEmployee = listCollaborativeEmployee;
    }
}
