package cheetah.alphacapital.reportApp.getset;

/**
 * Created by Ravi Patel on 25-02-2019.
 */
public class SelfAcquiredAUMGetSet
{

    /**
     * $id : 3
     * Id : 2
     * employee_id : 3
     * Year : 2019
     * AUM_Amount : 5000000
     * Note : Get amount from ravi patel
     * created_date : 2019-02-15T15:06:17.773
     * timetstamp : AAAAAAAKQGw=
     */
    private int Id;
    private int employee_id;
    private int Year;
    private double AUM_Amount;
    private String Note;
    private String created_date;
    private String timetstamp;

    public SelfAcquiredAUMGetSet(int id, int employee_id, int year, double AUM_Amount, String note, String created_date)
    {
        Id = id;
        this.employee_id = employee_id;
        Year = year;
        this.AUM_Amount = AUM_Amount;
        Note = note;
        this.created_date = created_date;
    }

    public int getId()
    {
        return Id;
    }

    public void setId(int Id)
    {
        this.Id = Id;
    }

    public int getEmployee_id()
    {
        return employee_id;
    }

    public void setEmployee_id(int employee_id)
    {
        this.employee_id = employee_id;
    }

    public int getYear()
    {
        return Year;
    }

    public void setYear(int Year)
    {
        this.Year = Year;
    }

    public double getAUM_Amount()
    {
        return AUM_Amount;
    }

    public void setAUM_Amount(double AUM_Amount)
    {
        this.AUM_Amount = AUM_Amount;
    }

    public String getNote()
    {
        return Note;
    }

    public void setNote(String Note)
    {
        this.Note = Note;
    }

    public String getCreated_date()
    {
        return created_date;
    }

    public void setCreated_date(String created_date)
    {
        this.created_date = created_date;
    }

    public String getTimetstamp()
    {
        return timetstamp;
    }

    public void setTimetstamp(String timetstamp)
    {
        this.timetstamp = timetstamp;
    }
}
