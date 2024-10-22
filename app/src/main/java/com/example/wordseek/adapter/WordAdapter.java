package com.example.wordseek.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wordseek.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordViewHolder> implements Filterable {
    Context context;
    private WordAdapterInterface wordAdapterInterface = null;
    private final List<String> words;
    private List<String> allWords;

    public WordAdapter(Context context, List<String> words, WordAdapterInterface wordAdapterInterface){
        this.words = words;
        this.context = context;
        this.allWords = new ArrayList<>(words);
        this.wordAdapterInterface = wordAdapterInterface;
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.words_adapter_items, parent, false);
        return new WordAdapter.WordViewHolder(view, wordAdapterInterface);
    }


    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        if (words.get(position) != null){
            holder.bind(words.get(position));
        }
    }

    public List<String> getAllWords(){
        return words;
    }

    public void addWord(String word){
        words.add(word);
        notifyItemInserted(words.size()-1);
    }

    public void addAll(List<String> newItems) {
        words.addAll(newItems);
    }

    public void clear() {
        words.clear();
    }

    public void delete(int position){
        words.remove(position);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return words.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<String> filteredList = new ArrayList<>();
            if (charSequence.toString().isEmpty()){
                filteredList.addAll(allWords);
            } else{
                for (String word: allWords){
                    if (word.toLowerCase().contains(charSequence.toString().toLowerCase())){
                        filteredList.add(word);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            words.clear();
            words.addAll((Collection<? extends String>) filterResults.values);
            notifyDataSetChanged();
        }
    };


    public static class WordViewHolder extends RecyclerView.ViewHolder{
        private final TextView word;
        private final ImageView deleteWord;

        public WordViewHolder(@NonNull View itemView, WordAdapterInterface wordAdapterInterface) {
            super(itemView);
            this.word = itemView.findViewById(R.id.wordItem);
            this.deleteWord = itemView.findViewById(R.id.deleteWord);
            deleteWord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (wordAdapterInterface != null) {
                        int position = getAbsoluteAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            wordAdapterInterface.wordAdapterInterfaceDeleteWord(position);
                        }
                    }
                }
            });
            word.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (wordAdapterInterface != null) {
                        int position = getAbsoluteAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            wordAdapterInterface.wordAdapterInterfaceOnClick(position);
                        }
                    }
                }
            });
        }
        public void bind(String word){
            this.word.setText(word);
        }
    }
}
