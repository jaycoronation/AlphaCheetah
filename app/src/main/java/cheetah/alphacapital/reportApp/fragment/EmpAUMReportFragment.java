package cheetah.alphacapital.reportApp.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import cheetah.alphacapital.network.ApiClient;
import cheetah.alphacapital.network.ApiInterface;
import cheetah.alphacapital.reportApp.activity.admin.AumEmployeeMonthlySummeryActivity;
import cheetah.alphacapital.utils.AppAPIUtils;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import cheetah.alphacapital.R;
import cheetah.alphacapital.databinding.FragmentEmpAumreportBinding;
import cheetah.alphacapital.databinding.RowviewAumEmpListBinding;
import cheetah.alphacapital.reportApp.getset.AUMReportResponse;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class EmpAUMReportFragment extends Fragment
{
    private Activity activity;
    private SessionManager sessionManager;
    private ApiInterface apiService;
    private FragmentEmpAumreportBinding binding;

    private List<AUMReportResponse.DataBean.AumEmployeeYearlySummeryResultBean> listReport = new ArrayList<>();
    private int employeeId = 0;

    private String YEAR = "Year";
    private String selectedYear = "";

    private List< String> listYear = new ArrayList<>();
    private BottomSheetDialog listDialog;

    @SuppressLint("ValidFragment") public EmpAUMReportFragment(int employeeId)
    {
        this.employeeId = employeeId;
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        activity = getActivity();
        sessionManager = new SessionManager(activity);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_emp_aumreport, container, false);
        initView();
        getYearData();
        return binding.getRoot();
    }

    private void initView()
    {
        selectedYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        binding.noData.tvNoDataText.setText("Sorry, We could not find AUM Report.");

        binding.llSelectYear.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View view)
            {
                showListDialog(YEAR);
            }
        });
    }

    @SuppressLint("StaticFieldLeak") void getYearData()
    {
        if (sessionManager.isNetworkAvailable())
        {
            binding.noInternet.llNoInternet.setVisibility(View.GONE);
            new AsyncTask<Void, Void, Void>()
            {
                @Override protected void onPreExecute()
                {
                    super.onPreExecute();

                    binding.loading.llLoading.setVisibility(View.VISIBLE);
                }

                @Override protected Void doInBackground(Void... voids)
                {
                    getMonthYearData();
                    return null;
                }

                private void getMonthYearData()
                {
                    try
                    {
                        String response = "";

                        HashMap<String, String> hashMap = new HashMap<>();

                        AppUtils.printLog(activity, "month year Request ", hashMap.toString());

                        response = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.MONTH_YEAR, hashMap);

                        AppUtils.printLog(activity, "month year Response ", response.toString());

                        JSONObject jsonObject = new JSONObject(response);

                        boolean isSuccessful = AppUtils.getValidAPIBooleanResponseHas(jsonObject, "success");

                        if (isSuccessful)
                        {
                            if (jsonObject.has("data"))
                            {
                                JSONObject dataObject = jsonObject.getJSONObject("data");
                                JSONArray yearArray = dataObject.getJSONArray("year");
                                if (yearArray.length() > 0)
                                {
                                    listYear.clear();

                                    for (int i = 0; i < yearArray.length(); i++)
                                    {
                                        if (Integer.parseInt(yearArray.getString(i)) <= Calendar.getInstance().get(Calendar.YEAR))
                                        {
                                            listYear.add(yearArray.getString(i));
                                        }
                                    }

                                    if (listYear.size() > 0)
                                    {
                                        selectedYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
                                    }
                                }
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

                @Override protected void onPostExecute(Void aVoid)
                {
                    super.onPostExecute(aVoid);
                    if (listYear.size() > 0)
                    {
                        binding.tvYear.setText(selectedYear);
                    }
                    binding.loading.llLoading.setVisibility(View.GONE);
                    getData();
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);
        }
        else
        {
            binding.noInternet.llNoInternet.setVisibility(View.VISIBLE);
        }
    }

    void getData()
    {
        if (sessionManager.isNetworkAvailable())
        {
            binding.noInternet.llNoInternet.setVisibility(View.GONE);
            binding.loading.llLoading.setVisibility(View.VISIBLE);
            apiService.getAUMYearWise(selectedYear, String.valueOf(employeeId)).enqueue(new Callback<AUMReportResponse>()
            {
                @Override public void onResponse(Call<AUMReportResponse> call, Response<AUMReportResponse> response)
                {
                    if (response.isSuccessful())
                    {
                        if (response.body().isSuccess())
                        {
                            binding.noData.llNoData.setVisibility(View.GONE);
                            listReport = response.body().getData().getAumEmployeeYearlySummeryResult();

                            if (listReport.size() > 0)
                            {
                                binding.noData.llNoData.setVisibility(View.GONE);

                                AUMReportResponse.DataBean.AumEmployeeYearlySummeryResultBean bean = new AUMReportResponse.DataBean.AumEmployeeYearlySummeryResultBean();
                                bean.setMonth_End_AUM(response.body().getData().getSummery().getTotal().getMonthEndAUM());
                                bean.setSip(response.body().getData().getSummery().getTotal().getSIP());
                                bean.setMeetings_Existing(response.body().getData().getSummery().getTotal().getMeetings_Existing());
                                bean.setMeetings_New(response.body().getData().getSummery().getTotal().getMeetings_New());
                                bean.setInflow_Outfolw(response.body().getData().getSummery().getTotal().getInflow_Outfolw());
                                bean.setNew_Clients_Convered(response.body().getData().getSummery().getTotal().getNew_Clients_Convered());
                                bean.setSelf_Acquired_AUM((long) response.body().getData().getSummery().getTotal().getSelf_Acquired_AUM());
                                bean.setSummery_mail(response.body().getData().getSummery().getTotal().getSummery_mail());
                                bean.setDay_forward_mail(response.body().getData().getSummery().getTotal().getDay_forward_mail());
                                bean.setDAR(response.body().getData().getSummery().getTotal().getDAR());
                                bean.setNewClientConverted(response.body().getData().getSummery().getTotal().getNewClientConverted());
                                bean.setSelfAcquiredAUM(response.body().getData().getSummery().getTotal().getSelfAcquiredAUM());
                                bean.setFull_month("Total");
                                listReport.add(bean);

                                bean = new AUMReportResponse.DataBean.AumEmployeeYearlySummeryResultBean();
                                bean.setMonth_End_AUM(response.body().getData().getSummery().getAverage().getMonthEndAUM());
                                bean.setSip(response.body().getData().getSummery().getAverage().getSIP());
                                bean.setMeetings_Existing(response.body().getData().getSummery().getAverage().getMeetings_Existing());
                                bean.setMeetings_New(response.body().getData().getSummery().getAverage().getMeetings_New());
                                bean.setInflow_Outfolw(response.body().getData().getSummery().getAverage().getInflow_Outfolw());
                                bean.setNew_Clients_Convered(response.body().getData().getSummery().getAverage().getNew_Clients_Convered());
                                bean.setSelf_Acquired_AUM(response.body().getData().getSummery().getAverage().getSelf_Acquired_AUM());
                                bean.setSummery_mail(response.body().getData().getSummery().getAverage().getSummery_mail());
                                bean.setDay_forward_mail(response.body().getData().getSummery().getAverage().getDay_forward_mail());
                                bean.setDAR(response.body().getData().getSummery().getAverage().getDAR());
                                bean.setNewClientConverted(response.body().getData().getSummery().getAverage().getNewClientConverted());
                                bean.setSelfAcquiredAUM(response.body().getData().getSummery().getAverage().getSelfAcquiredAUM());
                                bean.setFull_month("Average");
                                listReport.add(bean);

                                bean = new AUMReportResponse.DataBean.AumEmployeeYearlySummeryResultBean();
                                bean.setMonth_End_AUM(response.body().getData().getSummery().getTarget().getMonthEndAUM());
                                bean.setSip(response.body().getData().getSummery().getTarget().getSIP());
                                bean.setMeetings_Existing(response.body().getData().getSummery().getTarget().getMeetings_Existing());
                                bean.setMeetings_New(response.body().getData().getSummery().getTarget().getMeetings_New());
                                bean.setInflow_Outfolw(response.body().getData().getSummery().getTarget().getInflow_Outfolw());
                                bean.setNew_Clients_Convered(response.body().getData().getSummery().getTarget().getNew_Clients_Convered());
                                bean.setSelf_Acquired_AUM(response.body().getData().getSummery().getTarget().getSelf_Acquired_AUM());
                                bean.setSummery_mail(response.body().getData().getSummery().getTarget().getSummery_mail());
                                bean.setDay_forward_mail(response.body().getData().getSummery().getTarget().getDay_forward_mail());
                                bean.setDAR(0);
                                bean.setNewClientConverted(response.body().getData().getSummery().getTarget().getNew_Clients_Convered());
                                bean.setSelfAcquiredAUM(response.body().getData().getSummery().getTarget().getSelf_Acquired_AUM());
                                bean.setFull_month("Target");
                                listReport.add(bean);

                                bean = new AUMReportResponse.DataBean.AumEmployeeYearlySummeryResultBean();
                                bean.setMonth_End_AUM(response.body().getData().getSummery().getVariance().getMonthEndAUM());
                                bean.setSip(response.body().getData().getSummery().getVariance().getSIP());
                                bean.setMeetings_Existing(response.body().getData().getSummery().getVariance().getMeetings_Existing());
                                bean.setMeetings_New(response.body().getData().getSummery().getVariance().getMeetings_New());
                                bean.setInflow_Outfolw(response.body().getData().getSummery().getVariance().getInflow_Outfolw());
                                bean.setNew_Clients_Convered(response.body().getData().getSummery().getVariance().getNew_Clients_Convered());
                                bean.setSelf_Acquired_AUM(response.body().getData().getSummery().getVariance().getSelf_Acquired_AUM());
                                bean.setSummery_mail(response.body().getData().getSummery().getVariance().getSummery_mail());
                                bean.setDay_forward_mail(response.body().getData().getSummery().getVariance().getDay_forward_mail());
                                bean.setDAR(0);
                                bean.setNewClientConverted(response.body().getData().getSummery().getVariance().getNew_Clients_Convered());
                                bean.setSelfAcquiredAUM(response.body().getData().getSummery().getVariance().getSelf_Acquired_AUM());
                                bean.setFull_month("Variance");
                                listReport.add(bean);


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

                @Override public void onFailure(Call<AUMReportResponse> call, Throwable t)
                {
                    binding.loading.llLoading.setVisibility(View.GONE);
                    binding.noData.llNoData.setVisibility(View.VISIBLE);
                    Log.e("<><><>", "onFailure: " + t.getMessage());
                }
            });
        }
        else
        {
            binding.noInternet.llNoInternet.setVisibility(View.VISIBLE);
        }
    }

    void setAdapter()
    {
        binding.rvAUMReport.setLayoutManager(new LinearLayoutManager(activity));
        ReportAdapter reportAdapter = new ReportAdapter();
        binding.rvAUMReport.setAdapter(reportAdapter);
    }

    public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder>
    {
        @Override public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_aum_emp_list, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override public void onBindViewHolder(final ViewHolder holder, final int position)
        {
            AUMReportResponse.DataBean.AumEmployeeYearlySummeryResultBean bean = listReport.get(position);

            holder.binding.tvDate.setText(bean.getFull_month());
            holder.binding.tvMonthEndAum.setText(AppUtils.convertToDecimalValueForListing(String.valueOf(bean.getMonth_End_AUM())));
            holder.binding.tvMeetingNew.setText(String.valueOf(bean.getMeetings_New()));
            holder.binding.tvMeetingExisting.setText(String.valueOf(bean.getMeetings_Existing()));
            holder.binding.tvInflowOutflow.setText(AppUtils.convertToDecimalValueForListing(String.valueOf(bean.getInflow_Outfolw())));
            holder.binding.tvSip.setText(AppUtils.convertToDecimalValueForListing(String.valueOf(bean.getSip())));
            holder.binding.tvClientReference.setText(String.valueOf(bean.getReferences()));
            holder.binding.tvSummaryMail.setText(String.valueOf(bean.getSummery_mail()));
            holder.binding.tvDayForwardMail.setText(String.valueOf(bean.getDay_forward_mail()));
            holder.binding.tvNewClientConverted.setText(String.valueOf(bean.getNew_Clients_Convered()));
            holder.binding.tvSelfAcquiredAUM.setText(AppUtils.convertToDecimalValueForListing(String.valueOf(bean.getSelf_Acquired_AUM())));
            holder.binding.tvDAR.setText(String.valueOf(bean.getDAR()));

            holder.binding.tvDate.setOnClickListener(new View.OnClickListener()
            {
                @Override public void onClick(View v)
                {
                    Intent intent = new Intent(activity, AumEmployeeMonthlySummeryActivity.class);
                    intent.putExtra("selectedEmployee", String.valueOf(employeeId));
                    intent.putExtra("selectedMonth", String.valueOf(bean.getMonth()));
                    intent.putExtra("selectedMonthName", bean.getFull_month());
                    intent.putExtra("selectedYear", selectedYear);
                    startActivity(intent);
                }
            });
        }

        @Override public int getItemCount()
        {
            return listReport.size();
        }

        @SuppressWarnings("unused")
        public class ViewHolder extends RecyclerView.ViewHolder
        {
            RowviewAumEmpListBinding binding;

            ViewHolder(View convertView)
            {
                super(convertView);
                binding = DataBindingUtil.bind(convertView);
            }
        }
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
            @Override public void onClick(View v)
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
            @Override public void onClick(View v)
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

        @Override public DialogListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowview_common_list, parent, false);
            return new DialogListAdapter.ViewHolder(v);
        }

        @Override public void onBindViewHolder(DialogListAdapter.ViewHolder holder, final int position)
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
                holder.tvValue.setText(listYear.get(position).toString());
                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override public void onClick(View v)
                    {
                        listDialog.dismiss();
                        if (!selectedYear.equalsIgnoreCase(listYear.get(position).toString()))
                        {
                            if (sessionManager.isNetworkAvailable())
                            {
                                selectedYear = listYear.get(position);
                                binding.tvYear.setText(selectedYear);
                                getData();
                            }
                        }
                    }
                });
            }
        }

        @Override public int getItemCount()
        {
            if (isFor.equalsIgnoreCase(YEAR))
            {
                return listYear.size();
            }
            else
            {
                return 0;
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
