package ua.com.adr.android.mapdownloader;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Regions mRegions = new Regions();
        int[] myDataset = mRegions.allRegions;

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new RegionsAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(
                getApplicationContext()
        ));
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent intent = new Intent(MainActivity.this, CountryActivity.class);
                        intent.putExtra("region", position);
                        startActivity(intent);
                    }

                    @Override public void onLongItemClick(View view, int position) {

                    }
                })
        );

        final TextView freeSpaceText = (TextView)findViewById(R.id.tv_free_space);
        final ProgressBar progressIndicator = (ProgressBar)findViewById(R.id.progressBarFreeSpace);
        final float totalSpace = DeviceMemory.getInternalStorageSpace();
        final float occupiedSpace = DeviceMemory.getInternalUsedSpace();
        final float freeSpace = DeviceMemory.getInternalFreeSpace();
        final DecimalFormat outputFormat = new DecimalFormat("#.##");

        if (null != freeSpaceText) {
            freeSpaceText.setText("Free " + outputFormat.format(freeSpace/1024) + " GB");
        }

        if (null != progressIndicator) {
            progressIndicator.setMax((int) totalSpace);
            progressIndicator.setProgress((int)occupiedSpace);
        }
    }
    public static class DeviceMemory {

        public static final int MEGABYTE_IN_BYTES = 1048576;

        public static float getInternalStorageSpace() {
            float total;
            StatFs statFs = new StatFs(Environment.getDataDirectory().getAbsolutePath());
            if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
                // Для устройств до Android 4.3 (sdk 18)
                total = ((float)statFs.getBlockCount() * statFs.getBlockSize()) / MEGABYTE_IN_BYTES;
            } else {
                // Для новых устройств
                total = ((float)statFs.getBlockCountLong()* statFs.getBlockSizeLong()) / MEGABYTE_IN_BYTES;
            }
            return total;
        }

        public static float getInternalFreeSpace() {
            StatFs statFs = new StatFs(Environment.getDataDirectory().getAbsolutePath());
            float free  = ((float)statFs.getAvailableBlocks() * statFs.getBlockSize()) / MEGABYTE_IN_BYTES;
            return free;
        }

        public static float getInternalUsedSpace() {
            float total, free;
            StatFs statFs = new StatFs(Environment.getDataDirectory().getAbsolutePath());
            if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
                // Для устройств до Android 4.3 (sdk 18)
                total = ((float)statFs.getBlockCount() * statFs.getBlockSize()) / MEGABYTE_IN_BYTES;
                free  = ((float)statFs.getAvailableBlocks() * statFs.getBlockSize()) / MEGABYTE_IN_BYTES;
            } else {
                // Для новых устройств
                total = ((float)statFs.getBlockCountLong()* statFs.getBlockSizeLong()) / MEGABYTE_IN_BYTES;
                free  = ((float)statFs.getAvailableBlocksLong() * statFs.getBlockSizeLong()) / MEGABYTE_IN_BYTES;
            }

            float busy  = total - free;
            return busy;
        }
    }
}
