package it.redluck.materialdesign.activity;

import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import it.redluck.materialdesign.R;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private Toolbar toolbar;
    private FragmentDrawer fragmentDrawer;

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
                fragment = new Fragment03();
                tag = "frag01";
                break;
            case 1:
                fragment = new Fragment02();
                tag = "frag02";
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
}