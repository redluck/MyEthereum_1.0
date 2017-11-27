package it.redluck.materialdesign.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import java.util.Calendar;
import java.util.Date;
import it.redluck.materialdesign.R;
import it.redluck.materialdesign.async_tasks.GetCoinbaseHistoricalJSONTask;

public class Fragment03 extends Fragment {

    //http://www.android-graphview.org/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_03, container, false);

        GetCoinbaseHistoricalJSONTask task = new GetCoinbaseHistoricalJSONTask(getActivity());
        task.execute();

        return rootView;
    }

    private void createGraph(View view) {

        //Elenco delle date da visualizzare sull'asse X
        /*Calendar calendar = Calendar.getInstance();
        Date d1 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d2 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d3 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d4 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d5 = calendar.getTime();*/

        //Creazione del grafico
        GraphView graph = (GraphView) view.findViewById(R.id.graph);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(new String[] {"08/08/2017 15:40", "08/08/2017 15:40", "08/08/2017 15:40", "08/08/2017 15:40", "08/08/2017 15:40"});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        graph.getGridLabelRenderer().setHorizontalLabelsAngle(90);

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(1, 1),
                new DataPoint(2, 5),
                new DataPoint(3, 3),
                new DataPoint(4, 3),
                new DataPoint(5, 7),
        });
        graph.addSeries(series);

        //Formattiamo le label delle date
        //graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
        //Impostiamo l'angolo delle label
        //graph.getGridLabelRenderer().setHorizontalLabelsAngle(90);
        //Impostiamo il numero di label da visualizzare
        //graph.getGridLabelRenderer().setNumHorizontalLabels(3);

        //Impostiamo manualmente il limite dati
        /*graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(d1.getTime());
        graph.getViewport().setMaxX(d5.getTime());*/

        //Rendiamo il grafico scrollabile e zoomabile
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScrollableY(true);
        //graph.getViewport().setScalable(true);
        //graph.getViewport().setScalableY(true);
    }
}