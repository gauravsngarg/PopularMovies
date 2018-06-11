package gauravsngarg.com.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import gauravsngarg.com.popularmovies.R;
import gauravsngarg.com.popularmovies.model.MovieReviews;



public class ReviewsListAdapter extends RecyclerView.Adapter<ReviewsListAdapter.ReviewsViewHolder> {
    private Context mContext;
    private static List<MovieReviews> list;
    private static int reviewsCount;

    public ReviewsListAdapter(int mReviewsCount, List<MovieReviews> list) {
        this.reviewsCount = mReviewsCount;
        this.list = list;
    }

    @Override
    public ReviewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(R.layout.review_list_item, parent, false);

        ReviewsViewHolder viewHolder = new ReviewsViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewsViewHolder holder, int position) {

        holder.review_content.setText(list.get(position).getContent().toString());
        holder.review_author_name.setText(list.get(position).getAuthor().toString());
    }

    @Override
    public int getItemCount() {
        return reviewsCount;
    }

    public class ReviewsViewHolder extends RecyclerView.ViewHolder{
        final TextView review_author_name;
        final TextView review_content;

        public ReviewsViewHolder(View itemView) {
            super(itemView);
            review_author_name = (TextView) itemView.findViewById(R.id.review_author_name);
            review_content = (TextView) itemView.findViewById(R.id.review_content);
        }
    }
}
