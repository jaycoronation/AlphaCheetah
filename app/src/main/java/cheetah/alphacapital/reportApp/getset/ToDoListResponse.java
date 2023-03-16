package cheetah.alphacapital.reportApp.getset;

import java.util.ArrayList;
import java.util.ArrayList;

public class ToDoListResponse
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
        private ArrayList<TaskListBean> TaskList = new ArrayList<TaskListBean>();
        private ArrayList<TaskListBean> PinnedTaskList = new ArrayList<TaskListBean>();
        private ArrayList<FilterEmployeeListBean> FilterEmployeeList = new ArrayList<FilterEmployeeListBean>();
        private ArrayList<FilterClientListBean> FilterClientList = new ArrayList<FilterClientListBean>();

        public ArrayList<TaskListBean> getTaskList()
        {
            return TaskList;
        }

        public void setTaskList(ArrayList<TaskListBean> TaskList)
        {
            this.TaskList = TaskList;
        }

        public ArrayList<TaskListBean> getPinnedTaskList()
        {
            return PinnedTaskList;
        }

        public void setPinnedTaskList(ArrayList<TaskListBean> PinnedTaskList)
        {
            this.PinnedTaskList = PinnedTaskList;
        }

        public ArrayList<FilterEmployeeListBean> getFilterEmployeeList()
        {
            return FilterEmployeeList;
        }

        public void setFilterEmployeeList(ArrayList<FilterEmployeeListBean> FilterEmployeeList)
        {
            this.FilterEmployeeList = FilterEmployeeList;
        }

        public ArrayList<FilterClientListBean> getFilterClientList()
        {
            return FilterClientList;
        }

        public void setFilterClientList(ArrayList<FilterClientListBean> FilterClientList)
        {
            this.FilterClientList = FilterClientList;
        }

        public static class TaskListBean
        {
            private int id=0;
            private int employee_id=0;
            private int task_status_id=0;
            private String task_message ="";
            private String due_date="";
            private String created_date="";
            private String formated_created_date="";
            private String task_added_by="";
            private String task_status="";
            private String all_employee_ids="";
            private String all_employee_name="";
            private boolean IsPinedTask =false;
            private String PinnedTaskDate ="",all_Client_Name ="";
            private int unReadCount =0;
            private ArrayList<LstemployeeBean> lstemployee = new ArrayList<LstemployeeBean>();
            private ArrayList<LstclientBean> lstclient = new ArrayList<LstclientBean>();




            public String getAll_Client_Name()
            {
                return all_Client_Name;
            }

            public void setAll_Client_Name(String all_Client_Name)
            {
                this.all_Client_Name = all_Client_Name;
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

            public boolean isIsPinedTask()
            {
                return IsPinedTask;
            }

            public void setIsPinedTask(boolean IsPinedTask)
            {
                this.IsPinedTask = IsPinedTask;
            }

            public String getPinnedTaskDate()
            {
                return PinnedTaskDate;
            }

            public void setPinnedTaskDate(String PinnedTaskDate)
            {
                this.PinnedTaskDate = PinnedTaskDate;
            }

            public int getUnReadCount()
            {
                return unReadCount;
            }

            public void setUnReadCount(int unReadCount)
            {
                this.unReadCount = unReadCount;
            }

            public ArrayList<LstemployeeBean> getLstemployee()
            {
                return lstemployee;
            }

            public void setLstemployee(ArrayList<LstemployeeBean> lstemployee)
            {
                this.lstemployee = lstemployee;
            }

            public ArrayList<LstclientBean> getLstclient()
            {
                return lstclient;
            }

            public void setLstclient(ArrayList<LstclientBean> lstclient)
            {
                this.lstclient = lstclient;
            }

            public static class LstemployeeBean
            {
                /**
                 * $id : 4
                 * employee_id : 13
                 * employee_name : Kiran Patel
                 */

                private int employee_id;
                private String employee_name;

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

            public static class LstclientBean
            {
                /**
                 * $id : 7
                 * client_id : 644
                 * client_name : Aakarsh Mohan
                 */

                private int client_id;
                private String client_name;

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

        public static class FilterEmployeeListBean
        {
            private int employee_id =0;
            private String employee_name ="";
            private String email ="";
            private boolean isSelected =false;


            public boolean isSelected()
            {
                return isSelected;
            }

            public void setSelected(boolean selected)
            {
                isSelected = selected;
            }

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

            public String getEmail()
            {
                return email;
            }

            public void setEmail(String email)
            {
                this.email = email;
            }
        }

        public static class FilterClientListBean
        {
            private int client_id =0;
            private String client_name ="";
            private String email ="";
            private boolean isSelected =false;

            public boolean isSelected()
            {
                return isSelected;
            }

            public void setSelected(boolean selected)
            {
                isSelected = selected;
            }

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

            public String getEmail()
            {
                return email;
            }

            public void setEmail(String email)
            {
                this.email = email;
            }
        }
    }
}
