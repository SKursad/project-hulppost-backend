package nl.novi.hulppost.payload;

import lombok.Builder;

import javax.validation.constraints.Pattern;

@Builder
public class Password {

        private String email;
        private String oldPassword;
        @Pattern(regexp = "^(?!.*\\u0020+).*$", message = "{hulppost.constraints.emptySpace.pass.Pattern.message}")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", message = "{hulppost.constraints.password.Pattern.message}")
        private String newPassword;

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
