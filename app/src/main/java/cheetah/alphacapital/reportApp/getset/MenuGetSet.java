package cheetah.alphacapital.reportApp.getset;

import android.graphics.drawable.Drawable;

/**
 * Created by Ravi Patel on 04-06-2019.
 */
public class MenuGetSet
{
    String menuId ="",menuName ="";
    Drawable drawable;

    public MenuGetSet()
    {
    }

    public MenuGetSet(String menuId, String menuName, Drawable drawable)
    {
        this.menuId = menuId;
        this.menuName = menuName;
        this.drawable = drawable;
    }



    public String getMenuName()
    {
        return menuName;
    }

    public void setMenuName(String menuName)
    {
        this.menuName = menuName;
    }

    public String getMenuId()
    {
        return menuId;
    }

    public void setMenuId(String menuId)
    {
        this.menuId = menuId;
    }

    public Drawable getDrawable()
    {
        return drawable;
    }

    public void setDrawable(Drawable drawable)
    {
        this.drawable = drawable;
    }
}
