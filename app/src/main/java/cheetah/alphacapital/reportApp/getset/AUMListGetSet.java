package cheetah.alphacapital.reportApp.getset;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Ravi Patel on 23-02-2019.
 */
public class AUMListGetSet implements Parcelable, Serializable
{
    private String message = "";
    private boolean success = false;
    private ArrayList<DataBean> data = new ArrayList<DataBean>();

    public AUMListGetSet()
    {
    }

    protected AUMListGetSet(Parcel in)
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

    public static final Creator<AUMListGetSet> CREATOR = new Creator<AUMListGetSet>()
    {
        @Override
        public AUMListGetSet createFromParcel(Parcel in)
        {
            return new AUMListGetSet(in);
        }

        @Override
        public AUMListGetSet[] newArray(int size)
        {
            return new AUMListGetSet[size];
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
        private int id = 0;
        private int employee_id = 0;
        private int client_id = 0;
        private long Meeting = 0;
        private long New_Meeting = 0;
        private long Existing_Meeting = 0;
        private long Inflow_outflow = 0;
        private long SIP = 0;
        private long ClientReferences = 0;
        private long Month_End_AUM = 0;
        private int AUM_Month = 0;
        private int AUM_Year = 0;
        private String created_date = "";
        private String emp_f_name = "";
        private String emp_l_name = "";
        private String client_f_name = "";
        private String client_l_name = "";
        private long summery_mail = 0;
        private long day_forward_mail = 0;
        private long NewClientConverted = 0;
        private long SelfAcquiredAUM = 0;
        private long DAR = 0;

        public DataBean()
        {
        }

        protected DataBean(Parcel in)
        {
            id = in.readInt();
            employee_id = in.readInt();
            client_id = in.readInt();
            Meeting = in.readLong();
            New_Meeting = in.readLong();
            Existing_Meeting = in.readLong();
            Inflow_outflow = in.readLong();
            SIP = in.readLong();
            ClientReferences = in.readLong();
            Month_End_AUM = in.readLong();
            AUM_Month = in.readInt();
            AUM_Year = in.readInt();
            created_date = in.readString();
            emp_f_name = in.readString();
            emp_l_name = in.readString();
            client_f_name = in.readString();
            client_l_name = in.readString();
            summery_mail = in.readLong();
            day_forward_mail = in.readLong();
            NewClientConverted = in.readLong();
            SelfAcquiredAUM = in.readLong();
            DAR = in.readLong();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags)
        {
            dest.writeInt(id);
            dest.writeInt(employee_id);
            dest.writeInt(client_id);
            dest.writeLong(Meeting);
            dest.writeLong(New_Meeting);
            dest.writeLong(Existing_Meeting);
            dest.writeLong(Inflow_outflow);
            dest.writeLong(SIP);
            dest.writeLong(ClientReferences);
            dest.writeLong(Month_End_AUM);
            dest.writeInt(AUM_Month);
            dest.writeInt(AUM_Year);
            dest.writeString(created_date);
            dest.writeString(emp_f_name);
            dest.writeString(emp_l_name);
            dest.writeString(client_f_name);
            dest.writeString(client_l_name);
            dest.writeLong(summery_mail);
            dest.writeLong(day_forward_mail);
            dest.writeLong(NewClientConverted);
            dest.writeLong(SelfAcquiredAUM);
            dest.writeLong(DAR);
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

        public long getMeeting()
        {
            return Meeting;
        }

        public void setMeeting(long meeting)
        {
            Meeting = meeting;
        }

        public long getNew_Meeting()
        {
            return New_Meeting;
        }

        public void setNew_Meeting(long new_Meeting)
        {
            New_Meeting = new_Meeting;
        }

        public long getExisting_Meeting()
        {
            return Existing_Meeting;
        }

        public void setExisting_Meeting(long existing_Meeting)
        {
            Existing_Meeting = existing_Meeting;
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

        public long getClientReferences()
        {
            return ClientReferences;
        }

        public void setClientReferences(long clientReferences)
        {
            ClientReferences = clientReferences;
        }

        public long getMonth_End_AUM()
        {
            return Month_End_AUM;
        }

        public void setMonth_End_AUM(long month_End_AUM)
        {
            Month_End_AUM = month_End_AUM;
        }

        public int getAUM_Month()
        {
            return AUM_Month;
        }

        public void setAUM_Month(int AUM_Month)
        {
            this.AUM_Month = AUM_Month;
        }

        public int getAUM_Year()
        {
            return AUM_Year;
        }

        public void setAUM_Year(int AUM_Year)
        {
            this.AUM_Year = AUM_Year;
        }

        public String getCreated_date()
        {
            return created_date;
        }

        public void setCreated_date(String created_date)
        {
            this.created_date = created_date;
        }

        public String getEmp_f_name()
        {
            return emp_f_name;
        }

        public void setEmp_f_name(String emp_f_name)
        {
            this.emp_f_name = emp_f_name;
        }

        public String getEmp_l_name()
        {
            return emp_l_name;
        }

        public void setEmp_l_name(String emp_l_name)
        {
            this.emp_l_name = emp_l_name;
        }

        public String getClient_f_name()
        {
            return client_f_name;
        }

        public void setClient_f_name(String client_f_name)
        {
            this.client_f_name = client_f_name;
        }

        public String getClient_l_name()
        {
            return client_l_name;
        }

        public void setClient_l_name(String client_l_name)
        {
            this.client_l_name = client_l_name;
        }

        public long getSummery_mail()
        {
            return summery_mail;
        }

        public void setSummery_mail(long summery_mail)
        {
            this.summery_mail = summery_mail;
        }

        public long getDay_forward_mail()
        {
            return day_forward_mail;
        }

        public void setDay_forward_mail(long day_forward_mail)
        {
            this.day_forward_mail = day_forward_mail;
        }

        public long getNewClientConverted()
        {
            return NewClientConverted;
        }

        public void setNewClientConverted(long newClientConverted)
        {
            NewClientConverted = newClientConverted;
        }

        public long getSelfAcquiredAUM()
        {
            return SelfAcquiredAUM;
        }

        public void setSelfAcquiredAUM(long selfAcquiredAUM)
        {
            SelfAcquiredAUM = selfAcquiredAUM;
        }

        public long getDAR()
        {
            return DAR;
        }

        public void setDAR(long DAR)
        {
            this.DAR = DAR;
        }
    }
}
