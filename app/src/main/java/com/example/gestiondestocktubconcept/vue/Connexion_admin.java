package com.example.gestiondestocktubconcept.vue;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gestiondestocktubconcept.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Connexion_admin extends AppCompatActivity{


    // url de la page sur laquelle s'affiche le php
    private static String JSON_URL = "http://aico2742.alwaysdata.net/PDO_requete_Json_admin.php";

    ArrayList<HashMap<String, String>> mots_de_passe_liste;
    ArrayList<HashMap<String, String>> produitsliste;

    String id_utilisateur, mail, motdepasse;

    List<String> mail_liste = new ArrayList<>();
    List<String> mdp_liste = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {            // au lancement de l'application, ce qu'il y a dans les crochets va s'executer.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connexion_admin);

        mots_de_passe_liste = new ArrayList<>();
        produitsliste = new ArrayList<>();

        Connexion_admin.GetData getData = new Connexion_admin.GetData();
        getData.execute();


        //TODO: mettre les profils administrateurs sur la base de données


        /* ++propriétées++ */
        EditText txt_id_admin = findViewById(R.id.txt_id_admin);
        EditText mdp_admin = findViewById(R.id.mdp_admin);
        Button btn_connexion_admin = findViewById(R.id.btn_connexion_admin);


        /* --propriétées-- */

        btn_connexion_admin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if(mail_liste.contains(txt_id_admin.getText().toString())){
                    Integer indexMail = mail_liste.indexOf(txt_id_admin.getText().toString());
                    String mdpHashe =sha1Hash(mdp_admin.getText().toString());
                    Log.i("message",mdpHashe);
                    if(mdp_liste.get(indexMail).equals(sha1Hash(mdp_admin.getText().toString()))){
                        Intent m = new Intent(Connexion_admin.this, choix_magasin_produits.class);
                        startActivity(m);
                    }else{
                        Toast.makeText(Connexion_admin.this, "Mauvais mot de pasee", Toast.LENGTH_SHORT).show();
                        Log.i("message","Mauvais mot de passe");
                    }

                }else{
                    Toast.makeText(Connexion_admin.this, "Mauvais identifiants", Toast.LENGTH_SHORT).show();
                    Log.i("message", "Mauvais identifiants");
                }

            }
        });
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
                JSONArray jsonArray = JsonObject.getJSONArray("administrateur");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    // affectation des champs dans le php au variables
                    id_utilisateur = jsonObject1.getString("id");
                    mail = jsonObject1.getString("mail");
                    motdepasse = jsonObject1.getString("mdp");


                    // hashmap
                    HashMap<String, String> utilisateurs = new HashMap<>();

                    utilisateurs.put("id", id_utilisateur);
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


    // parti qui convert les string en hashing sha1
    String sha1Hash( String toHash ) {
        String hash = null;
        try
        {
            MessageDigest digest = MessageDigest.getInstance( "SHA-1" );
            byte[] bytes = toHash.getBytes("UTF-8");
            digest.update(bytes, 0, bytes.length);
            bytes = digest.digest();

            // This is ~55x faster than looping and String.formating()
            hash = bytesToHex( bytes );
        }
        catch( NoSuchAlgorithmException e )
        {
            e.printStackTrace();
        }
        catch( UnsupportedEncodingException e )
        {
            e.printStackTrace();
        }
        return hash;
    }

    // http://stackoverflow.com/questions/9655181/convert-from-byte-array-to-hex-string-in-java
    final protected static char[] hexArray = "0123456789abcdef".toCharArray();
    public static String bytesToHex( byte[] bytes )
    {
        char[] hexChars = new char[ bytes.length * 2 ];
        for( int j = 0; j < bytes.length; j++ )
        {
            int v = bytes[ j ] & 0xFF;
            hexChars[ j * 2 ] = hexArray[ v >>> 4 ];
            hexChars[ j * 2 + 1 ] = hexArray[ v & 0x0F ];
        }
        return new String( hexChars );
    }
}
