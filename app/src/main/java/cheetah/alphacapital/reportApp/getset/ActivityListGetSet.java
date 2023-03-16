package cheetah.alphacapital.reportApp.getset;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Ravi Patel on 29-01-2019.
 */
public class ActivityListGetSet implements Parcelable,Serializable
{

    /**
     * id : 56
     * employee_id : 24
     * client_id : 2
     * scheme_id : 1
     * activity_status_id : 2
     * activity_message : Test Ravi Activity with due date
     * due_date : 2019-02-22T03:50:00
     * created_date : 2019-02-07T13:00:39.267
     * activity_added_by : Ravi Patel
     * client_name : Pratik Kalariya
     * scheme_name : ICICI Prudential Balanced Advantage Fund
     * activity_status : completed
     */

    private int id =0;
    private int employee_id =0;
    private int client_id =0;
    private int scheme_id =0;
    private int activity_status_id =0;
    private String activity_message ="";
    private String due_date="";
    private String created_date="";
    private String activity_added_by="";
    private String client_name="";
    private String scheme_name="";
    private int activity_type_id;
    private String activity_status="",activity_hours="",activity_min ="",all_employee_ids="",all_employee_name="",activityTypeName ="";

    public ActivityListGetSet()
    {

    }


    protected ActivityListGetSet(Parcel in)
    {
        id = in.readInt();
        employee_id = in.readInt();
        client_id = in.readInt();
        scheme_id = in.readInt();
        activity_status_id = in.readInt();
        activity_message = in.readString();
        due_date = in.readString();
        created_date = in.readString();
        activity_added_by = in.readString();
        client_name = in.readString();
        scheme_name = in.readString();
        activity_type_id = in.readInt();
        activity_status = in.readString();
        activity_hours = in.readString();
        activity_min = in.readString();
        all_employee_ids = in.readString();
        all_employee_name = in.readString();
        activityTypeName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(id);
        dest.writeInt(employee_id);
        dest.writeInt(client_id);
        dest.writeInt(scheme_id);
        dest.writeInt(activity_status_id);
        dest.writeString(activity_message);
        dest.writeString(due_date);
        dest.writeString(created_date);
        dest.writeString(activity_added_by);
        dest.writeString(client_name);
        dest.writeString(scheme_name);
        dest.writeInt(activity_type_id);
        dest.writeString(activity_status);
        dest.writeString(activity_hours);
        dest.writeString(activity_min);
        dest.writeString(all_employee_ids);
        dest.writeString(all_employee_name);
        dest.writeString(activityTypeName);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    public static final Creator<ActivityListGetSet> CREATOR = new Creator<ActivityListGetSet>()
    {
        @Override
        public ActivityListGetSet createFromParcel(Parcel in)
        {
            return new ActivityListGetSet(in);
        }

        @Override
        public ActivityListGetSet[] newArray(int size)
        {
            return new ActivityListGetSet[size];
        }
    };

    public String getActivity_min()
    {
        return activity_min;
    }

    public void setActivity_min(String activity_min)
    {
        this.activity_min = activity_min;
    }

    public int getActivity_type_id()
    {
        return activity_type_id;
    }

    public void setActivity_type_id(int activity_type_id)
    {
        this.activity_type_id = activity_type_id;
    }

    public String getActivityTypeName()
    {
        return activityTypeName;
    }

    public void setActivityTypeName(String activityTypeName)
    {
        this.activityTypeName = activityTypeName;
    }

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

    public int getClient_id()
    {
        return client_id;
    }

    public void setClient_id(int client_id)
    {
        this.client_id = client_id;
    }

    public int getScheme_id()
    {
        return scheme_id;
    }

    public void setScheme_id(int scheme_id)
    {
        this.scheme_id = scheme_id;
    }

    public int getActivity_status_id()
    {
        return activity_status_id;
    }

    public void setActivity_status_id(int activity_status_id)
    {
        this.activity_status_id = activity_status_id;
    }

    public String getActivity_message()
    {
        return activity_message;
    }

    public void setActivity_message(String activity_message)
    {
        this.activity_message = activity_message;
    }

    public String getDue_date()
    {
        return due_date;
    }

    public void setDue_date(String due_date)
    {
        this.due_date = due_date;
    }

    public String getCreated_date()
    {
        return created_date;
    }

    public void setCreated_date(String created_date)
    {
        this.created_date = created_date;
    }

    public String getActivity_added_by()
    {
        return activity_added_by;
    }

    public void setActivity_added_by(String activity_added_by)
    {
        this.activity_added_by = activity_added_by;
    }

    public String getClient_name()
    {
        return client_name;
    }

    public void setClient_name(String client_name)
    {
        this.client_name = client_name;
    }

    public String getScheme_name()
    {
        return scheme_name;
    }

    public void setScheme_name(String scheme_name)
    {
        this.scheme_name = scheme_name;
    }

    public String getActivity_status()
    {
        return activity_status;
    }

    public void setActivity_status(String activity_status)
    {
        this.activity_status = activity_status;
    }

    public String getActivity_hours()
    {
        return activity_hours;
    }

    public void setActivity_hours(String activity_hours)
    {
        this.activity_hours = activity_hours;
    }

    public String getAll_employee_ids()
    {
        return all_employee_ids;
    }

    public void setAll_employee_ids(String all_employee_ids)
    {
        this.all_employee_ids = all_employee_ids;
    }

    public String getAll_employee_name()
    {
        return all_employee_name;
    }

    public void setAll_employee_name(String all_employee_name)
    {
        this.all_employee_name = all_employee_name;
    }
}
