package cheetah.alphacapital.reportApp.getset;

import java.util.ArrayList;
import java.util.ArrayList;

/**
 * Created by Ravi Patel on 06-03-2019.
 */
public class CommentsGetSet
{
    private String message ="";
    private boolean success =false;
    private ArrayList<DataBean> data = new ArrayList<DataBean>();

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

    public static class DataBean
    {
      
        private int id=0;
        private int employee_id=0;
        private int task_id=0;
        private String msg_txt="";
        private String img_url ="";
        private boolean is_image =false;
        private String added_date ="";
        private String first_name ="";
        private String last_name ="";
        private String fullname="";

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

        public int getTask_id()
        {
            return task_id;
        }

        public void setTask_id(int task_id)
        {
            this.task_id = task_id;
        }

        public String getMsg_txt()
        {
            return msg_txt;
        }

        public void setMsg_txt(String msg_txt)
        {
            this.msg_txt = msg_txt;
        }

        public String getImg_url()
        {
            return img_url;
        }

        public void setImg_url(String img_url)
        {
            this.img_url = img_url;
        }

        public boolean isIs_image()
        {
            return is_image;
        }

        public void setIs_image(boolean is_image)
        {
            this.is_image = is_image;
        }

        public String getAdded_date()
        {
            return added_date;
        }

        public void setAdded_date(String added_date)
        {
            this.added_date = added_date;
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

        public String getFullname()
        {
            return fullname;
        }

        public void setFullname(String fullname)
        {
            this.fullname = fullname;
        }
    }
}
