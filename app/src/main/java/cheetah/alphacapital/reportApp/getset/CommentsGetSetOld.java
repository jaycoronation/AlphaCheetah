package cheetah.alphacapital.reportApp.getset;

/**
 * Created by Ravi Patel on 06-03-2019.
 */
public class CommentsGetSetOld
{

    /**
     * id : 1
     * employee_id : 1
     * task_id : 27
     * msg_txt : Hello
     * img_url : null
     * is_image : false
     * added_date : 2019-03-06T14:17:15.873
     * first_name : Mukesh
     * last_name : Jindal
     */

    private String id = "";
    private String employee_id = "";
    private String task_id = "";
    private String msg_txt = "";
    private String img_url = "";
    private boolean is_image = false;
    private String added_date = "";
    private String full_name = "";

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getEmployee_id()
    {
        return employee_id;
    }

    public void setEmployee_id(String employee_id)
    {
        this.employee_id = employee_id;
    }

    public String getTask_id()
    {
        return task_id;
    }

    public void setTask_id(String task_id)
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

    public String getFull_name()
    {
        return full_name;
    }

    public void setFull_name(String full_name)
    {
        this.full_name = full_name;
    }
}
