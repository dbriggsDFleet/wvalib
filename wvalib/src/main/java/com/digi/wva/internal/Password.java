package com.digi.wva.internal;

import android.util.Log;

import com.digi.wva.async.WvaCallback;

import org.json.JSONException;
import org.json.JSONObject;

public class Password {
    private static final String TAG = "wvalib Password";
    private static final String PASSWORD_URI = "password";
    private static final String PASSWORD_KEY = "password";

    private final HttpClient httpClient;

    public Password(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    /**
     * uriPut Doesn't work for password because it doesn't support empty json call back.  Expose
     * password as separate api.
     *
     * @param newPassword new admin password for the device
     * @param callback callback to give feedback on whether call succeeds or not
     */
    public void password(final String newPassword, final WvaCallback<Void> callback) throws JSONException {
        JSONObject passwordObject = new JSONObject();
        passwordObject.put(PASSWORD_KEY, newPassword);

        httpClient.put(PASSWORD_URI, passwordObject, new HttpClient.ExpectEmptyCallback() {
            @Override
            public void onBodyNotEmpty(String body) {
                Log.e(TAG, "password got unexpected response body content:\n" + body);
                onFailure(new Exception("Unexpected response body: " + body));
            }

            @Override
            public void onSuccess() {
                if (callback != null) {
                    callback.onResponse(null, null);
                }
            }

            @Override
            public void onFailure(Throwable error) {
                if (callback != null) {
                    callback.onResponse(error, null);
                }
            }
        });
    }
}
