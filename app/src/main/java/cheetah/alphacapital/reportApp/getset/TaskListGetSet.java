package cheetah.alphacapital.reportApp.getset;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Ravi Patel on 01-03-2019.
 */
public class TaskListGetSet implements Parcelable, Serializable
{
    private int id = 0;
    private int employee_id = 0;
    private int task_status_id = 0;
    private String task_message = "";
    private String due_date = "";
    private String created_date = "";
    private String formated_created_date = "";
    private String task_added_by = "";
    private String task_status = "";
    private String all_employee_ids = "";
    private String all_employee_name = "";
    private boolean IsPinedTask = false;
    private int unReadCount = 0;
    private ArrayList<AssignedClientGetSetOld> listEmployee = new ArrayList<AssignedClientGetSetOld>();

    public TaskListGetSet()
    {
    }


    protected TaskListGetSet(Parcel in)
    {
        id = in.readInt();
        employee_id = in.readInt();
        task_status_id = in.readInt();
        task_message = in.readString();
        due_date = in.readString();
        created_date = in.readString();
        formated_created_date = in.readString();
        task_added_by = in.readString();
        task_status = in.readString();
        all_employee_ids = in.readString();
        all_employee_name = in.readString();
        IsPinedTask = in.readByte() != 0;
        unReadCount = in.readInt();
        listEmployee = in.createTypedArrayList(AssignedClientGetSetOld.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(id);
        dest.writeInt(employee_id);
        dest.writeInt(task_status_id);
        dest.writeString(task_message);
        dest.writeString(due_date);
        dest.writeString(created_date);
        dest.writeString(formated_created_date);
        dest.writeString(task_added_by);
        dest.writeString(task_status);
        dest.writeString(all_employee_ids);
        dest.writeString(all_employee_name);
        dest.writeByte((byte) (IsPinedTask ? 1 : 0));
        dest.writeInt(unReadCount);
        dest.writeTypedList(listEmployee);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    public static final Creator<TaskListGetSet> CREATOR = new Creator<TaskListGetSet>()
    {
        @Override
        public TaskListGetSet createFromParcel(Parcel in)
        {
            return new TaskListGetSet(in);
        }

        @Override
        public TaskListGetSet[] newArray(int size)
        {
            return new TaskListGetSet[size];
        }
    };

    public int getUnReadCount()
    {
        return unReadCount;
    }

    public void setUnReadCount(int unReadCount)
    {
        this.unReadCount = unReadCount;
    }

    public boolean isPinedTask()
    {
        return IsPinedTask;
    }

    public void setPinedTask(boolean pinedTask)
    {
        IsPinedTask = pinedTask;
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

    public int getTask_status_id()
    {
        return task_status_id;
    }

    public void setTask_status_id(int task_status_id)
    {
        this.task_status_id = task_status_id;
    }

    public String getTask_message()
    {
        return task_message;
    }

    public void setTask_message(String task_message)
    {
        this.task_message = task_message;
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

    public String getFormated_created_date()
    {
        return formated_created_date;
    }

    public void setFormated_created_date(String formated_created_date)
    {
        this.formated_created_date = formated_created_date;
    }

    public String getTask_added_by()
    {
        return task_added_by;
    }

    public void setTask_added_by(String task_added_by)
    {
        this.task_added_by = task_added_by;
    }

    public String getTask_status()
    {
        return task_status;
    }

    public void setTask_status(String task_status)
    {
        this.task_status = task_status;
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

    public ArrayList<AssignedClientGetSetOld> getListEmployee()
    {
        return listEmployee;
    }

    public void setListEmployee(ArrayList<AssignedClientGetSetOld> listEmployee)
    {
        this.listEmployee = listEmployee;
    }
}
