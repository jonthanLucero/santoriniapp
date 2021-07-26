package com.example.santoriniapp.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.santoriniapp.dao.LoginDAO;
import com.example.santoriniapp.dao.PaymentDAO;
import com.example.santoriniapp.dao.PaymentDetailDAO;
import com.example.santoriniapp.dao.PaymentTypeDAO;
import com.example.santoriniapp.entity.Login;
import com.example.santoriniapp.entity.Payment;
import com.example.santoriniapp.entity.PaymentDetail;
import com.example.santoriniapp.entity.PaymentType;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {
        Login.class,
        Payment.class,
        PaymentDetail.class,
        PaymentType.class
}, version = 1, exportSchema = false)
public abstract class SantoriniDatabase extends RoomDatabase
{
    abstract LoginDAO loginDAO ();
    abstract PaymentDAO paymentDAO ();
    abstract PaymentDetailDAO paymentDetailDAO ();
    abstract PaymentTypeDAO paymentTypeDAO ();

    private static volatile SantoriniDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);


    public static SantoriniDatabase getDatabase ( final Context context)
    {
        if (INSTANCE == null) {
            synchronized (SantoriniDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            SantoriniDatabase.class, "santorini_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;

    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            //TODO: quitar
        /*
        databaseWriteExecutor.execute(() -> {
            // Populate the database in the background.
            // If you want to start with more words, just add them.
            WordDao dao = INSTANCE.wordDao();
            dao.deleteAll();

            Word word = new Word("Hello");
            dao.insert(word);
            word = new Word("World");
            dao.insert(word);
        });

         */
        }
    };
}
