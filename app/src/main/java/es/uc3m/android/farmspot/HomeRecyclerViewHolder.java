package es.uc3m.android.farmspot;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HomeRecyclerViewHolder extends RecyclerView.ViewHolder {

    public final HomeRecyclerViewAdapter parent;
    public final ImageView image;
    public final TextView title, description;

    public HomeRecyclerViewHolder(HomeRecyclerViewAdapter parent, View view) {
        super(view);
        this.image = view.findViewById(R.id.homeCardImage);
        this.title = view.findViewById(R.id.cardTitle);
        this.description = view.findViewById(R.id.cardDescription);
        this.parent = parent;
    }

    void bindData(final HomeCardElement item) {
        title.setText(item.getTitle());
        description.setText(item.getDescription());
    }

}
