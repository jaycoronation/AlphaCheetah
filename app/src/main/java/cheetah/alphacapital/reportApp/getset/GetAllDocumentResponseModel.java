
package cheetah.alphacapital.reportApp.getset;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class GetAllDocumentResponseModel {

    @SerializedName("$id")
    private String m$id;
    @SerializedName("data")
    private List<Document> mData;
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

    public List<Document> getData() {
        return mData;
    }

    public void setData(List<Document> data) {
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

    public static class Document {

        @SerializedName("$id")
        private String m$id;
        @SerializedName("client_id")
        private Long mClientId;
        @SerializedName("created_date")
        private String mCreatedDate;
        @SerializedName("employee_f_name")
        private String mEmployeeFName;
        @SerializedName("employee_id")
        private Long mEmployeeId;
        @SerializedName("employee_id_assign")
        private Long mEmployeeIdAssign;
        @SerializedName("employee_l_name")
        private String mEmployeeLName;
        @SerializedName("id")
        private Long mId;
        @SerializedName("link")
        private String mLink;
        @SerializedName("title")
        private String mTitle;

        public String get$id() {
            return m$id;
        }

        public void set$id(String $id) {
            m$id = $id;
        }

        public Long getClientId() {
            return mClientId;
        }

        public void setClientId(Long clientId) {
            mClientId = clientId;
        }

        public String getCreatedDate() {
            return mCreatedDate;
        }

        public void setCreatedDate(String createdDate) {
            mCreatedDate = createdDate;
        }

        public String getEmployeeFName() {
            return mEmployeeFName;
        }

        public void setEmployeeFName(String employeeFName) {
            mEmployeeFName = employeeFName;
        }

        public Long getEmployeeId() {
            return mEmployeeId;
        }

        public void setEmployeeId(Long employeeId) {
            mEmployeeId = employeeId;
        }

        public Long getEmployeeIdAssign() {
            return mEmployeeIdAssign;
        }

        public void setEmployeeIdAssign(Long employeeIdAssign) {
            mEmployeeIdAssign = employeeIdAssign;
        }

        public String getEmployeeLName() {
            return mEmployeeLName;
        }

        public void setEmployeeLName(String employeeLName) {
            mEmployeeLName = employeeLName;
        }

        public Long getId() {
            return mId;
        }

        public void setId(Long id) {
            mId = id;
        }

        public String getLink() {
            return mLink;
        }

        public void setLink(String link) {
            mLink = link;
        }

        public String getTitle() {
            return mTitle;
        }

        public void setTitle(String title) {
            mTitle = title;
        }

    }


}
