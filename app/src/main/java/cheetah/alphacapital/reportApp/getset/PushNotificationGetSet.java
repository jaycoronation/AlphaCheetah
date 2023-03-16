package cheetah.alphacapital.reportApp.getset;

public class PushNotificationGetSet
{
    private String message_id ="";
    private String contentTitle="";
    private String task_name="";
    private String user_id="";
    private int newcommentcount =0;
    private String task_id="";
    private String msg_txt="";
    private String tickerText="";
    private String notification_type ="";
    private String user_name ="";

    public int getNewcommentcount()
    {
        return newcommentcount;
    }

    public void setNewcommentcount(int newcommentcount)
    {
        this.newcommentcount = newcommentcount;
    }

    public String getUser_name()
    {
        return user_name;
    }

    public void setUser_name(String user_name)
    {
        this.user_name = user_name;
    }

    public String getNotification_type()
    {
        return notification_type;
    }

    public void setNotification_type(String notification_type)
    {
        this.notification_type = notification_type;
    }

    public String getMessage_id()
    {
        return message_id;
    }

    public void setMessage_id(String message_id)
    {
        this.message_id = message_id;
    }

    public String getContentTitle()
    {
        return contentTitle;
    }

    public void setContentTitle(String contentTitle)
    {
        this.contentTitle = contentTitle;
    }

    public String getTask_name()
    {
        return task_name;
    }

    public void setTask_name(String task_name)
    {
        this.task_name = task_name;
    }

    public String getUser_id()
    {
        return user_id;
    }

    public void setUser_id(String user_id)
    {
        this.user_id = user_id;
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

    public String getTickerText()
    {
        return tickerText;
    }

    public void setTickerText(String tickerText)
    {
        this.tickerText = tickerText;
    }

}
