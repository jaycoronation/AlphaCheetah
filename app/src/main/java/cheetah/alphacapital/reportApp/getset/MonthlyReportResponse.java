package cheetah.alphacapital.reportApp.getset;

import java.util.ArrayList;

public class MonthlyReportResponse
{
    private Data data;
    private boolean success;
    private Object message;
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

    public void setMessage(Object message)
    {
        this.message = message;
    }

    public Object getMessage()
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
        private Summery Summery;
        private ArrayList<MonthlyReportYearlySummeryDataItem> MonthlyReportYearlySummeryData;
        private String $id;

        public void setSummery(Summery summery)
        {
            this.Summery = summery;
        }

        public Summery getSummery()
        {
            return Summery;
        }

        public void setMonthlyReportYearlySummeryData(ArrayList<MonthlyReportYearlySummeryDataItem> monthlyReportYearlySummeryData)
        {
            this.MonthlyReportYearlySummeryData = monthlyReportYearlySummeryData;
        }

        public ArrayList<MonthlyReportYearlySummeryDataItem> getMonthlyReportYearlySummeryData()
        {
            return MonthlyReportYearlySummeryData;
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

    public static class MonthlyReportYearlySummeryDataItem
    {
        private String Attendance;
        private String SelfAcquiredAUM;
        private String LastDateOfPortfolio;
        private String NumberOfPortfolios;
        private String full_month;
        private String LastDateOfPortfolio_format;
        private int total;
        private int month;
        private String DAR;
        private int CurrentYear;
        private int employee_id;
        private int Id;
        private String KnowledgeSessions;
        private String $id;
        private Boolean isClickable = true;

        public Boolean getClickable()
        {
            return isClickable;
        }

        public void setClickable(Boolean clickable)
        {
            isClickable = clickable;
        }

        public void setAttendance(String attendance)
        {
            this.Attendance = attendance;
        }

        public String getAttendance()
        {
            return Attendance;
        }

        public void setSelfAcquiredAUM(String selfAcquiredAUM)
        {
            this.SelfAcquiredAUM = selfAcquiredAUM;
        }

        public String getSelfAcquiredAUM()
        {
            return SelfAcquiredAUM;
        }

        public void setLastDateOfPortfolio(String lastDateOfPortfolio)
        {
            this.LastDateOfPortfolio = lastDateOfPortfolio;
        }

        public String getLastDateOfPortfolio()
        {
            return LastDateOfPortfolio;
        }

        public void setNumberOfPortfolios(String numberOfPortfolios)
        {
            this.NumberOfPortfolios = numberOfPortfolios;
        }

        public String getNumberOfPortfolios()
        {
            return NumberOfPortfolios;
        }

        public void setFull_month(String full_month)
        {
            this.full_month = full_month;
        }

        public String getFull_month()
        {
            return full_month;
        }

        public void setLastDateOfPortfolio_format(String lastDateOfPortfolio_format)
        {
            this.LastDateOfPortfolio_format = lastDateOfPortfolio_format;
        }

        public String getLastDateOfPortfolio_format()
        {
            return LastDateOfPortfolio_format;
        }

        public void setTotal(int total)
        {
            this.total = total;
        }

        public int getTotal()
        {
            return total;
        }

        public void setMonth(int month)
        {
            this.month = month;
        }

        public int getMonth()
        {
            return month;
        }

        public void setDAR(String dAR)
        {
            this.DAR = dAR;
        }

        public String getDAR()
        {
            return DAR;
        }

        public void setCurrentYear(int currentYear)
        {
            this.CurrentYear = currentYear;
        }

        public int getCurrentYear()
        {
            return CurrentYear;
        }

        public void setEmployee_id(int employee_id)
        {
            this.employee_id = employee_id;
        }

        public int getEmployee_id()
        {
            return employee_id;
        }

        public void set$id(int $id)
        {
            this.Id = $id;
        }

        public int get$id()
        {
            return Id;
        }

        public void setKnowledgeSessions(String knowledgeSessions)
        {
            this.KnowledgeSessions = knowledgeSessions;
        }

        public String getKnowledgeSessions()
        {
            return KnowledgeSessions;
        }

        public void setId(String id)
        {
            this.$id = id;
        }

        public String getId()
        {
            return $id;
        }
    }

    public class Summery{
        private Target Target;
        private Target Total;
        private Target Average;
        private Target Variance;
        private String $id;

        public void setTarget(Target target){
            this.Target = target;
        }

        public Target getTarget(){
            return Target;
        }

        public void setTotal(Target total){
            this.Total = total;
        }

        public Target getTotal(){
            return Total;
        }

        public void setAverage(Target average){
            this.Average = average;
        }

        public Target getAverage(){
            return Average;
        }

        public void setVariance(Target variance){
            this.Variance = variance;
        }

        public Target getVariance(){
            return Variance;
        }

        public void set$id(String $id){
            this.$id = $id;
        }

        public String get$id(){
            return $id;
        }
    }

    public class Target{
        private double Attendance;
        private double SelfAcquiredAUM;
        private double NumberOfPortfolios;
        private double DAR;
        private double KnowledgeSessions;
        private String $id;
        private Boolean isSelected;

        public Boolean getSelected()
        {
            return isSelected;
        }

        public void setSelected(Boolean selected)
        {
            isSelected = selected;
        }

        public void setAttendance(double attendance){
            this.Attendance = attendance;
        }

        public double getAttendance(){
            return Attendance;
        }

        public void setSelfAcquiredAUM(double selfAcquiredAUM){
            this.SelfAcquiredAUM = selfAcquiredAUM;
        }

        public double getSelfAcquiredAUM(){
            return SelfAcquiredAUM;
        }

        public void setNumberOfPortfolios(double numberOfPortfolios){
            this.NumberOfPortfolios = numberOfPortfolios;
        }

        public double getNumberOfPortfolios(){
            return NumberOfPortfolios;
        }

        public void setDAR(double dAR){
            this.DAR = dAR;
        }

        public double getDAR(){
            return DAR;
        }

        public void setKnowledgeSessions(double knowledgeSessions){
            this.KnowledgeSessions = knowledgeSessions;
        }

        public double getKnowledgeSessions(){
            return KnowledgeSessions;
        }

        public void set$id(String $id){
            this.$id = $id;
        }

        public String get$id(){
            return $id;
        }
    }



}
