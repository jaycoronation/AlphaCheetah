package cheetah.alphacapital.reportApp.getset;

import java.util.ArrayList;
import java.util.List;

public class EmployeeListResponse
{

    /**
     * $id : 1
     * message : null
     * success : true
     * data : {"$id":"2","AllEmployee":[{"$id":"3","PageCount":0,"RowNo":1,"RecordCount":27,"id":1,"first_name":"Mukesh","last_name":"Jindal","contact_no":"9558587973","emp_type":"Regional Manager","email":"mukesh@alphacapital.in ","password":"mukesh@123","address":"15, Ground Floor, \r\nDosti shoppe Link Dosti Acres,\r\nAntop Hill, \r\nWadala  (East) \r\nMumbai\t- 400 037\r\nMaharashtra","country_id":1,"country_name":"India","state_id":22,"state_name":"Maharashtra","city_id":2707,"city_name":"Mumbai","is_admin":true,"is_active":true,"created_date":"2019-01-16T16:52:32.263","today_activity_count":0,"yesterday_activity_count":0,"day_before_yesterday_activity_count":0},{"$id":"4","PageCount":0,"RowNo":2,"RecordCount":27,"id":18,"first_name":"Ajay","last_name":"Tripathi","contact_no":"8879209990","emp_type":"Operation Manager","email":"ajay@alphacapital.in","password":"ajayt31196","address":"Mumbai","country_id":1,"country_name":"India","state_id":22,"state_name":"Maharashtra","city_id":2707,"city_name":"Mumbai","is_admin":false,"is_active":true,"created_date":"2019-07-29T16:07:56.46","today_activity_count":0,"yesterday_activity_count":0,"day_before_yesterday_activity_count":0},{"$id":"5","PageCount":0,"RowNo":3,"RecordCount":27,"id":19,"first_name":"Gulnaz","last_name":"Khan","contact_no":"8879773158","emp_type":"Operation Manager","email":"Gulnaz@alphacapital.in","password":"gulnaz123","address":"Mumbai","country_id":1,"country_name":"India","state_id":22,"state_name":"Maharashtra","city_id":2707,"city_name":"Mumbai","is_admin":false,"is_active":true,"created_date":"2019-07-29T16:44:06.6","today_activity_count":0,"yesterday_activity_count":0,"day_before_yesterday_activity_count":0},{"$id":"6","PageCount":0,"RowNo":4,"RecordCount":27,"id":20,"first_name":"Ashwarya","last_name":"Kandoi","contact_no":"9664385434","emp_type":"Regional Manager","email":"ashwarya@alphacapital.in","password":"krishna81","address":"SION","country_id":1,"country_name":"India","state_id":22,"state_name":"Maharashtra","city_id":2707,"city_name":"Mumbai","is_admin":false,"is_active":true,"created_date":"2019-07-29T17:33:39.96","today_activity_count":0,"yesterday_activity_count":0,"day_before_yesterday_activity_count":0},{"$id":"7","PageCount":0,"RowNo":5,"RecordCount":27,"id":21,"first_name":"Arif","last_name":"Shaikh","contact_no":"8424912362","emp_type":"Operation Manager","email":"arif@alphacapital.in","password":"arifs31196","address":"SION","country_id":1,"country_name":"India","state_id":22,"state_name":"Maharashtra","city_id":2707,"city_name":"Mumbai","is_admin":false,"is_active":true,"created_date":"2019-07-29T17:42:56.39","today_activity_count":0,"yesterday_activity_count":0,"day_before_yesterday_activity_count":0},{"$id":"8","PageCount":0,"RowNo":6,"RecordCount":27,"id":22,"first_name":"Ranjit","last_name":"Kamble","contact_no":"9768155848","emp_type":"Operation Manager","email":"ranjit@alphacapital.in","password":"ranjitk31196","address":"Dadar","country_id":1,"country_name":"India","state_id":22,"state_name":"Maharashtra","city_id":2707,"city_name":"Mumbai","is_admin":false,"is_active":true,"created_date":"2019-07-29T17:51:38.587","today_activity_count":0,"yesterday_activity_count":0,"day_before_yesterday_activity_count":0},{"$id":"9","PageCount":0,"RowNo":7,"RecordCount":27,"id":23,"first_name":"Manasi","last_name":"Gawade","contact_no":"9769810973","emp_type":"Regional Manager","email":"manasi@alphacapital.in","password":"manasi3198","address":"Mumbai","country_id":1,"country_name":"India","state_id":22,"state_name":"Maharashtra","city_id":2707,"city_name":"Mumbai","is_admin":false,"is_active":true,"created_date":"2019-07-29T17:54:33.443","today_activity_count":0,"yesterday_activity_count":0,"day_before_yesterday_activity_count":0},{"$id":"10","PageCount":0,"RowNo":8,"RecordCount":27,"id":25,"first_name":"Pravin","last_name":"Dhonde","contact_no":"8169205526","emp_type":"Operation Manager","email":"pravin@alphacapital.in","password":"Pravin2693@","address":"Mumbai","country_id":1,"country_name":"India","state_id":22,"state_name":"Maharashtra","city_id":2707,"city_name":"Mumbai","is_admin":false,"is_active":true,"created_date":"2019-07-29T19:05:57.38","today_activity_count":0,"yesterday_activity_count":0,"day_before_yesterday_activity_count":0},{"$id":"11","PageCount":0,"RowNo":9,"RecordCount":27,"id":26,"first_name":"Karambir","last_name":"Yadav","contact_no":"9999238299","emp_type":"Operation Manager","email":"karambir@alphacapital.in","password":"munsi@2014","address":"Gurugram","country_id":1,"country_name":"India","state_id":13,"state_name":"Haryana","city_id":1126,"city_name":"Gurgaon","is_admin":false,"is_active":true,"created_date":"2019-09-25T11:45:08.75","today_activity_count":0,"yesterday_activity_count":0,"day_before_yesterday_activity_count":0},{"$id":"12","PageCount":0,"RowNo":10,"RecordCount":27,"id":27,"first_name":"Kunal","last_name":"Jain","contact_no":"9582044405","emp_type":"Regional Manager","email":"kunal@alphacapital.in","password":"kunal234","address":"Gurgaon","country_id":1,"country_name":"India","state_id":13,"state_name":"Haryana","city_id":1126,"city_name":"Gurgaon","is_admin":false,"is_active":true,"created_date":"2019-09-25T11:51:45.21","today_activity_count":0,"yesterday_activity_count":0,"day_before_yesterday_activity_count":6},{"$id":"13","PageCount":0,"RowNo":11,"RecordCount":27,"id":28,"first_name":"Amit","last_name":"Singh","contact_no":"9810105541","emp_type":"Operation Manager","email":"amit@alphacapital.in","password":"amit123","address":"Gurgaon","country_id":1,"country_name":"India","state_id":13,"state_name":"Haryana","city_id":1126,"city_name":"Gurgaon","is_admin":false,"is_active":true,"created_date":"2019-09-25T11:54:20.503","today_activity_count":0,"yesterday_activity_count":2,"day_before_yesterday_activity_count":5},{"$id":"14","PageCount":0,"RowNo":12,"RecordCount":27,"id":29,"first_name":"Manjeet","last_name":"Singh","contact_no":"9818991340","emp_type":"Regional Manager","email":"manjeet@alphacapital.in","password":"manjeet123","address":"Gurgaon","country_id":1,"country_name":"India","state_id":13,"state_name":"Haryana","city_id":1126,"city_name":"Gurgaon","is_admin":false,"is_active":true,"created_date":"2019-09-25T12:00:11.223","today_activity_count":0,"yesterday_activity_count":0,"day_before_yesterday_activity_count":0},{"$id":"15","PageCount":0,"RowNo":13,"RecordCount":27,"id":30,"first_name":"Neeru","last_name":"Seal","contact_no":"9560823803","emp_type":"Regional Manager","email":"neeru@alphacapital.in","password":"neeru123","address":"Gurgaon","country_id":1,"country_name":"India","state_id":13,"state_name":"Haryana","city_id":1126,"city_name":"Gurgaon","is_admin":false,"is_active":true,"created_date":"2019-09-25T12:06:23.85","today_activity_count":0,"yesterday_activity_count":0,"day_before_yesterday_activity_count":0},{"$id":"16","PageCount":0,"RowNo":14,"RecordCount":27,"id":31,"first_name":"Rohtash","last_name":"Kumar","contact_no":"9873985365","emp_type":"Operation Manager","email":"rohtash@alphacapital.in","password":"rohtash123","address":"Gurgaon","country_id":1,"country_name":"India","state_id":13,"state_name":"Haryana","city_id":1126,"city_name":"Gurgaon","is_admin":false,"is_active":true,"created_date":"2019-09-25T12:10:20.013","today_activity_count":0,"yesterday_activity_count":0,"day_before_yesterday_activity_count":3},{"$id":"17","PageCount":0,"RowNo":15,"RecordCount":27,"id":32,"first_name":"Rakhal","last_name":"Roy","contact_no":"8527624983","emp_type":"Operation Manager","email":"alphacapitalboy2016@gmail.com","password":"roy123","address":"Gurgaon","country_id":1,"country_name":"India","state_id":13,"state_name":"Haryana","city_id":1126,"city_name":"Gurgaon","is_admin":false,"is_active":true,"created_date":"2019-09-25T12:27:59.35","today_activity_count":0,"yesterday_activity_count":0,"day_before_yesterday_activity_count":0},{"$id":"18","PageCount":0,"RowNo":16,"RecordCount":27,"id":33,"first_name":"Tejinder","last_name":"Mandhan","contact_no":"9836024024","emp_type":"Regional Manager","email":"tejinder@alphacapital.in","password":"tejinder123","address":"Kolkata","country_id":1,"country_name":"India","state_id":41,"state_name":"West Bengal","city_id":5583,"city_name":"Kolkata","is_admin":false,"is_active":true,"created_date":"2019-09-25T12:36:07.15","today_activity_count":0,"yesterday_activity_count":0,"day_before_yesterday_activity_count":0},{"$id":"19","PageCount":0,"RowNo":17,"RecordCount":27,"id":34,"first_name":"Bhagirath","last_name":"Sharma","contact_no":"8800681915","emp_type":"Operation Manager","email":"bhagirath@alphacapital.in","password":"Tushar@123","address":"Gurgaon","country_id":1,"country_name":"India","state_id":13,"state_name":"Haryana","city_id":1126,"city_name":"Gurgaon","is_admin":false,"is_active":true,"created_date":"2019-09-25T12:58:12.557","today_activity_count":0,"yesterday_activity_count":0,"day_before_yesterday_activity_count":0},{"$id":"20","PageCount":0,"RowNo":18,"RecordCount":27,"id":35,"first_name":"Debmalya","last_name":"bhattacharya","contact_no":"8902691055","emp_type":"Operation Manager","email":"debmalya@alphacapital.in","password":"debmalya123","address":"Kolkata","country_id":1,"country_name":"India","state_id":41,"state_name":"West Bengal","city_id":5583,"city_name":"Kolkata","is_admin":false,"is_active":true,"created_date":"2019-09-25T13:22:08.123","today_activity_count":0,"yesterday_activity_count":0,"day_before_yesterday_activity_count":0},{"$id":"21","PageCount":0,"RowNo":19,"RecordCount":27,"id":36,"first_name":"Pankaj","last_name":"Kumar","contact_no":"9015615190","emp_type":"Regional Manager","email":"pankaj@alphacapital.in","password":"pankaj123","address":"Gurgaon","country_id":1,"country_name":"India","state_id":13,"state_name":"Haryana","city_id":1126,"city_name":"Gurgaon","is_admin":false,"is_active":true,"created_date":"2019-09-25T13:25:59.493","today_activity_count":0,"yesterday_activity_count":0,"day_before_yesterday_activity_count":0},{"$id":"22","PageCount":0,"RowNo":20,"RecordCount":27,"id":37,"first_name":"Prasenjit","last_name":"Dutta","contact_no":"9126412365","emp_type":"Regional Manager","email":"prasenjit@alphacapital.in","password":"prasenjit123","address":"Silliguri","country_id":1,"country_name":"India","state_id":41,"state_name":"West Bengal","city_id":5707,"city_name":"Siliguri","is_admin":false,"is_active":true,"created_date":"2019-09-25T14:05:09.57","today_activity_count":0,"yesterday_activity_count":0,"day_before_yesterday_activity_count":0},{"$id":"23","PageCount":0,"RowNo":21,"RecordCount":27,"id":38,"first_name":"Sudhangshu","last_name":"Kashyap","contact_no":"9401909560","emp_type":"Regional Manager","email":"sudhangshu@alphacapital.in","password":"sudhangsu123","address":"Gurgaon","country_id":1,"country_name":"India","state_id":13,"state_name":"Haryana","city_id":1126,"city_name":"Gurgaon","is_admin":false,"is_active":true,"created_date":"2019-09-25T15:03:06.593","today_activity_count":0,"yesterday_activity_count":0,"day_before_yesterday_activity_count":0},{"$id":"24","PageCount":0,"RowNo":22,"RecordCount":27,"id":39,"first_name":"Anny","last_name":"Ghosh","contact_no":"9674174034","emp_type":"Regional Manager","email":"anny@alphacapital.in","password":"anny123","address":"Kolkata","country_id":1,"country_name":"India","state_id":41,"state_name":"West Bengal","city_id":5583,"city_name":"Kolkata","is_admin":false,"is_active":true,"created_date":"2019-09-25T15:24:49.56","today_activity_count":0,"yesterday_activity_count":0,"day_before_yesterday_activity_count":0},{"$id":"25","PageCount":0,"RowNo":23,"RecordCount":27,"id":40,"first_name":"Ram","last_name":"Lakhan","contact_no":"9716685058","emp_type":"Operation Manager","email":"ram@alphacapital.in","password":"Ram*123","address":"Gurgaon","country_id":1,"country_name":"India","state_id":13,"state_name":"Haryana","city_id":1126,"city_name":"Gurgaon","is_admin":false,"is_active":true,"created_date":"2019-09-25T15:28:57.54","today_activity_count":0,"yesterday_activity_count":0,"day_before_yesterday_activity_count":0},{"$id":"26","PageCount":0,"RowNo":24,"RecordCount":27,"id":41,"first_name":"Deepti","last_name":"Goel","contact_no":"9810770359","emp_type":"Regional Manager","email":"deepti@alphacapital.in","password":"deepti123","address":"Gurgaon","country_id":1,"country_name":"India","state_id":13,"state_name":"Haryana","city_id":1126,"city_name":"Gurgaon","is_admin":false,"is_active":true,"created_date":"2019-09-25T15:53:53.807","today_activity_count":0,"yesterday_activity_count":0,"day_before_yesterday_activity_count":0},{"$id":"27","PageCount":0,"RowNo":25,"RecordCount":27,"id":42,"first_name":"Nikhil","last_name":"Vikamsey","contact_no":"9787746260","emp_type":"Regional Manager","email":"nikhil@alphacapital.in","password":"nikhil123","address":"coimbatore","country_id":1,"country_name":"India","state_id":35,"state_name":"Tamil Nadu","city_id":3683,"city_name":"Coimbatore","is_admin":false,"is_active":true,"created_date":"2019-09-25T15:59:28.347","today_activity_count":0,"yesterday_activity_count":0,"day_before_yesterday_activity_count":0},{"$id":"28","PageCount":0,"RowNo":26,"RecordCount":27,"id":43,"first_name":"Himanshu","last_name":"Shukla","contact_no":"9999132841","emp_type":"Regional Manager","email":"himanshu@alphacapital.in","password":"himanshu123","address":"Gurgaon","country_id":1,"country_name":"India","state_id":13,"state_name":"Haryana","city_id":1126,"city_name":"Gurgaon","is_admin":false,"is_active":true,"created_date":"2019-09-25T16:05:32.613","today_activity_count":0,"yesterday_activity_count":0,"day_before_yesterday_activity_count":12},{"$id":"29","PageCount":0,"RowNo":27,"RecordCount":27,"id":44,"first_name":"Akhil","last_name":"Bhardwaj","contact_no":"9999596036","emp_type":"Regional Manager","email":"akhil@alphacapital.in","password":"akhil123","address":"Gurgaon","country_id":1,"country_name":"India","state_id":13,"state_name":"Haryana","city_id":1126,"city_name":"Gurgaon","is_admin":true,"is_active":true,"created_date":"2019-09-25T16:11:20.217","today_activity_count":0,"yesterday_activity_count":0,"day_before_yesterday_activity_count":0}],"ClientAssignedEmployee":[]}
     */

