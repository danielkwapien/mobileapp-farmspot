package es.uc3m.android.farmspot;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HomeRecyclerViewHolder extends RecyclerView.ViewHolder {

    public final HomeRecyclerViewAdapter parent;
    public static List<String> data;
    public final TextView textView;

    public HomeRecyclerViewHolder(HomeRecyclerViewAdapter parent, List<String> data, View view) {
        super(view);
        this.textView = view.findViewById(R.id.textview);
        this.parent = parent;
        this.data = data;

        view.setOnClickListener(view1 -> {
            int position = getLayoutPosition();
            data.remove(position);
            parent.notifyDataSetChanged();
        });

    }

}
