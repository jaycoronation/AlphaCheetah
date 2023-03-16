package cheetah.alphacapital.reportApp.activity.admin;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cheetah.alphacapital.reportApp.getset.AUMEntryGetSet;
import cheetah.alphacapital.textutils.CustomTextInputLayout;
import cheetah.alphacapital.textutils.CustomTextViewSemiBold;
import cheetah.alphacapital.utils.AppAPIUtils;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.utils.SessionManager;
import cheetah.alphacapital.R;

public class AddSelfAcquiredAumNewActivity extends AppCompatActivity
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


    private TextView tvSubmit;
    private CustomTextInputLayout inputNote, inputAumAmount, inputYear;
    private EditText edtNote, edtAumAmount, edtYear;

    private LinearLayout llNoInternet;
    private LinearLayout llRetry;

    private LinearLayout llSubmit;
    private boolean isStatusBarHidden = false;

    private List<String> listYear = new ArrayList<>();

    private final String YEAR = "Year";

    private BottomSheetDialog listDialog;

    private String selectedYear = "", isFor = "";

    private AUMEntryGetSet editGetSet;

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
        AppUtils.setLightStatusBar(activity);
        isFor = getIntent().getStringExtra("isFor");

        if (isFor.equals("edit"))
        {
            if (getIntent().hasExtra("data"))
            {
                editGetSet = new Gson().fromJson(getIntent().getStringExtra("data"), AUMEntryGetSet.class);
            }
        }


        setContentView(R.layout.activity_add_self_acquired_aum);

        setupViews();

        onClickEvents();

        if (sessionManager.isNetworkAvailable())
        {
            llNoInternet.setVisibility(View.GONE);
            getYearData();
        }
        else
        {
            llNoInternet.setVisibility(View.VISIBLE);
        }
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
            if (isFor.equalsIgnoreCase("add"))
            {
                txtTitle.setText("Add Self Acquired AUM");
            }
            else
            {
                txtTitle.setText("Update Self Acquired AUM");
            }
            llBackNavigation.setVisibility(View.VISIBLE);

            /*ImageView ivHeader = findViewById(R.id.ivHeader);
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
            ivHeader.setLayoutParams(params);*/

            tvSubmit = findViewById(R.id.tvSubmit);

            ivLogo = (ImageView) findViewById(R.id.ivLogo);
            ivIcon = (ImageView) findViewById(R.id.ivIcon);
            txtTitle = (TextView) findViewById(R.id.txtTitle);
            ivContactUs = (ImageView) findViewById(R.id.ivContactUs);
            llNotification = (LinearLayout) findViewById(R.id.llNotification);
            llLoading = (LinearLayout) findViewById(R.id.llLoading);
            llNoInternet = (LinearLayout) findViewById(R.id.llNoInternet);
            llRetry = (LinearLayout) findViewById(R.id.llRetry);

            llSubmit = (LinearLayout) findViewById(R.id.llSubmit);

            inputNote = findViewById(R.id.inputNote);
            inputAumAmount = findViewById(R.id.inputAumAmount);

            edtNote = findViewById(R.id.edtNote);
            edtAumAmount = findViewById(R.id.edtAumAmount);

            inputYear = findViewById(R.id.inputYear);

            edtYear = findViewById(R.id.edtYear);

            setSupportActionBar(toolbar);

            if (isFor.equalsIgnoreCase("add"))
            {
                tvSubmit.setText("Add");
            }
            else
            {
                tvSubmit.setText("Update");
            }

            if (isFor.equals("edit"))
            {
                edtAumAmount.setText(String.valueOf(editGetSet.getAUM_Amount()));
                edtNote.setText(editGetSet.getNote());
                edtYear.setText(editGetSet.getYear());
                selectedYear = editGetSet.getYear();
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

        edtYear.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showListDialog(YEAR);
            }
        });

        llSubmit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                //MitsUtils.hideKeyboard(activity);

                if (checkValidation())
                {
                    String aumAmount = "0";
                    String note = "0";

                    aumAmount = edtAumAmount.getText().toString().trim();
                    note = edtNote.getText().toString().trim();


                    AUMEntryGetSet bean = new AUMEntryGetSet();
                    bean.setAUM_Amount(Double.parseDouble(aumAmount));
                    bean.setNote(note);
                    bean.setYear(selectedYear);

                    if (AUMEntryActivity.handler != null)
                    {
                        Message message = Message.obtain();
                        message.what = 1;
                        message.obj = bean;
                        if (isFor.equals("add"))
                        {
                            message.arg1 = 0;
                        }
                        else
                        {
                            message.arg1 = 1;
                        }
                        AUMEntryActivity.handler.sendMessage(message);
                    }

                    finish();
                    AppUtils.finishActivityAnimation(activity);
                }
            }
        });
    }

    private boolean checkValidation()
    {
        boolean isValid = true;

        if (edtYear.getText().toString().trim().equals(""))
        {
            inputYear.setError("Please select Year.");
            isValid = false;
        }
        else if (edtAumAmount.getText().toString().trim().equals(""))
        {
            inputAumAmount.setError("Please enter AUM amount.");
            isValid = false;
        }

        AppUtils.removeError(edtAumAmount, inputAumAmount);
        AppUtils.removeError(edtYear, inputYear);

        return isValid;
    }

    private void getYearData()
    {
        if (sessionManager.isNetworkAvailable())
        {
            new AsyncTask<Void, Void, Void>()
            {
                boolean isSuccess;

                @Override
                protected void onPreExecute()
                {
                    super.onPreExecute();

                    llLoading.setVisibility(View.VISIBLE);
                }

                @Override
                protected Void doInBackground(Void... voids)
                {
                    getMonthYearData();
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
                                if (yearArray.length() > 0)
                                {
                                    for (int i = 0; i < yearArray.length(); i++)
                                    {
                                        listYear.add(yearArray.getString(i));
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
                    llLoading.setVisibility(View.GONE);
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);
        }
        else
        {
            //
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
            if (isFor.equalsIgnoreCase(YEAR))
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
                                edtYear.setText(selectedYear);
                            }
                        }

                    }
                });
            }
        }

        @Override
        public int getItemCount()
        {
            if (isFor.equalsIgnoreCase(YEAR))
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
