package cheetah.alphacapital.reportApp.activity.admin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cheetah.alphacapital.network.ApiClient;
import cheetah.alphacapital.network.ApiInterface;
import cheetah.alphacapital.textutils.SnackbarHelper;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.utils.SessionManager;
import cheetah.alphacapital.R;


public class BaseActivity extends AppCompatActivity
{
    public Activity activity;
    public SessionManager sessionManager;
    public ApiInterface apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        activity = this;
        sessionManager = new SessionManager(activity);
        apiService = ApiClient.getClient().create(ApiInterface.class);
    }

    public String getStringFromEditText(EditText editText)
    {
        return editText.getText().toString().trim();
    }

    public void setStatusBar(Activity activity)
    {
        Window window = activity.getWindow();
        View view = window.getDecorView();
        setLightStatusBar(view, activity);
    }

    void openKeyboard(View view)
    {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(view.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
    }

    public void setLightStatusBar(View view, Activity activity)
    {
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, R.color.colorPrimary));
        }*/
    }

    public static double roundMyData(double Rval, int numberOfDigitsAfterDecimal) {
        double p = (float)Math.pow(10,numberOfDigitsAfterDecimal);
        Rval = Rval * p;
        double tmp = Math.floor(Rval);
        return (double)tmp/p;
    }

    public void callIntent(String number)
    {
        Intent intentDial = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
        startActivity(intentDial);
    }

    public void composeEmail(String[] addresses, String subject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void hideStatusBar()
    {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void startActivityAnimation()
    {
        hideKeyboard();
        //activity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public void finishActivityAnimation()
    {
        hideKeyboard();
        //activity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    public void hideKeyboard()
    {
        View view = activity.findViewById(android.R.id.content);
        if (view != null)
        {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public boolean isEditTextEmpty(EditText editText)
    {
        return editText.getText().toString().trim().length() == 0;
    }

    public void showSnackBar(String message)
    {
        View view = activity.getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            try
            {
                Snackbar snack = Snackbar.make(view, message, Toast.LENGTH_SHORT);
                SnackbarHelper.configSnackbar(activity, snack);
                snack.show();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            try
            {
                int color = ContextCompat.getColor(activity, R.color.colorPrimary);
                Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
                textView.setTextColor(Color.parseColor("#FFFFFF"));
                textView.setTypeface(AppUtils.getTypefaceRegular(activity));
                sbView.setBackgroundColor(color);
                snackbar.show();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public void noInternetSnackBar()
    {
        View view = activity.getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            try
            {
                Snackbar snack = Snackbar.make(view, activity.getString(R.string.network_failed_message), Toast.LENGTH_SHORT);
                SnackbarHelper.configSnackbar(activity, snack);
                snack.show();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            try
            {
                int color = ContextCompat.getColor(activity, R.color.colorPrimary);
                Snackbar snackbar = Snackbar.make(view, activity.getString(R.string.network_failed_message), Snackbar.LENGTH_SHORT);
                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
                textView.setTextColor(Color.parseColor("#FFFFFF"));
                textView.setTypeface(AppUtils.getTypefaceRegular(activity));
                sbView.setBackgroundColor(color);
                snackbar.show();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public void apiFailedSnackBar()
    {
        View view = activity.getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            try
            {
                Snackbar snack = Snackbar.make(view, activity.getString(R.string.api_failed), Toast.LENGTH_SHORT);
                SnackbarHelper.configSnackbar(activity, snack);
                snack.show();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            try
            {
                int color = ContextCompat.getColor(activity, R.color.colorPrimary);
                Snackbar snackbar = Snackbar.make(view, activity.getString(R.string.api_failed), Snackbar.LENGTH_SHORT);
                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
                textView.setTextColor(Color.parseColor("#FFFFFF"));
                textView.setTypeface(AppUtils.getTypefaceRegular(activity));
                sbView.setBackgroundColor(color);
                snackbar.show();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public void showToast(String msg)
    {
        try
        {
            if (activity != null)
            {
                LayoutInflater inflater = activity.getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast, null);
                TextView text = (TextView) layout.findViewById(R.id.text);
                text.setText(msg);
                Toast toast = new Toast(activity);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);
                if (toast.getView().isShown())
                {
                    toast.cancel();
                }
                else
                {
                    toast.show();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
