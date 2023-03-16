package cheetah.alphacapital.reportApp.activity.admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cheetah.alphacapital.reportApp.getset.RMlistResponse;
import cheetah.alphacapital.utils.AppAPIUtils;
import cheetah.alphacapital.utils.AppUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import cheetah.alphacapital.R;
import cheetah.alphacapital.databinding.ActivityAddDarnewBinding;
import cheetah.alphacapital.databinding.RowviewDarListBinding;
import cheetah.alphacapital.reportApp.getset.NewClientListResponse;
import cheetah.alphacapital.reportApp.getset.CommonGetSet;
import cheetah.alphacapital.reportApp.getset.DARDetailsReportListResponse;

public class AddDARNewActivity extends BaseActivity {
    private ActivityAddDarnewBinding binding;
    private List<DARDetailsReportListResponse.DataBean> listReport = new ArrayList<>();
    private ReportAdapter reportAdapter;

    private List<NewClientListResponse.DataBean.AllClientByEmployeeBean> listClient = new ArrayList<>();
    private List<CommonGetSet> listActivityType = new ArrayList<CommonGetSet>();
    private List<RMlistResponse.DataBean> listRMName = new ArrayList<>();

    public static Handler handler;
    private boolean isStatusBarHidden = false;
    private int editClickedPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            {
                Window window = getWindow();
                window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                isStatusBarHidden = true;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(activity, R.layout.activity_add_darnew);

        initView();

        if (sessionManager.isNetworkAvailable())
        {
            Log.e("client List >>> ", "onCreate: " + sessionManager.getClientList());
            getClientData();
        }
        else
        {
            binding.noInternet.llNoInternet.setVisibility(View.VISIBLE);
        }

        handler = new Handler(message -> {
            if (message.what == 1)//For Get List Object to add in list
            {
                DARDetailsReportListResponse.DataBean bean = (DARDetailsReportListResponse.DataBean) message.obj;
                if (message.arg1 == 0)
                {
                    listReport.add(bean);
                }
                else
                {
                    listReport.set(editClickedPosition, bean);
                }
                setAdapter();
            }
            return false;
        });
    }

    private void initView()
    {
        binding.toolbar.ivContactUs.setVisibility(View.VISIBLE);
        binding.toolbar.ivContactUs.setImageResource(R.drawable.ic_add_white);
        binding.noData.tvNoDataText.setText("Click on + button to add DAR");
        binding.rvReport.setLayoutManager(new LinearLayoutManager(activity));
        int height = 56;
        if (isStatusBarHidden)
        {
            height = 56 + 25;
            binding.toolbar.toolbar.findViewById(R.id.viewStatusBar).setVisibility(View.INVISIBLE);
        }
        else
        {
            binding.toolbar.toolbar.findViewById(R.id.viewStatusBar).setVisibility(View.GONE);
        }
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) binding.toolbar.ivHeader.getLayoutParams();
        params.height = (int) AppUtils.pxFromDp(activity, height);
        binding.toolbar.ivHeader.setLayoutParams(params);
        binding.toolbar.ivHeader.setImageResource(R.drawable.img_portfolio);

        binding.toolbar.ivLogo.setVisibility(View.GONE);
        binding.toolbar.llNotification.setVisibility(View.GONE);
        binding.toolbar.txtTitle.setText("Add DAR");
        binding.toolbar.llBackNavigation.setVisibility(View.VISIBLE);

        binding.toolbar.llBackNavigation.setOnClickListener(view -> {
            finish();
            finishActivityAnimation();
        });

        binding.toolbar.ivContactUs.setOnClickListener(view -> {
            Intent intent = new Intent(activity, AddDarEntryActivity.class);
            intent.putExtra("isFor", "add");
            startActivity(intent);
        });

        binding.tvSubmit.setOnClickListener(view -> {
            if (listReport.size() > 0)
            {
                for (int i = 0; i < listReport.size(); i++)
                {
                    AddDARReport(listReport.get(i), i);
                }
            }
            else
            {
                AppUtils.showToast(activity, "Please add DAR to submit");
            }
        });

