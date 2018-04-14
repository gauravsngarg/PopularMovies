package gauravsngarg.com.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

import gauravsngarg.com.popularmovies.R;
import gauravsngarg.com.popularmovies.model.MovieItem;
import gauravsngarg.com.popularmovies.utils.GenerateMovieThumbnailsURL;

/**
 * Created by GG on 14/04/18.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MoviesViewHolder> {

    private static int mMovieItemsCount;
    private static List<MovieItem> list;
    private static int count;


    public MovieAdapter(int movieCount, Context mContext, List<MovieItem> listitem) {
        mMovieItemsCount = movieCount;
        list = listitem;
        count = 0;
    }

    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context mContext = parent.getContext();
        LayoutInflater inflator = LayoutInflater.from(mContext);

        View view = inflator.inflate(R.layout.list_item, parent, false);
        MoviesViewHolder viewHolder = new MoviesViewHolder(view);
        viewHolder.mMovieDetail.setText(list.get(count).getMovieTitle());
        viewHolder.mMovieDetail.setVisibility(View.GONE);

        URL url = null;
        url = GenerateMovieThumbnailsURL.buildURL(list.get(count).getMoviePosterPath());
        Picasso.with(mContext).load(url.toString()).into(viewHolder.mMovieItem);

        count++;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MoviesViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return mMovieItemsCount;
    }

    class MoviesViewHolder extends RecyclerView.ViewHolder {

        TextView mMovieDetail;
        ImageView mMovieItem;

        public MoviesViewHolder(View itemView) {
            super(itemView);

            mMovieDetail = (TextView) itemView.findViewById(R.id.tv_item);
            mMovieItem = (ImageView) itemView.findViewById(R.id.iv_movieitem);

        }
    }
}