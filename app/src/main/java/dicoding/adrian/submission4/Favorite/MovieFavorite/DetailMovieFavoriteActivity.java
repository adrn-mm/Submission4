package dicoding.adrian.submission4.Favorite.MovieFavorite;

import android.content.Intent;
import android.graphics.drawable.Drawable;
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

import dicoding.adrian.submission4.Favorite.MovieFavorite.Database.MovieHelper;
import dicoding.adrian.submission4.MainActivity;
import dicoding.adrian.submission4.Movie.MovieAdapter;
import dicoding.adrian.submission4.Movie.MovieItems;
import dicoding.adrian.submission4.R;

public class DetailMovieFavoriteActivity extends AppCompatActivity {

    // Default Keys Values
    public static final int REQUEST_UPDATE = 200;
    public static final int RESULT_DELETE = 301;

    // Position Variable
    private int position;

    // Default Values
    public static final String EXTRA_MOVIE = "extra_movie";
    public static final String EXTRA_POSITION = "extra_position";

    // Database Declaration
    private MovieHelper movieHelper;

    // Adaoter Declaration
    MovieAdapter adapter;

    // Instance Movie Items
    private MovieItems movie;

    // Widget Variables Declaration
    TextView txtTitleDetail;
    TextView txtOverviewDetail;
    ImageView posterBanner;
    Button btnDislike;
    ImageButton btnBack;
    ProgressBar progressBar;
    RatingBar scoreDetailFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie_favorite);

        // Movie Helper Instance
        movieHelper = MovieHelper.getInstance(getApplicationContext());
        movieHelper.open();

        // Adapter Instance
        adapter = new MovieAdapter();
        adapter.notifyDataSetChanged();

        // Translucent Status Bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // Casting Data Variables
        txtTitleDetail = findViewById(R.id.txt_title_detail_favorite);
        txtOverviewDetail = findViewById(R.id.txt_overviewDetail_favorite);
        posterBanner = findViewById(R.id.poster_banner_favorite);
        scoreDetailFavorite = findViewById(R.id.score_detail_movie_favorite);

        // Casting Button Variables
        btnBack = findViewById(R.id.btn_back_favorite);
        btnDislike = findViewById(R.id.btn_dislike_movie_favorite);

        // Progress Bar Declaration
        progressBar = findViewById(R.id.progressBar_detailMovie_favorite);
        progressBar.bringToFront();

        // Menerima Intent Movie dan Positon
        movie = getIntent().getParcelableExtra(EXTRA_MOVIE);
        position = getIntent().getIntExtra(EXTRA_POSITION, 0);

        // Mengisi data String
        txtTitleDetail.setText(movie.getTitle());
        txtOverviewDetail.setText(movie.getOverview());
        double score = movie.getScore() * 10;
        scoreDetailFavorite.setRating((float) ((score * 5) / 100));

        // Mengisi data image
        String url = "https://image.tmdb.org/t/p/original" + movie.getBackdrop();
        Glide.with(DetailMovieFavoriteActivity.this)
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

        // setOnClickListener untuk Button Dislike
        btnDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long result = movieHelper.deleteMovie(movie.getId());
                if (result > 0) {
                    Intent intent = new Intent(DetailMovieFavoriteActivity.this, MainActivity.class);
                    intent.putExtra(EXTRA_POSITION, position);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(intent, REQUEST_UPDATE);
                    setResult(RESULT_DELETE);
                    finish();
                    String remove = getString(R.string.dislike);
                    Toast.makeText(DetailMovieFavoriteActivity.this, remove, Toast.LENGTH_SHORT).show();
                } else {
                    String failedRemove = getString(R.string.FailedDislike);
                    Toast.makeText(DetailMovieFavoriteActivity.this, failedRemove, Toast.LENGTH_SHORT).show();
                }
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
        DetailMovieFavoriteActivity.this.overridePendingTransition(R.anim.no_animation, R.anim.slide_down);
    }
}
