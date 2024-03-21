package es.uc3m.android.farmspot;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HomeRecyclerViewHolder extends RecyclerView.ViewHolder {

    public final HomeRecyclerViewAdapter parent;
    public final ImageView image;
    public final TextView title, price, location, category;

    public final LinearLayout container;



    public HomeRecyclerViewHolder(HomeRecyclerViewAdapter parent, View view) {
        super(view);
        this.image = view.findViewById(R.id.homeCardImage);
        this.title = view.findViewById(R.id.cardTitle);
        this.price = view.findViewById(R.id.cardPrice);
        this.location = view.findViewById(R.id.cardLocation);
        this.category = view.findViewById(R.id.cardCategory);
        this.container = view.findViewById(R.id.homeContainer);
        this.parent = parent;
    }

    void bindData(final HomeCardElement item) {
        image.setImageResource(item.getImage());
        title.setText(item.getTitle());
        price.setText(item.getPrice());
        location.setText(item.getLocation());
        category.setText(item.getCategory());

        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.listener.onItemClick(item);
            }
        });


    }

}
