package cheetah.alphacapital.reportApp.getset;

import java.util.List;

public class LeadsDeatilsResponseModel
{

    private String $id;
    private Object message;
    private Boolean success;
    private List<Datum> data = null;

    public String get$id()
    {
        return $id;
    }

    public void set$id(String $id)
    {
        this.$id = $id;
    }

    public Object getMessage()
    {
        return message;
    }

    public void setMessage(Object message)
    {
        this.message = message;
    }

    public Boolean getSuccess()
    {
        return success;
    }

    public void setSuccess(Boolean success)
    {
        this.success = success;
    }

    public List<Datum> getData()
    {
        return data;
    }

    public void setData(List<Datum> data)
    {
        this.data = data;
    }

    public class Datum {
        private String $id;
        private Integer Id;
        private Integer employee_id;
        private String ClientName;
        private String Category;
        private String Category_Id;
        private String ReferenceDate;
        private String ReferenceDate_Format;
        private String ReferenceFrom;
        private String Status;
        private Integer AUM_Month;
        private Integer AUM_Year;
        private String created_date;
        private String timetstamp;

        public String get$id() {
            return $id;
        }

        public void set$id(String $id) {
            this.$id = $id;
        }

        public Integer getId() {
            return Id;
        }

        public void setId(Integer id) {
            this.Id = id;
        }

        public Integer getEmployee_id() {
            return employee_id;
        }

        public void setEmployee_id(Integer employee_id) {
            this.employee_id = employee_id;
        }

        public String getClientName() {
            return ClientName;
        }

        public void setClientName(String clientName) {
            this.ClientName = clientName;
        }

        public String getCategory() {
            return Category;
        }

        public void setCategory(String category) {
            this.Category = category;
        }

        public String getCategory_Id() {
            return Category_Id;
        }

        public void setCategory_Id(String category_Id) {
            this.Category_Id = category_Id;
        }

        public String getReferenceDate() {
            return ReferenceDate;
        }

        public void setReferenceDate(String referenceDate) {
            this.ReferenceDate = referenceDate;
        }

        public String getReferenceDate_Format() {
            return ReferenceDate_Format;
        }

        public void setReferenceDate_Format(String referenceDate_Format) {
            this.ReferenceDate_Format = referenceDate_Format;
        }

        public String getReferenceFrom() {
            return ReferenceFrom;
        }

        public void setReferenceFrom(String referenceFrom) {
            this.ReferenceFrom = referenceFrom;
        }

        public String getStatus() {
            return Status;
        }

        public void setStatus(String status) {
            this.Status = status;
        }

        public Integer getAUMMonth() {
            return AUM_Month;
        }

        public void setAUMMonth(Integer aUMMonth) {
            this.AUM_Month = aUMMonth;
        }

        public Integer getAUMYear() {
            return AUM_Year;
        }

        public void setAUMYear(Integer aUMYear) {
            this.AUM_Year = aUMYear;
        }

        public String getCreated_date() {
            return created_date;
        }

        public void setCreated_date(String created_date) {
            this.created_date = created_date;
        }

        public String getTimetstamp() {
            return timetstamp;
        }

        public void setTimetstamp(String timetstamp) {
            this.timetstamp = timetstamp;
        }

    }

}