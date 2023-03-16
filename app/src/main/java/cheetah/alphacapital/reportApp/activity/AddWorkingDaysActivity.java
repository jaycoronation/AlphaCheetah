package cheetah.alphacapital.reportApp.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cheetah.alphacapital.reportApp.activity.admin.BaseActivity;
import cheetah.alphacapital.reportApp.activity.admin.WorkingDaysActivity;
import cheetah.alphacapital.textutils.CustomAppEditText;
import cheetah.alphacapital.textutils.CustomEditText;
import cheetah.alphacapital.textutils.CustomTextInputLayout;
import cheetah.alphacapital.textutils.CustomTextViewMedium;
import cheetah.alphacapital.textutils.CustomTextViewSemiBold;
import cheetah.alphacapital.utils.AppAPIUtils;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import cheetah.alphacapital.R;
import cheetah.alphacapital.reportApp.getset.MonthYearResponse;
import cheetah.alphacapital.reportApp.getset.WorkingDaysResponse;

public class AddWorkingDaysActivity extends BaseActivity
{

    @BindView(R.id.ivHeader)
    ImageView ivHeader;
    @BindView(R.id.viewStatusBar)
    View viewStatusBar;
    @BindView(R.id.emptyView)
    View emptyView;
    @BindView(R.id.llBackNavigation)
    LinearLayout llBackNavigation;
    @BindView(R.id.ivLogo)
    ImageView ivLogo;
    @BindView(R.id.ivIcon)
    ImageView ivIcon;
    @BindView(R.id.txtTitle)
    CustomTextViewSemiBold txtTitle;
    @BindView(R.id.edtSearch)
    CustomEditText edtSearch;
    @BindView(R.id.ivSerach)
    ImageView ivSerach;
    @BindView(R.id.ivClose)
    ImageView ivClose;
    @BindView(R.id.ivContactUs)
    ImageView ivContactUs;
    @BindView(R.id.llNotification)
    LinearLayout llNotification;
    @BindView(R.id.ivChangePassword)
    ImageView ivChangePassword;
    @BindView(R.id.ivLogout)
    ImageView ivLogout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.pbLoading)
    ProgressBar pbLoading;
    @BindView(R.id.txtLoading)
    AppCompatTextView txtLoading;
    @BindView(R.id.llLoading)
    LinearLayout llLoading;
    @BindView(R.id.txtRetry)
    AppCompatTextView txtRetry;
    @BindView(R.id.llRetry)
    LinearLayout llRetry;
    @BindView(R.id.llNoInternet)
    LinearLayout llNoInternet;
    @BindView(R.id.edtYear)
    CustomAppEditText edtYear;
    @BindView(R.id.inputYear)
    CustomTextInputLayout inputYear;
    @BindView(R.id.edtMonth)
    CustomAppEditText edtMonth;
    @BindView(R.id.inputMonth)
    CustomTextInputLayout inputMonth;
    @BindView(R.id.edtWorkingDays)
    CustomAppEditText edtWorkingDays;
    @BindView(R.id.inputWorkingDays)
    CustomTextInputLayout inputWorkingDays;
    @BindView(R.id.llSubmit)
    LinearLayout llSubmit;
    private boolean isStatusBarHidden = false;
    private List<String> listYear = new ArrayList<>();
    private List<MonthYearResponse.DataBean.MonthBean> listMonth = new ArrayList<MonthYearResponse.DataBean.MonthBean>();
    private final String MONTH = "Month";
    private final String YEAR = "Year";
    private BottomSheetDialog listDialog;
    private String selectedYear = "", selectedMonth = "", isFor = "";
    WorkingDaysResponse.DataBean getSet = new WorkingDaysResponse.DataBean();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
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

        //AppUtils.setLightStatusBar(activity);

        isFor = getIntent().getStringExtra("isFor");

        getSet = getIntent().getExtras().getParcelable("WorkingDaysGetSet");

        setContentView(R.layout.activity_add_working_days);

        ButterKnife.bind(this);

        setupViews();

        onClickEvents();

        if (sessionManager.isNetworkAvailable())
        {
            llNoInternet.setVisibility(View.GONE);
            getMonthYearData();
        }
        else
        {
            llNoInternet.setVisibility(View.VISIBLE);
        }
    }

    private void setupViews()
    {
        try
        {
            ivLogo.setVisibility(View.GONE);
            llNotification.setVisibility(View.GONE);

            if (isFor.equalsIgnoreCase("add"))
            {
                txtTitle.setText("Add Working Day");
            }
            else
            {
                txtTitle.setText("Update Working Day");
                selectedMonth = String.valueOf(getSet.getMonth());
                selectedYear = String.valueOf(getSet.getYear());

                edtYear.setText(String.valueOf(getSet.getYear()));
                edtMonth.setText(AppUtils.getMonthName(getSet.getMonth()));
                edtWorkingDays.setText(String.valueOf(getSet.getWorking_days()));
            }
            llBackNavigation.setVisibility(View.VISIBLE);
            /*ivHeader.setImageResource(R.drawable.img_portfolio);

            int height = 56;
            if (isStatusBarHidden)
            {
                height = 56 + 25;
                toolbar.findViewById(R.id.viewStatusBar).setVisibility(View.INVISIBLE);
            }
            else
            {
                toolbar.findViewById(R.id.viewStatusBar).setVisibility(View.GONE);
            }
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) ivHeader.getLayoutParams();
            params.height = (int) AppUtils.pxFromDp(activity, height);
            ivHeader.setLayoutParams(params);

            setSupportActionBar(toolbar);*/
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void onClickEvents()
    {
        llBackNavigation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    AppUtils.hideKeyboard(toolbar, activity);
                    activity.finish();
                    //activity.overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        edtMonth.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showListDialog(MONTH);
            }
        });

        edtYear.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showListDialog(YEAR);
            }
        });

        llSubmit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                //MitsUtils.hideKeyboard(activity);
                hideKeyboard();

                if (checkValidation())
                {
                    if (sessionManager.isNetworkAvailable())
                    {
                        addDays(edtWorkingDays.getText().toString().trim());
                    }
                    else
                    {
                        Toast.makeText(activity, getResources().getString(R.string.network_failed_message), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


    private boolean checkValidation()
    {
        boolean isValid = true;

        if (selectedYear.length() == 0)
        {
            inputYear.setError("Please select year.");
            isValid = false;
        }
        else if (selectedMonth.length() == 0)
        {
            inputMonth.setError("Please select month.");
            isValid = false;
        }
        else if (edtWorkingDays.getText().toString().length()==0)
        {
            inputWorkingDays.setError("Please enter working days.");
            isValid = false;
        }

        AppUtils.removeError(edtYear, inputYear);
        AppUtils.removeError(edtMonth, inputMonth);
        AppUtils.removeError(edtWorkingDays, inputWorkingDays);

        return isValid;
    }

    private void addDays(final String workingDays)
    {
        if (sessionManager.isNetworkAvailable())
        {
            new AsyncTask<Void, Void, Void>()
            {
                String message = "";
                boolean success = false;

                @Override
                protected void onPreExecute()
                {
                    super.onPreExecute();
                    llLoading.setVisibility(View.VISIBLE);
                }

                @Override
                protected Void doInBackground(Void... voids)
                {
                    try
                    {
                        HashMap<String, String> hashMap = new HashMap<>();

                        if (!isFor.equalsIgnoreCase("add"))
                        {
                            hashMap.put("Id", String.valueOf(getSet.getId()));
                        }

                        hashMap.put("month", selectedMonth);
                        hashMap.put("year", selectedYear);
                        hashMap.put("working_days", workingDays);

                        Log.e("add working days req ", "doInBackground: " + hashMap.toString());

                        String serverResponse = "";

                        if (isFor.equalsIgnoreCase("add"))
                        {
                            serverResponse = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.ADD_MANGE_WORKING_DAYS, hashMap);
                        }
                        else
                        {
                            serverResponse = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.UPDATE_MANGE_WORKING_DAYS, hashMap);
                        }

                        Log.e("add working days resp ", "doInBackground: " + serverResponse);

                        if (serverResponse != null)
                        {
                            try
                            {
                                JSONObject jsonObject = new JSONObject(serverResponse);

                                success = AppUtils.getValidAPIBooleanResponseHas(jsonObject, "success");

                                message = AppUtils.getValidAPIStringResponseHas(jsonObject, "message");

                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
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
                protected void onPostExecute(Void aVoid)
                {
                    super.onPostExecute(aVoid);

                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();

                    llLoading.setVisibility(View.GONE);

                    if (isFor.equalsIgnoreCase("add"))
                    {
                        if (WorkingDaysActivity.handler != null)
                        {
                            Message message = Message.obtain();
                            message.what = 100;
                            WorkingDaysActivity.handler.sendMessage(message);
                        }
                    }
                    else
                    {
                        getSet.setWorking_days(Integer.parseInt(workingDays));
                        getSet.setYear(Integer.parseInt(selectedYear));
                        getSet.setMonth(Integer.parseInt(selectedMonth));

                        if (WorkingDaysActivity.handler != null)
                        {
                            Message message = Message.obtain();
                            message.obj = getSet;
                            message.what = 101;
                            WorkingDaysActivity.handler.sendMessage(message);
                        }
                    }

                    finish();
                }
            }.execute();
        }

    }

    private void getMonthYearData()
    {
        Call<MonthYearResponse> call = apiService.getMonthYear();

        call.enqueue(new Callback<MonthYearResponse>()
        {
            @Override
            public void onResponse(Call<MonthYearResponse> call, Response<MonthYearResponse> response)
            {
                if (response.isSuccessful())
                {
                    if (response.body().isSuccess())
                    {
                        listYear = new ArrayList<>();
                        listMonth = new ArrayList<MonthYearResponse.DataBean.MonthBean>();
                        listYear.addAll(response.body().getData().getYear());
                        listMonth.addAll(response.body().getData().getMonth());
                    }
                    else
                    {
                        apiFailedSnackBar();
                    }
                }
                else
                {
                    apiFailedSnackBar();
                }
            }

            @Override
            public void onFailure(Call<MonthYearResponse> call, Throwable t)
            {
                apiFailedSnackBar();
            }
        });
    }

    DialogListAdapter dialogListAdapter;

    public void showListDialog(final String isFor)
    {
        listDialog = new BottomSheetDialog(activity, R.style.MaterialDialogSheetTemp);
        listDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        final View sheetView = activity.getLayoutInflater().inflate(R.layout.dialog_list, null);
        listDialog.setContentView(sheetView);
        AppUtils.setLightStatusBarBottomDialog(listDialog, activity);
        listDialog.findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listDialog.dismiss();
                listDialog.cancel();
            }
        });

        TextView btnNo = (TextView) listDialog.findViewById(R.id.btnNo);

        TextView tvTitle = (TextView) listDialog.findViewById(R.id.tvTitle);
        tvTitle.setText("Select " + isFor);

        TextView tvDone = (TextView) listDialog.findViewById(R.id.tvDone);

        tvDone.setVisibility(View.GONE);

        final RecyclerView rvListDialog = (RecyclerView) listDialog.findViewById(R.id.rvDialog);

        dialogListAdapter = new DialogListAdapter(isFor);
        rvListDialog.setLayoutManager(new LinearLayoutManager(activity));
        rvListDialog.setAdapter(dialogListAdapter);

        btnNo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listDialog.dismiss();
                listDialog.cancel();
            }
        });

        listDialog.show();
    }

    private class DialogListAdapter extends RecyclerView.Adapter<DialogListAdapter.ViewHolder>
    {
        String isFor = "";

        DialogListAdapter(String isFor)
        {
            this.isFor = isFor;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowview_common_list, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position)
        {
            if (position == getItemCount() - 1)
            {
                holder.viewLine.setVisibility(View.GONE);
            }
            else
            {
                holder.viewLine.setVisibility(View.VISIBLE);
            }
            if (isFor.equals(MONTH))
            {
                final MonthYearResponse.DataBean.MonthBean getSet = listMonth.get(position);
                holder.tvValue.setText(getSet.getName());
                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        listDialog.dismiss();
                        if (!selectedMonth.equalsIgnoreCase(String.valueOf(getSet.getId())))
                        {
                            edtMonth.setText(getSet.getName());
                            selectedMonth = String.valueOf(getSet.getId());
                        }
                    }
                });
            }
            else if (isFor.equalsIgnoreCase(YEAR))
            {
                holder.tvValue.setText(listYear.get(position).toString());
                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        listDialog.dismiss();
                        if (!selectedYear.equalsIgnoreCase(listYear.get(position).toString()))
                        {
                            if (sessionManager.isNetworkAvailable())
                            {
                                selectedYear = listYear.get(position).toString();
                                edtYear.setText(selectedYear);
                            }
                        }

                    }
                });
            }
        }

        @Override
        public int getItemCount()
        {
            if (isFor.equalsIgnoreCase(MONTH))
            {
                return listMonth.size();
            }
            else
            {
                return listYear.size();
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            private TextView tvValue;
            private View viewLine;

            public ViewHolder(View itemView)
            {
                super(itemView);
                tvValue = (TextView) itemView.findViewById(R.id.tvValue);
                viewLine = itemView.findViewById(R.id.viewLine);
            }
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
