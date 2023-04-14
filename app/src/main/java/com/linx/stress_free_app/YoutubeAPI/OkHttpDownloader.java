package com.linx.stress_free_app.YoutubeAPI;

import org.schabi.newpipe.extractor.downloader.Downloader;
import org.schabi.newpipe.extractor.exceptions.ReCaptchaException;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class OkHttpDownloader extends Downloader {
    private final OkHttpClient client;

    public OkHttpDownloader() {
        client = new OkHttpClient.Builder().build();
    }

    @Override
    public org.schabi.newpipe.extractor.downloader.Response execute(org.schabi.newpipe.extractor.downloader.Request request) throws IOException, ReCaptchaException {
        return null;
    }


    public InputStream stream(String siteUrl) throws IOException, ReCaptchaException {
        Request request = new Request.Builder()
                .url(siteUrl)
                .headers(Headers.of("User-Agent", "Mozilla/5.0 (Windows NT 10.0; rv:78.0) Gecko/20100101 Firefox/78.0"))
                .build();
        Response response = client.newCall(request).execute();
        ResponseBody responseBody = response.body();
        if (responseBody != null) {
            return responseBody.byteStream();
        } else {
            throw new IOException("Failed to obtain InputStream");
        }
    }



    public String download(String siteUrl) throws IOException, ReCaptchaException {
        try (InputStream inputStream = stream(siteUrl)) {
            return convertStreamToString(inputStream);
        }
    }


    private String convertStreamToString(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        }
    }



}
