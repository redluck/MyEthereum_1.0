package it.redluck.materialdesign.activity;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.redluck.materialdesign.R;
import it.redluck.materialdesign.async_tasks.GetCoinmarketcapJSONTask;

public class Fragment01 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_01, container, false);

        GetCoinmarketcapJSONTask task = new GetCoinmarketcapJSONTask(getActivity());
        task.execute();

        return rootView;
    }
}