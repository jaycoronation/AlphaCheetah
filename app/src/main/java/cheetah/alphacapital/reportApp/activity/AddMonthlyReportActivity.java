package cheetah.alphacapital.reportApp.activity;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cheetah.alphacapital.R;
import cheetah.alphacapital.databinding.ActivityAddMonthlyReportBinding;
import cheetah.alphacapital.reportApp.activity.admin.BaseActivity;
import cheetah.alphacapital.reportApp.fragment.ClientAUMFragment;
import cheetah.alphacapital.reportApp.fragment.ClientDARFragment;
import cheetah.alphacapital.reportApp.getset.ClientListResponse;
import cheetah.alphacapital.reportApp.getset.CommentResponse;
import cheetah.alphacapital.reportApp.getset.CommonGetSet;
import cheetah.alphacapital.reportApp.getset.MonthYearResponse;
import cheetah.alphacapital.reportApp.getset.MonthlyReportResponse;
import cheetah.alphacapital.reportApp.getset.SaveMonthlyReportResponse;
import cheetah.alphacapital.utils.AppUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMonthlyReportActivity extends BaseActivity
{
    ActivityAddMonthlyReportBinding binding;
    private BottomSheetDialog listDialog;
    private List<String> listYear = new ArrayList<>();
    private ArrayList<MonthYearResponse.DataBean.MonthBean> listMonth = new ArrayList<MonthYearResponse.DataBean.MonthBean>();
    private final String MONTH = "Month";
    private final String YEAR = "Year";
    private String selectedYear = "", selectedMonth = "";
    private MonthlyReportResponse.MonthlyReportYearlySummeryDataItem getSet = new MonthlyReportResponse.MonthlyReportYearlySummeryDataItem();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_add_monthly_report);
        initView();
        onClick();
    }

    private void initView()
    {
        binding.toolbar.ivLogo.setVisibility(View.GONE);
        binding.toolbar.txtTitle.setVisibility(View.VISIBLE);
        binding.toolbar.txtTitle.setText("Add Monthly Report");

        AppUtils.removeError(binding.edtNumberOfPortfolio, binding.inputNumberOfPortfolio);
        AppUtils.removeError(binding.edtAttendance, binding.inputedtClient);
        AppUtils.removeError(binding.edtKnowladgeSession, binding.inputActivityType);
        AppUtils.removeError(binding.edtDAR, binding.inputRMName);
        AppUtils.removeError(binding.edtSelfAUM, binding.inputRemarksComment);
        AppUtils.removeError(binding.edtHours, binding.inputHours);
        AppUtils.removeError(binding.edtYear, binding.inputMinutes);
        AppUtils.removeError(binding.edtMonth, binding.inputMonth);

        if (sessionManager.isNetworkAvailable())
        {
            getMonthYearData();
        }

        if (getIntent().hasExtra("data"))
        {
            getSet = new Gson().fromJson(getIntent().getStringExtra("data"), MonthlyReportResponse.MonthlyReportYearlySummeryDataItem.class);
            binding.edtNumberOfPortfolio.setText(getSet.getNumberOfPortfolios());
            binding.edtAttendance.setText(getSet.getAttendance());
            binding.edtKnowladgeSession.setText(getSet.getKnowledgeSessions());
            binding.edtDAR.setText(getSet.getDAR());
            binding.edtSelfAUM.setText(getSet.getSelfAcquiredAUM());
            binding.edtHours.setText(getSet.getLastDateOfPortfolio_format());
            binding.edtYear.setText(String.valueOf(getSet.getCurrentYear()));
            binding.edtMonth.setText(getSet.getFull_month());
        }
    }

    private void onClick()
    {
        binding.toolbar.llBackNavigation.setOnClickListener(v -> finish());

        binding.edtHours.setOnClickListener(v -> showDatePickerDialog(binding.edtHours));

        binding.edtYear.setOnClickListener(v -> showListDialog(YEAR));

        binding.edtMonth.setOnClickListener(v -> showListDialog(MONTH));

        binding.llSubmit.setOnClickListener(v ->
        {
            try
            {
                if (binding.edtNumberOfPortfolio.getText().toString().trim().isEmpty())
                {
                    binding.inputNumberOfPortfolio.setError("Enter Number of Portfolio");
                    return;
                }
                if (binding.edtAttendance.getText().toString().trim().isEmpty())
                {
                    binding.inputedtClient.setError("Enter Attendance");
                    return;
                }
                if (binding.edtKnowladgeSession.getText().toString().trim().isEmpty())
                {
                    binding.inputActivityType.setError("Enter Knowledge Session");
                    return;
                }
                if (binding.edtDAR.getText().toString().trim().isEmpty())
                {
                    binding.inputRMName.setError("Enter DAR");
                    return;
                }
                if (binding.edtSelfAUM.getText().toString().trim().isEmpty())
                {
                    binding.inputRemarksComment.setError("Enter Self Accure AUM");
                    return;
                }
                if (binding.edtHours.getText().toString().trim().isEmpty())
                {
                    binding.inputHours.setError("Enter Last date of portfolio");
                    return;
                }
                if (binding.edtYear.getText().toString().trim().isEmpty())
                {
                    binding.inputMinutes.setError("Enter Year");
                    return;
                }
                if (binding.edtMonth.getText().toString().trim().isEmpty())
                {
                    binding.inputMonth.setError("Enter Month");
                    return;
                }
                callSaveMonthlyReportAPI();
            }
            catch (Exception e)
            {
                binding.llLoading.llLoading.setVisibility(View.GONE);
                e.printStackTrace();
            }
        });
    }

    private void callSaveMonthlyReportAPI()
    {
        String currentTime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
        String dateTime = binding.edtHours.getText().toString().trim() + " " + currentTime;

        Calendar cal = null;
        try
        {
            Date date = new SimpleDateFormat("MMMM", Locale.ENGLISH).parse(binding.edtMonth.getText().toString().trim());
            cal = Calendar.getInstance();
            cal.setTime(date);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        binding.llLoading.llLoading.setVisibility(View.VISIBLE);
        apiService.addMonthlyReport(Integer.valueOf(cal.get(Calendar.MONTH)) + 1, Integer.valueOf(binding.edtYear.getText().toString().trim()), Integer.valueOf(binding.edtAttendance.getText().toString().trim()), Integer.valueOf(binding.edtDAR.getText().toString().trim()), Integer.valueOf(binding.edtKnowladgeSession.getText().toString().trim()), dateTime, Integer.valueOf(binding.edtNumberOfPortfolio.getText().toString().trim()), Integer.valueOf(binding.edtSelfAUM.getText().toString().trim()), Integer.valueOf(sessionManager.getUserId())).enqueue(new Callback<SaveMonthlyReportResponse>()
        {
            @Override
            public void onResponse(Call<SaveMonthlyReportResponse> call, Response<SaveMonthlyReportResponse> response)
            {
                if (response.isSuccessful())
                {
                    if (response.body().getSuccess())
                    {
                        if (MonthlyReportActivity.handler != null)
                        {
                            Message message = Message.obtain();
                            message.what = 111;
                            MonthlyReportActivity.handler.sendMessage(message);
                        }
                        showToast(response.body().getMessage());
                        finish();
                    }
                    else
                    {
                        showToast(response.body().getMessage());
                    }
                }
                binding.llLoading.llLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<SaveMonthlyReportResponse> call, Throwable t)
            {
                apiFailedSnackBar();
                binding.llLoading.llLoading.setVisibility(View.GONE);
            }
        });
    }

    private void showDatePickerDialog(TextView textView)
    {
        try
        {
            DialogFragment newFragment = new AppUtils.SelectDateFragment(textView);
            newFragment.show(getSupportFragmentManager(), "DatePicker");
        }
        catch (Exception e)
        {
            e.printStackTrace();
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
                        if (response.body().getData().getYear().size() > 0)
                        {
                            listYear = new ArrayList<String>();

                            for (int i = 0; i < response.body().getData().getYear().size(); i++)
                            {
                                listYear.add(response.body().getData().getYear().get(i).toString().trim());
                            }
                        }

                        if (response.body().getData().getMonth().size() > 0)
                        {
                            listMonth = new ArrayList<MonthYearResponse.DataBean.MonthBean>();

                            for (int i = 0; i < response.body().getData().getMonth().size(); i++)
                            {
                                listMonth.add(response.body().getData().getMonth().get(i));
                            }
                        }
                    }
                    else
                    {
                        AppUtils.apiFailedSnackBar(activity);
                    }
                }
                else
                {
                    AppUtils.apiFailedSnackBar(activity);
                }
            }

            @Override
            public void onFailure(Call<MonthYearResponse> call, Throwable t)
            {
                AppUtils.apiFailedSnackBar(activity);
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

        @NonNull
        @Override
        public DialogListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowview_common_list, parent, false);
            return new DialogListAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(DialogListAdapter.ViewHolder holder, int position)
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
                            binding.edtMonth.setText(getSet.getName());
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
                                binding.edtYear.setText(selectedYear);
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
}