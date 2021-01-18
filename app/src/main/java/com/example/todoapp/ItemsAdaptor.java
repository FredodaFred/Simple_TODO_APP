package com.example.todoapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

//Responsible for displayig data from the model intopo a row in the recycler view
public class ItemsAdaptor extends RecyclerView.Adapter<ItemsAdaptor.ViewHolder> {

    public interface OnLongClickListener{
        void onItemLongClicked(int position);
    }
    public interface OnClickListener{
        void onItemClicked(int position);
    }

    List<String> items;
    OnLongClickListener longClickListener; //check for long clicks
    OnClickListener clickListener; // check for reg clicks

    public ItemsAdaptor(List<String> items, OnLongClickListener longClickListener, OnClickListener clickListener){
        this.items = items;
        this.longClickListener = longClickListener;
        this.clickListener = clickListener;
    }
    /**
     * Creates each "view"
     * Uses an layout inflator to inflate the view
     * wraps it inside a view holder and returns it
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        //inflate view
        View todoView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1,
                parent, false);
        //return it
        return new ViewHolder(todoView);
    }

    /**
     * Takes data from its ordered position, then binds it to a particular "viewholder"
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //grab the item at the position
        String item = items.get(position);
        //bind the item into a specified view holder
        holder.bind(item);
    }

    /**
     * Number of items within the data(list of items)
     * @return the number of items
     */
    @Override
    public int getItemCount() {
        return items.size();
    }

    //Container to provide easy access to views that represent each row of the list
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(android.R.id.text1);
        }

        //Update view/text inside of the view holder with new data (String item)
        public void bind(String item) {
            tvItem.setText(item);
            tvItem.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v) {
                    //notify the listener which position was long pressed
                    longClickListener.onItemLongClicked(getAdapterPosition());
                    return true;
                }
            });
            tvItem.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    clickListener.onItemClicked(getAdapterPosition());
                }
            });
        }
    }
}
