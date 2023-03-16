package cheetah.alphacapital.reportApp.activity.admin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cheetah.alphacapital.reportApp.getset.EmployeeListResponse;
import cheetah.alphacapital.utils.AppAPIUtils;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import cheetah.alphacapital.R;
import cheetah.alphacapital.textutils.CustomTextViewSemiBold;

public class ManageEmployeeActivity extends BaseActivity
{
    private Toolbar toolbar;
    private LinearLayout llBackNavigation;
    private ImageView ivLogo;
    private ImageView ivIcon;
    private TextView txtTitle;
    private ImageView ivContactUs;
    private LinearLayout llNotification;
    private LinearLayout llLoading, llNoData;

    ProgressBar llMore;
    EditText edtSearch;
    ImageView ivSerach;
    ImageView ivClose;
    CardView cvCard;
    NestedScrollView nestedScrollView;

    private boolean isStatusBarHidden = false;
    private LinearLayout llNoInternet;
    private LinearLayout llRetry;

    private ImageView ivNoData, ivAddClient;
    private RecyclerView rvEmployeeList;
    private LinearLayoutManager linearLayoutManager;

    private Timer timer = new Timer();
    private final long DELAY = 400;
    private List<EmployeeListResponse.DataBean.AllEmployeeBean> listEmployee = new ArrayList<EmployeeListResponse.DataBean.AllEmployeeBean>();
    private List<EmployeeListResponse.DataBean.AllEmployeeBean> listEmployee_Search = new ArrayList<EmployeeListResponse.DataBean.AllEmployeeBean>();

    private EmployeeListAdapter employeeListAdapter;
    public static Handler handler;

    //paging
    int pageIndex = 1;
    boolean isLoading = false;
    boolean isLastPage = false;
    int pageResults = 25;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
       /* try
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
        }*/

        try
        {
            super.onCreate(savedInstanceState);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_manage_employee);

        setupViews();

        onClickEvents();

