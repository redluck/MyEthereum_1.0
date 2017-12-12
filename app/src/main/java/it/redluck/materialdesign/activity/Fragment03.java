package it.redluck.materialdesign.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import it.redluck.materialdesign.R;
import it.redluck.materialdesign.async_tasks.GetCoinbaseHistoricalJSONTask;

public class Fragment03 extends Fragment {

    /*----------------------------------------------------------------------------------------------------*
	| onCreateView()                                                                                      |
	*----------------------------------------------------------------------------------------------------*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_03, container, false);
        GraphView graph = (GraphView) rootView.findViewById(R.id.graph);

        GetCoinbaseHistoricalJSONTask task = new GetCoinbaseHistoricalJSONTask(getActivity()) {
            //Facendo l'override del metodo onPostExecute(Result result) è possibile utilizzare
            //i valori ritornati dal task asincrono all'interno del fragment
            @Override
            protected void onPostExecute(ArrayList<HashMap<Date, Double>> result) {
                super.onPostExecute(result);

                /*for(int i = 0; i < arrayList.size(); i++) {
                    Log.d("MyLog", "1");
                    HashMap<String, Double> map = arrayList.get(i);
                    for(String key : map.keySet()) {
                        Log.d("MyLog", map.get(key).toString());
                    }
                }*/
            }
        };
        task.execute();

        createGraph2(rootView, result);

        return rootView;
    }

    private void createGraph2(View view, ArrayList<HashMap<Date, Double>> data) {

        Log.d("MyLog", data.toString());

        // generate Dates
        Calendar calendar = Calendar.getInstance();
        Date d1 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d2 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d3 = calendar.getTime();

        GraphView graph = (GraphView) view.findViewById(R.id.graph);

        // you can directly pass Date objects to DataPoint-Constructor
        // this will convert the Date to double via Date#getTime()
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(d1, 1),
                new DataPoint(d2, 5),
                new DataPoint(d3, 3)
        });

        graph.addSeries(series);

        // set date label formatter
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
        graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space

        // set manual x bounds to have nice steps
        graph.getViewport().setMinX(d1.getTime());
        graph.getViewport().setMaxX(d3.getTime());
        graph.getViewport().setXAxisBoundsManual(true);

        // as we use dates as labels, the human rounding to nice readable numbers
        // is not necessary
        graph.getGridLabelRenderer().setHumanRounding(false);
    }

    /*----------------------------------------------------------------------------------------------------*
	| createGraph() - http://www.android-graphview.org                                                    |
	*----------------------------------------------------------------------------------------------------*/
    private void createGraph(ArrayList<HashMap<String, Double>> data) {

        //Creiamo il grafico
        GraphView graph = (GraphView) getActivity().findViewById(R.id.graph);
        //Distanza grafico/etichette dal bordo della pagina
        graph.getGridLabelRenderer().setPadding(25);

        //Ricaviamo i valori di ogni coppia x/y
        String[] dates = new String[data.size()];
        DataPoint[] profits = new DataPoint[data.size()];
        for(int i=0; i<data.size(); i++){
            HashMap<String, Double> map = data.get(i);
            for(String key : map.keySet()) {
                dates[i] = key;
                profits[i] = new DataPoint(i+1, map.get(key));
            }
        }

        //Impostiamo le label x
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(dates);
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        //Posizioniamo le label x in verticale
        graph.getGridLabelRenderer().setHorizontalLabelsAngle(90);
        //Evitiamo che le label x, messe in verticale, si sovrappongano al grafico
        graph.getGridLabelRenderer().setLabelHorizontalHeight(400);
        //Visualizziamo solo 4 label x nella schermata
        //graph.getGridLabelRenderer().setNumHorizontalLabels(4);

        //Aggiungiamo i valori di ogni coppia x/y al grafico
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(profits);
        graph.addSeries(series);

        //Visualizziamo un punto cliccabile sul grafico per ogni coppia x/y
        series.setDrawDataPoints(true);
        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(getActivity(), "€ " + dataPoint.getY(), Toast.LENGTH_SHORT).show();
            }
        });

        //La schermata deve contenere solo 4 valori x per volta
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMaxX(4);

        //Rendiamo il grafico scrollabile in orizzontale e partiamo dalla fine
        graph.getViewport().setScrollable(true);
        graph.getViewport().scrollToEnd();
    }
}