package it.redluck.materialdesign.async_tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DecimalFormat;
import it.redluck.materialdesign.R;
import it.redluck.materialdesign.utilities.JSONParser;


public class GetCoinbaseJSONTask extends AsyncTask<Void, Void, JSONObject>{

	//La progress dialog
	private ProgressDialog progressDialog;
	//Il Context
	private Context GetCoinbaseJSONTaskContext;

	//Url del JSON
	private static final String URL_JSON = "https://min-api.cryptocompare.com/data/price?fsym=ETH&tsyms=EUR&e=Coinbase";
	//Nomi dei nodi del JSON
	private static final String TAG_PRICE = "EUR";

	/*----------------------------------------------------------------------------------------------------*
	| Costruttore                                                                                         |
	*----------------------------------------------------------------------------------------------------*/
	public GetCoinbaseJSONTask(Context context){
		this.GetCoinbaseJSONTaskContext = context;
	}

	/*----------------------------------------------------------------------------------------------------*
	| onPreExecute() - prima di lanciare il thread in background mostriamo la ProgressDialog              |
	*----------------------------------------------------------------------------------------------------*/
	@Override
	protected void onPreExecute(){
		super.onPreExecute();
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
		progressDialog.show();
	}

	/*----------------------------------------------------------------------------------------------------*
	| doInBackground() - estraiamo i dati dalla url e carichiamoli in un JSONObject che verrà             |
	|                    passato all'onPostExecute()                                                      |
	*----------------------------------------------------------------------------------------------------*/
    @Override
    protected JSONObject doInBackground(Void... args){
        //JSONObject che verrà passato all'onPostExecute()
        JSONObject returnJsonObject = new JSONObject();
		//List<NameValuePair> è utilizzato per inviare valori da un'applicazione android ad un server
		//(in questo caso sarà vuota in quanto non dobbiamo inviare valori ma solo riceverne)
		//List<NameValuePair> params = new ArrayList<NameValuePair>();
		//Istanziamo un oggetto per il parsing JSON
		JSONParser jParser = new JSONParser();
		//E dalla sua chiamata al webservice otteniamo un JSONObject
        try {
            //(con la url in https la richiesta deve essere in get)
            returnJsonObject = jParser.getJsonObjectFromUrl(URL_JSON);
            Log.d("MyLog", returnJsonObject.toString());
	    }
        catch (Exception e) {
            e.printStackTrace();
        }
        return returnJsonObject;
    }

