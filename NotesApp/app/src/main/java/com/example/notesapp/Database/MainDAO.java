package com.example.notesapp.Database;

import static androidx.room.OnConflictStrategy.REPLACE;

import android.provider.ContactsContract;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.notesapp.Models.Notes;
import com.google.android.material.internal.ViewOverlayImpl;

import java.util.List;

@Dao
public interface MainDAO {

    // creating Method to access database
    @Insert(onConflict = REPLACE)
    void insert(Notes notes);

    // method to get all the data
    @Query("SELECT * FROM notes ORDER BY id DESC")
    List<Notes> getAll();

    @Query("UPDATE notes SET title = :title, notes = :notes WHERE ID = :id")
    void update(int id, String title, String notes);

    @Delete
    void delete(Notes notes);

    @Query("UPDATE notes SET pinned = :pin WHERE ID = :id")
    void pin(int id, boolean pin);
}
