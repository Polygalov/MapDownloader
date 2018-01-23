package ua.com.adr.android.mapdownloader;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Andy on 21.01.2018.
 */

public class CountryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int FRANCE = 7;
    private static final int GERMANY = 8;
    private static final int ITALY = 11;
    private String[] mDataset;
    int thisRegion;


    public static class HolderFirstType extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public HolderFirstType(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.tv_recycler_item);
        }
    }

    public static class HolderZeroType extends RecyclerView.ViewHolder
    {
        public TextView mTextView;
        public HolderZeroType(View v)
        {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.tv_recycler_item);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CountryAdapter(String[] myDataset, int region) {
        mDataset = myDataset;
        thisRegion = region;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        RecyclerView.ViewHolder vh = null;
        View itemLayoutView;

        switch (viewType)
        {
            case 0:
                itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_countries, parent, false);
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
        switch (this.getItemViewType(position))
        {
            case 0:
                HolderZeroType zero = (HolderZeroType) holder;
                zero.mTextView.setText(mDataset[position]);
                break;
            case 1:
                HolderFirstType first = (HolderFirstType) holder;
                first.mTextView.setText(mDataset[position]);
                first.mTextView.setOnClickListener (new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), AreasActivity.class);
                        intent.putExtra("country", position);
                        intent.putExtra("region", thisRegion);
                        v.getContext().startActivity(intent);
                    }
                });
                break;
        }

    }

    @Override
    public int getItemViewType(int position) {
        int typeItem;
        switch (position) {
            case FRANCE:
            case GERMANY:
            case ITALY:
                typeItem = 1;
                break;
            default:
                typeItem = 0;
                break;
        }
        return typeItem;
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}