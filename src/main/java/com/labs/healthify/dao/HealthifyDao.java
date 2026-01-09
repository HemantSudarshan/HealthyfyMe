package com.labs.healthify.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.labs.healthify.models.CartItem;
import com.labs.healthify.models.Order;
import com.labs.healthify.models.User;

import java.util.List;

@Dao
public interface HealthifyDao {
    
    // User operations
    @Insert
    void insertUser(User user);
    
    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    User getUser(String username, String password);
    
    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    User getUserByUsername(String username);
    
    // Cart operations
    @Insert
    void insertCartItem(CartItem cartItem);
    
    @Query("SELECT * FROM cart WHERE username = :username AND otype = :otype")
    LiveData<List<CartItem>> getCartItems(String username, String otype);
    
    @Query("SELECT COUNT(*) FROM cart WHERE username = :username AND product = :product")
    int checkCartItem(String username, String product);
    
    @Query("DELETE FROM cart WHERE username = :username AND otype = :otype")
    void deleteCartItems(String username, String otype);
    
    // Order operations
    @Insert
    void insertOrder(Order order);
    
    @Query("SELECT * FROM orderplace WHERE username = :username")
    LiveData<List<Order>> getOrders(String username);
    
    @Query("SELECT COUNT(*) FROM orderplace WHERE username = :username AND name = :name AND address = :address AND connum = :connum AND date = :date AND time = :time")
    int checkAppointmentExists(String username, String name, String address, String connum, String date, String time);
}
