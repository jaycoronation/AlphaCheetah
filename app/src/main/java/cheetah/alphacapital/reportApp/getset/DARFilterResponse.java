package cheetah.alphacapital.reportApp.getset;

import java.util.ArrayList;
import java.util.List;

public class DARFilterResponse
{
    private String message ="";
    private boolean success = false;
    private DataBean data = new DataBean();

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {

        private String Timespant_Total ="";
        private List<EmployeeWiseBean> EmployeeWise = new ArrayList<>();
        private List<ActivityTypeWiseBean> activity_type_wise = new ArrayList<>();
        private List<ClientWiseBean> client_wise = new ArrayList<>();

        public String getTimespant_Total() {
            return Timespant_Total;
        }

        public void setTimespant_Total(String Timespant_Total) {
            this.Timespant_Total = Timespant_Total;
        }

        public List<EmployeeWiseBean> getEmployeeWise() {
            return EmployeeWise;
        }

        public void setEmployeeWise(List<EmployeeWiseBean> EmployeeWise) {
            this.EmployeeWise = EmployeeWise;
        }

        public List<ActivityTypeWiseBean> getActivity_type_wise() {
            return activity_type_wise;
        }

        public void setActivity_type_wise(List<ActivityTypeWiseBean> activity_type_wise) {
            this.activity_type_wise = activity_type_wise;
        }

        public List<ClientWiseBean> getClient_wise() {
            return client_wise;
        }

        public void setClient_wise(List<ClientWiseBean> client_wise) {
            this.client_wise = client_wise;
        }

        public static class EmployeeWiseBean {
            /**
             * $id : 3
             * name : Ravi Patel
             * TimeSpent : 0
             * TimeSpent_Min : 0
             * TimeSpent_total : 3.0
             * Percentage : 100.0
             */

            private int id = 0;
            private String name;
            private int TimeSpent;
            private int TimeSpent_Min;
            private double TimeSpent_total;
            private double Percentage;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getTimeSpent() {
                return TimeSpent;
            }

            public void setTimeSpent(int TimeSpent) {
                this.TimeSpent = TimeSpent;
            }

            public int getTimeSpent_Min() {
                return TimeSpent_Min;
            }

            public void setTimeSpent_Min(int TimeSpent_Min) {
                this.TimeSpent_Min = TimeSpent_Min;
            }

            public double getTimeSpent_total() {
                return TimeSpent_total;
            }

            public void setTimeSpent_total(double TimeSpent_total) {
                this.TimeSpent_total = TimeSpent_total;
            }

            public double getPercentage() {
                return Percentage;
            }

            public void setPercentage(double Percentage) {
                this.Percentage = Percentage;
            }
        }

        public static class ActivityTypeWiseBean {
            /**
             * $id : 4
             * name : Exisiting Meeting
             * TimeSpent : 0
             * TimeSpent_Min : 0
             * TimeSpent_total : 3.0
             * Percentage : 100.0
             */

            private int id = 0;
            private String name;
            private int TimeSpent;
            private int TimeSpent_Min;
            private double TimeSpent_total;
            private double Percentage;


            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getTimeSpent() {
                return TimeSpent;
            }

            public void setTimeSpent(int TimeSpent) {
                this.TimeSpent = TimeSpent;
            }

            public int getTimeSpent_Min() {
                return TimeSpent_Min;
            }

            public void setTimeSpent_Min(int TimeSpent_Min) {
                this.TimeSpent_Min = TimeSpent_Min;
            }

            public double getTimeSpent_total() {
                return TimeSpent_total;
            }

            public void setTimeSpent_total(double TimeSpent_total) {
                this.TimeSpent_total = TimeSpent_total;
            }

            public double getPercentage() {
                return Percentage;
            }

            public void setPercentage(double Percentage) {
                this.Percentage = Percentage;
            }
        }

        public static class ClientWiseBean {
            /**
             * $id : 5
             * name : Ravi Patel
             * TimeSpent : 0
             * TimeSpent_Min : 0
             * TimeSpent_total : 1.0
             * Percentage : 33.33
             */

            private String name;
            private int TimeSpent;
            private int TimeSpent_Min;
            private double TimeSpent_total;
            private double Percentage;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getTimeSpent() {
                return TimeSpent;
            }

            public void setTimeSpent(int TimeSpent) {
                this.TimeSpent = TimeSpent;
            }

            public int getTimeSpent_Min() {
                return TimeSpent_Min;
            }

            public void setTimeSpent_Min(int TimeSpent_Min) {
                this.TimeSpent_Min = TimeSpent_Min;
            }

            public double getTimeSpent_total() {
                return TimeSpent_total;
            }

            public void setTimeSpent_total(double TimeSpent_total) {
                this.TimeSpent_total = TimeSpent_total;
            }

            public double getPercentage() {
                return Percentage;
            }

            public void setPercentage(double Percentage) {
                this.Percentage = Percentage;
            }
        }
    }
}
