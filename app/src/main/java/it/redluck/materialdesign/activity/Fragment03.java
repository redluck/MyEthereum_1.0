package it.redluck.materialdesign.activity;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import it.redluck.materialdesign.R;


public class Fragment03 extends Fragment {

    //View rappresentante il fragment_03
    private View fragment3;

    /*----------------------------------------------------------------------------------------------------*
	| onCreateView()                                                                                      |
	*----------------------------------------------------------------------------------------------------*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragment3 = inflater.inflate(R.layout.fragment_03, container, false);

        //GetCoinbaseHistoricalJSONTask task = new GetCoinbaseHistoricalJSONTask(getActivity());
        //task.execute();

        //Ricaviamo i dati inviati al fragment corrente dalla MainActivity
        Bundle bundle = this.getArguments();
        if(bundle.getSerializable("data_for_graph") != null) {
            ArrayList<HashMap<Date, Double>> list = (ArrayList<HashMap<Date, Double>>) bundle.getSerializable("data_for_graph");
            Log.d("MyLog", list.toString());
            createGraph(list);
        }

        return fragment3;
    }

    /*----------------------------------------------------------------------------------------------------*
	| createGraph() - http://www.android-graphview.org                                                    |
	*----------------------------------------------------------------------------------------------------*/
    private void createGraph(ArrayList<HashMap<Date, Double>> data) {

        //Creiamo il grafico
        GraphView graph = (GraphView) fragment3.findViewById(R.id.graph);

        //Ricaviamo i valori di ogni coppia x/y
        DataPoint[] profits = new DataPoint[data.size()];
        for(int i=0; i<data.size(); i++){
            HashMap<Date, Double> map = data.get(i);
            for(Date key : map.keySet()) {
                profits[i] = new DataPoint(key, map.get(key));
            }
        }
        //Log.d("MyLog", data.toString());

        //Aggiungiamo i valori di ogni coppia x/y al grafico
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(profits);
        graph.addSeries(series);

        //Visualizziamo un punto cliccabile sul grafico per ogni coppia x/y
        series.setDrawDataPoints(true);
        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                //Convertiamo il double fornitoci in data
                Date myDate = new Date((long) dataPoint.getX());
                //Quindi formattiamo la data
                DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                String date = format.format(myDate);

                Toast.makeText(getActivity(), date + ": € " + dataPoint.getY(), Toast.LENGTH_SHORT).show();
            }
        });

        //Posizioniamo le label x in verticale
        graph.getGridLabelRenderer().setHorizontalLabelsAngle(90);
        //Evitiamo che le label x, messe in verticale, si sovrappongano al grafico
        graph.getGridLabelRenderer().setLabelHorizontalHeight(225);
        //Formattiamo le label x delle date
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
        //Visualizziamo un numero di label x
        //graph.getGridLabelRenderer().setNumHorizontalLabels(5);

        //La schermata deve contenere solo un tot valori x per volta
        graph.getViewport().setMinX(profits[0].getX());
        graph.getViewport().setMaxX(profits[4].getX());
        graph.getViewport().setXAxisBoundsManual(true);

        //Rendiamo il grafico scrollabile e scalabile in orizzontale e partiamo dalla fine
        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);
        graph.getViewport().scrollToEnd();

        //Impostiamo un colore di sfondo per la linea del grafico
        series.setDrawBackground(true);
        //I primi due numeri del colore indicano l'opacità
        series.setBackgroundColor(Color.parseColor("#55C1FFD5"));
    }
}