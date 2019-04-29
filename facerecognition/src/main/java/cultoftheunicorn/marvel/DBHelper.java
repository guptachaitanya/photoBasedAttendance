package cultoftheunicorn.marvel;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kapil on 01/12/18.
 */

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "fr", null, 15);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table student(name VARCHAR,email VARCHAR)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

db.execSQL("drop table student");
onCreate(db);

    }
}