    private String message ="";
    private boolean success =false;
    private DataBean data =new DataBean();

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
         * AllEmployee : [{"$id":"3","PageCount":0,"RowNo":1,"RecordCount":27,"id":1,"first_name":"Mukesh","last_name":"Jindal","contact_no":"9558587973","emp_type":"Regional Manager","email":"mukesh@alphacapital.in ","password":"mukesh@123","address":"15, Ground Floor, \r\nDosti shoppe Link Dosti Acres,\r\nAntop Hill, \r\nWadala  (East) \r\nMumbai\t- 400 037\r\nMaharashtra","country_id":1,"country_name":"India","state_id":22,"state_name":"Maharashtra","city_id":2707,"city_name":"Mumbai","is_admin":true,"is_active":true,"created_date":"2019-01-16T16:52:32.263","today_activity_count":0,"yesterday_activity_count":0,"day_before_yesterday_activity_count":0},{"$id":"4","PageCount":0,"RowNo":2,"RecordCount":27,"id":18,"first_name":"Ajay","last_name":"Tripathi","contact_no":"8879209990","emp_type":"Operation Manager","email":"ajay@alphacapital.in","password":"ajayt31196","address":"Mumbai","country_id":1,"country_name":"India","state_id":22,"state_name":"Maharashtra","city_id":2707,"city_name":"Mumbai","is_admin":false,"is_active":true,"created_date":"2019-07-29T16:07:56.46","today_activity_count":0,"yesterday_activity_count":0,"day_before_yesterday_activity_count":0},{"$id":"5","PageCount":0,"RowNo":3,"RecordCount":27,"id":19,"first_name":"Gulnaz","last_name":"Khan","contact_no":"8879773158","emp_type":"Operation Manager","email":"Gulnaz@alphacapital.in","password":"gulnaz123","address":"Mumbai","country_id":1,"country_name":"India","state_id":22,"state_name":"Maharashtra","city_id":2707,"city_name":"Mumbai","is_admin":false,"is_active":true,"created_date":"2019-07-29T16:44:06.6","today_activity_count":0,"yesterday_activity_count":0,"day_before_yesterday_activity_count":0},{"$id":"6","PageCount":0,"RowNo":4,"RecordCount":27,"id":20,"first_name":"Ashwarya","last_name":"Kandoi","contact_no":"9664385434","emp_type":"Regional Manager","email":"ashwarya@alphacapital.in","password":"krishna81","address":"SION","country_id":1,"country_name":"India","state_id":22,"state_name":"Maharashtra","city_id":2707,"city_name":"Mumbai","is_admin":false,"is_active":true,"created_date":"2019-07-29T17:33:39.96","today_activity_count":0,"yesterday_activity_count":0,"day_before_yesterday_activity_count":0},{"$id":"7","PageCount":0,"RowNo":5,"RecordCount":27,"id":21,"first_name":"Arif","last_name":"Shaikh","contact_no":"8424912362","emp_type":"Operation Manager","email":"arif@alphacapital.in","password":"arifs31196","address":"SION","country_id":1,"country_name":"India","state_id":22,"state_name":"Maharashtra","city_id":2707,"city_name":"Mumbai","is_admin":false,"is_active":true,"created_date":"2019-07-29T17:42:56.39","today_activity_count":0,"yesterday_activity_count":0,"day_before_yesterday_activity_count":0},{"$id":"8","PageCount":0,"RowNo":6,"RecordCount":27,"id":22,"first_name":"Ranjit","last_name":"Kamble","contact_no":"9768155848","emp_type":"Operation Manager","email":"ranjit@alphacapital.in","password":"ranjitk31196","address":"Dadar","country_id":1,"country_name":"India","state_id":22,"state_name":"Maharashtra","city_id":2707,"city_name":"Mumbai","is_admin":false,"is_active":true,"created_date":"2019-07-29T17:51:38.587","today_activity_count":0,"yesterday_activity_count":0,"day_before_yesterday_activity_count":0},{"$id":"9","PageCount":0,"RowNo":7,"RecordCount":27,"id":23,"first_name":"Manasi","last_name":"Gawade","contact_no":"9769810973","emp_type":"Regional Manager","email":"manasi@alphacapital.in","password":"manasi3198","address":"Mumbai","country_id":1,"country_name":"India","state_id":22,"state_name":"Maharashtra","city_id":2707,"city_name":"Mumbai","is_admin":false,"is_active":true,"created_date":"2019-07-29T17:54:33.443","today_activity_count":0,"yesterday_activity_count":0,"day_before_yesterday_activity_count":0},{"$id":"10","PageCount":0,"RowNo":8,"RecordCount":27,"id":25,"first_name":"Pravin","last_name":"Dhonde","contact_no":"8169205526","emp_type":"Operation Manager","email":"pravin@alphacapital.in","password":"Pravin2693@","address":"Mumbai","country_id":1,"country_name":"India","state_id":22,"state_name":"Maharashtra","city_id":2707,"city_name":"Mumbai","is_admin":false,"is_active":true,"created_date":"2019-07-29T19:05:57.38","today_activity_count":0,"yesterday_activity_count":0,"day_before_yesterday_activity_count":0},{"$id":"11","PageCount":0,"RowNo":9,"RecordCount":27,"id":26,"first_name":"Karambir","last_name":"Yadav","contact_no":"9999238299","emp_type":"Operation Manager","email":"karambir@alphacapital.in","password":"munsi@2014","address":"Gurugram","country_id":1,"country_name":"India","state_id":13,"state_name":"Haryana","city_id":1126,"city_name":"Gurgaon","is_admin":false,"is_active":true,"created_date":"2019-09-25T11:45:08.75","today_activity_count":0,"yesterday_activity_count":0,"day_before_yesterday_activity_count":0},{"$id":"12","PageCount":0,"RowNo":10,"RecordCount":27,"id":27,"first_name":"Kunal","last_name":"Jain","contact_no":"9582044405","emp_type":"Regional Manager","email":"kunal@alphacapital.in","password":"kunal234","address":"Gurgaon","country_id":1,"country_name":"India","state_id":13,"state_name":"Haryana","city_id":1126,"city_name":"Gurgaon","is_admin":false,"is_active":true,"created_date":"2019-09-25T11:51:45.21","today_activity_count":0,"yesterday_activity_count":0,"day_before_yesterday_activity_count":6},{"$id":"13","PageCount":0,"RowNo":11,"RecordCount":27,"id":28,"first_name":"Amit","last_name":"Singh","contact_no":"9810105541","emp_type":"Operation Manager","email":"amit@alphacapital.in","password":"amit123","address":"Gurgaon","country_id":1,"country_name":"India","state_id":13,"state_name":"Haryana","city_id":1126,"city_name":"Gurgaon","is_admin":false,"is_active":true,"created_date":"2019-09-25T11:54:20.503","today_activity_count":0,"yesterday_activity_count":2,"day_before_yesterday_activity_count":5},{"$id":"14","PageCount":0,"RowNo":12,"RecordCount":27,"id":29,"first_name":"Manjeet","last_name":"Singh","contact_no":"9818991340","emp_type":"Regional Manager","email":"manjeet@alphacapital.in","password":"manjeet123","address":"Gurgaon","country_id":1,"country_name":"India","state_id":13,"state_name":"Haryana","city_id":1126,"city_name":"Gurgaon","is_admin":false,"is_active":true,"created_date":"2019-09-25T12:00:11.223","today_activity_count":0,"yesterday_activity_count":0,"day_before_yesterday_activity_count":0},{"$id":"15","PageCount":0,"RowNo":13,"RecordCount":27,"id":30,"first_name":"Neeru","last_name":"Seal","contact_no":"9560823803","emp_type":"Regional Manager","email":"neeru@alphacapital.in","password":"neeru123","address":"Gurgaon","country_id":1,"country_name":"India","state_id":13,"state_name":"Haryana","city_id":1126,"city_name":"Gurgaon","is_admin":false,"is_active":true,"created_date":"2019-09-25T12:06:23.85","today_activity_count":0,"yesterday_activity_count":0,"day_before_yesterday_activity_count":0},{"$id":"16","PageCount":0,"RowNo":14,"RecordCount":27,"id":31,"first_name":"Rohtash","last_name":"Kumar","contact_no":"9873985365","emp_type":"Operation Manager","email":"rohtash@alphacapital.in","password":"rohtash123","address":"Gurgaon","country_id":1,"country_name":"India","state_id":13,"state_name":"Haryana","city_id":1126,"city_name":"Gurgaon","is_admin":false,"is_active":true,"created_date":"2019-09-25T12:10:20.013","today_activity_count":0,"yesterday_activity_count":0,"day_before_yesterday_activity_count":3},{"$id":"17","PageCount":0,"RowNo":15,"RecordCount":27,"id":32,"first_name":"Rakhal","last_name":"Roy","contact_no":"8527624983","emp_type":"Operation Manager","email":"alphacapitalboy2016@gmail.com","password":"roy123","address":"Gurgaon","country_id":1,"country_name":"India","state_id":13,"state_name":"Haryana","city_id":1126,"city_name":"Gurgaon","is_admin":false,"is_active":true,"created_date":"2019-09-25T12:27:59.35","today_activity_count":0,"yesterday_activity_count":0,"day_before_yesterday_activity_count":0},{"$id":"18","PageCount":0,"RowNo":16,"RecordCount":27,"id":33,"first_name":"Tejinder","last_name":"Mandhan","contact_no":"9836024024","emp_type":"Regional Manager","email":"tejinder@alphacapital.in","password":"tejinder123","address":"Kolkata","country_id":1,"country_name":"India","state_id":41,"state_name":"West Bengal","city_id":5583,"city_name":"Kolkata","is_admin":false,"is_active":true,"created_date":"2019-09-25T12:36:07.15","today_activity_count":0,"yesterday_activity_count":0,"day_before_yesterday_activity_count":0},{"$id":"19","PageCount":0,"RowNo":17,"RecordCount":27,"id":34,"first_name":"Bhagirath","last_name":"Sharma","contact_no":"8800681915","emp_type":"Operation Manager","email":"bhagirath@alphacapital.in","password":"Tushar@123","address":"Gurgaon","country_id":1,"country_name":"India","state_id":13,"state_name":"Haryana","city_id":1126,"city_name":"Gurgaon","is_admin":false,"is_active":true,"created_date":"2019-09-25T12:58:12.557","today_activity_count":0,"yesterday_activity_count":0,"day_before_yesterday_activity_count":0},{"$id":"20","PageCount":0,"RowNo":18,"RecordCount":27,"id":35,"first_name":"Debmalya","last_name":"bhattacharya","contact_no":"8902691055","emp_type":"Operation Manager","email":"debmalya@alphacapital.in","password":"debmalya123","address":"Kolkata","country_id":1,"country_name":"India","state_id":41,"state_name":"West Bengal","city_id":5583,"city_name":"Kolkata","is_admin":false,"is_active":true,"created_date":"2019-09-25T13:22:08.123","today_activity_count":0,"yesterday_activity_count":0,"day_before_yesterday_activity_count":0},{"$id":"21","PageCount":0,"RowNo":19,"RecordCount":27,"id":36,"first_name":"Pankaj","last_name":"Kumar","contact_no":"9015615190","emp_type":"Regional Manager","email":"pankaj@alphacapital.in","password":"pankaj123","address":"Gurgaon","country_id":1,"country_name":"India","state_id":13,"state_name":"Haryana","city_id":1126,"city_name":"Gurgaon","is_admin":false,"is_active":true,"created_date":"2019-09-25T13:25:59.493","today_activity_count":0,"yesterday_activity_count":0,"day_before_yesterday_activity_count":0},{"$id":"22","PageCount":0,"RowNo":20,"RecordCount":27,"id":37,"first_name":"Prasenjit","last_name":"Dutta","contact_no":"9126412365","emp_type":"Regional Manager","email":"prasenjit@alphacapital.in","password":"prasenjit123","address":"Silliguri","country_id":1,"country_name":"India","state_id":41,"state_name":"West Bengal","city_id":5707,"city_name":"Siliguri","is_admin":false,"is_active":true,"created_date":"2019-09-25T14:05:09.57","today_activity_count":0,"yesterday_activity_count":0,"day_before_yesterday_activity_count":0},{"$id":"23","PageCount":0,"RowNo":21,"RecordCount":27,"id":38,"first_name":"Sudhangshu","last_name":"Kashyap","contact_no":"9401909560","emp_type":"Regional Manager","email":"sudhangshu@alphacapital.in","password":"sudhangsu123","address":"Gurgaon","country_id":1,"country_name":"India","state_id":13,"state_name":"Haryana","city_id":1126,"city_name":"Gurgaon","is_admin":false,"is_active":true,"created_date":"2019-09-25T15:03:06.593","today_activity_count":0,"yesterday_activity_count":0,"day_before_yesterday_activity_count":0},{"$id":"24","PageCount":0,"RowNo":22,"RecordCount":27,"id":39,"first_name":"Anny","last_name":"Ghosh","contact_no":"9674174034","emp_type":"Regional Manager","email":"anny@alphacapital.in","password":"anny123","address":"Kolkata","country_id":1,"country_name":"India","state_id":41,"state_name":"West Bengal","city_id":5583,"city_name":"Kolkata","is_admin":false,"is_active":true,"created_date":"2019-09-25T15:24:49.56","today_activity_count":0,"yesterday_activity_count":0,"day_before_yesterday_activity_count":0},{"$id":"25","PageCount":0,"RowNo":23,"RecordCount":27,"id":40,"first_name":"Ram","last_name":"Lakhan","contact_no":"9716685058","emp_type":"Operation Manager","email":"ram@alphacapital.in","password":"Ram*123","address":"Gurgaon","country_id":1,"country_name":"India","state_id":13,"state_name":"Haryana","city_id":1126,"city_name":"Gurgaon","is_admin":false,"is_active":true,"created_date":"2019-09-25T15:28:57.54","today_activity_count":0,"yesterday_activity_count":0,"day_before_yesterday_activity_count":0},{"$id":"26","PageCount":0,"RowNo":24,"RecordCount":27,"id":41,"first_name":"Deepti","last_name":"Goel","contact_no":"9810770359","emp_type":"Regional Manager","email":"deepti@alphacapital.in","password":"deepti123","address":"Gurgaon","country_id":1,"country_name":"India","state_id":13,"state_name":"Haryana","city_id":1126,"city_name":"Gurgaon","is_admin":false,"is_active":true,"created_date":"2019-09-25T15:53:53.807","today_activity_count":0,"yesterday_activity_count":0,"day_before_yesterday_activity_count":0},{"$id":"27","PageCount":0,"RowNo":25,"RecordCount":27,"id":42,"first_name":"Nikhil","last_name":"Vikamsey","contact_no":"9787746260","emp_type":"Regional Manager","email":"nikhil@alphacapital.in","password":"nikhil123","address":"coimbatore","country_id":1,"country_name":"India","state_id":35,"state_name":"Tamil Nadu","city_id":3683,"city_name":"Coimbatore","is_admin":false,"is_active":true,"created_date":"2019-09-25T15:59:28.347","today_activity_count":0,"yesterday_activity_count":0,"day_before_yesterday_activity_count":0},{"$id":"28","PageCount":0,"RowNo":26,"RecordCount":27,"id":43,"first_name":"Himanshu","last_name":"Shukla","contact_no":"9999132841","emp_type":"Regional Manager","email":"himanshu@alphacapital.in","password":"himanshu123","address":"Gurgaon","country_id":1,"country_name":"India","state_id":13,"state_name":"Haryana","city_id":1126,"city_name":"Gurgaon","is_admin":false,"is_active":true,"created_date":"2019-09-25T16:05:32.613","today_activity_count":0,"yesterday_activity_count":0,"day_before_yesterday_activity_count":12},{"$id":"29","PageCount":0,"RowNo":27,"RecordCount":27,"id":44,"first_name":"Akhil","last_name":"Bhardwaj","contact_no":"9999596036","emp_type":"Regional Manager","email":"akhil@alphacapital.in","password":"akhil123","address":"Gurgaon","country_id":1,"country_name":"India","state_id":13,"state_name":"Haryana","city_id":1126,"city_name":"Gurgaon","is_admin":true,"is_active":true,"created_date":"2019-09-25T16:11:20.217","today_activity_count":0,"yesterday_activity_count":0,"day_before_yesterday_activity_count":0}]
         * ClientAssignedEmployee : []
         */

