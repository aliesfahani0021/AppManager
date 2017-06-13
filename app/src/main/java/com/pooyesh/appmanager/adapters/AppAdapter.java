package com.pooyesh.appmanager.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.pooyesh.appmanager.AppInfo;
import com.pooyesh.appmanager.R;
import com.pooyesh.appmanager.Services.FloatingWindow;
import com.pooyesh.appmanager.StarterApplication;
import com.pooyesh.appmanager.utils.AppPreferences;
import com.pooyesh.appmanager.utils.UtilsApplication;


public class AppAdapter extends RecyclerView.Adapter<AppAdapter.AppViewHolder> {
    private Context ctx;
    private List<AppInfo> appList;
    private AppPreferences appPreferences;


    public AppAdapter(List<AppInfo> appList, Context ctx) {
        this.appList = appList;
        this.ctx = ctx;
        this.appPreferences = StarterApplication.getAppPreferences();

    }

    @Override
    public int getItemCount() {
        return appList == null ? 0 : appList.size();
    }

    public void clear() {
        appList.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(AppViewHolder appViewHolder, int i) {
        AppPreferences appPreferences = new AppPreferences(ctx);
        String sortMode = appPreferences.getSortModeOfApks();


        AppInfo appInfo = appList.get(i);
        appViewHolder.txtName.setText(appInfo.getName());
        appViewHolder.txtApk.setText(appInfo.getAPK());
        if (sortMode.equals("2")) {
            appViewHolder.txtSize.setTypeface(null, Typeface.BOLD);
        } else if (sortMode.equals("3")) {
            appViewHolder.txtInstallationDate.setTypeface(null, Typeface.BOLD);

        }
        appViewHolder.txtSize.setText(appInfo.getSize());
        appViewHolder.txtInstallationDate.setText(appInfo.getInstallationDate());
        appViewHolder.imgIcon.setImageDrawable(appInfo.getIcon());

        setButtonEvent(appViewHolder, appInfo);

    }

    private void setButtonEvent(AppViewHolder appViewHolder, final AppInfo appInfo) {
        TextView appExtract = appViewHolder.txtExtract;
        final TextView appShare = appViewHolder.txtShare;
        final ImageView appIcon = appViewHolder.imgIcon;
        final CardView cardView = appViewHolder.cardView;


        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGingerbreadInstalledAppDetailsActivity(appInfo.getAPK(), view.getContext());
                ctx.startService(new Intent(ctx, FloatingWindow.class));
                StarterApplication.getInstance().trackEvent("Move","MoveApp","MoveApp");


            }
        });
        appExtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UtilsApplication.copyFilesToAplace(appInfo);
                Snackbar snackbar = Snackbar
                        .make(view, "File Created\r\nstorage/emulated/0/AppManager ", Snackbar.LENGTH_LONG)
                        .setAction("Open", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openFolder();
                            }
                        });
                snackbar.setActionTextColor(Color.RED);
                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(ctx.getResources().getColor(R.color.colorAccent));
                snackbar.show();
                StarterApplication.getInstance().trackEvent("Extract","ExtractApp","ExtractApp");

            }
        });
        appShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UtilsApplication.copyFilesToAplace(appInfo);
                Intent shareIntent = UtilsApplication.getShareIntent(UtilsApplication.getOutputFilenames(appInfo));
                ctx.startActivity(Intent.createChooser(shareIntent, String.format(ctx.getResources().getString(R.string.send_to), appInfo.getName())));
                StarterApplication.getInstance().trackEvent("Share","ShareApp","ShareApp");


            }
        });
    }

    public void openFolder()
    {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()
                + "/AppManager/");
        intent.setDataAndType(uri, "*/*");
        ctx.startActivity(Intent.createChooser(intent, "Open folder"));
    }


    public boolean startGingerbreadInstalledAppDetailsActivity(String packagename, Context con) {
        boolean result = false;
        Intent i = new Intent();
        i.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + packagename));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            con.startActivity(i);
            result = true;

        } catch (Exception ex) {
            result = false;
        }

        return result;
    }

    @Override
    public AppViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View appAdapterView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.app_layout, viewGroup, false);
        return new AppViewHolder(appAdapterView);
    }

    public static class AppViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtName;
        protected TextView txtApk;
        protected TextView txtSize;
        protected TextView txtInstallationDate;
        protected TextView txtExtract;
        protected TextView txtShare;
        protected ImageView imgIcon;
        protected CardView cardView;

        public AppViewHolder(View v) {
            super(v);
            txtName = (TextView) v.findViewById(R.id.txtName);
            txtApk = (TextView) v.findViewById(R.id.txtApk);
            txtSize = (TextView) v.findViewById(R.id.txtSize);
            txtInstallationDate = (TextView) v.findViewById(R.id.txtInstallationDate);
            imgIcon = (ImageView) v.findViewById(R.id.imgIcon);
            txtExtract = (TextView) v.findViewById(R.id.btnExtract);
            txtShare = (TextView) v.findViewById(R.id.btnShare);
            cardView = (CardView) v.findViewById(R.id.app_card);


        }
    }

}
