package com.example.gestiondestocktubconcept.vue;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

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
import java.util.List;

public class Mots_de_passe extends AppCompatActivity {


    public List<String> getMdp_liste() {
        return mdp_liste;
    }

    public List<String> getMail_liste() {
        return mail_liste;
    }

    public List<String> mdp_liste = new ArrayList<>();
    public List<String> mail_liste = new ArrayList<>();
    //definition des varaible qui apparaissent dans le php
    String id_utilisateur, pseudo, mail, motdepasse;



    // url de la page sur laquelle s'affiche le php
    private static String JSON_URL = "https://run.mocky.io/v3/9fd0eec4-4fb9-4e40-8f0d-df483cc75860";



    ArrayList<HashMap<String, String>> mots_de_passe_liste;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mots_de_passe_liste = new ArrayList<>();

        GetData getData = new GetData();
        getData.execute();

        Log.i("message", id_utilisateur);

    }


    public  class GetData extends AsyncTask<String, String, String> {


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
                JSONArray jsonArray = JsonObject.getJSONArray("utilisateur");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    // affectation des champs dans le php au variables
                    id_utilisateur = jsonObject1.getString("id_utilisateur");
                    pseudo = jsonObject1.getString("pseudo");
                    mail = jsonObject1.getString("mail");
                    motdepasse = jsonObject1.getString("motdepasse");


                    // hashmap
                    HashMap<String, String> utilisateurs = new HashMap<>();

                    utilisateurs.put("id_utilisateur", id_utilisateur);
                    utilisateurs.put("pseudo", pseudo);
                    utilisateurs.put("mail", mail);
                    utilisateurs.put("motdepasse", motdepasse);

                    mail_liste.add(mail);
                    mdp_liste.add(motdepasse);

                    mots_de_passe_liste.add(utilisateurs);



                }
            } catch (JSONException e) {
                e.printStackTrace();
            }



        }


    }


}







