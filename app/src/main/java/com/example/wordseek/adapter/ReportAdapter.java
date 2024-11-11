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

public class ReportAdapter  extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {
    Context context;
    private final List<Quotable> quotables;

    public ReportAdapter(Context context, List<Quotable> quotables){
        this.context = context;
        this.quotables = quotables;
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.quotables_adapter_items_report, parent, false);
        return new ReportAdapter.ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        Quotable quotable = quotables.get(position);
        if (quotable != null) {
            holder.date.setText(quotable.getDate());
            holder.quotable.setText(quotable.getQuotable());
            holder.userId.setText(quotable.getUserId().toString());
            holder.word.setText(quotable.getWord());
        }
    }

    @Override
    public int getItemCount() {
        return quotables.size();
    }

    public void addAll(List<Quotable> newItems) {
        quotables.addAll(newItems);
        notifyDataSetChanged();
    }

    public static class ReportViewHolder extends RecyclerView.ViewHolder{
        private final TextView userId, word, quotable, date;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            this.date = itemView.findViewById(R.id.reportDate);
            this.quotable = itemView.findViewById(R.id.reportQuotable);
            this.userId = itemView.findViewById(R.id.reportUserID);
            this.word = itemView.findViewById(R.id.reportWord);
        }
    }
}
