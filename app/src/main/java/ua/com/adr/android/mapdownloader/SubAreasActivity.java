package ua.com.adr.android.mapdownloader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.Collections;

public class SubAreasActivity extends AppCompatActivity implements SubAreasAdapter.ClickListener {
    String thisCountry, thisRegion;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    XmlPullParser parser, parser2;
    ArrayList<String> areasList;
    String parentSuffix;
    String parentPreffix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);
        thisCountry = getIntent().getExtras().getString("country");
        thisRegion = getIntent().getExtras().getString("region");
        parentSuffix = getIntent().getExtras().getString("parentSuffix");
        parentPreffix = thisRegion + "_" + thisCountry;

        if (thisRegion.equals("russia")) {
            parentSuffix = "asia";
            parentPreffix = "Russia";
        }

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(thisCountry);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        areasList = new ArrayList<>();

        parser = getResources().getXml(R.xml.regions);

        try {
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName().equals("region") && parser.getDepth() == 4
                        && !parser.getAttributeValue(0).equalsIgnoreCase(thisCountry)) {

                    while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                        parser.next();
                        if (parser.getEventType() == XmlPullParser.START_TAG
                                && parser.getName().equals("region") && parser.getDepth() == 4
                                && parser.getAttributeValue(0).equalsIgnoreCase(thisCountry)) {
                            break;
                        }
                    }
                }
                if (parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName().equals("region") && parser.getDepth() == 5
                        ) {
                    for (int i = 0; i < parser.getAttributeCount(); i++) {
                        if (parser.getAttributeName(i).equals("name"))
                            areasList.add(convertCountryName(parser.getAttributeValue(i)));
                    }
                }
                parser.next();
            }
        } catch (Throwable t) {
            Toast.makeText(this,
                    "Ошибка при загрузке XML-документа: " + t.toString(), Toast.LENGTH_LONG)
                    .show();
        }

        Collections.sort(areasList);
        String[] myDataset = areasList.toArray(new String[areasList.size()]);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new SubAreasAdapter(myDataset, parentPreffix, parentSuffix, this, this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(
                getApplicationContext()
        ));
    }

    private String convertCountryName(String attributeValue) {
        String countryName = "";
        countryName = attributeValue.substring(0, 1).toUpperCase() + attributeValue.substring(1);
        return countryName;
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
