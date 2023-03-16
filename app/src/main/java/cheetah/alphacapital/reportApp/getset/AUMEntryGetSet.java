package cheetah.alphacapital.reportApp.getset;

public class AUMEntryGetSet
{
    private double AUM_Amount = 0;
    private String Note = "";
    private String Year = "";

    public double getAUM_Amount() {
        return AUM_Amount;
    }

    public void setAUM_Amount(double AUM_Amount) {
        this.AUM_Amount = AUM_Amount;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }
}
