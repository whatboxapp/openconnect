package app.openconnect;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import static app.openconnect.AppFilterTools.getDisabledPackages;

public class AppFilterAdapter extends RecyclerView.Adapter<AppFilterAdapter.PerAppViewHolder> implements Filterable {
    @Override
    public Filter getFilter() {
        if(this.filter == null )
            this.filter = new AppFilter(mPackages,this);
        return this.filter;
    }
    private AppFilter filter;
    public void checkAll() {
        disabledPackages.clear();
        setDisabledPackages(disabledPackages);
        notifyDataSetChanged();
    }

    public void uncheckAll() {
        disabledPackages = getInstalledApps(false);
        setDisabledPackages(disabledPackages);
        notifyDataSetChanged();
    }
    private List<String> getInstalledApps(boolean getSysPackages) {
        List<String> res = new ArrayList<>();
        List<android.content.pm.PackageInfo> packs = mContext.getPackageManager().getInstalledPackages(0);
        for(int i=0;i<packs.size();i++) {
            android.content.pm.PackageInfo p = packs.get(i);
            if ((!getSysPackages) && (p.versionName == null)) {
                continue ;
            }
            if(p.packageName.equals(mContext.getPackageName()))
                continue;
            res.add(p.packageName);
        }
        return res;
    }


    public class PerAppViewHolder extends RecyclerView.ViewHolder {
        CheckBox chkAppName ;
        ImageView imgAppIcon ;
        public PerAppViewHolder(View itemView) {
            super(itemView);
            chkAppName = itemView.findViewById(R.id.chkAppName);
            imgAppIcon = itemView.findViewById(R.id.imgAppIcon);
        }
    }

    public void setPackages(List<InstalledPackage> mPackages) {
        this.mPackages = mPackages;
        notifyDataSetChanged();
    }

    private List<InstalledPackage> mPackages ;
    private Context mContext;
    private List<String> disabledPackages;
    public AppFilterAdapter(Context context,List<InstalledPackage> packages) {
        this.mPackages = packages;
        this.mContext = context;
        disabledPackages = getDisabledPackages(mContext);
    }

    @NonNull
    @Override
    public PerAppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.app_filter_row, parent, false);
        return new PerAppViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final PerAppViewHolder holder, int position) {
        Log.d("ViewHolderBinder", String.format("onBindViewHolder: %d : %d", holder.getAdapterPosition(),position) );
        holder.chkAppName.setText(mPackages.get(holder.getAdapterPosition()).appname);
        if(disabledPackages.contains(mPackages.get(holder.getAdapterPosition()).pname))
            holder.chkAppName.setChecked(false);
        else
            holder.chkAppName.setChecked(true);
        holder.imgAppIcon.setImageDrawable(mPackages.get(holder.getAdapterPosition()).icon);
        holder.chkAppName.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String packageName = mPackages.get(holder.getAdapterPosition()).pname;

            if(isChecked) {
                removeDisabledPackage(packageName);
            }else{
                Log.d("TAG", packageName);
                addDisabledPackage(packageName);
            }
        });
    }
    private void addDisabledPackage(String pname){
        if(!disabledPackages.contains(pname)) {
            disabledPackages.add(pname);
            setDisabledPackages(disabledPackages);
        }
    }
    private void removeDisabledPackage(String pname){
        if(disabledPackages.contains(pname)) {
            disabledPackages.remove(pname);
            setDisabledPackages(disabledPackages);
        }
    }
    private void setDisabledPackages(List<String> disabledPackages){
        StringBuilder result = new StringBuilder();
        for (String disabled :
                disabledPackages) {
            result.append(disabled);
            result.append(",");
        }
        SharedPreferenceHelper.setSharedPreferenceString(mContext,"DISABLED_APPS",result.toString());
    }

    @Override
    public int getItemCount() {
        return mPackages.size();
    }


}
