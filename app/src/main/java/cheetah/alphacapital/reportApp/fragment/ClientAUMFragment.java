package cheetah.alphacapital.reportApp.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cheetah.alphacapital.network.ApiClient;
import cheetah.alphacapital.network.ApiInterface;
import cheetah.alphacapital.utils.AppAPIUtils;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import cheetah.alphacapital.R;
import cheetah.alphacapital.reportApp.activity.AddAUMActivity;
import cheetah.alphacapital.reportApp.getset.AUMListGetSet;
import cheetah.alphacapital.reportApp.getset.MonthYearResponse;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClientAUMFragment extends Fragment
{
    private Activity activity;
    private SessionManager sessionManager;
    private View rootView;
    public ApiInterface apiService;
    @BindView(R.id.llLoading)
    LinearLayout llLoading;
    @BindView(R.id.llRetry)
    LinearLayout llRetry;
    @BindView(R.id.llNoInternet)
    LinearLayout llNoInternet;
    @BindView(R.id.tvMonth)
    TextView tvMonth;
    @BindView(R.id.llSelectMonth)
    LinearLayout llSelectMonth;
    @BindView(R.id.tvYear)
    TextView tvYear;
    @BindView(R.id.llSelectYear)
    LinearLayout llSelectYear;
    @BindView(R.id.ivNoData)
    ImageView ivNoData;
    @BindView(R.id.tvNoDataText)
    TextView tvNoDataText;
    @BindView(R.id.llNoData)
    LinearLayout llNoData;
    @BindView(R.id.rvListAum)
    RecyclerView rvListAum;
    @BindView(R.id.ivAddAum)
    ImageView ivAddAum;
    public  List<AUMListGetSet.DataBean> listAum = new ArrayList<AUMListGetSet.DataBean>();
    private AumAdapter aumAdapter;
    private BottomSheetDialog listDialog;
    private List<String> listYear = new ArrayList<>();
    private ArrayList<MonthYearResponse.DataBean.MonthBean> listMonth = new ArrayList<MonthYearResponse.DataBean.MonthBean>();
    private final String MONTH = "Month";
    private final String YEAR = "Year";
    private String selectedYear = "", selectedMonth = "0";
    private String clientId;
    public static Handler handler;

    public ClientAUMFragment()
    {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_client_aum, container, false);
        activity = getActivity();
        ButterKnife.bind(this, rootView);
        sessionManager = new SessionManager(activity);
        apiService = ApiClient.getClient().create(ApiInterface.class);

        clientId = getArguments().getString("clientid");
        setupViews(rootView);
        onClicks();

        handler = new Handler(new Handler.Callback()
        {
            @Override
            public boolean handleMessage(Message msg)
            {
                if (msg.what == 111)
                {
                    getAumDetails();
                }
                return false;
            }
        });
        return rootView;
    }

    private void setupViews(View rootView)
    {
        rvListAum.setLayoutManager(new LinearLayoutManager(activity));
        tvNoDataText.setText("No Aum Data Found.");
        selectedMonth = AppUtils.getCurrentMonth();
        selectedYear = AppUtils.getCurrentYear();

        if (sessionManager.isNetworkAvailable())
        {
            getMonthYearData();
            getAumDetails();
            llNoInternet.setVisibility(View.GONE);
        }
        else
        {
            llNoInternet.setVisibility(View.VISIBLE);
        }

        Log.i("***********", "setupViews: " + selectedMonth + "    " + selectedYear);
    }

    private void onClicks()
    {
        ivAddAum.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(activity, AddAUMActivity.class);
                intent.putExtra("clientId", clientId);
                intent.putExtra("isFor", "add");
                intent.putExtra("AUMListGetSet", (Parcelable) new AUMListGetSet.DataBean());
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
    }

    private void getAumDetails()
    {
        llLoading.setVisibility(View.VISIBLE);
        Call<AUMListGetSet> call = apiService.getAllAumDetailByEmaployee(
                "0",
                "0",
                "0",
                selectedYear,
                "0",
                clientId); ////emp id 0 for gettting all list
        call.enqueue(new Callback<AUMListGetSet>()
        {
            @Override
            public void onResponse(Call<AUMListGetSet> call, Response<AUMListGetSet> response)
            {
                if (response.isSuccessful())
                {
                    if (response.body().isSuccess())
                    {
                        listAum = new ArrayList<AUMListGetSet.DataBean>();
                        listAum.addAll(response.body().getData());

                        if (listAum.size() > 0)
                        {
                            llNoData.setVisibility(View.GONE);
                            aumAdapter = new AumAdapter(listAum);
                            rvListAum.setAdapter(aumAdapter);
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
            public void onFailure(Call<AUMListGetSet> call, Throwable t)
            {
                llLoading.setVisibility(View.GONE);
                llNoData.setVisibility(View.VISIBLE);
                AppUtils.apiFailedSnackBar(activity);
            }
        });
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
                        if (response.body().getData().getYear().size() > 0)
                        {
                            listYear = new ArrayList<String>();

                            for (int i = 0; i < response.body().getData().getYear().size(); i++)
                            {
                                listYear.add(response.body().getData().getYear().get(i).toString().trim());
                                if (response.body().getData().getYear().get(i).equals(selectedYear))
                                {
                                    tvYear.setText(listYear.get(i).toString());
                                    break;
                                }
                            }
                        }

                        if (response.body().getData().getMonth().size() > 0)
                        {
                            listMonth = new ArrayList<MonthYearResponse.DataBean.MonthBean>();

                            for (int i = 0; i < response.body().getData().getMonth().size(); i++)
                            {
                                listMonth.add(response.body().getData().getMonth().get(i));
                                if (response.body().getData().getMonth().get(i).getId() == Integer.parseInt(selectedMonth))
                                {
                                    tvMonth.setText(listMonth.get(i).getName());
                                    break;
                                }
                            }
                        }
                    }
                    else
                    {
                        AppUtils.apiFailedSnackBar(activity);
                    }
                }
                else
                {
                    AppUtils.apiFailedSnackBar(activity);
                }
            }

            @Override
            public void onFailure(Call<MonthYearResponse> call, Throwable t)
            {
                AppUtils.apiFailedSnackBar(activity);
            }
        });
    }

    public class AumAdapter extends RecyclerView.Adapter<AumAdapter.ViewHolder>
    {
        List<AUMListGetSet.DataBean> listItems;


        public AumAdapter(List<AUMListGetSet.DataBean> listClient)
        {
            this.listItems = listClient;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_aum_list, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position)
        {
            try
            {
                final AUMListGetSet.DataBean getSet = listItems.get(position);
              //  holder.tvMonthEndAum.setText(AppUtils.getFormattedAmount(activity, (long) getSet.getMonth_End_AUM()));
                holder.tvMonthEndAum.setText(AppUtils.convertToDecimalValueForListing(String.valueOf(getSet.getMonth_End_AUM())));
                holder.tvInflowOutflow.setText(AppUtils.convertToDecimalValueForListing(String.valueOf(getSet.getInflow_outflow())));
                holder.tvMeetingNew.setText(String.valueOf(getSet.getNew_Meeting()));
                holder.tvMeetingExisting.setText(String.valueOf(getSet.getExisting_Meeting()));
                holder.tvSip.setText(AppUtils.convertToDecimalValueForListing(String.valueOf(getSet.getSIP())));
                holder.tvClientReference.setText(String.valueOf(getSet.getClientReferences()));
                holder.tvSummaryMail.setText(String.valueOf(getSet.getSummery_mail()));
                holder.tvDayForwardMail.setText(String.valueOf(getSet.getDay_forward_mail()));
                holder.tvNewClientConverted.setText(String.valueOf(getSet.getNewClientConverted()));
                holder.tvSelfAcquiredAUM.setText(AppUtils.convertToDecimalValueForListing(String.valueOf(getSet.getSelfAcquiredAUM())));
                holder.tvMonth.setText(AppUtils.getMonthName(getSet.getAUM_Month()));
                holder.tvDAR.setText(String.valueOf(getSet.getDAR()));
                holder.tvEmployee.setText(getSet.getEmp_f_name() + " " + getSet.getEmp_l_name());

                //holder.tvCreatedDate.setText(String.valueOf(getSet.getCreated_date()));
                //holder.tvCreatedDate.setText(AppUtils.universalDateConvert(String.valueOf(getSet.getCreated_date()), "yyyy-MM-dd'T'HH:mm:ss.SSS", "dd MMM yyyy hh:mm a"));


                if (getSet.getEmployee_id() == Integer.parseInt(sessionManager.getUserId()))
                {
                    holder.ivEdit.setVisibility(View.VISIBLE);
                }
                else
                {
                    holder.ivEdit.setVisibility(View.GONE);
                }

                holder.ivEdit.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        try
                        {
                            Intent intent = new Intent(activity, AddAUMActivity.class);
                            intent.putExtra("clientId", clientId);
                            intent.putExtra("isFor", "edit");
                            intent.putExtra("AUMListGetSet", (Parcelable) getSet);
                            activity.startActivity(intent);
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

        @Override
        public int getItemCount()
        {
            return listItems.size();
        }

        @SuppressWarnings("unused")
        public class ViewHolder extends RecyclerView.ViewHolder
        {
            @BindView(R.id.tvMonthEndAum)
            TextView tvMonthEndAum;
            @BindView(R.id.tvInflowOutflow)
            TextView tvInflowOutflow;
            @BindView(R.id.tvMeeting_New)
            TextView tvMeetingNew;
            @BindView(R.id.tvMeeting_Existing)
            TextView tvMeetingExisting;
            @BindView(R.id.tvSip)
            TextView tvSip;
            @BindView(R.id.tvClientReference)
            TextView tvClientReference;
            @BindView(R.id.tvSummaryMail)
            TextView tvSummaryMail;
            @BindView(R.id.tvDayForwardMail)
            TextView tvDayForwardMail;
            @BindView(R.id.tvNewClientConverted)
            TextView tvNewClientConverted;
            @BindView(R.id.tvSelfAcquiredAUM)
            TextView tvSelfAcquiredAUM;
            @BindView(R.id.tvMonth)
            TextView tvMonth;
            @BindView(R.id.tvDAR)
            TextView tvDAR;
            @BindView(R.id.tvEmployee)
            TextView tvEmployee;
            @BindView(R.id.ivEdit)
            ImageView ivEdit;

            ViewHolder(View convertView)
            {
                super(convertView);
                ButterKnife.bind(this, convertView);
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
                                getAumDetails();
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
                                getAumDetails();
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

    public void selectDeleteDialog(final AUMListGetSet.DataBean getSet, final int pos)
    {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity, R.style.MaterialDialogSheetTemp);
        bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        View sheetView = activity.getLayoutInflater().inflate(R.layout.dialog_delete_activity, null);
        bottomSheetDialog.setContentView(sheetView);
        AppUtils.setLightStatusBarBottomDialog(bottomSheetDialog, activity);
        final LinearLayout llRemove, llCancel;
        llCancel = (LinearLayout) sheetView.findViewById(R.id.llCancel);
        llRemove = (LinearLayout) sheetView.findViewById(R.id.llRemove);

        TextView tvTitle = sheetView.findViewById(R.id.tvTitle);
        TextView tvDescription = sheetView.findViewById(R.id.tvDescription);

        tvTitle.setText("Remove AUM");
        tvDescription.setText("Are you sure want to remove this AUM?");

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
                    removeAmu(getSet, pos);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });


        bottomSheetDialog.show();

    }

    private void removeAmu(final AUMListGetSet.DataBean getSet, final int pos)
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
                    hashMap.put("activity_id", String.valueOf(getSet.getId()));
                    hashMap.put("employee_id", AppUtils.getEmployeeIdForAdmin(activity));
                    AppUtils.printLog(activity, "DELETE_ACTIVITY Request ", hashMap.toString());

                    response = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.DELETE_ACTIVITY, hashMap);

                    AppUtils.printLog(activity, "DELETE_ACTIVITY Response ", response.toString());

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
                        /*listActivity.remove(pos);
                        activityListAdapter.notifyDataSetChanged();

                        if (listActivity.size() > 0)
                        {
                            llNoData.setVisibility(View.GONE);
                        }
                        else
                        {
                            llNoData.setVisibility(View.VISIBLE);
                        }*/
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
    public void onDestroy()
    {
        super.onDestroy();

    }
}
