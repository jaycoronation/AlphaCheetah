package cheetah.alphacapital.reportApp.getset;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Ravi Patel on 29-01-2019.
 */
public class EmployeeTargetGetSet implements Parcelable, Serializable
{

    private String id="";
    private String employee_id="";
    private String first_name="";
    private String last_name="";
    private String year="";
    private String meetings_exisiting="";
    private String meetings_new="";
    private String reference_client="";
    private String fresh_aum="";
    private String sip="";
    private String new_clients_converted="";
    private String self_acquired_aum ="";

    public EmployeeTargetGetSet()
    {
    }

    protected EmployeeTargetGetSet(Parcel in)
    {
        id = in.readString();
        employee_id = in.readString();
        first_name = in.readString();
        last_name = in.readString();
        year = in.readString();
        meetings_exisiting = in.readString();
        meetings_new = in.readString();
        reference_client = in.readString();
        fresh_aum = in.readString();
        sip = in.readString();
        new_clients_converted = in.readString();
        self_acquired_aum = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(id);
        dest.writeString(employee_id);
        dest.writeString(first_name);
        dest.writeString(last_name);
        dest.writeString(year);
        dest.writeString(meetings_exisiting);
        dest.writeString(meetings_new);
        dest.writeString(reference_client);
        dest.writeString(fresh_aum);
        dest.writeString(sip);
        dest.writeString(new_clients_converted);
        dest.writeString(self_acquired_aum);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    public static final Creator<EmployeeTargetGetSet> CREATOR = new Creator<EmployeeTargetGetSet>()
    {
        @Override
        public EmployeeTargetGetSet createFromParcel(Parcel in)
        {
            return new EmployeeTargetGetSet(in);
        }

        @Override
        public EmployeeTargetGetSet[] newArray(int size)
        {
            return new EmployeeTargetGetSet[size];
        }
    };

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getEmployee_id()
    {
        return employee_id;
    }

    public void setEmployee_id(String employee_id)
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

    public String getYear()
    {
        return year;
    }

    public void setYear(String year)
    {
        this.year = year;
    }

    public String getMeetings_exisiting()
    {
        return meetings_exisiting;
    }

    public void setMeetings_exisiting(String meetings_exisiting)
    {
        this.meetings_exisiting = meetings_exisiting;
    }

    public String getMeetings_new()
    {
        return meetings_new;
    }

    public void setMeetings_new(String meetings_new)
    {
        this.meetings_new = meetings_new;
    }

    public String getReference_client()
    {
        return reference_client;
    }

    public void setReference_client(String reference_client)
    {
        this.reference_client = reference_client;
    }

    public String getFresh_aum()
    {
        return fresh_aum;
    }

    public void setFresh_aum(String fresh_aum)
    {
        this.fresh_aum = fresh_aum;
    }

    public String getSip()
    {
        return sip;
    }

    public void setSip(String sip)
    {
        this.sip = sip;
    }

    public String getNew_clients_converted()
    {
        return new_clients_converted;
    }

    public void setNew_clients_converted(String new_clients_converted)
    {
        this.new_clients_converted = new_clients_converted;
    }

    public String getSelf_acquired_aum()
    {
        return self_acquired_aum;
    }

    public void setSelf_acquired_aum(String self_acquired_aum)
    {
        this.self_acquired_aum = self_acquired_aum;
    }
}
