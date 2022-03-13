package com.example.santoriniapp.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverters;
import androidx.room.Update;

import com.example.santoriniapp.entity.Aliquote;
import com.example.santoriniapp.utils.DateConverter;

import java.util.List;

@Dao
public interface AliquoteDAO
{
    @Insert
    Long insertAliquote(Aliquote aliquote);

    @Query("SELECT * FROM aliquote_table where userid = :userId ORDER BY aliquoteyear asc,aliquotemonthcode asc ")
    LiveData<List<Aliquote>> getAllAliquotes(String userId);

    @Query("SELECT * FROM aliquote_table where userid = :userId ORDER BY aliquoteyear asc,aliquotemonthcode asc ")
    List<Aliquote> getAllAliquoteList(String userId);


    @TypeConverters(DateConverter.class)
    @Query("SELECT * FROM aliquote_table WHERE userid = :userId AND aliquoteyear = :aliquoteYear AND aliquotemonthcode = :monthCode ")
    Aliquote getAliquote(String userId,int aliquoteYear,String monthCode);

    @Query("UPDATE aliquote_table set aliquotestatus = 'X' where userid = :userId ")
    void markAllAliquoteAsDeleted(String userId);

    @Update
    void updateAliquote(Aliquote aliquote);

    @Delete
    void deleteAliquote(Aliquote aliquote);

    @Query("DELETE FROM aliquote_table")
    void deleteAllAliquotes();
}
