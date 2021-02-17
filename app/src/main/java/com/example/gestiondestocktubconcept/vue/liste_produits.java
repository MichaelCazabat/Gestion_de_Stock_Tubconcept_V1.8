package com.example.gestiondestocktubconcept.vue;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gestiondestocktubconcept.R;
import com.example.gestiondestocktubconcept.modele.Profil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class liste_produits extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {

    List<Profil> liste_produits;
    MyRecyclerViewAdapter adapter;


    Spinner spinner;
    String URL = "https://run.mocky.io/v3/88daf293-af6f-44cc-949f-2d2278d47ff6";
    ArrayList<String> liste_categorie;
    String categorie;

    StringBuilder data = new StringBuilder();


    TextView txt_categorie_produit;
    TextView txt_reference_produit;
    TextView txt_nom_produit;
    TextView txt_prix_produit;
    TextView txt_quantite_produit;
    TextView txt_description_produit;

    EditText value_categorie;
    EditText value_reference;
    EditText value_nom;
    EditText value_prix;
    EditText value_quantite;
    EditText value_description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_produits);
        liste_categorie = new ArrayList<>();
        spinner = (Spinner) findViewById(R.id.spinner);
        loadSpinnerData(URL);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                categorie = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });


        //value_categorie =  findViewById(R.id.txt_input_categorie);
        value_reference = findViewById(R.id.txt_input_reference);
        value_nom = findViewById(R.id.txt_input_nom);
        value_prix = findViewById(R.id.txt_input_prix);
        value_quantite = findViewById(R.id.txt_input_quantite);
        value_description = findViewById(R.id.txt_input_description);


        // Données pour remplir le RecyclerView :
        liste_produits = new ArrayList<>();


        // set up le RecyclerView:
        RecyclerView recyclerView = findViewById(R.id.rv_produits);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        adapter = new MyRecyclerViewAdapter(this, liste_produits);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);


        adapter.setOnItemClickListener(new MyRecyclerViewAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //ici, le code va s'executer sur le click d'un ligne
            }

            @Override
            public void onDeleteClick(int position) {
                // ici, le code va s'effectuer sur le click de l'image "delete"
                liste_produits.remove(position);
                adapter.notifyItemRemoved(position);
            }
        });

    }

    public boolean isEmpty(EditText editText) {
        if (editText.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Veuillez remplir tous les champs", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void loadSpinnerData(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("produits");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        categorie = jsonObject1.getString("nom_sous_categorie");
                        liste_categorie.add(categorie);
                    }

                    spinner.setAdapter(new ArrayAdapter<String>(liste_produits.this, android.R.layout.simple_spinner_dropdown_item, liste_categorie));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }

    public void onButtonClick(View view) {

        if (isEmpty(value_description) && isEmpty(value_nom) && isEmpty(value_prix) && isEmpty(value_quantite) && isEmpty(value_reference)) {
            Double value_prix_double = Double.parseDouble(value_prix.getText().toString().replace(",", "."));
            Integer value_quantite_int = Integer.parseInt(value_quantite.getText().toString().replace(",", "."));
            ajout_un_item(categorie, value_reference.getText().toString(), value_nom.getText().toString(), value_prix_double, value_quantite_int, value_description.getText().toString());
            // : 09/02/2021 quand tu mets une valeur a virgule dans quantité ou prix ca crash , convert la virgule en un point
            //résolu


        } else {
            Toast.makeText(this, "Veuillez entrer de valeurs valides", Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void onItemClick(View view, int position) {
        Log.i("message", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
    }

    private void ajout_un_item(String categorie, String reference, String nom, Double prix, Integer quantite, String description) {
        Profil item = new Profil(categorie, reference, nom, prix, quantite, description);
        int insertIndex = 0;
        liste_produits.add(insertIndex, item);
        adapter.notifyItemInserted(insertIndex);


    }


    public void export(View view) {


        //   // /*  ++Creation des données++  */
//
        //   int nbr_produits = adapter.getItemCount();
        //   for (int s = 0; s < nbr_produits; s++) {
        //       data.append("\n"+String.valueOf(liste_produits.get(s).convertToJSONArray()));
        //       Log.i("message", String.valueOf(liste_produits.get(s).convertToJSONArray()));
//
        //   }
//
        //   String data1 = data.toString();
        //   String data2 = data1.replaceAll("]",",");
        //   String data3 = data2.replaceAll("\\[","");
        //   String data4 = data3.replaceAll("\"","");
        //   Log.i("message", data4);
        //   // set la lenght a 0 c'est mieu pour la mémoire que d'en refaire une.
        //   data.setLength(0);
        //   data.append("Categorie,Reference,Nom,Prix,Quantites,Description");
        //   data.append(data4);
        //   /*  --Creation des données--  */
//
        //   try {
        //       /*  ++Sauvegarde des données dans l'appareil++ */
//
        //       FileOutputStream out = openFileOutput("data.csv", Context.MODE_PRIVATE);
        //       out.write((data.toString()).getBytes());
        //       out.close();
//
        //       /*  --Sauvegarde des données dans l'appareil-- */
//
        //       /*  ++Export des données++  */
//
        //       Context context = getApplicationContext();
        //       File filelocation = new File(getFilesDir(), "data.csv");
        //       Uri path = FileProvider.getUriForFile(context, "com.example.gestiondestocktubconcept.FileProvider", filelocation);
        //       Intent fileIntent = new Intent(Intent.ACTION_SEND);
        //       fileIntent.setType("text/csv");
        //       fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Data");
        //       fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //       fileIntent.putExtra(Intent.EXTRA_STREAM, path);
        //       startActivity(Intent.createChooser(fileIntent, "Send mail"));
//
        //       /*  --Export des données--  */
        //   } catch (Exception e) {
        //       e.printStackTrace();
        //   }






        String s = "ALEEEEEEEEED";
        String url = "http://localhost/Test_Json/TEST.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(liste_produits.this,response.trim(),Toast.LENGTH_LONG).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(liste_produits.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<String, String>();
                params.put("data",s);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(liste_produits.this);
        requestQueue.add(stringRequest);

    }

}

