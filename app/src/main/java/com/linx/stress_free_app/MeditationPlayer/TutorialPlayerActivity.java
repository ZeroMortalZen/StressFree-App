package com.linx.stress_free_app.MeditationPlayer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.linx.stress_free_app.JavaScriptInterface;
import com.linx.stress_free_app.R;

public class TutorialPlayerActivity extends AppCompatActivity {

    private static final String VIDEO_ID = "6r22lfFDHLc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_player);
        ProgressBar progressBar = findViewById(R.id.progress_bar);

        WebView webView = findViewById(R.id.webview_youtube_player);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.addJavascriptInterface(new JavaScriptInterface(progressBar), "Android");

        String html = "<!DOCTYPE html>" +
                "<html>" +
                "   <body>" +
                "       <div id='player'></div>" +
                "       <script>" +
                "           var tag = document.createElement('script');" +
                "           tag.src = 'https://www.youtube.com/iframe_api';" +
                "           var firstScriptTag = document.getElementsByTagName('script')[0];" +
                "           firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);" +
                "           var player;" +
                "           function onYouTubeIframeAPIReady() {" +
                "               player = new YT.Player('player', {" +
                "                   height: '100%%'," +
                "                   width: '100%%'," +
                "                   videoId: '%s'," +
                "                   events: {" +
                "                       'onReady': onPlayerReady," +
                "                       'onStateChange': onPlayerStateChange" +
                "                   }" +
                "               });" +
                "           }" +
                "           function onPlayerReady(event) {" +
                "               event.target.playVideo();" +
                "           }" +
                "           function onPlayerStateChange(event) {" +
                "               if (event.data == YT.PlayerState.PLAYING) {" +
                "                   updateProgress();" +
                "               }" +
                "               if (event.data == YT.PlayerState.ENDED) {" +
                "                   Android.videoEnded();" +
                "               }" +
                "           }" +
                "           function updateProgress() {" +
                "               var duration = player.getDuration();" +
                "               var currentTime = player.getCurrentTime();" +
                "               var progress = (currentTime / duration) * 100;" +
                "               Android.updateProgress(progress);" +
                "               if (player.getPlayerState() == YT.PlayerState.PLAYING) {" +
                "                   setTimeout(updateProgress, 1000);" +
                "               }" +
                "           }" +
                "       </script>" +
                "   </body>" +
                "</html>";

        String iframePlayer = String.format(html, VIDEO_ID);
        webView.loadData(iframePlayer, "text/html", "utf-8");
    }


}