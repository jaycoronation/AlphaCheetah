package cheetah.alphacapital.reportApp.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

import cheetah.alphacapital.checkboxlibs.SmoothCheckBox;
import cheetah.alphacapital.textutils.CustomTextInputLayout;
import cheetah.alphacapital.utils.AppAPIUtils;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.utils.SessionManager;
import cheetah.alphacapital.R;
import cheetah.alphacapital.reportApp.getset.SchemeGetSet;


public class AssignSchemeFragment extends Fragment
{
    private Activity activity;
    private SessionManager sessionManager;
    private View rootView;
    private LinearLayout llLoading;
    private ProgressBar pbLoading;
    private TextView txtLoading;
    private LinearLayout llNoData;
    private ImageView ivNoData;
    private LinearLayout llNoInternet;
    private LinearLayout llRetry;
    private TextView txtRetry;
    private CustomTextInputLayout inputSearch;
    private EditText edtSearch;
    private RecyclerView rvSchemeList;
    private LinearLayout llSubmit,llSerach;

    private ArrayList<SchemeGetSet> listScheme = new ArrayList<SchemeGetSet>();
    private ArrayList<Integer> listAssignedSchemeByClient = new ArrayList<Integer>();
    private SchemeListAdapter schemeListAdapter;

    private String clientid = "";

    public AssignSchemeFragment()
    {

    }

    @SuppressLint("ValidFragment")
    public AssignSchemeFragment(String clientidIntent)
    {
        clientid = clientidIntent;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_assign_scheme, container, false);

        activity = getActivity();

        sessionManager = new SessionManager(activity);

        setupViews(rootView);

        onClicks();

        if (sessionManager.isNetworkAvailable())
        {
            llNoInternet.setVisibility(View.GONE);
            getDataFromApi();
        }
        else
        {
            llNoInternet.setVisibility(View.VISIBLE);
        }


