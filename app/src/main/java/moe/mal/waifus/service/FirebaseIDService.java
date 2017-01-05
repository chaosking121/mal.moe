package moe.mal.waifus.service;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import moe.mal.waifus.Ougi;

/**
 * Extended class to handle the Firebase token stuff
 * Created by Arshad on 04/01/2017.
 */

public class FirebaseIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        sendRegistrationToServer(refreshedToken);
    }

    /**
     * Persist token to third-party servers.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        Ougi.getInstance().setFCMToken(token);
    }

}
