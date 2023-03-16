package cheetah.alphacapital.reportApp.activity.admin;

import androidx.databinding.DataBindingUtil;

import android.os.Build;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import cheetah.alphacapital.reportApp.fragment.EmpAUMReportFragment;
import cheetah.alphacapital.reportApp.fragment.EmpDARReportFragment;
import cheetah.alphacapital.reportApp.fragment.EmpNotesFragment;
import cheetah.alphacapital.reportApp.fragment.EmpOverviewFragment;
import cheetah.alphacapital.reportApp.fragment.EmpProfileFragment;
import cheetah.alphacapital.reportApp.fragment.EmpTargetFragment;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.R;
import cheetah.alphacapital.databinding.ActivityEmployeeDetailsBinding;

public class EmployeeDetailsActivity extends BaseActivity
{

    private ActivityEmployeeDetailsBinding binding;

    private String[] tabsTitle = {
            "Overview",
            "Profile",
            "DAR Report",
            "AUM Report",
            "Target",
            "Notes"
    };

    private boolean isStatusBarHidden = false;

    private int employeeId = 0, selectedPosition = 0;
    private String employeeName = "";

    private boolean isForEmployeeLogin = false;

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

        if (getIntent().hasExtra("isForEmployeeLogin"))
        {
            isForEmployeeLogin = getIntent().getBooleanExtra("isForEmployeeLogin", false);
        }

        if (getIntent().hasExtra("employeeId"))
        {
            employeeId = getIntent().getIntExtra("employeeId", 0);
        }

        if (getIntent().hasExtra("pos"))
        {
            selectedPosition = getIntent().getIntExtra("pos", 0);
        }

        if (getIntent().hasExtra("employeeName"))
        {
            employeeName = getIntent().getStringExtra("employeeName");
        }

        binding = DataBindingUtil.setContentView(activity, R.layout.activity_employee_details);
        initView();
        setupPagerData();
    }

    private void initView()
    {
      /*  int height = 56;
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
        binding.toolbar.txtTitle.setText("Employee Details");
        binding.toolbar.llBackNavigation.setVisibility(View.VISIBLE);

        binding.toolbar.llBackNavigation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
                finishActivityAnimation();
            }
        });
    }

    private void setupPagerData()
    {
        TabAdapter mAdapter = new TabAdapter();
        binding.pager.setAdapter(mAdapter);
        binding.pager.setCurrentItem(selectedPosition);
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

        @Override
        public Fragment getItem(int position)
        {
            if (position == 0)
            {
                return new EmpOverviewFragment(employeeId, employeeName, isForEmployeeLogin);
            }
            else if (position == 1)
            {
                return new EmpProfileFragment(employeeId);
            }
            else if (position == 2)
            {
                return new EmpDARReportFragment(employeeId);
            }
            else if (position == 3)
            {
                return new EmpAUMReportFragment(employeeId);
            }
            else if (position == 4)
            {
                return new EmpTargetFragment(employeeId);
            }
            else
            {
                return new EmpNotesFragment(employeeId);
            }
        }

        @Override
        public int getCount()
        {
            return tabsTitle.length;
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return null;
        }
    }
}
