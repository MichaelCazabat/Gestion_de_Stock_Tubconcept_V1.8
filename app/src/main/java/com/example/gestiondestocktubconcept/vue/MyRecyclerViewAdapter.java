package com.example.gestiondestocktubconcept.vue;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.gestiondestocktubconcept.R;
import com.example.gestiondestocktubconcept.modele.Profil;

import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private onItemClickListener mListener;

    public interface onItemClickListener{
        void onItemClick(int position);
        void onDeleteClick(int position);

    }

    public void setOnItemClickListener(onItemClickListener listener){
        mListener = listener;
    }

    public String input1;
    public String input2;
    public String input3;
    public String input4;
    public String input5;
    public String input6;

    private List<Profil> liste_produits;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private ImageView mDelete;

    // data is passed into the constructor
    MyRecyclerViewAdapter(Context context, List<Profil> data) {

        this.mInflater = LayoutInflater.from(context);
        this.liste_produits = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //String animal = mData.get(position);
        //holder.myTextView.setText(animal);


        holder.txt_categorie.setText(liste_produits.get(position).getCategorie());
        holder.txt_reference.setText(liste_produits.get(position).getReference());
        holder.txt_nom.setText(liste_produits.get(position).getNom());
        holder.txt_prix.setText(liste_produits.get(position).getPrixUnite().toString());
        holder.txt_quantite.setText(liste_produits.get(position).getQuantite().toString());
        holder.txt_description.setText(liste_produits.get(position).getDescription());

    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_categorie;
        TextView txt_reference;
        TextView txt_nom;
        TextView txt_prix;
        TextView txt_quantite;
        TextView txt_description;

        ViewHolder(View itemView) {
            super(itemView);
            txt_categorie = itemView.findViewById(R.id.tv_categorie_produit);
            txt_reference = itemView.findViewById(R.id.tv_reference_produit);
            txt_nom = itemView.findViewById(R.id.tv_nom_produit);
            txt_prix = itemView.findViewById(R.id.tv_prix_produit);
            txt_quantite = itemView.findViewById(R.id.tv_quantite_produit);
            txt_description = itemView.findViewById(R.id.tv_description_produit);
            mDelete = itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
            mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mListener.onDeleteClick(position);
                        }
                    }
                }
            });
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }


    // total number of rows
    @Override
    public int getItemCount() {
        return liste_produits.size();
    }

    // convenience method for getting data at click position
    Profil getItem(int id) {
        return liste_produits.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }



}