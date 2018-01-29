package ua.com.adr.android.mapdownloader;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;

import static android.view.View.GONE;

/**
 * Created by Andy on 28.01.2018.
 */

public class SubAreasAdapter extends RecyclerView.Adapter<SubAreasAdapter.ViewHolder> {
    private String[] mDataset;
    private static ClickListener clickListener;
    private static String parentSuffix;
    private static String parentPrefix;
    String parentPreffix;
    XmlPullParser parser;
    private Context context;


    public interface ClickListener {
        void onItemClick(int position, View v);

        void onItemLongClick(int position, View v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivDownload, ivFlag;
        TextView mTextView;
        ProgressBar mProgressBar;
        boolean isDownload = false;
        DownloadFileFromURL ddl;

        public ViewHolder(View v) {
            super(v);
            ddl = new DownloadFileFromURL();
            ivDownload = (ImageView) itemView.findViewById(R.id.iv_download);
            ivFlag = (ImageView) itemView.findViewById(R.id.iv_flag);
            mTextView = (TextView) v.findViewById(R.id.tv_recycler_item);
            mProgressBar = (ProgressBar) v.findViewById(R.id.progressBar);
            mProgressBar.setVisibility(GONE);
            ivDownload.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                clickListener.onItemClick(getLayoutPosition(), v);

                if (isDownload) {
                    isDownload = false;
                    ddl.cancel(false);
                } else {
                    ddl = new DownloadFileFromURL();
                    mProgressBar.setVisibility(v.VISIBLE);
                    ivDownload.setImageResource(R.drawable.ic_action_remove_dark);
                    ddl.execute(mDataset[getLayoutPosition()], mProgressBar, ivDownload, ivFlag, parentSuffix, parentPrefix);
                    isDownload = true;
                }
            }
        }
    }

    public SubAreasAdapter(String[] myDataset, String parentPrefix, String parentSuffix, ClickListener clickListener, Context current) {
        mDataset = myDataset;
        this.parentPrefix = parentPrefix;
        this.parentSuffix = parentSuffix;
        setOnItemClickListener(clickListener);
        this.context = current;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_download, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.mTextView.setText(mDataset[position]);

    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }


    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

}