        private List<AllEmployeeBean> AllEmployee = new ArrayList<>();
        private List<?> ClientAssignedEmployee = new ArrayList<>();

        public List<AllEmployeeBean> getAllEmployee() {
            return AllEmployee;
        }

        public void setAllEmployee(List<AllEmployeeBean> AllEmployee) {
            this.AllEmployee = AllEmployee;
        }

        public List<?> getClientAssignedEmployee() {
            return ClientAssignedEmployee;
        }

        public void setClientAssignedEmployee(List<?> ClientAssignedEmployee) {
            this.ClientAssignedEmployee = ClientAssignedEmployee;
        }

        public static class AllEmployeeBean {
            /**
             * $id : 3
             * PageCount : 0
             * RowNo : 1
             * RecordCount : 27
             * id : 1
             * first_name : Mukesh
             * last_name : Jindal
             * contact_no : 9558587973
             * emp_type : Regional Manager
             * email : mukesh@alphacapital.in
             * password : mukesh@123
             * address : 15, Ground Floor,
             Dosti shoppe Link Dosti Acres,
             Antop Hill,
             Wadala  (East)
             Mumbai	- 400 037
             Maharashtra
             * country_id : 1
             * country_name : India
             * state_id : 22
             * state_name : Maharashtra
             * city_id : 2707
             * city_name : Mumbai
             * is_admin : true
             * is_active : true
             * created_date : 2019-01-16T16:52:32.263
             * today_activity_count : 0
             * yesterday_activity_count : 0
             * day_before_yesterday_activity_count : 0
             */

