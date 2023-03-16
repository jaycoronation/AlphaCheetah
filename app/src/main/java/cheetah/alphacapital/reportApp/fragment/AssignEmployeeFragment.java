package cheetah.alphacapital.reportApp.fragment;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import cheetah.alphacapital.checkboxlibs.SmoothCheckBox;
import cheetah.alphacapital.network.ApiClient;
import cheetah.alphacapital.network.ApiInterface;
import cheetah.alphacapital.utils.AppAPIUtils;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import cheetah.alphacapital.R;
import cheetah.alphacapital.reportApp.getset.AllEmployeeResponse;


public class AssignEmployeeFragment extends Fragment
{
    private Activity activity;
    private SessionManager sessionManager;
    public ApiInterface apiService;
    private View rootView;
    @BindView(R.id.llLoading)
    LinearLayout llLoading;
    @BindView(R.id.llRetry)
    LinearLayout llRetry;
    @BindView(R.id.llNoInternet)
    LinearLayout llNoInternet;
    @BindView(R.id.llNoData)
    LinearLayout llNoData;
    @BindView(R.id.tvNoDataText)
    TextView tvNoDataText;
    @BindView(R.id.rvEmployeeList)
    RecyclerView rvEmployeeList;
    @BindView(R.id.llSubmit)
    LinearLayout llSubmit;

    private Timer timer = new Timer();
    private final long DELAY = 400;
    public static ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean> listEmployeeMain = new ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean>();
    private ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean> listEmployee = new ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean>();
    private ArrayList<String> listSelected = new ArrayList<String>();
    private ArrayList<AllEmployeeResponse.DataBean.ClientAssignedEmployeeBean> listAssignedEmployeeByClient = new ArrayList<>();
    private EmployeeListAdapter employeeListAdapter;
    private String clientid = "";
    public static Handler handler;

    public AssignEmployeeFragment()
    {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_assign_employee, container, false);

        activity = getActivity();

        sessionManager = new SessionManager(activity);

        apiService = ApiClient.getClient().create(ApiInterface.class);

        ButterKnife.bind(this, rootView);

        clientid = getArguments().getString("clientid");

        setupViews(rootView);

        onClicks();

