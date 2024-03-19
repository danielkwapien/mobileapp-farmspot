package es.uc3m.android.farmspot;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HomeViewAdapter extends RecyclerView.Adapter<HomeViewHolder> {
    private static List<String> data;

    public HomeViewAdapter(List<String> data) {
        this.data = data;
    }

    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_view, parent, false);
        return new HomeViewHolder(this, data, rowItem);
    }

    @Override
    public void onBindViewHolder(HomeViewHolder holder, int position) {
        holder.textView.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}
