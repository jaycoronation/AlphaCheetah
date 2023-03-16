
package cheetah.alphacapital.reportApp.getset;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class LstLeadStatusDetail {

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
    @SerializedName("id")
    private Long mId;
    @SerializedName("lead_id")
    private Long mLeadId;
    @SerializedName("lead_status_id")
    private Long mLeadStatusId;
    @SerializedName("lead_status_name")
    private String mLeadStatusName;
    @SerializedName("planned_date")
    private String mPlannedDate;
    @SerializedName("planned_date_format")
    private String mPlannedDateFormat;
    @SerializedName("status")
    private Boolean mStatus;
    @SerializedName("time_delay")
    private Long mTimeDelay;

    public boolean isButtonVisible() {
        return isButtonVisible;
    }

    public void setButtonVisible(boolean buttonVisible) {
        isButtonVisible = buttonVisible;
    }

    private boolean isButtonVisible = false;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    private boolean checked = false;

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

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public Long getLeadId() {
        return mLeadId;
    }

    public void setLeadId(Long leadId) {
        mLeadId = leadId;
    }

    public Long getLeadStatusId() {
        return mLeadStatusId;
    }

    public void setLeadStatusId(Long leadStatusId) {
        mLeadStatusId = leadStatusId;
    }

    public String getLeadStatusName() {
        return mLeadStatusName;
    }

    public void setLeadStatusName(String leadStatusName) {
        mLeadStatusName = leadStatusName;
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

    public Boolean getStatus() {
        return mStatus;
    }

    public void setStatus(Boolean status) {
        mStatus = status;
    }

    public Long getTimeDelay() {
        return mTimeDelay;
    }

    public void setTimeDelay(Long timeDelay) {
        mTimeDelay = timeDelay;
    }

}
