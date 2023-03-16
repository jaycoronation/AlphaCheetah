package cheetah.alphacapital.reportApp.getset;

import java.util.List;

public class TaskReportResponseModel
{
    private Data data;
    private boolean success;
    private String message;
    private String $id;

    public void setData(Data data)
    {
        this.data = data;
    }

    public Data getData()
    {
        return data;
    }

    public void setSuccess(boolean success)
    {
        this.success = success;
    }

    public boolean isSuccess()
    {
        return success;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getMessage()
    {
        return message;
    }

    public void set$id(String $id)
    {
        this.$id = $id;
    }

    public String get$id()
    {
        return $id;
    }

    public static class Data
    {
        private Average average;
        private List<DataItem> data;
        private String $id;
        private Title title;

        public void setAverage(Average average)
        {
            this.average = average;
        }

        public Average getAverage()
        {
            return average;
        }

        public void setTitle(Title title)
        {
            this.title = title;
        }

        public Title getTitle()
        {
            return title;
        }

        public void setData(List<DataItem> data)
        {
            this.data = data;
        }

        public List<DataItem> getData()
        {
            return data;
        }

        public void set$id(String $id)
        {
            this.$id = $id;
        }

        public String get$id()
        {
            return $id;
        }
    }

    public static class Title
    {
        private String total_task = "Total Task";
        private String task_closed = "Task Closed";
        private String Avg_days_taken = "Avg Days Taken";
        private String open_task = "Open Task";
        private String Avg_open_taken = "Avg Open Days";

		public void setTotal_task(String total_task)
		{
			this.total_task = total_task;
		}

		public String getTotal_task()
		{
			return total_task;
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
			return Avg_days_taken;
		}

		public void setAvg_days_taken(String avg_days_taken)
		{
			Avg_days_taken = avg_days_taken;
		}

		public String getOpen_task()
		{
			return open_task;
		}

		public void setOpen_task(String open_task)
		{
			this.open_task = open_task;
		}

		public String getAvg_open_taken()
		{
			return Avg_open_taken;
		}

		public void setAvg_open_taken(String avg_open_taken)
		{
			Avg_open_taken = avg_open_taken;
		}
	}

    public class Average
    {
        private CurrentMonthTaskAverage CurrentMonthTaskAverage;
        private CurrentMonthTaskAverage LastMonthTaskAverage;
        private CurrentMonthTaskAverage SinceBeginningTaskAverage;
        private String $id;

        public void setCurrentMonthTaskAverage(CurrentMonthTaskAverage currentMonthTaskAverage)
        {
            this.CurrentMonthTaskAverage = currentMonthTaskAverage;
        }

        public CurrentMonthTaskAverage getCurrentMonthTaskAverage()
        {
            return CurrentMonthTaskAverage;
        }

        public void setLastMonthTaskAverage(CurrentMonthTaskAverage lastMonthTaskAverage)
        {
            this.LastMonthTaskAverage = lastMonthTaskAverage;
        }

        public CurrentMonthTaskAverage getLastMonthTaskAverage()
        {
            return LastMonthTaskAverage;
        }

        public void setSinceBeginningTaskAverage(CurrentMonthTaskAverage sinceBeginningTaskAverage)
        {
            this.SinceBeginningTaskAverage = sinceBeginningTaskAverage;
        }

        public CurrentMonthTaskAverage getSinceBeginningTaskAverage()
        {
            return SinceBeginningTaskAverage;
        }

        public void set$id(String $id)
        {
            this.$id = $id;
        }

        public String get$id()
        {
            return $id;
        }
    }

    public static class CurrentMonthTaskAverage
    {
        private double AvgDaysTaken;
        private double AvgOpenDays;
        private double TotalTasks;
        private double TasksClosed;
        private double OpenTasks;
        private String $id;

        public void setAvgDaysTaken(double avgDaysTaken)
        {
            this.AvgDaysTaken = avgDaysTaken;
        }

        public double getAvgDaysTaken()
        {
            return AvgDaysTaken;
        }

        public void setAvgOpenDays(double avgOpenDays)
        {
            this.AvgOpenDays = avgOpenDays;
        }

        public double getAvgOpenDays()
        {
            return AvgOpenDays;
        }

        public void setTotalTasks(double totalTasks)
        {
            this.TotalTasks = totalTasks;
        }

        public double getTotalTasks()
        {
            return TotalTasks;
        }

        public void setTasksClosed(double tasksClosed)
        {
            this.TasksClosed = tasksClosed;
        }

        public double getTasksClosed()
        {
            return TasksClosed;
        }

        public void setOpenTasks(double openTasks)
        {
            this.OpenTasks = openTasks;
        }

        public double getOpenTasks()
        {
            return OpenTasks;
        }

        public void set$id(String $id)
        {
            this.$id = $id;
        }

        public String get$id()
        {
            return $id;
        }
    }

    public static class DataItem
    {
        private CurrentMonthTaskAverage SinceBeginning;
        private int employee_id;
        private CurrentMonthTaskAverage LastMonth;
        private String employee_name;
        private CurrentMonthTaskAverage CurrentMonth;
        private String $id;

        public void setSinceBeginning(CurrentMonthTaskAverage sinceBeginning)
        {
            this.SinceBeginning = sinceBeginning;
        }

        public CurrentMonthTaskAverage getSinceBeginning()
        {
            return SinceBeginning;
        }

        public void setEmployee_id(int employee_id)
        {
            this.employee_id = employee_id;
        }

        public int getEmployee_id()
        {
            return employee_id;
        }

        public void setLastMonth(CurrentMonthTaskAverage lastMonth)
        {
            this.LastMonth = lastMonth;
        }

        public CurrentMonthTaskAverage getLastMonth()
        {
            return LastMonth;
        }

        public void setEmployee_name(String employee_name)
        {
            this.employee_name = employee_name;
        }

        public String getEmployee_name()
        {
            return employee_name;
        }

        public void setCurrentMonth(CurrentMonthTaskAverage currentMonth)
        {
            this.CurrentMonth = currentMonth;
        }

        public CurrentMonthTaskAverage getCurrentMonth()
        {
            return CurrentMonth;
        }

        public void set$id(String $id)
        {
            this.$id = $id;
        }

        public String get$id()
        {
            return $id;
        }
    }

}
