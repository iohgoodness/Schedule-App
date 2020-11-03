package modules;

// will return the adjusted time
public interface AdjustedTime {
    public String getAdjustedTime();
}

/*

uses this basic format to easily grab the time and
return it back to whatever par tof the program is currently
needing to utilize it, whether this be the SQL database or
be the logs.txt file, just a simple utility function
in the "lambda" format

AdjustedTime aj = () -> {
    Date date = new Date();
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    df.setTimeZone(TimeZone.getDefault());
    String adjustedTime = df.format(date);

    return adjustedTime;
};

String adjustedTime = aj.getAdjustedTime();
 */