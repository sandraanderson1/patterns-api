package com.guardian.api.dataModels;

import lombok.Getter;

@Getter
//no Lombok builder pattern
public class UserData {

    private String userTier;
    private String userName;

    public static class Builder {

        private String userTier;
        private String userName;

        public Builder(String userTier) {
            this.userTier = userTier;
        }

        public Builder withUserName(String userName){
            this.userName = userName;
            return this;
        }

        public UserData build(){
            UserData userData = new UserData();
            userData.userTier = this.userTier;
            userData.userName = this.userName;

            return userData;
        }
    }

    private UserData() {
    }
}