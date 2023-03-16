package cheetah.alphacapital.reportApp.getset;

public class EmployeeDetailResponse
{
    private String message ="";
    private boolean success = false;
    private DataBean data = new DataBean();

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
        private int id = 0;
        private String first_name = "";
        private String last_name = "";
        private String contact_no = "";
        private String email = "";
        private String password = "";
        private String reset_token = "";
        private String address = "";
        private int country_id = 0;
        private int state_id = 0;
        private int city_id = 0;
        private String country_name = "";
        private String state_name = "";
        private String city_name = "";
        private String emp_type = "";
        private String parents_name = "";
        private String spouse_name = "";
        private String children_name = "";
        private String birthdate = "";
        private String degrees = "";
        private String examinations_cleared = "";
        private boolean is_admin = false;
        private boolean is_active = false;
        private boolean is_deleted = false;
        private String created_date = "";
        private String updated_date = "";
        private String timestamp = "";

        public String getJoining_date()
        {
            return joining_date;
        }

        public void setJoining_date(String joining_date)
        {
            this.joining_date = joining_date;
        }

        private String joining_date = "";

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getFirst_name() {
            return first_name;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public void setLast_name(String last_name) {
            this.last_name = last_name;
        }

        public String getContact_no() {
            return contact_no;
        }

        public void setContact_no(String contact_no) {
            this.contact_no = contact_no;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getReset_token() {
            return reset_token;
        }

        public void setReset_token(String reset_token) {
            this.reset_token = reset_token;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getCountry_id() {
            return country_id;
        }

        public void setCountry_id(int country_id) {
            this.country_id = country_id;
        }

        public int getState_id() {
            return state_id;
        }

        public void setState_id(int state_id) {
            this.state_id = state_id;
        }

        public int getCity_id() {
            return city_id;
        }

        public void setCity_id(int city_id) {
            this.city_id = city_id;
        }

        public String getEmp_type() {
            return emp_type;
        }

        public void setEmp_type(String emp_type) {
            this.emp_type = emp_type;
        }

        public String getParents_name() {
            return parents_name;
        }

        public void setParents_name(String parents_name) {
            this.parents_name = parents_name;
        }

        public String getSpouse_name() {
            return spouse_name;
        }

        public void setSpouse_name(String spouse_name) {
            this.spouse_name = spouse_name;
        }

        public String getChildren_name() {
            return children_name;
        }

        public void setChildren_name(String children_name) {
            this.children_name = children_name;
        }

        public String getBirthdate() {
            return birthdate;
        }

        public void setBirthdate(String birthdate) {
            this.birthdate = birthdate;
        }

        public String getDegrees() {
            return degrees;
        }

        public void setDegrees(String degrees) {
            this.degrees = degrees;
        }

        public String getExaminations_cleared() {
            return examinations_cleared;
        }

        public void setExaminations_cleared(String examinations_cleared) {
            this.examinations_cleared = examinations_cleared;
        }

        public boolean isIs_admin() {
            return is_admin;
        }

        public void setIs_admin(boolean is_admin) {
            this.is_admin = is_admin;
        }

        public boolean isIs_active() {
            return is_active;
        }

        public void setIs_active(boolean is_active) {
            this.is_active = is_active;
        }

        public boolean isIs_deleted() {
            return is_deleted;
        }

        public void setIs_deleted(boolean is_deleted) {
            this.is_deleted = is_deleted;
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

        public String getCountry_name() {
            return country_name;
        }

        public void setCountry_name(String country_name) {
            this.country_name = country_name;
        }

        public String getState_name() {
            return state_name;
        }

        public void setState_name(String state_name) {
            this.state_name = state_name;
        }

        public String getCity_name() {
            return city_name;
        }

        public void setCity_name(String city_name) {
            this.city_name = city_name;
        }
    }
}
