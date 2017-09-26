package it.redluck.materialdesign.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import it.redluck.materialdesign.R;
import it.redluck.materialdesign.async_tasks.GetCoinbaseJSONTask;

public class Fragment01 extends Fragment {

    //View rappresentante il fragment_01
    private View fragment1;

    /*----------------------------------------------------------------------------------------------------*
    | onCreateView()                                                                                      |
    *----------------------------------------------------------------------------------------------------*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragment1 = inflater.inflate(R.layout.fragment_01, container, false);

        GetCoinbaseJSONTask task = new GetCoinbaseJSONTask(getActivity());
        task.execute();

        //Bottone in fondo alla pagina per aggiungere una nuova nota
        /*FloatingActionButton fab = (FloatingActionButton) fragment1.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), NewRecordActivity.class);
                startActivity(i);
            }
        });*/

        return fragment1;
    }

}