package cheetah.alphacapital.reportApp.getset;

public class SaveTaskResponse
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
        private int Id =0;
        private int employee_id=0;
        private int task_status_id=0;
        private String task_message ="";
        private String due_date ="";
        private String created_date ="";
        private String updated_date ="";
        private String timestamp ="";

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
