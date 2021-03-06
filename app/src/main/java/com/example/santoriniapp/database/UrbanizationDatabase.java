package com.example.santoriniapp.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.santoriniapp.dao.AliquoteDAO;
import com.example.santoriniapp.dao.LoginDAO;
import com.example.santoriniapp.dao.PaymentDAO;
import com.example.santoriniapp.dao.PaymentDetailDAO;
import com.example.santoriniapp.dao.PaymentPhotoDAO;
import com.example.santoriniapp.dao.PaymentTypeDAO;
import com.example.santoriniapp.entity.Aliquote;
import com.example.santoriniapp.entity.Login;
import com.example.santoriniapp.entity.Payment;
import com.example.santoriniapp.entity.PaymentDetail;
import com.example.santoriniapp.entity.PaymentPhoto;
import com.example.santoriniapp.entity.PaymentType;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {
        Login.class,
        Payment.class,
        PaymentDetail.class,
        PaymentType.class,
        PaymentPhoto.class,
        Aliquote.class

}, version = 2, exportSchema = false)
public abstract class UrbanizationDatabase extends RoomDatabase
{
    public abstract LoginDAO loginDAO ();
    public abstract PaymentDAO paymentDAO ();
    public abstract PaymentDetailDAO paymentDetailDAO ();
    public abstract PaymentTypeDAO paymentTypeDAO ();
    public abstract PaymentPhotoDAO paymentPhotoDAO();
    public abstract AliquoteDAO aliquoteDAO();

    private static UrbanizationDatabase INSTANCE;

    public static UrbanizationDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (UrbanizationDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            UrbanizationDatabase.class, "urbanization_db")
                            // Wipes and rebuilds instead of migrating if no Migration object.
                            // Migration is not part of this codelab.
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
