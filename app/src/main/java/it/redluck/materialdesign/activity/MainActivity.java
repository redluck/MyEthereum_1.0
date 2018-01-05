package it.redluck.materialdesign.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import it.redluck.materialdesign.R;
import it.redluck.materialdesign.async_tasks.GetCoinbaseHistoricalJSONTask2;
import it.redluck.materialdesign.async_tasks.OnTaskCompleted;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener, OnTaskCompleted {

    private Toolbar toolbar;
    private FragmentDrawer fragmentDrawer;

    //ArrayList contenente i dati da passare al fragment in cui creeremo il grafico
    private ArrayList<HashMap<Date, Double>> dataForGraph;

    /*----------------------------------------------------------------------------------------------------*
    | onCreate()                                                                                          |
    *----------------------------------------------------------------------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Impostazione della Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Impostazione del navigation drawer
        fragmentDrawer = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        fragmentDrawer.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        fragmentDrawer.setFragmentDrawerListener(this);

        //Avviamo il task asincrono per caricare i dati del grafico (da usare poi nel fragment )
        GetCoinbaseHistoricalJSONTask2 task = new GetCoinbaseHistoricalJSONTask2(this, this);
        task.execute();

        //Visualizziamo la prima voce del navigation drawer al lancio dell'applicazione
        onDrawerItemSelected(0);
    }

    /*----------------------------------------------------------------------------------------------------*
    | onCreateOptionsMenu()                                                                               |
    | creazione del menù tradizionale                                                                     |
    *----------------------------------------------------------------------------------------------------*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /*----------------------------------------------------------------------------------------------------*
    | onOptionsItemSelected()                                                                             |
    | click su voce del menù tradizionale                                                                 |
    *----------------------------------------------------------------------------------------------------*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.refresh){
        }
        else {
        }
        return super.onOptionsItemSelected(item);
    }

    /*----------------------------------------------------------------------------------------------------*
    | onDrawerItemSelected()                                                                              |
    | click su voce del menù laterale                                                                     |
    | override del metodo dell'interfaccia FragmentDrawer.FragmentDrawerListener                          |
    *----------------------------------------------------------------------------------------------------*/
    @Override
    public void onDrawerItemSelected(int position) {
        Fragment fragment = null;
        String tag = "";
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new Fragment02();
                tag = "frag02";
                break;
            case 1:
                fragment = new Fragment01();
                tag = "frag01";
                break;
            case 2:
                fragment = new Fragment03();
                //Passiamo al fragment i dati per creare il grafico
                Bundle bundle = new Bundle();
                bundle.putSerializable("data_for_graph", dataForGraph);
                fragment.setArguments(bundle);
                tag = "frag03";
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment, tag);
            fragmentTransaction.commit();

            //Impostazione del titolo della toolbar
            getSupportActionBar().setTitle(title);
        }
    }

    /*----------------------------------------------------------------------------------------------------*
	| Implementazione del metodo dell'interfaccia creata                                                  |
	*----------------------------------------------------------------------------------------------------*/
    @Override
    public void useInterfaceParameters(ArrayList<HashMap<Date, Double>> data){
        //Otteniamo i dati da passare al fragment dove creeremo il grafico
        this.dataForGraph = data;
    }
}