	/*----------------------------------------------------------------------------------------------------*
	| onPostExecute() - al completamento del task in background rimuoviamo la ProgressDialog              |
	|                   e creiamo il ListAdapter                                                          |
	*----------------------------------------------------------------------------------------------------*/
	@Override
	protected void onPostExecute(JSONObject result){

		progressDialog.dismiss();

		//Se vogliamo ritardare di qualche secondo la scomparsa della Progress Dialog
        /*Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        }, 5000);*/

        //Riempimento della TextView sul valore attuale dell'Ethereum
        TextView tv = (TextView) ((Activity) GetCoinbaseJSONTaskContext).findViewById(R.id.label);
        try {
            tv.setText("Quotazione Ethereum su Coinbase\n€ " + result.getString(TAG_PRICE));
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        tv.setBackgroundResource(R.drawable.textview_background_transparent);

        //Valore attuale dell'Ethereum (utilizzato in tutte le TextView successive)
        double valoreEthAttuale = 0;
        try {
            valoreEthAttuale = Double.parseDouble(result.getString(TAG_PRICE));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Riempimento della TextView sul primo investimento
		TextView firstValue = (TextView) ((Activity) GetCoinbaseJSONTaskContext).findViewById(R.id.value01);
		String dataInvestimento01 = "08/08/2017 15:40";
        double valoreEthAllaData01 = 242.18;
		double ethAcquistato01 = 0.92904223;
		double spesa01 = 225.00;
        double valoreAttualeInvestimento01 = ethAcquistato01 * valoreEthAttuale;
        double guadagno01 = valoreAttualeInvestimento01 - spesa01;
		firstValue.setText("Data investimento: " + dataInvestimento01 + "\nValore ETH alla data: € " + valoreEthAllaData01 + "\nETH acquistato: " + ethAcquistato01 + "\nSpesa: € " + new DecimalFormat("#.00").format(spesa01) + "\nValore attuale investimento: € " + new DecimalFormat("#.00").format(valoreAttualeInvestimento01) + "\nGuadagno: € " + new DecimalFormat("#.00").format(guadagno01));
        if(guadagno01 >= 0){
            firstValue.setBackgroundResource(R.drawable.textview_background_green);
        }
        else{
            firstValue.setBackgroundResource(R.drawable.textview_background_red);
        }

        //Riempimento della TextView sul secondo investimento
        TextView secondValue = (TextView) ((Activity) GetCoinbaseJSONTaskContext).findViewById(R.id.value02);
        String dataInvestimento02 = "09/08/2017 12:25";
        double valoreEthAllaData02 = 245.93;
        double ethAcquistato02 = 1.08000000;
        double spesa02 = 265.60;
        double valoreAttualeInvestimento02 = ethAcquistato02 * valoreEthAttuale;
        double guadagno02 = valoreAttualeInvestimento02 - spesa02;
        secondValue.setText("Data investimento: " + dataInvestimento02 + "\nValore ETH alla data: € " + valoreEthAllaData02 + "\nETH acquistato: " + ethAcquistato02 + "\nSpesa: € " + new DecimalFormat("#.00").format(spesa02) + "\nValore attuale investimento: € " + new DecimalFormat("#.00").format(valoreAttualeInvestimento02) + "\nGuadagno: € " + new DecimalFormat("#.00").format(guadagno02));
        if(guadagno02 >= 0){
            secondValue.setBackgroundResource(R.drawable.textview_background_green);
        }
        else{
            secondValue.setBackgroundResource(R.drawable.textview_background_red);
        }

        //Riempimento della TextView sul terzo investimento
        TextView thirdValue = (TextView) ((Activity) GetCoinbaseJSONTaskContext).findViewById(R.id.value03);
        String dataInvestimento03 = "29/08/2017 15:10";
        double valoreEthAllaData03 = 303.59;
        double ethAcquistato03 = 0.99095777;
        double spesa03 = 300.84;
        double valoreAttualeInvestimento03 = ethAcquistato03 * valoreEthAttuale;
        double guadagno03 = valoreAttualeInvestimento03 - spesa03;
        thirdValue.setText("Data investimento: " + dataInvestimento03 + "\nValore ETH alla data: € " + valoreEthAllaData03 + "\nETH acquistato: " + ethAcquistato03 + "\nSpesa: € " + new DecimalFormat("#.00").format(spesa03) + "\nValore attuale investimento: € " + new DecimalFormat("#.00").format(valoreAttualeInvestimento03) + "\nGuadagno: € " + new DecimalFormat("#.00").format(guadagno03));
        if(guadagno03 >= 0){
            thirdValue.setBackgroundResource(R.drawable.textview_background_green);
        }
        else{
            thirdValue.setBackgroundResource(R.drawable.textview_background_red);
        }

        //Riempimento della TextView sui totali
        TextView total = (TextView) ((Activity) GetCoinbaseJSONTaskContext).findViewById(R.id.total);
        double ethAcquistatoTot = ethAcquistato01 + ethAcquistato02 + ethAcquistato03;
        double spesaTot = spesa01 + spesa02 + spesa03;
        double valoreAttualeInvestimentoTot = valoreAttualeInvestimento01 + valoreAttualeInvestimento02 + valoreAttualeInvestimento03;
        double guadagnoTot = valoreAttualeInvestimentoTot - spesaTot;
        total.setText("Spesa totale: € " + new DecimalFormat("#.00").format(spesaTot) + "\nValore attuale investimento totale: € " + new DecimalFormat("#.00").format(valoreAttualeInvestimentoTot) + "\nGuadagno totale: € " + new DecimalFormat("#.00").format(guadagnoTot));
        if(guadagnoTot >= 0){
            total.setBackgroundResource(R.drawable.textview_background_green_total);
        }
        else{
            total.setBackgroundResource(R.drawable.textview_background_red_total);
        }
	}
}