package cheetah.alphacapital.reportApp.activity.admin;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import cheetah.alphacapital.reportApp.fragment.DARReportFragment;
import cheetah.alphacapital.reportApp.fragment.EmpDARActivityAnalysisFragment;
import cheetah.alphacapital.reportApp.fragment.EmpDARClientAnalysisFragment;
import cheetah.alphacapital.reportApp.fragment.EmpDAREmployeeAnalysisFragment;
import cheetah.alphacapital.reportApp.getset.TaskReportResponseModel;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.utils.SessionManager;
import cheetah.alphacapital.R;
import cheetah.alphacapital.reportApp.getset.AllEmployeeResponse;


public class DailyActivityReportsDetailsActivity extends AppCompatActivity
{
    private Activity activity;
    private SessionManager sessionManager;
    private boolean isStatusBarHidden = false;
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
    private TabLayout tabs;
    private ViewPager viewpager;
    private String[] tabsTitle = {"DAR Report", "RM Analysis ", "Activity Analysis", "Client Analysis"};
    private TabAdapter mAdapter;
    private AllEmployeeResponse.DataBean.AllEmployeeBean getSet = new AllEmployeeResponse.DataBean.AllEmployeeBean();

    private Integer employeeId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
       /* try
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            {
                Window w = getWindow();
                w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
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

        setContentView(R.layout.activity_dar_details);

        getSet = getIntent().getExtras().getParcelable("EmployeeGetSet");

        if (getIntent().hasExtra("id"))
        {
            employeeId = getIntent().getIntExtra("id",0);
        }

        setupViews();

        onClickEvents();

        setupViewPager(viewpager);
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
            tabs = (TabLayout) findViewById(R.id.tabs);
            viewpager = (ViewPager) findViewById(R.id.viewpager);

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
            txtTitle.setText("Daily Activity Reports Details");
            llBackNavigation.setVisibility(View.VISIBLE);
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


    }

    private void setupViewPager(ViewPager viewPager)
    {
        try
        {
            mAdapter = new TabAdapter();
            viewPager.setAdapter(mAdapter);
            viewPager.setOffscreenPageLimit(4);
            tabs.setupWithViewPager(viewPager);
            setupTabIcons();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void setupTabIcons()
    {
        try
        {
            final View tabOne = LayoutInflater.from(activity).inflate(R.layout.rowview_tablayout, null);
            TextView txtTabOne = (TextView) tabOne.findViewById(R.id.txtTab);
            txtTabOne.setText(tabsTitle[0]);
            tabs.getTabAt(0).setCustomView(tabOne);

            View tabTwo = LayoutInflater.from(activity).inflate(R.layout.rowview_tablayout, null);
            TextView txtTabTwo = (TextView) tabTwo.findViewById(R.id.txtTab);
            txtTabTwo.setText(tabsTitle[1]);
            tabs.getTabAt(1).setCustomView(tabTwo);

            View tabThree = LayoutInflater.from(activity).inflate(R.layout.rowview_tablayout, null);
            TextView txtTabThree = (TextView) tabThree.findViewById(R.id.txtTab);
            txtTabThree.setText(tabsTitle[2]);
            tabs.getTabAt(2).setCustomView(tabThree);

            View tabFour = LayoutInflater.from(activity).inflate(R.layout.rowview_tablayout, null);
            TextView txtTabFour = (TextView) tabFour.findViewById(R.id.txtTab);
            txtTabFour.setText(tabsTitle[3]);
            tabs.getTabAt(3).setCustomView(tabFour);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    class TabAdapter extends FragmentPagerAdapter
    {

        public TabAdapter()
        {
            super(getSupportFragmentManager());
        }

        @Override
        public Fragment getItem(int position)
        {

            if(position == 0)
            {
                DARReportFragment darReportFragment = new DARReportFragment(employeeId);
                return darReportFragment;
            }
            else if(position == 1)
            {
                return new EmpDAREmployeeAnalysisFragment(employeeId,Integer.parseInt(AppUtils.getCurrentMonth()),Integer.parseInt(AppUtils.getCurrentYear()));
            }
            else if(position == 2)
            {
                return new EmpDARActivityAnalysisFragment(employeeId,Integer.parseInt(AppUtils.getCurrentMonth()),Integer.parseInt(AppUtils.getCurrentYear()));
            }
            else
            {
                return new EmpDARClientAnalysisFragment(employeeId,Integer.parseInt(AppUtils.getCurrentMonth()),Integer.parseInt(AppUtils.getCurrentYear()));
            }

            /*if (position == 0)
            {
                DARReportFragment darReportFragment = new DARReportFragment(getSet);
                return darReportFragment;
            }
            else if (position == 1)
            {
                DARReportFragment darReportFragment = new DARReportFragment(getSet);
                return darReportFragment;
            }
            else if (position == 2)
            {
                DARReportFragment darReportFragment = new DARReportFragment(getSet);
                return darReportFragment;
            }
            else
            {
                DARReportFragment darReportFragment = new DARReportFragment(getSet);
                return darReportFragment;
            }*/
        }

        @Override
        public int getCount()
        {
            return tabsTitle.length;
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return tabsTitle[position].toUpperCase();
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

