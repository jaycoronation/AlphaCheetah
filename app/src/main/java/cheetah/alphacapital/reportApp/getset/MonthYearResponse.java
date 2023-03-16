package cheetah.alphacapital.reportApp.getset;

import java.util.ArrayList;
import java.util.ArrayList;

public class MonthYearResponse
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
        private ArrayList<MonthBean> month = new ArrayList<MonthBean>();
        private ArrayList<String> year = new ArrayList<String>();

        public ArrayList<MonthBean> getMonth()
        {
            return month;
        }

        public void setMonth(ArrayList<MonthBean> month)
        {
            this.month = month;
        }

        public ArrayList<String> getYear()
        {
            return year;
        }

        public void setYear(ArrayList<String> year)
        {
            this.year = year;
        }

        public static class MonthBean
        {
            private int id = 0;
            private String name ="";

            public int getId()
            {
                return id;
            }

            public void setId(int id)
            {
                this.id = id;
            }

            public String getName()
            {
                return name;
            }

            public void setName(String name)
            {
                this.name = name;
            }
        }
    }
}
