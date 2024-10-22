package com.example.wordseek.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wordseek.R;
import com.example.wordseek.entities.Quotable;

import java.util.List;

public class QuotableAdapter extends RecyclerView.Adapter<QuotableAdapter.QuotableViewHolder> {
    Context context;
    private QuotableAdapterInterface quotableAdapterInterface = null;
    private final List<Quotable> quotables;

    public QuotableAdapter(Context context, List<Quotable> quotables, QuotableAdapterInterface quotableAdapterInterface){
        this.quotables = quotables;
        this.context = context;
        this.quotableAdapterInterface = quotableAdapterInterface;
    }

    @NonNull
    @Override
    public QuotableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.quotables_adapter_items, parent, false);
        return new QuotableAdapter.QuotableViewHolder(view, quotableAdapterInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull QuotableViewHolder holder, int position) {
        if (quotables.get(position) != null){
            holder.bind(quotables.get(position));
        }
    }
    public List<Quotable> getAllQuotes(){
        return quotables;
    }

    public void addQuote(Quotable quotable){
        quotables.add(quotable);
        notifyItemInserted(quotables.size()-1);
    }

    public void addAll(List<Quotable> newItems) {
        quotables.addAll(newItems);
    }

    public void clear() {
        quotables.clear();
    }

    public void delete(int position){
        quotables.remove(position);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return quotables.size();
    }


    public static class QuotableViewHolder extends RecyclerView.ViewHolder{
        private final TextView quotable;

        public QuotableViewHolder(@NonNull View itemView, QuotableAdapterInterface quotableAdapterInterface) {
            super(itemView);
            quotable = itemView.findViewById(R.id.quotableItem);
            quotable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (quotableAdapterInterface != null) {
                        int position = getAbsoluteAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            quotableAdapterInterface.quotableAdapterInterfaceOnClick(position);
                        }
                    }
                }
            });
        }
        public void bind(Quotable quotable){
            this.quotable.setText(quotable.getQuotable());
        }
    }
}
