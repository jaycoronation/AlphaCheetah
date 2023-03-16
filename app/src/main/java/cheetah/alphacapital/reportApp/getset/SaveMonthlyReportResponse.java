package cheetah.alphacapital.reportApp.getset;

public class SaveMonthlyReportResponse {
 private String $id;
 private String message;
 private boolean success;
 Data DataObject;


 // Getter Methods 

 public String get$id() {
  return $id;
 }

 public String getMessage() {
  return message;
 }

 public boolean getSuccess() {
  return success;
 }

 public Data getData() {
  return DataObject;
 }

 // Setter Methods 

 public void set$id(String $id) {
  this.$id = $id;
 }

 public void setMessage(String message) {
  this.message = message;
 }

 public void setSuccess(boolean success) {
  this.success = success;
 }

 public void setData(Data dataObject) {
  this.DataObject = dataObject;
 }

 public class Data {
  private String $id;
  private float Id;
  private float employee_id;
  private float NumberOfPortfolios;
  private String LastDateOfPortfolio;
  private float Attendance;
  private float KnowledgeSessions;
  private float DAR;
  private float SelfAcquiredAUM;
  private float AUM_Month;
  private float AUM_Year;
  private String created_date;
  private String timetstamp;


  // Getter Methods

  public String get$id() {
   return $id;
  }

  public float getId() {
   return Id;
  }

  public float getEmployee_id() {
   return employee_id;
  }

  public float getNumberOfPortfolios() {
   return NumberOfPortfolios;
  }

  public String getLastDateOfPortfolio() {
   return LastDateOfPortfolio;
  }

  public float getAttendance() {
   return Attendance;
  }

  public float getKnowledgeSessions() {
   return KnowledgeSessions;
  }

  public float getDAR() {
   return DAR;
  }

  public float getSelfAcquiredAUM() {
   return SelfAcquiredAUM;
  }

  public float getAUM_Month() {
   return AUM_Month;
  }

  public float getAUM_Year() {
   return AUM_Year;
  }

  public String getCreated_date() {
   return created_date;
  }

  public String getTimetstamp() {
   return timetstamp;
  }

  // Setter Methods

  public void set$id(String $id) {
   this.$id = $id;
  }

  public void setId(float Id) {
   this.Id = Id;
  }

  public void setEmployee_id(float employee_id) {
   this.employee_id = employee_id;
  }

  public void setNumberOfPortfolios(float NumberOfPortfolios) {
   this.NumberOfPortfolios = NumberOfPortfolios;
  }

  public void setLastDateOfPortfolio(String LastDateOfPortfolio) {
   this.LastDateOfPortfolio = LastDateOfPortfolio;
  }

  public void setAttendance(float Attendance) {
   this.Attendance = Attendance;
  }

  public void setKnowledgeSessions(float KnowledgeSessions) {
   this.KnowledgeSessions = KnowledgeSessions;
  }

  public void setDAR(float DAR) {
   this.DAR = DAR;
  }

  public void setSelfAcquiredAUM(float SelfAcquiredAUM) {
   this.SelfAcquiredAUM = SelfAcquiredAUM;
  }

  public void setAUM_Month(float AUM_Month) {
   this.AUM_Month = AUM_Month;
  }

  public void setAUM_Year(float AUM_Year) {
   this.AUM_Year = AUM_Year;
  }

  public void setCreated_date(String created_date) {
   this.created_date = created_date;
  }

  public void setTimetstamp(String timetstamp) {
   this.timetstamp = timetstamp;
  }
 }

}