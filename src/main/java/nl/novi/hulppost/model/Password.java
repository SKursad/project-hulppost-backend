package nl.novi.hulppost.model;

import lombok.Builder;

@Builder
public class Password {

        private String email;
        private String oldPassword;
        private String newPassword;

        public Password() {
        }

        public Password(String email, String oldPassword, String newPassword) {
                this.email = email;
                this.oldPassword = oldPassword;
                this.newPassword = newPassword;
        }

        public String getEmail() {
                return email;
        }

        public void setEmail(String email) {
                this.email = email;
        }

        public String getOldPassword() {
                return oldPassword;
        }

        public void setOldPassword(String oldPassword) {
                this.oldPassword = oldPassword;
        }

        public String getNewPassword() {
                return newPassword;
        }

        public void setNewPassword(String newPassword) {
                this.newPassword = newPassword;
        }
}
