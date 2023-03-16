package cheetah.alphacapital.reportApp.getset;

import java.util.List;

public class LeadsResponseModel
{
	private List<DataItem> data;
	private boolean success;
	private Object message;
	private String id;

	public void setData(List<DataItem> data){
		this.data = data;
	}

	public List<DataItem> getData(){
		return data;
	}

	public void setSuccess(boolean success){
		this.success = success;
	}

	public boolean isSuccess(){
		return success;
	}

	public void setMessage(Object message){
		this.message = message;
	}

	public Object getMessage(){
		return message;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public class DataItem{
		private String Gold;
		private String A;
		private double total;
		private String B;
		private String C;
		private String month;
		private String D;
		private String E;
		private String currentYear;
		private String full_month;
		private String $id;

		public void setGold(String gold){
			this.Gold = gold;
		}

		public String getGold(){
			return Gold;
		}

		public void setA(String A){
			this.A = A;
		}

		public String getA(){
			return A;
		}

		public void setTotal(double total){
			this.total = total;
		}

		public double getTotal(){
			return total;
		}

		public void setB(String B){
			this.B = B;
		}

		public String getB(){
			return B;
		}

		public void setC(String C){
			this.C = C;
		}

		public String getC(){
			return C;
		}

		public void setMonth(String month){
			this.month = month;
		}

		public String getMonth(){
			return month;
		}

		public void setD(String D){
			this.D = D;
		}

		public String getD(){
			return D;
		}

		public void setE(String E){
			this.E = E;
		}

		public String getE(){
			return E;
		}

		public void setCurrentYear(String currentYear){
			this.currentYear = currentYear;
		}

		public String getCurrentYear(){
			return currentYear;
		}

		public void setFullMonth(String fullMonth){
			this.full_month = fullMonth;
		}

		public String getFullMonth(){
			return full_month;
		}

		public void setId(String id){
			this.$id = id;
		}

		public String getId(){
			return $id;
		}
	}

}