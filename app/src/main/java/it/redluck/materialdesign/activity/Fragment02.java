package it.redluck.materialdesign.activity;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import it.redluck.materialdesign.R;
import it.redluck.materialdesign.async_tasks.GetCoinbaseJSONTask;

public class Fragment02 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_02, container, false);

        GetCoinbaseJSONTask task = new GetCoinbaseJSONTask(getActivity());
        task.execute();

        return rootView;
    }
}