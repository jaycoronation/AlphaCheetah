package cheetah.alphacapital.reportApp.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import cheetah.alphacapital.R;
import cheetah.alphacapital.checkboxlibs.SmoothCheckBox;
import cheetah.alphacapital.databinding.FragmentAssignEmployeeSecondaryBinding;
import cheetah.alphacapital.network.ApiClient;
import cheetah.alphacapital.network.ApiInterface;
import cheetah.alphacapital.reportApp.getset.AllEmployeeResponse;
import cheetah.alphacapital.utils.AppAPIUtils;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssignEmployeeSecondaryFragment extends Fragment {
    private Activity activity;
    private SessionManager sessionManager;
    public ApiInterface apiService;
    private FragmentAssignEmployeeSecondaryBinding binding;

    private Timer timer = new Timer();
    private final long DELAY = 400;
    public static ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean> listEmployeeMain = new ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean>();
    private ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean> listEmployee = new ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean>();
    private ArrayList<String> listSelected = new ArrayList<String>();
    private ArrayList<AllEmployeeResponse.DataBean.ClientAssignedEmployeeBean> listAssignedEmployeeByClient = new ArrayList<AllEmployeeResponse.DataBean.ClientAssignedEmployeeBean>();
    private EmployeeListAdapter employeeListAdapter;
    private String clientid = "";
    public static Handler handler;

    public AssignEmployeeSecondaryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_assign_employee_secondary, container, false);
        activity = getActivity();

        sessionManager = new SessionManager(activity);

        apiService = ApiClient.getClient().create(ApiInterface.class);

        clientid = getArguments().getString("clientid");

        setupViews();

        onClicks();

        handler = new Handler(Looper.getMainLooper())
        {
            public void handleMessage(Message msg)
            {
               if (msg.what == 112)
                {
                    if (sessionManager.isNetworkAvailable())
                    {
                        binding.llNoInternet.llNoInternet.setVisibility(View.GONE);
                        getDataFromApi();
                    }
                    else
                    {
                        binding.llNoInternet.llNoInternet.setVisibility(View.VISIBLE);
                    }
                }
            }
        };

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        if (sessionManager.isNetworkAvailable())
        {
            binding.llNoInternet.llNoInternet.setVisibility(View.GONE);
            getDataFromApi();
        }
        else
        {
            binding.llNoInternet.llNoInternet.setVisibility(View.VISIBLE);
        }
        super.onResume();
    }

    private void setupViews()
    {
        binding.rvEmployeeList.setLayoutManager(new LinearLayoutManager(activity));
        binding.llNoData.tvNoDataText.setText("No Assign Employee Found.");
        if (sessionManager.isNetworkAvailable())
        {
            binding.llNoInternet.llNoInternet.setVisibility(View.GONE);
            getDataFromApi();
        }
        else
        {
            binding.llNoInternet.llNoInternet.setVisibility(View.VISIBLE);
        }
    }

    private void onClicks()
    {

        binding.llNoInternet.llRetry.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try
                {
                    if (sessionManager.isNetworkAvailable())
                    {
                        binding.llNoInternet.llNoInternet.setVisibility(View.GONE);
                        getDataFromApi();
                    }
                    else
                    {
                        binding.llNoInternet.llNoInternet.setVisibility(View.VISIBLE);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        binding.llSubmit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (sessionManager.isNetworkAvailable())
                {
                    mapClientWithEmployee();
                }
                else
                {
                    Toast.makeText(activity, getResources().getString(R.string.network_failed_message), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void getDataFromApi()
    {
        binding.llLoading.llLoading.setVisibility(View.VISIBLE);
        Call<AllEmployeeResponse> call = apiService.getAllEmployee(clientid, "0", "0");
        call.enqueue(new Callback<AllEmployeeResponse>()
        {
            @Override
            public void onResponse(Call<AllEmployeeResponse> call, Response<AllEmployeeResponse> response)
            {
                if (response.isSuccessful())
                {
                    if (response.body().isSuccess())
                    {
                        listEmployeeMain = new ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean>();
                        listEmployee = new ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean>();
                        listEmployeeMain.addAll(response.body().getData().getAllEmployee());
                        listEmployee.addAll(response.body().getData().getAllEmployee());

                        if (response.body().getData().getClientAssignedEmployee().size() > 0)
                        {
                            listAssignedEmployeeByClient.addAll(response.body().getData().getClientAssignedEmployee());
                        }

                        if (listEmployee.size() > 0)
                        {
                            for (int i = 0; i < listEmployee.size(); i++)
                            {
                                for (int j = 0; j < listAssignedEmployeeByClient.size(); j++)
                                {
                                    if (listAssignedEmployeeByClient.get(j).getId() == listEmployee.get(i).getId())
                                    {
                                        AllEmployeeResponse.DataBean.AllEmployeeBean getSet = listEmployee.get(i);
                                        getSet.setIs_selected(true);
                                        getSet.setIs_primary(listAssignedEmployeeByClient.get(j).isIs_primary());
                                        listEmployee.set(i, getSet);
                                        Log.e("<><>EMPLOYEE ==== " , getSet.getFirst_name() + " ==== " + String.valueOf(getSet.isIs_primary()));
                                    }
                                }
                            }

                            employeeListAdapter = new EmployeeListAdapter(listEmployee, activity);
                            binding.rvEmployeeList.setAdapter(employeeListAdapter);
                            binding.llNoData.llNoData.setVisibility(View.GONE);
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
                }
                else
                {
                    binding.llNoData.llNoData.setVisibility(View.VISIBLE);
                    AppUtils.apiFailedSnackBar(activity);
                }
                binding.llLoading.llLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<AllEmployeeResponse> call, Throwable t)
            {

                binding.llLoading.llLoading.setVisibility(View.GONE);
                AppUtils.apiFailedSnackBar(activity);
                binding.llNoData.llNoData.setVisibility(View.VISIBLE);
            }
        });
    }

    public class EmployeeListAdapter extends RecyclerView.Adapter<EmployeeListAdapter.ViewHolder>
    {
        ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean> listItems;
        private Activity activity;
        private SessionManager sessionManager;
        private boolean isdone;

        public EmployeeListAdapter(ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean> listClient, Activity activity)
        {
            this.listItems = listClient;
            this.activity = activity;
            sessionManager = new SessionManager(activity);
        }

        @Override
        public EmployeeListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_check_box, viewGroup, false);
            return new EmployeeListAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final EmployeeListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position)
        {
            try
            {
                final AllEmployeeResponse.DataBean.AllEmployeeBean getSet = listItems.get(position);

                holder.txtTitle.setText(AppUtils.toDisplayCase(getSet.getFirst_name() + " " + getSet.getLast_name()));

                isdone = true;

                if (getSet.isIs_primary())
                {
                    holder.llMain.setVisibility(View.GONE);
                }
                else
                {
                    holder.llMain.setVisibility(View.VISIBLE);
                }

                if (getSet.isIs_selected())
                {
                    holder.smoothCheckBox.setChecked(true);
                    holder.txtTitle.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                else
                {
                    holder.smoothCheckBox.setChecked(false);
                    holder.txtTitle.setTextColor(getResources().getColor(R.color.black));
                }

                holder.smoothCheckBox.setClickable(false);

                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (!isdone)
                        {
                            if (getSet.isIs_selected())
                            {
                                getSet.setIs_selected(false);
                                listSelected.remove(String.valueOf(getSet.getId()));
                                holder.smoothCheckBox.setChecked(false, true);
                                holder.txtTitle.setTextColor(getResources().getColor(R.color.black));
                            }
                            else
                            {
                                getSet.setIs_selected(true);
                                listSelected.add(String.valueOf(getSet.getId()));
                                holder.smoothCheckBox.setChecked(true, true);
                                holder.txtTitle.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                            }

                            notifyItemChanged(position);
                        }

                    }
                });

                isdone = false;

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        public String getSelectedId()
        {
            String ids = "";

            try
            {
                for (int i = 0; i < listItems.size(); i++)
                {
                    if (listItems.get(i).isIs_selected())
                    {
                        if (ids.equalsIgnoreCase(""))
                        {
                            ids = String.valueOf(listItems.get(i).getId());
                        }
                        else
                        {
                            ids = ids + "," + listItems.get(i).getId();
                        }
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return ids;

        }

        @Override
        public int getItemCount()
        {
            return listItems.size();
        }

        @SuppressWarnings("unused")
        public class ViewHolder extends RecyclerView.ViewHolder
        {
            private TextView txtTitle;
            private LinearLayout llMain;
            private SmoothCheckBox smoothCheckBox;
            private View viewLine;

            ViewHolder(View convertView)
            {
                super(convertView);
                llMain = (LinearLayout) convertView.findViewById(R.id.llMain);
                txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
                smoothCheckBox = (SmoothCheckBox) convertView.findViewById(R.id.smoothCheckBox);
                viewLine = (View) convertView.findViewById(R.id.viewLine);
            }
        }
    }

    private boolean isProductSelect()
    {
        boolean isValid = false;

        if (listEmployeeMain.size() > 0)
        {
            for (int i = 0; i < listEmployeeMain.size(); i++)
            {
                if (listSelected.contains(String.valueOf(listEmployeeMain.get(i).getId())))
                {
                    listEmployeeMain.get(i).setIs_selected(true);
                    isValid = true;
                }
                else
                {
                    listEmployeeMain.get(i).setIs_selected(false);
                }
            }
        }

        return isValid;
    }

    public static String getSelectedData()
    {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < listEmployeeMain.size(); i++)
        {
            if (listEmployeeMain.get(i).isIs_selected())
            {
                if (str.length() == 0)
                {
                    str.append(String.valueOf(listEmployeeMain.get(i).getId()));
                }
                else
                {
                    str.append("," + String.valueOf(listEmployeeMain.get(i).getId()));
                }
            }
        }

        return String.valueOf(str);
    }

    private void mapClientWithEmployee()
    {
        new AsyncTask<Void, Void, Void>()
        {
            private String message = "";

            private boolean is_success = false;

            @Override
            protected void onPreExecute()
            {
                try
                {
                    binding.llLoading.llLoading.setVisibility(View.VISIBLE);
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
                    hashMap.put("client_id", clientid);
                    hashMap.put("employee_ids_primary",AssignEmployeeFragment.getSelectedData());
                    hashMap.put("employee_ids", getSelectedData());

                    AppUtils.printLog(activity, "Map_Client_With_Employee Request ", hashMap.toString());

                    response = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.Map_Client_With_Employee, hashMap);

                    AppUtils.printLog(activity, "Map_Client_With_Employee Response ", response.toString());

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
                    binding.llLoading.llLoading.setVisibility(View.GONE);
                    binding.llNoInternet.llNoInternet.setVisibility(View.GONE);

                    if (is_success)
                    {
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e1)
                {
                    e1.printStackTrace();
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);
    }
    
}