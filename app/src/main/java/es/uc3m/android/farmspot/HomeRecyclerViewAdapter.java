package es.uc3m.android.farmspot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewHolder> {
    private List<HomeCardElement> data;
    private LayoutInflater inflater;
    private Context context;
    private ItemListener listener;

    public HomeRecyclerViewAdapter(List<HomeCardElement> itemList, Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.data = itemList;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public HomeRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.home_card_view, parent, false);
        return new HomeRecyclerViewHolder(this, view);
    }

    @Override
    public void onBindViewHolder(HomeRecyclerViewHolder holder, final int position) {
        holder.bindData(data.get(position));
    }

    public void setItems(List<HomeCardElement> items ){
        data = items;
    }

}
