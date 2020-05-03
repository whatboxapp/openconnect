package app.openconnect;

import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

public class AppFilter extends Filter {
    AppFilterAdapter adapter;
    List<InstalledPackage> filterList;

    public AppFilter(List<InstalledPackage> filterList,AppFilterAdapter adapter)
    {
        this.adapter=adapter;
        this.filterList=filterList;

    }

    //FILTERING OCURS
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();
        if(constraint != null && constraint.length() > 0)
        {
            constraint=constraint.toString().toLowerCase();
            List<InstalledPackage> filteredPlayers=new ArrayList<>();

            for (int i=0;i<filterList.size();i++)
            {
                if(filterList.get(i).appname.toLowerCase().contains(constraint))
                {
                    filteredPlayers.add(filterList.get(i));
                }
            }

            results.count=filteredPlayers.size();
            results.values=filteredPlayers;
        }else
        {
            results.count=filterList.size();
            results.values=filterList;

        }

        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.setPackages((List<InstalledPackage>) results.values);
    }
}
