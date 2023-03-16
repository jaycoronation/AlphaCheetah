package cheetah.alphacapital.reportApp.getset;

/**
 * Created by Ravi Patel on 20-02-2019.
 */
public class DashboardPagerGetSet
{
    private String title = "";
    private String remaining = "";
    private String total = "";


    public DashboardPagerGetSet(String title, String remaining, String total)
    {
        this.title = title;
        this.remaining = remaining;
        this.total = total;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getRemaining()
    {
        return remaining;
    }

    public void setRemaining(String remaining)
    {
        this.remaining = remaining;
    }

    public String getTotal()
    {
        return total;
    }

    public void setTotal(String total)
    {
        this.total = total;
    }
}
