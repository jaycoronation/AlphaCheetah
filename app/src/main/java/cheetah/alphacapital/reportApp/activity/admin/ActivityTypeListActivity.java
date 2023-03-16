package cheetah.alphacapital.reportApp.activity.admin;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import androidx.appcompat.widget.AppCompatTextView;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;
import cheetah.alphacapital.textutils.CustomEditText;
import cheetah.alphacapital.textutils.CustomTextViewMedium;
import cheetah.alphacapital.textutils.CustomTextViewSemiBold;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import cheetah.alphacapital.R;
import cheetah.alphacapital.reportApp.activity.AddActivityTypeActivity;
import cheetah.alphacapital.reportApp.getset.CommonResponse;
import cheetah.alphacapital.reportApp.getset.ActivityTypeResponse;

public class ActivityTypeListActivity extends BaseActivity
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
    CustomTextViewSemiBold txtTitle;
    @BindView(R.id.edtSearch)
    CustomEditText edtSearch;
    @BindView(R.id.ivSerach)
    ImageView ivSerach;
    @BindView(R.id.ivClose)
    ImageView ivClose;
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
    AppCompatTextView txtLoading;
    @BindView(R.id.llLoading)
    LinearLayout llLoading;
    @BindView(R.id.txtRetry)
    AppCompatTextView txtRetry;
    @BindView(R.id.llRetry)
    LinearLayout llRetry;
    @BindView(R.id.llNoInternet)
    LinearLayout llNoInternet;
    @BindView(R.id.rvActivityType)
    RecyclerView rvActivityType;
    @BindView(R.id.ivNoData)
    ImageView ivNoData;
    @BindView(R.id.tvNoDataText)
    CustomTextViewMedium tvNoDataText;
    @BindView(R.id.llNoData)
    LinearLayout llNoData;
    @BindView(R.id.ivAddActivityType)
    ImageView ivAddActivityType;
    private LinearLayoutManager linearLayoutManager;
    private boolean isStatusBarHidden = false;

    private ArrayList<ActivityTypeResponse.DataBean> listActivityType = new ArrayList<ActivityTypeResponse.DataBean>();
    private ActivityTypeAdapter activityTypeAdapter;
    public static Handler handler;
    private Timer timer = new Timer();
    private final long DELAY = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        /*try
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

        setContentView(R.layout.activity_manage_activity_type);

        ButterKnife.bind(this);

        setupViews();

        onClickEvents();

        try
        {
            if (sessionManager.isNetworkAvailable())
            {
                llNoInternet.setVisibility(View.GONE);
                getAllActivityTypeFromApi();
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
                            getAllActivityTypeFromApi();
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
                            ActivityTypeResponse.DataBean dataBean = (ActivityTypeResponse.DataBean) msg.obj;
                            for (int i = 0; i < listActivityType.size(); i++)
                            {
                                if (dataBean.getId() == listActivityType.get(i).getId())
                                {
                                    listActivityType.set(i, dataBean);
                                    activityTypeAdapter.notifyItemChanged(i);
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
            }
        });
    }

    private void setupViews()
    {
        try
        {
            ivLogo.setVisibility(View.GONE);
            llNotification.setVisibility(View.GONE);
            txtTitle.setText("Activity Type List");
            tvNoDataText.setText("No Activity Type Found.");
            llBackNavigation.setVisibility(View.VISIBLE);
            /*ivHeader.setImageResource(R.drawable.img_portfolio);

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
            ivHeader.setLayoutParams(params);*/
            linearLayoutManager = new LinearLayoutManager(activity);
            rvActivityType.setLayoutManager(linearLayoutManager);
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

        ivAddActivityType.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(activity, AddActivityTypeActivity.class);
                intent.putExtra("isFor", "add");
                intent.putExtra("ActivityTypeResponse", (Parcelable) new ActivityTypeResponse.DataBean());
                startActivity(intent);
            }
        });
    }

    private void getAllActivityTypeFromApi()
    {
        llLoading.setVisibility(View.VISIBLE);

        Call<ActivityTypeResponse> call = apiService.getAllActivityType();

        call.enqueue(new Callback<ActivityTypeResponse>()
        {
            @Override
            public void onResponse(Call<ActivityTypeResponse> call, Response<ActivityTypeResponse> response)
            {
                if (response.isSuccessful())
                {
                    if (response.body().isSuccess())
                    {
                        listActivityType = new ArrayList<ActivityTypeResponse.DataBean>();
                        listActivityType.addAll(response.body().getData());
                        if (listActivityType.size() > 0)
                        {
                            activityTypeAdapter = new ActivityTypeAdapter(listActivityType);
                            rvActivityType.setAdapter(activityTypeAdapter);
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
                    apiFailedSnackBar();
                }
                llLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ActivityTypeResponse> call, Throwable t)
            {

                llLoading.setVisibility(View.GONE);
                apiFailedSnackBar();
                llNoData.setVisibility(View.VISIBLE);
            }
        });
    }

    public class ActivityTypeAdapter extends RecyclerView.Adapter<ActivityTypeAdapter.ViewHolder>
    {
        ArrayList<ActivityTypeResponse.DataBean> listItems;

        private boolean isdone;

        public ActivityTypeAdapter(ArrayList<ActivityTypeResponse.DataBean> listActivityType)
        {
            this.listItems = listActivityType;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_activity_type_list, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position)
        {
            try
            {
                final ActivityTypeResponse.DataBean getSet = listItems.get(position);

                isdone = true;

                if (getSet.getActivityTypeName().length() > 0)
                {
                    holder.tvName.setText(String.valueOf(getSet.getActivityTypeName()));
                }
                else
                {
                    holder.tvName.setText("");
                }

                if (getSet.isIsActive())
                {
                    holder.tvStatus.setText("Active");
                }
                else
                {
                    holder.tvStatus.setText("InActive");
                }



                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        Intent intent = new Intent(activity, AddActivityTypeActivity.class);
                        intent.putExtra("isFor", "edit");
                        intent.putExtra("ActivityTypeResponse", (Parcelable) getSet);
                        startActivity(intent);
                    }
                });

                holder.ivEdit.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        Intent intent = new Intent(activity, AddActivityTypeActivity.class);
                        intent.putExtra("isFor", "edit");
                        intent.putExtra("ActivityTypeResponse", (Parcelable) getSet);
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
            @BindView(R.id.tvName)
            TextView tvName;
            @BindView(R.id.tvStatus)
            TextView tvStatus;
            @BindView(R.id.ivEdit)
            ImageView ivEdit;
            @BindView(R.id.ivDelete)
            ImageView ivDelete;

            ViewHolder(View convertView)
            {
                super(convertView);
                ButterKnife.bind(this, convertView);
            }
        }
    }

    public void selectDeleteDialog(final ActivityTypeResponse.DataBean getSet, final int pos)
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
        tvTitle.setText("Remove Activity Type");
        tvDescription.setText("Are you sure you want to remove this Activity Type?");


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
                    removeActivityType(getSet, pos);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });


        bottomSheetDialog.show();

    }

    private void removeActivityType(final ActivityTypeResponse.DataBean getSet, final int pos)
    {
        llLoading.setVisibility(View.VISIBLE);
        Call<CommonResponse> call = apiService.deleteActivityType(String.valueOf(getSet.getId()));

        call.enqueue(new Callback<CommonResponse>()
        {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response)
            {
                if (response.isSuccessful())
                {
                    if (response.body().isSuccess())
                    {
                        listActivityType.remove(pos);
                        activityTypeAdapter.notifyDataSetChanged();
                        if (listActivityType.size() > 0)
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
