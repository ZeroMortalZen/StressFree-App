package com.linx.stress_free_app.YoutubeAPI;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import com.github.kotvertolet.youtubejextractor.exception.VideoIsUnavailable;
import com.github.kotvertolet.youtubejextractor.exception.YoutubeRequestException;
import com.github.kotvertolet.youtubejextractor.models.newModels.VideoPlayerConfig;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.linx.stress_free_app.JavaScriptInterface;
import com.linx.stress_free_app.R;
import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.downloader.Downloader;
import org.schabi.newpipe.extractor.stream.AudioStream;
import org.schabi.newpipe.extractor.stream.StreamExtractor;
import com.github.kotvertolet.youtubejextractor.YoutubeJExtractor;
import com.github.kotvertolet.youtubejextractor.exception.ExtractionException;
import com.github.kotvertolet.youtubejextractor.models.youtube.videoData.StreamingData;
import com.github.kotvertolet.youtubejextractor.models.youtube.videoData.YoutubeVideoData;


import java.util.List;

import java.util.List;

public class AudioPlayerActivity extends AppCompatActivity {
    private SimpleExoPlayer exoPlayer;
    private Button playButton;
    private Button pauseButton;
    private String videoId;
    private WebView webView;
    private ProgressBar progressBar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);
        playButton = findViewById(R.id.play_button);
        pauseButton = findViewById(R.id.pause_button);
        progressBar = findViewById(R.id.progress_BarAudio);


        webView = findViewById(R.id.webview);
        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.clearCache(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webView.addJavascriptInterface(new JavaScriptInterface(progressBar), "Android");
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onConsoleMessage(String message, int lineNumber, String sourceID) {
                Log.d("MyApp", "WebView console: " + message + " -- From line " + lineNumber + " of " + sourceID);
            }
        });

        videoId = getIntent().getStringExtra("videoId");
        if (videoId == null) {
            Toast.makeText(this, "No video ID provided", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        playButton.setOnClickListener(v -> webView.loadUrl("javascript:player.playVideo();"));
        pauseButton.setOnClickListener(v -> webView.loadUrl("javascript:player.pauseVideo();"));

        initializeWebView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.destroy();
            webView = null;
        }
    }

    private String getHtmlContent(String videoId) {
        return "<!DOCTYPE html><html><head><style>body {margin: 0;} iframe {display: none; width: 0; height: 0;}</style><script>  var player;  function onYouTubeIframeAPIReady() {    player = new YT.Player('player', {      height: '0',      width: '0',      videoId: '1ZYbU82GVz4',      events: {        'onReady': onPlayerReady,        'onStateChange': onPlayerStateChange      },      playerVars: {        'controls': 0,        'playsinline': 1      }    });  }  function onPlayerReady(event) {    event.target.playVideo();  }  function onPlayerStateChange(event) {    if (event.data == YT.PlayerState.ENDED) {      Android.videoEnded();    }    if (event.data == YT.PlayerState.PLAYING) {      setInterval(updateWatchedTime, 1000);    }  }  function updateWatchedTime() {    var currentTime = Math.floor(player.getCurrentTime());    Android.updateWatchedTime(currentTime);    var progress = (currentTime / 10) * 100;    Android.updateProgress(progress);    console.log('currentTime:', currentTime);  }</script><script src='https://www.youtube.com/iframe_api'></script></head><body><div id='player' style='width: 0; height: 0;'></div></body></html>";
    }

    private void initializeWebView( ) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.addJavascriptInterface(new JavaScriptInterface(progressBar), "Android");
        webView.loadDataWithBaseURL(null, getHtmlContent(videoId), "text/html", "utf-8", null);
    }


}
