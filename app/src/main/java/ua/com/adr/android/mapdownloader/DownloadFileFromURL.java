package ua.com.adr.android.mapdownloader;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.view.View.GONE;

/**
 * Created by Andy on 28.01.2018.
 */

public class DownloadFileFromURL extends AsyncTask<Object, Integer, File> {

    private Exception m_error = null;
    ProgressBar progressBar;
    ImageView atIvDownload;
    ImageView atIvFlag;
    View v;
    final static String BASE_URL = "http://download.osmand.net/download.php?standard=yes&file=";
    final static String END_SUFFIX = "_2.obf.zip";


    @Override
    protected void onPreExecute() {

    }

    @Override
    protected File doInBackground(Object... params) {
        URL url;
        HttpURLConnection urlConnection;
        InputStream inputStream;
        int totalSize;
        int downloadedSize;
        byte[] buffer;
        int bufferLength;

        File file = null;
        FileOutputStream fos = null;

        //   params[4] - Sufix, params[5] - Prefix
        try {
            if (params[5] == null) {
                url = new URL(BASE_URL + modify(((String) params[0]).toLowerCase() + "_" + params[4] + END_SUFFIX));
            } else {
                url = new URL(BASE_URL + modify(((String) params[5]).toLowerCase() + "_" + ((String) params[0]).toLowerCase() + "_" + params[4] + END_SUFFIX));
            }
            progressBar = (ProgressBar) params[1];
            atIvDownload = (ImageView) params[2];
            atIvFlag = (ImageView) params[3];
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.connect();

            file = File.createTempFile("Map", "download");
            fos = new FileOutputStream(file);
            inputStream = urlConnection.getInputStream();

            totalSize = urlConnection.getContentLength();
            downloadedSize = 0;

            buffer = new byte[1024];

            while ((bufferLength = inputStream.read(buffer)) > 0) {
                if (isCancelled())
                    return null;
                fos.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                publishProgress(downloadedSize, totalSize);
            }

            fos.close();
            inputStream.close();

            return file;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            m_error = e;
        } catch (IOException e) {
            e.printStackTrace();
            m_error = e;
        }

        return null;
    }

    private String modify(String countryName) {
        String modCountryName = countryName.substring(0, 1).toUpperCase() + countryName.substring(1);
        return modCountryName;
    }

    protected void onProgressUpdate(Integer... values) {
        progressBar.setProgress((int) ((values[0] / (float) values[1]) * 100));
    }


    @Override
    protected void onCancelled() {
        super.onCancelled();
        progressBar.setVisibility(GONE);
        atIvDownload.setImageResource(R.drawable.ic_action_import);
    }

    @Override
    protected void onPostExecute(File file) {
        if (m_error != null) {
            m_error.printStackTrace();
            return;
        }

        progressBar.setVisibility(GONE);
        atIvDownload.setImageResource(R.drawable.ic_action_import);
        atIvFlag.setColorFilter(new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP));
        file.delete(); // удаляем файл в тестовом режиме!
    }
}