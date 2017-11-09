package it.redluck.materialdesign.async_tasks;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.TextView;
import it.redluck.materialdesign.R;
import it.redluck.materialdesign.utilities.JSONParser;

public class GetCoinmarketcapJSONTask extends AsyncTask<Void, Void, JSONObject>{

	//La progress dialog
	private ProgressDialog progressDialog;
	//Il Context
	private Context getJsonTaskContext;

	//Url del JSON
	private static final String URL_JSON = "https://api.coinmarketcap.com/v1/ticker/?convert=EUR&limit=2";
	//Nomi dei nodi del JSON
	private static final String TAG_ID = "id";
	private static final String TAG_PRICE = "price_eur";
	private static final String TAG_UPDATED_AT = "last_updated";

	/*----------------------------------------------------------------------------------------------------*
	| Costruttore                                                                                         |
	*----------------------------------------------------------------------------------------------------*/
	public GetCoinmarketcapJSONTask(Context context){
		this.getJsonTaskContext = context;
	}

	/*----------------------------------------------------------------------------------------------------*
	| onPreExecute() - prima di lanciare il thread in background mostriamo la ProgressDialog              |
	*----------------------------------------------------------------------------------------------------*/
	@Override
	protected void onPreExecute(){
		super.onPreExecute();
        //Creiamo una Progress Dialog associandogli uno stile personalizzato
		progressDialog = new ProgressDialog(getJsonTaskContext, R.style.MyProgressDialogTheme);
		//Rendiamo la ProgressDialog cancellabile
		progressDialog.setCancelable(true);
		//Ma non al semplice tocco di una parte qualsiasi del display
		progressDialog.setCanceledOnTouchOutside(false);
		//Solo al tocco del tasto indietro
		progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener(){
			public void onCancel(DialogInterface dialog){
				((Activity) getJsonTaskContext).finish();
			}
		});
		//Visualizziamo la Progress Dialog
		progressDialog.show();
	}

	/*----------------------------------------------------------------------------------------------------*
	| doInBackground() - estraiamo i dati dalla url e carichiamoli in un JSONObject che verrà             |
	|                    passato all'onPostExecute()                                                      |
	*----------------------------------------------------------------------------------------------------*/
	protected JSONObject doInBackground(Void... args){
        //JSONObject che verrà passato all'onPostExecute()
        JSONObject returnJsonObject = new JSONObject();
		//Istanziamo un oggetto per il parsing JSON
		JSONParser jParser = new JSONParser();
		//E dalla sua chiamata al webservice otteniamo un JSONArray
        try {
            //(con la url in https la richiesta deve essere il get)
            JSONArray json = jParser.getJsonArrayFromUrl(URL_JSON);
			//Cicliamo il JSONArray
            for (int i = 0; i < json.length(); i++) {
                JSONObject jsonObject = json.getJSONObject(i);
				//Quindi prendiamo in considerazione solo il JSONObject che ci interessa
				if(jsonObject.getString(TAG_ID).equals("ethereum")) {
                    returnJsonObject = jsonObject;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
		return returnJsonObject;
	}

	/*----------------------------------------------------------------------------------------------------*
	| onPostExecute() - al completamento del task in background rimuoviamo la ProgressDialog              |
	|                   e riempiamo la TextView                                                           |
	*----------------------------------------------------------------------------------------------------*/
	@Override
	protected void onPostExecute(JSONObject result){

		progressDialog.dismiss();

        //Riempimento della TextView sul valore attuale dell'Ethereum
        TextView tv = (TextView) ((Activity) getJsonTaskContext).findViewById(R.id.label);
        try {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date(Long.parseLong(result.getString(TAG_UPDATED_AT)) * 1000);
            String dateString = df.format(date);
            tv.setText("Quotazione Ethereum su CoinMarketCap\n" + dateString + "\n€ " + result.getString(TAG_PRICE).substring(0, 6));
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        tv.setBackgroundResource(R.drawable.textview_background_transparent);
	}
}