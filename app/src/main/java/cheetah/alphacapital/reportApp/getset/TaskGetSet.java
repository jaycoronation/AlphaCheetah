package cheetah.alphacapital.reportApp.getset;


import java.util.ArrayList;

public class TaskGetSet
{

    private String Id  ="";
    private String employee_id ="";
    private String task_status_id ="";
    private String task_message ="";
    private String due_date ="";
    private String created_date ="";
    private ArrayList<CommonGetSet> listEmployee = new ArrayList<CommonGetSet>();

    public String getId()
    {
        return Id;
    }

    public void setId(String id)
    {
        Id = id;
    }

    public String getEmployee_id()
    {
        return employee_id;
    }

    public void setEmployee_id(String employee_id)
    {
        this.employee_id = employee_id;
    }

    public String getTask_status_id()
    {
        return task_status_id;
    }

    public void setTask_status_id(String task_status_id)
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

    public ArrayList<CommonGetSet> getListEmployee()
    {
        return listEmployee;
    }

    public void setListEmployee(ArrayList<CommonGetSet> listEmployee)
    {
        this.listEmployee = listEmployee;
    }
}
