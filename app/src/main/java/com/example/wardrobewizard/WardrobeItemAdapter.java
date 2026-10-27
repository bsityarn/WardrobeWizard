package com.example.wardrobewizard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/** Shared adapter for wardrobe grid tiles and Styling Studio thumbnails. */
public class WardrobeItemAdapter extends RecyclerView.Adapter<WardrobeItemAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(WardrobeItem item);
    }

    private List<WardrobeItem> items;
    private final @LayoutRes int layoutRes;
    private final OnItemClickListener listener;

    public WardrobeItemAdapter(List<WardrobeItem> items, @LayoutRes int layoutRes, OnItemClickListener listener) {
        this.items = items;
        this.layoutRes = layoutRes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WardrobeItem item = items.get(position);
        if (holder.tvName != null) holder.tvName.setText(item.getName());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateData(List<WardrobeItem> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvWardrobeItemName);
        }
    }
}
