package com.sameglobal.loto5_90;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class StringListAdapter extends RecyclerView.Adapter<StringListAdapter.LigneViewHolder> {

    private List<String> lignes;

    public StringListAdapter(List<String> lignes) {
        this.lignes = lignes;
    }

    @NonNull
    @Override
    public LigneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ligne_historique, parent, false);
        return new LigneViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LigneViewHolder holder, int position) {
        holder.tvLigne.setText(lignes.get(position));
    }

    @Override
    public int getItemCount() {
        return lignes.size();
    }

    static class LigneViewHolder extends RecyclerView.ViewHolder {
        TextView tvLigne;

        LigneViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLigne = itemView.findViewById(R.id.tvLigne);
        }
    }
}
