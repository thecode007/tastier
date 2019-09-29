package com.reflexit.tastier.ui;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.reflexit.tastier.database.entities.Person;
import java.util.Calendar;

public class ProfileViewModel extends ViewModel {

    public MutableLiveData<String> name;
    public MutableLiveData<String> lastVisit;
    public MutableLiveData<String> id;
    public MutableLiveData<String> mobile;
    public MutableLiveData<String> email;
    public MutableLiveData<String> points;

    public ProfileViewModel() {
        name = new MutableLiveData<>();
        lastVisit = new MutableLiveData<>();
        id = new MutableLiveData<>();
        mobile = new MutableLiveData<>();
        email = new MutableLiveData<>();
        points = new MutableLiveData<>();
    }

    public void setPerson(Person person) {
        name.setValue(person.getName());
        id.setValue(person.getPersonId());
        mobile.setValue(person.getMobileNumber());
        lastVisit.setValue(dateDifference(person.getLastVisit()).toString());
        points.setValue(String.valueOf(person.getPoints()));
        email.setValue(person.getEmail());
    }

    private StringBuilder dateDifference(long date) {
        Calendar current = Calendar.getInstance();
        long difference = current.getTimeInMillis() - date;
        Calendar calendarDiff = Calendar.getInstance();
        calendarDiff.setTimeInMillis(difference);

        StringBuilder dateDifference = new StringBuilder("Last visit was ");
        int initialLength = dateDifference.length();
        int dayDifference = calendarDiff.get(Calendar.DAY_OF_MONTH) - 1;
        int hoursDiff = calendarDiff.get(Calendar.HOUR) - 2;
        int minutesDiff = calendarDiff.get(Calendar.MINUTE);

        if (dayDifference == 0 && hoursDiff == 0 && minutesDiff < 1) {
            dateDifference.append("a moment ago");
            return dateDifference;
        }

        if (dayDifference != 0) {
            dateDifference.append(dayDifference).append(" days ");
        }

        if (hoursDiff != 0) {
            if (dateDifference.length() > initialLength) {
                dateDifference.append("and ");
            }
            dateDifference.append(hoursDiff).append(" hours ");
        }

        if (minutesDiff >= 1) {
            if (dateDifference.length() > initialLength) {
                dateDifference.append("and ");
            }
            dateDifference.append(minutesDiff).append(" minutes ");
        }
        dateDifference.append("ago");

        return dateDifference;
    }
}
