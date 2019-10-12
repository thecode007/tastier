package com.reflexit.tastier.ui;

import com.reflexit.tastier.database.entities.PersonFaces;

public class PersonFacesSelector {

    private PersonFaces personFaces;
    private boolean isSelected = false;

    public PersonFaces getPersonFaces() {
        return personFaces;
    }
    public void setPersonFaces(PersonFaces personFaces) {
        this.personFaces = personFaces;
    }
    public boolean isSelected() {
        return isSelected;
    }
    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
