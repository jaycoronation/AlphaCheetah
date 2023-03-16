package cheetah.alphacapital.reportApp.getset;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.ArrayList;

public class DARResponse implements Parcelable, Serializable
{
    private String message ="";
    private boolean success =false;
    private ArrayList<DataBean> data = new ArrayList<DataBean>();

    public DARResponse()
    {
    }

    protected DARResponse(Parcel in)
    {
        message = in.readString();
        success = in.readByte() != 0;
        data = in.createTypedArrayList(DataBean.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(message);
        dest.writeByte((byte) (success ? 1 : 0));
        dest.writeTypedList(data);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    public static final Creator<DARResponse> CREATOR = new Creator<DARResponse>() {
        @Override
        public DARResponse createFromParcel(Parcel in)
        {
            return new DARResponse(in);
        }

        @Override
        public DARResponse[] newArray(int size)
        {
            return new DARResponse[size];
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

    public ArrayList<DataBean> getData()
    {
        return data;
    }

    public void setData(ArrayList<DataBean> data)
    {
        this.data = data;
    }



    public static class DataBean implements Parcelable, Serializable
    {
        private int Id =0;
        private int employee_id=0;
        private int client_id=0;
        private int activity_type_id=0;
        private String Dar_message ="";
        private String RMName="";
        private int TimeSpent=0;
        private int TimeSpent_Min=0;
        private String RemarksComment="";
        private String ReportDate="";
        private String created_date="";
        private String first_name="";
        private String last_name="";
        private String c_first_name="";
        private String c_last_name="";
        private String activity_type_name="";

        public DataBean()
        {
        }

        protected DataBean(Parcel in)
        {
            Id = in.readInt();
            employee_id = in.readInt();
            client_id = in.readInt();
            activity_type_id = in.readInt();
            Dar_message = in.readString();
            RMName = in.readString();
            TimeSpent = in.readInt();
            TimeSpent_Min = in.readInt();
            RemarksComment = in.readString();
            ReportDate = in.readString();
            created_date = in.readString();
            first_name = in.readString();
            last_name = in.readString();
            c_first_name = in.readString();
            c_last_name = in.readString();
            activity_type_name = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags)
        {
            dest.writeInt(Id);
            dest.writeInt(employee_id);
            dest.writeInt(client_id);
            dest.writeInt(activity_type_id);
            dest.writeString(Dar_message);
            dest.writeString(RMName);
            dest.writeInt(TimeSpent);
            dest.writeInt(TimeSpent_Min);
            dest.writeString(RemarksComment);
            dest.writeString(ReportDate);
            dest.writeString(created_date);
            dest.writeString(first_name);
            dest.writeString(last_name);
            dest.writeString(c_first_name);
            dest.writeString(c_last_name);
            dest.writeString(activity_type_name);
        }

        @Override
        public int describeContents()
        {
            return 0;
        }

        public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
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

        public int getTimeSpent_Min()
        {
            return TimeSpent_Min;
        }

        public void setTimeSpent_Min(int TimeSpent_Min)
        {
            this.TimeSpent_Min = TimeSpent_Min;
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
}
