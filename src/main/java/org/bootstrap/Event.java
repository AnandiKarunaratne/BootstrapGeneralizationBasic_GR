package org.bootstrap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Event {
    String caseId;
    String activityName;
    Date timestamp;
    // Date format is used to parse a String timestamp into Date
    // This can change according to the timestamp format of the event log
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Event(String caseId, String activityName, Date timestamp) {
        this.caseId = caseId;
        this.activityName = activityName;
        this.timestamp = timestamp;
    }

    public Event(String caseId, String activityName, String timestamp) throws ParseException {
        this.caseId = caseId;
        this.activityName = activityName;
        this.timestamp = dateFormat.parse(timestamp);
    }

    public String getCaseId() {
        return caseId;
    }

    public String getActivityName() {
        return activityName;
    }

    public Date getTimestamp() {
        return timestamp;
    }

}
