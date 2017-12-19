package br.com.livroandroid.carrosjava;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        Response.Listener, Response.ErrorListener, ClickRecyclerView_Interface {

    private ProgressBar progressBar;
    private final String url = "http://livrowebservices.com.br/rest/carros";
    private final String TAG = "Some TAG";
    private RequestQueue queue;
    private UserAdapter userList;
    private ListView list;
    private Spinner spinnerSearch;
    private TabLayout tabLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    RecyclerTesteAdapter adapter;
    private List<Carro> carroArrayList = new ArrayList<>();
    private FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        queue = CustomRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();
        userList = new UserAdapter(this, CustomRequestQueue.getInstance(this.getApplicationContext()).getImageLoader());
        //list = (ListView) findViewById(R.id.list);
        //list.setAdapter(userList);



        addRequest();

        setaRecyclerView();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void addRequest( ){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, this, this);
        jsonArrayRequest.setTag(TAG);
        queue.add(jsonArrayRequest);

       // progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResponse(Object response){
        // List<Carro> tmpCarroList = new ArrayList<>();
        /*try {
            JSONArray jsonArray = (JSONArray) response;
            for(int i = 0; i < jsonArray.length(); i++){
                Carro carro = new Carro();
                JSONObject userJson = jsonArray.getJSONObject(i);
                carro.setId(userJson.getInt("id"));
                carro.setName(userJson.getString("UserName"));
                tmpCarroList.add(carro);
            }

            userList.addNewList(tmpCarroList);
        }
        catch (JSONException e){
            e.printStackTrace();
            Toast.makeText(this, "Something went wrong (JSONException)",Toast.LENGTH_LONG).show();
        }*/

        //JsonArray jsonArray = (JsonArray) response;
        Type listType = new TypeToken<ArrayList<Carro>>(){}.getType();
        List<Carro> list = new Gson().fromJson(response.toString(), listType);

        carroArrayList.addAll(list);
        adapter.notifyDataSetChanged();

       // userList.addNewList(list);


       // progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        progressBar.setVisibility(View.GONE);
    }

    protected  void onStop(){
        super.onStop();
        if (queue!=null){
            queue.cancelAll(TAG);
        }
    }


    public void setaRecyclerView(){

        //Aqui é instanciado o Recyclerview
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_recyclerteste);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        adapter = new RecyclerTesteAdapter(this, carroArrayList, this, CustomRequestQueue.getInstance(this.getApplicationContext()).getImageLoader());
        mRecyclerView.setAdapter(adapter);
    }

    /**
     * Aqui é o método onde trata o clique em um item da lista
     */
    @Override
    public void onCustomClick(Object object) {
        Carro carro = (Carro) object;

        Toast.makeText(this, carro.getName(), Toast.LENGTH_LONG).show();
    }

}
