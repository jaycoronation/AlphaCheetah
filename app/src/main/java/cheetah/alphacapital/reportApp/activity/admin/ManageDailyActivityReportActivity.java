package cheetah.alphacapital.reportApp.activity.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import cheetah.alphacapital.textutils.CustomAppEditText;
import cheetah.alphacapital.textutils.CustomTextInputLayout;
import cheetah.alphacapital.textutils.CustomTextViewSemiBold;
import cheetah.alphacapital.utils.AppAPIUtils;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.utils.SessionManager;
import cheetah.alphacapital.R;
import cheetah.alphacapital.reportApp.activity.AddDarActivity;
import cheetah.alphacapital.reportApp.getset.DailyActivityReportGetSet;

public class ManageDailyActivityReportActivity extends AppCompatActivity
{
    private Activity activity;
    private SessionManager sessionManager;
    private Toolbar toolbar;
    private LinearLayout llBackNavigation;
    private ImageView ivLogo;
    private ImageView ivIcon;
    private TextView txtTitle;
    private ImageView ivContactUs;
    private LinearLayout llNotification;
    private TextView tvNoDataText;
    private LinearLayout llLoading, llNoData;
    private CustomTextInputLayout inputEmployee;
    private CustomAppEditText edtEmployee;
    private boolean isStatusBarHidden = false;
    private LinearLayout llNoInternet;
    private LinearLayout llRetry;
    private RecyclerView rvReportList;
    private ImageView ivAddDAR;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<DailyActivityReportGetSet> listReport = new ArrayList<DailyActivityReportGetSet>();
    private ReportListAdapterMain reportListAdapterMain;
    public static Handler handler;
    private String id_employee = "";

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

