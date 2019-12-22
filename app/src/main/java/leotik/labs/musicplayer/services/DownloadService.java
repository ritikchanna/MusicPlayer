package leotik.labs.musicplayer.services;

import android.app.DownloadManager;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import leotik.labs.musicplayer.utils.SongsUtils;

public class DownloadService extends IntentService {
    private static final String DOWNLOAD_PATH = "Download_path";
    private static final String DESTINATION_PATH = "Destination_path";
    private static final String FILE_NAME = "File_name";

    private SongsUtils songsUtils;
    private Context context;

    public DownloadService() {
        super("DownloadService");

    }

    public static Intent getDownloadService(final @NonNull Context callingClassContext, final @NonNull String downloadPath, final @NonNull String destinationPath, String fileName) {

        return new Intent(callingClassContext, DownloadService.class)
                .putExtra(DOWNLOAD_PATH, downloadPath)
                .putExtra(DESTINATION_PATH, destinationPath)
                .putExtra(FILE_NAME, fileName);
    }

    public void onCreate() {
        super.onCreate();
        Log.d("Ritik", ">>>onCreate()");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        String downloadPath = intent.getStringExtra(DOWNLOAD_PATH);
        String destinationPath = intent.getStringExtra(DESTINATION_PATH);
        String filename = intent.getStringExtra(FILE_NAME);
        songsUtils = new SongsUtils(getApplicationContext());
        startDownload(downloadPath, destinationPath, filename);
    }

    private void startDownload(String downloadPath, String destinationPath, String fileName) {
        String stream = songsUtils.getAudioStream(downloadPath);
        Uri uri = Uri.parse(stream); // Path where you want to download file.
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);  // Tell on which network you want to download file.
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);  // This will show notification on top when downloading the file.
        request.setTitle("Downloading Song"); // Title for notification.
        request.setVisibleInDownloadsUi(true);
        request.allowScanningByMediaScanner();
        request.setDestinationInExternalPublicDir(destinationPath, fileName);  // Storage directory path
        ((DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE)).enqueue(request); // This will start downloading
    }
}