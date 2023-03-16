package cheetah.alphacapital.textutils;

import android.content.Context;
import android.graphics.Typeface;
import com.google.android.material.textfield.TextInputEditText;
import android.util.AttributeSet;

import cheetah.alphacapital.R;

public class CustomAppEditText extends TextInputEditText
{
    public CustomAppEditText(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        setType(context);
    }

    public CustomAppEditText(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setType(context);
    }

    public CustomAppEditText(Context context)
    {
        super(context);
        setType(context);
    }

    private void setType(Context context)
    {
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), context.getResources().getString(R.string.font_regular)));
    }
}
