package cheetah.alphacapital.reportApp.getset;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Ravi Patel on 29-01-2019.
 */
public class ClientListGetSet implements Parcelable, Serializable
{
    private String message ="";
    private boolean success =false;
    private ArrayList<DataBean> data = new ArrayList<DataBean>();

    public ClientListGetSet()
    {
    }

    protected ClientListGetSet(Parcel in)
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

    public static final Creator<ClientListGetSet> CREATOR = new Creator<ClientListGetSet>()
    {
        @Override
        public ClientListGetSet createFromParcel(Parcel in)
        {
            return new ClientListGetSet(in);
        }

        @Override
        public ClientListGetSet[] newArray(int size)
        {
            return new ClientListGetSet[size];
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

    public static class DataBean implements Parcelable,Serializable
    {
        private int id =0;
        private String first_name ="";
        private String last_name="";
        private String organization="";
        private String contact_no="";
        private String email="";
        private String address="";
        private int country_id=0;
        private String country_name="";
        private int state_id=0;
        private String state_name="";
        private int city_id =0;
        private String city_name="";
        private boolean is_approved_by_admin =false;
        private boolean is_active =false;
        private String created_date="";

        public DataBean()
        {
        }

        protected DataBean(Parcel in)
        {
            id = in.readInt();
            first_name = in.readString();
            last_name = in.readString();
            organization = in.readString();
            contact_no = in.readString();
            email = in.readString();
            address = in.readString();
            country_id = in.readInt();
            country_name = in.readString();
            state_id = in.readInt();
            state_name = in.readString();
            city_id = in.readInt();
            city_name = in.readString();
            is_approved_by_admin = in.readByte() != 0;
            is_active = in.readByte() != 0;
            created_date = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags)
        {
            dest.writeInt(id);
            dest.writeString(first_name);
            dest.writeString(last_name);
            dest.writeString(organization);
            dest.writeString(contact_no);
            dest.writeString(email);
            dest.writeString(address);
            dest.writeInt(country_id);
            dest.writeString(country_name);
            dest.writeInt(state_id);
            dest.writeString(state_name);
            dest.writeInt(city_id);
            dest.writeString(city_name);
            dest.writeByte((byte) (is_approved_by_admin ? 1 : 0));
            dest.writeByte((byte) (is_active ? 1 : 0));
            dest.writeString(created_date);
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
    }
}
