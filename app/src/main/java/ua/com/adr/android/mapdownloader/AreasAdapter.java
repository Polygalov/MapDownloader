package ua.com.adr.android.mapdownloader;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.view.View.GONE;

/**
 * Created by Andy on 21.01.2018.
 */
public class AreasAdapter extends RecyclerView.Adapter<AreasAdapter.ViewHolder> {
    private int[] mDataset;
    private static ClickListener clickListener;


    public interface ClickListener {
        void onItemClick(int position, View v);

        void onItemLongClick(int position, View v);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        ImageView ivDownload, ivFlag;
        TextView mTextView;
        ProgressBar mProgressBar;
        boolean isDownload = false;
        DownloadFileFromURL ddl;

        Regions regions = new Regions();

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
                    ddl.execute(regions.germanyLinks[getLayoutPosition()], mProgressBar, ivDownload, ivFlag);
                    isDownload = true;
                }
            }
        }
    }

    public AreasAdapter(int[] myDataset, ClickListener clickListener) {
        mDataset = myDataset;
        setOnItemClickListener(clickListener);
    }

    @Override
    public AreasAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_download, parent, false);

        AreasAdapter.ViewHolder vh = new AreasAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.mTextView.setText(holder.itemView.getContext().getResources().getString(mDataset[position]));


    }


    @Override
    public int getItemCount() {
        return mDataset.length;
    }


    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    class DownloadFileFromURL extends AsyncTask<Object, Integer, File> {

        private Exception m_error = null;
        ProgressBar progressBar;
        ImageView atIvDownload;
        ImageView atIvFlag;
        View v;


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

            try {
                url = new URL((String) params[0]);
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
                bufferLength = 0;

                // читаем со входа и пишем в выход,
                // с каждой итерацией публикуем прогресс
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

        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress((int) ((values[0] / (float) values[1]) * 100));
        }

        ;


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

}
