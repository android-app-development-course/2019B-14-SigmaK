package com.example.abc.sigmak;

import android.content.Context;
import android.os.AsyncTask;

import com.example.abc.sigmak.Utility.Manager;

public class DataOperateTask extends AsyncTask<Context, Integer, Long> {
    @Override
    protected Long doInBackground(Context... i) {
//        int count = integers.length;
//        long totalSize = 0;
//        for (int i = 0; i < count; i++) {
//            totalSize += Downloader.downloadFile(urls[i]);
//            publishProgress((int) ((i / (float) count) * 100));
//            // Escape early if cancel() is called
//            if (isCancelled()) break;
//        }

        Manager manager= Manager.getInstance(i[0]);
        manager.CreateTestData(i[0]);
        return 1L;
    }

    protected void onProgressUpdate(Integer... progress) {
//        setProgressPercent(progress[0]);
    }

    protected void onPostExecute(Long result) {
//        showDialog("Downloaded " + result + " bytes");
    }
}
