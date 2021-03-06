package com.margo.mymovies;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.margo.mymovies.adapter.ReviewAdapter;
import com.margo.mymovies.adapter.TrailerAdapter;
import com.margo.mymovies.data.FavouriteMovie;
import com.margo.mymovies.data.MainViewModel;
import com.margo.mymovies.data.Movie;
import com.margo.mymovies.data.Review;
import com.margo.mymovies.data.Trailer;
import com.margo.mymovies.utils.JSONUtils;
import com.margo.mymovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    private ImageView imageViewBigPoster;
    private ImageView imageViewAddToFavourite;
    private TextView textViewTitle;
    private TextView textViewOriginalTitle;
    private TextView textViewRating;
    private TextView textViewReleaseDate;
    private TextView textViewOverview;
    private ScrollView scrollViewInfo;

    private int id;
    private Movie movie;
    private MainViewModel viewModel;
    private FavouriteMovie favouriteMovie;

    private RecyclerView recyclerViewTrailers;
    private TrailerAdapter trailerAdapter;

    private RecyclerView recyclerViewReviews;
    private ReviewAdapter reviewAdapter;

    private static String language;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.itemMain:
                Intent intentMain = new Intent(this, MainActivity.class);
                startActivity(intentMain);
                break;
            case R.id.itemFavorite:
                Intent intentFavorite = new Intent(this, FavouriteActivity.class);
                startActivity(intentFavorite);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        language = Locale.getDefault().getLanguage();

        imageViewBigPoster = findViewById(R.id.imageViewBigPoster);
        imageViewAddToFavourite = findViewById(R.id.imageViewAddToFavourite);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewOriginalTitle = findViewById(R.id.textViewOriginalTitle);
        textViewRating = findViewById(R.id.textViewRating);
        textViewReleaseDate = findViewById(R.id.textViewReleaseDate);
        textViewOverview = findViewById(R.id.textViewOverview);
        scrollViewInfo = findViewById(R.id.scrollViewInfo);

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("ID"))
            id = intent.getIntExtra("ID", -1);
        else
            finish();

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        movie = viewModel.getMovieById(id);

        Picasso.get().load(movie.getBigPosterPath()).placeholder(R.drawable.placeholder).into(imageViewBigPoster);

        textViewTitle.setText(movie.getTitle());
        textViewOriginalTitle.setText(movie.getOriginalTitle());
        textViewRating.setText(Double.toString(movie.getVoteAverage()));
        textViewReleaseDate.setText(movie.getReleaseDate());
        textViewOverview.setText(movie.getOverview());

        setFavourite();

        recyclerViewReviews = findViewById(R.id.recyclerViewReviews);
        recyclerViewTrailers = findViewById(R.id.recyclerViewTrailers);

        reviewAdapter = new ReviewAdapter();
        trailerAdapter = new TrailerAdapter();

        trailerAdapter.setOnTrailerClickListener(new TrailerAdapter.OnTrailerClickListener() {
            @Override
            public void onTrailerClick(String url) {
                Intent intentToTrailer = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intentToTrailer);
            }
        });

        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTrailers.setLayoutManager(new LinearLayoutManager(this));

        recyclerViewReviews.setAdapter(reviewAdapter);
        recyclerViewTrailers.setAdapter(trailerAdapter);

        JSONObject jsonObjectReviews = NetworkUtils.getJSONForReviws(movie.getId(), language);
        JSONObject jsonObjectTrailers = NetworkUtils.getJSONForVideos(movie.getId(), language);

        ArrayList<Review> reviews = JSONUtils.getReviewsFromJSON(jsonObjectReviews);
        ArrayList<Trailer> trailers = JSONUtils.getTrailersFromJSON(jsonObjectTrailers);

        reviewAdapter.setReviews(reviews);
        trailerAdapter.setTrailers(trailers);

        scrollViewInfo.smoothScrollTo(0,0);
    }

    public void onClickChangeFavourite(View view) {
        if(favouriteMovie == null) {
            viewModel.insertFavouriteMovie(new FavouriteMovie(movie));
            Toast.makeText(this, getString(R.string.add_to_favorite), Toast.LENGTH_SHORT).show();
        }
        else {
            viewModel.deleteFavouriteMovie(favouriteMovie);
            Toast.makeText(this, getString(R.string.remove_from_favorite), Toast.LENGTH_SHORT).show();
        }

        setFavourite();
    }

    private void setFavourite(){
        favouriteMovie = viewModel.getFavouriteMovieById(id);

        if(favouriteMovie == null)
            imageViewAddToFavourite.setImageResource(R.drawable.favourite_add_to);
        else
            imageViewAddToFavourite.setImageResource(R.drawable.favourite_remove);
    }
}
