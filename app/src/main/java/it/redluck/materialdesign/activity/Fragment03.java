package it.redluck.materialdesign.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import it.redluck.materialdesign.R;
import it.redluck.materialdesign.async_tasks.GetCoinbaseHistoricalJSONTask;

public class Fragment03 extends Fragment {

    /*----------------------------------------------------------------------------------------------------*
	| onCreateView()                                                                                      |
	*----------------------------------------------------------------------------------------------------*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_03, container, false);

        GetCoinbaseHistoricalJSONTask task = new GetCoinbaseHistoricalJSONTask(getActivity());
        task.execute();

        return rootView;
    }
}