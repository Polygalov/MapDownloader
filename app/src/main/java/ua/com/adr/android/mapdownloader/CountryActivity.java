package ua.com.adr.android.mapdownloader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.Collections;

public class CountryActivity extends AppCompatActivity implements CountryAdapter.ClickListener {
    String thisRegion;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    XmlPullParser parser, parser2;
    ArrayList<String> countryList;
    String parentSuffix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);
        thisRegion = getIntent().getExtras().getString("region");

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(convertCountryName(thisRegion));
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        countryList = new ArrayList<>();

        parser = getResources().getXml(R.xml.regions);

        try {
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                // если Россия
                if (thisRegion.equals("russia")) {
                    if (parser.getEventType() == XmlPullParser.START_TAG
                            && parser.getName().equals("region") && parser.getDepth() == 2
                            && !parser.getAttributeValue(0).equals(thisRegion)) {

                        while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                            parser.next();
                            if (parser.getEventType() == XmlPullParser.START_TAG
                                    && parser.getName().equals("region") && parser.getDepth() == 2
                                    && parser.getAttributeValue(0).equals(thisRegion))
                                break;
                        }
                    }
                } // Если не Россия
                else if (parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName().equals("region") && parser.getDepth() == 2
                        && !parser.getAttributeValue(1).equals(thisRegion)) {

                    while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                        parser.next();
                        if (parser.getEventType() == XmlPullParser.START_TAG
                                && parser.getName().equals("region") && parser.getDepth() == 2
                                && parser.getAttributeValue(1).equals(thisRegion)) {
                            break;
                        }
                    }
                }
                if (parser.getEventType() == XmlPullParser.START_TAG
                        && parser.getName().equals("region") && parser.getDepth() == 3
                        ) {
                    countryList.add(convertCountryName(parser.getAttributeValue(0)));
                }

                parser.next();
            }
        } catch (Throwable t) {
            Toast.makeText(this,
                    "Ошибка при загрузке XML-документа: " + t.toString(), Toast.LENGTH_LONG)
                    .show();
        }

        if (thisRegion.equalsIgnoreCase("russia")) {
            parentSuffix = "asia";
        } else {
            parser2 = getResources().getXml(R.xml.regions);
            try {

                while (parser2.getEventType() != XmlPullParser.END_DOCUMENT) {

                    if (parser2.getEventType() == XmlPullParser.START_TAG
                            && parser2.getName().equals("region") && parser2.getDepth() == 2
                            && parser2.getAttributeValue(1).equals(thisRegion)) {
                        for (int i = 0; i < parser2.getAttributeCount(); i++) {
                            if (parser2.getAttributeName(i).contains("download_suffix")) {
                                parentSuffix = parser2.getAttributeValue(i);
                            }
                        }
                    }

                    parser2.next();
                }

            } catch (Throwable t) {
                Toast.makeText(this,
                        "Ошибка при загрузке XML-документа: " + t.toString(), Toast.LENGTH_LONG)
                        .show();
            }
        }

        Collections.sort(countryList);
        String[] myDataset = countryList.toArray(new String[countryList.size()]);


        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new CountryAdapter(myDataset, thisRegion, parentSuffix, this, this);
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
