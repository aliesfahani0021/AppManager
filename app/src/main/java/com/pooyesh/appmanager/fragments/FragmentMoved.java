package com.pooyesh.appmanager.fragments;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pnikosis.materialishprogress.ProgressWheel;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.pooyesh.appmanager.AppInfo;
import com.pooyesh.appmanager.R;
import com.pooyesh.appmanager.StarterApplication;
import com.pooyesh.appmanager.adapters.AppAdapter;
import com.pooyesh.appmanager.utils.AppPreferences;


public class FragmentMoved extends Fragment {

    public FragmentMoved() {
    }

    private AppPreferences appPreferences;
    private ArrayList<AppInfo> appList;
    private AppAdapter appAdapter;
    private RecyclerView recyclerView;
    private ProgressWheel progressWheel;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movable, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.appList);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.pull_to_refresh);
        progressWheel = (ProgressWheel) view.findViewById(R.id.progress);
        appPreferences = new AppPreferences(getActivity());

        mSwipeRefreshLayout.setEnabled(false);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        progressWheel.setVisibility(View.VISIBLE);

        setInitialConfiguration();
        new getInstalledApps().execute();
        StarterApplication.getInstance().trackEvent("Fragment", "FragmentMoved", "FragmentMoved");
        return view;
    }

    private void setInitialConfiguration() {
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.app_name);
        }
    }

    class getInstalledApps extends AsyncTask<Void, String, Void> {
        public getInstalledApps() {
            appList = new ArrayList<>();

        }

        @Override
        protected Void doInBackground(Void... params) {
            final PackageManager packageManager = getContext().getPackageManager();
            List<PackageInfo> packages = packageManager.getInstalledPackages(PackageManager.GET_META_DATA);
            switch (appPreferences.getSortModeOfApks()) {
                case "1":
                    Collections.sort(packages, new Comparator<PackageInfo>() {
                        @Override
                        public int compare(PackageInfo p1, PackageInfo p2) {
                            return p1.packageName.toLowerCase().compareTo(p2.packageName.toLowerCase());
                        }
                    });
                    break;
                case "2":
                    Collections.sort(packages, new Comparator<PackageInfo>() {
                        @Override
                        public int compare(PackageInfo p1, PackageInfo p2) {
                            Long size1 = new File(p1.applicationInfo.sourceDir).length();
                            Long size2 = new File(p2.applicationInfo.sourceDir).length();
                            return size2.compareTo(size1);
                        }
                    });
                    break;
                case "3":
                    Collections.sort(packages, new Comparator<PackageInfo>() {
                        @Override
                        public int compare(PackageInfo p1, PackageInfo p2) {
                            return Long.toString(p2.firstInstallTime).compareTo(Long.toString(p1.firstInstallTime));
                        }
                    });
                    break;
            }

            for (PackageInfo packageInfo : packages) {
                Long p = new File(packageInfo.applicationInfo.sourceDir).length();
                float sizeOf = p;
                sizeOf = (sizeOf / 1024) / 1024;

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                String d = sdf.format(new Date(packageInfo.firstInstallTime));

                ApplicationInfo ai = packageInfo.applicationInfo;
                if (((ai.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == ApplicationInfo.FLAG_EXTERNAL_STORAGE)) {
                    if (packageInfo.installLocation == PackageInfo.INSTALL_LOCATION_AUTO
                            || packageInfo.installLocation == PackageInfo.INSTALL_LOCATION_PREFER_EXTERNAL) {
                        try {
                            AppInfo tempApp = new AppInfo(packageManager.getApplicationLabel(packageInfo.applicationInfo).toString(), packageInfo.packageName, packageInfo.versionName, packageInfo.applicationInfo.sourceDir, packageInfo.applicationInfo.dataDir, String.valueOf(sizeOf).substring(0, 4) + " mb", d, packageManager.getApplicationIcon(packageInfo.applicationInfo), false);
                            appList.add(tempApp);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... progress) {
            super.onProgressUpdate(progress);
            progressWheel.setProgress(Float.parseFloat(progress[0]));
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (appList != null && !appList.isEmpty())
                appAdapter = new AppAdapter(appList, getActivity());
            recyclerView.setAdapter(appAdapter);
            mSwipeRefreshLayout.setEnabled(true);
            mSwipeRefreshLayout.setColorSchemeColors(getActivity().getResources().getColor(R.color.colorPrimary));
            progressWheel.setVisibility(View.GONE);
            setPullToRefreshViewForUpdateList(mSwipeRefreshLayout);
        }

    }

    private void setPullToRefreshViewForUpdateList(final SwipeRefreshLayout swipeRefreshLayout) {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                appAdapter.clear();
                recyclerView.setAdapter(null);
                new getInstalledApps().execute();

                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1200);
            }
        });
    }

    public void doSearch(String text) {
        if (appList != null && !appList.isEmpty()) {
            appAdapter = new AppAdapter(getSearchResult(text), getActivity());
            recyclerView.setAdapter(appAdapter);
        }
    }

    private List<AppInfo> getSearchResult(String text) {
        List<AppInfo> result = new ArrayList<>();
        for (AppInfo appInfo : appList)
            if (appInfo.getName().toLowerCase().contains(text.toLowerCase()))
                result.add(appInfo);
        return result;
    }
}
