package map;

public class TimeConverter {
    public static String hhmm(double mins){
        int hoursInt = (int) Math.floor(mins/60);
        String hours = Integer.toString(hoursInt);
        String minutes = String.valueOf((int) Math.floor(mins-hoursInt*60));
        if(hours.length() < 2){
            hours = "0" + hours;
        }
        if(minutes.length() < 2){
            minutes = "0" + minutes;
        }
        return hours + ":" + minutes;
    }

    public static String getHoursWithUnit(double mins){
        int hoursInt = (int) Math.floor(mins/60);
        if (hoursInt == 0) return "";
        return hoursInt + " h";
    }

    public static String getMinsWithUnit(double mins){
        int hoursInt = (int) Math.floor(mins/60);
        int minsInt = (int) Math.floor(mins-hoursInt*60);
        if(minsInt == 0) return "";
        return minsInt + " min";
    }
}
