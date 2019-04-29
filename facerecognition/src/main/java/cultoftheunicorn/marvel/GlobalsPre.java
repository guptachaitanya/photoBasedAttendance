package cultoftheunicorn.marvel;

import android.content.Context;
import android.content.SharedPreferences;

public class GlobalsPre {

    private static SharedPreferences pref;

    // Shared preferences file name
    private static final String PREF_NAME = "FaceDetect";

    private static final String IS_FIRST_TIME_LAUNCH = "FIRST_TIME_LAUNCH";

    private static  String IS_LOGIN="USER_LOGIN";



    private static  String USERNAME = "USERNAME";


    private static  String PASSWORD = "PASSWORD";




    public static void init(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static boolean shouldShowSlider() {
        return (pref == null) || pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public static void saveFirstTimeLaunch(boolean isFirstTime) {
        if (pref == null)
            return;

        SharedPreferences.Editor editor = pref.edit();
        if (editor != null) {
            editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
            editor.apply();
        }
    }

    public static boolean isLogin(){
        boolean b = pref.getBoolean(IS_LOGIN,false);;
        return  b;
    }

    public static void userSignedIn(boolean isSignedIn){

        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(IS_LOGIN,isSignedIn);
        editor.apply();

    }

    public static void setUserData(int uid, String username, String password, String email, String contact, String imagename){
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(USERNAME,username);
        editor.putString(PASSWORD,password);

        editor.commit();
    }

    public static void clearUserData(){
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(USERNAME,null);
        editor.putString(PASSWORD,null);
        editor.commit();
    }
}
