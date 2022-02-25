package it.matlice.ingsw.controller;

import it.matlice.ingsw.data.User;

import static it.matlice.ingsw.controller.Settings.LOGIN_EXPIRATION_TIME;

public class Controller {

    public static class AuthImpl implements Authentication{
        private User user_ref;
        private long login_time;

        @Override
        public User getUser() {
            return user_ref;
        }

        @Override
        public long getLoginTime() {
            return login_time;
        }

        @Override
        public long getExpirationTime() {
            return login_time + LOGIN_EXPIRATION_TIME;
        }

        @Override
        public boolean isExpired() {
            var current_time = System.currentTimeMillis() / 1000L;
            return current_time >= login_time + LOGIN_EXPIRATION_TIME;
        }
    }

}
