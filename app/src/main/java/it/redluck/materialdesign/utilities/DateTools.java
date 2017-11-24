package it.redluck.materialdesign.utilities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;

public class DateTools {

	/*--------------------------------------------------------------------------------------------------------------*
	| getSysdate()                                                                                                  |
	*--------------------------------------------------------------------------------------------------------------*/
	public String getSysdate(){

        //Creiamo un formato che utilizzeremo per le date (quello standard è: "Fri Jul 04 18:37:18 CEST 2014")
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        //Sysdate
        Date todayDate = new Date();
        String sysdate = format.format(todayDate);
        return sysdate;
    }

    /*--------------------------------------------------------------------------------------------------------------*
	| generateCompareDates()                                                                                        |
	*--------------------------------------------------------------------------------------------------------------*/
    //Funzione per generare le date di interesse in un dato intervallo
    public LinkedHashMap<String, Long> generateCompareDates(String investmentDate, String sysdate) {

        //Mappa che conterrà tutte le date in formato String e Unix Epoch
        //(LinkedHashMap a differenza di HashMap mantiene i record nell'ordine di inserimento)
        LinkedHashMap<String, Long> map = new LinkedHashMap<>();
        //Formato che utilizzeremo per le date (quello standard è: "Fri Jul 04 18:37:18 CEST 2014")
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        try{
            //Aggiungiamo un giorno alla data investimento fino a raggiungere il valore della data odierna
            while(isTheFirstDateAfter(sysdate, investmentDate)){
                //Parsiamo investmentDate da String a Date
                Date increasedDate = format.parse(investmentDate);
                //Quindi incrementiamolo di un giorno
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(increasedDate);
                calendar.add(Calendar.DATE, 1);
                increasedDate = calendar.getTime();
                //E ritrasformiamolo in String
                investmentDate = format.format(increasedDate);

                //Escludiamo domani (prima condizione)
                //e  prendiamo solo il 1° e il 16° giorno, se presenti (seconda condizione)
                if(isTheFirstDateAfter(sysdate, investmentDate) && (investmentDate.substring(0, 2).equals("01") || investmentDate.substring(0, 2).equals("16"))) {
                    String dateOfInterestString = investmentDate.substring(0, 10) + " 06:00";
                    Date dateOfInterest = format.parse(dateOfInterestString);
                    //Preleviamo il timestamp unix che ci servirà per il link da inserire nel task
                    long epoch = dateOfInterest.getTime()/1000;
                    //E riempiamo la mappa
                    map.put(dateOfInterestString, epoch);
                }
            }
        }
        catch (ParseException e){
            e.printStackTrace();
        }
        return map;
    }

    /*--------------------------------------------------------------------------------------------------------------*
	| isTheFirstDateLater()                                                                                         |
	*--------------------------------------------------------------------------------------------------------------*/
    //Funzione per confrontare 2 date in formato String "dd/MM/yyyy HH:mm"
    public boolean isTheFirstDateAfter(String firstDate, String secondDate) {

        //Variabile da ritornare
        boolean returnVariable = false;
        //Formato che utilizzeremo per le date (quello standard è: "Fri Jul 04 18:37:18 CEST 2014")
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        try {
            Date dateOne = format.parse(firstDate);
            Date dateTwo = format.parse(secondDate);
            returnVariable = dateOne.after(dateTwo);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return returnVariable;
    }
}