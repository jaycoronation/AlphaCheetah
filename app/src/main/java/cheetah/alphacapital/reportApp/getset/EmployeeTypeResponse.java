package cheetah.alphacapital.reportApp.getset;

import java.util.ArrayList;
import java.util.List;

public class EmployeeTypeResponse
{

    /**
     * $id : 1
     * message : null
     * success : true
     * data : ["Regional Manager","Operation Manager"]
     */

    private String message ="";
    private boolean success = false;
    private List<String> data = new ArrayList<>();

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

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
