package it.redluck.materialdesign.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

public class JSONParser{

	//Ciò che ci viene ritornato dalla richiesta HTTP è in formato InputStream
	private InputStream httpInputStream;
	//Noi poi lo trasformeremo in formato String
	private String httpString;
	//E infine da String in formato JSONArray o JSONObject
	private JSONArray httpJsonArray;
    private JSONObject httpJsonObject;

	/*--------------------------------------------------------------------------------------------------------------*
	| getJsonArrayFromUrl()                                                                                         |
	*--------------------------------------------------------------------------------------------------------------*/
	public JSONArray getJsonArrayFromUrl(String url){

		//****************************************************************************************************
		//1-Otteniamo un oggetto di tipo InputStream a partire dalla classe URL
		//(è la superclasse di tutti i flussi in input)
		//****************************************************************************************************
		try {
			httpInputStream = new URL(url).openStream();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		//****************************************************************************************************
		//2-A partire dall'InputStream appena ottenuto realizziamo un oggetto di tipo String
		//****************************************************************************************************
		try{
			//Costruiamo un BufferedReader a partire dall'InputStream ottenuto al punto precedente
			//(BufferedReader è una classe dedicata alla lettura di buffers, sequenze di caratteri,
			//che il metodo readLine() restituisce sotto forma di stringhe)
			BufferedReader reader = new BufferedReader(new InputStreamReader(httpInputStream, Charset.forName("UTF-8")));
			//Costruiamo una stringa modificabile in cui andremo ad inserire il contenuto della pagina richiamata dalla URL
			StringBuilder sb = new StringBuilder();
			//Leggiamo una riga per volta dal BufferedReader e aggiungiamola alla stringa modificabile
			String line;
			while((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}
			httpInputStream.close();
			//Quindi trasformiamo la stringa modificabile in semplice stringa
			httpString = sb.toString();
		}
		catch(Exception e){
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		//****************************************************************************************************
		//3-A questo punto proviamo a parsare la stringa in un JSONArray
		//****************************************************************************************************
		try{
			httpJsonArray = new JSONArray(httpString);
		}
		catch(JSONException e){
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}
		return httpJsonArray;
	}

    /*--------------------------------------------------------------------------------------------------------------*
	| getJsonObjectFromUrl()                                                                                        |
	*--------------------------------------------------------------------------------------------------------------*/
    public JSONObject getJsonObjectFromUrl(String url){

        //****************************************************************************************************
        //1-Otteniamo un oggetto di tipo InputStream a partire dalla classe URL
        //(è la superclasse di tutti i flussi in input)
        //****************************************************************************************************
        try {
            httpInputStream = new URL(url).openStream();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        //****************************************************************************************************
        //2-A partire dall'InputStream appena ottenuto realizziamo un oggetto di tipo String
        //****************************************************************************************************
        try{
            //Costruiamo un BufferedReader a partire dall'InputStream ottenuto al punto precedente
            //(BufferedReader è una classe dedicata alla lettura di buffers, sequenze di caratteri,
            //che il metodo readLine() restituisce sotto forma di stringhe)
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpInputStream, Charset.forName("UTF-8")));
            //Costruiamo una stringa modificabile in cui andremo ad inserire il contenuto della pagina richiamata dalla URL
            StringBuilder sb = new StringBuilder();
            //Leggiamo una riga per volta dal BufferedReader e aggiungiamola alla stringa modificabile
            String line;
            while((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            httpInputStream.close();
            //Quindi trasformiamo la stringa modificabile in semplice stringa
            httpString = sb.toString();
        }
        catch(Exception e){
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        //****************************************************************************************************
        //3-A questo punto proviamo a parsare la stringa in un JSONObject
        //****************************************************************************************************
        try{
            httpJsonObject = new JSONObject(httpString);
        }
        catch(JSONException e){
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        return httpJsonObject;
    }
}