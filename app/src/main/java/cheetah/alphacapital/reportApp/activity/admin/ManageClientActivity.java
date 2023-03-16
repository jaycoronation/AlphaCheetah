package cheetah.alphacapital.reportApp.activity.admin;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import cheetah.alphacapital.reportApp.activity.AddClientActivity;
import cheetah.alphacapital.reportApp.activity.ClientDetailsActivity;
import cheetah.alphacapital.reportApp.getset.ClientListResponse;
import cheetah.alphacapital.reportApp.getset.CommonResponse;
import cheetah.alphacapital.reportApp.getset.MonthYearResponse;
import cheetah.alphacapital.textutils.CustomEditText;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import cheetah.alphacapital.R;

public class ManageClientActivity extends BaseActivity
{
    @BindView(R.id.ivHeader)
    ImageView ivHeader;
    @BindView(R.id.viewStatusBar)
    View viewStatusBar;
    @BindView(R.id.emptyView)
    View emptyView;
    @BindView(R.id.llBackNavigation)
    LinearLayout llBackNavigation;
    @BindView(R.id.ivLogo)
    ImageView ivLogo;
    @BindView(R.id.ivIcon)
    ImageView ivIcon;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.ivContactUs)
    ImageView ivContactUs;
    @BindView(R.id.llNotification)
    LinearLayout llNotification;
    @BindView(R.id.ivChangePassword)
    ImageView ivChangePassword;
    @BindView(R.id.ivLogout)
    ImageView ivLogout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.pbLoading)
    ProgressBar pbLoading;
    @BindView(R.id.txtLoading)
    TextView txtLoading;
    @BindView(R.id.llLoading)
    LinearLayout llLoading;
    @BindView(R.id.txtRetry)
    TextView txtRetry;
    @BindView(R.id.llRetry)
    LinearLayout llRetry;
    @BindView(R.id.llNoInternet)
    LinearLayout llNoInternet;
    @BindView(R.id.rvClientList)
    RecyclerView rvClientList;
    @BindView(R.id.ivNoData)
    ImageView ivNoData;
    @BindView(R.id.tvNoDataText)
    TextView tvNoDataText;
    @BindView(R.id.llNoData)
    LinearLayout llNoData;
    @BindView(R.id.ivAddClient)
    ImageView ivAddClient;
    @BindView(R.id.edtSearch)
    CustomEditText edtSearch;
    @BindView(R.id.cvCard)
    CardView cvCard;
    @BindView(R.id.ivSerach)
    ImageView ivSerach;
    @BindView(R.id.ivClose)
    ImageView ivClose;
    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;

    private LinearLayoutManager linearLayoutManager;
    private boolean isStatusBarHidden = false;

    private ArrayList<ClientListResponse.DataBean> listClient = new ArrayList<ClientListResponse.DataBean>();
    private ArrayList<ClientListResponse.DataBean> listClientSearch = new ArrayList<ClientListResponse.DataBean>();
    private ClientListAdapter clientListAdapter;
    private BottomSheetDialog listDialog;
    private final String MONTH = "Month";
    private final String YEAR = "Year";
    @BindView(R.id.tvMonth)
    TextView tvMonth;
    @BindView(R.id.llSelectMonth)
    LinearLayout llSelectMonth;
    @BindView(R.id.tvYear)
    TextView tvYear;
    @BindView(R.id.llSelectYear)
    LinearLayout llSelectYear;
    @BindView(R.id.llMonthYear)
    LinearLayout llMonthYear;
    @BindView(R.id.llMore)
    ProgressBar llMore;
    private String selectedYear = "", selectedMonth = "", selectedMonthName = "";
    private ArrayList<String> listYear = new ArrayList<>();
    private ArrayList<MonthYearResponse.DataBean.MonthBean> listMonth = new ArrayList<MonthYearResponse.DataBean.MonthBean>();

    public static Handler handler;
    private Timer timer = new Timer();
    private final long DELAY = 400;

    //paging
    int pageIndex = 1;
    boolean isLoading = false;
    boolean isLastPage = false;
    int pageResults = 25;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
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

        setContentView(R.layout.activity_manage_cleint);

        ButterKnife.bind(this);

        selectedYear = getIntent().getExtras().getString("selectedYear");

        selectedMonth = getIntent().getExtras().getString("selectedMonth");

        selectedMonthName = getIntent().getExtras().getString("selectedMonthName");

        setupViews();

        onClickEvents();

        try
        {
            if (sessionManager.isNetworkAvailable())
            {
                getMonthYearData();
                llNoInternet.setVisibility(View.GONE);
                getAllAssignedClientByEmployee(true, "");
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

        handler = new Handler(msg ->
        {
            try
            {
                if (msg.what == 100)
                {
                    if (sessionManager.isNetworkAvailable())
                    {
                        llNoInternet.setVisibility(View.GONE);
                        getAllAssignedClientByEmployee(true, "");
                    }
                    else
                    {
                        llNoInternet.setVisibility(View.VISIBLE);
                    }
                }

                if (msg.what == 101)
                {
                    try
                    {
                        ClientListResponse.DataBean dataBean = (ClientListResponse.DataBean) msg.obj;
                        for (int i = 0; i < listClient.size(); i++)
                        {
                            if (dataBean.getId() == listClient.get(i).getId())
                            {
                                listClient.set(i, dataBean);
                                clientListAdapter.notifyItemChanged(i);
                                linearLayoutManager.scrollToPositionWithOffset(i, 20);
                                break;
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return false;
        });
    }

    private void setupViews()
    {
        try
        {
            ivLogo.setVisibility(View.GONE);
            llNotification.setVisibility(View.GONE);
            txtTitle.setText("Client List");
            tvNoDataText.setText("No Client List Found.");
            llBackNavigation.setVisibility(View.VISIBLE);

            linearLayoutManager = new LinearLayoutManager(activity);
            rvClientList.setLayoutManager(linearLayoutManager);
            tvMonth.setText(selectedMonthName);
            tvYear.setText(selectedYear);

            if (sessionManager.isAdmin())
            {
                llMonthYear.setVisibility(View.GONE);
            }
            else
            {
                llMonthYear.setVisibility(View.VISIBLE);
            }

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
                                    if (listClient.size() > 0)
                                    {
                                        llMore.setVisibility(View.VISIBLE);
                                        getAllAssignedClientByEmployee(false, "");
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

        ivAddClient.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(activity, AddClientActivity.class);
                intent.putExtra("isFor", "add");
                intent.putExtra("ClientInfo", (Parcelable) new ClientListResponse.DataBean());
                startActivity(intent);
            }
        });

        llSelectMonth.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showListDialog(MONTH);
            }
        });

        llSelectYear.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showListDialog(YEAR);
            }
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
                                        listClientSearch = new ArrayList<ClientListResponse.DataBean>();
                                        if (listClient != null && listClient.size() > 0)
                                        {
                                           /* for (int i = 0; i < listClient.size(); i++)
                                            {
                                                final String text = listClient.get(i).getFirst_name() + " " + listClient.get(i).getLast_name();

                                                String text1 = AppUtils.getCapitalText(text);

                                                String cs1 = AppUtils.getCapitalText(String.valueOf(finalCs));

                                                if (text1.contains(cs1))
                                                {
                                                    listClientSearch.add(listClient.get(i));
                                                }
                                            }*/

                                            getAllAssignedClientByEmployee(true,edtSearch.getText().toString().trim());

                                            if (listClientSearch.size() > 0)
                                            {
                                                Log.e("<><> SEARCH Size: ", listClientSearch.size() + " END ");
                                                llNoData.setVisibility(View.GONE);
                                                clientListAdapter = new ClientListAdapter(listClientSearch);
                                                rvClientList.setAdapter(clientListAdapter);
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

                        getAllAssignedClientByEmployee(true,"");

                        if (listClient.size() > 0)
                        {
                            clientListAdapter = new ClientListAdapter(listClient);
                            rvClientList.setAdapter(clientListAdapter);
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
        hideKeyboard();
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
        //MitsUtils.openKeyboard(edtSearch, activity);
        openKeyboard(edtSearch);
    }

    private void getMonthYearData()
    {
        Call<MonthYearResponse> call = apiService.getMonthYear();
        call.enqueue(new Callback<MonthYearResponse>()
        {
            @Override
            public void onResponse(Call<MonthYearResponse> call, Response<MonthYearResponse> response)
            {
                if (response.isSuccessful())
                {
                    if (response.body().isSuccess())
                    {
                        listYear = new ArrayList<>();
                        listMonth = new ArrayList<MonthYearResponse.DataBean.MonthBean>();
                        listYear.addAll(response.body().getData().getYear());
                        listMonth.addAll(response.body().getData().getMonth());
                    }
                    else
                    {
                        apiFailedSnackBar();
                    }
                }
                else
                {
                    apiFailedSnackBar();
                }
            }

            @Override
            public void onFailure(Call<MonthYearResponse> call, Throwable t)
            {
                apiFailedSnackBar();
            }
        });
    }

    private void getAllAssignedClientByEmployee(boolean isFirstTime, String searchText)
    {
        if (isFirstTime)
        {
            llLoading.setVisibility(View.VISIBLE);
            listClient = new ArrayList<ClientListResponse.DataBean>();
            isLoading = false;
            pageIndex = 1;
            isLastPage = false;
        }
        Call<ClientListResponse> call = null;
        if (sessionManager.isAdmin())
        {
            call = apiService.getAllClientForAdmin(pageIndex, pageResults,searchText);
        }
        else
        {
            call = apiService.getAllClientForEmployee(pageIndex, pageResults, selectedMonth, selectedYear, AppUtils.getEmployeeIdForAdmin(activity));
        }

        call.enqueue(new Callback<ClientListResponse>()
        {
            @Override
            public void onResponse(Call<ClientListResponse> call, Response<ClientListResponse> response)
            {
                if (response.isSuccessful())
                {
                    if (response.body().isSuccess())
                    {
                        if (isFirstTime)
                        {
                            if (listClient.size() > 0)
                            {
                                listClient = new ArrayList<>();
                            }
                        }
                        ArrayList<ClientListResponse.DataBean> tempList = new ArrayList<ClientListResponse.DataBean>();
                        tempList.addAll(response.body().getData());
                        listClient.addAll(tempList);

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
                            if (listClient.size() > 0)
                            {
                                ivSerach.setVisibility(View.VISIBLE);
                                clientListAdapter = new ClientListAdapter(listClient);
                                rvClientList.setAdapter(clientListAdapter);
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
                            clientListAdapter.notifyDataSetChanged();
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
                    apiFailedSnackBar();
                }
                llMore.setVisibility(View.GONE);
                llLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ClientListResponse> call, Throwable t)
            {
                llLoading.setVisibility(View.GONE);
                apiFailedSnackBar();
                llNoData.setVisibility(View.VISIBLE);
                llMore.setVisibility(View.GONE);
            }
        });
    }

    public class ClientListAdapter extends RecyclerView.Adapter<ClientListAdapter.ViewHolder>
    {
        ArrayList<ClientListResponse.DataBean> listItems;
        private boolean isdone;

        public ClientListAdapter(ArrayList<ClientListResponse.DataBean> listClient)
        {
            this.listItems = listClient;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_manage_cleint, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position)
        {
            try
            {
                final ClientListResponse.DataBean getSet = listItems.get(position);
                isdone = true;
                holder.tvFullName.setText(AppUtils.toDisplayCase(getSet.getFirst_name() + " " + getSet.getLast_name()));

                if (getSet.isIs_active())
                {
                    holder.tvStatus.setText("Active");
                    holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.green));
                }

                if (!getSet.isIs_active())
                {
                    holder.tvStatus.setText("Inactive");
                    holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.dash_text_red));
                }

                holder.tvPhone.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        //callIntent(getSet.getContact_no());
                    }
                });

                holder.ivOption.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        AppUtils.hideKeyboard(edtSearch, activity);
                        showOptionDialog(getSet, position);
                    }
                });

                holder.llMain.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        try
                        {
                            Intent intent = new Intent(activity, ClientDetailsActivity.class);
                            intent.putExtra("clientid", String.valueOf(getSet.getId()));
                            intent.putExtra("ClientInfo", (Parcelable) getSet);
                            activity.startActivity(intent);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                });

                if (getSet.getAddress().length() > 0)
                {
                    holder.tvAddress.setText(getSet.getAddress());
                }
                else
                {
                    holder.tvAddress.setText("");
                }

                if (getSet.getEmail() != null)
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
            private TextView tvFullName, tvEmail, tvPhone, tvAddress, tvStatus;
            private ImageView ivOption;
            private LinearLayout llMain;

            ViewHolder(View convertView)
            {
                super(convertView);
                tvFullName = (TextView) convertView.findViewById(R.id.tvFullName);
                tvEmail = (TextView) convertView.findViewById(R.id.tvEmail);
                tvPhone = (TextView) convertView.findViewById(R.id.tvPhone);
                tvAddress = (TextView) convertView.findViewById(R.id.tvAddress);
                tvStatus = (TextView) convertView.findViewById(R.id.tvStatus);
                ivOption = (ImageView) convertView.findViewById(R.id.ivOption);
                llMain = (LinearLayout) convertView.findViewById(R.id.llMain);
            }
        }
    }

    public void showOptionDialog(ClientListResponse.DataBean getSet, int position)
    {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity, R.style.BaseBottomSheetDialog);
        View sheetView = activity.getLayoutInflater().inflate(R.layout.dialog_client_list_option, null);
        bottomSheetDialog.setContentView(sheetView);
        TextView tvEdit, tvDelete;
        Switch switchApprove;

        tvEdit = sheetView.findViewById(R.id.tvEdit);
        tvDelete = sheetView.findViewById(R.id.tvDelete);
        switchApprove = (Switch) sheetView.findViewById(R.id.switchApprove);

        switchApprove.setChecked(getSet.isIs_approved_by_admin());

        switchApprove.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                try
                {
                    bottomSheetDialog.dismiss();
                    approveOrNotApprove(getSet, position);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        tvEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                bottomSheetDialog.dismiss();
                try
                {
                    Intent intent = new Intent(activity, AddClientActivity.class);
                    intent.putExtra("isFor", "edit");
                    intent.putExtra("ClientInfo", (Parcelable) getSet);
                    startActivity(intent);
                    doClose();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        tvDelete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                bottomSheetDialog.dismiss();

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

        bottomSheetDialog.show();
    }

    public void selectDeleteDialog(final ClientListResponse.DataBean getSet, final int pos)
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
        tvTitle.setText("Remove Client");
        tvDescription.setText("Are you sure you want to remove this Client?");


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
                    removeClient(getSet, pos);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });


        bottomSheetDialog.show();

    }

    private void removeClient(final ClientListResponse.DataBean getSet, final int pos)
    {
        llLoading.setVisibility(View.VISIBLE);
        Call<CommonResponse> call = apiService.deleteClient(String.valueOf(getSet.getId()));

        call.enqueue(new Callback<CommonResponse>()
        {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response)
            {
                if (response.isSuccessful())
                {
                    if (response.body().isSuccess())
                    {
                        listClient.remove(pos);
                        clientListAdapter.notifyDataSetChanged();
                        if (listClient.size() > 0)
                        {
                            llNoData.setVisibility(View.GONE);
                        }
                        else
                        {
                            llNoData.setVisibility(View.VISIBLE);
                        }
                    }
                    else
                    {
                        apiFailedSnackBar();
                    }
                }
                else
                {
                    apiFailedSnackBar();
                }
                llLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t)
            {
                llLoading.setVisibility(View.GONE);
                apiFailedSnackBar();
            }
        });
    }

    private void approveOrNotApprove(final ClientListResponse.DataBean getSet, final int position)
    {
        String status = "";
        if (getSet.isIs_approved_by_admin())
        {
            status = String.valueOf(false);
        }
        else
        {
            status = String.valueOf(true);
        }

        Call<CommonResponse> call = apiService.ApproveClientByEmployee(String.valueOf(getSet.getId()), status);

        call.enqueue(new Callback<CommonResponse>()
        {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response)
            {
                if (response.isSuccessful())
                {
                    if (response.body().isSuccess())
                    {
                        if (getSet.isIs_approved_by_admin())
                        {
                            getSet.setIs_approved_by_admin(false);

                        }
                        else
                        {
                            getSet.setIs_approved_by_admin(true);
                        }

                        clientListAdapter.notifyItemChanged(position);
                    }
                    else
                    {
                        apiFailedSnackBar();
                    }
                }
                else
                {
                    apiFailedSnackBar();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t)
            {
                apiFailedSnackBar();
            }
        });


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
            @Override
            public void onClick(View v)
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
            @Override
            public void onClick(View v)
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

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowview_common_list, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position)
        {
            if (position == getItemCount() - 1)
            {
                holder.viewLine.setVisibility(View.GONE);
            }
            else
            {
                holder.viewLine.setVisibility(View.VISIBLE);
            }

            if (isFor.equals(MONTH))
            {
                final MonthYearResponse.DataBean.MonthBean getSet = listMonth.get(position);
                holder.tvValue.setText(getSet.getName());
                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        listDialog.dismiss();
                        if (!selectedMonth.equalsIgnoreCase(String.valueOf(getSet.getId())))
                        {
                            tvMonth.setText(getSet.getName());
                            selectedMonth = String.valueOf(getSet.getId());
                            if (sessionManager.isNetworkAvailable())
                            {
                                llNoInternet.setVisibility(View.GONE);
                                getAllAssignedClientByEmployee(false, "");
                            }
                            else
                            {
                                llNoInternet.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
            }
            else if (isFor.equalsIgnoreCase(YEAR))
            {
                holder.tvValue.setText(listYear.get(position).toString());
                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        listDialog.dismiss();
                        if (!selectedYear.equalsIgnoreCase(listYear.get(position).toString()))
                        {
                            if (sessionManager.isNetworkAvailable())
                            {
                                selectedYear = listYear.get(position).toString();
                                tvYear.setText(selectedYear);
                                if (sessionManager.isNetworkAvailable())
                                {
                                    llNoInternet.setVisibility(View.GONE);
                                    getAllAssignedClientByEmployee(false, "");
                                }
                                else
                                {
                                    llNoInternet.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }
                });
            }
        }

        @Override
        public int getItemCount()
        {
            if (isFor.equalsIgnoreCase(MONTH))
            {
                return listMonth.size();
            }
            else
            {
                return listYear.size();
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
