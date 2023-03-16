package cheetah.alphacapital.reportApp.getset;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Ravi Patel on 30-01-2019.
 */
public class SchemeGetSet implements Parcelable, Serializable
{


    /**
     * id : 1
     * scheme_type_id : 2
     * scheme_type_name : Debt - Short Term
     * scheme_name : ICICI Prudential Balanced Advantage Fund
     * is_active : true
     * created_date : 2019-01-21T17:35:32.84
     */

    private int id =0;
    private int scheme_type_id =0;
    private String scheme_type_name ="";
    private String scheme_name ="";
    private boolean is_active =false;
    private String created_date ="";
    private boolean is_selected=false;

    public SchemeGetSet()
    {
    }

    protected SchemeGetSet(Parcel in)
    {
        id = in.readInt();
        scheme_type_id = in.readInt();
        scheme_type_name = in.readString();
        scheme_name = in.readString();
        is_active = in.readByte() != 0;
        created_date = in.readString();
        is_selected = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(id);
        dest.writeInt(scheme_type_id);
        dest.writeString(scheme_type_name);
        dest.writeString(scheme_name);
        dest.writeByte((byte) (is_active ? 1 : 0));
        dest.writeString(created_date);
        dest.writeByte((byte) (is_selected ? 1 : 0));
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    public static final Creator<SchemeGetSet> CREATOR = new Creator<SchemeGetSet>()
    {
        @Override
        public SchemeGetSet createFromParcel(Parcel in)
        {
            return new SchemeGetSet(in);
        }

        @Override
        public SchemeGetSet[] newArray(int size)
        {
            return new SchemeGetSet[size];
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

    public int getScheme_type_id()
    {
        return scheme_type_id;
    }

    public void setScheme_type_id(int scheme_type_id)
    {
        this.scheme_type_id = scheme_type_id;
    }

    public String getScheme_type_name()
    {
        return scheme_type_name;
    }

    public void setScheme_type_name(String scheme_type_name)
    {
        this.scheme_type_name = scheme_type_name;
    }

    public String getScheme_name()
    {
        return scheme_name;
    }

    public void setScheme_name(String scheme_name)
    {
        this.scheme_name = scheme_name;
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

    public boolean isIs_selected()
    {
        return is_selected;
    }

    public void setIs_selected(boolean is_selected)
    {
        this.is_selected = is_selected;
    }


}
