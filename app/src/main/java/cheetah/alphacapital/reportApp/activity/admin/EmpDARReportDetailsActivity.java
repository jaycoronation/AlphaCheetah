package cheetah.alphacapital.reportApp.activity.admin;

import androidx.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Message;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;


import java.util.Timer;

import cheetah.alphacapital.reportApp.fragment.EmpDARActivityAnalysisFragment;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.R;
import cheetah.alphacapital.databinding.ActivityEmpDarreportDetailsBinding;
import cheetah.alphacapital.reportApp.fragment.EmpDARClientAnalysisFragment;
import cheetah.alphacapital.reportApp.fragment.EmpDAREmployeeAnalysisFragment;
import cheetah.alphacapital.reportApp.fragment.EmpDARReportListFragment;

public class EmpDARReportDetailsActivity extends BaseActivity
{
    private ActivityEmpDarreportDetailsBinding binding;
    private String[] tabsTitle = {"DAR Report", "RM Analysis", "Activity Analysis", "Client Analysis"};

    private boolean isStatusBarHidden = false;

    private int employeeId = 0, currentMonth = 0, currentYear = 0;
    private String monthName = "";

    private Timer timer = new Timer();
    private final long DELAY = 400;

    @Override protected void onCreate(Bundle savedInstanceState)
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

        if (getIntent().hasExtra("employeeId"))
        {
            employeeId = getIntent().getIntExtra("employeeId", 0);
        }

        if (getIntent().hasExtra("currentMonth"))
        {
            currentMonth = getIntent().getIntExtra("currentMonth", 0);
        }

        if (getIntent().hasExtra("currentYear"))
        {
            currentYear = getIntent().getIntExtra("currentYear", 0);
        }

        if (getIntent().hasExtra("monthName"))
        {
            monthName = getIntent().getStringExtra("monthName");
        }

        binding = DataBindingUtil.setContentView(activity, R.layout.activity_emp_darreport_details);
        initView();
        setupPagerData();
    }

    private void initView()
    {
       /* int height = 56;
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
        binding.toolbar.ivHeader.setImageResource(R.drawable.img_portfolio);*/

        binding.toolbar.ivLogo.setVisibility(View.GONE);
        binding.toolbar.llNotification.setVisibility(View.GONE);
        binding.toolbar.txtTitle.setText("DAR Report");
        binding.toolbar.llBackNavigation.setVisibility(View.VISIBLE);

        binding.toolbar.llBackNavigation.setOnClickListener(view ->
        {
            finish();
            finishActivityAnimation();
        });

        binding.toolbar.ivSerach.setVisibility(View.VISIBLE);
        binding.toolbar.edtSearch.setHint("Search message");

        binding.toolbar.ivSerach.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View v)
            {
                doSearch();
            }
        });

        binding.toolbar.ivClose.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View v)
            {
                doClose();
            }
        });

        binding.toolbar.edtSearch.addTextChangedListener(new TextWatcher()
        {
            @Override public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3)
            {
                try
                {
                    if (EmpDARReportListFragment.handler != null)
                    {
                        Message message = Message.obtain();
                        message.obj = binding.toolbar.edtSearch.getText().toString().trim();
                        message.what = 1;
                        EmpDARReportListFragment.handler.sendMessage(message);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3)
            {
            }

            @Override public void afterTextChanged(Editable arg0)
            {
            }
        });
    }

    private void setupPagerData()
    {
        TabAdapter mAdapter = new TabAdapter();
        binding.pager.setAdapter(mAdapter);
        binding.tabs.setupWithViewPager(binding.pager);
        binding.pager.setOffscreenPageLimit(4);
        setupTabIcons();
    }

    private void setupTabIcons()
    {
        try
        {
            for (int i = 0; i < tabsTitle.length; i++)
            {
                final View tab = LayoutInflater.from(activity).inflate(R.layout.rowview_tab, null);
                TextView title = (TextView) tab.findViewById(R.id.txtTab);
                title.setText(tabsTitle[i]);
                binding.tabs.getTabAt(i).setCustomView(tab);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private class TabAdapter extends FragmentPagerAdapter
    {
        public TabAdapter()
        {
            super(getSupportFragmentManager());
        }

        @Override public Fragment getItem(int position)
        {
            if (position == 0)
            {
                return new EmpDARReportListFragment(employeeId, currentMonth, currentYear, monthName);
            }
            else if (position == 1)
            {
                return new EmpDAREmployeeAnalysisFragment(employeeId, currentMonth, currentYear);
            }
            else if (position == 2)
            {
                return new EmpDARActivityAnalysisFragment(employeeId, currentMonth, currentYear);
            }
            else
            {
                return new EmpDARClientAnalysisFragment(employeeId, currentMonth, currentYear);
            }
        }

        @Override public int getCount()
        {
            return tabsTitle.length;
        }

        @Override public CharSequence getPageTitle(int position)
        {
            return null;
        }
    }

    private void doClose()
    {
        binding.toolbar.llBackNavigation.setVisibility(View.VISIBLE);
        binding.toolbar.txtTitle.setVisibility(View.VISIBLE);
        binding.toolbar.edtSearch.setVisibility(View.GONE);
        binding.toolbar.ivSerach.setVisibility(View.VISIBLE);
        binding.toolbar.ivClose.setVisibility(View.GONE);
        binding.toolbar.edtSearch.setText("");
        if (EmpDARReportListFragment.handler != null)
        {
            Message message = Message.obtain();
            message.what = 2;
            EmpDARReportListFragment.handler.sendMessage(message);
        }
    }

    private void doSearch()
    {
        binding.toolbar.edtSearch.requestFocus();
        binding.toolbar.llBackNavigation.setVisibility(View.GONE);
        binding.toolbar.txtTitle.setVisibility(View.GONE);
        binding.toolbar.edtSearch.setVisibility(View.VISIBLE);
        binding.toolbar.ivSerach.setVisibility(View.GONE);
        binding.toolbar.ivClose.setVisibility(View.VISIBLE);
        //MitsUtils.openKeyboard(binding.toolbar.edtSearch, activity);
    }
}
