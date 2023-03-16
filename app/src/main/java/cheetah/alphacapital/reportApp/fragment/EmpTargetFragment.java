package cheetah.alphacapital.reportApp.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cheetah.alphacapital.network.ApiClient;
import cheetah.alphacapital.network.ApiInterface;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import cheetah.alphacapital.R;
import cheetah.alphacapital.databinding.FragmentEmpTargetBinding;
import cheetah.alphacapital.databinding.RowviewTargetEmpListBinding;
import cheetah.alphacapital.reportApp.activity.admin.AddEmployeeTargetActivity;
import cheetah.alphacapital.reportApp.getset.TargetListResponse;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class EmpTargetFragment extends Fragment {

    private Activity activity;
    private SessionManager sessionManager;
    private ApiInterface apiService;
    private FragmentEmpTargetBinding binding;

    private List<TargetListResponse.DataBean> listReport = new ArrayList<>();
    private int employeeId = 0;

    public static Handler handler;

    @SuppressLint("ValidFragment")
    public EmpTargetFragment(int employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = getActivity();
        sessionManager = new SessionManager(activity);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_emp_target, container, false);
        initView();
        getData();
        handler = new Handler(message -> {
            if(message.what==1)
            {
                getData();
            }
            return false;
        });
        return binding.getRoot();
    }

    private void initView() {
        binding.noData.tvNoDataText.setText("Sorry, We could not find Targets. Click + button to add Target.");
        binding.ivAddTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, AddEmployeeTargetActivity.class);
                intent.putExtra("isFor", "add");
                intent.putExtra("employeeId",employeeId);
                startActivity(intent);
                AppUtils.startActivityAnimation(activity);
            }
        });
    }

    void getData()
    {
        if(sessionManager.isNetworkAvailable())
        {
            binding.loading.llLoading.setVisibility(View.VISIBLE);
            apiService.getEmployeeTargetList("",
                    "",
                    "0",
                    String.valueOf(employeeId))
                    .enqueue(new Callback<TargetListResponse>() {
                        @Override
                        public void onResponse(Call<TargetListResponse> call, Response<TargetListResponse> response) {
                            if(response.isSuccessful())
                            {
                                if(response.body().isSuccess())
                                {
                                    binding.noData.llNoData.setVisibility(View.GONE);
                                    listReport = response.body().getData();

                                    if(listReport.size()>0)
                                    {
                                        binding.noData.llNoData.setVisibility(View.GONE);
                                        setAdapter();
                                    }
                                    else
                                    {
                                        binding.noData.llNoData.setVisibility(View.VISIBLE);
                                    }
                                }
                                else
                                {
                                    binding.noData.llNoData.setVisibility(View.VISIBLE);
                                }
                            }
                            else
                            {
                                binding.noData.llNoData.setVisibility(View.VISIBLE);
                            }
                            binding.loading.llLoading.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(Call<TargetListResponse> call, Throwable t) {
                            binding.loading.llLoading.setVisibility(View.GONE);
                            binding.noData.llNoData.setVisibility(View.VISIBLE);
                            Log.e("<><><>", "onFailure: "+t.getMessage() );
                        }
                    });
        }
    }

    void setAdapter()
    {
        binding.rvTarget.setLayoutManager(new LinearLayoutManager(activity));
        ReportAdapter reportAdapter = new ReportAdapter();
        binding.rvTarget.setAdapter(reportAdapter);
    }

    public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_target_emp_list, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            TargetListResponse.DataBean bean = listReport.get(position);

            holder.binding.tvDate.setText(String.valueOf(bean.getYear()));
            holder.binding.tvFreshAUM.setText(AppUtils.convertToDecimalValueForListing(String.valueOf(bean.getFresh_aum())));
            holder.binding.tvMeetingNew.setText(String.valueOf(bean.getMeetings_new()));
            holder.binding.tvMeetingExisting.setText(String.valueOf(bean.getMeetings_exisiting()));
            holder.binding.tvInflowOutflow.setText(AppUtils.convertToDecimalValueForListing(String.valueOf(bean.getInflow_outflow())));
            holder.binding.tvSip.setText(AppUtils.convertToDecimalValueForListing(String.valueOf(bean.getSip())));
            holder.binding.tvClientReference.setText(String.valueOf(bean.getReference_client()));
            holder.binding.tvSummaryMail.setText(String.valueOf(bean.getSummery_mail()));
            holder.binding.tvDayForwardMail.setText(String.valueOf(bean.getDay_forward_mail()));
            holder.binding.tvNewClientConverted.setText(String.valueOf(bean.getNew_clients_converted()));
            holder.binding.tvSelfAcquiredAUM.setText(AppUtils.convertToDecimalValueForListing(String.valueOf(bean.getSelf_acquired_aum())));

            holder.binding.ivEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, AddEmployeeTargetActivity.class);
                    intent.putExtra("isFor", "edit");
                    intent.putExtra("employeeId", bean.getEmployee_id());
                    intent.putExtra("data", new Gson().toJson(bean));
                    startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return listReport.size();
        }

        @SuppressWarnings("unused")
        public class ViewHolder extends RecyclerView.ViewHolder
        {
            RowviewTargetEmpListBinding binding;

            ViewHolder(View convertView) {
                super(convertView);
                binding = DataBindingUtil.bind(convertView);
            }
        }
    }
}
