package dicoding.adrian.submission4.Favorite.MovieFavorite.Adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

import dicoding.adrian.submission4.Movie.MovieItems;
import dicoding.adrian.submission4.R;

public class MovieFavoriteAdapter extends RecyclerView.Adapter<MovieFavoriteAdapter.MovieFavoriteViewHolder> {

    private ArrayList<MovieItems> listMovies = new ArrayList<>();

    private Activity activity;

    public MovieFavoriteAdapter(Activity activity) {
        this.activity = activity;
    }

    public ArrayList<MovieItems> getListMovies() {
        return listMovies;
    }

    public void setListMovie(ArrayList<MovieItems> listMovies) {
        if (listMovies.size() > 0) {
            this.listMovies.clear();
        }
        this.listMovies.addAll(listMovies);
        notifyDataSetChanged();
    }

    public void addItem(MovieItems movieItems) {
        this.listMovies.add(movieItems);
        notifyItemInserted(listMovies.size() - 1);
    }

    public void updateItem(int position, MovieItems movieItems) {
        this.listMovies.set(position, movieItems);
        notifyItemChanged(position, movieItems);
    }

    public void removeItem(int position) {
        this.listMovies.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listMovies.size());
    }

    @NonNull
    @Override
    public MovieFavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_movie, parent, false);
        return new MovieFavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieFavoriteAdapter.MovieFavoriteViewHolder holder, int position) {
        holder.tvTitle.setText(listMovies.get(position).getTitle());
        holder.tvOverview.setText(listMovies.get(position).getOverview());
        String uri = "https://image.tmdb.org/t/p/original" + listMovies.get(position).getPoster();
        Glide.with(holder.itemView.getContext())
                .load(uri)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.pgMovie.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.ivPoster);
    }

    @Override
    public int getItemCount() {
        return listMovies.size();
    }

    public class MovieFavoriteViewHolder extends RecyclerView.ViewHolder {
        final TextView tvTitle, tvOverview;
        final ImageView ivPoster;
        final ProgressBar pgMovie;

        MovieFavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_item_title_favorite_movie);
            tvOverview = itemView.findViewById(R.id.tv_item_overview_favorite_movie);
            ivPoster = itemView.findViewById(R.id.img_item_poster_favorite_movie);
            pgMovie = itemView.findViewById(R.id.progressBar_item_favorite_movie);
        }
    }
}