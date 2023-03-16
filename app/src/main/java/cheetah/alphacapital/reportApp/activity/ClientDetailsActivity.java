package cheetah.alphacapital.reportApp.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import butterknife.BindView;
import butterknife.ButterKnife;
import cheetah.alphacapital.reportApp.activity.admin.BaseActivity;
import cheetah.alphacapital.reportApp.fragment.AssignEmployeeSecondaryFragment;
import cheetah.alphacapital.reportApp.fragment.ClientAUMFragment;
import cheetah.alphacapital.reportApp.fragment.ClientDetailsDocumentFragment;
import cheetah.alphacapital.reportApp.fragment.ClientDetailsToDoFragment;
import cheetah.alphacapital.reportApp.fragment.ClientOverViewFragment;
import cheetah.alphacapital.textutils.CustomTextViewMedium;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.R;
import cheetah.alphacapital.reportApp.fragment.AssignEmployeeFragment;
import cheetah.alphacapital.reportApp.fragment.ClientDARFragment;
import cheetah.alphacapital.reportApp.getset.ClientListResponse;


public class ClientDetailsActivity extends BaseActivity
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
    @BindView(R.id.cvCard)
    CardView cvCard;
    TabLayout tabs;
    ViewPager viewpager;
    TextView txtTitle;
    EditText edtSearch;
    ImageView ivSerach;
    ImageView ivClose;
    LinearLayout llCalender;
    LinearLayout llFilter;

    private boolean isStatusBarHidden = false;
    private String[] tabsTitle = {"Overview", "Primary Assign Employee","Secondary Assign Employee" , "AUM", "DAR", "To Do", "Document"};
    private TabAdapter mAdapter;
    private String clientid = "";
    private ClientListResponse.DataBean getSet = new ClientListResponse.DataBean();
    boolean isSearchVisibleAssign = false,isSearchVisibleTodo = false,isSearchVisibleDAR = false;
    String serach_text_assign = "",serach_text_todo = "",serach_text_dar = "";


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

        setContentView(R.layout.activity_client_details);

        ButterKnife.bind(this);

        clientid = getIntent().getExtras().getString("clientid");

        getSet = getIntent().getExtras().getParcelable("ClientInfo");

        setupViews();

        onClickEvents();

        setupViewPager(viewpager);
    }

    private void setupViews()
    {
        try
        {
           /* setSupportActionBar(toolbar);
            ivHeader.setImageResource(R.drawable.img_portfolio);
            int height = 56;
            if (isStatusBarHidden)
            {
                height = 56 + 25;
                viewStatusBar.setVisibility(View.INVISIBLE);
            }
            else
            {
                viewStatusBar.setVisibility(View.GONE);
            }
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) ivHeader.getLayoutParams();
            params.height = (int) AppUtils.pxFromDp(activity, height);
            ivHeader.setLayoutParams(params);*/

            ivLogo.setVisibility(View.GONE);
            llNotification.setVisibility(View.GONE);
            llBackNavigation.setVisibility(View.VISIBLE);

            tabs = (TabLayout) findViewById(R.id.tabs);
            viewpager = (ViewPager) findViewById(R.id.viewpager);
            txtTitle = (TextView) findViewById(R.id.txtTitle);
            edtSearch = (EditText) findViewById(R.id.edtSearch);
            ivSerach = (ImageView) findViewById(R.id.ivSerach);
            ivClose = (ImageView) findViewById(R.id.ivClose);
            llCalender = (LinearLayout) findViewById(R.id.llCalender);
            llFilter = (LinearLayout) findViewById(R.id.llFilter);

            txtTitle.setText("Client Details");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void onClickEvents()
    {
        llBackNavigation.setOnClickListener(v -> {
            try
            {
                isSearchVisibleAssign = false;
                serach_text_assign = "";
                isSearchVisibleTodo = false;
                serach_text_todo = "";
                isSearchVisibleDAR = false;
                serach_text_dar = "";
                AppUtils.hideKeyboard(edtSearch, activity);
                activity.finish();
                //activity.overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });

        llFilter.setOnClickListener(v -> {
            try
            {
                if (ClientDetailsToDoFragment.handler != null)
                {
                    Message msgObj = Message.obtain();
                    msgObj.what = 333;
                    ClientDetailsToDoFragment.handler.sendMessage(msgObj);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });

        llCalender.setOnClickListener(view -> {
            try
            {
                if (ClientDetailsToDoFragment.handler != null)
                {
                    Message msgObj = Message.obtain();
                    msgObj.what = 444;
                    ClientDetailsToDoFragment.handler.sendMessage(msgObj);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });

        ivSerach.setOnClickListener(v -> doSearch());

        ivClose.setOnClickListener(v -> closeSearch());

        edtSearch.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3)
            {
                try
                {
                    cs = edtSearch.getText().toString().trim();

                    if (cs.length() > 0)
                    {
                        if (viewpager.getCurrentItem() == 1)
                        {
                            serach_text_assign = cs.toString().trim();
                            if (AssignEmployeeFragment.handler != null)
                            {
                                Message msgObj = Message.obtain();
                                msgObj.obj = cs;
                                msgObj.what = 111;
                                AssignEmployeeFragment.handler.sendMessage(msgObj);
                            }
                        }

                        if (viewpager.getCurrentItem() == 3)
                        {
                            serach_text_dar = cs.toString().trim();
                            if (ClientDARFragment.handler != null)
                            {
                                Message msgObj = Message.obtain();
                                msgObj.obj = cs;
                                msgObj.what = 111;
                                ClientDARFragment.handler.sendMessage(msgObj);
                            }
                        }

                        if (viewpager.getCurrentItem() == 4)
                        {
                            serach_text_todo = cs.toString().trim();
                            if (ClientDetailsToDoFragment.handler != null)
                            {
                                Message msgObj = Message.obtain();
                                msgObj.obj = cs;
                                msgObj.what = 111;
                                ClientDetailsToDoFragment.handler.sendMessage(msgObj);
                            }
                        }
                    }
                    else
                    {
                        if (viewpager.getCurrentItem() == 1)
                        {
                            serach_text_assign = "";
                            if (AssignEmployeeFragment.handler != null)
                            {
                                Message msgObj = Message.obtain();
                                msgObj.obj = "";
                                msgObj.what = 111;
                                AssignEmployeeFragment.handler.sendMessage(msgObj);
                            }
                        }

                        if (viewpager.getCurrentItem() == 3)
                        {
                            serach_text_dar = "";
                            if (ClientDARFragment.handler != null)
                            {
                                Message msgObj = Message.obtain();
                                msgObj.obj = "";
                                msgObj.what = 111;
                                ClientDARFragment.handler.sendMessage(msgObj);
                            }
                        }

                        if (viewpager.getCurrentItem() == 4)
                        {
                            serach_text_todo = "";
                            if (ClientDetailsToDoFragment.handler != null)
                            {
                                Message msgObj = Message.obtain();
                                msgObj.obj = "";
                                msgObj.what = 111;
                                ClientDetailsToDoFragment.handler.sendMessage(msgObj);
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
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3)
            {
            }

            @Override
            public void afterTextChanged(Editable arg0)
            {
            }
        });

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {

            }

            @Override
            public void onPageSelected(int position)
            {
                try
                {
                    if (position == 1)
                    {
                        edtSearch.setHint("Search Employee..");
                        Message msg = new Message();
                        msg.what = 112;
                        msg.obj = "";
                        msg.arg1 = 0;
                        msg.arg2 = 0;
                        AssignEmployeeFragment.handler.sendMessage(msg);

                        if (isSearchVisibleAssign)
                        {
                            edtSearch.setVisibility(View.VISIBLE);
                            cvCard.setVisibility(View.VISIBLE);
                            ivClose.setVisibility(View.VISIBLE);
                            txtTitle.setVisibility(View.GONE);
                            ivSerach.setVisibility(View.GONE);
                            llFilter.setVisibility(View.GONE);
                            llCalender.setVisibility(View.GONE);
                            edtSearch.setText(serach_text_assign);
                            edtSearch.setSelection(edtSearch.getText().length());
                        }
                        else
                        {
                            txtTitle.setVisibility(View.VISIBLE);
                            ivSerach.setVisibility(View.VISIBLE);
                            edtSearch.setVisibility(View.GONE);
                            cvCard.setVisibility(View.GONE);
                            ivClose.setVisibility(View.GONE);
                            llFilter.setVisibility(View.GONE);
                            llCalender.setVisibility(View.GONE);
                        }
                    }
                    else if (position == 2)
                    {
                        edtSearch.setHint("Search Employee..");
                        Message msg = new Message();
                        msg.what = 112;
                        msg.obj = "";
                        msg.arg1 = 0;
                        msg.arg2 = 0;
                        AssignEmployeeSecondaryFragment.handler.sendMessage(msg);
                        if (isSearchVisibleAssign)
                        {
                            edtSearch.setVisibility(View.VISIBLE);
                            cvCard.setVisibility(View.VISIBLE);
                            ivClose.setVisibility(View.VISIBLE);
                            txtTitle.setVisibility(View.GONE);
                            ivSerach.setVisibility(View.GONE);
                            llFilter.setVisibility(View.GONE);
                            llCalender.setVisibility(View.GONE);
                            edtSearch.setText(serach_text_assign);
                            edtSearch.setSelection(edtSearch.getText().length());
                        }
                        else
                        {
                            txtTitle.setVisibility(View.VISIBLE);
                            ivSerach.setVisibility(View.VISIBLE);
                            edtSearch.setVisibility(View.GONE);
                            cvCard.setVisibility(View.GONE);
                            ivClose.setVisibility(View.GONE);
                            llFilter.setVisibility(View.GONE);
                            llCalender.setVisibility(View.GONE);
                        }
                    }
                    else if (position == 3)
                    {
                        edtSearch.setHint("Search DAR message..");

                        if (isSearchVisibleDAR)
                        {
                            edtSearch.setVisibility(View.VISIBLE);
                            cvCard.setVisibility(View.VISIBLE);
                            ivClose.setVisibility(View.VISIBLE);
                            txtTitle.setVisibility(View.GONE);
                            ivSerach.setVisibility(View.GONE);
                            llFilter.setVisibility(View.GONE);
                            llCalender.setVisibility(View.GONE);
                            edtSearch.setText(serach_text_dar);
                            edtSearch.setSelection(edtSearch.getText().length());
                        }
                        else
                        {
                            txtTitle.setVisibility(View.VISIBLE);
                            ivSerach.setVisibility(View.VISIBLE);
                            edtSearch.setVisibility(View.GONE);
                            cvCard.setVisibility(View.GONE);
                            ivClose.setVisibility(View.GONE);
                            llFilter.setVisibility(View.GONE);
                            llCalender.setVisibility(View.GONE);
                        }
                    }
                    else if (position == 4)
                    {
                        edtSearch.setHint("Search Task..");
                        Log.e("<><> ToDo Status", isSearchVisibleTodo + "");
                        if (isSearchVisibleTodo)
                        {
                            edtSearch.setVisibility(View.VISIBLE);
                            cvCard.setVisibility(View.VISIBLE);
                            ivClose.setVisibility(View.VISIBLE);
                            txtTitle.setVisibility(View.GONE);
                            ivSerach.setVisibility(View.GONE);
                            llFilter.setVisibility(View.GONE);
                            llCalender.setVisibility(View.GONE);
                            edtSearch.setText(serach_text_todo);
                            edtSearch.setSelection(edtSearch.getText().length());
                        }
                        else
                        {
                            txtTitle.setVisibility(View.VISIBLE);
                            ivSerach.setVisibility(View.VISIBLE);
                            edtSearch.setVisibility(View.GONE);
                            cvCard.setVisibility(View.GONE);
                            ivClose.setVisibility(View.GONE);
                            llFilter.setVisibility(View.VISIBLE);
                            llCalender.setVisibility(View.VISIBLE);
                        }
                    }
                    else
                    {
                        AppUtils.hideKeyboard(edtSearch, activity);
                        txtTitle.setVisibility(View.VISIBLE);
                        ivSerach.setVisibility(View.GONE);
                        ivClose.setVisibility(View.GONE);
                        edtSearch.setVisibility(View.GONE);
                        cvCard.setVisibility(View.GONE);
                        llFilter.setVisibility(View.GONE);
                        llCalender.setVisibility(View.GONE);
                    }

                    Log.e("<><> Assign Status", isSearchVisibleAssign + "");
                    Log.e("<><> ToDo Status", isSearchVisibleTodo + "");
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {
            }
        });
    }

    private void doSearch()
    {
        txtTitle.setVisibility(View.GONE);
        ivSerach.setVisibility(View.GONE);
        edtSearch.setVisibility(View.VISIBLE);
        cvCard.setVisibility(View.VISIBLE);
        ivClose.setVisibility(View.VISIBLE);
        llCalender.setVisibility(View.GONE);
        llFilter.setVisibility(View.GONE);
        edtSearch.setText("");

        if (viewpager.getCurrentItem() == 1)
        {
            isSearchVisibleAssign = true;
        }
        else if(viewpager.getCurrentItem() ==4)
        {
            isSearchVisibleTodo = true;
        }
        else
        {
            isSearchVisibleDAR =true;
        }

        //MitsUtils.openKeyboard(edtSearch, activity);
    }

    private void closeSearch()
    {
        txtTitle.setVisibility(View.VISIBLE);
        ivSerach.setVisibility(View.VISIBLE);
        edtSearch.setVisibility(View.GONE);
        cvCard.setVisibility(View.GONE);
        ivClose.setVisibility(View.GONE);
        edtSearch.setText("");
        if(viewpager.getCurrentItem() ==1)
        {
            isSearchVisibleAssign = false;
            llCalender.setVisibility(View.GONE);
            llFilter.setVisibility(View.GONE);
        }
        else if (viewpager.getCurrentItem() == 4)
        {
            isSearchVisibleTodo = false;
            llCalender.setVisibility(View.VISIBLE);
            llFilter.setVisibility(View.VISIBLE);
        }
        else
        {
            isSearchVisibleDAR = false;
            llCalender.setVisibility(View.GONE);
            llFilter.setVisibility(View.GONE);
        }

        //MitsUtils.hideKeyboard(activity);
        hideKeyboard();
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

            View tabThreeNew = LayoutInflater.from(activity).inflate(R.layout.rowview_tablayout, null);
            TextView txtTabThreeNew = (TextView) tabThreeNew.findViewById(R.id.txtTab);
            txtTabThreeNew.setText(tabsTitle[2]);
            tabs.getTabAt(2).setCustomView(tabThreeNew);

            View tabThree = LayoutInflater.from(activity).inflate(R.layout.rowview_tablayout, null);
            TextView txtTabThree = (TextView) tabThree.findViewById(R.id.txtTab);
            txtTabThree.setText(tabsTitle[2]);
            tabs.getTabAt(3).setCustomView(tabThree);

            View tabFour = LayoutInflater.from(activity).inflate(R.layout.rowview_tablayout, null);
            TextView txtTabFour = (TextView) tabFour.findViewById(R.id.txtTab);
            txtTabFour.setText(tabsTitle[3]);
            tabs.getTabAt(4).setCustomView(tabFour);

            View tabFive = LayoutInflater.from(activity).inflate(R.layout.rowview_tablayout, null);
            TextView txtTabFive = (TextView) tabFive.findViewById(R.id.txtTab);
            txtTabFive.setText(tabsTitle[4]);
            tabs.getTabAt(5).setCustomView(tabFive);

            View tabSix = LayoutInflater.from(activity).inflate(R.layout.rowview_tablayout, null);
            TextView txtTabSix = (TextView) tabSix.findViewById(R.id.txtTab);
            txtTabSix.setText(tabsTitle[5]);
            tabs.getTabAt(6).setCustomView(tabSix);

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
            if (position == 0)
            {
                Bundle bundle = new Bundle();
                bundle.putString("clientid", clientid);
                ClientOverViewFragment clientOverViewFragment = new ClientOverViewFragment();
                clientOverViewFragment.setArguments(bundle);
                return clientOverViewFragment;
            }
            else if (position == 1)
            {
                Bundle bundle = new Bundle();
                bundle.putString("clientid", clientid);
                AssignEmployeeFragment assignEmployeeFragment = new AssignEmployeeFragment();
                assignEmployeeFragment.setArguments(bundle);
                return assignEmployeeFragment;
            }
            else if (position == 2)
            {
                Bundle bundle = new Bundle();
                bundle.putString("clientid", clientid);
                AssignEmployeeSecondaryFragment assignEmployeeFragment = new AssignEmployeeSecondaryFragment();
                assignEmployeeFragment.setArguments(bundle);
                return assignEmployeeFragment;
            }
            else if (position == 3)
            {
                Bundle bundle = new Bundle();
                bundle.putString("clientid", clientid);
                ClientAUMFragment clientAUMFragment = new ClientAUMFragment();
                clientAUMFragment.setArguments(bundle);
                return clientAUMFragment;
            }
            else if (position == 4)
            {
                Bundle bundle = new Bundle();
                bundle.putString("clientid", clientid);
                ClientDARFragment clientDARFragment = new ClientDARFragment();
                clientDARFragment.setArguments(bundle);
                return clientDARFragment;
            }
            else if (position == 5)
            {

                Bundle bundle = new Bundle();
                bundle.putString("clientid", clientid);
                ClientDetailsToDoFragment clientDetailsToDoFragment = new ClientDetailsToDoFragment();
                clientDetailsToDoFragment.setArguments(bundle);
                return clientDetailsToDoFragment;
            }
            else
            {
                Bundle bundle = new Bundle();
                bundle.putString("clientid", clientid);
                ClientDetailsDocumentFragment clientDetailsToDoFragment = new ClientDetailsDocumentFragment();
                clientDetailsToDoFragment.setArguments(bundle);
                return clientDetailsToDoFragment;
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
            return tabsTitle[position].toUpperCase();
        }
    }

    @Override
    public void onBackPressed()
    {
        try
        {
            isSearchVisibleAssign = false;
            serach_text_assign = "";
            isSearchVisibleTodo = false;
            serach_text_todo = "";
            isSearchVisibleDAR = false;
            serach_text_dar = "";
            AppUtils.hideKeyboard(edtSearch, activity);
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

