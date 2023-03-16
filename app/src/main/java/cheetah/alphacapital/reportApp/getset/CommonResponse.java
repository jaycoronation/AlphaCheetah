package cheetah.alphacapital.reportApp.getset;

public class CommonResponse {

    /**
     * $id : 1
     * message : Note deleted successfully.
     * success : true
     * data : null
     */

    private String message = "";
    private boolean success = false;

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
}
