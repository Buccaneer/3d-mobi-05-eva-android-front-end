package be.evavzw.eva21daychallenge.models;

import java.util.ArrayList;
import java.util.Date;

public class User {
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

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUserId() {
        return userId;
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

    public String getTypeOfVegan() {
        return typeOfVegan;
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
}
