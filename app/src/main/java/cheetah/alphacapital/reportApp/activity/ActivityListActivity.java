package cheetah.alphacapital.reportApp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cheetah.alphacapital.classes.EndlessRecyclerViewScrollListener;
import cheetah.alphacapital.utils.AppAPIUtils;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.utils.SessionManager;
import cheetah.alphacapital.R;
import cheetah.alphacapital.reportApp.getset.ActivityListGetSet;
import cheetah.alphacapital.reportApp.getset.AssignedClientGetSetOld;


public class ActivityListActivity extends AppCompatActivity
{
    private Activity activity;
    private SessionManager sessionManager;
    private Toolbar toolbar;
    private View viewStatusBar;
    private LinearLayout llBackNavigation;
    private ImageView ivLogo;
    private ImageView ivIcon;
    private TextView txtTitle;
    private ImageView ivContactUs;
    private LinearLayout llNotification;
    private LinearLayout llLoading;
    private ProgressBar pbLoading;
    private TextView txtLoading;
    private LinearLayout llNoInternet;
    private LinearLayout llRetry;
    private TextView txtRetry;
    private RecyclerView rvActivityList;
    private LinearLayout llLoadingMore;
    private TextView tvLoadingText;
    private LinearLayout llNoData;
    private ImageView ivNoData;
    private ImageView ivAddActivity;
    private LinearLayoutManager linearLayoutManager;
    private boolean isStatusBarHidden = false;
    private boolean isLoading = false;
    private int pageIndex = 0;
    private String pagesize = "50";
    private boolean isLastPage = false;
    private AssignedClientGetSetOld getSet = new AssignedClientGetSetOld();
    private ArrayList<ActivityListGetSet> listActivity = new ArrayList<ActivityListGetSet>();
    private ActivityListAdapter activityListAdapter;
    public static Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
       /* try
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            {
                Window w = getWindow();
                w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                isStatusBarHidden = true;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }*/

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

        setContentView(R.layout.activity_list_activity);

        getSet = getIntent().getExtras().getParcelable("ClientInfo");

        setupViews();

        onClickEvents();

