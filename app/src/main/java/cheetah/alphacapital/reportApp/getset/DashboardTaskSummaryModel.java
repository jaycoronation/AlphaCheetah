package cheetah.alphacapital.reportApp.getset;

import com.google.android.gms.tasks.Tasks;

import java.util.List;

public class DashboardTaskSummaryModel
{
    private String title;
    private String total_tasks;
    private String task_closed;
    private String avg_days_taken;
    private String open_task;
    private String avg_days_open;

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTotal_tasks()
    {
        return total_tasks;
    }

    public void setTotal_tasks(String total_tasks)
    {
        this.total_tasks = total_tasks;
    }

    public String getTask_closed()
    {
        return task_closed;
    }

    public void setTask_closed(String task_closed)
    {
        this.task_closed = task_closed;
    }

    public String getAvg_days_taken()
    {
        return avg_days_taken;
    }

    public void setAvg_days_taken(String avg_days_taken)
    {
        this.avg_days_taken = avg_days_taken;
    }

    public String getOpen_task()
    {
        return open_task;
    }

    public void setOpen_task(String open_task)
    {
        this.open_task = open_task;
    }

    public String getAvg_days_open()
    {
        return avg_days_open;
    }

    public void setAvg_days_open(String avg_days_open)
    {
        this.avg_days_open = avg_days_open;
    }
}

