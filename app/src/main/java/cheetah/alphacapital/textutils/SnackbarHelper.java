package cheetah.alphacapital.textutils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresApi;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.view.ViewCompat;
import android.view.ViewGroup;
import android.widget.TextView;

import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.R;


public class SnackbarHelper
{
    private Context context;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void configSnackbar(Activity context, Snackbar snack)
    {
        context = context;
        addMargins(snack);
        setRoundBordersBg(context, snack);
        ViewCompat.setElevation(snack.getView(), 6f);
    }

    private static void addMargins(Snackbar snack)
    {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) snack.getView().getLayoutParams();
        params.setMargins(12, 12, 12, 20);
        snack.getView().setLayoutParams(params);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static void setRoundBordersBg(Activity context, Snackbar snackbar)
    {
        snackbar.getView().setBackground(context.getDrawable(R.drawable.snackbar_bg));
        TextView textView = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
        textView.setMaxLines(5);
        textView.setTypeface(AppUtils.getTypefaceRegular(context));
    }
}
