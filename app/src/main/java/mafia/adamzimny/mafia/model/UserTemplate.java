package mafia.adamzimny.mafia.model;

import android.net.Uri;

import java.util.Date;

/**
 * Created by adamz on 19.08.2016.
 */
public class UserTemplate {

    String firstName;
    String lastName;

    @Override
    public String toString() {
        return "UserTemplate{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", targetDistance=" + targetDistance +
                ", notifications='" + notifications + '\'' +
                ", gender='" + gender + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", description='" + description + '\'' +
                ", profilePicture='" + profilePicture + '\'' +
                '}';
    }

    String email;
    String username;
    String password;
    float targetDistance;
    String notifications;
    String gender;

    Date dateOfBirth;
    String description;
    String profilePicture;

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public float getTargetDistance() {
        return targetDistance;
    }

    public void setTargetDistance(float targetDistance) {
        this.targetDistance = targetDistance;
    }

    public String getNotifications() {
        return notifications;
    }

    public void setNotifications(String notifications) {
        this.notifications = notifications;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
