package cheetah.alphacapital.reportApp.activity.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.core.content.ContextCompat;
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
import java.util.HashMap;

import cheetah.alphacapital.classes.EndlessRecyclerViewScrollListener;
import cheetah.alphacapital.textutils.CustomTextViewSemiBold;
import cheetah.alphacapital.utils.AppAPIUtils;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.utils.SessionManager;
import cheetah.alphacapital.R;
import cheetah.alphacapital.reportApp.getset.SchemeGetSet;

public class ManageSchemeActivity extends AppCompatActivity
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
    private LinearLayout llLoading, llNoData;

    private boolean isStatusBarHidden = false;
    private LinearLayout llNoInternet;
    private LinearLayout llRetry;

    private ImageView ivNoData, ivAddScheme;
    private RecyclerView rvSchemeList;
    private LinearLayoutManager linearLayoutManager;

    private boolean isLoading = false;
    private int pageIndex = 0;
    private String pagesize = "50";
    private boolean isLastPage = false;
    private LinearLayout llLoadingMore;

    private ArrayList<SchemeGetSet> listScheme = new ArrayList<SchemeGetSet>();
    private SchemeListAdapter schemeListAdapter;
    public static Handler handler;

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

        setContentView(R.layout.activity_manage_scheme);

        setupViews();

        onClickEvents();

        try
        {
            if (sessionManager.isNetworkAvailable())
            {
                llNoInternet.setVisibility(View.GONE);
                pageIndex = 0;
                isLastPage = false;
                getAllScheme(true);
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
                    if (msg.what == 100)
                    {
                        if (sessionManager.isNetworkAvailable())
                        {
                            llNoInternet.setVisibility(View.GONE);
                            pageIndex = 0;
                            isLastPage = false;
                            getAllScheme(true);
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
            txtTitle.setText("Manage Scheme");
            llBackNavigation.setVisibility(View.VISIBLE);

            ivLogo = (ImageView) findViewById(R.id.ivLogo);
            ivIcon = (ImageView) findViewById(R.id.ivIcon);
            txtTitle = (TextView) findViewById(R.id.txtTitle);
            ivContactUs = (ImageView) findViewById(R.id.ivContactUs);
            llNotification = (LinearLayout) findViewById(R.id.llNotification);
            llLoading = (LinearLayout) findViewById(R.id.llLoading);
            llNoInternet = (LinearLayout) findViewById(R.id.llNoInternet);
            llRetry = (LinearLayout) findViewById(R.id.llRetry);
            llLoadingMore = findViewById(R.id.llLoadingMore);
            llNoData = findViewById(R.id.llNoData);

            ivAddScheme = (ImageView) findViewById(R.id.ivAddScheme);
            rvSchemeList = (RecyclerView) findViewById(R.id.rvSchemeList);
            linearLayoutManager = new LinearLayoutManager(activity);
            rvSchemeList.setLayoutManager(linearLayoutManager);

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
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });


        ivAddScheme.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(activity, AddSchemeActivity.class);
                intent.putExtra("isFor", "add");
                intent.putExtra("SchemeInfo", (Parcelable) new SchemeGetSet());
                startActivity(intent);
            }
        });

        rvSchemeList.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager)
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
                            getAllScheme(false);
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

    private void getAllScheme(final boolean isFirstTime)
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
                        listScheme = new ArrayList<SchemeGetSet>();
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
                    hashMap.put("pageindex", String.valueOf(pageIndex));
                    hashMap.put("pagesize", pagesize);

                    AppUtils.printLog(activity, "GET_ALL_SCHEME Request ", hashMap.toString());

                    response = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.GET_ALL_SCHEME, hashMap);

                    AppUtils.printLog(activity, "GET_ALL_SCHEME Response ", response.toString());

                    JSONObject jsonObject = new JSONObject(response);

                    is_success = AppUtils.getValidAPIBooleanResponseHas(jsonObject, "success");

                    message = AppUtils.getValidAPIStringResponseHas(jsonObject, "message");

                    if (is_success)
                    {
                        if (jsonObject.has("data"))
                        {
                            JSONObject dataObj = jsonObject.getJSONObject("data");

                            if (dataObj.has("AllScheme"))
                            {
                                JSONArray dataArray = dataObj.getJSONArray("AllScheme");

                                pageIndex = pageIndex + 1;

                                if (dataArray.length() == 0 || dataArray.length() % 50 != 0)
                                {
                                    isLastPage = true;
                                }

                                if (dataArray.length() > 0)
                                {
                                    for (int i = 0; i < dataArray.length(); i++)
                                    {
                                        SchemeGetSet schemeGetSet = new SchemeGetSet();
                                        JSONObject dataObject = (JSONObject) dataArray.get(i);
                                        schemeGetSet.setId(AppUtils.getValidAPIIntegerResponseHas(dataObject, "id"));
                                        schemeGetSet.setScheme_type_id(AppUtils.getValidAPIIntegerResponseHas(dataObject, "scheme_type_id"));
                                        schemeGetSet.setScheme_type_name(AppUtils.getValidAPIStringResponseHas(dataObject, "scheme_type_name"));
                                        schemeGetSet.setScheme_name(AppUtils.getValidAPIStringResponseHas(dataObject, "scheme_name"));
                                        schemeGetSet.setIs_active(AppUtils.getValidAPIBooleanResponseHas(dataObject, "is_active"));
                                        listScheme.add(schemeGetSet);
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
                            if (listScheme.size() > 0)
                            {
                                schemeListAdapter = new SchemeListAdapter(listScheme, activity);
                                rvSchemeList.setAdapter(schemeListAdapter);
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
                            if (schemeListAdapter.getItemCount() > 0)
                            {
                                schemeListAdapter.notifyDataSetChanged();
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

    public class SchemeListAdapter extends RecyclerView.Adapter<SchemeListAdapter.ViewHolder>
    {
        ArrayList<SchemeGetSet> listItems;
        private Activity activity;
        private SessionManager sessionManager;
        private boolean isdone;

        public SchemeListAdapter(ArrayList<SchemeGetSet> listScheme, Activity activity)
        {
            this.listItems = listScheme;
            this.activity = activity;
            sessionManager = new SessionManager(activity);
        }

        @Override
        public SchemeListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_manage_scheme, viewGroup, false);
            return new SchemeListAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final SchemeListAdapter.ViewHolder holder, final int position)
        {
            try
            {
                final SchemeGetSet getSet = listItems.get(position);

                isdone = true;

                holder.tvSchemeTypeName.setText(getSet.getScheme_type_name());
                holder.tvSchemeName.setText(getSet.getScheme_name());

                if (getSet.isIs_active())
                {
                    holder.tvStatus.setText("Active");
                    holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.black));
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
                        Intent intent = new Intent(activity, AddSchemeActivity.class);
                        intent.putExtra("isFor", "edit");
                        intent.putExtra("SchemeInfo", (Parcelable) getSet);
                        startActivity(intent);
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
            private TextView tvSchemeTypeName;
            private TextView tvSchemeName;
            private TextView tvStatus;
            private ImageView ivDelete;
            private ImageView ivEdit;

            ViewHolder(View convertView)
            {
                super(convertView);
                tvSchemeTypeName = (TextView) convertView.findViewById(R.id.tvSchemeTypeName);
                tvSchemeName = (TextView) convertView.findViewById(R.id.tvSchemeName);
                tvStatus = (TextView) convertView.findViewById(R.id.tvStatus);
                ivDelete = (ImageView) convertView.findViewById(R.id.ivDelete);
                ivEdit = (ImageView) convertView.findViewById(R.id.ivEdit);

            }
        }
    }

    public void selectDeleteDialog(final SchemeGetSet getSet, final int pos)
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
        tvTitle.setText("Remove Scheme");
        tvDescription.setText("Are you sure you want to remove this Scheme?");


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
                    removeScheme(getSet, pos);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });


        bottomSheetDialog.show();

    }

    private void removeScheme(final SchemeGetSet getSet, final int pos)
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
                    hashMap.put("schemeid", String.valueOf(getSet.getId()));
                    AppUtils.printLog(activity, "DELETE_SCHEME Request ", hashMap.toString());

                    response = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.DELETE_SCHEME, hashMap);

                    AppUtils.printLog(activity, "DELETE_SCHEME Response ", response.toString());

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
                        listScheme.remove(pos);
                        schemeListAdapter.notifyDataSetChanged();
                        if (listScheme.size() > 0)
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
