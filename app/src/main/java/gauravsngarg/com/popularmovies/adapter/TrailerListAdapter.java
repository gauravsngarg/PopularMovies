package gauravsngarg.com.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import gauravsngarg.com.popularmovies.R;
import gauravsngarg.com.popularmovies.model.MovieTrailer;



public class TrailerListAdapter extends RecyclerView.Adapter<TrailerListAdapter.TrailerViewHolder>{

    private Context mContext;
    private static List<MovieTrailer> list;
    private static int mTrailerCount;

    private final TrailerListitemClickListner mOnClickListener;

    public TrailerListAdapter(int trailerCount, List<MovieTrailer> list, TrailerListitemClickListner listener){
        mTrailerCount = trailerCount;
        this.list = list;
        mOnClickListener = listener;
    }

    public interface TrailerListitemClickListner{
        void onTrailerListItemClick(int clickedItemIndex);
    }
    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.trailer_list_item, parent, false);
        TrailerViewHolder viewHolder = new TrailerViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {

        holder.tv_trailer_label.setText("Trailer "+ String.valueOf(position + 1));
    }

    @Override
    public int getItemCount() {
        return mTrailerCount;
    }

    public  class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        final TextView tv_trailer_label;
        final Button btn_play;
        public TrailerViewHolder(View itemView){
            super(itemView);

            tv_trailer_label = (TextView) itemView.findViewById(R.id.trailer_number_label);
            btn_play = (Button) itemView.findViewById(R.id.btn_play);

            btn_play.setOnClickListener(this);
            tv_trailer_label.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int itemClickedPos = getAdapterPosition();
            mOnClickListener.onTrailerListItemClick(itemClickedPos);
        }
    }
}
