package es.uc3m.android.farmspot;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewHolder> {
    List<HomeCardElement> data;
    private final LayoutInflater inflater;
    private Context context;
    final ItemListener listener;

    public HomeRecyclerViewAdapter(List<HomeCardElement> itemList, Context context, ItemListener listener) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.data = itemList;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @NonNull
    @Override
    public HomeRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.home_card_view, parent, false);
        return new HomeRecyclerViewHolder(this, view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeRecyclerViewHolder holder, int position) {
        holder.bindData(data.get(position));
    }

    public void setItems(List<HomeCardElement> items ){
        data = items;
    }

}
