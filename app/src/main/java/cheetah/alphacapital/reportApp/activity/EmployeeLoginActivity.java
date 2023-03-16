package cheetah.alphacapital.reportApp.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.Toolbar;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONObject;

import java.util.HashMap;

import cheetah.alphacapital.textutils.CustomTextInputLayout;
import cheetah.alphacapital.utils.AppAPIUtils;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.utils.SessionManager;
import cheetah.alphacapital.R;


public class EmployeeLoginActivity extends AppCompatActivity
{
    private Activity activity;
    private SessionManager sessionManager;
    private LinearLayout llLoading;
    private ProgressBar pbLoading;
    private TextView txtLoading;
    private LinearLayout llNoInternet;
    private LinearLayout llRetry;
    private TextView txtRetry;
    private TextInputLayout inputEmail;
    private EditText edtEmail;
    private TextInputLayout inputPassword;
    private EditText edtPassword;
    private AppCompatCheckBox checkBox;
    private LinearLayout llForgotPassword;
    private LinearLayout llSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            {
                Window w = getWindow();
                w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        try
        {
            super.onCreate(savedInstanceState);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        activity = this;

        sessionManager = new SessionManager(activity);

        setContentView(R.layout.activity_employee_login);

        setupViews();

        onClickEvents();
    }

    private void setupViews()
    {
        try
        {
            llLoading = (LinearLayout) findViewById(R.id.llLoading);
            pbLoading = (ProgressBar) findViewById(R.id.pbLoading);
            txtLoading = (TextView) findViewById(R.id.txtLoading);
            llNoInternet = (LinearLayout) findViewById(R.id.llNoInternet);
            llRetry = (LinearLayout) findViewById(R.id.llRetry);
            txtRetry = (TextView) findViewById(R.id.txtRetry);
            inputEmail = (TextInputLayout) findViewById(R.id.inputEmail);
            edtEmail = (EditText) findViewById(R.id.edtEmail);
            inputPassword = (TextInputLayout) findViewById(R.id.inputPassword);
            edtPassword = (EditText) findViewById(R.id.edtPassword);
            checkBox = (AppCompatCheckBox) findViewById(R.id.checkBox);
            llForgotPassword = (LinearLayout) findViewById(R.id.llForgotPassword);
            llSubmit = (LinearLayout) findViewById(R.id.llSubmit);
            checkBox.setTypeface(AppUtils.getTypefaceRegular(activity));
            edtPassword.setTransformationMethod(new PasswordTransformationMethod());
            edtPassword.setTypeface(AppUtils.getTypefaceRegular(activity));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void onClickEvents()
    {

        try
        {
            llForgotPassword.setOnClickListener(v -> showForgotPasswordDialog());

            llSubmit.setOnClickListener(v ->
            {
                AppUtils.hideKeyboard(edtEmail, activity);

                if (checkValidationForLogin())
                {
                    if (sessionManager.isNetworkAvailable())
                    {
                        LoginAsync(edtEmail.getText().toString().trim(), edtPassword.getText().toString().trim());
                    }
                    else
                    {
                        Toast.makeText(activity, getResources().getString(R.string.network_failed_message), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private boolean checkValidationForLogin()
    {
        boolean isValid = true;
        if (edtEmail.getText().toString().length() == 0)
        {
            inputEmail.setError("Please enter user Id.");
            isValid = false;
        }
        else if (edtPassword.getText().toString().length() == 0)
        {
            inputPassword.setError("Please enter password.");
            isValid = false;
        }

        AppUtils.removeError(edtEmail, inputEmail);
        AppUtils.removeError(edtPassword, inputPassword);

        return isValid;
    }

    private void LoginAsync(final String emailparam, final String passwordparam)
    {
        try
        {
            new AsyncTask<Void, Void, Void>()
            {
                private String message = "", first_name = "", last_name = "", contact_no = "", email = "", password = "", reset_token = "", address = "";
                private int id, country_id, state_id, city_id;
                private boolean is_success = false, is_admin = false, is_active = false, is_deleted = false;

                @Override
                protected void onPreExecute()
                {
                    try
                    {
                        llLoading.setVisibility(View.VISIBLE);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    super.onPreExecute();
                }

                @Override
                protected Void doInBackground(Void... params)
                {
                    try
                    {
                        String response = "";
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("email", emailparam);
                        hashMap.put("password", passwordparam);
                        hashMap.put("login_request_from", "app");

                        AppUtils.printLog(activity, "LOGIN Request ", hashMap.toString());

                        response = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.LOGIN, hashMap);

                        AppUtils.printLog(activity, "LOGIN Response ", response.toString());

                        JSONObject jsonObject = new JSONObject(response);

                        is_success = AppUtils.getValidAPIBooleanResponseHas(jsonObject, "success");

                        message = AppUtils.getValidAPIStringResponseHas(jsonObject, "message");

                        if (is_success)
                        {
                            if (jsonObject.has("data"))
                            {
                                JSONObject dataObject = jsonObject.getJSONObject("data");
                                id = AppUtils.getValidAPIIntegerResponseHas(dataObject, "id");
                                first_name = AppUtils.getValidAPIStringResponseHas(dataObject, "first_name");
                                last_name = AppUtils.getValidAPIStringResponseHas(dataObject, "last_name");
                                contact_no = AppUtils.getValidAPIStringResponseHas(dataObject, "contact_no");
                                email = AppUtils.getValidAPIStringResponseHas(dataObject, "email");
                                password = AppUtils.getValidAPIStringResponseHas(dataObject, "password");
                                reset_token = AppUtils.getValidAPIStringResponseHas(dataObject, "reset_token");
                                address = AppUtils.getValidAPIStringResponseHas(dataObject, "address");
                                country_id = AppUtils.getValidAPIIntegerResponseHas(dataObject, "country_id");
                                state_id = AppUtils.getValidAPIIntegerResponseHas(dataObject, "state_id");
                                city_id = AppUtils.getValidAPIIntegerResponseHas(dataObject, "city_id");
                                is_admin = AppUtils.getValidAPIBooleanResponseHas(dataObject, "is_admin");
                                is_active = AppUtils.getValidAPIBooleanResponseHas(dataObject, "is_active");
                                is_deleted = AppUtils.getValidAPIBooleanResponseHas(dataObject, "is_deleted");
                            }
                        }

                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void result)
                {
                    super.onPostExecute(result);
                    try
                    {
                        llLoading.setVisibility(View.GONE);

                        if (is_success)
                        {
                            //Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                            String userName = first_name + " " + last_name;
                            sessionManager.createLoginSession(String.valueOf(id), userName, contact_no, email, address, String.valueOf(country_id), String.valueOf(state_id), String.valueOf(city_id), is_admin, is_active, is_deleted);
                            activity.finish();
                            startActivity(new Intent(activity, DashboardActivity.class));
                            //activity.overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                        }
                        else
                        {
                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (Exception e1)
                    {
                        e1.printStackTrace();
                    }
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void showForgotPasswordDialog()
    {
        BottomSheetDialog  dialog = new BottomSheetDialog(activity, R.style.BaseBottomSheetDialog);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.setCanceledOnTouchOutside(true);
        final View sheetView = activity.getLayoutInflater().inflate(R.layout.layout_dialog, null);
        dialog.setContentView(sheetView);
        AppUtils.configureBottomSheetBehavior(sheetView);
        AppUtils.setLightStatusBarBottomDialog(dialog, activity);
        final EditText edtForgotEmail = (EditText) sheetView.findViewById(R.id.edtForgotEmail);
        final TextInputLayout inputForgotPassword = (TextInputLayout) sheetView.findViewById(R.id.inputForgotEmail);
        LinearLayout btnYes = (LinearLayout) sheetView.findViewById(R.id.llDialogYes);
        btnYes.setOnClickListener(v ->
        {
            try
            {
                boolean isValid = true;

                if (edtForgotEmail.getText().toString().length() == 0)
                {
                    inputForgotPassword.setError("Please enter email Id.");
                    isValid = false;
                }
                else if (!AppUtils.validateEmail(edtForgotEmail.getText().toString().trim()))
                {
                    inputForgotPassword.setError("Please enter valid email Id.");
                    isValid = false;
                }

                AppUtils.removeError(edtForgotEmail, inputForgotPassword);

                if (isValid)
                {
                    //Forgot Password api will call here
                    forgotPasswordAsync(edtForgotEmail.getText().toString().trim());

                    Log.e("Validation OKay ", "onClick: ");

                    dialog.dismiss();
                    dialog.cancel();
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener()
        {
            @Override
            public void onDismiss(DialogInterface dialogInterface)
            {
                //MitsUtils.hideKeyboard(activity);
                //hideKeyboard();
                // AppUtils.hideKeyboard(edtTaskName, activity);
            }
        });

        dialog.show();
    }

    private void forgotPasswordAsync(final String email)
    {
        try
        {
            new AsyncTask<Void, Void, Void>()
            {
                private String message = "";
                private boolean is_success = false;

                @Override
                protected void onPreExecute()
                {
                    try
                    {
                        llLoading.setVisibility(View.VISIBLE);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    super.onPreExecute();
                }

                @Override
                protected Void doInBackground(Void... params)
                {
                    try
                    {
                        String response = "";
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("email", email);
                        hashMap.put("login_request_from", "");

                        AppUtils.printLog(activity, "SEND_RESET_PASSWORD_MAIL Request ", hashMap.toString());

                        response = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.SEND_RESET_PASSWORD_MAIL, hashMap);

                        AppUtils.printLog(activity, "SEND_RESET_PASSWORD_MAIL Response ", response.toString());

                        JSONObject jsonObject = new JSONObject(response);

                        is_success = AppUtils.getValidAPIBooleanResponseHas(jsonObject, "success");

                        message = AppUtils.getValidAPIStringResponseHas(jsonObject, "message");
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void result)
                {
                    super.onPostExecute(result);
                    try
                    {
                        llLoading.setVisibility(View.GONE);

                        if (is_success)
                        {


                        }
                        else
                        {
                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (Exception e1)
                    {
                        e1.printStackTrace();
                    }
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed()
    {
        try
        {
            activity.finish();
            //activity.overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        super.onBackPressed();
    }
}

