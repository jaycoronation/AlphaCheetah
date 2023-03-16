package cheetah.alphacapital.reportApp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cheetah.alphacapital.textutils.CustomTextViewSemiBold;
import cheetah.alphacapital.utils.AppAPIUtils;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.utils.SessionManager;
import cheetah.alphacapital.R;
import cheetah.alphacapital.reportApp.getset.SelfAcquiredAUMGetSet;

public class SelfAcquiredListActivity extends AppCompatActivity
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
    private LinearLayout llLoading;

    private LinearLayout llNoInternet,llNoData,llRetry;
    private ImageView ivAddAum;
    private RecyclerView rvListAum;

    private boolean isStatusBarHidden = false;

    public static List<SelfAcquiredAUMGetSet> listAum = new ArrayList<>();

    private AumAdapter aumAdapter;

    private BottomSheetDialog listDialog;

    private List<String> listYear = new ArrayList<>();

    private LinearLayout llSelectYear;
    private TextView tvYear;

    private final String YEAR = "Year";

    private String selectedYear = "";

    public static Handler handler;

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

        activity = this;

        sessionManager = new SessionManager(activity);

        setContentView(R.layout.activity_self_acquired_list);

        setupViews();

        onClickEvents();

        if (sessionManager.isNetworkAvailable())
        {
            llNoInternet.setVisibility(View.GONE);
            getAumList(true);
        }
        else
        {
            llNoInternet.setVisibility(View.VISIBLE);
        }

        handler = new Handler(msg ->
        {
            if(msg.what==111)
            {
                getAumList(true);
            }
            return false;
        });
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
            public void onClick(View v)
            {
                getAumList(true);
            }
        });

        ivAddAum.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(activity, AddSelfAcquiredAumActivity.class);
                intent.putExtra("isFor","add");
                intent.putExtra("position",0);
                startActivity(intent);
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
            txtTitle.setText("Self Acquired AUM");

            llBackNavigation.setVisibility(View.VISIBLE);

            /*ImageView ivHeader = findViewById(R.id.ivHeader);
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

            ivLogo = (ImageView) findViewById(R.id.ivLogo);
            ivIcon = (ImageView) findViewById(R.id.ivIcon);
            txtTitle = (TextView) findViewById(R.id.txtTitle);
            ivContactUs = (ImageView) findViewById(R.id.ivContactUs);
            llNotification = (LinearLayout) findViewById(R.id.llNotification);
            llLoading = (LinearLayout) findViewById(R.id.llLoading);
            llNoData = (LinearLayout) findViewById(R.id.llNoData);
            llNoInternet = (LinearLayout) findViewById(R.id.llNoInternet);
            llRetry = (LinearLayout) findViewById(R.id.llRetry);

            setSupportActionBar(toolbar);

            ivAddAum = findViewById(R.id.ivAddAum);

            rvListAum = findViewById(R.id.rvListAum);
            rvListAum.setLayoutManager(new LinearLayoutManager(activity));

            llSelectYear = findViewById(R.id.llSelectYear);

            tvYear = findViewById(R.id.tvYear);

            selectedYear = AppUtils.getCurrentYear();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void getAumList(final boolean isFroMonthYear)
    {
        if(sessionManager.isNetworkAvailable())
        {
            llNoInternet.setVisibility(View.GONE);
            new AsyncTask<Void,Void,Void>()
            {
                boolean success = false;

                @Override
                protected void onPreExecute()
                {
                    super.onPreExecute();
                    llLoading.setVisibility(View.VISIBLE);
                }

                @Override
                protected Void doInBackground(Void... voids)
                {
                    getAUMDetails();
                    if(isFroMonthYear)
                    {
                        getMonthYearData();
                    }
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
                                if(yearArray.length()>0)
                                {
                                    listYear = new ArrayList<>();
                                    for (int i = 0; i < yearArray.length(); i++)
                                    {
                                        listYear.add(yearArray.getString(i));
                                        if(yearArray.get(i).equals(selectedYear))
                                        {
                                            break;
                                        }
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

                private void getAUMDetails()
                {
                    try
                    {
                        HashMap<String,String> hashMap = new HashMap<>();
                        hashMap.put("Year",selectedYear);
                        hashMap.put("employee_id",AppUtils.getEmployeeIdForAdmin(activity));

                        Log.i("Acquired AUM List Req : ", "doInBackground: " + hashMap.toString());
                        String serverResponse = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.GET_SELF_ACQUIRED_ALUM_LIST,hashMap);
                        Log.i("Acquired AUM List Res : ", "doInBackground: " + serverResponse);
                        if(serverResponse!=null)
                        {
                            JSONObject jsonObject = new JSONObject(serverResponse);
                            if(jsonObject!=null)
                            {
                                success = AppUtils.getValidAPIBooleanResponseHas(jsonObject,"success");
                                if(success)
                                {
                                    if(jsonObject.has("data"))
                                    {
                                        JSONArray dataArray = jsonObject.getJSONArray("data");
                                        if(dataArray.length()>0)
                                        {
                                            listAum = new ArrayList<>();
                                            for (int i = 0; i < dataArray.length(); i++)
                                            {
                                                JSONObject objData = (JSONObject) dataArray.get(i);
                                                SelfAcquiredAUMGetSet getSet = new SelfAcquiredAUMGetSet(AppUtils.getValidAPIIntegerResponseHas(objData,"Id"),
                                                        AppUtils.getValidAPIIntegerResponseHas(objData,"employee_id"),
                                                        AppUtils.getValidAPIIntegerResponseHas(objData,"Year"),
                                                        AppUtils.getValidAPIDoubleResponseHas(objData,"AUM_Amount"),
                                                        AppUtils.getValidAPIStringResponseHas(objData,"Note"),
                                                        AppUtils.getValidAPIStringResponseHas(objData,"created_date"));
                                                listAum.add(getSet);
                                            }
                                        }
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

                @Override
                protected void onPostExecute(Void aVoid)
                {
                    super.onPostExecute(aVoid);

                    Log.e("********", "onPostExecute: "+listAum.size() );

                    llLoading.setVisibility(View.GONE);

                    if(isFroMonthYear)
                    {
                        for (int i = 0; i < listYear.size(); i++)
                        {
                            if(listYear.get(i).toString().equalsIgnoreCase(selectedYear))
                            {
                                tvYear.setText(listYear.get(i).toString());
                            }
                        }
                    }

                    if(success)
                    {
                        if(listAum.size()>0)
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
            }.execute();
        }
        else
        {
            llNoInternet.setVisibility(View.VISIBLE);
        }
    }

    public class AumAdapter extends RecyclerView.Adapter<AumAdapter.ViewHolder>
    {
        List<SelfAcquiredAUMGetSet> listItems;

        public AumAdapter(List<SelfAcquiredAUMGetSet> listClient)
        {
            this.listItems = listClient;
        }

        @Override
        public AumAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_self_acquired_aum, viewGroup, false);
            return new AumAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final AumAdapter.ViewHolder holder, final int position)
        {
            try
            {
                final SelfAcquiredAUMGetSet getSet = listItems.get(position);

                holder.tvYear.setText(String.valueOf(getSet.getYear()));
                holder.tvCreatedDate.setText(AppUtils.universalDateConvert(String.valueOf(getSet.getCreated_date()), "yyyy-MM-dd'T'HH:mm:ss.SSS", "dd MMM yyyy hh:mm a"));
                holder.tvAumAmount.setText(AppUtils.getFormattedAmount(activity, (long) getSet.getAUM_Amount()));
                holder.tvNote.setText(getSet.getNote());

                holder.ivEdit.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        try
                        {
                            Intent intent = new Intent(activity, AddSelfAcquiredAumActivity.class);
                            intent.putExtra("isFor","edit");
                            intent.putExtra("position",position);
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
            private TextView tvYear,tvAumAmount,tvNote,tvMonthEndAum,tvCreatedDate;
            private ImageView ivEdit;

            ViewHolder(View convertView)
            {
                super(convertView);
                tvYear = (TextView) convertView.findViewById(R.id.tvYear);
                tvAumAmount = (TextView) convertView.findViewById(R.id.tvAumAmount);
                tvNote = (TextView) convertView.findViewById(R.id.tvNote);
                tvCreatedDate = (TextView) convertView.findViewById(R.id.tvCreatedDate);
                ivEdit = convertView.findViewById(R.id.ivEdit);
            }
        }
    }

    DialogListAdapter dialogListAdapter;
    public void showListDialog(final String isFor)
    {
        listDialog = new BottomSheetDialog(activity ,R.style.MaterialDialogSheetTemp);
        listDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        final View sheetView = activity.getLayoutInflater().inflate(R.layout.dialog_list, null);
        listDialog.setContentView(sheetView);
        AppUtils.setLightStatusBarBottomDialog(listDialog, activity);
        listDialog.findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listDialog.dismiss();
                listDialog.cancel();
            }
        });

        TextView btnNo = (TextView) listDialog.findViewById(R.id.btnNo);

        TextView tvTitle = (TextView) listDialog.findViewById(R.id.tvTitle);
        tvTitle.setText("Select "+isFor);

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
        public DialogListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowview_common_list, parent, false);
            return new DialogListAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(DialogListAdapter.ViewHolder holder, final int position)
        {
            if(position == getItemCount()-1)
            {
                holder.viewLine.setVisibility(View.GONE);
            }
            else
            {
                holder.viewLine.setVisibility(View.VISIBLE);
            }
            if(isFor.equalsIgnoreCase(YEAR))
            {
                holder.tvValue.setText(listYear.get(position).toString());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        listDialog.dismiss();
                        if(!selectedYear.equalsIgnoreCase( listYear.get(position).toString()))
                        {
                            if(sessionManager.isNetworkAvailable())
                            {
                                selectedYear = listYear.get(position).toString();
                                tvYear.setText(selectedYear);
                                getAumList(false);
                            }
                        }

                    }
                });
            }
        }

        @Override
        public int getItemCount()
        {
            if(isFor.equalsIgnoreCase(YEAR))
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


  /*  public void selectDeleteDialog(final AUMListGetSet getSet, final int pos)
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

    private void removeAmu(final AUMListGetSet getSet, final int pos)
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
                        *//*listActivity.remove(pos);
                        activityListAdapter.notifyDataSetChanged();

                        if (listActivity.size() > 0)
                        {
                            llNoData.setVisibility(View.GONE);
                        }
                        else
                        {
                            llNoData.setVisibility(View.VISIBLE);
                        }*//*
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                super.onPostExecute(result);

            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);
    }*/

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        listAum.clear();
    }
}