        try
        {
            if (sessionManager.isNetworkAvailable())
            {
                llNoInternet.setVisibility(View.GONE);
                pageIndex = 0;
                isLastPage = false;
                getAllActivity(true);
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
                            pageIndex = 0;
                            isLastPage = false;
                            getAllActivity(true);
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
            viewStatusBar = (View) findViewById(R.id.viewStatusBar);
            llBackNavigation = (LinearLayout) findViewById(R.id.llBackNavigation);
            ivLogo = (ImageView) findViewById(R.id.ivLogo);
            ivIcon = (ImageView) findViewById(R.id.ivIcon);
            txtTitle = (TextView) findViewById(R.id.txtTitle);
            ivContactUs = (ImageView) findViewById(R.id.ivContactUs);
            llNotification = (LinearLayout) findViewById(R.id.llNotification);
            llLoading = (LinearLayout) findViewById(R.id.llLoading);
            pbLoading = (ProgressBar) findViewById(R.id.pbLoading);
            txtLoading = (TextView) findViewById(R.id.txtLoading);
            llNoInternet = (LinearLayout) findViewById(R.id.llNoInternet);
            llRetry = (LinearLayout) findViewById(R.id.llRetry);
            txtRetry = (TextView) findViewById(R.id.txtRetry);
            rvActivityList = (RecyclerView) findViewById(R.id.rvActivityList);
            llLoadingMore = (LinearLayout) findViewById(R.id.llLoadingMore);
            tvLoadingText = (TextView) findViewById(R.id.tvLoadingText);
            llNoData = (LinearLayout) findViewById(R.id.llNoData);
            ivNoData = (ImageView) findViewById(R.id.ivNoData);
            ivAddActivity = (ImageView) findViewById(R.id.ivAddActivity);
            linearLayoutManager = new LinearLayoutManager(activity);
            rvActivityList.setLayoutManager(linearLayoutManager);

           /* setSupportActionBar(toolbar);

            ImageView ivHeader = findViewById(R.id.ivHeader);
            ivHeader.setImageResource(R.drawable.img_portfolio);

            int height = 56;
            if(isStatusBarHidden)
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
            ivHeader.setLayoutParams(params);*/

            ivLogo.setVisibility(View.GONE);

            llNotification.setVisibility(View.GONE);

            txtTitle.setText("Activity List");

            llBackNavigation.setVisibility(View.VISIBLE);

            rvActivityList.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager)
            {
                @Override
                public void onScrolled(RecyclerView view, int dx, int dy)
                {
                    super.onScrolled(view, dx, dy);
                }

                @Override
                public void onLoadMore(int page, int totalItemsCount)
                {
                    try
                    {
                        if (!isLoading && !isLastPage)
                        {
                            if (sessionManager.isNetworkAvailable())
                            {
                                llLoadingMore.setVisibility(View.VISIBLE);
                                getAllActivity(false);
                            }
                            else
                            {
                                llLoadingMore.setVisibility(View.GONE);
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });

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
                        pageIndex = 0;
                        isLastPage = false;
                        getAllActivity(true);
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

        ivAddActivity.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    Intent intent = new Intent(activity, AddNewActivity.class);
                    intent.putExtra("ClientInfo", (Parcelable) getSet);
                    intent.putExtra("isForEdit", false);
                    startActivity(intent);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getAllActivity(final boolean isFirstTime)
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
                    isLoading = true;

                    if (isFirstTime)
                    {
                        llLoading.setVisibility(View.VISIBLE);
                        listActivity = new ArrayList<ActivityListGetSet>();
                    }
                    else
                    {
                        llLoadingMore.setVisibility(View.VISIBLE);
                    }

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
                    hashMap.put("employeeid",AppUtils.getEmployeeIdForAdmin(activity));
                    hashMap.put("clientid", String.valueOf(getSet.getId()));
                    hashMap.put("pageindex", String.valueOf(pageIndex));
                    hashMap.put("pagesize", pagesize);
                    hashMap.put("activity_statusid", "");
                    hashMap.put("fromdate", "");
                    hashMap.put("schemeid", "");
                    hashMap.put("todate", "");

                    AppUtils.printLog(activity, "GET_ALL_ACTIVITY Request ", hashMap.toString());

                    response = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.GET_ALL_ACTIVITY, hashMap);

                    AppUtils.printLog(activity, "GET_ALL_ACTIVITY Response ", response.toString());

                    JSONObject jsonObject = new JSONObject(response);

                    is_success = AppUtils.getValidAPIBooleanResponseHas(jsonObject,"success");

                    message = AppUtils.getValidAPIStringResponseHas(jsonObject,"message");

                    if (is_success)
                    {
                        if (jsonObject.has("data"))
                        {
                            JSONArray dataArray = jsonObject.getJSONArray("data");

                            pageIndex = pageIndex + 1;

                            if (dataArray.length() == 0 || dataArray.length() % 50 != 0)
                            {
                                isLastPage = true;
                            }

                            if (dataArray.length() > 0)
                            {
                                for (int i = 0; i < dataArray.length(); i++)
                                {
                                    ActivityListGetSet activityListGetSet = new ActivityListGetSet();
                                    JSONObject dataObject = (JSONObject) dataArray.get(i);

                                    activityListGetSet.setId(AppUtils.getValidAPIIntegerResponseHas(dataObject,"id"));
                                    activityListGetSet.setEmployee_id(AppUtils.getValidAPIIntegerResponseHas(dataObject,"employee_id"));
                                    activityListGetSet.setClient_id(AppUtils.getValidAPIIntegerResponseHas(dataObject,"client_id"));
                                    activityListGetSet.setScheme_id(AppUtils.getValidAPIIntegerResponseHas(dataObject,"scheme_id"));
                                    activityListGetSet.setActivity_status_id(AppUtils.getValidAPIIntegerResponseHas(dataObject,"activity_status_id"));
                                    activityListGetSet.setActivity_message(AppUtils.getValidAPIStringResponseHas(dataObject,"activity_message"));
                                    activityListGetSet.setDue_date(AppUtils.getValidAPIStringResponseHas(dataObject,"due_date"));
                                    activityListGetSet.setCreated_date(AppUtils.getValidAPIStringResponseHas(dataObject,"created_date"));
                                    activityListGetSet.setActivity_added_by(AppUtils.getValidAPIStringResponseHas(dataObject,"activity_added_by"));
                                    activityListGetSet.setClient_name(AppUtils.getValidAPIStringResponseHas(dataObject,"client_name"));
                                    activityListGetSet.setScheme_name(AppUtils.getValidAPIStringResponseHas(dataObject,"scheme_name"));
                                    activityListGetSet.setActivity_status(AppUtils.getValidAPIStringResponseHas(dataObject,"activity_status"));
                                    activityListGetSet.setActivity_hours(AppUtils.getValidAPIStringResponseHas(dataObject,"activity_hours"));
                                    activityListGetSet.setActivity_min(AppUtils.getValidAPIStringResponseHas(dataObject,"activity_min"));
                                    activityListGetSet.setAll_employee_ids(AppUtils.getValidAPIStringResponseHas(dataObject,"all_employee_ids"));
                                    activityListGetSet.setAll_employee_name(AppUtils.getValidAPIStringResponseHas(dataObject,"all_employee_name"));
                                    activityListGetSet.setActivity_type_id(AppUtils.getValidAPIIntegerResponseHas(dataObject,"activity_type_id"));
                                    activityListGetSet.setActivityTypeName(AppUtils.getValidAPIStringResponseHas(dataObject,"ActivityTypeName"));

                                    listActivity.add(activityListGetSet);

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

            @Override
            protected void onPostExecute(Void result)
            {
                super.onPostExecute(result);

                try
                {
                    isLoading = false;
                    llLoading.setVisibility(View.GONE);
                    llLoadingMore.setVisibility(View.GONE);
                    llNoInternet.setVisibility(View.GONE);

                    if (is_success)
                    {
                        if (isFirstTime)
                        {
                            if (listActivity.size() > 0)
                            {
                                activityListAdapter = new ActivityListAdapter(listActivity);
                                rvActivityList.setAdapter(activityListAdapter);
                                llNoData.setVisibility(View.GONE);
                            }
                            else
                            {
                                llNoData.setVisibility(View.VISIBLE);
                            }
                        }
                        else
                        {
                            llNoData.setVisibility(View.GONE);

                            if (activityListAdapter.getItemCount() > 0)
                            {
                                activityListAdapter.notifyDataSetChanged();
                            }
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

    public class ActivityListAdapter extends RecyclerView.Adapter<ActivityListAdapter.ViewHolder>
    {
        ArrayList<ActivityListGetSet> listItems;
        private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

        public ActivityListAdapter(ArrayList<ActivityListGetSet> listClient)
        {
            this.listItems = listClient;
            viewBinderHelper.setOpenOnlyOne(true);
        }

        @Override
        public ActivityListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_activity_list_new, viewGroup, false);
            return new ActivityListAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ActivityListAdapter.ViewHolder holder, final int position)
        {
            try
            {
                final ActivityListGetSet activityListGetSet = listItems.get(position);
                viewBinderHelper.bind(holder.swipeLayout, String.valueOf(activityListGetSet.getId()));
                holder.txtActivityName.setText(activityListGetSet.getActivity_message());
                holder.txtClientName.setText(activityListGetSet.getClient_name());
                holder.txtEmployeeName.setText(activityListGetSet.getAll_employee_name());
                holder.txtActivityName.setText(activityListGetSet.getActivityTypeName());

                if(activityListGetSet.getActivity_min().length() >0 && activityListGetSet.getActivity_hours().length() >0)
                {
                    holder.txthour.setText(String.valueOf(activityListGetSet.getActivity_hours()) + " Hours " + String.valueOf(activityListGetSet.getActivity_min()) + " Miuntes");
                }
                else
                {
                    if(activityListGetSet.getActivity_hours().length() >0)
                    {
                        holder.txthour.setText(String.valueOf(activityListGetSet.getActivity_hours()) + " Hours");
                    }
                    else
                    {
                        holder.txthour.setText(String.valueOf(activityListGetSet.getActivity_min()) + " Miuntes");
                    }
                }

                 /* if (activityListGetSet.getDue_date().length() > 0)
                {
                    holder.viewline.setVisibility(View.VISIBLE);
                    holder.txtDueDate.setVisibility(View.VISIBLE);
                    holder.txtDueDate.setText(AppUtils.universalDateConvert(activityListGetSet.getDue_date(), "yyyy-MM-dd'T'HH:mm:ss", "dd MMM yyyy hh:mm a"));
                }
                else
                {
                    holder.viewline.setVisibility(View.GONE);
                    holder.txtDueDate.setVisibility(View.GONE);
                }*/

                if (activityListGetSet.getCreated_date().length() > 0)
                {
                    holder.txtCreatedDate.setVisibility(View.VISIBLE);
                    holder.txtCreatedDate.setText(AppUtils.universalDateConvert(activityListGetSet.getCreated_date(), "yyyy-MM-dd'T'HH:mm:ss.SSS", "dd MMM yyyy hh:mm a"));
                }
                else
                {
                    holder.txtCreatedDate.setVisibility(View.GONE);
                }

                holder.llEdit.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        try
                        {
                            holder.swipeLayout.close(true);
                            Intent intent = new Intent(activity, AddNewActivity.class);
                            intent.putExtra("EditGetSet", (Parcelable) activityListGetSet);
                            intent.putExtra("ClientInfo", (Parcelable) getSet);
                            intent.putExtra("isForEdit", true);
                            activity.startActivity(intent);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                });

                holder.llDelete.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        try
                        {
                            holder.swipeLayout.close(true);
                            selectDeleteDialog(activityListGetSet, position);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                });

                setViewHeight(holder.mainLayout, holder.layoutAction);
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
            private TextView txtActivityName;
            private TextView txtClientName;
            private TextView txthour;
            private TextView txtCreatedDate;
            private TextView txtEmployeeName;
            private SwipeRevealLayout swipeLayout;
            private FrameLayout layoutAction;
            private LinearLayout llEdit;
            private LinearLayout llDelete;
            private FrameLayout mainLayout;
            private LinearLayout llMain;

            ViewHolder(View convertView)
            {
                super(convertView);
                txtActivityName = (TextView) convertView.findViewById(R.id.txtActivityName);
                txtClientName = (TextView) convertView.findViewById(R.id.txtClientName);
                txthour = (TextView) convertView.findViewById(R.id.txthour);
                txtCreatedDate = (TextView) convertView.findViewById(R.id.txtCreatedDate);
                txtEmployeeName = (TextView) convertView.findViewById(R.id.txtEmployeeName);
                swipeLayout = (SwipeRevealLayout) convertView.findViewById(R.id.swipe_layout);
                layoutAction = (FrameLayout) convertView.findViewById(R.id.layout_action);
                llEdit = (LinearLayout) convertView.findViewById(R.id.llEdit);
                llDelete = (LinearLayout) convertView.findViewById(R.id.llDelete);
                mainLayout = (FrameLayout) convertView.findViewById(R.id.main_layout);
                llMain = (LinearLayout) convertView.findViewById(R.id.llMain);
            }
        }
    }

    private void setViewHeight(final FrameLayout mainLayout, final FrameLayout layoutAction)
    {
        ViewTreeObserver vto = mainLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
            @Override
            public void onGlobalLayout()
            {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
                {
                    mainLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                else
                {
                    mainLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                int width = mainLayout.getMeasuredWidth();
                int height = mainLayout.getMeasuredHeight();
                //  Log.e("<><> Hight ", String.valueOf(height) + " " + String.valueOf(position));

                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, height);
                layoutAction.setLayoutParams(lp);
            }
        });
    }

    public void selectDeleteDialog(final ActivityListGetSet activityListGetSet, final int pos)
    {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity, R.style.MaterialDialogSheetTemp);
        bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        View sheetView = activity.getLayoutInflater().inflate(R.layout.dialog_delete_activity, null);
        bottomSheetDialog.setContentView(sheetView);
        AppUtils.setLightStatusBarBottomDialog(bottomSheetDialog, activity);
        final LinearLayout llRemove, llCancel;
        llCancel = (LinearLayout) sheetView.findViewById(R.id.llCancel);
        llRemove = (LinearLayout) sheetView.findViewById(R.id.llRemove);


        llCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try
                {
                    bottomSheetDialog.dismiss();
                    bottomSheetDialog.cancel();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });


        llRemove.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try
                {
                    bottomSheetDialog.dismiss();
                    bottomSheetDialog.cancel();
                    removeActivity(activityListGetSet, pos);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });


        bottomSheetDialog.show();

    }

    private void removeActivity(final ActivityListGetSet activityListGetSet, final int pos)
    {
        new AsyncTask<Void, Void, Void>()
        {
            private boolean isSuccess = false;
            private String message = "";

            @Override
            protected void onPreExecute()
            {
                llLoading.setVisibility(View.VISIBLE);
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params)
            {
                try
                {
                    String response = "";

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("activity_id", String.valueOf(activityListGetSet.getId()));
                    hashMap.put("employee_id",  AppUtils.getEmployeeIdForAdmin(activity));
                    AppUtils.printLog(activity, "DELETE_ACTIVITY Request ", hashMap.toString());

                    response = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.DELETE_ACTIVITY, hashMap);

                    AppUtils.printLog(activity, "DELETE_ACTIVITY Response ", response.toString());

                    JSONObject jsonObject = new JSONObject(response);

                    isSuccess = AppUtils.getValidAPIBooleanResponseHas(jsonObject,"success");

                    message = AppUtils.getValidAPIStringResponseHas(jsonObject,"message");

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
                llLoading.setVisibility(View.GONE);

                try
                {
                    if (isSuccess)
                    {
                        listActivity.remove(pos);
                        activityListAdapter.notifyDataSetChanged();

                        if (listActivity.size() > 0)
                        {
                            llNoData.setVisibility(View.GONE);
                        }
                        else
                        {
                            llNoData.setVisibility(View.VISIBLE);
                        }
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                super.onPostExecute(result);

            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);
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

