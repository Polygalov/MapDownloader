package ua.com.adr.android.mapdownloader;

import android.content.Context;
import android.content.Intent;
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
 * Created by Andy on 21.01.2018.
 */

public class CountryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static String[] mDataset;
    private static String parentSuffix;
    String thisRegion;
    private static ClickListener clickListener;
    XmlPullParser parser;
    private Context context;

    public interface ClickListener {
        void onItemClick(int position, View v);

        void onItemLongClick(int position, View v);
    }

    // Big countries
    public static class HolderFirstType extends RecyclerView.ViewHolder {
        TextView mTextView;

        HolderFirstType(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.tv_recycler_item);
        }
    }

    // Small countries
    public static class HolderZeroType extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivDownload, ivFlag;
        TextView mTextView;
        ProgressBar mProgressBar;
        boolean isDownload = false;
        DownloadFileFromURL ddl;


        public HolderZeroType(View v) {
            super(v);
            ddl = new DownloadFileFromURL();
            ivDownload = (ImageView) itemView.findViewById(R.id.iv_download);
            ivFlag = (ImageView) itemView.findViewById(R.id.iv_flag);
            mTextView = (TextView) v.findViewById(R.id.tv_recycler_item);
            mProgressBar = (ProgressBar) v.findViewById(R.id.progressBar);
            mProgressBar.setVisibility(GONE);
            ivDownload.setOnClickListener(this);
            mTextView = (TextView) v.findViewById(R.id.tv_recycler_item);
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
                    ddl.execute(mDataset[getLayoutPosition()], mProgressBar, ivDownload, ivFlag, parentSuffix, null);
                    isDownload = true;
                }
            }
        }
    }

    public CountryAdapter(String[] myDataset, String region, String parentSuffix, ClickListener clickListener, Context current) {
        mDataset = myDataset;
        thisRegion = region;
        this.parentSuffix = parentSuffix;
        setOnItemClickListener(clickListener);
        this.context = current;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh = null;
        View itemLayoutView;

        switch (viewType) {
            case 0:
                itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_download, parent, false);
                vh = new HolderZeroType(itemLayoutView);
                break;
            case 1:
                itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_big_countries, parent, false);
                vh = new HolderFirstType(itemLayoutView);
                break;
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        switch (this.getItemViewType(position)) {
            case 0:
                HolderZeroType zero = (HolderZeroType) holder;
                zero.mTextView.setText(mDataset[position]);
                break;
            case 1:
                HolderFirstType first = (HolderFirstType) holder;
                first.mTextView.setText(mDataset[position]);
                first.mTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), AreasActivity.class);
                        intent.putExtra("country", mDataset[position]);
                        intent.putExtra("region", thisRegion);
                        v.getContext().startActivity(intent);
                    }
                });
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        int typeItem = 0;
        parser = context.getResources().getXml(R.xml.regions);

        try {
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName().equals("region") && parser.getDepth() == 3
                        && (parser.getAttributeValue(0).equalsIgnoreCase(mDataset[position]))) {
                    parser.next();

                    if (parser.getEventType() == XmlPullParser.START_TAG
                            && parser.getName().equals("region") && parser.getDepth() > 3) {
                        if (parser.getAttributeName(0).equalsIgnoreCase("type")) {

                        } else
                            typeItem = 1;
                        break;
                    }
                }
                parser.next();
            }
        } catch (Throwable t) {

        }

        return typeItem;
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

}