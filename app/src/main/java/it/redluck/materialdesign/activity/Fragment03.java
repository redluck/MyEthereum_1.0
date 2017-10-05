package it.redluck.materialdesign.activity;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import it.redluck.materialdesign.R;
import it.redluck.materialdesign.async_tasks.GetCoinbaseJSONTask2;

public class Fragment03 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_03, container, false);

        GetCoinbaseJSONTask2 task = new GetCoinbaseJSONTask2(getActivity());
        task.execute();

        return rootView;
    }
}