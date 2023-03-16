package cheetah.alphacapital.reportApp.getset;

import java.util.ArrayList;
import java.util.ArrayList;

public class TaskDetailsResponse
{
    private String message ="";
    private boolean success =false;
    private DataBean data = new DataBean();

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

    public DataBean getData()
    {
        return data;
    }

    public void setData(DataBean data)
    {
        this.data = data;
    }

    public static class DataBean
    {
        
        private TaskDetailBean TaskDetail = new TaskDetailBean();
        private ArrayList<EmployeeListBean> EmployeeList = new ArrayList<EmployeeListBean>();
        private ArrayList<ClientListBean> clientList = new ArrayList<ClientListBean>();

        public TaskDetailBean getTaskDetail()
        {
            return TaskDetail;
        }

        public void setTaskDetail(TaskDetailBean TaskDetail)
        {
            this.TaskDetail = TaskDetail;
        }

        public ArrayList<EmployeeListBean> getEmployeeList()
        {
            return EmployeeList;
        }

        public void setEmployeeList(ArrayList<EmployeeListBean> EmployeeList)
        {
            this.EmployeeList = EmployeeList;
        }

        public ArrayList<ClientListBean> getClientList()
        {
            return clientList;
        }

        public void setClientList(ArrayList<ClientListBean> clientList)
        {
            this.clientList = clientList;
        }

        public static class TaskDetailBean
        {
            private int Id =0;
            private int employee_id=0;
            private int task_status_id=0;
            private String task_message ="";
            private String due_date ="";
            private String created_date ="";
            private String updated_date ="";
            private String employee_name ="";

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

            public String getUpdated_date()
            {
                return updated_date;
            }

            public void setUpdated_date(String updated_date)
            {
                this.updated_date = updated_date;
            }

            public String getEmployee_name()
            {
                return employee_name;
            }

            public void setEmployee_name(String employee_name)
            {
                this.employee_name = employee_name;
            }
        }

        public static class EmployeeListBean
        {

            private int employee_id =0;
            private String employee_name ="";

            public int getEmployee_id()
            {
                return employee_id;
            }

            public void setEmployee_id(int employee_id)
            {
                this.employee_id = employee_id;
            }

            public String getEmployee_name()
            {
                return employee_name;
            }

            public void setEmployee_name(String employee_name)
            {
                this.employee_name = employee_name;
            }
        }

        public static class ClientListBean
        {

            private int client_id =0;
            private String client_name ="";

            public int getClient_id()
            {
                return client_id;
            }

            public void setClient_id(int client_id)
            {
                this.client_id = client_id;
            }

            public String getClient_name()
            {
                return client_name;
            }

            public void setClient_name(String client_name)
            {
                this.client_name = client_name;
            }
        }
    }
}
