package it.redluck.materialdesign.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.Collections;
import java.util.List;
import it.redluck.materialdesign.model.*;
import it.redluck.materialdesign.R;

public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder> {

    List<NavigationDrawerItem> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;

    /*----------------------------------------------------------------------------------------------------*
    | costruttore                                                                                         |
    *----------------------------------------------------------------------------------------------------*/
    public NavigationDrawerAdapter(Context context, List<NavigationDrawerItem> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    /*----------------------------------------------------------------------------------------------------*
    | onCreateViewHolder()                                                                                |
    *----------------------------------------------------------------------------------------------------*/
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.navigation_drawer_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    /*----------------------------------------------------------------------------------------------------*
    | onBindViewHolder()                                                                                  |
    *----------------------------------------------------------------------------------------------------*/
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NavigationDrawerItem current = data.get(position);
        holder.title.setText(current.getTitle());
    }

    /*----------------------------------------------------------------------------------------------------*
    | getItemCount()                                                                                      |
    *----------------------------------------------------------------------------------------------------*/
    @Override
    public int getItemCount() {
        return data.size();
    }

    /*====================================================================================================*
    | CLASS MyViewHolder                                                                                  |
    *====================================================================================================*/
    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }
}