        handler = new Handler(Looper.getMainLooper())
        {
            public void handleMessage(Message msg)
            {

                if (msg.what == 101)
                {
                   /* try
                    {
                        Log.e("<><> Handler Call Assign" ,"True");
                        ClientDetailsActivity.isSearchVisibleAssign = true;
                        ClientDetailsActivity.txtTitle.setVisibility(View.GONE);
                        ClientDetailsActivity.ivSerach.setVisibility(View.GONE);
                        ClientDetailsActivity.edtSearch.setVisibility(View.VISIBLE);
                        ClientDetailsActivity.ivClose.setVisibility(View.VISIBLE);
                        ClientDetailsActivity.edtSearch.setText(ClientDetailsActivity.serach_text_assign);
                        ClientDetailsActivity.edtSearch.setSelection(ClientDetailsActivity.edtSearch.getText().length());
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }*/
                }
                else if (msg.what == 111 && msg.obj != null)
                {
                    try
                    {
                        String aResponse = (String) msg.obj;

                        if ((null != aResponse))
                        {
                            if (!aResponse.equalsIgnoreCase(""))
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
                                                    try
                                                    {
                                                        ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean> list_search = new ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean>();

                                                        for (int i = 0; i < listEmployee.size(); i++)
                                                        {
                                                            String employeeName = listEmployee.get(i).getFirst_name() + " " + listEmployee.get(i).getLast_name();

                                                            if (employeeName.toLowerCase(Locale.getDefault()).contains(aResponse))
                                                            {
                                                                list_search.add(listEmployee.get(i));
                                                            }
                                                        }

                                                        if (list_search.size() > 0)
                                                        {
                                                            employeeListAdapter = new EmployeeListAdapter(list_search, activity);
                                                            rvEmployeeList.setAdapter(employeeListAdapter);
                                                            llNoData.setVisibility(View.GONE);
                                                        }
                                                        else
                                                        {
                                                            llNoData.setVisibility(View.VISIBLE);
                                                        }
                                                    }
                                                    catch (Exception e)
                                                    {
                                                        e.printStackTrace();
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
                                if (listEmployee.size() > 0)
                                {
                                    employeeListAdapter = new EmployeeListAdapter(listEmployee, activity);
                                    rvEmployeeList.setAdapter(employeeListAdapter);
                                    llNoData.setVisibility(View.GONE);
                                }
                                else
                                {
                                    llNoData.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                else if (msg.what == 112)
                {
                    if (sessionManager.isNetworkAvailable())
                    {
                        llNoInternet.setVisibility(View.GONE);
                        getDataFromApi();
                    }
                    else
                    {
                        llNoInternet.setVisibility(View.VISIBLE);
                    }
                }
            }
        };

        return rootView;
    }

    private void setupViews(View rootView)
    {
        rvEmployeeList.setLayoutManager(new LinearLayoutManager(activity));
        tvNoDataText.setText("No Assign Employee Found.");
        if (sessionManager.isNetworkAvailable())
        {
            llNoInternet.setVisibility(View.GONE);
            getDataFromApi();
        }
        else
        {
            llNoInternet.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        if (sessionManager.isNetworkAvailable())
        {
            llNoInternet.setVisibility(View.GONE);
            getDataFromApi();
        }
        else
        {
            llNoInternet.setVisibility(View.VISIBLE);
        }
        super.onResume();
    }

    private void onClicks()
    {

        llRetry.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try
                {
                    if (sessionManager.isNetworkAvailable())
                    {
                        llNoInternet.setVisibility(View.GONE);
                        getDataFromApi();
                    }
                    else
                    {
                        llNoInternet.setVisibility(View.VISIBLE);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        llSubmit.setOnClickListener(new View.OnClickListener()
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
        llLoading.setVisibility(View.VISIBLE);
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
                        listEmployeeMain = new ArrayList<>();
                        listEmployee = new ArrayList<>();
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
                            rvEmployeeList.setAdapter(employeeListAdapter);
                            llNoData.setVisibility(View.GONE);
                        }
                        else
                        {
                            llNoData.setVisibility(View.VISIBLE);
                        }
                    }
                    else
                    {
                        llNoData.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    llNoData.setVisibility(View.VISIBLE);
                    AppUtils.apiFailedSnackBar(activity);
                }
                llLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<AllEmployeeResponse> call, Throwable t)
            {

                llLoading.setVisibility(View.GONE);
                AppUtils.apiFailedSnackBar(activity);
                llNoData.setVisibility(View.VISIBLE);
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
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_check_box, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position)
        {
            try
            {
                final AllEmployeeResponse.DataBean.AllEmployeeBean getSet = listItems.get(position);

                holder.txtTitle.setText(AppUtils.toDisplayCase(getSet.getFirst_name() + " " + getSet.getLast_name()));

                isdone = true;

                if (getSet.isIs_selected())
                {
                    if (getSet.isIs_primary())
                    {
                        holder.smoothCheckBox.setChecked(true);
                        holder.txtTitle.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    }
                    else
                    {
                        holder.llMain.setVisibility(View.GONE);
                    }
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
                                getSet.setIs_primary(false);
                                listSelected.remove(String.valueOf(getSet.getId()));
                                holder.smoothCheckBox.setChecked(false, true);
                                holder.txtTitle.setTextColor(getResources().getColor(R.color.black));
                            }
                            else
                            {
                                getSet.setIs_selected(true);
                                getSet.setIs_primary(true);
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
                            ids = ids + "," + String.valueOf(listItems.get(i).getId());
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
                    llLoading.setVisibility(View.VISIBLE);
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
                    hashMap.put("employee_ids_primary",getSelectedData());
                    hashMap.put("employee_ids", AssignEmployeeSecondaryFragment.getSelectedData());

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
                    llLoading.setVisibility(View.GONE);
                    llNoInternet.setVisibility(View.GONE);

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

    @Override
    public void onDestroy()
    {
        // isSearchVisibleAssign = false;
        super.onDestroy();
    }
}
