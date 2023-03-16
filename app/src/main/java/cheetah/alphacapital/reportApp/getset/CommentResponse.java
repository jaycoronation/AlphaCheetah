package cheetah.alphacapital.reportApp.getset;

public class CommentResponse
{

    /**
     * $id : 1
     * message : Task Message successfully.
     * success : true
     * data : {"$id":"2","Id":134,"employee_id":1,"task_id":418,"msg_txt":"test 2","img_url":null,"is_image":false,"added_date":"2019-10-18T17:16:51.0961036+05:30","timestamp":"AAAAAAAASdc="}
     */

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
        /**
         * $id : 2
         * Id : 134
         * employee_id : 1
         * task_id : 418
         * msg_txt : test 2
         * img_url : null
         * is_image : false
         * added_date : 2019-10-18T17:16:51.0961036+05:30
         * timestamp : AAAAAAAASdc=
         */

        private int Id =0;
        private int employee_id =0;
        private int task_id =0;
        private String msg_txt ="";
        private String img_url ="";
        private boolean is_image =false;
        private String added_date ="";
        private String timestamp ="";
        private String fullname="";

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

        public String getTimestamp()
        {
            return timestamp;
        }

        public void setTimestamp(String timestamp)
        {
            this.timestamp = timestamp;
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
