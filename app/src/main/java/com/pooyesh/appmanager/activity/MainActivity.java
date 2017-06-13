package com.pooyesh.appmanager.activity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;

import java.util.ArrayList;
import java.util.List;

import com.pooyesh.appmanager.R;
import com.pooyesh.appmanager.StarterApplication;
import com.pooyesh.appmanager.fragments.FragmentMovable;
import com.pooyesh.appmanager.fragments.FragmentMoved;
import com.pooyesh.appmanager.utils.AppPreferences;

public class MainActivity extends AppCompatActivity {
    private ViewPagerAdapter adapter;
    private FragmentMoved fragmentMoved;
    private FragmentMovable fragmentMovable;
    private EditText search;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private MenuItem menuItemSort, menuItemSerach;
    private AppPreferences appPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StartAppSDK.init(this, "203220624", true);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        search = new EditText(this);
        search.setHint("Enter search key");
        search.setHintTextColor(Color.WHITE);
        search.setTextColor(Color.WHITE);


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                StarterApplication.getInstance().trackEvent("Search", "Search", "Search");

                if (adapter.getItem(viewPager.getCurrentItem()) instanceof FragmentMovable) {
                    fragmentMovable.doSearch(search.getText().toString());
                    Log.d("ssss", search.getText().toString());
                } else if (adapter.getItem(viewPager.getCurrentItem()) instanceof FragmentMoved) {
                    fragmentMoved.doSearch(search.getText().toString());
                    Log.d("tttt", search.getText().toString());


                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        setSupportActionBar(toolbar);

        Toolbar.LayoutParams params = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.LEFT;

        Toolbar.LayoutParams searchParam = new Toolbar.LayoutParams(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        searchParam.gravity = Gravity.CENTER;

        toolbar.addView(search, searchParam);
        search.setVisibility(View.GONE);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        setupTabIcons();


    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menuItemSort = menu.findItem(R.id.action_sort);
        menuItemSerach = menu.findItem(R.id.action_search);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_search:
                if (search.getVisibility() == View.VISIBLE) {
                    search.setVisibility(View.GONE);
                    fragmentMoved.doSearch("");
                    fragmentMovable.doSearch("");
                    search.setText("");
                    menuItemSort.setVisible(true);
                    item.setIcon(R.drawable.ic_action_search);


                } else {
                    search.setVisibility(View.VISIBLE);
                    menuItemSort.setVisible(false);
                    item.setIcon(R.drawable.cross);
                }
                break;
            case R.id.action_sort:

                View menuItemView = findViewById(R.id.action_sort);
                PopupMenu popup = new PopupMenu(MainActivity.this, menuItemView);
                popup.getMenuInflater()
                        .inflate(R.menu.pop_up, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        appPreferences = new AppPreferences(getApplicationContext());

                        switch (item.getItemId()) {
                            case R.id.sort_name:
                                appPreferences.setSortModeOfApks("1");
                                StarterApplication.getInstance().trackEvent("Sort", "SortByName", "SortByName");
                                break;
                            case R.id.sort_size:
                                appPreferences.setSortModeOfApks("2");
                                StarterApplication.getInstance().trackEvent("Sort", "SortBySize", "SortBySize");

                                break;
                            case R.id.sort_date:
                                appPreferences.setSortModeOfApks("3");
                                StarterApplication.getInstance().trackEvent("Sort", "SortByInstalledDate", "SortByInstalledDate");

                                break;


                        }
                        int current = viewPager.getCurrentItem();
                        setupViewPager(viewPager);
                        viewPager.setCurrentItem(current);
                        return true;
                    }
                });

                popup.show();


        }
        return super.onOptionsItemSelected(item);
    }

    private void setupTabIcons() {
        TextView textView = new TextView(this);
        textView.setText("Movable");
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.WHITE);
        textView.setTypeface(Typeface.create("casual", Typeface.NORMAL));
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.in_card);
        LinearLayout view = new LinearLayout(this);
        view.setOrientation(LinearLayout.VERTICAL);
        view.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.tab_lay_icon), getResources().getDimensionPixelSize(R.dimen.tab_lay_icon));
        params.bottomMargin = getResources().getDimensionPixelSize(R.dimen.tab_icon_margin);
        view.addView(imageView, params);
        view.addView(textView);
        tabLayout.getTabAt(0).setCustomView(view);


        TextView textView1 = new TextView(this);
        textView1.setText("Moved");
        textView1.setGravity(Gravity.CENTER);
        textView1.setTextColor(Color.WHITE);
        textView1.setTypeface(Typeface.create("casual", Typeface.NORMAL));
        ImageView imageView1 = new ImageView(this);
        imageView1.setImageResource(R.drawable.sd_card);
        LinearLayout view1 = new LinearLayout(this);
        view1.setOrientation(LinearLayout.VERTICAL);
        view1.setGravity(Gravity.CENTER);
        params.bottomMargin = getResources().getDimensionPixelSize(R.dimen.tab_icon_margin);
        view1.addView(imageView1, params);
        view1.addView(textView1);
        tabLayout.getTabAt(1).setCustomView(view1);

    }

    private void setupViewPager(ViewPager viewPager) {
        if (fragmentMoved == null)
            fragmentMoved = new FragmentMoved();
        if (fragmentMovable == null)
            fragmentMovable = new FragmentMovable();
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(fragmentMovable, "Movable");
        adapter.addFragment(fragmentMoved, "Moved");
        viewPager.setAdapter(adapter);

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (search.getVisibility() == View.VISIBLE) {
            search.setVisibility(View.GONE);
            fragmentMoved.doSearch("");
            fragmentMovable.doSearch("");
            search.setText("");
            menuItemSort.setVisible(true);
            menuItemSerach.setIcon(R.drawable.ic_action_search);

        }
        StartAppAd.onBackPressed(this);
    }
}
