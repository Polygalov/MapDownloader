package ua.com.adr.android.mapdownloader;

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

/**
 * Created by Andy on 21.01.2018.
 */
 public class AreasAdapter extends RecyclerView.Adapter<AreasAdapter.ViewHolder> {
    private String[] mDataset;
    private static ClickListener clickListener;


    public interface ClickListener {
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            // each data item is just a string in this case
            ImageView ivDownload, ivFlag;
            TextView mTextView;
            ProgressBar mProgressBar;
            boolean isDownload = false;

            Regions regions = new Regions();
            public ViewHolder(View v) {
                super(v);
                ivDownload = (ImageView) itemView.findViewById(R.id.iv_download);
                ivFlag = (ImageView) itemView.findViewById(R.id.iv_flag);
                mTextView = (TextView) v.findViewById(R.id.tv_recycler_item);
                mProgressBar = (ProgressBar) v.findViewById(R.id.progressBar);
                mProgressBar.setVisibility(v.GONE);
                ivDownload.setOnClickListener(this);
            }

        @Override
        public void onClick(View v) {
            if (clickListener!=null){
                clickListener.onItemClick(getLayoutPosition(), v);
                if (isDownload){

                    isDownload = false;
                } else {
                    mProgressBar.setVisibility(v.VISIBLE);
                    ivDownload.setImageResource(R.drawable.ic_action_remove_dark);
                    downloadFile(regions.germanyLinks[getLayoutPosition()], v, mProgressBar, ivFlag);
                }
            }
        }

        void setIconToDownload() {
            ivDownload.setImageResource(R.drawable.ic_action_remove_dark);
        }
    }

        // Provide a suitable constructor (depends on the kind of dataset)
        public AreasAdapter(String[] myDataset, ClickListener clickListener) {
            mDataset = myDataset;
            setOnItemClickListener(clickListener);
        }

        // Create new views (invoked by the layout manager)
        @Override
        public AreasAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            RecyclerView.ViewHolder vh = null;
//            View itemLayoutView;

            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_item_download, parent, false);
           //  set the view's size, margins, paddings and layout parameters

            AreasAdapter.ViewHolder vh = new AreasAdapter.ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

                holder.mTextView.setText(mDataset[position]);


        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.length;
        }


    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    
     static void downloadFile(String url, final View v, final ProgressBar mPB, final ImageView ivFlag) {
//        final ProgressDialog progressDialog = new ProgressDialog(v.getContext());

        new AsyncTask<String, Integer, File>() {
            private Exception m_error = null;

            @Override
            protected void onPreExecute() {
//                View v = LayoutInflater.from(parent.getContext())
//                        .inflate(R.layout.recycler_item_download, parent, false);
//                progressDialog.setMessage("Downloading ...");
//                progressDialog.setCancelable(true);
//                progressDialog.setMax(100);
//                progressDialog
//                        .setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//
//                progressDialog.show();
                mPB.setProgress((int) (0));
            }

            @Override
            protected File doInBackground(String... params) {
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
                    url = new URL(params[0]);
                    urlConnection = (HttpURLConnection) url.openConnection();

                    urlConnection.setRequestMethod("GET");
                    urlConnection.setDoOutput(true);
                    urlConnection.connect();

                    file = File.createTempFile("Mustachify", "download");
                    fos = new FileOutputStream(file);
                    inputStream = urlConnection.getInputStream();

                    totalSize = urlConnection.getContentLength();
                    downloadedSize = 0;

                    buffer = new byte[1024];
                    bufferLength = 0;

                    // читаем со входа и пишем в выход,
                    // с каждой итерацией публикуем прогресс
                    while ((bufferLength = inputStream.read(buffer)) > 0) {
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

            // обновляем progressDialog
            protected void onProgressUpdate(Integer... values) {
               // progressDialog
                mPB.setProgress((int) ((values[0] / (float) values[1]) * 100));
            };

            public void downloadFile(){
                this.download = false;
            }


            @Override
            protected void onPostExecute(File file) {
                // отображаем сообщение, если возникла ошибка
                if (m_error != null) {
                    m_error.printStackTrace();
                    return;
                }
                // закрываем прогресс и удаляем временный файл
     //           progressDialog.hide();
//                mPB.setVisibility(v.GONE);
                new ViewHolder(v).setIconToDownload();
                file.delete();
 //               ivFlag.setColorFilter(new PorterDuffColorFilter(0xffff00, PorterDuff.Mode.MULTIPLY));
            }
        }.execute(url);
    }


}
