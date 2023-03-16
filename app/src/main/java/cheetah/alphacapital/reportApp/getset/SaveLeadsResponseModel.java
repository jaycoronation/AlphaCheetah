package cheetah.alphacapital.reportApp.getset;

public class SaveLeadsResponseModel{
    private Data data;
    private boolean success;
    private String message;
    private String $id;

    public void setData(Data data){
        this.data = data;
    }

    public Data getData(){
        return data;
    }

    public void setSuccess(boolean success){
        this.success = success;
    }

    public boolean isSuccess(){
        return success;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }

    public void setId(String id){
        this.$id = id;
    }

    public String getId(){
        return $id;
    }

    public class Data{
        private String Status;
        private String Category;
        private int AUM_Year;
        private int AUM_Month;
        private String Category_Id;
        private String ReferenceFrom;
        private String ClientName;
        private Object ReferenceDate_Format;
        private int employee_id;
        private String timetstamp;
        private int Id;
        private String created_date;
        private String ReferenceDate;
        private String $id;

        public void setStatus(String status){
            this.Status = status;
        }

        public String getStatus(){
            return Status;
        }

        public void setCategory(String category){
            this.Category = category;
        }

        public String getCategory(){
            return Category;
        }

        public void setAUMYear(int aUMYear){
            this.AUM_Year = aUMYear;
        }

        public int getAUMYear(){
            return AUM_Year;
        }

        public void setAUMMonth(int aUMMonth){
            this.AUM_Month = aUMMonth;
        }

        public int getAUMMonth(){
            return AUM_Month;
        }

        public void setCategory_Id(String category_Id){
            this.Category_Id = category_Id;
        }

        public String getCategory_Id(){
            return Category_Id;
        }

        public void setReferenceFrom(String referenceFrom){
            this.ReferenceFrom = referenceFrom;
        }

        public String getReferenceFrom(){
            return ReferenceFrom;
        }

        public void setClientName(String clientName){
            this.ClientName = clientName;
        }

        public String getClientName(){
            return ClientName;
        }

        public void setReferenceDate_Format(Object referenceDate_Format){
            this.ReferenceDate_Format = referenceDate_Format;
        }

        public Object getReferenceDate_Format(){
            return ReferenceDate_Format;
        }

        public void setEmployee_id(int employee_id){
            this.employee_id = employee_id;
        }

        public int getEmployee_id(){
            return employee_id;
        }

        public void setTimetstamp(String timetstamp){
            this.timetstamp = timetstamp;
        }

        public String getTimetstamp(){
            return timetstamp;
        }

        public void setId(String id){
            this.$id = id;
        }

        public String get$Id(){
            return $id;
        }

        public void setCreated_date(String created_date){
            this.created_date = created_date;
        }

        public String getCreated_date(){
            return created_date;
        }

        public void setReferenceDate(String referenceDate){
            this.ReferenceDate = referenceDate;
        }

        public String getReferenceDate(){
            return ReferenceDate;
        }

        public void setId(int id){
            this.Id = id;
        }

        public Integer getId(){
            return Id;
        }
    }

}
