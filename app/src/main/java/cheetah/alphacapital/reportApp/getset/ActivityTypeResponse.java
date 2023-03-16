package cheetah.alphacapital.reportApp.getset;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class ActivityTypeResponse implements Parcelable,Serializable
{
    private String message ="";
    private boolean success =false;
    private ArrayList<DataBean> data = new ArrayList<DataBean>();

    public ActivityTypeResponse()
    {
    }

    protected ActivityTypeResponse(Parcel in)
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

    public static final Creator<ActivityTypeResponse> CREATOR = new Creator<ActivityTypeResponse>()
    {
        @Override
        public ActivityTypeResponse createFromParcel(Parcel in)
        {
            return new ActivityTypeResponse(in);
        }

        @Override
        public ActivityTypeResponse[] newArray(int size)
        {
            return new ActivityTypeResponse[size];
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
        private String ActivityTypeName ="";
        private boolean IsActive =false;
        private String created_date ="";
        private String timetstamp ="";

        public DataBean()
        {
        }

        protected DataBean(Parcel in)
        {
            Id = in.readInt();
            ActivityTypeName = in.readString();
            IsActive = in.readByte() != 0;
            created_date = in.readString();
            timetstamp = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags)
        {
            dest.writeInt(Id);
            dest.writeString(ActivityTypeName);
            dest.writeByte((byte) (IsActive ? 1 : 0));
            dest.writeString(created_date);
            dest.writeString(timetstamp);
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

        public int getId()
        {
            return Id;
        }

        public void setId(int Id)
        {
            this.Id = Id;
        }

        public String getActivityTypeName()
        {
            return ActivityTypeName;
        }

        public void setActivityTypeName(String ActivityTypeName)
        {
            this.ActivityTypeName = ActivityTypeName;
        }

        public boolean isIsActive()
        {
            return IsActive;
        }

        public void setIsActive(boolean IsActive)
        {
            this.IsActive = IsActive;
        }

        public String getCreated_date()
        {
            return created_date;
        }

        public void setCreated_date(String created_date)
        {
            this.created_date = created_date;
        }

        public String getTimetstamp()
        {
            return timetstamp;
        }

        public void setTimetstamp(String timetstamp)
        {
            this.timetstamp = timetstamp;
        }
    }
}