            private int id = 0;
            private String first_name = "";
            private String last_name = "";
            private String contact_no = "";
            private String emp_type = "";
            private String email = "";
            private String password = "";
            private String address = "";
            private int country_id = 0;
            private String country_name = "";
            private int state_id = 0;
            private String state_name = "";
            private int city_id = 0;
            private String city_name = "";
            private boolean is_admin = false;
            private boolean is_active = false;
            private String created_date = "";
            private int today_activity_count = 0;
            private int yesterday_activity_count = 0;
            private int day_before_yesterday_activity_count = 0;
            private String birthdate = "";
            private String parents_name = "";
            private String spouse_name = "";
            private String children_name = "";
            private String degrees = "";
            private String examinations_cleared = "";

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

            public String getEmp_type() {
                return emp_type;
            }

            public void setEmp_type(String emp_type) {
                this.emp_type = emp_type;
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

            public String getCountry_name() {
                return country_name;
            }

            public void setCountry_name(String country_name) {
                this.country_name = country_name;
            }

            public int getState_id() {
                return state_id;
            }

            public void setState_id(int state_id) {
                this.state_id = state_id;
            }

            public String getState_name() {
                return state_name;
            }

            public void setState_name(String state_name) {
                this.state_name = state_name;
            }

            public int getCity_id() {
                return city_id;
            }

            public void setCity_id(int city_id) {
                this.city_id = city_id;
            }

            public String getCity_name() {
                return city_name;
            }

            public void setCity_name(String city_name) {
                this.city_name = city_name;
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

            public String getCreated_date() {
                return created_date;
            }

            public void setCreated_date(String created_date) {
                this.created_date = created_date;
            }

            public int getToday_activity_count() {
                return today_activity_count;
            }

            public void setToday_activity_count(int today_activity_count) {
                this.today_activity_count = today_activity_count;
            }

            public int getYesterday_activity_count() {
                return yesterday_activity_count;
            }

            public void setYesterday_activity_count(int yesterday_activity_count) {
                this.yesterday_activity_count = yesterday_activity_count;
            }

            public int getDay_before_yesterday_activity_count() {
                return day_before_yesterday_activity_count;
            }

            public void setDay_before_yesterday_activity_count(int day_before_yesterday_activity_count) {
                this.day_before_yesterday_activity_count = day_before_yesterday_activity_count;
            }

            public String getBirthdate() {
                return birthdate;
            }

            public void setBirthdate(String birthdate) {
                this.birthdate = birthdate;
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
        }
    }
}
