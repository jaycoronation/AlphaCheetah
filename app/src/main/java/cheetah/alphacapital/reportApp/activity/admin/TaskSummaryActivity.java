package cheetah.alphacapital.reportApp.activity.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import cheetah.alphacapital.R;
import cheetah.alphacapital.databinding.ActivityTaskSummaryBinding;
import cheetah.alphacapital.reportApp.getset.DARSummaryResponseModel;
import cheetah.alphacapital.reportApp.getset.TaskReportResponseModel;
import cheetah.alphacapital.utils.AppUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskSummaryActivity extends BaseActivity
{
    private ActivityTaskSummaryBinding binding;
    private ArrayList<TaskReportResponseModel.DataItem> listData = new ArrayList<>();
    private ArrayList<TaskReportResponseModel.DataItem> listClientSearch = new ArrayList<>();
    private DataAdapter viewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(activity,R.layout.activity_task_summary);
        initView();
        onClick();
    }

    private void initView()
    {
        if (sessionManager.isNetworkAvailable())
        {
            binding.llNoInternet.llNoInternet.setVisibility(View.GONE);
            getTaskSummary();
        }
        else
        {
            binding.llNoInternet.llNoInternet.setVisibility(View.VISIBLE);
        }

        binding.toolbar.ivSerach.setVisibility(View.VISIBLE);

        binding.toolbar.llNotification.setVisibility(View.GONE);

        binding.toolbar.ivLogo.setVisibility(View.GONE);
        binding.toolbar.txtTitle.setText("Task Summary");
    }

    private Timer timer = new Timer();
    private final long DELAY = 400;
    
    private void onClick()
    {
        binding.toolbar.llBackNavigation.setOnClickListener(v ->
        {
            try
            {
                AppUtils.hideKeyboard(binding.toolbar.emptyView, activity);
                activity.finish();
                //activity.overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });

        binding.toolbar.edtSearch.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3)
            {
                try
                {
                    cs = binding.toolbar.edtSearch.getText().toString().trim();
                    CharSequence finalCs = cs;

                    if (finalCs.length() > 0)
                    {
                        try
                        {
                            timer.cancel();
                            timer = new Timer();

                            timer.schedule(new TimerTask()
                            {
                                @Override
                                public void run()
                                {
                                    activity.runOnUiThread(new Runnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            listClientSearch = new ArrayList<>();
                                            if (listData != null && listData.size() > 0)
                                            {
                                                for (int i = 0; i < listData.size(); i++)
                                                {
                                                    final String text = listData.get(i).getEmployee_name();

                                                    String text1 = AppUtils.getCapitalText(text);

                                                    String cs1 = AppUtils.getCapitalText(String.valueOf(finalCs));

                                                    if (text1.contains(cs1))
                                                    {
                                                        listClientSearch.add(listData.get(i));
                                                    }
                                                }

                                                if (listClientSearch.size() > 0)
                                                {
                                                    Log.e("<><> SEARCH Size: ", listClientSearch.size() + " END ");
                                                    binding.llNoData.llNoData.setVisibility(View.GONE);
                                                    viewAdapter = new DataAdapter(listClientSearch);
                                                    binding.rvList.setAdapter(viewAdapter);
                                                }
                                                else
                                                {
                                                    binding.llNoData.llNoData.setVisibility(View.VISIBLE);
                                                }
                                            }
                                        }
                                    });
                                }
                            }, DELAY);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        if (listData.size() > 0)
                        {
                            viewAdapter = new DataAdapter(listData);
                            binding.rvList.setAdapter(viewAdapter);
                            binding.llNoData.llNoData.setVisibility(View.GONE);
                        }
                        else
                        {
                            binding.llNoData.llNoData.setVisibility(View.VISIBLE);
                        }
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3)
            {
            }

            @Override
            public void afterTextChanged(Editable arg0)
            {
            }
        });

        binding.toolbar.ivSerach.setOnClickListener(v -> doSearch());

        binding.toolbar.ivClose.setOnClickListener(v -> doClose());
    }

    private void doClose()
    {
        binding.toolbar.llBackNavigation.setVisibility(View.VISIBLE);
        binding.toolbar.txtTitle.setVisibility(View.VISIBLE);
        binding.toolbar.edtSearch.setVisibility(View.GONE);
        binding.toolbar.cvCard.setVisibility(View.GONE);
        binding.toolbar.ivSerach.setVisibility(View.VISIBLE);
        binding.toolbar.ivClose.setVisibility(View.GONE);
        //MitsUtils.hideKeyboard(activity);
        hideKeyboard();
        binding.toolbar.edtSearch.setText("");
    }

    private void doSearch()
    {
        binding.toolbar.edtSearch.requestFocus();
        binding.toolbar.llBackNavigation.setVisibility(View.GONE);
        binding.toolbar.txtTitle.setVisibility(View.GONE);
        binding.toolbar.edtSearch.setVisibility(View.VISIBLE);
        binding.toolbar.edtSearch.setHint("Search Employee");
        binding.toolbar.cvCard.setVisibility(View.VISIBLE);
        binding.toolbar.ivSerach.setVisibility(View.GONE);
        binding.toolbar.ivClose.setVisibility(View.VISIBLE);
        //MitsUtils.openKeyboard(binding.toolbar.edtSearch, activity);
    }

    private void getTaskSummary()
    {
        binding.llLoading.llLoading.setVisibility(View.VISIBLE);
        apiService.getTaskReportAPI(Integer.valueOf(sessionManager.getUserId()),0).enqueue(new Callback<TaskReportResponseModel>()
        {
            @Override
            public void onResponse(Call<TaskReportResponseModel> call, Response<TaskReportResponseModel> response)
            {
                if (response.isSuccessful())
                {
                    if (response.body().isSuccess())
                    {
                        if (response.body().getData().getData().size() > 0)
                        {
                            listData.clear();
                            listData.addAll(response.body().getData().getData());
                            Collections.sort(listData, new Comparator<TaskReportResponseModel.DataItem>() {
                                @Override
                                public int compare(TaskReportResponseModel.DataItem lhs, TaskReportResponseModel.DataItem rhs) {
                                    return lhs.getEmployee_name().compareTo(rhs.getEmployee_name());
                                }
                            });
                            viewAdapter = new DataAdapter(listData);
                            binding.rvList.setAdapter(viewAdapter);
                        }
                    }
                    else
                    {
                        showToast(response.body().getMessage());
                    }
                }
                binding.llLoading.llLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<TaskReportResponseModel> call, Throwable throwable)
            {
                binding.llLoading.llLoading.setVisibility(View.GONE);
                apiFailedSnackBar();
            }
        });
    }

    private class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder>
    {
        private ArrayList<TaskReportResponseModel.DataItem> listData = new ArrayList<>();
        public DataAdapter(ArrayList<TaskReportResponseModel.DataItem> listData)
        {
            this.listData = listData;
        }

        @NonNull
        @Override
        public DataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_task_summary, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull DataAdapter.ViewHolder holder, int i)
        {
            TaskReportResponseModel.DataItem getSet = listData.get(i);
            //holder.tvMonth.setText("Current Month");
            holder.tvEmployeeName.setText(getSet.getEmployee_name());
            holder.tvTotalTask.setText(String.valueOf(getSet.getCurrentMonth().getTotalTasks()).replace(".0",""));
            holder.tvTaskClose.setText(String.valueOf(getSet.getCurrentMonth().getTasksClosed()).replace(".0",""));
            holder.tvAvgDays.setText(String.valueOf(getSet.getCurrentMonth().getAvgDaysTaken()).replace(".0",""));
            holder.tvOpenTask.setText(String.valueOf(getSet.getCurrentMonth().getOpenTasks()).replace(".0",""));
            holder.tvAvgOpenDays.setText(String.valueOf(getSet.getCurrentMonth().getAvgOpenDays()).replace(".0",""));

            holder.tvEmployeeName.setOnClickListener(v ->
            {
                try
                {
                    Intent intent = new Intent(activity, EmployeeDetailsActivity.class);
                    intent.putExtra("employeeId", getSet.getEmployee_id());
                    intent.putExtra("employeeName", sessionManager.getUserName());
                    intent.putExtra("isForEmployeeLogin", false);
                    startActivity(intent);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            });

            holder.llMain.setOnClickListener(v ->
            {
                Gson gson = new Gson();
                Intent intent = new Intent(activity, TaskSummaryDetailsActivity.class);
                intent.putExtra("data", gson.toJson(getSet));
                startActivity(intent);
            });

        }

        @Override
        public int getItemCount()
        {
            return listData.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            AppCompatTextView tvMonth,tvEmployeeName, tvTotalTask,tvTaskClose,tvAvgDays,tvOpenTask,tvAvgOpenDays;
            LinearLayout llEmployee, llMain;
            public ViewHolder(@NonNull View itemView)
            {
                super(itemView);
                tvMonth = itemView.findViewById(R.id.tvMonth);
                llEmployee = itemView.findViewById(R.id.llEmployee);
                llMain = itemView.findViewById(R.id.llMain);
                tvEmployeeName = itemView.findViewById(R.id.tvEmployeeName);
                tvTotalTask = itemView.findViewById(R.id.tvTotalTask);
                tvTaskClose = itemView.findViewById(R.id.tvTaskClose);
                tvAvgDays = itemView.findViewById(R.id.tvAvgDays);
                tvOpenTask = itemView.findViewById(R.id.tvOpenTask);
                tvAvgOpenDays = itemView.findViewById(R.id.tvAvgOpenDays);
            }
        }
    }
}