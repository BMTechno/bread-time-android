package com.bread.time.breadtime;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.bread.time.breadtime.database.DatabaseHelper;
import com.google.firebase.messaging.FirebaseMessaging;

public class ConfigActivity extends AppCompatActivity {

    Switch sw_bread, sw_promo, sw_recipe, sw_products;
    DatabaseHelper taskkeeperdb;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        // Check Switch
        checkSwitch();

        // Switch Actions
        switchNotifications();

        // Top Home Button to go back to previous activity
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void checkSwitch(){

        sw_bread = (Switch) findViewById(R.id.switch_bread_notification);
        sw_promo = (Switch) findViewById(R.id.switch_promo_notification);
        sw_recipe = (Switch) findViewById(R.id.switch_recipe_notification);
        sw_products = (Switch) findViewById(R.id.switch_products_notification);

        //Verify if FCM Service Topic is active
        taskkeeperdb = new DatabaseHelper(this);
        db = taskkeeperdb.getReadableDatabase();
        Cursor result = db.rawQuery("select * from Notification", null);
        if (result != null) {
            while (result.moveToNext()) {
                String name = result.getString(result.getColumnIndex("TOPIC"));
                boolean value = result.getInt(result.getColumnIndex("ACTIVATE"))>0;
                switch (name) {
                    case "Fornadas":
                        if (value) {
                            sw_bread.setChecked(true);
                        } else {
                            sw_bread.setChecked(false);
                        }
                        break;
                    case "Promocoes":
                        if (value) {
                            sw_promo.setChecked(true);
                        } else {
                            sw_promo.setChecked(false);
                        }
                        break;
                    case "Receitas":
                        if (value) {
                            sw_recipe.setChecked(true);
                        } else {
                            sw_recipe.setChecked(false);
                        }
                        break;
                    case "Produtos":
                        if (value) {
                            sw_products.setChecked(true);
                        } else {
                            sw_products.setChecked(false);
                        }
                        break;
                    default:
                        break;
                }
            }
            if (!result.isClosed()) {
                result.close();
            }
        }
    }

    // Handle Multiple Switch Actions
    public void switchNotifications(){
        taskkeeperdb = new DatabaseHelper(this);

        // Switch Fornada
        sw_bread = (Switch) findViewById(R.id.switch_bread_notification);
        sw_bread.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                db = taskkeeperdb.getWritableDatabase();
                ContentValues cv = new ContentValues();
                if(isChecked){
                    FirebaseMessaging.getInstance().subscribeToTopic("bread");
                    cv.put("ACTIVATE", true);
                    db.update("Notification", cv, "TOPIC='Fornadas'", null);
                    cv.clear();
                }else{
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("bread");
                    cv.put("ACTIVATE", false);
                    db.update("Notification", cv, "TOPIC='Fornadas'", null);
                    cv.clear();
                }
            }
        });

        // Switch Ofertas
        sw_promo = (Switch) findViewById(R.id.switch_promo_notification);
        sw_promo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                db = taskkeeperdb.getWritableDatabase();
                ContentValues cv = new ContentValues();
                if(isChecked){
                    FirebaseMessaging.getInstance().subscribeToTopic("promo");
                    cv.put("ACTIVATE", true);
                    db.update("Notification", cv, "TOPIC='Promocoes'", null);
                    cv.clear();
                }else{
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("promo");
                    cv.put("ACTIVATE", false);
                    db.update("Notification", cv, "TOPIC='Promocoes'", null);
                    cv.clear();
                }
            }
        });

        // Switch Receitas
        sw_recipe = (Switch) findViewById(R.id.switch_recipe_notification);
        sw_recipe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                db = taskkeeperdb.getWritableDatabase();
                ContentValues cv = new ContentValues();
                if(isChecked){
                    FirebaseMessaging.getInstance().subscribeToTopic("recipe");
                    cv.put("ACTIVATE", true);
                    db.update("Notification", cv, "TOPIC='Receitas'", null);
                    cv.clear();
                }else{
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("recipe");
                    cv.put("ACTIVATE", false);
                    db.update("Notification", cv, "TOPIC='Receitas'", null);
                    cv.clear();
                }
            }
        });

        // Switch Produtos
        sw_products = (Switch) findViewById(R.id.switch_products_notification);
        sw_products.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                db = taskkeeperdb.getWritableDatabase();
                ContentValues cv = new ContentValues();
                if(isChecked){
                    FirebaseMessaging.getInstance().subscribeToTopic("products");
                    cv.put("ACTIVATE", true);
                    db.update("Notification", cv, "TOPIC='Produtos'", null);
                    cv.clear();
                }else{
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("products");
                    cv.put("ACTIVATE", false);
                    db.update("Notification", cv, "TOPIC='Produtos'", null);
                    cv.clear();
                }
            }
        });
    }
}
