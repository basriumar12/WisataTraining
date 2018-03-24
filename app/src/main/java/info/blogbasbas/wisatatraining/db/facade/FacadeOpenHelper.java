package info.blogbasbas.wisatatraining.db.facade;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import info.blogbasbas.wisatatraining.db.model.DaoMaster;

/**
 * Created by User on 24/03/2018.
 */

public class FacadeOpenHelper extends DaoMaster.OpenHelper{

        public FacadeOpenHelper(Context context, String name) {
                super(context, name);
                }

        public FacadeOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
                super(context, name, factory);
                }
}
