
package cheetah.alphacapital.reportApp.getset;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class AddCapturedLeadResponseModel {

    @Expose
    private String $id;
    @Expose
    private CapturedLead capturedLead;
    @Expose
    private String message;
    @Expose
    private Boolean success;

    public String get$id() {
        return $id;
    }

    public void set$id(String $id) {
        this.$id = $id;
    }

    public CapturedLead getData() {
        return capturedLead;
    }

    public void setData(CapturedLead capturedLead) {
        this.capturedLead = capturedLead;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public static class CapturedLead {

        @Expose
        private String $id;
        @SerializedName("added_date")
        private String addedDate;
        @SerializedName("employee_id")
        private Long employeeId;
        @Expose
        private Long id;
        @SerializedName("is_active")
        private Boolean isActive;
        @SerializedName("last_lead_status_id")
        private Object lastLeadStatusId;
        @SerializedName("lead_email")
        private String leadEmail;
        @SerializedName("lead_mobile")
        private String leadMobile;
        @SerializedName("lead_name")
        private String leadName;
        @SerializedName("lead_source")
        private String leadSource;
        @SerializedName("name_who_gave_lead")
        private String nameWhoGaveLead;
        @SerializedName("rm_employee_id")
        private Object rmEmployeeId;
        @Expose
        private String timestamp;

        public String get$id() {
            return $id;
        }

        public void set$id(String $id) {
            this.$id = $id;
        }

        public String getAddedDate() {
            return addedDate;
        }

        public void setAddedDate(String addedDate) {
            this.addedDate = addedDate;
        }

        public Long getEmployeeId() {
            return employeeId;
        }

        public void setEmployeeId(Long employeeId) {
            this.employeeId = employeeId;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Boolean getIsActive() {
            return isActive;
        }

        public void setIsActive(Boolean isActive) {
            this.isActive = isActive;
        }

        public Object getLastLeadStatusId() {
            return lastLeadStatusId;
        }

        public void setLastLeadStatusId(Object lastLeadStatusId) {
            this.lastLeadStatusId = lastLeadStatusId;
        }

        public String getLeadEmail() {
            return leadEmail;
        }

        public void setLeadEmail(String leadEmail) {
            this.leadEmail = leadEmail;
        }

        public String getLeadMobile() {
            return leadMobile;
        }

        public void setLeadMobile(String leadMobile) {
            this.leadMobile = leadMobile;
        }

        public String getLeadName() {
            return leadName;
        }

        public void setLeadName(String leadName) {
            this.leadName = leadName;
        }

        public String getLeadSource() {
            return leadSource;
        }

        public void setLeadSource(String leadSource) {
            this.leadSource = leadSource;
        }

        public String getNameWhoGaveLead() {
            return nameWhoGaveLead;
        }

        public void setNameWhoGaveLead(String nameWhoGaveLead) {
            this.nameWhoGaveLead = nameWhoGaveLead;
        }

        public Object getRmEmployeeId() {
            return rmEmployeeId;
        }

        public void setRmEmployeeId(Object rmEmployeeId) {
            this.rmEmployeeId = rmEmployeeId;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

    }


}
