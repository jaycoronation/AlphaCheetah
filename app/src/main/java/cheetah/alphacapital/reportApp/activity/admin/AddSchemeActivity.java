package cheetah.alphacapital.reportApp.activity.admin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import cheetah.alphacapital.textutils.CustomTextInputLayout;
import cheetah.alphacapital.utils.AppAPIUtils;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.utils.SessionManager;
import cheetah.alphacapital.R;
import cheetah.alphacapital.reportApp.getset.CommonGetSet;
import cheetah.alphacapital.reportApp.getset.SchemeGetSet;


public class AddSchemeActivity extends AppCompatActivity
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
    private CustomTextInputLayout inputSchemeType;
    private EditText edtSchemeType;
    private ProgressBar pbSchemeType;
    private CustomTextInputLayout inputSchemeName;
    private EditText edtSchemeName;
    private CustomTextInputLayout inputIsActive;
    private EditText edtIsActive;
    private LinearLayout llSubmit;
    private ArrayList<CommonGetSet> listSchemeType = new ArrayList<CommonGetSet>();
 
    private CommonListAdapter commonListAdapter;
    private boolean isStatusBarHidden = false, is_status_active = false,is_Admin =false;
    private String schemeType_id = "",  schemeType_name = "";


    private String isFor = "";
    private SchemeGetSet getSet = new SchemeGetSet();

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

        AppUtils.setLightStatusBar(activity);

        isFor = getIntent().getStringExtra("isFor");
        getSet = getIntent().getExtras().getParcelable("SchemeInfo");

        setContentView(R.layout.activity_add_scheme);

        setupViews();

        onClickEvents();

        if (sessionManager.isNetworkAvailable())
        {
            llNoInternet.setVisibility(View.GONE);
            GetAllSchemeType();
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
            inputIsActive = (CustomTextInputLayout) findViewById(R.id.inputIsActive);
            edtIsActive = (EditText) findViewById(R.id.edtIsActive);
            llSubmit = (LinearLayout) findViewById(R.id.llSubmit);
            inputSchemeType = (CustomTextInputLayout) findViewById(R.id.inputSchemeType);
            edtSchemeType = (EditText) findViewById(R.id.edtSchemeType);
            pbSchemeType = (ProgressBar) findViewById(R.id.pbSchemeType);
            inputSchemeName = (CustomTextInputLayout) findViewById(R.id.inputSchemeName);
            edtSchemeName = (EditText) findViewById(R.id.edtSchemeName);

            /*setSupportActionBar(toolbar);

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
            ivHeader.setLayoutParams(params);*/

            ivLogo.setVisibility(View.GONE);
            llNotification.setVisibility(View.GONE);
            if (isFor.equalsIgnoreCase("add"))
            {
                txtTitle.setText("Add Scheme");
            }
            else
            {
                txtTitle.setText("Update Scheme");
            }
            llBackNavigation.setVisibility(View.VISIBLE);

            if (isFor.equalsIgnoreCase("edit"))
            {
                edtSchemeType.setText(getSet.getScheme_type_name());
                edtSchemeName.setText(getSet.getScheme_name().toString().trim());
                if (getSet.isIs_active())
                {
                    edtIsActive.setText("Active");
                    is_status_active = true;
                }
                else
                {
                    edtIsActive.setText("InActive");
                    is_status_active = false;
                }
                
                schemeType_id = String.valueOf(getSet.getScheme_type_id());
                schemeType_name = getSet.getScheme_type_name();
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

        edtSchemeType.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    if (listSchemeType.size() > 0)
                    {
                        showDialog("Country");
                    }
                    else
                    {
                        Toast.makeText(activity, "No Country List Found.", Toast.LENGTH_SHORT).show();
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        edtIsActive.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    showIsActiveDialog();
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

                //MitsUtils.hideKeyboard(activity);
                //hideKeyboard();

                if (checkValidation())
                {
                    if (sessionManager.isNetworkAvailable())
                    {
                        AddSchemeAsync(edtSchemeName.getText().toString().trim());
                    }
                    else
                    {
                        Toast.makeText(activity, getResources().getString(R.string.network_failed_message), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void AddSchemeAsync(final String scheme_name)
    {
        try
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
                        if (isFor.equalsIgnoreCase("edit"))
                        {
                            hashMap.put("id", String.valueOf(getSet.getId()));
                        }
                        else
                        {
                            hashMap.put("id", "0");
                        }
                        hashMap.put("scheme_name", scheme_name);

                        hashMap.put("scheme_type_id", schemeType_id);

                        if (is_status_active)
                        {
                            hashMap.put("is_active", String.valueOf(true));
                        }
                        else
                        {
                            hashMap.put("is_active", String.valueOf(false));
                        }


                        if (isFor.equalsIgnoreCase("edit"))
                        {
                            AppUtils.printLog(activity, "UPDATE_SCHEME Request ", hashMap.toString());
                            response = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.UPDATE_SCHEME, hashMap);
                            AppUtils.printLog(activity, "UPDATE_SCHEME Response ", response.toString());
                        }
                        else
                        {
                            AppUtils.printLog(activity, "ADD_SCHEME Request ", hashMap.toString());
                            response = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.ADD_SCHEME, hashMap);
                            AppUtils.printLog(activity, "ADD_SCHEME Response ", response.toString());
                        }

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
                        if (is_success)
                        {
                            llLoading.setVisibility(View.GONE);

                            if (ManageSchemeActivity.handler != null)
                            {
                                Message message = Message.obtain();
                                message.what = 100;
                                ManageSchemeActivity.handler.sendMessage(message);
                            }

                            activity.finish();
                        }
                        else
                        {
                            llLoading.setVisibility(View.GONE);
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
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void GetAllSchemeType()
    {
        new AsyncTask<Void, Void, Void>()
        {
            private boolean isSuccess = false;

            @Override
            protected void onPreExecute()
            {
                pbSchemeType.setVisibility(View.VISIBLE);
                listSchemeType = new ArrayList<CommonGetSet>();
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params)
            {
                try
                {
                    String response = "";

                    HashMap<String, String> hashMap = new HashMap<>();

                    response = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.GETALL_SCHEME_TYPE, hashMap);

                    AppUtils.printLog(activity, "<><> GETALL_SCHEME_TYPE ", response.toString());

                    JSONObject jsonObject = new JSONObject(response);

                    isSuccess = AppUtils.getValidAPIBooleanResponseHas(jsonObject, "success");

                    if (isSuccess)
                    {
                        if (jsonObject.has("data"))
                        {
                            JSONArray dataArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < dataArray.length(); i++)
                            {
                                JSONObject dataObject = dataArray.getJSONObject(i);
                                CommonGetSet commonGetSet = new CommonGetSet();
                                commonGetSet.setId(AppUtils.getValidAPIIntegerResponseHas(dataObject, "id"));
                                commonGetSet.setName(AppUtils.getValidAPIStringResponseHas(dataObject, "scheme_type_name"));
                                listSchemeType.add(commonGetSet);
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
                pbSchemeType.setVisibility(View.GONE);
                super.onPostExecute(result);

                if (isFor.equalsIgnoreCase("edit"))
                {
                   
                }

            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);
    }

    private void showDialog(final String isFor)
    {
        try
        {
            final Dialog dialog = new Dialog(activity, R.style.MaterialDialogSheetTemp);
            dialog.setContentView(R.layout.dialog_chooser);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setGravity(Gravity.TOP);
            dialog.setCanceledOnTouchOutside(true);

            AppUtils.setLightStatusBarDialog(dialog, activity);
            final RecyclerView rvList = (RecyclerView) dialog.findViewById(R.id.rvList);
            final EditText edtSearch = (EditText) dialog.findViewById(R.id.edtSearch);
            final TextView txtTitle = (TextView) dialog.findViewById(R.id.txtTitle);
            final ImageView ivBack = (ImageView) dialog.findViewById(R.id.ivBack);
            rvList.setLayoutManager(new LinearLayoutManager(activity));

            AppUtils.showKeyboard(edtSearch, activity);

            ivBack.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    AppUtils.hideKeyboard(edtSearch, activity);
                    try
                    {
                        dialog.dismiss();
                        dialog.cancel();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });

            rvList.setOnTouchListener(new View.OnTouchListener()
            {
                @Override
                public boolean onTouch(View v, MotionEvent event)
                {
                    AppUtils.hideKeyboard(edtSearch, activity);
                    return false;
                }
            });


            edtSearch.addTextChangedListener(new TextWatcher()
            {
                @SuppressLint("DefaultLocale")
                @Override
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3)
                {
                    int textlength = edtSearch.getText().length();

                    ArrayList<CommonGetSet> list_search = new ArrayList<CommonGetSet>();

                    for (int i = 0; i < listSchemeType.size(); i++)
                    {
                        if (textlength <= listSchemeType.get(i).getName().length())
                        {
                            if (listSchemeType.get(i).getName().toLowerCase(Locale.getDefault()).contains(edtSearch.getText().toString().toLowerCase().trim()))
                            {
                                list_search.add(listSchemeType.get(i));
                            }
                        }
                    }

                    AppendList(list_search, rvList, dialog, isFor, edtSearch);
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

            commonListAdapter = new CommonListAdapter(listSchemeType, dialog, isFor, edtSearch);
            rvList.setAdapter(commonListAdapter);


            dialog.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public class CommonListAdapter extends RecyclerView.Adapter<CommonListAdapter.ViewHolder>
    {
        ArrayList<CommonGetSet> listItems;
        private Dialog dialog;
        private String dialogFor = "";
        private EditText edtSearch;

        public CommonListAdapter(ArrayList<CommonGetSet> list, Dialog dialog, String isFor, EditText edtSearch)
        {
            this.listItems = list;
            this.dialog = dialog;
            this.dialogFor = isFor;
            this.edtSearch = edtSearch;
        }

        @Override
        public CommonListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_country_state_city, viewGroup, false);
            return new CommonListAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final CommonListAdapter.ViewHolder holder, final int position)
        {
            try
            {
                final CommonGetSet getSet = listItems.get(position);
                if (position == (listItems.size() - 1))
                {
                    holder.viewLine.setVisibility(View.INVISIBLE);
                }
                else
                {
                    holder.viewLine.setVisibility(View.VISIBLE);
                }

                holder.txtName.setText(getSet.getName());

                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {

                        AppUtils.hideKeyboard(edtSearch, activity);
                        try
                        {
                            dialog.dismiss();
                            dialog.cancel();
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                        schemeType_id = String.valueOf(getSet.getId());
                        schemeType_name = getSet.getName();
                        edtSchemeType.setText(schemeType_name);
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
            private TextView txtName;
            private View viewLine;

            ViewHolder(View convertView)
            {
                super(convertView);
                txtName = (TextView) convertView.findViewById(R.id.txtName);
                viewLine = (View) convertView.findViewById(R.id.viewLine);
            }
        }
    }

    private void AppendList(ArrayList<CommonGetSet> listItem, RecyclerView recyclerView, Dialog dialog, String isfor, EditText edtSearch)
    {
        commonListAdapter = new CommonListAdapter(listItem, dialog, isfor, edtSearch);
        recyclerView.setAdapter(commonListAdapter);
        commonListAdapter.notifyDataSetChanged();
    }

    private void showIsActiveDialog()
    {
        try
        {
            final Dialog dialog = new Dialog(activity, R.style.MaterialDialogSheetTemp);
            dialog.setContentView(R.layout.dialog_status);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setGravity(Gravity.TOP);
            dialog.setCanceledOnTouchOutside(true);
            AppUtils.setLightStatusBarDialog(dialog, activity);
            final TextView txtInActive;
            final TextView txtActive;
            final ImageView ivBack;

            txtInActive = (TextView) dialog.findViewById(R.id.txtInActive);
            txtActive = (TextView) dialog.findViewById(R.id.txtActive);
            ivBack = (ImageView) dialog.findViewById(R.id.ivBack);


            ivBack.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try
                    {
                        dialog.dismiss();
                        dialog.cancel();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });


            txtInActive.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try
                    {
                        is_status_active = false;
                        edtIsActive.setText("InActive");
                        dialog.dismiss();
                        dialog.cancel();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });


            txtActive.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try
                    {
                        is_status_active = true;
                        edtIsActive.setText("Active");
                        dialog.dismiss();
                        dialog.cancel();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });

            dialog.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private boolean checkValidation()
    {
        boolean isValid = true;
        if (schemeType_id.length() == 0)
        {
            inputSchemeType.setError("Please Select Scheme Type.");
            isValid = false;
        }
        else if (edtSchemeName.getText().toString().length() == 0)
        {
            inputSchemeName.setError("Please Enter Scheme Name.");
            isValid = false;
        }
        else if (edtIsActive.getText().toString().length() == 0)
        {
            inputIsActive.setError("Please select isActive.");
            isValid = false;
        }

        AppUtils.removeError(edtSchemeType, inputSchemeType);
        AppUtils.removeError(edtSchemeName, inputSchemeName);
        AppUtils.removeError(edtIsActive, inputIsActive);

        return isValid;
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

