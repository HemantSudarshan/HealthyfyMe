package com.labs.healthify.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.labs.healthify.dao.HealthifyDao;
import com.labs.healthify.models.CartItem;
import com.labs.healthify.models.Order;
import com.labs.healthify.models.User;

@Database(entities = {User.class, CartItem.class, Order.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    
    private static AppDatabase instance;
    
    public abstract HealthifyDao healthifyDao();
    
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "healthify")
                    .addMigrations(MIGRATION_1_2)
                    .build();
        }
        return instance;
    }
    
    /**
     * Migration from version 1 (old SQLite schema) to version 2 (Room schema)
     * Adds auto-increment IDs to cart and orderplace tables
     */
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Create new cart table with ID
            database.execSQL(
                "CREATE TABLE IF NOT EXISTS cart_new (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "username TEXT, " +
                "product TEXT, " +
                "price REAL NOT NULL, " +
                "otype TEXT)"
            );
            
            // Copy data from old cart table
            database.execSQL(
                "INSERT INTO cart_new (username, product, price, otype) " +
                "SELECT username, product, price, otype FROM cart"
            );
            
            // Drop old cart table
            database.execSQL("DROP TABLE cart");
            
            // Rename new table to cart
            database.execSQL("ALTER TABLE cart_new RENAME TO cart");
            
            // Create new orderplace table with ID
            database.execSQL(
                "CREATE TABLE IF NOT EXISTS orderplace_new (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "username TEXT, " +
                "name TEXT, " +
                "address TEXT, " +
                "connum TEXT, " +
                "pin INTEGER NOT NULL, " +
                "date TEXT, " +
                "time TEXT, " +
                "amount REAL NOT NULL, " +
                "otype TEXT)"
            );
            
            // Copy data from old orderplace table
            database.execSQL(
                "INSERT INTO orderplace_new (username, name, address, connum, pin, date, time, amount, otype) " +
                "SELECT username, name, address, connum, pin, date, time, amount, otype FROM orderplace"
            );
            
            // Drop old orderplace table
            database.execSQL("DROP TABLE orderplace");
            
            // Rename new table to orderplace
            database.execSQL("ALTER TABLE orderplace_new RENAME TO orderplace");
        }
    };
}
