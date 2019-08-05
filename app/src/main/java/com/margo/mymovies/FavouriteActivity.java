package com.margo.mymovies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.margo.mymovies.adapter.MovieAdapter;
import com.margo.mymovies.data.FavouriteMovie;
import com.margo.mymovies.data.MainViewModel;
import com.margo.mymovies.data.Movie;

import java.util.ArrayList;
import java.util.List;

public class FavouriteActivity extends AppCompatActivity {

    private RecyclerView recyclerViewFavoriteMovies;
    private MovieAdapter adapter;

    private MainViewModel viewModel;

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
        setContentView(R.layout.activity_favourite);

        recyclerViewFavoriteMovies = findViewById(R.id.recyclerViewFavoriteMovies);
        recyclerViewFavoriteMovies.setLayoutManager(new GridLayoutManager(this, 2));

        adapter = new MovieAdapter();
        recyclerViewFavoriteMovies.setAdapter(adapter);

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        LiveData<List<FavouriteMovie>> favouriteMovies = viewModel.getFavouriteMovies();
        favouriteMovies.observe(this, new Observer<List<FavouriteMovie>>() {
            @Override
            public void onChanged(@Nullable List<FavouriteMovie> favouriteMovies) {
                if (favouriteMovies != null) {
                    List<Movie> movies = new ArrayList<>();
                    movies.addAll(favouriteMovies);
                    adapter.setMovies(movies);
                }
            }
        });

        // take movie detail
        adapter.setOnPosterClickListener(new MovieAdapter.OnPosterClickListener() {
            @Override
            public void onPosterClick(int position) {
                Movie movie = adapter.getMovies().get(position);

                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra("ID", movie.getId());
                startActivity(intent);
            }
        });
    }
}
