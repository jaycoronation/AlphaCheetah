
package cheetah.alphacapital.reportApp.getset;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetStartEndYearResponseModel {

    @Expose
    private String $id;
    @Expose
    private Data data;
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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
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

    public static class Data {

        @Expose
        private String $id;
        @SerializedName("last_year")
        private Long lastYear;
        @SerializedName("start_year")
        private Long startYear;

        public String get$id() {
            return $id;
        }

        public void set$id(String $id) {
            this.$id = $id;
        }

        public Long getLastYear() {
            return lastYear;
        }

        public void setLastYear(Long lastYear) {
            this.lastYear = lastYear;
        }

        public Long getStartYear() {
            return startYear;
        }

        public void setStartYear(Long startYear) {
            this.startYear = startYear;
        }

    }


}
