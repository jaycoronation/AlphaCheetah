package cheetah.alphacapital.reportApp.getset;

public class AddNoteResponse
{

    /**
     * $id : 1
     * message : Note updated successfully.
     * success : true
     * data : {"$id":"2","id":91,"client_id":0,"employee_id":1,"employee_id_assign":3,"title":"New Hello Note Edited","description":"","created_date":"2019-10-12T17:31:49.733","updated_date":"2019-10-12T17:36:20.9585476+05:30","timestamp":"AAAAAAAASQc="}
     */

    private String message = "";
    private boolean success = false;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * $id : 2
         * id : 91
         * client_id : 0
         * employee_id : 1
         * employee_id_assign : 3
         * title : New Hello Note Edited
         * description :
         * created_date : 2019-10-12T17:31:49.733
         * updated_date : 2019-10-12T17:36:20.9585476+05:30
         * timestamp : AAAAAAAASQc=
         */

        private int id = 0;
        private int client_id = 0;
        private int employee_id = 0;
        private int employee_id_assign = 0;
        private String title = "";
        private String description = "";
        private String created_date = "";
        private String updated_date = "";
        private String timestamp = "";

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getClient_id() {
            return client_id;
        }

        public void setClient_id(int client_id) {
            this.client_id = client_id;
        }

        public int getEmployee_id() {
            return employee_id;
        }

        public void setEmployee_id(int employee_id) {
            this.employee_id = employee_id;
        }

        public int getEmployee_id_assign() {
            return employee_id_assign;
        }

        public void setEmployee_id_assign(int employee_id_assign) {
            this.employee_id_assign = employee_id_assign;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getCreated_date() {
            return created_date;
        }

        public void setCreated_date(String created_date) {
            this.created_date = created_date;
        }

        public String getUpdated_date() {
            return updated_date;
        }

        public void setUpdated_date(String updated_date) {
            this.updated_date = updated_date;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
    }
}
