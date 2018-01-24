package ua.com.adr.android.mapdownloader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

public class AreasActivity extends AppCompatActivity implements AreasAdapter.ClickListener {
    int thisCountry, thisRegion;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);
        Regions regions = new Regions();
        thisCountry = getIntent().getExtras().getInt("country");
        thisRegion = getIntent().getExtras().getInt("region");

        System.out.println("REGION - " + thisRegion);
        System.out.println("COUNTRY - " + thisCountry);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(regions.regionsList.get(thisRegion)[thisCountry]);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new AreasAdapter(regions.europeCountryList.get(thisCountry), this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(
                getApplicationContext()
        ));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(int position, View v) {

    }

    @Override
    public void onItemLongClick(int position, View v) {

    }
}
