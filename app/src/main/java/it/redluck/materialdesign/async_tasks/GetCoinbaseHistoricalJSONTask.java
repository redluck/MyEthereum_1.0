package it.redluck.materialdesign.async_tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import it.redluck.materialdesign.R;
import it.redluck.materialdesign.model.Investment;
import it.redluck.materialdesign.utilities.DateTools;
import it.redluck.materialdesign.utilities.JSONParser;
import it.redluck.materialdesign.utilities.NewJSONParser;

import static java.lang.Double.parseDouble;


public class GetCoinbaseHistoricalJSONTask extends AsyncTask<Void, Void, ArrayList<HashMap<String, Double>>>{

	//La progress dialog
	private ProgressDialog progressDialog;
	//Il Context
	private Context context;

	//Url del JSON escluso il timestamp
	private static final String URL_JSON = "https://min-api.cryptocompare.com/data/pricehistorical?fsym=ETH&tsyms=EUR&e=Coinbase&ts=";
	//Nomi dei nodi del JSON
    private static final String TAG_COIN = "ETH";
	private static final String TAG_PRICE = "EUR";

	/*----------------------------------------------------------------------------------------------------*
	| Costruttore                                                                                         |
	*----------------------------------------------------------------------------------------------------*/
	public GetCoinbaseHistoricalJSONTask(Context context){
		this.context = context;
	}

	/*----------------------------------------------------------------------------------------------------*
	| onPreExecute() - prima di lanciare il thread in background mostriamo la ProgressDialog              |
	*----------------------------------------------------------------------------------------------------*/
	@Override
	protected void onPreExecute(){
		/*super.onPreExecute();
        //Creiamo una Progress Dialog associandogli uno stile personalizzato
		progressDialog = new ProgressDialog(GetCoinbaseJSONTaskContext, R.style.MyProgressDialogTheme);
		//Rendiamo la ProgressDialog cancellabile
		progressDialog.setCancelable(true);
		//Ma non al semplice tocco di una parte qualsiasi del display
		progressDialog.setCanceledOnTouchOutside(false);
		//Solo al tocco del tasto indietro
		progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener(){
			public void onCancel(DialogInterface dialog){
				((Activity) GetCoinbaseJSONTaskContext).finish();
			}
		});
		//Visualizziamo la Progress Dialog
		progressDialog.show();*/
	}

	/*----------------------------------------------------------------------------------------------------*
	| doInBackground() - estraiamo i dati dalla url e carichiamoli in un ArrayList che verrà              |
	|                    passato all'onPostExecute()                                                      |
	*----------------------------------------------------------------------------------------------------*/
    protected ArrayList<HashMap<String, Double>> doInBackground(Void... args) {

        //ArrayList da passare all'onPostExecute() contenente le coppie data/guadagno
        ArrayList<HashMap<String, Double>> data = new ArrayList<>();
        //Generiamo le date da analizzare
        DateTools dt = new DateTools();
        LinkedHashMap<String, Long> dates = dt.generateCompareDates("08/08/2017 15:40", dt.getSysdate());
        //Istanziamo un oggetto per il parsing JSON
        NewJSONParser jParser = new NewJSONParser();

        //Cicliamo su tutte le date estratte
        for (Map.Entry<String, Long> entry : dates.entrySet()) {
            //Per ognuna di esse costruiamo dinamicamente la url per la request http
            String url = URL_JSON + entry.getValue();
            try {
                //E dalla sua chiamata al webservice otteniamo un JSONObject
                JSONObject jsonObject = jParser.makeHttpRequest(url, "POST", "");
                //Il valore associato alla chiave "ETH" è a sua volta un JSONObject
                JSONObject jsonObjectInside = new JSONObject(jsonObject.getString(TAG_COIN));
                //Dal quale andiamo ad estrarre il valore associato alla chiave "EUR"
                String ethHistoricalPrice = jsonObjectInside.getString(TAG_PRICE);
                String controlDate = entry.getKey();

                //Ethereum acquistato fino alla data di controllo
                double boughtEthAtDate = 0;
                //Valore dell'Ethereum acquistato fino alla data di controllo
                double costInEu = 0;
                //Cicliamo su tutti gli oggetti dell'enum
                for(int i = 0; i < Investment.values().length; i++){
                    //Preleviamo la data dell'investimento
                    String investmentDate = Investment.values()[i].getDate();
                    //Se è inferiore alla data di controllo
                    if(dt.isTheFirstDateAfter(controlDate, investmentDate)) {
                        //Sommiamo l'Ethereum acquistato fino alla data di controllo
                        boughtEthAtDate += Investment.values()[i].getBoughtEth();
                        //Sommiamo il valore dell'Ethereum acquistato fino alla data di controllo
                        costInEu += Investment.values()[i].getCostInEu();
                    }
                }
                //Valore dell'investimento alla data di controllo
                double currentInvestmentValue = boughtEthAtDate*Double.parseDouble(ethHistoricalPrice);
                //Valore del guadagno  alla data di controllo
                double profit = currentInvestmentValue - costInEu;
                //Formattiamo il campo profit con due decimali
                DecimalFormat df = new DecimalFormat("#.00");
                String profit_formatted_comma = df.format(profit);
                String profit_formatted_dot = profit_formatted_comma.replaceAll(",", ".");
                double profit_double_2_decimals = Double.parseDouble(profit_formatted_dot);

                //Creiamo la coppia data/guadagno e aggiungiamola all'ArrayList da ritornare
                HashMap dateProfit = new HashMap();
                dateProfit.put(controlDate, profit_double_2_decimals);
                data.add(dateProfit);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

        }
        return data;
    }

	/*----------------------------------------------------------------------------------------------------*
	| onPostExecute() - al completamento del task in background rimuoviamo la ProgressDialog,             |
	|                   creiamo il ListAdapter e associamolo alla ListView                                |
	*----------------------------------------------------------------------------------------------------*/
	protected void onPostExecute(ArrayList<HashMap<String, Double>> result){

		//progressDialog.dismiss();

        createGraph(result);
	}

    /*----------------------------------------------------------------------------------------------------*
	| createGraph() - http://www.android-graphview.org                                                    |
	*----------------------------------------------------------------------------------------------------*/
    private void createGraph(ArrayList<HashMap<String, Double>> data) {

        //Creiamo il grafico
        GraphView graph = (GraphView) ((Activity) context).findViewById(R.id.graph);
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
        graph.getGridLabelRenderer().setNumHorizontalLabels(4);

        //Aggiungiamo i valori di ogni coppia x/y al grafico
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(profits);
        graph.addSeries(series);

        //Visualizziamo un punto cliccabile sul grafico per ogni coppia x/y
        series.setDrawDataPoints(true);
        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(context, "€ " + dataPoint.getY(), Toast.LENGTH_SHORT).show();
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