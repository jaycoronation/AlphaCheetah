package cheetah.alphacapital.reportApp.getset;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Ravi Patel on 30-01-2019.
 */
public class CommonGetSet implements Serializable, Parcelable
{
    int id=0;
    String name="";
    boolean isSelect=false;

    public CommonGetSet()
    {
    }

    public CommonGetSet(int id, String name)
    {
        this.id = id;
        this.name = name;
    }

    protected CommonGetSet(Parcel in)
    {
        id = in.readInt();
        name = in.readString();
        isSelect = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeByte((byte) (isSelect ? 1 : 0));
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    public static final Creator<CommonGetSet> CREATOR = new Creator<CommonGetSet>()
    {
        @Override
        public CommonGetSet createFromParcel(Parcel in)
        {
            return new CommonGetSet(in);
        }

        @Override
        public CommonGetSet[] newArray(int size)
        {
            return new CommonGetSet[size];
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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean isSelect()
    {
        return isSelect;
    }

    public void setSelect(boolean select)
    {
        isSelect = select;
    }

    @Override
    public String toString() {
        return "\n" +"name=" + name + "   id=" + id ;
    }
}
