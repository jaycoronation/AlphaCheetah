package cheetah.alphacapital.reportApp.getset;

import java.util.ArrayList;
import java.util.List;

public class EmpDARReportListResponse
{

    /**
     * $id : 1
     * message : null
     * success : true
     * data : [{"$id":"2","sip":0,"aum":0,"month":1,"full_month":"January","totalDays":0,"workingdays":0,"totalHours":0,"workingHours":0},{"$id":"3","sip":0,"aum":0,"month":2,"full_month":"February","totalDays":0,"workingdays":0,"totalHours":0,"workingHours":0},{"$id":"4","sip":0,"aum":0,"month":3,"full_month":"March","totalDays":0,"workingdays":0,"totalHours":0,"workingHours":0},{"$id":"5","sip":0,"aum":0,"month":4,"full_month":"April","totalDays":0,"workingdays":0,"totalHours":0,"workingHours":0},{"$id":"6","sip":0,"aum":0,"month":5,"full_month":"May","totalDays":0,"workingdays":0,"totalHours":0,"workingHours":0},{"$id":"7","sip":0,"aum":0,"month":6,"full_month":"June","totalDays":0,"workingdays":0,"totalHours":0,"workingHours":0},{"$id":"8","sip":0,"aum":0,"month":7,"full_month":"July","totalDays":0,"workingdays":0,"totalHours":0,"workingHours":0},{"$id":"9","sip":0,"aum":0,"month":8,"full_month":"August","totalDays":30,"workingdays":0,"totalHours":0,"workingHours":0},{"$id":"10","sip":0,"aum":0,"month":9,"full_month":"September","totalDays":25,"workingdays":0,"totalHours":0,"workingHours":0},{"$id":"11","sip":0,"aum":0,"month":10,"full_month":"October","totalDays":22,"workingdays":0,"totalHours":0,"workingHours":0},{"$id":"12","sip":0,"aum":0,"month":11,"full_month":"November","totalDays":0,"workingdays":0,"totalHours":0,"workingHours":0},{"$id":"13","sip":0,"aum":0,"month":12,"full_month":"December","totalDays":0,"workingdays":0,"totalHours":0,"workingHours":0}]
     */

    private String message ="";
    private boolean success = false;
    private List<DataBean> data = new ArrayList<>();

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * $id : 2
         * sip : 0
         * aum : 0
         * month : 1
         * full_month : January
         * totalDays : 0
         * workingdays : 0
         * totalHours : 0
         * workingHours : 0
         */

        private int sip = 0;
        private int aum = 0;
        private int month = 0;
        private String full_month = "";
        private float totalDays = 0;
        private float workingdays = 0;
        private float totalHours = 0;
        private float workingHours = 0;

        public int getSip() {
            return sip;
        }

        public void setSip(int sip) {
            this.sip = sip;
        }

        public int getAum() {
            return aum;
        }

        public void setAum(int aum) {
            this.aum = aum;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public String getFull_month() {
            return full_month;
        }

        public void setFull_month(String full_month) {
            this.full_month = full_month;
        }

        public float getTotalDays() {
            return totalDays;
        }

        public void setTotalDays(float totalDays) {
            this.totalDays = totalDays;
        }

        public float getWorkingdays() {
            return workingdays;
        }

        public void setWorkingdays(float workingdays) {
            this.workingdays = workingdays;
        }

        public float getTotalHours() {
            return totalHours;
        }

        public void setTotalHours(float totalHours) {
            this.totalHours = totalHours;
        }

        public float getWorkingHours() {
            return workingHours;
        }

        public void setWorkingHours(float workingHours) {
            this.workingHours = workingHours;
        }
    }
}
