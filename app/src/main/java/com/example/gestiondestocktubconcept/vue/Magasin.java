package com.example.gestiondestocktubconcept.vue;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.gestiondestocktubconcept.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class Magasin extends AppCompatActivity {

    private ListView lv_magasin;
    //definition des varaible qui apparaissent dans le php
    String id_magasin, nom_magasin, siret, telephone, adresse, codepostale;
    // url de la page sur laquelle s'affiche le php
    private static String JSON_URL = "https://run.mocky.io/v3/49f43b01-7a95-44e2-9aaf-67bcd1194a7b";

    ArrayList<HashMap<String, String>> liste_magasin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.magasin);


        liste_magasin = new ArrayList<>();
        lv_magasin = findViewById(R.id.lv_magasin);

        GetData getData = new GetData();
        getData.execute();
    }

    public class GetData extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... strings) {
            String current = "";


            try {
                URL url;
                HttpURLConnection urlconnection = null;

                try {
                    url = new URL(JSON_URL);
                    urlconnection = (HttpURLConnection) url.openConnection();

                    InputStream in = urlconnection.getInputStream();
                    InputStreamReader isr = new InputStreamReader(in);

                    int data = isr.read();
                    while (data != -1) {
                        current += (char) data;
                        data = isr.read();

                    }

                    return current;

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (urlconnection != null) {
                        urlconnection.disconnect();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            return current;
        }



        protected void onPostExecute(String s) {
            try {
                //nom de la "table" dans le php
                JSONObject JsonObject = new JSONObject(s);
                JSONArray jsonArray = JsonObject.getJSONArray("magasin");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    // affectation des champs dans le php au variables
                    id_magasin = jsonObject1.getString("id_magasin");
                    nom_magasin = jsonObject1.getString("nom_magasin");
                    siret =jsonObject1.getString("siret");
                    telephone =jsonObject1.getString("telephone");
                    adresse =jsonObject1.getString("adresse");
                    codepostale =jsonObject1.getString("codepostale");



                    // hashmap
                    HashMap<String, String> magasin = new HashMap<>();

                    magasin.put("id_magasin",id_magasin);
                    magasin.put("nom_magasin",nom_magasin);
                    magasin.put("siret",siret );
                    magasin.put("telephone",telephone);
                    magasin.put("adresse",adresse);
                    magasin.put("codepostale",codepostale);

                    liste_magasin.add(magasin);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            //afficher les resultats

            ListAdapter adapter = new SimpleAdapter(
                    Magasin.this,liste_magasin,
                    R.layout.row_layout_magasin,
                    new String[]{"id_magasin","nom_magasin","siret","telephone","adresse","codepostale"},
                    new int[]{R.id.row_id_magasin, R.id.row_nom_magasin, R.id.row_siret_magasin, R.id.row_telephone_magasin, R.id.row_adresse_magasin, R.id.row_codepostale_magasin});
            lv_magasin.setAdapter(adapter);
        }

    }
}







