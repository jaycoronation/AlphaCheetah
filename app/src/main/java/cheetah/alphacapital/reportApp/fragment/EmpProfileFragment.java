package cheetah.alphacapital.reportApp.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import cheetah.alphacapital.network.ApiClient;
import cheetah.alphacapital.network.ApiInterface;
import cheetah.alphacapital.reportApp.getset.EmployeeListResponse;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import cheetah.alphacapital.R;
import cheetah.alphacapital.databinding.FragmentEmpProfileBinding;
import cheetah.alphacapital.reportApp.activity.admin.AddEmployeeActivity;
import cheetah.alphacapital.reportApp.getset.EmployeeDetailResponse;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class EmpProfileFragment extends Fragment
{
    private Activity activity;
    private SessionManager sessionManager;
    private ApiInterface apiService;
    private FragmentEmpProfileBinding binding;

    private int employeeId = 0;
    private EmployeeListResponse.DataBean.AllEmployeeBean employeeBean;

    public static Handler handler;

    @SuppressLint("ValidFragment")
    public EmpProfileFragment(int employeeId)
    {
        this.employeeId = employeeId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_emp_profile, container, false);
        activity = getActivity();
        sessionManager = new SessionManager(activity);
        apiService = ApiClient.getClient().create(ApiInterface.class);

        binding.tvEdit.setOnClickListener(view ->
        {
            try
            {
                Intent intent = new Intent(activity, AddEmployeeActivity.class);
                intent.putExtra("isFor", "edit");
                intent.putExtra("ClientInfo", new Gson().toJson(employeeBean));
                startActivity(intent);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });

        getEmployeeDetails();

        handler = new Handler(new Handler.Callback()
        {
            @Override
            public boolean handleMessage(Message message)
            {
                if (message.what == 1)
                {
                    employeeId = (int) message.obj;
                    getEmployeeDetails();
                }
                return false;
            }
        });

        return binding.getRoot();
    }

    void getEmployeeDetails()
    {
        if (sessionManager.isNetworkAvailable())
        {
            binding.loading.llLoading.setVisibility(View.VISIBLE);
            apiService.getEmployeeDetails(String.valueOf(employeeId)).enqueue(new Callback<EmployeeDetailResponse>()
            {
                @Override
                public void onResponse(Call<EmployeeDetailResponse> call, Response<EmployeeDetailResponse> response)
                {
                    if (response.isSuccessful())
                    {
                        if (response.body().isSuccess())
                        {
                            binding.noData.llNoData.setVisibility(View.GONE);
                            binding.tvEmployee.setText(AppUtils.toDisplayCase(response.body().getData().getFirst_name() + " " + response.body().getData().getLast_name()));
                            binding.tvDesignation.setVisibility(response.body().getData().getEmp_type() != null && response.body().getData().getEmp_type().length() > 0 ? View.VISIBLE : View.GONE);
                            binding.tvDesignation.setText(response.body().getData().getEmp_type());
                            binding.tvEmpShortName.setText(response.body().getData().getFirst_name().toUpperCase().charAt(0) + " " + response.body().getData().getLast_name().toUpperCase().charAt(0));
                            binding.tvDateOfJoin.setText(AppUtils.universalDateConvert(response.body().getData().getJoining_date(), "yyyy-MM-dd'T'HH:mm:ss", "dd MMM, yyyy"));
                            binding.tvDegree.setText(response.body().getData().getDegrees() != null && response.body().getData().getDegrees().length() > 0 ? response.body().getData().getDegrees() : "-");
                            binding.tvClearedExam.setText(response.body().getData().getExaminations_cleared() != null && response.body().getData().getExaminations_cleared().length() > 0 ? response.body().getData().getExaminations_cleared() : "-");
                            binding.tvBirthdate.setText(response.body().getData().getBirthdate() != null && response.body().getData().getBirthdate().length() > 0 ? AppUtils.universalDateConvert(response.body().getData().getBirthdate(), "yyyy-MM-dd'T'HH:mm:ss", "dd MMM, yyyy") : "-");
                            binding.tvContact.setText(response.body().getData().getContact_no() != null && response.body().getData().getContact_no().length() > 0 ? response.body().getData().getContact_no() : "-");
                            binding.tvEmail.setText(response.body().getData().getEmail() != null && response.body().getData().getEmail().length() > 0 ? response.body().getData().getEmail() : "-");
                            binding.tvAddress.setText(response.body().getData().getAddress() != null && response.body().getData().getAddress().length() > 0 ? response.body().getData().getAddress() : "-");
                            binding.tvFatherName.setText(response.body().getData().getParents_name() != null && response.body().getData().getParents_name().length() > 0 ? response.body().getData().getParents_name() : "-");
                            binding.tvSpouseName.setText(response.body().getData().getSpouse_name() != null && response.body().getData().getSpouse_name().length() > 0 ? response.body().getData().getSpouse_name() : "-");
                            binding.tvChildrenName.setText(response.body().getData().getChildren_name() != null && response.body().getData().getChildren_name().length() > 0 ? response.body().getData().getChildren_name() : "-");


                            employeeBean = new EmployeeListResponse.DataBean.AllEmployeeBean();
                            employeeBean.setId(response.body().getData().getId());
                            employeeBean.setFirst_name(response.body().getData().getFirst_name());
                            employeeBean.setLast_name(response.body().getData().getLast_name());
                            employeeBean.setContact_no(response.body().getData().getContact_no());
                            employeeBean.setEmp_type(response.body().getData().getEmp_type());
                            employeeBean.setEmail(response.body().getData().getEmail());
                            employeeBean.setAddress(response.body().getData().getAddress());
                            employeeBean.setCountry_id(response.body().getData().getCountry_id());
                            employeeBean.setCountry_name(response.body().getData().getCountry_name());
                            employeeBean.setState_id(response.body().getData().getState_id());
                            employeeBean.setState_name(response.body().getData().getState_name());
                            employeeBean.setCity_id(response.body().getData().getCity_id());
                            employeeBean.setCity_name(response.body().getData().getCity_name());
                            employeeBean.setIs_admin(response.body().getData().isIs_admin());
                            employeeBean.setIs_active(response.body().getData().isIs_active());
                            employeeBean.setPassword(response.body().getData().getPassword());
                            employeeBean.setCreated_date(response.body().getData().getCreated_date());
                            employeeBean.setBirthdate(response.body().getData().getBirthdate());
                            employeeBean.setParents_name(response.body().getData().getParents_name());
                            employeeBean.setSpouse_name(response.body().getData().getSpouse_name());
                            employeeBean.setChildren_name(response.body().getData().getChildren_name());
                            employeeBean.setDegrees(response.body().getData().getDegrees());
                            employeeBean.setExaminations_cleared(response.body().getData().getExaminations_cleared());

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
                public void onFailure(Call<EmployeeDetailResponse> call, Throwable t)
                {
                    AppUtils.showToast(activity, "Error while getting Employee details");
                    binding.noData.llNoData.setVisibility(View.VISIBLE);
                    binding.loading.llLoading.setVisibility(View.GONE);
                }
            });
        }
        else
        {
            AppUtils.showToast(activity, activity.getString(R.string.network_failed_message));
        }
    }


}
