package com.android.mig.simpletimeclock.source.model.tasks;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.util.Log;

import com.android.mig.simpletimeclock.source.TimeClockContract;
import com.android.mig.simpletimeclock.source.TimeClockDbHelper;
import com.android.mig.simpletimeclock.source.model.ActiveEmployeesInteractor;

public class UpdateTimeStatusTask extends AsyncTask<Object, Void, Boolean> {

    private static final int CLOCK_OUT_CODE = 0;
    private static final int BREAK_START_CODE = 1;
    private static final int BREAK_END_CODE = 2;

    private Context mContext;
    private ActiveEmployeesInteractor.OnFinishedTransactionListener mOnFinishedTransactionActiveListener;

    public UpdateTimeStatusTask(Context context, ActiveEmployeesInteractor.OnFinishedTransactionListener onFinishedTransactionListener) {
        this.mContext = context;
        this.mOnFinishedTransactionActiveListener = onFinishedTransactionListener;
    }

    @Override
    protected Boolean doInBackground(Object... params) {
        TimeClockDbHelper mTimeClockDbHelper = new TimeClockDbHelper(mContext);
        final SQLiteDatabase db = mTimeClockDbHelper.getWritableDatabase();
        Boolean isSuccess = false;
        int timeId = (int) params[0];
        int breakId = (int) params[1];
        boolean isOnBreak = (boolean) params[2];
        boolean isClockOut = (boolean) params[3];

        //Log.d("TASK", String.valueOf(actionCode));

        try {
            db.beginTransaction();

            long currentTime = System.currentTimeMillis() / 1000;
            if (isClockOut) {
                String sqlUpdateQuery = "UPDATE " + TimeClockContract.Timeclock.TABLE_TIMECLOCK + " SET " +
                        TimeClockContract.Timeclock.TIMECLOCK_CLOCK_OUT + " =? WHERE " +
                        TimeClockContract.Timeclock.TIMECLOCK_ID + " =?";
                SQLiteStatement sqLiteStatement = db.compileStatement(sqlUpdateQuery);
                sqLiteStatement.clearBindings();
                sqLiteStatement.bindLong(1, currentTime);
                sqLiteStatement.bindLong(2, timeId);
                sqLiteStatement.execute();
                sqLiteStatement.close();
                Log.d("UPDATETIMETASK", "updated clock out");
            }

            if (isOnBreak) {
                String sqlUpdateQuery = "UPDATE " + TimeClockContract.Breaks.TABLE_BREAKS + " SET " +
                        TimeClockContract.Breaks.TIMECLOCK_BREAK_END + " =? WHERE " +
                        TimeClockContract.Breaks.BREAK_ID + " =?";
                SQLiteStatement sqLiteStatement = db.compileStatement(sqlUpdateQuery);
                sqLiteStatement.clearBindings();
                sqLiteStatement.bindLong(1, currentTime);
                sqLiteStatement.bindLong(2, breakId);
                sqLiteStatement.execute();
                sqLiteStatement.close();
                Log.d("UPDATETIMETASK", "updated break end");
            } else {
                String sqlUpdateQuery = "INSERT INTO " + TimeClockContract.Breaks.TABLE_BREAKS + " (" +
                        TimeClockContract.Breaks.BREAK_TIMECLOCK_ID + ", " +
                        TimeClockContract.Breaks.TIMECLOCK_BREAK_START + ") VALUES (?,?);";
                SQLiteStatement sqLiteStatement = db.compileStatement(sqlUpdateQuery);
                sqLiteStatement.clearBindings();
                sqLiteStatement.bindLong(1, timeId);
                sqLiteStatement.bindLong(2, currentTime);
                sqLiteStatement.executeInsert();
                sqLiteStatement.close();
                Log.d("UPDATETIMETASK", "updated break start");
            }
/*
            switch (actionCode) {
                case CLOCK_OUT_CODE:
                    sqlUpdateQuery = "UPDATE " + TimeClockContract.Timeclock.TABLE_TIMECLOCK + " SET " +
                            TimeClockContract.Timeclock.TIMECLOCK_CLOCK_OUT + " =? WHERE " +
                            TimeClockContract.Timeclock.TIMECLOCK_ID + " =?";
                    break;
                case BREAK_START_CODE:
                    sqlUpdateQuery = "UPDATE " + TimeClockContract.Timeclock.TABLE_TIMECLOCK + " SET " +
                            TimeClockContract.Timeclock.TIMECLOCK_BREAK_START + " =? WHERE " +
                            TimeClockContract.Timeclock.TIMECLOCK_ID + " =?";
                    break;
                case BREAK_END_CODE:
                    sqlUpdateQuery = "UPDATE " + TimeClockContract.Timeclock.TABLE_TIMECLOCK + " SET " +
                            TimeClockContract.Timeclock.TIMECLOCK_BREAK_END + " =? WHERE " +
                            TimeClockContract.Timeclock.TIMECLOCK_ID + " =?";
                    break;
            }

            SQLiteStatement sqLiteStatement = db.compileStatement(sqlUpdateQuery);
            sqLiteStatement.clearBindings();
            sqLiteStatement.bindLong(1, System.currentTimeMillis() / 1000);
            sqLiteStatement.bindLong(2, timeId);
            sqLiteStatement.execute();
*/
            db.setTransactionSuccessful();
            isSuccess = true;
        } catch (Exception e) {
            Log.w("Exception: ", e);
        } finally {
            db.endTransaction();
        }
        return isSuccess;
    }

    @Override
    protected void onPostExecute(Boolean isSuccess) {
        if (isSuccess) {
            this.mOnFinishedTransactionActiveListener.onUpdateSuccess();
        }
    }
}
