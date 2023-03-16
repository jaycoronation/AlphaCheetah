package cheetah.alphacapital.reportApp.activity.admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cheetah.alphacapital.reportApp.getset.AUMEntryGetSet;
import cheetah.alphacapital.utils.AppAPIUtils;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.R;
import cheetah.alphacapital.databinding.ActivityAumentryBinding;
import cheetah.alphacapital.databinding.RowviewAumListEntryBinding;

public class AUMEntryActivity extends BaseActivity
{

    private ActivityAumentryBinding binding;
    private List<AUMEntryGetSet> listReport = new ArrayList<>();
    private ReportAdapter reportAdapter;
    public static Handler handler;
    private boolean isStatusBarHidden = false;
    private int editClickedPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
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
        }

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_aumentry);
        initView();
        handler = new Handler(message -> {
            if (message.what == 1)//For Get List Object to add in list
            {
                AUMEntryGetSet bean = (AUMEntryGetSet) message.obj;
                if (message.arg1 == 0)
                {
                    listReport.add(bean);
                }
                else
                {
                    listReport.set(editClickedPosition, bean);
                }
                setAdapter();
            }
            return false;
        });
    }

    private void initView()
    {
        binding.toolbar.ivContactUs.setVisibility(View.VISIBLE);
        binding.toolbar.ivContactUs.setImageResource(R.drawable.ic_add_white);
        binding.noData.tvNoDataText.setText("Click on + button to add Self Acquired AUM");
        binding.rvReport.setLayoutManager(new LinearLayoutManager(activity));
        int height = 56;
        if (isStatusBarHidden)
        {
            height = 56 + 25;
            binding.toolbar.toolbar.findViewById(R.id.viewStatusBar).setVisibility(View.INVISIBLE);
        }
        else
        {
            binding.toolbar.toolbar.findViewById(R.id.viewStatusBar).setVisibility(View.GONE);
        }
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) binding.toolbar.ivHeader.getLayoutParams();
        params.height = (int) AppUtils.pxFromDp(activity, height);
        binding.toolbar.ivHeader.setLayoutParams(params);
        binding.toolbar.ivHeader.setImageResource(R.drawable.img_portfolio);

        binding.toolbar.ivLogo.setVisibility(View.GONE);
        binding.toolbar.llNotification.setVisibility(View.GONE);
        binding.toolbar.txtTitle.setText("Add Self Acquired AUM");
        binding.toolbar.llBackNavigation.setVisibility(View.VISIBLE);

        binding.toolbar.llBackNavigation.setOnClickListener(view -> {
            finish();
            finishActivityAnimation();
        });

        binding.toolbar.ivContactUs.setOnClickListener(view -> {
            Intent intent = new Intent(activity, AddSelfAcquiredAumNewActivity.class);
            intent.putExtra("isFor", "add");
            startActivity(intent);
        });

        binding.tvSubmit.setOnClickListener(view -> {
            if (listReport.size() > 0)
            {
                for (int i = 0; i < listReport.size(); i++)
                {
                    addAUM(listReport.get(i), i);
                }
            }
            else
            {
                AppUtils.showToast(activity, "Please add Self Acquired AUM to submit");
            }
        });

        setAdapter();
    }

    private void setAdapter()
    {
        if (reportAdapter != null)
        {
            reportAdapter.notifyDataSetChanged();
        }
        else
        {
            reportAdapter = new ReportAdapter();
            binding.rvReport.setAdapter(reportAdapter);
        }

        if (listReport.size() == 0)
        {
            binding.noData.llNoData.setVisibility(View.VISIBLE);
            binding.tvSubmit.setVisibility(View.GONE);
        }
        else
        {
            binding.noData.llNoData.setVisibility(View.GONE);
            binding.tvSubmit.setVisibility(View.VISIBLE);
        }
    }

    public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder>
    {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_aum_list_entry, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position)
        {

            AUMEntryGetSet bean = listReport.get(position);
            holder.binding.tvAUMAmount.setText(String.valueOf(bean.getAUM_Amount()));
            holder.binding.tvNotes.setText(bean.getNote());
            holder.binding.tvYear.setText(bean.getYear());

            holder.binding.ivEdit.setOnClickListener(view -> {
                editClickedPosition = position;
                Intent intent = new Intent(activity, AddSelfAcquiredAumNewActivity.class);
                intent.putExtra("isFor", "edit");
                intent.putExtra("data", new Gson().toJson(listReport.get(position)));
                startActivity(intent);
            });

            holder.binding.ivDelete.setOnClickListener(view -> {
                if (listReport.size() > 0)
                {
                    listReport.remove(position);

                    if (listReport.size() == 0)
                    {
                        binding.noData.llNoData.setVisibility(View.VISIBLE);
                    }

                    setAdapter();
                }
            });

        }

        @Override
        public int getItemCount()
        {
            return listReport.size();
        }

        @SuppressWarnings("unused")
        public class ViewHolder extends RecyclerView.ViewHolder
        {
            RowviewAumListEntryBinding binding;

            ViewHolder(View convertView)
            {
                super(convertView);
                binding = DataBindingUtil.bind(convertView);
                binding.ivDelete.setVisibility(View.VISIBLE);
                binding.ivEdit.setVisibility(View.VISIBLE);
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void addAUM(AUMEntryGetSet getSet, int position)
    {
        if (sessionManager.isNetworkAvailable())
        {
            new AsyncTask<Void, Void, Void>()
            {
                String message = "";
                boolean success = false;

                @Override
                protected void onPreExecute()
                {
                    super.onPreExecute();
                    binding.loading.llLoading.setVisibility(View.VISIBLE);
                }

                @Override
                protected Void doInBackground(Void... voids)
                {
                    try
                    {
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("Id", "0");
                        hashMap.put("employee_id", AppUtils.getEmployeeIdForAdmin(activity));
                        hashMap.put("Year", getSet.getYear());
                        hashMap.put("AUM_Amount", String.valueOf(getSet.getAUM_Amount()));
                        hashMap.put("Note", getSet.getNote());

                        Log.e("add amu req ", "doInBackground: " + hashMap.toString());
                        String serverResponse = "";
                        serverResponse = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.ADD_SELF_ACQUIRED_AUM, hashMap);
                        Log.e("add amu resp ", "doInBackground: " + serverResponse);
                        if (serverResponse != null)
                        {
                            try
                            {
                                JSONObject jsonObject = new JSONObject(serverResponse);

                                success = AppUtils.getValidAPIBooleanResponseHas(jsonObject, "success");

                                message = AppUtils.getValidAPIStringResponseHas(jsonObject, "message");

                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
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
                protected void onPostExecute(Void aVoid)
                {
                    super.onPostExecute(aVoid);

                    if (success)
                    {
                        if (position == listReport.size() - 1)
                        {
                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                            binding.loading.llLoading.setVisibility(View.GONE);
                            activity.finish();
                            //activity.overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
                        }
                    }
                    else
                    {
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                        binding.loading.llLoading.setVisibility(View.GONE);
                    }
                }
            }.execute();
        }
        else
        {
            AppUtils.showToast(activity, activity.getString(R.string.network_failed_message));
        }
    }
}
