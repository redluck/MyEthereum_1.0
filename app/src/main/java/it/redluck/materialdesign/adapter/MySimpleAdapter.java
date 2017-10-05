package it.redluck.materialdesign.adapter;

import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import it.redluck.materialdesign.R;

//Creare un SimpleAdapter è utile se, ad esempio, abbiamo bisogno di assegnare diversi colori ai singoli list item in base a determinate caratteristiche
public class MySimpleAdapter extends SimpleAdapter{

	/*----------------------------------------------------------------------------------------------------+
	| Costruttore                                                                                         |
	+----------------------------------------------------------------------------------------------------*/
	public MySimpleAdapter(Context context, List<HashMap<String, String>> items, int resource, String[] from, int[] to){
		super(context, items, resource, from, to);
	}

	/*----------------------------------------------------------------------------------------------------+
	| getView()                                                                                           |
	+----------------------------------------------------------------------------------------------------*/
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		View view = super.getView(position, convertView, parent);
		
		//TextView relativa al profitto del singolo list item
		TextView tv = (TextView) view.findViewById(R.id.profit);
		//Prendiamo in considerazione solo la parte numerica
		String profit_string = tv.getText().toString().substring(10);
		double profit_double = Double.parseDouble(profit_string);
		
		if(profit_double >= 0){
			view.setBackgroundResource(R.drawable.textview_background_green);
		}
		else{
			view.setBackgroundResource(R.drawable.textview_background_red);
		}
		return view;
	}
}