package cheetah.alphacapital.reportApp.activity.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cheetah.alphacapital.R;
import cheetah.alphacapital.databinding.ActivityLeadTrackerBinding;
import cheetah.alphacapital.reportApp.fragment.EmpDARReportFragment;
import cheetah.alphacapital.reportApp.getset.EmployeeListResponse;
import cheetah.alphacapital.reportApp.getset.LeadsResponseModel;
import cheetah.alphacapital.reportApp.getset.MonthYearResponse;
import cheetah.alphacapital.textutils.CustomTextViewSemiBold;
import cheetah.alphacapital.utils.AppUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeadTrackerActivity extends BaseActivity
{
    private ActivityLeadTrackerBinding binding;

    private final String MONTH = "Month";
    private final String YEAR = "Year";
    private LeadsAdapter dataAdapter;
    private BottomSheetDialog listDialog;
    DialogListAdapter dialogListAdapter;

    private ArrayList<LeadsResponseModel.DataItem> listData = new ArrayList<LeadsResponseModel.DataItem>();
    private ArrayList<String> listYear = new ArrayList<>();
    private ArrayList<MonthYearResponse.DataBean.MonthBean> listMonth = new ArrayList<MonthYearResponse.DataBean.MonthBean>();

    private String selectedYear = "";
    private boolean isStatusBarHidden = false, is_status_active = false, is_Admin = false;

    //paging
    int pageIndex = 1;
    boolean isLoading = false;
    boolean isLastPage = false;
    int pageResults = 25;

    public static Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_lead_tracker);

        handler = new Handler(new Handler.Callback()
        {
            @Override
            public boolean handleMessage(Message msg)
            {
                try
                {
                    if (msg.what == 100)
                    {
                        if (sessionManager.isNetworkAvailable())
                        {
                            binding.llNoInternet.llNoInternet.setVisibility(View.GONE);
                            callGetLeadsTrackerAPI(true);
                        }
                        else
                        {
                            binding.llNoInternet.llNoInternet.setVisibility(View.VISIBLE);
                        }
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                return false;
            }
        });

        initView();
        onClick();
    }

    private void initView()
    {
        if (sessionManager.isNetworkAvailable())
        {
            binding.llNoInternet.llNoInternet.setVisibility(View.GONE);
            selectedYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
            binding.tvYear.setText(selectedYear);
            callGetLeadsTrackerAPI(true);
            getMonthYearData();
        }
        else
        {
            binding.llNoInternet.llNoInternet.setVisibility(View.VISIBLE);
        }

        binding.toolbar.llNotification.setVisibility(View.GONE);

        binding.toolbar.ivLogo.setVisibility(View.GONE);
        binding.toolbar.txtTitle.setText("Leads Tracker");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        binding.nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) ->
        {
            if (v.getChildAt(v.getChildCount() - 1) != null)
            {
                if (scrollY >= v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight() && scrollY > oldScrollY)
                {
                    Integer visibleItemCount = linearLayoutManager.getChildCount();
                    Integer totalItemCount = linearLayoutManager.getItemCount();
                    Integer firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

                    if (!isLoading && !isLastPage)
                    {
                        if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0)
                        {
                            if (listData.size() > 0)
                            {
                                binding.llMore.setVisibility(View.VISIBLE);
                                callGetLeadsTrackerAPI(false);
                            }
                        }
                    }
                }
            }
        });
    }

    private void callGetLeadsTrackerAPI(boolean isFirstTime)
    {
        if (isFirstTime)
        {
            binding.llLoading.llLoading.setVisibility(View.VISIBLE);
        }
        apiService.getLeadsList(selectedYear, pageIndex, pageResults, sessionManager.getUserId()).enqueue(new Callback<LeadsResponseModel>()
        {
            @Override
            public void onResponse(Call<LeadsResponseModel> call, Response<LeadsResponseModel> response)
            {
                if (response.isSuccessful())
                {
                    if (response.body().isSuccess())
                    {
                        if (isFirstTime)
                        {
                            if (listData.size() > 0)
                            {
                                listData = new ArrayList<>();
                            }
                        }
                        ArrayList<LeadsResponseModel.DataItem> tempList = new ArrayList<LeadsResponseModel.DataItem>();
                        tempList.addAll(response.body().getData());
                        listData.addAll(tempList);

                        if (tempList.size() != 0)
                        {
                            pageIndex += 1;
                            if (tempList.size() == 0 || tempList.size() % pageResults != 0)
                            {
                                isLastPage = true;
                            }
                        }
                        isLoading = false;

                        if (isFirstTime)
                        {
                            binding.llNoData.llNoData.setVisibility(View.GONE);
                            if (listData.size() > 0)
                            {
                                dataAdapter = new LeadsAdapter(listData, activity);
                                binding.rvLeadsList.setAdapter(dataAdapter);
                                binding.llNoData.llNoData.setVisibility(View.GONE);
                            }
                            else
                            {
                                binding.llNoData.llNoData.setVisibility(View.VISIBLE);
                            }
                        }
                        else
                        {
                            dataAdapter.notifyDataSetChanged();
                        }
                    }
                    else
                    {
                        binding.llNoData.llNoData.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    binding.llNoData.llNoData.setVisibility(View.VISIBLE);
                }

                binding.llMore.setVisibility(View.GONE);
                binding.llLoading.llLoading.setVisibility(View.GONE);
                binding.llNoInternet.llNoInternet.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<LeadsResponseModel> call, Throwable t)
            {
                binding.llLoading.llLoading.setVisibility(View.GONE);
                binding.llNoData.llNoData.setVisibility(View.VISIBLE);
                binding.llNoInternet.llNoInternet.setVisibility(View.GONE);
                apiFailedSnackBar();
            }
        });
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
                        binding.llLoading.llLoading.setVisibility(View.GONE);
                        apiFailedSnackBar();
                    }
                }
                else
                {
                    binding.llLoading.llLoading.setVisibility(View.GONE);
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

    private void onClick()
    {
        binding.toolbar.llBackNavigation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
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
            }
        });

        binding.llSelectYear.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showListDialog(YEAR);
            }
        });

        binding.ivLeadsAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(activity, AddLeadsActivity.class);
                startActivity(intent);
            }
        });

    }

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

        TextView btnNo = listDialog.findViewById(R.id.btnNo);
        TextView tvTitle = listDialog.findViewById(R.id.tvTitle);
        tvTitle.setText("Select " + isFor);
        TextView tvDone = listDialog.findViewById(R.id.tvDone);
        tvDone.setVisibility(View.GONE);

        final RecyclerView rvListDialog = listDialog.findViewById(R.id.rvDialog);

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
        public void onBindViewHolder(DialogListAdapter.ViewHolder holder, final int position)
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
                        /*if (!selectedMonth.equalsIgnoreCase(String.valueOf(getSet.getId())))
                        {
                            tvMonth.setText(getSet.getName());
                            selectedMonth = String.valueOf(getSet.getId());
                            if (sessionManager.isNetworkAvailable())
                            {
                                llNoInternet.setVisibility(View.GONE);
                                getAllAssignedClientByEmployee(false);
                            }
                            else
                            {
                                llNoInternet.setVisibility(View.VISIBLE);
                            }
                        }*/
                    }
                });
            }
            else if (isFor.equalsIgnoreCase(YEAR))
            {
                holder.tvValue.setText(listYear.get(position));
                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        listDialog.dismiss();
                        if (!selectedYear.equalsIgnoreCase(listYear.get(position)))
                        {
                            if (sessionManager.isNetworkAvailable())
                            {
                                selectedYear = listYear.get(position);
                                binding.tvYear.setText(selectedYear);
                                if (sessionManager.isNetworkAvailable())
                                {
                                    binding.llNoInternet.llNoInternet.setVisibility(View.GONE);
                                    callGetLeadsTrackerAPI(true);
                                }
                                else
                                {
                                    binding.llNoInternet.llNoInternet.setVisibility(View.VISIBLE);
                                }
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
            private final TextView tvValue;
            private final View viewLine;

            public ViewHolder(View itemView)
            {
                super(itemView);
                tvValue = itemView.findViewById(R.id.tvValue);
                viewLine = itemView.findViewById(R.id.viewLine);
            }
        }
    }

    private class LeadsAdapter extends RecyclerView.Adapter<LeadsAdapter.ViewHolder>
    {
        public LeadsAdapter(ArrayList<LeadsResponseModel.DataItem> listData, Activity activity)
        {
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_leads_tracker, viewGroup, false);
            return new LeadsAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int i)
        {
            final LeadsResponseModel.DataItem getSet = listData.get(i);
            Log.e("<><><>MONTH", getSet.getTotal() + "=====" + getSet.getGold());
            holder.tvMonth.setText(getSet.getFullMonth());
            holder.tvGold.setText(getSet.getGold());
            holder.tvFinancialPlan.setText(getSet.getA());
            holder.tvClient.setText(getSet.getB());
            holder.tvNoContact.setText(getSet.getC());
            holder.tvNotRightClient.setText(getSet.getD());
            holder.tvNotInterestedClient.setText(getSet.getE());
            holder.tvTotal.setText(String.valueOf(getSet.getTotal()));

            holder.llView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(activity, LeadsDetailsActivity.class);
                    intent.putExtra("month", getSet.getMonth());
                    intent.putExtra("month_title", getSet.getFullMonth());
                    intent.putExtra("year", selectedYear);
                    startActivity(intent);
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
            AppCompatTextView tvGold, tvFinancialPlan, tvClient, tvNoContact, tvNotRightClient, tvNotInterestedClient, tvTotal, tvMonth;
            LinearLayout llView;

            public ViewHolder(View itemView)
            {
                super(itemView);
                tvMonth = itemView.findViewById(R.id.tvMonth);
                tvGold = itemView.findViewById(R.id.tvGold);
                tvFinancialPlan = itemView.findViewById(R.id.tvFinancialPlan);
                tvClient = itemView.findViewById(R.id.tvClient);
                tvNoContact = itemView.findViewById(R.id.tvNoContact);
                tvNotRightClient = itemView.findViewById(R.id.tvNotRightClient);
                tvNotInterestedClient = itemView.findViewById(R.id.tvNotInterestedClient);
                tvTotal = itemView.findViewById(R.id.tvTotal);
                llView = itemView.findViewById(R.id.llView);
            }
        }
    }
}