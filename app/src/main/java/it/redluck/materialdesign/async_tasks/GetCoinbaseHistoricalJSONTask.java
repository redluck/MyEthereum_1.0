package it.redluck.materialdesign.async_tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import it.redluck.materialdesign.model.Investment;
import it.redluck.materialdesign.utilities.DateTools;
import it.redluck.materialdesign.utilities.JSONParser;
import it.redluck.materialdesign.utilities.NewJSONParser;

import static java.lang.Double.parseDouble;


public class GetCoinbaseHistoricalJSONTask extends AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>{

	//La progress dialog
	private ProgressDialog progressDialog;
	//Il Context
	private Context GetCoinbaseJSONTaskContext;

	//Url del JSON escluso il timestamp
	private static final String URL_JSON = "https://min-api.cryptocompare.com/data/pricehistorical?fsym=ETH&tsyms=EUR&e=Coinbase&ts=";
	//Nomi dei nodi del JSON
    private static final String TAG_COIN = "ETH";
	private static final String TAG_PRICE = "EUR";

	/*----------------------------------------------------------------------------------------------------*
	| Costruttore                                                                                         |
	*----------------------------------------------------------------------------------------------------*/
	public GetCoinbaseHistoricalJSONTask(Context context){
		this.GetCoinbaseJSONTaskContext = context;
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
    protected ArrayList<HashMap<String, String>> doInBackground(Void... args) {

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

                Log.d("MyLog", "Data di controllo: " + controlDate + /*" / ETH acquistato: " + boughtEthAtDate + " / Spesa complessiva: " + costInEu + " / Valore investimento: " + currentInvestmentValue + */" / Guadagno: " + profit_double_2_decimals);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

        }
        return null;
    }

	/*----------------------------------------------------------------------------------------------------*
	| onPostExecute() - al completamento del task in background rimuoviamo la ProgressDialog,             |
	|                   creiamo il ListAdapter e associamolo alla ListView                                |
	*----------------------------------------------------------------------------------------------------*/
	protected void onPostExecute(ArrayList<HashMap<String, String>> result){

		/*progressDialog.dismiss();

        MySimpleAdapter adapter = new MySimpleAdapter(GetCoinbaseJSONTaskContext, result, R.layout.list_item, new String[]{"Date", "ETH value at date", "Bought ETH", "Cost in EU", "Current investment value", "Profit"}, new int[]{R.id.date, R.id.eth_value_at_date, R.id.bought_eth, R.id.cost_in_eu, R.id.current_investment_value, R.id.profit});
        ListView lv = (ListView)((Activity) GetCoinbaseJSONTaskContext).findViewById(R.id.list);
        lv.setAdapter(adapter);*/
	}

    //Metodi del guadagno totale alla dataDiControllo
    /*
    - prendo tutte le date investimento minori di dataDiControllo
      e ricavo la somma dell'eth fino a quel momento e la somma della spesa in €
    - prendo il valore dell'eth alla dataDiControllo e calcolo il valore in €
      della somma di eth
    - sottraggo alla seconda somma quella della spesa in €
     */
}