package gauravsngarg.com.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

import gauravsngarg.com.popularmovies.R;
import gauravsngarg.com.popularmovies.model.MovieItem;
import gauravsngarg.com.popularmovies.utils.GenerateMovieThumbnailsURL;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MoviesViewHolder> {

    private static int mMovieItemsCount;
    private static List<MovieItem> list;
    private static int count;

    private final MovieListitemClickListner mOnClickListner;

    public MovieAdapter(int movieCount, List<MovieItem> listitem, MovieListitemClickListner listener) {
        mMovieItemsCount = movieCount;
        list = listitem;
        mOnClickListner = listener;
        count = 0;

    }

    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context mContext = parent.getContext();
        LayoutInflater inflator = LayoutInflater.from(mContext);

        View view = inflator.inflate(R.layout.list_item, parent, false);
        MoviesViewHolder viewHolder = new MoviesViewHolder(view);

        URL url = GenerateMovieThumbnailsURL.buildURL(list.get(count).getMoviePosterPath());
        Picasso.with(mContext).load(url.toString()).into(viewHolder.mMovieItem);

        count++;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MoviesViewHolder holder, int position) {
    }

    public interface MovieListitemClickListner{
        void onMovieListItemClick(int clickedItemIndex);
    }

    @Override
    public int getItemCount() {
        return mMovieItemsCount;
    }

    class MoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        final ImageView mMovieItem;

        public MoviesViewHolder(View itemView) {
            super(itemView);

            mMovieItem = (ImageView) itemView.findViewById(R.id.iv_movieitem);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {


            int clickedPosition = getAdapterPosition();
            mOnClickListner.onMovieListItemClick(clickedPosition);

        }
    }
}
