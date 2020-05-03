package app.openconnect;

import android.content.Context;
import android.net.VpnService;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

public class AppFilterTools {
    public static List<String> getDisabledPackages(Context mContext){
        String disabledAppsStr = SharedPreferenceHelper.getSharedPreferenceString(mContext,"DISABLED_APPS","");
        String[] disabledApps = disabledAppsStr.split(",");
        List<String> lstDisabled = new ArrayList<>();
        for (String disabledApp : disabledApps) {
            if (!disabledApp.equals(""))
                lstDisabled.add(disabledApp);
        }
        return lstDisabled;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void configurePerApp(Context mContext , VpnService.Builder builder){
        List<String> disabledApps = AppFilterTools.getDisabledPackages(mContext);
        for (String app :
                disabledApps) {
            try {
                builder.addDisallowedApplication(app);
            } catch (Exception ignored) {

            }
        }
    }
    public static List<InstalledPackage> getInstalledApps(Context context,boolean getSysPackages) {
        List<InstalledPackage> res = new ArrayList<>();
        List<android.content.pm.PackageInfo> packs = context.getPackageManager().getInstalledPackages(0);
        for(int i=0;i<packs.size();i++) {
            android.content.pm.PackageInfo p = packs.get(i);
            if ((!getSysPackages) && (p.versionName == null)) {
                continue ;
            }
            if(p.packageName.equals(context.getPackageName()))
                continue;
            InstalledPackage newInfo = new InstalledPackage();
            newInfo.appname = p.applicationInfo.loadLabel( context.getPackageManager()).toString();
            newInfo.pname = p.packageName;
            newInfo.versionName = p.versionName;
            newInfo.versionCode = p.versionCode;
            newInfo.icon = p.applicationInfo.loadIcon( context.getPackageManager());
            res.add(newInfo);
        }
        return res;
    }
}
