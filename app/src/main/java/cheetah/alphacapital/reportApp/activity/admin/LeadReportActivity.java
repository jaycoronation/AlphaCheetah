package cheetah.alphacapital.reportApp.activity.admin;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cheetah.alphacapital.R;
import cheetah.alphacapital.databinding.ActivityLeadReportBinding;
import cheetah.alphacapital.databinding.RowviewListReportBinding;
import cheetah.alphacapital.reportApp.activity.AssignedClientListActivity;
import cheetah.alphacapital.reportApp.getset.AssignedClientGetSetOld;
import cheetah.alphacapital.reportApp.getset.GetAllLeadsReportResponse;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeadReportActivity extends BaseActivity {

    private ActivityLeadReportBinding binding;
    private ArrayList<GetAllLeadsReportResponse.LstEmpDataItem> listLeadsReport = new ArrayList<>();
    private ArrayList<GetAllLeadsReportResponse.LstEmpDataItem> listLeadsReport_Search = new ArrayList<>();
    private LeadsReportAdapter leadsReportAdapter;

    private Timer timer = new Timer();
    private final long DELAY = 400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(activity,R.layout.activity_lead_report);
        initView();
        onClick();
    }

    private void onClick() {
        binding.toolbar.llBackNavigation.setOnClickListener(view -> finish());

        binding.toolbar.ivSerach.setOnClickListener(v -> doSearch());

        binding.toolbar.ivClose.setOnClickListener(v -> doClose());

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
                                            listLeadsReport_Search = new ArrayList<GetAllLeadsReportResponse.LstEmpDataItem>();
                                            if (listLeadsReport != null && listLeadsReport.size() > 0)
                                            {
                                                for (int i = 0; i < listLeadsReport.size(); i++)
                                                {
                                                    final String text = listLeadsReport.get(i).getEmployeeName();

                                                    String text1 = AppUtils.getCapitalText(text);

                                                    String cs1 = AppUtils.getCapitalText(String.valueOf(finalCs));

                                                    if (text1.contains(cs1))
                                                    {
                                                        listLeadsReport_Search.add(listLeadsReport.get(i));
                                                    }
                                                }


                                                if (listLeadsReport_Search.size() > 0)
                                                {
                                                    leadsReportAdapter = new LeadsReportAdapter(listLeadsReport_Search, activity);
                                                    binding.rvLeads.setAdapter(leadsReportAdapter);
                                                    binding.llNoData.llNoData.setVisibility(View.GONE);
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
                        if (listLeadsReport.size() > 0)
                        {
                            leadsReportAdapter = new LeadsReportAdapter(listLeadsReport, activity);
                            binding.rvLeads.setAdapter(leadsReportAdapter);
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
    }

    private void initView() {

        binding.toolbar.ivLogo.setVisibility(View.GONE);
        binding.toolbar.txtTitle.setText("Leads Report");
        binding.toolbar.llNotification.setVisibility(View.GONE);
        binding.toolbar.ivSerach.setVisibility(View.VISIBLE);

        getAllLeadReport();
    }

    private void doClose()
    {
        binding.toolbar.llBackNavigation.setVisibility(View.VISIBLE);
        binding.toolbar.txtTitle.setVisibility(View.VISIBLE);
        binding.toolbar.edtSearch.setVisibility(View.GONE);
        binding.toolbar.cvCard.setVisibility(View.GONE);
        binding.toolbar.ivSerach.setVisibility(View.VISIBLE);
        binding.toolbar.ivClose.setVisibility(View.GONE);
        AppUtils.hideKeyboard(binding.toolbar.edtSearch,activity);
        binding.toolbar.edtSearch.setText("");
    }

    private void doSearch()
    {
        binding.toolbar.edtSearch.requestFocus();
        binding.toolbar.llBackNavigation.setVisibility(View.GONE);
        binding.toolbar.txtTitle.setVisibility(View.GONE);
        binding.toolbar.edtSearch.setVisibility(View.VISIBLE);
        binding.toolbar.cvCard.setVisibility(View.VISIBLE);
        binding.toolbar.ivSerach.setVisibility(View.GONE);
        binding.toolbar.ivClose.setVisibility(View.VISIBLE);
    }

    private void getAllLeadReport() {
        binding.llLoading.llLoading.setVisibility(View.VISIBLE);
        apiService.GetAllEMPLeadReport(0).enqueue(new Callback<GetAllLeadsReportResponse>() {
            @Override
            public void onResponse(Call<GetAllLeadsReportResponse> call, Response<GetAllLeadsReportResponse> response)
            {
                if (response.isSuccessful())
                {
                    if (response.body().isSuccess())
                    {
                        listLeadsReport = new ArrayList<>();
                        listLeadsReport.addAll(response.body().getData().getLstEmpData());

                        if (listLeadsReport.size() > 0)
                        {
                            leadsReportAdapter = new LeadsReportAdapter(listLeadsReport, activity);
                            binding.rvLeads.setAdapter(leadsReportAdapter);
                            binding.llNoData.llNoData.setVisibility(View.GONE);
                        }
                        else
                        {
                            binding.llNoData.llNoData.setVisibility(View.VISIBLE);
                        }
                    }
                    else
                    {
                        //showToast(response.body().getMessage());
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
            public void onFailure(Call<GetAllLeadsReportResponse> call, Throwable t)
            {
                binding.llLoading.llLoading.setVisibility(View.GONE);
                binding.llNoData.llNoData.setVisibility(View.VISIBLE);
                binding.llNoInternet.llNoInternet.setVisibility(View.GONE);
            }
        });
    }

    private static class LeadsReportAdapter extends RecyclerView.Adapter<LeadsReportAdapter.ViewHolder> {
        List<GetAllLeadsReportResponse.LstEmpDataItem> listItems;
        private Activity activity;

        public LeadsReportAdapter(List<GetAllLeadsReportResponse.LstEmpDataItem> listEmployee, Activity activity)
        {
            this.listItems = listEmployee;
            this.activity = activity;
        }

        @NonNull
        @Override
        public LeadsReportAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int i)
        {
            View view = LayoutInflater.from(activity).inflate(R.layout.rowview_list_report,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final LeadsReportAdapter.ViewHolder holder, final int position)
        {
            try
            {
                final GetAllLeadsReportResponse.LstEmpDataItem getSet = listItems.get(position);
                holder.binding.setDataGetSet(getSet);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount()
        {
            return listItems.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder
        {
            RowviewListReportBinding binding;
            public ViewHolder(View itemView) {
                super(itemView);
                binding = DataBindingUtil.bind(itemView);
            }
        }

    }

}