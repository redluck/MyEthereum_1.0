package it.redluck.materialdesign.utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

public class NewJSONParser {

    //Url a cui connettersi
    private HttpURLConnection conn;
    //Responso del tentativo di connessione
    private int responseCode;
    //Stringa contenente il contenuto della risposta http
    private String httpString;
    //JSONObject contenente la stringa di cui sopra
    private JSONObject httpJsonObject;

    /*----------------------------------------------------------------------------------------------------*
	| makeHttpRequest() - funzione che ritorna il JSON dalla richiesta HTTP alla url in POST o GET        |
	*----------------------------------------------------------------------------------------------------*/
    public JSONObject makeHttpRequest(String reqUrl, String method, String encodedParams) {

        //****************************************************************************************************
        //1-Effettuiamo la request HTTP e otteniamo un response code sull'esito della connessione
        //****************************************************************************************************
        try {
            //Se la tipologia della request è in POST prima di leggere il response apriamo un OutputStream
            //dall'HttpURLConnection e scriviamoci i parametri da inviare
            if (method.equals("POST")) {
                //Impostiamo il path della nostra url
                URL url = new URL(reqUrl);
                //Apriamo la connessione
                conn = (HttpURLConnection) url.openConnection();
                //Impostiamo il metodo (il valore di default è GET)
                conn.setRequestMethod("POST");
                //Metodo utilizzato per consentire la ricezione di dati tramite la connessione (di default è true)
                conn.setDoInput(true);
                //Metodo utilizzato con il POST per consentire l'invio di un body tramite la connessione (di default è false)
                conn.setDoOutput(true);
                //Ricaviamo lo stream dei dati in uscita
                OutputStream outputStream = conn.getOutputStream();
                //E inviamo tramite di esso i parametri sotto forma di buffer (sequenza di caratteri)
                //La stringa contenente i parametri deve essere correttamente codificata con i caratteri & e =
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                writer.write(encodedParams);
                writer.flush();
                writer.close();
                outputStream.close();
                //Ricaviamo il responso dalla connessione
                responseCode = conn.getResponseCode();
            }
            //Se la tipologia della request è in GET
            else if (method.equals("GET")) {
                //Impostiamo il path della nostra url che conterrà i parametri
                URL url = new URL(reqUrl + "?" + encodedParams);
                //Apriamo la connessione
                conn = (HttpURLConnection) url.openConnection();
                //Impostiamo il metodo
                conn.setRequestMethod("GET");
                //Ricaviamo il responso dalla connessione
                responseCode = conn.getResponseCode();
            }
        }
        catch (Exception e) {
            Log.e("MyLog", "Exception: " + e.getMessage());
        }

        //****************************************************************************************************
        //2-Inseriamo il contenuto della risposta http in una stringa
        //****************************************************************************************************
        try {
            //Se è andato tutto a buon fine
            if (responseCode == HttpURLConnection.HTTP_OK) {
                //Costruiamo un BufferedReader (classe dedicata alla lettura di buffers, sequenze di caratteri,
                //che il metodo readLine() restituisce sotto forma di stringhe)
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "iso-8859-1"), 8);
                //Costruiamo una stringa modificabile in cui andremo ad inserire il contenuto della risposta http
                StringBuilder sb = new StringBuilder();
                //Leggiamo una riga per volta dal BufferedReader e aggiungiamola alla stringa modificabile creata
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                reader.close();
                //Quindi trasformiamo la stringa modificabile in semplice stringa
                httpString = sb.toString();
                Log.d("MyLog", "La request POST è stata stabilita");
            }
            //Altrimenti
            else {
                Log.d("MyLog", "La request POST non ha funzionato");
            }
        } catch (Exception e) {
            Log.e("MyLog", "Exception: " + e.getMessage());
        }

        //****************************************************************************************************
        //3-A questo punto proviamo a parsare la stringa in un JSONObject
        //****************************************************************************************************
        try {
            httpJsonObject = new JSONObject(httpString);
        }
        catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        return httpJsonObject;
    }

    /*----------------------------------------------------------------------------------------------------*
	| Metodi creati per comodità al fine di trasformare un JSONObject / HashMap (dove memorizzare         |
	| i parametri) in query string da passare alla richiesta http                                         |
	*----------------------------------------------------------------------------------------------------*/
    public String jsonobjectToString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        //Impostiamo un flag che dopo la prima iterazione verrà settato a false
        boolean first = true;
        //Iteriamo il JSONObject
        Iterator<String> iterator = params.keys();
        while(iterator.hasNext()) {
            //Preleviamo chiavi e valori da ciascuna coppia
            String key = iterator.next();
            Object value = params.get(key);
            //Dalla seconda iterazione in poi aggiungiamo un '&' per concatenare le varie coppie
            if(first){
                first = false;
            }
            else{
                result.append("&");
            }
            //Quindi concateniamo le varie coppie nome/valore
            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));
        }
        return result.toString();
    }

    public String hashmapToString(HashMap<String, String> map) throws Exception {

        StringBuilder result = new StringBuilder();
        //Iteriamo su tutte le chiavi della mappa
        for (String key : map.keySet()) {
            //Se la query string già ha un parametro aggiungiamo la &
            if (result.length() > 0) {
                result.append("&");
            }
            //Ricaviamo il valore associato alla chiave attuale
            String value = map.get(key);
            //Quindi concateniamo le varie coppie nome/valore
            result.append((key != null ? URLEncoder.encode(key, "UTF-8") : ""));
            result.append("=");
            result.append(value != null ? URLEncoder.encode(value, "UTF-8") : "");
        }
        return result.toString();
    }
}