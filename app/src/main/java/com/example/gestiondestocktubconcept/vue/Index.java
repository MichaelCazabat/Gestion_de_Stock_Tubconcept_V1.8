package com.example.gestiondestocktubconcept.vue;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gestiondestocktubconcept.R;
import com.example.gestiondestocktubconcept.controleur.Controle;

public class Index extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {            // au lancement de l'application, ce qu'il y a dans les crochets va s'executer.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index);
        init();
        this.controle = Controle.getInstance();

        init();
    }

    /* ++propriétés++ */

    private Button btn_admin;
    private Button btn_commercant;
    private Controle controle;
    /*  --propriétés-- */






    private void init() {       //initialisation de l'application




        /* ++intitalisation des liens avec les objects graphiques++ */

        Button btn_admin = (Button)findViewById(R.id.btn_admin);
        Button btn_commercant = (Button)findViewById(R.id.btn_commercants);

        /* --intitalisation des liens avec les objects graphiques-- */



        /* ++ click sur les buttons pour changer d'activity ++ */

        //startActivity(new Intent(CurrentActivityName.this, NewActivityName.class)); permet de changer d'activité en 1 ligne


    btn_admin.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent i = new Intent(Index.this, Connexion_admin.class);
        startActivity(i);
        }
    });

    btn_commercant.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent j = new Intent(Index.this, Page_login_commercant.class);
            startActivity(j);
        }
    });
        /* -- click sur les buttons pour changer d'activity -- */

    }
}








