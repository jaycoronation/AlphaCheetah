
package cheetah.alphacapital.reportApp.getset;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class CapturedLeadsResponseModel {

    @SerializedName("$id")
    private String m$id;
    @SerializedName("data")
    private List<LeadsData> mData;
    @SerializedName("message")
    private Object mMessage;
    @SerializedName("success")
    private Boolean mSuccess;

    public String get$id() {
        return m$id;
    }

    public void set$id(String $id) {
        m$id = $id;
    }

    public List<LeadsData> getData() {
        return mData;
    }

    public void setData(List<LeadsData> data) {
        mData = data;
    }

    public Object getMessage() {
        return mMessage;
    }

    public void setMessage(Object message) {
        mMessage = message;
    }

    public Boolean getSuccess() {
        return mSuccess;
    }

    public void setSuccess(Boolean success) {
        mSuccess = success;
    }


    @SuppressWarnings("unused")
    public static class LeadsData {

        @SerializedName("$id")
        private String m$id;
        @SerializedName("actual_date")
        private String mActualDate;
        @SerializedName("actual_date_format")
        private String mActualDateFormat;
        @SerializedName("actual_date_format_time")
        private String mActualDateFormatTime;
        @SerializedName("added_date")
        private String mAddedDate;
        @SerializedName("added_date_format")
        private String mAddedDateFormat;
        @SerializedName("employee_id")
        private Long mEmployeeId;
        @SerializedName("id")
        private Long mId;
        @SerializedName("is_active")
        private Boolean mIsActive;
        @SerializedName("last_lead_status_id")
        private Long mLastLeadStatusId;
        @SerializedName("last_lead_status_name")
        private String mLastLeadStatusName;
        @SerializedName("lead_email")
        private String mLeadEmail;
        @SerializedName("lead_mobile")
        private String mLeadMobile;
        @SerializedName("lead_name")
        private String mLeadName;
        @SerializedName("leadOwnerEmp")
        private String mLeadOwnerEmp;
        @SerializedName("lead_RM_Emp")
        private String mLeadRMEmp;
        @SerializedName("lead_source")
        private String mLeadSource;
        @SerializedName("lstLeadStatusDetails")
        private ArrayList<LstLeadStatusDetail> mLstLeadStatusDetails;
        @SerializedName("name_who_gave_lead")
        private String mNameWhoGaveLead;
        @SerializedName("planned_date")
        private String mPlannedDate;
        @SerializedName("planned_date_format")
        private String mPlannedDateFormat;
        @SerializedName("RecordCount")
        private Long mRecordCount;
        @SerializedName("rm_employee_id")
        private Long mRmEmployeeId;
        @SerializedName("RowNo")
        private Long mRowNo;
        @SerializedName("status")
        private String mStatus;
        @SerializedName("time_delay")
        private Object mTimeDelay;

        public String get$id() {
            return m$id;
        }

        public void set$id(String $id) {
            m$id = $id;
        }

        public String getActualDate() {
            return mActualDate;
        }

        public void setActualDate(String actualDate) {
            mActualDate = actualDate;
        }

        public String getActualDateFormat() {
            return mActualDateFormat;
        }

        public void setActualDateFormat(String actualDateFormat) {
            mActualDateFormat = actualDateFormat;
        }

        public String getActualDateFormatTime() {
            return mActualDateFormatTime;
        }

        public void setActualDateFormatTime(String actualDateFormatTime) {
            mActualDateFormatTime = actualDateFormatTime;
        }

        public String getAddedDate() {
            return mAddedDate;
        }

        public void setAddedDate(String addedDate) {
            mAddedDate = addedDate;
        }

        public String getAddedDateFormat() {
            return mAddedDateFormat;
        }

        public void setAddedDateFormat(String addedDateFormat) {
            mAddedDateFormat = addedDateFormat;
        }

        public Long getEmployeeId() {
            return mEmployeeId;
        }

        public void setEmployeeId(Long employeeId) {
            mEmployeeId = employeeId;
        }

        public Long getId() {
            return mId;
        }

        public void setId(Long id) {
            mId = id;
        }

        public Boolean getIsActive() {
            return mIsActive;
        }

        public void setIsActive(Boolean isActive) {
            mIsActive = isActive;
        }

        public Long getLastLeadStatusId() {
            return mLastLeadStatusId;
        }

        public void setLastLeadStatusId(Long lastLeadStatusId) {
            mLastLeadStatusId = lastLeadStatusId;
        }

        public String getLastLeadStatusName() {
            return mLastLeadStatusName;
        }

        public void setLastLeadStatusName(String lastLeadStatusName) {
            mLastLeadStatusName = lastLeadStatusName;
        }

        public String getLeadEmail() {
            return mLeadEmail;
        }

        public void setLeadEmail(String leadEmail) {
            mLeadEmail = leadEmail;
        }

        public String getLeadMobile() {
            return mLeadMobile;
        }

        public void setLeadMobile(String leadMobile) {
            mLeadMobile = leadMobile;
        }

        public String getLeadName() {
            return mLeadName;
        }

        public void setLeadName(String leadName) {
            mLeadName = leadName;
        }

        public String getLeadOwnerEmp() {
            return mLeadOwnerEmp;
        }

        public void setLeadOwnerEmp(String leadOwnerEmp) {
            mLeadOwnerEmp = leadOwnerEmp;
        }

        public String getLeadRMEmp() {
            return mLeadRMEmp;
        }

        public void setLeadRMEmp(String leadRMEmp) {
            mLeadRMEmp = leadRMEmp;
        }

        public String getLeadSource() {
            return mLeadSource;
        }

        public void setLeadSource(String leadSource) {
            mLeadSource = leadSource;
        }

        public ArrayList<LstLeadStatusDetail> getLstLeadStatusDetails() {
            return mLstLeadStatusDetails;
        }

        public void setLstLeadStatusDetails(ArrayList<LstLeadStatusDetail> lstLeadStatusDetails) {
            mLstLeadStatusDetails = lstLeadStatusDetails;
        }

        public String getNameWhoGaveLead() {
            return mNameWhoGaveLead;
        }

        public void setNameWhoGaveLead(String nameWhoGaveLead) {
            mNameWhoGaveLead = nameWhoGaveLead;
        }

        public String getPlannedDate() {
            return mPlannedDate;
        }

        public void setPlannedDate(String plannedDate) {
            mPlannedDate = plannedDate;
        }

        public String getPlannedDateFormat() {
            return mPlannedDateFormat;
        }

        public void setPlannedDateFormat(String plannedDateFormat) {
            mPlannedDateFormat = plannedDateFormat;
        }

        public Long getRecordCount() {
            return mRecordCount;
        }

        public void setRecordCount(Long recordCount) {
            mRecordCount = recordCount;
        }

        public Long getRmEmployeeId() {
            return mRmEmployeeId;
        }

        public void setRmEmployeeId(Long rmEmployeeId) {
            mRmEmployeeId = rmEmployeeId;
        }

        public Long getRowNo() {
            return mRowNo;
        }

        public void setRowNo(Long rowNo) {
            mRowNo = rowNo;
        }

        public String getStatus() {
            return mStatus;
        }

        public void setStatus(String status) {
            mStatus = status;
        }

        public Object getTimeDelay() {
            return mTimeDelay;
        }

        public void setTimeDelay(Object timeDelay) {
            mTimeDelay = timeDelay;
        }

    }

}