        setAdapter();
    }

    @SuppressLint("StaticFieldLeak")
    private void getAllData()
    {
        new AsyncTask<Void, Void, Void>() {
            private boolean isSuccessForType = false;

            @Override
            protected void onPreExecute()
            {
                binding.loading.llLoading.setVisibility(View.VISIBLE);
                listActivityType = new ArrayList<CommonGetSet>();
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params)
            {
                getAllActivityType();
                return null;
            }

            private void getAllActivityType()
            {
                try
                {
                    String response = "";

                    HashMap<String, String> hashMap = new HashMap<>();

                    response = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.GET_ALL_ACTIVITY_TYPE, hashMap);

                    AppUtils.printLog(activity, "<><> GET_ALL_ACTIVITY_TYPE", response.toString());

                    JSONObject jsonObject = new JSONObject(response);

                    isSuccessForType = AppUtils.getValidAPIBooleanResponseHas(jsonObject, "success");

                    if (isSuccessForType)
                    {
                        if (jsonObject.has("data"))
                        {
                            JSONArray dataArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < dataArray.length(); i++)
                            {
                                JSONObject dataObject = dataArray.getJSONObject(i);
                                CommonGetSet commonGetSet = new CommonGetSet();
                                commonGetSet.setId(AppUtils.getValidAPIIntegerResponseHas(dataObject, "Id"));
                                commonGetSet.setName(AppUtils.getValidAPIStringResponseHas(dataObject, "ActivityTypeName"));
                                commonGetSet.setSelect(AppUtils.getValidAPIBooleanResponseHas(dataObject, "IsActive"));
                                listActivityType.add(commonGetSet);
                            }
                        }

                        sessionManager.setActivityList(new Gson().toJson(listActivityType));
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onPostExecute(Void result)
            {
                getRMData();
                super.onPostExecute(result);

            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);
    }

    void getClientData()
    {
        binding.toolbar.ivContactUs.setVisibility(View.GONE);
        if (sessionManager.isNetworkAvailable())
        {
            binding.loading.llLoading.setVisibility(View.VISIBLE);
            apiService.getAllClientByEmployee_new("",
                    "",
                    sessionManager.getUserId())
                    .enqueue(new Callback<NewClientListResponse>() {
                        @Override
                        public void onResponse(Call<NewClientListResponse> call, Response<NewClientListResponse> response)
                        {
                            if (response.isSuccessful())
                            {
                                try
                                {
                                    if (response.body().isSuccess())
                                    {
                                        listClient = response.body().getData().getAllClientByEmployee();
                                        sessionManager.setClientList(new Gson().toJson(listClient));
                                        getAllData();
                                    }
                                    else
                                    {
                                        getAllData();
                                    }
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                    getAllData();
                                }
                            }
                            else
                            {
                                getAllData();
                            }

                            //binding.loading.llLoading.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(Call<NewClientListResponse> call, Throwable t)
                        {
                            Log.e("<><><>", "onFailure: " + t.getCause().toString());
                            //binding.loading.llLoading.setVisibility(View.GONE);
                            getAllData();
                        }
                    });
        }
    }

    void getRMData()
    {
        if (sessionManager.isNetworkAvailable())
        {
            binding.loading.llLoading.setVisibility(View.VISIBLE);
            apiService.getRMList()
                    .enqueue(new Callback<RMlistResponse>() {
                        @Override
                        public void onResponse(Call<RMlistResponse> call, Response<RMlistResponse> response)
                        {
                            if (response.isSuccessful())
                            {
                                if (response.body().isSuccess())
                                {
                                    listRMName = response.body().getData();
                                    sessionManager.setRMList(new Gson().toJson(listRMName));
                                }
                            }
                            binding.loading.llLoading.setVisibility(View.GONE);
                            binding.toolbar.ivContactUs.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onFailure(Call<RMlistResponse> call, Throwable t)
                        {
                            binding.loading.llLoading.setVisibility(View.GONE);
                            binding.toolbar.ivContactUs.setVisibility(View.VISIBLE);
                        }
                    });
        }

    }

    private void setAdapter()
    {
        if (reportAdapter != null)
        {
            reportAdapter.notifyDataSetChanged();
        }
        else
        {
            reportAdapter = new ReportAdapter();
            binding.rvReport.setAdapter(reportAdapter);
        }

        if (listReport.size() == 0)
        {
            binding.noData.llNoData.setVisibility(View.VISIBLE);
            binding.tvSubmit.setVisibility(View.GONE);
        }
        else
        {
            binding.noData.llNoData.setVisibility(View.GONE);
            binding.tvSubmit.setVisibility(View.VISIBLE);

            int total = (sumHours() * 60) + sumMins();

            int hours = total / 60;
            int minutes = total % 60;

            Log.e("<><><><>", "setAdapter:  Hours : " + hours + "   Minutes : " + minutes);

        }
    }

    public int sumHours()
    {
        int sum = 0;

        for (DARDetailsReportListResponse.DataBean bean : listReport)
            sum = sum + bean.getTimeSpent();
        return sum;
    }

    public int sumMins()
    {
        int sum = 0;
        for (DARDetailsReportListResponse.DataBean bean : listReport)
            sum = sum + bean.getTimeSpent_Min();
        return sum;
    }

    public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_dar_list, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position)
        {

            DARDetailsReportListResponse.DataBean bean = listReport.get(position);

            holder.binding.tvMessage.setText(bean.getDar_message());
            //holder.binding.tvClient.setText(bean.getC_first_name() + " "+bean.getC_last_name());
            holder.binding.tvClient.setText(bean.getC_first_name());
            holder.binding.tvActivityType.setText(bean.getActivity_type_name());

            if (bean.getReportDate_T_Y_D() == 1)
            {
                holder.binding.tvDate.setText("Today");
            }
            else if (bean.getReportDate_T_Y_D() == 2)
            {
                holder.binding.tvDate.setText("Yesterday");
            }
            else
            {
                holder.binding.tvDate.setText("Day Before Yesterday");
            }

            holder.binding.tvDateTime.setText(bean.getTimeSpent() + " Hours, " + bean.getTimeSpent_Min() + " Minutes");
            holder.binding.tvEmployee.setText(bean.getFirst_name() + " " + bean.getLast_name());
            holder.binding.tvRemark.setText(bean.getRemarksComment());

            holder.binding.ivEdit.setOnClickListener(view -> {
                editClickedPosition = position;
                Intent intent = new Intent(activity, AddDarEntryActivity.class);
                intent.putExtra("isFor", "edit");
                intent.putExtra("data", new Gson().toJson(listReport.get(position)));
                startActivity(intent);
            });

            holder.binding.ivDelete.setOnClickListener(view -> {
                if (listReport.size() > 0)
                {
                    listReport.remove(position);

                    if (listReport.size() == 0)
                    {
                        binding.noData.llNoData.setVisibility(View.VISIBLE);
                    }

                    setAdapter();
                }
            });

        }

        @Override
        public int getItemCount()
        {
            return listReport.size();
        }

        @SuppressWarnings("unused")
        public class ViewHolder extends RecyclerView.ViewHolder {
            RowviewDarListBinding binding;

            ViewHolder(View convertView)
            {
                super(convertView);
                binding = DataBindingUtil.bind(convertView);
                binding.ivDelete.setVisibility(View.VISIBLE);
                binding.ivEdit.setVisibility(View.VISIBLE);
                binding.llEmployee.setVisibility(View.GONE);
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void AddDARReport(DARDetailsReportListResponse.DataBean bean, int position)
    {
        try
        {
            new AsyncTask<Void, Void, Void>() {
                private String message = "";
                private boolean is_success = false;

                @Override
                protected void onPreExecute()
                {
                    try
                    {
                        binding.loading.llLoading.setVisibility(View.VISIBLE);
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
                        hashMap.put("employee_id", AppUtils.getEmployeeIdForAdmin(activity));
                        hashMap.put("client_id", String.valueOf(bean.getClient_id()));
                        hashMap.put("activity_type_id", String.valueOf(bean.getActivity_type_id()));
                        hashMap.put("Dar_message", String.valueOf(bean.getDar_message()));
                        hashMap.put("RMName", bean.getRMName());

                        if (bean.getTimeSpent() == 0)
                        {
                            hashMap.put("TimeSpent", "0");
                        }
                        else
                        {
                            hashMap.put("TimeSpent", String.valueOf(bean.getTimeSpent()));
                        }

                        if (bean.getTimeSpent_Min() == 0)
                        {
                            hashMap.put("TimeSpent_Min", "0");
                        }
                        else
                        {
                            hashMap.put("TimeSpent_Min", String.valueOf(bean.getTimeSpent_Min()));
                        }

                        hashMap.put("RemarksComment", bean.getRemarksComment());

                        hashMap.put("IsTodayReport", String.valueOf(bean.getReportDate_T_Y_D()));


                        AppUtils.printLog(activity, "ADD_DAR_REPORT Request ", hashMap.toString());
                        response = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.ADD_DAR_REPORT, hashMap);
                        AppUtils.printLog(activity, "ADD_DAR_REPORT Response ", response.toString());

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
                        binding.loading.llLoading.setVisibility(View.GONE);

                        if (is_success)
                        {
                            if (position == listReport.size() - 1)
                            {
                                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                                listReport.clear();
                                binding.noData.llNoData.setVisibility(View.VISIBLE);
                                setAdapter();
                            }
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
}
