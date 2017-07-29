package com.bread.time.breadtime;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bread.time.breadtime.database.DatabaseHelper;
import com.bread.time.breadtime.fragmentOthers.AboutFragment;
import com.bread.time.breadtime.fragmentOthers.RestaurantFragment;
import com.bread.time.breadtime.fragmentsFeed.FornadaFragment;
import com.bread.time.breadtime.fragmentsFeed.ProdutosFragment;
import com.bread.time.breadtime.fragmentsFeed.PromocoesFragment;
import com.bread.time.breadtime.fragmentsFeed.ReceitasFragment;
import com.bread.time.breadtime.fragmentsProducts.GalleryFragment;
import com.bread.time.breadtime.fragmentsFeed.MainFragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient = null;
    NavigationView navigationView = null;
    Toolbar toolbar = null;
    private MenuItem mSearchAction;
    private boolean isSearchOpened = false;
    private EditText editSearch;
    SharedPreferences prefs = null;
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get Token
        FirebaseInstanceId.getInstance().getToken();

        // Verify Google Play Services
        checkGooglePlayServices();

        // Set loaded fragment
//        if(savedInstanceState == null){
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.add(R.id.fragment_container, new MainFragment());
//            ft.commit();
//        }

        // Set Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set Navigation Drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // Set the default checked item when navigation bar opens
        navigationView.getMenu().getItem(0).setChecked(true);

        // Set Tab and ViewPager
        viewPager = (ViewPager) findViewById(R.id.pagerView);
        viewPager.setAdapter(new CustomAdapter(getSupportFragmentManager(),
                MainActivity.this));
        tabLayout = (TabLayout) findViewById(R.id.tabView);
        tabLayout.setupWithViewPager(viewPager);

        // Check if app is running for the first time
        prefs = getSharedPreferences("com.bread.time.breadtime", MODE_PRIVATE);

    }

    // View Pager + Table Layout
    private class CustomAdapter extends FragmentPagerAdapter {
        private int[] fragments = { R.drawable.ic_home,
                                    R.drawable.ic_whatshot,
                                    R.drawable.ic_local_offer_white,
                                    R.drawable.ic_shopping_basket,
                                    R.drawable.ic_local_dining };
        private Context context;
        public CustomAdapter(FragmentManager fm, Context context){
            super(fm);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    Log.d("GG",""+position+"");
                    return MainFragment.newInstance();
                case 1:
                    Log.d("GG",""+position+"");
                   return FornadaFragment.newInstance();
                case 2:
                    Log.d("GG",""+position+"");
                    return PromocoesFragment.newInstance();
                case 3:
                    Log.d("GG",""+position+"");
                    return ProdutosFragment.newInstance();
                case 4:
                    Log.d("GG",""+position+"");
                    return ReceitasFragment.newInstance();
                default:
                    Log.d("GG",""+position+"");
                    return ProdutosFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Drawable image = ContextCompat.getDrawable(context, fragments[position]);
            image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
            SpannableString sb = new SpannableString(" ");
            ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
            sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return sb;
        }
    }


    // Navigation Drawer Method
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            // To call the search if drawer isn't opened
            if(isSearchOpened) {
                handleMenuSearch();
                return;
            }
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Subscribe user to all FCM topics if app is on its first run
        if (prefs.getBoolean("firstrun", true)) {
            // Create database
            new DatabaseHelper(this);

           // Set FCM Topics active
            FirebaseMessaging.getInstance().subscribeToTopic("Fornadas");
            FirebaseMessaging.getInstance().subscribeToTopic("Promocoes");
            FirebaseMessaging.getInstance().subscribeToTopic("Receitas");
            FirebaseMessaging.getInstance().subscribeToTopic("Produtos");

            // Not the first app run anymore
            prefs.edit().putBoolean("firstrun", false).apply();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_search, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mSearchAction = menu.findItem(R.id.action_search);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_search:
                handleMenuSearch();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (id == R.id.nav_camera) {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, MainFragment.newInstance())
                    .commit();

        } else if (id == R.id.nav_gallery) {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, GalleryFragment.newInstance())
                    .commit();

        } else if (id == R.id.nav_restaurant) {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, RestaurantFragment.newInstance())
                    .commit();

        } else if (id == R.id.nav_about) {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, AboutFragment.newInstance())
                    .commit();

        } else if (id == R.id.nav_notifications) {
            Intent config = new Intent(this, ConfigActivity.class);
            startActivity(config);

        } else if (id == R.id.nav_about_app){
            Intent aboutApp = new Intent(this, InfoAppActivity.class);
            startActivity(aboutApp);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Google Play Services Methods
    private boolean checkGooglePlayServices() {

        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        int REQUEST_CODE_RECOVER_PLAY_SERVICES = 200;

        if (requestCode == REQUEST_CODE_RECOVER_PLAY_SERVICES) {

            if (resultCode == RESULT_OK) {
                // Make sure the app is not already connected or attempting to connect
                if (!mGoogleApiClient.isConnecting() &&
                        !mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }
            }else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Google Play Services must be installed.",
                        Toast.LENGTH_SHORT).show();
                  finish();
            }
        }
    }
    @Override
    public void onConnectionSuspended(int arg0) {
        // what should i do here ? should i call mGoogleApiClient.connect() again ? ?
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    //Search Method
    protected void handleMenuSearch(){
        ActionBar action = getSupportActionBar(); //get the actionbar

        if(isSearchOpened){ //test if the search is open
            if(action != null) {
                action.setDisplayShowCustomEnabled(false); //disable a custom view inside the actionbar
                action.setDisplayShowTitleEnabled(true); //show the title in the action bar
            }
            //hides the keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editSearch.getWindowToken(), 0);

            //add the search icon in the action bar
            mSearchAction.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_search_white, null));

            isSearchOpened = false;
        } else { //open the search entry
            if(action != null) {
                action.setDisplayShowCustomEnabled(true); //enable it to display a
                // custom view in the action bar.

                action.setCustomView(R.layout.search_bar);//add the custom view
                action.setDisplayShowTitleEnabled(false); //hide the title

                editSearch = (EditText) action.getCustomView().findViewById(R.id.edtSearch); //the text editor
            }
            //this is a listener to do a search when the user clicks on search button
            editSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        doSearch();
                        return true;
                    }
                    return false;
                }
            });


            editSearch.requestFocus();

            //open the keyboard focused in the edtSearch
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editSearch, InputMethodManager.SHOW_IMPLICIT);


            //add the close icon
            mSearchAction.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_close, null));

            isSearchOpened = true;
        }
    }
    private void doSearch() {

    }

    // Check Internet Connection
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}