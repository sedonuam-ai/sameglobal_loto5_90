package com.sameglobal.loto5_90;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class GrillesAdapter extends RecyclerView.Adapter<GrillesAdapter.GrilleViewHolder> {

    private List<int[]> grilles;
    private GenerateurSysteme generateurSysteme;

    public GrillesAdapter(List<int[]> grilles, GenerateurSysteme generateurSysteme) {
        this.grilles = grilles;
        this.generateurSysteme = generateurSysteme;
    }

    @NonNull
    @Override
    public GrilleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grille, parent, false);
        return new GrilleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GrilleViewHolder holder, int position) {
        int[] grille = grilles.get(position);
        holder.tvNumeroGrille.setText("Grille " + (position + 1) + " :");
        holder.tvNumeros.setText(generateurSysteme.formaterGrille(grille));
    }

    @Override
    public int getItemCount() {
        return grilles.size();
    }

    static class GrilleViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumeroGrille;
        TextView tvNumeros;

        GrilleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNumeroGrille = itemView.findViewById(R.id.tvNumeroGrille);
            tvNumeros = itemView.findViewById(R.id.tvNumeros);
        }
    }
}
