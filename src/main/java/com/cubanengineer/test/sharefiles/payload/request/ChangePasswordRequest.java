package com.cubanengineer.test.sharefiles.payload.request;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ChangePasswordRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(min = 6, max = 40)
    private String newPassword;

    @NotBlank
    @Size(min = 6, max = 40)
    private String repeatNewPassword;

    @NotBlank
    @Size(min = 6, max = 40)
    private String currentPassword;


  
    public String getUsername() {
        return username;
    }

    public void setUsername(String pUsername) {
        this.username = pUsername;
    }

    public String getNewPassword() {
        return newPassword;
    }
 
    public void setNewPassword(String pNewPassword) {
        this.newPassword = pNewPassword;
    }
 
    public String getCurrentPassword() {
        return currentPassword;
    }
 
    public void setCurrentPassword(String pCurrentPassword) {
        this.currentPassword = pCurrentPassword;
    }

    public String getRepeatNewPasswordPassword() {
        return repeatNewPassword;
    }

    public void setRepeatNewPassword(String pRepeatedPassword) {
        this.repeatNewPassword = pRepeatedPassword;
    }

}
