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

public class liste_produits_admin extends AppCompatActivity {

    private ListView lv;
    //definition des varaible qui apparaissent dans le php
    String id, reference, nom, prix, quantite, description;
    // url de la page sur laquelle s'affiche le php
    private static String JSON_URL = "https://run.mocky.io/v3/e18a969b-0c4d-4df9-9a3a-ffffe2e2fc2d";

    ArrayList<HashMap<String, String>> produitsliste;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_produits_admin);


        produitsliste = new ArrayList<>();
        lv = findViewById(R.id.listeView);

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
                JSONArray jsonArray = JsonObject.getJSONArray("produits");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    // affectation des champs dans le php au variables
                    id = jsonObject1.getString("id");
                    reference = jsonObject1.getString("reference");
                    nom =jsonObject1.getString("nom");
                    prix =jsonObject1.getString("prix");
                    quantite =jsonObject1.getString("quantite");
                    description =jsonObject1.getString("description");



                    // hashmap
                    HashMap<String, String> produits = new HashMap<>();

                    produits.put("id",id);
                    produits.put("reference",reference);
                    produits.put("nom",nom );
                    produits.put("prix",prix);
                    produits.put("quantite",quantite);
                    produits.put("description",description);

                    produitsliste.add(produits);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            //afficher les resultats

            ListAdapter adapter = new SimpleAdapter(
                    liste_produits_admin.this,produitsliste,
                    R.layout.row_layout_liste_produits_admin,
                    new String[]{"id","reference","nom","prix","quantite","description"},
                    new int[]{R.id.row_id_magasin, R.id.row_nom_magasin, R.id.row_siret_magasin, R.id.row_telephone_magasin, R.id.row_adresse_magasin, R.id.row_codepostale_magasin});
                    lv.setAdapter(adapter);
        }

    }
}







