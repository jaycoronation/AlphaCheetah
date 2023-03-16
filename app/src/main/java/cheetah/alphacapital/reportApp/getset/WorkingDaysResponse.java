package cheetah.alphacapital.reportApp.getset;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WorkingDaysResponse implements Parcelable,Serializable
{
    private String message ="";
    private boolean success =false;
    private List<DataBean> data = new ArrayList<DataBean>();

    public WorkingDaysResponse()
    {
    }

    protected WorkingDaysResponse(Parcel in)
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

    public static final Creator<WorkingDaysResponse> CREATOR = new Creator<WorkingDaysResponse>()
    {
        @Override
        public WorkingDaysResponse createFromParcel(Parcel in)
        {
            return new WorkingDaysResponse(in);
        }

        @Override
        public WorkingDaysResponse[] newArray(int size)
        {
            return new WorkingDaysResponse[size];
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

    public List<DataBean> getData()
    {
        return data;
    }

    public void setData(List<DataBean> data)
    {
        this.data = data;
    }

    public static class DataBean implements Parcelable, Serializable
    {
        private int id =0;
        private int year =0;
        private int month = 0;
        private int working_days =0;
        private boolean is_deleted =false;
        private String added_date ="";
        private String timestamp ="";


        public DataBean()
        {
        }

        protected DataBean(Parcel in)
        {
            id = in.readInt();
            year = in.readInt();
            month = in.readInt();
            working_days = in.readInt();
            is_deleted = in.readByte() != 0;
            added_date = in.readString();
            timestamp = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags)
        {
            dest.writeInt(id);
            dest.writeInt(year);
            dest.writeInt(month);
            dest.writeInt(working_days);
            dest.writeByte((byte) (is_deleted ? 1 : 0));
            dest.writeString(added_date);
            dest.writeString(timestamp);
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
            return id;
        }

        public void setId(int id)
        {
            this.id = id;
        }

        public int getYear()
        {
            return year;
        }

        public void setYear(int year)
        {
            this.year = year;
        }

        public int getMonth()
        {
            return month;
        }

        public void setMonth(int month)
        {
            this.month = month;
        }

        public int getWorking_days()
        {
            return working_days;
        }

        public void setWorking_days(int working_days)
        {
            this.working_days = working_days;
        }

        public boolean isIs_deleted()
        {
            return is_deleted;
        }

        public void setIs_deleted(boolean is_deleted)
        {
            this.is_deleted = is_deleted;
        }

        public String getAdded_date()
        {
            return added_date;
        }

        public void setAdded_date(String added_date)
        {
            this.added_date = added_date;
        }

        public String getTimestamp()
        {
            return timestamp;
        }

        public void setTimestamp(String timestamp)
        {
            this.timestamp = timestamp;
        }
    }
}