        return rootView;
    }

    private void setupViews(View rootView)
    {
        llLoading = (LinearLayout) rootView.findViewById(R.id.llLoading);
        pbLoading = (ProgressBar) rootView.findViewById(R.id.pbLoading);
        txtLoading = (TextView) rootView.findViewById(R.id.txtLoading);
        llNoData = (LinearLayout) rootView.findViewById(R.id.llNoData);
        ivNoData = (ImageView) rootView.findViewById(R.id.ivNoData);
        llNoInternet = (LinearLayout) rootView.findViewById(R.id.llNoInternet);
        llRetry = (LinearLayout) rootView.findViewById(R.id.llRetry);
        txtRetry = (TextView) rootView.findViewById(R.id.txtRetry);
        inputSearch = (CustomTextInputLayout) rootView.findViewById(R.id.inputSearch);
        edtSearch = (EditText) rootView.findViewById(R.id.edtSearch);
        rvSchemeList = (RecyclerView) rootView.findViewById(R.id.rvSchemeList);
        llSubmit = (LinearLayout) rootView.findViewById(R.id.llSubmit);
        llSerach  = (LinearLayout) rootView.findViewById(R.id.llSerach);
        rvSchemeList.setLayoutManager(new LinearLayoutManager(activity));
    }

    private void onClicks()
    {

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
                        getDataFromApi();
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

        edtSearch.addTextChangedListener(new TextWatcher()
        {
            @SuppressLint("DefaultLocale")
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3)
            {
                int textlength = edtSearch.getText().length();

                if (textlength > 0)
                {
                    ArrayList<SchemeGetSet> list_search = new ArrayList<SchemeGetSet>();

                    for (int i = 0; i < listScheme.size(); i++)
                    {
                        String schemeName = listScheme.get(i).getScheme_name();

                        if (schemeName.toLowerCase(Locale.getDefault()).contains(edtSearch.getText().toString().toLowerCase().trim()))
                        {
                            list_search.add(listScheme.get(i));
                        }
                    }

                    if (list_search.size() > 0)
                    {
                        schemeListAdapter = new SchemeListAdapter(list_search, activity);
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
                    if (listScheme.size() > 0)
                    {
                        llSerach.setVisibility(View.VISIBLE);
                        schemeListAdapter = new SchemeListAdapter(listScheme, activity);
                        rvSchemeList.setAdapter(schemeListAdapter);
                        llNoData.setVisibility(View.GONE);
                    }
                    else
                    {
                        llSerach.setVisibility(View.GONE);
                        llNoData.setVisibility(View.VISIBLE);
                    }
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

        llSubmit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (schemeListAdapter.getSelectedId().length() > 0)
                {
                    if (sessionManager.isNetworkAvailable())
                    {
                        mapClientWithScheme();
                    }
                    else
                    {
                        Toast.makeText(activity, getResources().getString(R.string.network_failed_message), Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    Toast.makeText(activity, "Please select atleast one employee.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void getDataFromApi()
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
                    listScheme = new ArrayList<SchemeGetSet>();
                    listAssignedSchemeByClient = new ArrayList<Integer>();
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
                getAllScheme();
                return null;
            }

            private void getAllScheme()
            {
                try
                {
                    String response = "";

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("pageindex", "0");
                    hashMap.put("pagesize", "0");
                    hashMap.put("clientid", clientid);
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
                                        schemeGetSet.setCreated_date(AppUtils.getValidAPIStringResponseHas(dataObject, "created_date"));
                                        schemeGetSet.setIs_active(AppUtils.getValidAPIBooleanResponseHas(dataObject, "is_active"));
                                        listScheme.add(schemeGetSet);
                                    }
                                }
                            }

                            if (dataObj.has("ClientAssignedScheme"))
                            {
                                JSONArray clientAssignedSchemeArray = dataObj.getJSONArray("ClientAssignedScheme");

                                if (clientAssignedSchemeArray.length() > 0)
                                {
                                    for (int i = 0; i < clientAssignedSchemeArray.length(); i++)
                                    {
                                        JSONObject dataObject = (JSONObject) clientAssignedSchemeArray.get(i);
                                        listAssignedSchemeByClient.add(AppUtils.getValidAPIIntegerResponseHas(dataObject, "id"));
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
            protected void onPostExecute(Void result)
            {
                super.onPostExecute(result);
                try
                {
                    llLoading.setVisibility(View.GONE);
                    llNoInternet.setVisibility(View.GONE);

                    if (is_success)
                    {
                        if (listScheme.size() > 0)
                        {
                            for (int i = 0; i < listScheme.size(); i++)
                            {
                                for (int j = 0; j < listAssignedSchemeByClient.size(); j++)
                                {
                                    if (listAssignedSchemeByClient.get(j) == listScheme.get(i).getId())
                                    {
                                        SchemeGetSet getSet = listScheme.get(i);
                                        getSet.setIs_selected(true);
                                        listScheme.set(i, getSet);
                                    }
                                }
                            }

                            schemeListAdapter = new SchemeListAdapter(listScheme, activity);
                            rvSchemeList.setAdapter(schemeListAdapter);
                            llNoData.setVisibility(View.GONE);
                            llSerach.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            llNoData.setVisibility(View.VISIBLE);
                            llSerach.setVisibility(View.GONE);
                        }
                    }
                    else
                    {
                        llSerach.setVisibility(View.GONE);
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

        public SchemeListAdapter(ArrayList<SchemeGetSet> listClient, Activity activity)
        {
            this.listItems = listClient;
            this.activity = activity;
            sessionManager = new SessionManager(activity);
        }

        @Override
        public SchemeListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_check_box, viewGroup, false);
            return new SchemeListAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final SchemeListAdapter.ViewHolder holder, final int position)
        {

            try
            {
                final SchemeGetSet getSet = listItems.get(position);

                holder.txtTitle.setText(getSet.getScheme_name());

                isdone = true;

                if (getSet.isIs_selected())
                {
                    holder.smoothCheckBox.setChecked(true);
                    holder.txtTitle.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                else
                {
                    holder.smoothCheckBox.setChecked(false);
                    holder.txtTitle.setTextColor(getResources().getColor(R.color.black));
                }

                holder.smoothCheckBox.setClickable(false);

                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (!isdone)
                        {
                            if (getSet.isIs_selected())
                            {
                                getSet.setIs_selected(false);
                                holder.smoothCheckBox.setChecked(false, true);
                                holder.txtTitle.setTextColor(getResources().getColor(R.color.black));
                            }
                            else
                            {
                                getSet.setIs_selected(true);
                                holder.smoothCheckBox.setChecked(true, true);
                                holder.txtTitle.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                            }

                            notifyItemChanged(position);
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

        public String getSelectedId()
        {
            String ids = "";

            try
            {
                for (int i = 0; i < listItems.size(); i++)
                {
                    if (listItems.get(i).isIs_selected())
                    {
                        if (ids.equalsIgnoreCase(""))
                        {
                            ids = String.valueOf(listItems.get(i).getId());
                        }
                        else
                        {
                            ids = ids + "," + String.valueOf(listItems.get(i).getId());
                        }
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return ids;

        }

        @Override
        public int getItemCount()
        {
            return listItems.size();
        }

        @SuppressWarnings("unused")
        public class ViewHolder extends RecyclerView.ViewHolder
        {
            private TextView txtTitle;
            private SmoothCheckBox smoothCheckBox;
            private View viewLine;

            ViewHolder(View convertView)
            {
                super(convertView);
                txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
                smoothCheckBox = (SmoothCheckBox) convertView.findViewById(R.id.smoothCheckBox);
                viewLine = (View) convertView.findViewById(R.id.viewLine);
            }
        }
    }

    private void mapClientWithScheme()
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
                    hashMap.put("client_id", clientid);
                    hashMap.put("scheme_ids", schemeListAdapter.getSelectedId());

                    AppUtils.printLog(activity, "MAP_CLIENT_WITH_SCHEME Request ", hashMap.toString());

                    response = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.MAP_CLIENT_WITH_SCHEME, hashMap);

                    AppUtils.printLog(activity, "MAP_CLIENT_WITH_SCHEME Response ", response.toString());

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
                    llLoading.setVisibility(View.GONE);
                    llNoInternet.setVisibility(View.GONE);

                    if (is_success)
                    {
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
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

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

}
