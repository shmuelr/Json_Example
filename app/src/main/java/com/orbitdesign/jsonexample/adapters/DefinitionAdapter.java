package com.orbitdesign.jsonexample.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.orbitdesign.jsonexample.R;
import com.orbitdesign.jsonexample.models.Definition;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sdros_000 on 6/14/2015.
 */
public class DefinitionAdapter extends RecyclerView.Adapter<DefinitionAdapter.DefinitionViewHolder> {


    protected static class DefinitionViewHolder extends RecyclerView.ViewHolder{

        public TextView textViewText, textViewAttribution;

        public DefinitionViewHolder(View itemView) {
            super(itemView);
            textViewText = (TextView)itemView.findViewById(R.id.textViewText);
            textViewAttribution = (TextView)itemView.findViewById(R.id.textViewAttribution);
        }

    }

    List<Definition> definitionList = new ArrayList<>();

    public void addDefinition(Definition newDefinition){
        definitionList.add(newDefinition);
        notifyDataSetChanged();
    }

    public void swapDefinitions(List<Definition> newDefinitionList){
        definitionList = newDefinitionList;
    }

    @Override
    public DefinitionViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.definition_layout, viewGroup, false);
        return new DefinitionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DefinitionViewHolder definitionViewHolder, int position) {

        Definition definition = definitionList.get(position);

        definitionViewHolder.textViewText.setText(definition.getText());
        definitionViewHolder.textViewAttribution.setText(definition.getAttribution());
    }

    @Override
    public int getItemCount() {
        return definitionList.size();
    }

}
