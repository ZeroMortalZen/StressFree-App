package com.linx.stress_free_app.MainMenu.MainMenuFragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linx.stress_free_app.R;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.linx.stress_free_app.R;
import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.ResponseTypeValues;


public class MusicFragment extends Fragment {
    private static final String CLIENT_ID = "8f62c4eabb284c4a9658cbcd91bf6159";
    private static final String REDIRECT_URI = "my-stressfree-app://callback";
    private static final int AUTH_REQUEST_CODE = 1001;

    private AuthorizationService authService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authService = new AuthorizationService(requireContext());
        authenticateWithSpotify();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_music, container, false);
    }

    private void authenticateWithSpotify() {
        AuthorizationServiceConfiguration serviceConfig = new AuthorizationServiceConfiguration(
                Uri.parse("https://accounts.spotify.com/authorize"),
                Uri.parse("https://accounts.spotify.com/api/token"));

        AuthorizationRequest.Builder authRequestBuilder = new AuthorizationRequest.Builder(
                serviceConfig,
                CLIENT_ID,
                ResponseTypeValues.CODE,
                Uri.parse(REDIRECT_URI))
                .setScopes("playlist-modify-public", "playlist-read-private", "playlist-read-collaborative", "app-remote-control");

        AuthorizationRequest authRequest = authRequestBuilder.build();
        Intent authIntent = authService.getAuthorizationRequestIntent(authRequest);
        startActivityForResult(authIntent, AUTH_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTH_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            AuthorizationResponse resp = AuthorizationResponse.fromIntent(data);
            AuthorizationException ex = AuthorizationException.fromIntent(data);
            AuthState authState = new AuthState(resp, ex);

            if (resp != null) {
                // Authentication was successful
                // Use authState.getAccessToken() to get the access token
                // Save the AuthState for later use, such as token refreshes
            } else {
                // Authentication failed, handle the error
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        authService.dispose();
    }
}