        try
        {
            if (sessionManager.isNetworkAvailable())
            {
                llNoInternet.setVisibility(View.GONE);
                getAllEmployee(true, "");
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
                            getAllEmployee(true, "");
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

            ivSerach = (ImageView) findViewById(R.id.ivSerach);
            ivClose = (ImageView) findViewById(R.id.ivClose);
            edtSearch = (EditText) findViewById(R.id.edtSearch);
            cvCard = findViewById(R.id.cvCard);
            edtSearch.setHint("Search Employee..");
            ivLogo.setVisibility(View.GONE);
            llNotification.setVisibility(View.GONE);
            txtTitle.setText("Manage Employee");
            llBackNavigation.setVisibility(View.VISIBLE);

            ImageView ivHeader = findViewById(R.id.ivHeader);
            //ivHeader.setImageResource(R.drawable.img_portfolio);

           /* int height = 56;
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
            ivHeader.setLayoutParams(params);*/

            ivLogo = (ImageView) findViewById(R.id.ivLogo);
            ivIcon = (ImageView) findViewById(R.id.ivIcon);
            txtTitle = (TextView) findViewById(R.id.txtTitle);
            ivContactUs = (ImageView) findViewById(R.id.ivContactUs);
            llNotification = (LinearLayout) findViewById(R.id.llNotification);
            llLoading = (LinearLayout) findViewById(R.id.llLoading);
            llNoInternet = (LinearLayout) findViewById(R.id.llNoInternet);
            llRetry = (LinearLayout) findViewById(R.id.llRetry);
            nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
            llNoData = findViewById(R.id.llNoData);
            llMore = findViewById(R.id.llMore);

            ivAddClient = (ImageView) findViewById(R.id.ivAddClient);
            rvEmployeeList = (RecyclerView) findViewById(R.id.rvEmployeeList);
            linearLayoutManager = new LinearLayoutManager(activity);
            rvEmployeeList.setLayoutManager(linearLayoutManager);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) ->
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
                                    if (listEmployee.size() > 0)
                                    {
                                        llMore.setVisibility(View.VISIBLE);
                                        getAllEmployee(false, "");
                                    }
                                }
                            }
                        }
                    }
                });
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


        ivAddClient.setOnClickListener(v -> {
            Intent intent = new Intent(activity, AddEmployeeActivity.class);
            intent.putExtra("isFor", "add");
            startActivity(intent);
        });

        ivSerach.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                doSearch();
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                doClose();
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3)
            {
                try
                {
                    cs = edtSearch.getText().toString().trim();
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
                                    activity.runOnUiThread(() ->
                                    {
                                        listEmployee_Search = new ArrayList<EmployeeListResponse.DataBean.AllEmployeeBean>();
                                        if (listEmployee != null && listEmployee.size() > 0)
                                        {
                                            getAllEmployee(true, edtSearch.getText().toString().trim());

                                            if (listEmployee_Search.size() > 0)
                                            {
                                                Log.e("<><> SEARCH Size: ", listEmployee_Search.size() + " END ");
                                                llNoData.setVisibility(View.GONE);
                                                employeeListAdapter = new EmployeeListAdapter(listEmployee_Search, activity);
                                                rvEmployeeList.setAdapter(employeeListAdapter);
                                            }
                                            else
                                            {
                                                llNoData.setVisibility(View.VISIBLE);
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
                        getAllEmployee(true, "");
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

    private void doClose()
    {
        llBackNavigation.setVisibility(View.VISIBLE);
        txtTitle.setVisibility(View.VISIBLE);
        edtSearch.setVisibility(View.GONE);
        cvCard.setVisibility(View.GONE);
        ivSerach.setVisibility(View.VISIBLE);
        ivClose.setVisibility(View.GONE);
        //MitsUtils.hideKeyboard(activity);
        edtSearch.setText("");
    }

    private void doSearch()
    {
        edtSearch.requestFocus();
        llBackNavigation.setVisibility(View.GONE);
        txtTitle.setVisibility(View.GONE);
        edtSearch.setVisibility(View.VISIBLE);
        cvCard.setVisibility(View.VISIBLE);
        ivSerach.setVisibility(View.GONE);
        ivClose.setVisibility(View.VISIBLE);
        openKeyboard(edtSearch);
    }

    void openKeyboard(View view)
    {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(view.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
    }


    private void getAllEmployee(boolean isFirstTime, String searchText)
    {
        if (isFirstTime)
        {
            llLoading.setVisibility(View.VISIBLE);
            pageIndex = 1;
            isLoading = false;
            isLastPage = false;
            pageResults = 25;
        }
        apiService.getAllEmployee(pageIndex, pageResults, searchText).enqueue(new Callback<EmployeeListResponse>()
        {
            @Override
            public void onResponse(Call<EmployeeListResponse> call, Response<EmployeeListResponse> response)
            {

                if (response.isSuccessful())
                {
                    if (response.body().isSuccess())
                    {
                        if (isFirstTime)
                        {
                            if (listEmployee.size() > 0)
                            {
                                listEmployee = new ArrayList<>();
                            }
                        }
                        ArrayList<EmployeeListResponse.DataBean.AllEmployeeBean> tempList = new ArrayList<>();
                        tempList.addAll(response.body().getData().getAllEmployee());
                        listEmployee.addAll(tempList);

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
                            llNoData.setVisibility(View.GONE);
                            if (listEmployee.size() > 0)
                            {
                                ivSerach.setVisibility(View.VISIBLE);
                                employeeListAdapter = new EmployeeListAdapter(listEmployee, activity);
                                rvEmployeeList.setAdapter(employeeListAdapter);
                                llNoData.setVisibility(View.GONE);
                            }
                            else
                            {
                                ivSerach.setVisibility(View.GONE);
                                llNoData.setVisibility(View.VISIBLE);
                            }
                        }
                        else
                        {
                            employeeListAdapter.notifyDataSetChanged();
                        }
                    }
                    else
                    {
                        showToast(response.body().getMessage());
                    }
                }
                else
                {
                    ivSerach.setVisibility(View.GONE);
                    llNoData.setVisibility(View.VISIBLE);
                }

                llMore.setVisibility(View.GONE);
                llLoading.setVisibility(View.GONE);
                llNoInternet.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<EmployeeListResponse> call, Throwable t)
            {
                ivSerach.setVisibility(View.GONE);
                llLoading.setVisibility(View.GONE);
                llNoData.setVisibility(View.VISIBLE);
                llNoInternet.setVisibility(View.GONE);
            }
        });

    }

    public class EmployeeListAdapter extends RecyclerView.Adapter<EmployeeListAdapter.ViewHolder>
    {
        List<EmployeeListResponse.DataBean.AllEmployeeBean> listItems;
        private Activity activity;
        private SessionManager sessionManager;
        private boolean isdone;

        public EmployeeListAdapter(List<EmployeeListResponse.DataBean.AllEmployeeBean> listEmployee, Activity activity)
        {
            this.listItems = listEmployee;
            this.activity = activity;
            sessionManager = new SessionManager(activity);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_manage_employee, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position)
        {
            try
            {
                final EmployeeListResponse.DataBean.AllEmployeeBean getSet = listItems.get(position);

                isdone = true;

                holder.tvFullName.setText(AppUtils.toDisplayCase(getSet.getFirst_name() + " " + getSet.getLast_name()));

                if (getSet.isIs_admin())
                {
                    holder.txtIsAdmin.setText("Yes");
                }
                else
                {
                    holder.txtIsAdmin.setText("No");
                }


                if (getSet.getEmail().length() > 0)
                {
                    holder.tvEmail.setText(getSet.getEmail());
                }
                else
                {
                    holder.tvEmail.setText("");
                }

                if (getSet.getContact_no().length() > 0)
                {
                    holder.tvPhone.setText(getSet.getContact_no());
                }
                else
                {
                    holder.tvPhone.setText("");
                }

                if (getSet.getAddress().length() > 0)
                {
                    holder.tvAddress.setText(getSet.getAddress());
                }
                else
                {
                    holder.tvAddress.setText("");
                }

                if (getSet.isIs_active())
                {
                    holder.tvStatus.setText("Active");
                    holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.green));
                }
                else
                {
                    holder.tvStatus.setText("Inactive");
                    holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.dash_text_red));
                }

                holder.ivEdit.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        try
                        {
                            Intent intent = new Intent(activity, AddEmployeeActivity.class);
                            intent.putExtra("isFor", "edit");
                            intent.putExtra("ClientInfo", (Parcelable) getSet);
                            startActivity(intent);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                });

                holder.ivView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        try
                        {
                            Intent intent = new Intent(activity, EmployeeTargetListActivity.class);
                            intent.putExtra("ClientInfo", (Parcelable) getSet);
                            startActivity(intent);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                    }
                });

                holder.ivDelete.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        try
                        {
                            selectDeleteDialog(getSet, position);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                });

                holder.ivOption.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        showOptionDialog(position);
                    }
                });

                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        Intent intent = new Intent(activity, EmployeeDetailsActivity.class);
                        intent.putExtra("employeeId", listItems.get(position).getId());
                        intent.putExtra("employeeName", listItems.get(position).getFirst_name() + " " + listItems.get(position).getLast_name());
                        intent.putExtra("pos", 0);
                        startActivity(intent);
                        startActivityAnimation();
                    }
                });

                isdone = false;
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
            private TextView tvFullName, tvEmail, tvPhone, tvAddress, tvStatus, txtIsAdmin;
            private ImageView ivEdit, ivDelete, ivView, ivOption;

            ViewHolder(View convertView)
            {
                super(convertView);
                tvFullName = (TextView) convertView.findViewById(R.id.tvFullName);
                tvEmail = (TextView) convertView.findViewById(R.id.tvEmail);
                tvPhone = (TextView) convertView.findViewById(R.id.tvPhone);
                tvAddress = (TextView) convertView.findViewById(R.id.tvAddress);
                tvStatus = (TextView) convertView.findViewById(R.id.tvStatus);
                txtIsAdmin = (TextView) convertView.findViewById(R.id.txtIsAdmin);
                ivEdit = (ImageView) convertView.findViewById(R.id.ivEdit);
                ivDelete = (ImageView) convertView.findViewById(R.id.ivDelete);
                ivView = (ImageView) convertView.findViewById(R.id.ivView);
                ivOption = convertView.findViewById(R.id.ivOption);
            }
        }
    }

    public void showOptionDialog(int position)
    {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity, R.style.BaseBottomSheetDialog);
        bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        View sheetView = activity.getLayoutInflater().inflate(R.layout.dialog_employeelist_option, null);
        bottomSheetDialog.setContentView(sheetView);

        TextView tvOverview, tvDARReport, tvAUMReport, tvAddTarget, tvEdit, tvDelete;
        tvOverview = sheetView.findViewById(R.id.tvOverview);
        tvDARReport = sheetView.findViewById(R.id.tvDARReport);
        tvAUMReport = sheetView.findViewById(R.id.tvAUMReport);
        tvAddTarget = sheetView.findViewById(R.id.tvAddTarget);
        tvEdit = sheetView.findViewById(R.id.tvEdit);
        tvDelete = sheetView.findViewById(R.id.tvDelete);

        tvOverview.setOnClickListener(view ->
        {
            bottomSheetDialog.dismiss();
            Intent intent = new Intent(activity, EmployeeDetailsActivity.class);
            intent.putExtra("employeeId", listEmployee.get(position).getId());
            intent.putExtra("employeeName", listEmployee.get(position).getFirst_name() + " " + listEmployee.get(position).getLast_name());
            intent.putExtra("pos", 0);
            startActivity(intent);
            startActivityAnimation();
        });

        tvDARReport.setOnClickListener(view ->
        {
            bottomSheetDialog.dismiss();
            Intent intent = new Intent(activity, EmployeeDetailsActivity.class);
            intent.putExtra("employeeId", listEmployee.get(position).getId());
            intent.putExtra("employeeName", listEmployee.get(position).getFirst_name() + " " + listEmployee.get(position).getLast_name());
            intent.putExtra("pos", 2);
            startActivity(intent);
            startActivityAnimation();

                  /*  Intent intent = new Intent(activity, AddDARNewActivity.class);
                    startActivity(intent);
                    startActivityAnimation();*/
        });

        tvAUMReport.setOnClickListener(view ->
        {
            bottomSheetDialog.dismiss();
            Intent intent = new Intent(activity, EmployeeDetailsActivity.class);
            intent.putExtra("employeeId", listEmployee.get(position).getId());
            intent.putExtra("employeeName", listEmployee.get(position).getFirst_name() + " " + listEmployee.get(position).getLast_name());
            intent.putExtra("pos", 3);
            startActivity(intent);
            startActivityAnimation();
        });

        tvAddTarget.setOnClickListener(view ->
        {
            bottomSheetDialog.dismiss();
            Intent intent = new Intent(activity, AddEmployeeTargetActivity.class);
            intent.putExtra("isFor", "add");
            intent.putExtra("employeeId", listEmployee.get(position).getId());
            startActivity(intent);
            startActivityAnimation();
        });

        tvEdit.setOnClickListener(view ->
        {
            bottomSheetDialog.dismiss();
            try
            {
                Intent intent = new Intent(activity, AddEmployeeActivity.class);
                intent.putExtra("isFor", "edit");
                intent.putExtra("ClientInfo", new Gson().toJson(listEmployee.get(position)));
                startActivity(intent);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });

        tvDelete.setOnClickListener(view ->
        {
            bottomSheetDialog.dismiss();
            selectDeleteDialog(listEmployee.get(position), position);
        });

        bottomSheetDialog.show();

    }

    public void selectDeleteDialog(final EmployeeListResponse.DataBean.AllEmployeeBean getSet, final int pos)
    {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity, R.style.MaterialDialogSheetTemp);
        bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        View sheetView = activity.getLayoutInflater().inflate(R.layout.dialog_delete_activity, null);
        bottomSheetDialog.setContentView(sheetView);
        AppUtils.setLightStatusBarBottomDialog(bottomSheetDialog, activity);
        final LinearLayout llRemove, llCancel;
        final TextView tvTitle;
        final TextView tvDescription;
        final TextView txtNo;
        final TextView txtYes;

        llCancel = (LinearLayout) sheetView.findViewById(R.id.llCancel);
        llRemove = (LinearLayout) sheetView.findViewById(R.id.llRemove);
        tvTitle = (TextView) sheetView.findViewById(R.id.tvTitle);
        tvDescription = (TextView) sheetView.findViewById(R.id.tvDescription);
        txtNo = (TextView) sheetView.findViewById(R.id.txtNo);
        txtYes = (TextView) sheetView.findViewById(R.id.txtYes);
        tvTitle.setText("Remove Employee");
        tvDescription.setText("Are you sure you want to remove this Employee?");


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
                    removeEmployee(getSet, pos);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });


        bottomSheetDialog.show();

    }

    private void removeEmployee(final EmployeeListResponse.DataBean.AllEmployeeBean getSet, final int pos)
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
                    hashMap.put("employeeid", String.valueOf(getSet.getId()));
                    AppUtils.printLog(activity, "DELETE_EMPLOYEE Request ", hashMap.toString());

                    response = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.DELETE_EMPLOYEE, hashMap);

                    AppUtils.printLog(activity, "DELETE_EMPLOYEE Response ", response.toString());

                    JSONObject jsonObject = new JSONObject(response);

                    isSuccess = AppUtils.getValidAPIBooleanResponseHas(jsonObject, "success");

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
                llLoading.setVisibility(View.GONE);

                try
                {
                    if (isSuccess)
                    {
                        listEmployee.remove(pos);
                        employeeListAdapter.notifyDataSetChanged();
                        if (listEmployee.size() > 0)
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
