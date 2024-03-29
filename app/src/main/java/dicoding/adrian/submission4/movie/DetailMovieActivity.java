package dicoding.adrian.submission4.movie;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import dicoding.adrian.submission4.R;

import static dicoding.adrian.submission4.favorite.moviefavorite.database.DatabaseContract.MovieColumns.BACKDROP;
import static dicoding.adrian.submission4.favorite.moviefavorite.database.DatabaseContract.MovieColumns.CONTENT_URI;
import static dicoding.adrian.submission4.favorite.moviefavorite.database.DatabaseContract.MovieColumns.OVERVIEW;
import static dicoding.adrian.submission4.favorite.moviefavorite.database.DatabaseContract.MovieColumns.POSTER;
import static dicoding.adrian.submission4.favorite.moviefavorite.database.DatabaseContract.MovieColumns.RELEASED;
import static dicoding.adrian.submission4.favorite.moviefavorite.database.DatabaseContract.MovieColumns.SCORE;
import static dicoding.adrian.submission4.favorite.moviefavorite.database.DatabaseContract.MovieColumns.TITLE;

public class DetailMovieActivity extends AppCompatActivity {

    // Default Keys Values
    public static final int REQUEST_ADD = 100;
    public static final int RESULT_ADD = 101;

    /// Position Variable
    private int position;

    // Default Values
    public static final String EXTRA_MOVIE = "extra_movie";
    public static final String EXTRA_POSITION = "extra_position";

    // Instance Movie Items
    private MovieItems movie;

    // Widget Variables Declaration
    TextView txtTitleDetail;
    TextView txtOverviewDetail;
    TextView txtReleasedDetail;
    ImageView posterBanner;
    Button btnLike;
    ImageButton btnBack;
    ProgressBar progressBar;
    RatingBar scoreDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        // Translucent Status Bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // Casting Data Variables
        txtTitleDetail = findViewById(R.id.txt_title_detail);
        txtOverviewDetail = findViewById(R.id.txt_overviewDetail);
        txtReleasedDetail = findViewById(R.id.txt_released_detailMovie);
        posterBanner = findViewById(R.id.poster_banner);
        scoreDetail = findViewById(R.id.score_detail_movie);

        // Casting Button Variables
        btnBack = findViewById(R.id.btn_back);
        btnLike = findViewById(R.id.btn_like_movie);

        // Progress Bar Declaration
        progressBar = findViewById(R.id.progressBar_detailMovie);
        progressBar.bringToFront();

        // Menerima Intent Movie dan Positon
        movie = getIntent().getParcelableExtra(EXTRA_MOVIE);
        if (movie != null) {
            position = getIntent().getIntExtra(EXTRA_POSITION, 0);
        } else {
            movie = new MovieItems();
        }

        Uri uri = getIntent().getData();

        if (uri != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) movie = new MovieItems(cursor);
                cursor.close();
            }
        }

        // Mengisi data String
        txtTitleDetail.setText(movie.getTitle());
        txtOverviewDetail.setText(movie.getOverview().trim());
        String released = (movie.getReleased()).substring(0, 4);
        txtReleasedDetail.setText(released);
        double score = movie.getScore() * 10;
        scoreDetail.setRating((float) ((score * 5) / 100));

        // Mengisi data image
        String url = "https://image.tmdb.org/t/p/original" + movie.getBackdrop();
        Glide.with(DetailMovieActivity.this)
                .load(url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(posterBanner);

        // setOnClickListener untuk Button Back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.no_animation, R.anim.slide_down);
            }
        });

        // setOnClickListener untuk Button Like
        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(EXTRA_MOVIE, movie);
                intent.putExtra(EXTRA_POSITION, position);

                ContentValues values = new ContentValues();
                values.put(TITLE, movie.getTitle());
                values.put(POSTER, movie.getPoster());
                values.put(OVERVIEW, movie.getOverview());
                values.put(SCORE, movie.getScore());
                values.put(RELEASED, movie.getReleased());
                values.put(BACKDROP, movie.getBackdrop());

                setResult(RESULT_ADD, intent);

                getContentResolver().insert(CONTENT_URI, values);

                String successLike = getString(R.string.like);
                Toast.makeText(DetailMovieActivity.this, successLike, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // Animation onBackPressed
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DetailMovieActivity.this.overridePendingTransition(R.anim.no_animation, R.anim.slide_down);
    }
}
