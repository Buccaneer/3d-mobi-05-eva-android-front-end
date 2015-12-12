package be.evavzw.eva21daychallenge.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User implements Serializable{
    private String userId;
    private String email;
    private String password;
    private boolean hasRegistered;
    private Date birthDay;
    private String firstName;
    private String lastName;
    private String budget;
    private String typeOfVegan;
    private ArrayList<Ingredient> allergies;
    private int peopleInFamily;
    private boolean doneSetup;
    private int challengesDone = 0;
    private int points;
    private List<String> badges;
    private boolean hasRequestedChallengeToday;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUserId() {
        return userId;
    }

    public List<String> getBadges() {
        return badges;
    }

    public boolean getHasRequestedChallengeToday(){
        return hasRequestedChallengeToday;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isRegistered() {
        return hasRegistered;
    }

    public void setHasRegistered(boolean hasRegistered) {
        this.hasRegistered = hasRegistered;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public int getPeopleInFamily() {
        return peopleInFamily;
    }

    public String getBudget() {
        return budget;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getChallengesDone() {
        return challengesDone;
    }

    public String getTypeOfVegan() {
        return typeOfVegan;
    }

    public int getPoints() {
        return points;
    }

    public ArrayList<Ingredient> getAllergies() {
        return allergies;
    }

    public void setAllergies(ArrayList<Ingredient> allergies) {
        this.allergies = allergies;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public boolean hasDoneSetup() {
        return doneSetup;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public void setDoneSetup(boolean doneSetup) {
        this.doneSetup = doneSetup;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPeopleInFamily(int peopleInFamily) {
        this.peopleInFamily = peopleInFamily;
    }

    public void setTypeOfVegan(String typeOfVegan) {
        this.typeOfVegan = typeOfVegan;
    }

    public void setChallengesDone(int challengesDone) {
        this.challengesDone = challengesDone;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setBadges(List<String> badges) {
        this.badges = badges;
    }

    public void setHasRequestedChallengeToday(boolean hasRequestedChallengeToday) {
        this.hasRequestedChallengeToday = hasRequestedChallengeToday;
    }
}
