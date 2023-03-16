package cheetah.alphacapital.reportApp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cheetah.alphacapital.R;
import cheetah.alphacapital.databinding.ActivityMonthlyReportBinding;
import cheetah.alphacapital.reportApp.activity.admin.BaseActivity;
import cheetah.alphacapital.reportApp.getset.MonthYearResponse;
import cheetah.alphacapital.reportApp.getset.MonthlyReportResponse;
import cheetah.alphacapital.utils.AppUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MonthlyReportActivity extends BaseActivity
{
    ActivityMonthlyReportBinding binding;
    private List<String> listYear = new ArrayList<>();
    private final String YEAR = "Year";
    private String selectedYear = "";
    private BottomSheetDialog listDialog;
    private ArrayList<MonthlyReportResponse.MonthlyReportYearlySummeryDataItem> listData = new ArrayList<MonthlyReportResponse.MonthlyReportYearlySummeryDataItem>();
    public static Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_monthly_report);
        initView();
        onClick();

        handler = new Handler(msg ->
        {
            if(msg.what==111)
            {
                getMonthYearData();
            }
            return false;
        });
    }

    private void initView()
    {
        binding.toolbar.ivLogo.setVisibility(View.GONE);
        binding.toolbar.txtTitle.setVisibility(View.VISIBLE);
        binding.toolbar.txtTitle.setText("Monthly Report");

        selectedYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        Log.e("<><> Month : ", "" + " Year " + selectedYear);

        if (sessionManager.isNetworkAvailable())
        {
            getMonthYearData();
        }
        else
        {
            noInternetSnackBar();
            binding.llNoInternet.llNoInternet.setVisibility(View.VISIBLE);
        }
    }

    private void onClick()
    {
        binding.toolbar.llBackNavigation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
                finishActivityAnimation();
            }
        });

        binding.llNoInternet.llRetry.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (sessionManager.isNetworkAvailable())
                {
                    getMonthYearData();
                }
                else
                {
                    noInternetSnackBar();
                }
            }
        });

        binding.fabAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(activity,AddMonthlyReportActivity.class);
                startActivity(intent);
            }
        });

        binding.llSelectEmployee.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showListDialog(YEAR);
            }
        });
    }

    private void getMonthYearData()
    {
        binding.llLoading.llLoading.setVisibility(View.VISIBLE);
        apiService.getMonthYear().enqueue(new Callback<MonthYearResponse>()
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
                                if (response.body().getData().getYear().get(i).equals(selectedYear))
                                {
                                    binding.tvYear.setText(listYear.get(i).toString());
                                    selectedYear = listYear.get(i);
                                    break;
                                }
                            }
                        }
                    }
                    else
                    {
                        binding.llLoading.llLoading.setVisibility(View.GONE);
                        AppUtils.apiFailedSnackBar(activity);
                    }
                }
                else
                {
                    AppUtils.apiFailedSnackBar(activity);
                }
                getMonthlyReportData();
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
        listDialog.findViewById(R.id.ivBack).setOnClickListener(v ->
        {
            listDialog.dismiss();
            listDialog.cancel();
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
            if (isFor.equalsIgnoreCase(YEAR))
            {
                holder.tvValue.setText(listYear.get(position));
                holder.itemView.setOnClickListener(v ->
                {
                    listDialog.dismiss();
                    if (!selectedYear.equalsIgnoreCase(listYear.get(position)))
                    {
                        if (sessionManager.isNetworkAvailable())
                        {
                            selectedYear = listYear.get(position);
                            binding.tvYear.setText(selectedYear);
                            binding.llLoading.llLoading.setVisibility(View.VISIBLE);
                            getMonthlyReportData();
                        }
                    }

                });
            }
        }

        @Override
        public int getItemCount()
        {
            return listYear.size();
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

    private void getMonthlyReportData()
    {
        apiService.getMonthlyReport(selectedYear,Integer.valueOf(sessionManager.getUserId()),1,10000).enqueue(new Callback<MonthlyReportResponse>()
        {
            @Override
            public void onResponse(Call<MonthlyReportResponse> call, Response<MonthlyReportResponse> response)
            {
                if (response.isSuccessful())
                {
                    if (response.body().isSuccess())
                    {
                        try
                        {
                            listData.clear();
                            if (response.body().getData().getMonthlyReportYearlySummeryData().size() > 0)
                            {
                                listData.addAll(response.body().getData().getMonthlyReportYearlySummeryData());
                                addDataToList(response.body().getData().getSummery().getTotal(),"Total");
                                addDataToList(response.body().getData().getSummery().getAverage(),"Average");
                                addDataToList(response.body().getData().getSummery().getTarget(),"Target");
                                addDataToList(response.body().getData().getSummery().getVariance(),"Variance");

                                binding.rvMonthlyReport.setAdapter(new MonthlyReportAdapter());
                            }
                            else
                            {
                                //binding.llNoData.llNoData.set
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        showToast("Something went wrong");
                    }
                }
                binding.llLoading.llLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<MonthlyReportResponse> call, Throwable t)
            {
                apiFailedSnackBar();
                binding.llLoading.llLoading.setVisibility(View.GONE);
            }
        });
    }

    private void addDataToList(MonthlyReportResponse.Target Data, String name)
    {
        MonthlyReportResponse.MonthlyReportYearlySummeryDataItem data = new MonthlyReportResponse.MonthlyReportYearlySummeryDataItem();
        MonthlyReportResponse.Target getSet = Data;

        data.set$id(Integer.parseInt(getSet.get$id()));
        data.setAttendance(String.valueOf(getSet.getAttendance()));
        data.setDAR(String.valueOf(getSet.getDAR()));
        data.setKnowledgeSessions(String.valueOf(getSet.getKnowledgeSessions()));
        data.setSelfAcquiredAUM(String.valueOf(getSet.getSelfAcquiredAUM()));
        data.setNumberOfPortfolios(String.valueOf(getSet.getNumberOfPortfolios()));
        data.setClickable(false);
        data.setFull_month(name);

        listData.add(data);
    }

    private class MonthlyReportAdapter extends RecyclerView.Adapter<MonthlyReportAdapter.ViewHolder>
    {
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowview_monthly_report, parent, false);
            return new MonthlyReportAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position)
        {
            MonthlyReportResponse.MonthlyReportYearlySummeryDataItem getSet = listData.get(position);
            if (getSet.getClickable())
            {
                holder.tvYear.setText(getSet.getFull_month() + ", " +String.valueOf(getSet.getCurrentYear()));
            }
            else
            {
                holder.tvYear.setText(getSet.getFull_month());
            }
            if (getSet.getSelfAcquiredAUM() != null)
            {
                holder.tvSelfAUM.setText(getSet.getSelfAcquiredAUM());
            }
            else
            {
                holder.tvSelfAUM.setText("-");
            }
            if (getSet.getAttendance() != null)
            {
                holder.tvAttendance.setText(getSet.getAttendance());
            }
            else
            {
                holder.tvAttendance.setText("-");
            }
            if (getSet.getDAR() != null)
            {
                holder.tvDAR.setText(getSet.getDAR());
            }
            else
            {
                holder.tvDAR.setText("-");
            }
            if (getSet.getKnowledgeSessions() != null)
            {
                holder.tvKnowladgeSession.setText(getSet.getKnowledgeSessions());
            }
            else
            {
                holder.tvKnowladgeSession.setText("-");
            }
            if (getSet.getLastDateOfPortfolio() != null)
            {
                holder.tvLastDatePortFolio.setText(getSet.getLastDateOfPortfolio());
            }
            else
            {
                holder.tvLastDatePortFolio.setText("-");
            }
            if (getSet.getNumberOfPortfolios() != null)
            {
                holder.tvPortfolioNumber.setText(getSet.getNumberOfPortfolios());
            }
            else
            {
                holder.tvPortfolioNumber.setText("-");
            }


            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (getSet.getClickable())
                    {
                        Gson gson = new Gson();
                        Intent intent = new Intent(activity,AddMonthlyReportActivity.class);
                        intent.putExtra("data", gson.toJson(getSet));
                        startActivity(intent);

                    }
                }
            });
        }

        @Override
        public int getItemCount()
        {
            return listData.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            AppCompatTextView tvYear,tvSelfAUM ,tvAttendance,tvDAR , tvKnowladgeSession , tvLastDatePortFolio, tvPortfolioNumber;
            public ViewHolder(@NonNull View itemView)
            {
                super(itemView);
                tvYear = itemView.findViewById(R.id.tvYear);
                tvSelfAUM = itemView.findViewById(R.id.tvSelfAUM);
                tvAttendance = itemView.findViewById(R.id.tvAttendance);
                tvDAR = itemView.findViewById(R.id.tvDAR);
                tvKnowladgeSession = itemView.findViewById(R.id.tvKnowladgeSession);
                tvLastDatePortFolio = itemView.findViewById(R.id.tvLastDatePortFolio);
                tvPortfolioNumber = itemView.findViewById(R.id.tvPortfolioNumber);
            }
        }
    }
}