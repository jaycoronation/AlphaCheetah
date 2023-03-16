package cheetah.alphacapital.textutils;

import android.content.Context;
import android.graphics.Typeface;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.TypedValue;

import cheetah.alphacapital.R;

public class CustomTextViewSemiBold extends AppCompatTextView
{
    public CustomTextViewSemiBold(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        if (!isInEditMode())
        {
            setType(context);
        }
    }

    public CustomTextViewSemiBold(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        if (!isInEditMode())
        {
            setType(context);
        }
    }

    public CustomTextViewSemiBold(Context context)
    {
        super(context);
        if (!isInEditMode())
        {
            setType(context);
        }
    }

    private void setType(Context context)
    {
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), context.getResources().getString(R.string.font_semibold)));
        this.setLineSpacing(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4.0f,  getResources().getDisplayMetrics()), 1.0f);
    }
}
