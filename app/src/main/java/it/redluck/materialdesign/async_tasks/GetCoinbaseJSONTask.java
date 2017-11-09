package it.redluck.materialdesign.async_tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import it.redluck.materialdesign.R;
import it.redluck.materialdesign.adapter.MySimpleAdapter;
import it.redluck.materialdesign.model.Investment;
import it.redluck.materialdesign.utilities.JSONParser;

import static java.lang.Double.parseDouble;


public class GetCoinbaseJSONTask extends AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>{

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
	| doInBackground() - estraiamo i dati dalla url e carichiamoli in un ArrayList che verrà              |
	|                    passato all'onPostExecute()                                                      |
	*----------------------------------------------------------------------------------------------------*/
    protected ArrayList<HashMap<String, String>> doInBackground(Void... args){

        //Lista che sarà passata al ListAdapter nell'onPostExecute()
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        //Variabile contenente il valore da estrarre dal JSON
        double ethCurrentValue = 0;
        //Istanziamo un oggetto per il parsing JSON
		JSONParser jParser = new JSONParser();
		//E dalla sua chiamata al webservice otteniamo un JSONObject
        try {
            JSONObject jsonObject = jParser.getJsonObjectFromUrl(URL_JSON);
            //da cui andiamo ad estrarre il valore che ci interessa
            ethCurrentValue = parseDouble(jsonObject.getString(TAG_PRICE));
	    }
        catch (Exception e) {
            e.printStackTrace();
        }

        //Inizializziamo delle variabili per tenere conto dei totali
        double totBoughtEth = 0;
        double totCostInEu = 0;
        double totCurrentInvestmentValue = 0;
        double totProfit = 0;

        //Cicliamo sugli oggetti dell'enum (di cui ora abbiamo tutti i campi grazie al valore ottenuto in precedenza)
        for(int i = 0; i < Investment.values().length; i++){
            //Campi ricavabili tramite getters dell'oggetto
            String date = Investment.values()[i].getDate();
            String ethValueAtDate = String.valueOf(Investment.values()[i].getEthValueAtDate());
            String boughtEth = String.valueOf(Investment.values()[i].getBoughtEth());
            String costInEu = String.valueOf(Investment.values()[i].getCostInEu());
            //Campi ricavabili tramite l'ethCurrentValue e da formattare
            //1- currentInvestmentValue
            double currentInvestmentValue_double = Investment.values()[i].getBoughtEth() * ethCurrentValue;
            //Formattiamo il double in string con due sole cifre decimali
            DecimalFormat df = new DecimalFormat("#.00");
            String currentInvestmentValue_formatted_comma = df.format(currentInvestmentValue_double);
            //Quindi sostituiamo la virgola con il punto
            String currentInvestmentValue_formatted_dot = currentInvestmentValue_formatted_comma.replaceAll(",", ".");
            //A questo punto possiamo ritrasformare la stringa in double
            double currentInvestmentValue_double_2_decimals = Double.parseDouble(currentInvestmentValue_formatted_dot);
            //E procediamo come per i campi precedenti
            Investment.values()[i].setCurrentInvestmentValue(currentInvestmentValue_double_2_decimals);
            String currentInvestmentValue = String.valueOf(Investment.values()[i].getCurrentInvestmentValue());
            //2- profit
			double profit_double = currentInvestmentValue_double - Investment.values()[i].getCostInEu();
			String profit_formatted_comma = df.format(profit_double);
            String profit_formatted_dot = profit_formatted_comma.replaceAll(",", ".");
            double profit_double_2_decimals = Double.parseDouble(profit_formatted_dot);
            Investment.values()[i].setProfit(profit_double_2_decimals);
            String profit = String.valueOf(Investment.values()[i].getProfit());

            //Procediamo alla creazione di una mappa contenente i valori di ogni singolo oggetto
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("Date", "Data investimento: " + date);
            map.put("ETH value at date", "Valore ETH alla data: " + ethValueAtDate);
            map.put("Bought ETH", "ETH acquistato: " + boughtEth);
            map.put("Cost in EU", "Spesa: " + costInEu);
            map.put("Current investment value", "Valore attuale investimento: " + currentInvestmentValue);
            map.put("Profit", "Guadagno: " + profit);
            //E aggiungiamola alla lista
            arrayList.add(map);

            //Calcoliamo anche i totali di cui vogliamo disporre
            totBoughtEth += Double.parseDouble(boughtEth);
            totCostInEu += Double.parseDouble(costInEu);
            totCurrentInvestmentValue += currentInvestmentValue_double_2_decimals;
            totProfit += profit_double_2_decimals;
        }
        //Formattiamo il campo totCostInEu con due decimali
        DecimalFormat df = new DecimalFormat("#.00");
        String totCostInEu_formatted_comma = df.format(totCostInEu);
        String totCostInEu_formatted_dot = totCostInEu_formatted_comma.replaceAll(",", ".");
        double totCostInEu_double_2_decimals = Double.parseDouble(totCostInEu_formatted_dot);

        //Facciamo lo stesso con il campo totCurrentInvestmentValue
        String totCurrentInvestmentValue_formatted_comma = df.format(totCurrentInvestmentValue);
        String totCurrentInvestmentValue_formatted_dot = totCurrentInvestmentValue_formatted_comma.replaceAll(",", ".");
        double totCurrentInvestmentValue_double_2_decimals = Double.parseDouble(totCurrentInvestmentValue_formatted_dot);

        //E con il campo totProfit
        String totProfit_formatted_comma = df.format(totProfit);
        String totProfit_formatted_dot = totProfit_formatted_comma.replaceAll(",", ".");
        double totProfit_double_2_decimals = Double.parseDouble(totProfit_formatted_dot);

        //Sysdate
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        String dataStr = sdf.format(new Date());

        //Creiamo una mappa contenente i valori dei totali
        HashMap<String, String> mapTot = new HashMap<String, String>();
        mapTot.put("Date", "TOTALI\n\nData: " + dataStr);
        mapTot.put("ETH value at date", "Valore ETH: " + ethCurrentValue);
        mapTot.put("Bought ETH", "ETH acquistato: " + totBoughtEth);
        mapTot.put("Cost in EU", "Spesa: " + totCostInEu_double_2_decimals);
        mapTot.put("Current investment value", "Valore attuale investimento: " + totCurrentInvestmentValue_double_2_decimals);
        mapTot.put("Profit", "Guadagno: " + totProfit_double_2_decimals);
        //E aggiungiamo anche questa alla lista
        arrayList.add(mapTot);

        return arrayList;
    }

	/*----------------------------------------------------------------------------------------------------*
	| onPostExecute() - al completamento del task in background rimuoviamo la ProgressDialog,             |
	|                   creiamo il ListAdapter e associamolo alla ListView                                |
	*----------------------------------------------------------------------------------------------------*/
	protected void onPostExecute(ArrayList<HashMap<String, String>> result){

		progressDialog.dismiss();

        MySimpleAdapter adapter = new MySimpleAdapter(GetCoinbaseJSONTaskContext, result, R.layout.list_item, new String[]{"Date", "ETH value at date", "Bought ETH", "Cost in EU", "Current investment value", "Profit"}, new int[]{R.id.date, R.id.eth_value_at_date, R.id.bought_eth, R.id.cost_in_eu, R.id.current_investment_value, R.id.profit});
        ListView lv = (ListView)((Activity) GetCoinbaseJSONTaskContext).findViewById(R.id.list);
        lv.setAdapter(adapter);
	}
}