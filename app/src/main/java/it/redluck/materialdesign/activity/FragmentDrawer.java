package it.redluck.materialdesign.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import it.redluck.materialdesign.adapter.NavigationDrawerAdapter;
import it.redluck.materialdesign.model.NavigationDrawerItem;

import it.redluck.materialdesign.R;

public class FragmentDrawer extends Fragment {

    private RecyclerView recyclerView;
    private static String[] titles = null;
    private NavigationDrawerAdapter adapter;
    private DrawerLayout drawerLayout;
    private View containerView;
    private ActionBarDrawerToggle drawerToggle;
    //Listener collegato agli item del menù
    private FragmentDrawerListener fragmentDrawerListener;

    /*----------------------------------------------------------------------------------------------------*
    | onCreate()                                                                                          |
    *----------------------------------------------------------------------------------------------------*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Creazione di un array contenente le voci del menù
        titles = getActivity().getResources().getStringArray(R.array.navigation_drawer_labels);
    }

    /*----------------------------------------------------------------------------------------------------*
    | onCreateView()                                                                                      |
    *----------------------------------------------------------------------------------------------------*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Inflate del layout del navigation drawer
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        //La vista rappresentante la lista degli item
        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);
        //Adapter contenente la lista degli item
        //(nel caso delle RecyclerView l'adapter deve necessariamente estendere RecyclerView.Adapter<>)
        adapter = new NavigationDrawerAdapter(getActivity(), getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Creazione del listener per il click sugli elementi della lista
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener(){
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null) {
                    int item = rv.getChildPosition(child);
                    fragmentDrawerListener.onDrawerItemSelected(item);
                    drawerLayout.closeDrawer(containerView);
                }
                return false;
            }
            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            }
            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });
        return layout;
    }

    /*----------------------------------------------------------------------------------------------------*
    | getData()                                                                                           |
    | creazione della lista degli item del menù                                                           |
    *----------------------------------------------------------------------------------------------------*/
    public static List<NavigationDrawerItem> getData() {

        List<NavigationDrawerItem> data = new ArrayList<>();

        for (int i = 0; i < titles.length; i++) {
            NavigationDrawerItem item = new NavigationDrawerItem();
            item.setTitle(titles[i]);
            data.add(item);
        }
        return data;
    }

    /*----------------------------------------------------------------------------------------------------*
    | setUp()                                                                                             |
    | interazione tra il layout del navigation drawer e la toolbar                                        |
    *----------------------------------------------------------------------------------------------------*/
    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {

        containerView = getActivity().findViewById(fragmentId);
        this.drawerLayout = drawerLayout;

        //L'ActionBarDrawerToggle permette un'interazione appropriata tra il drawer e l'icona della action bar
        drawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);
        drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                drawerToggle.syncState();
            }
        });
    }

    /*====================================================================================================*
    | INTERFACE FragmentDrawerListener                                                                    |
    | interfaccia rappresentante il listener collegato agli item del menù                                 |
    | (da implementare nella MainActivity)                                                                |
    *====================================================================================================*/
    public interface FragmentDrawerListener {
        void onDrawerItemSelected(int position);
    }

    /*----------------------------------------------------------------------------------------------------*
    | setFragmentDrawerListener()                                                                         |
    | il listener collegato agli item del menù                                                            |
    *----------------------------------------------------------------------------------------------------*/
    public void setFragmentDrawerListener(FragmentDrawerListener listener) {
        this.fragmentDrawerListener = listener;
    }
}