        try
        {
            super.onCreate(savedInstanceState);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        activity = this;

        sessionManager = new SessionManager(activity);

        setContentView(R.layout.activity_daily_activity_report);

        id_employee = getIntent().getExtras().getString("ID");

        setupViews();

        onClickEvents();

        try
        {
            if (sessionManager.isNetworkAvailable())
            {
                llNoInternet.setVisibility(View.GONE);
                getAllEmployeeReports();
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

        handler = new Handler(new Handler.Callback()
        {
            @Override
            public boolean handleMessage(Message msg)
            {
                try
                {
                    //Reload All Feeds
                    if (msg.what == 100)
                    {
                        if (sessionManager.isNetworkAvailable())
                        {
                            llNoInternet.setVisibility(View.GONE);
                            getAllEmployeeReports();

                        }
                        else
                        {
                            llNoInternet.setVisibility(View.VISIBLE);
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
    }

    private void setupViews()
    {
        try
        {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            llBackNavigation = (LinearLayout) findViewById(R.id.llBackNavigation);
            llNotification = (LinearLayout) findViewById(R.id.llNotification);
            ivLogo = (ImageView) findViewById(R.id.ivLogo);
            ivIcon = (ImageView) findViewById(R.id.ivIcon);
            txtTitle = (CustomTextViewSemiBold) findViewById(R.id.txtTitle);

            ivLogo.setVisibility(View.GONE);
            llNotification.setVisibility(View.GONE);
            txtTitle.setText("View Daily Activity Report");
            llBackNavigation.setVisibility(View.VISIBLE);

            ImageView ivHeader = findViewById(R.id.ivHeader);
            ivHeader.setImageResource(R.drawable.img_portfolio);

            int height = 56;
            if (isStatusBarHidden)
            {
                height = 56 + 25;
                toolbar.findViewById(R.id.viewStatusBar).setVisibility(View.INVISIBLE);
            }
            else
            {
                toolbar.findViewById(R.id.viewStatusBar).setVisibility(View.GONE);
            }
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) ivHeader.getLayoutParams();
            params.height = (int) AppUtils.pxFromDp(activity, height);
            ivHeader.setLayoutParams(params);

            ivLogo = (ImageView) findViewById(R.id.ivLogo);
            ivIcon = (ImageView) findViewById(R.id.ivIcon);
            txtTitle = (TextView) findViewById(R.id.txtTitle);
            ivContactUs = (ImageView) findViewById(R.id.ivContactUs);
            llNotification = (LinearLayout) findViewById(R.id.llNotification);
            llLoading = (LinearLayout) findViewById(R.id.llLoading);
            llNoInternet = (LinearLayout) findViewById(R.id.llNoInternet);
            llRetry = (LinearLayout) findViewById(R.id.llRetry);
            llNoData = findViewById(R.id.llNoData);
            tvNoDataText = (TextView) findViewById(R.id.tvNoDataText);
            tvNoDataText.setText("No Reports Found.");
            ivAddDAR = (ImageView) findViewById(R.id.ivAddDAR);
            inputEmployee = (CustomTextInputLayout) findViewById(R.id.inputEmployee);
            edtEmployee = (CustomAppEditText) findViewById(R.id.edtEmployee);
            rvReportList = (RecyclerView) findViewById(R.id.rvReportList);
            linearLayoutManager = new LinearLayoutManager(activity);
            rvReportList.setLayoutManager(linearLayoutManager);

            if (sessionManager.isAdmin())
            {
                ivAddDAR.setVisibility(View.GONE);
            }
            else
            {
                ivAddDAR.setVisibility(View.VISIBLE);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void onClickEvents()
    {
        llBackNavigation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    AppUtils.hideKeyboard(toolbar, activity);
                    activity.finish();
                    //activity.overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        ivAddDAR.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(activity, AddDarActivity.class));
            }
        });

    }

    private void getAllEmployeeReports()
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
                    listReport = new ArrayList<DailyActivityReportGetSet>();
                    llLoading.setVisibility(View.VISIBLE);
                    llNoData.setVisibility(View.GONE);
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
                    hashMap.put("pageindex", "0");
                    hashMap.put("pagesize", "0");
                    hashMap.put("employee_id", id_employee);
                    hashMap.put("FromDate", "");
                    hashMap.put("ToDate", "");

                    AppUtils.printLog(activity, "GET_DARREPORT_BYEMPLOYEE Request ", hashMap.toString());

                    response = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.GET_DARREPORT_BYEMPLOYEE, hashMap);

                    AppUtils.printLog(activity, "GET_DARREPORT_BYEMPLOYEE Response ", response.toString());

                    JSONObject jsonObject = new JSONObject(response);

                    is_success = AppUtils.getValidAPIBooleanResponseHas(jsonObject, "success");

                    message = AppUtils.getValidAPIStringResponseHas(jsonObject, "message");

                    if (is_success)
                    {

                        if (jsonObject.has("data"))
                        {
                            JSONArray dataArray = jsonObject.getJSONArray("data");

                            if (dataArray.length() > 0)
                            {
                                for (int i = 0; i < dataArray.length(); i++)
                                {
                                    DailyActivityReportGetSet dailyActivityReportGetSet = new DailyActivityReportGetSet();
                                    JSONObject dataObject = (JSONObject) dataArray.get(i);
                                    dailyActivityReportGetSet.setId(AppUtils.getValidAPIIntegerResponseHas(dataObject, "Id"));
                                    dailyActivityReportGetSet.setEmployee_id(AppUtils.getValidAPIIntegerResponseHas(dataObject, "employee_id"));
                                    dailyActivityReportGetSet.setClient_id(AppUtils.getValidAPIIntegerResponseHas(dataObject, "client_id"));
                                    dailyActivityReportGetSet.setActivity_type_id(AppUtils.getValidAPIIntegerResponseHas(dataObject, "activity_type_id"));
                                    dailyActivityReportGetSet.setTimeSpent(AppUtils.getValidAPIIntegerResponseHas(dataObject, "TimeSpent"));
                                    dailyActivityReportGetSet.setTimeSpent_Min(AppUtils.getValidAPIIntegerResponseHas(dataObject, "TimeSpent_Min"));
                                    dailyActivityReportGetSet.setDar_message(AppUtils.getValidAPIStringResponseHas(dataObject, "Dar_message"));
                                    dailyActivityReportGetSet.setRMName(AppUtils.getValidAPIStringResponseHas(dataObject, "RMName"));
                                    dailyActivityReportGetSet.setRemarksComment(AppUtils.getValidAPIStringResponseHas(dataObject, "RemarksComment"));
                                    dailyActivityReportGetSet.setReportDateOrg(AppUtils.getValidAPIStringResponseHas(dataObject, "ReportDate"));
                                    dailyActivityReportGetSet.setReportDate(AppUtils.universalDateConvert(AppUtils.getValidAPIStringResponseHas(dataObject, "ReportDate"), "yyyy-MM-dd'T'HH:mm:ss.SSS", "dd MMM yyyy"));
                                    dailyActivityReportGetSet.setActivity_type_name(AppUtils.getValidAPIStringResponseHas(dataObject, "activity_type_name"));
                                    dailyActivityReportGetSet.setCreated_date(AppUtils.getValidAPIStringResponseHas(dataObject, "created_date"));
                                    dailyActivityReportGetSet.setFirst_name(AppUtils.getValidAPIStringResponseHas(dataObject, "first_name"));
                                    dailyActivityReportGetSet.setLast_name(AppUtils.getValidAPIStringResponseHas(dataObject, "last_name"));
                                    dailyActivityReportGetSet.setC_first_name(AppUtils.getValidAPIStringResponseHas(dataObject, "c_first_name"));
                                    dailyActivityReportGetSet.setC_last_name(AppUtils.getValidAPIStringResponseHas(dataObject, "c_last_name"));
                                    listReport.add(dailyActivityReportGetSet);

                                }
                            }
                        }
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                return null;
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
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
                        if (listReport.size() > 0)
                        {
                            llNoData.setVisibility(View.GONE);

                            HashMap<String, ArrayList<DailyActivityReportGetSet>> hashMap = new HashMap<String, ArrayList<DailyActivityReportGetSet>>();

                            ArrayList<String> listDate = new ArrayList<String>();

                            for (int i = 0; i < listReport.size(); i++)
                            {
                                ArrayList<DailyActivityReportGetSet> listTemp = new ArrayList<DailyActivityReportGetSet>();

                                if (listDate.contains(listReport.get(i).getReportDate()))
                                {
                                    listTemp.addAll(hashMap.get(listReport.get(i).getReportDate()));
                                    listTemp.add(listReport.get(i));
                                    hashMap.put(listReport.get(i).getReportDate(), listTemp);
                                }
                                else
                                {
                                    listDate.add(listReport.get(i).getReportDate());
                                    listTemp.add(listReport.get(i));
                                    hashMap.put(listReport.get(i).getReportDate(), listTemp);
                                }
                            }

                            hashMap = hashMap.entrySet()      // Set<Entry<String, String>>
                                    .stream()               // Stream<Entry<String, String>>
                                    .sorted(Collections.reverseOrder(Map.Entry.comparingByKey())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));

                            reportListAdapterMain = new ReportListAdapterMain(hashMap);
                            rvReportList.setAdapter(reportListAdapterMain);
                        }
                        else
                        {
                            llNoData.setVisibility(View.VISIBLE);
                        }
                    }
                    else
                    {
                        llNoData.setVisibility(View.VISIBLE);
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

    public class ReportListAdapterMain extends RecyclerView.Adapter<ReportListAdapterMain.ViewHolder>
    {
        HashMap<String, ArrayList<DailyActivityReportGetSet>> hashMapItems;

        private ArrayList<String> mKeys;

        private String str_last_date = "";

        public ReportListAdapterMain(HashMap<String, ArrayList<DailyActivityReportGetSet>> hashMapItems)
        {
            this.hashMapItems = hashMapItems;
            mKeys = new ArrayList<String>(hashMapItems.keySet());
        }

        @Override
        public ReportListAdapterMain.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_reports_main, viewGroup, false);
            return new ReportListAdapterMain.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ReportListAdapterMain.ViewHolder holder, final int position)
        {
            try
            {
                String key = getKey(position);

                if (!str_last_date.equals(key))
                {
                    str_last_date = key;

                    if (key.length() > 0)
                    {
                        holder.txtDate.setVisibility(View.VISIBLE);
                        holder.txtDate.setText(key);
                    }
                    else
                    {
                        holder.txtDate.setVisibility(View.GONE);
                    }
                }

                if (hashMapItems.get(key).size() > 0)
                {
                    ReportListAdapter reportListAdapter = new ReportListAdapter(hashMapItems.get(key));
                    holder.rvReportListSub.setAdapter(reportListAdapter);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        public String getKey(int position)
        {
            return (String) mKeys.get(position);
        }

        @Override
        public int getItemCount()
        {
            return hashMapItems.size();
        }

        @SuppressWarnings("unused")
        public class ViewHolder extends RecyclerView.ViewHolder
        {
            private TextView txtDate;
            private RecyclerView rvReportListSub;

            ViewHolder(View convertView)
            {
                super(convertView);
                txtDate = (TextView) convertView.findViewById(R.id.txtDate);
                rvReportListSub = (RecyclerView) convertView.findViewById(R.id.rvReportListSub);
                rvReportListSub.setLayoutManager(new LinearLayoutManager(activity));
            }
        }
    }

    public class ReportListAdapter extends RecyclerView.Adapter<ReportListAdapter.ViewHolder>
    {
        ArrayList<DailyActivityReportGetSet> listItems;

        public ReportListAdapter(ArrayList<DailyActivityReportGetSet> list)
        {
            this.listItems = list;
        }

        @Override
        public ReportListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_reports_list, viewGroup, false);
            return new ReportListAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ReportListAdapter.ViewHolder holder, final int position)
        {
            try
            {
                final DailyActivityReportGetSet getSet = listItems.get(position);


                if (position == listItems.size() - 1)
                {
                    holder.viewLine.setVisibility(View.GONE);
                }
                else
                {
                    holder.viewLine.setVisibility(View.VISIBLE);
                }

                holder.txtMessage.setText(getSet.getDar_message());

                if (getSet.getTimeSpent_Min() > 0 && getSet.getTimeSpent() > 0)
                {
                    holder.txthour.setText(String.valueOf(getSet.getTimeSpent()) + " Hours " + String.valueOf(getSet.getTimeSpent_Min()) + " Miuntes");
                }
                else
                {
                    if (getSet.getTimeSpent() > 0)
                    {
                        holder.txthour.setText(String.valueOf(getSet.getTimeSpent()) + " Hours");
                    }
                    else
                    {
                        holder.txthour.setText(String.valueOf(getSet.getTimeSpent_Min()) + " Miuntes");
                    }
                }

                if (getSet.getReportDateOrg().length() > 0)
                {
                    holder.txtCreatedDate.setVisibility(View.VISIBLE);
                    holder.txtCreatedDate.setText(AppUtils.universalDateConvert(getSet.getReportDateOrg(), "yyyy-MM-dd'T'HH:mm:ss.SSS", "dd MMM yyyy hh:mm a"));
                }
                else
                {
                    holder.txtCreatedDate.setVisibility(View.GONE);
                }

                if (getSet.getActivity_type_name().length() > 0)
                {
                    holder.llActivityType.setVisibility(View.VISIBLE);
                    holder.txtActivityType.setText(getSet.getActivity_type_name());
                }
                else
                {
                    holder.llActivityType.setVisibility(View.GONE);
                }

                if (getSet.getRMName().length() > 0)
                {
                    holder.llRMName.setVisibility(View.VISIBLE);
                    holder.txtRMName.setText(getSet.getRMName());
                }
                else
                {
                    holder.llRMName.setVisibility(View.GONE);
                }

                if (getSet.getRemarksComment().length() > 0)
                {
                    holder.llRemarksComment.setVisibility(View.VISIBLE);
                    holder.txtRemarksComment.setText(getSet.getRemarksComment());
                }
                else
                {
                    holder.llRemarksComment.setVisibility(View.GONE);
                }

                if (getSet.getC_first_name().length() > 0 && getSet.getC_last_name().length() > 0)
                {
                    holder.llClientName.setVisibility(View.VISIBLE);
                    holder.txtClientName.setText(getSet.getC_first_name() + " " + getSet.getC_last_name());
                }
                else
                {
                    holder.llClientName.setVisibility(View.GONE);
                }
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

        @SuppressWarnings("unused")
        public class ViewHolder extends RecyclerView.ViewHolder
        {
            private LinearLayout llMain;
            private TextView txtMessage;
            private LinearLayout llActivityType;
            private TextView txtActivityType;
            private LinearLayout llRMName;
            private TextView txtRMName;
            private LinearLayout llClientName;
            private TextView txtClientName;
            private LinearLayout llRemarksComment;
            private TextView txtRemarksComment;
            private TextView txtCreatedDate;
            private TextView txthour;
            private View viewLine;

            ViewHolder(View convertView)
            {
                super(convertView);
                llMain = (LinearLayout) convertView.findViewById(R.id.llMain);
                txtMessage = (TextView) convertView.findViewById(R.id.txtMessage);
                llActivityType = (LinearLayout) convertView.findViewById(R.id.llActivityType);
                txtActivityType = (TextView) convertView.findViewById(R.id.txtActivityType);
                llRMName = (LinearLayout) convertView.findViewById(R.id.llRMName);
                txtRMName = (TextView) convertView.findViewById(R.id.txtRMName);
                llClientName = (LinearLayout) convertView.findViewById(R.id.llClientName);
                txtClientName = (TextView) convertView.findViewById(R.id.txtClientName);
                llRemarksComment = (LinearLayout) convertView.findViewById(R.id.llRemarksComment);
                txtRemarksComment = (TextView) convertView.findViewById(R.id.txtRemarksComment);
                txtCreatedDate = (TextView) convertView.findViewById(R.id.txtCreatedDate);
                txthour = (TextView) convertView.findViewById(R.id.txthour);
                viewLine = (View) convertView.findViewById(R.id.viewLine);
            }
        }
    }

    @Override
    public void onBackPressed()
    {
        try
        {
            activity.finish();
           //activity.overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        super.onBackPressed();
    }
}
