package com.labs.healthify.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.labs.healthify.dao.HealthifyDao;
import com.labs.healthify.db.AppDatabase;
import com.labs.healthify.models.CartItem;
import com.labs.healthify.models.Order;
import com.labs.healthify.models.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class HealthifyRepository {
    
    private final HealthifyDao dao;
    private final Executor executor;
    
    public HealthifyRepository(Context context) {
        AppDatabase database = AppDatabase.getInstance(context);
        dao = database.healthifyDao();
        executor = Executors.newSingleThreadExecutor();
    }
    
    /**
     * Hash password using SHA-256
     */
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }
    
    // User operations
    public void register(String username, String email, String password, RegistrationCallback callback) {
        executor.execute(() -> {
            try {
                User user = new User(username, email, hashPassword(password));
                dao.insertUser(user);
                callback.onSuccess();
            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        });
    }
    
    public void login(String username, String password, LoginCallback callback) {
        executor.execute(() -> {
            try {
                User user = dao.getUser(username, hashPassword(password));
                if (user != null) {
                    callback.onSuccess();
                } else {
                    callback.onError("Invalid credentials");
                }
            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        });
    }
    
    // Cart operations
    public void addToCart(String username, String product, float price, String otype, CartCallback callback) {
        executor.execute(() -> {
            try {
                CartItem cartItem = new CartItem(username, product, price, otype);
                dao.insertCartItem(cartItem);
                callback.onSuccess();
            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        });
    }
    
    public LiveData<List<CartItem>> getCartItems(String username, String otype) {
        return dao.getCartItems(username, otype);
    }
    
    public void checkCartItem(String username, String product, CheckCallback callback) {
        executor.execute(() -> {
            try {
                int count = dao.checkCartItem(username, product);
                callback.onResult(count > 0);
            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        });
    }
    
    public void removeCartItems(String username, String otype, CartCallback callback) {
        executor.execute(() -> {
            try {
                dao.deleteCartItems(username, otype);
                callback.onSuccess();
            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        });
    }
    
    // Order operations
    public void placeOrder(String username, String name, String address, String connum,
                          int pin, String date, String time, float amount, String otype,
                          OrderCallback callback) {
        executor.execute(() -> {
            try {
                Order order = new Order(username, name, address, connum, pin, date, time, amount, otype);
                dao.insertOrder(order);
                callback.onSuccess();
            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        });
    }
    
    public LiveData<List<Order>> getOrders(String username) {
        return dao.getOrders(username);
    }
    
    public void checkAppointmentExists(String username, String name, String address,
                                      String connum, String date, String time,
                                      CheckCallback callback) {
        executor.execute(() -> {
            try {
                int count = dao.checkAppointmentExists(username, name, address, connum, date, time);
                callback.onResult(count > 0);
            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        });
    }
    
    /**
     * Get cart data formatted as ArrayList of strings (product$price)
     * Used by CartBuyMedActivity and CartLabActivity
     */
    public void getCartData(String username, String otype, CartDataCallback callback) {
        executor.execute(() -> {
            try {
                List<CartItem> cartItems = dao.getCartItemsSync(username, otype);
                java.util.ArrayList<String> formattedData = new java.util.ArrayList<>();
                for (CartItem item : cartItems) {
                    formattedData.add(item.getProduct() + "$" + item.getPrice());
                }
                callback.onSuccess(formattedData);
            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        });
    }
    
    /**
     * Get order data formatted as ArrayList of strings
     * Format: name$address$connum$pin$date$time$amount$otype
     * Used by OrderDetailsActivity
     */
    public void getOrderData(String username, OrderDataCallback callback) {
        executor.execute(() -> {
            try {
                List<Order> orders = dao.getOrdersSync(username);
                java.util.ArrayList<String> formattedData = new java.util.ArrayList<>();
                for (Order order : orders) {
                    formattedData.add(
                        order.getName() + "$" +
                        order.getAddress() + "$" +
                        order.getConnum() + "$" +
                        order.getPin() + "$" +
                        order.getDate() + "$" +
                        order.getTime() + "$" +
                        order.getAmount() + "$" +
                        order.getOtype()
                    );
                }
                callback.onSuccess(formattedData);
            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        });
    }
    
    // Callback interfaces
    public interface RegistrationCallback {
        void onSuccess();
        void onError(String error);
    }
    
    public interface LoginCallback {
        void onSuccess();
        void onError(String error);
    }
    
    public interface CartCallback {
        void onSuccess();
        void onError(String error);
    }
    
    public interface OrderCallback {
        void onSuccess();
        void onError(String error);
    }
    
    public interface CheckCallback {
        void onResult(boolean exists);
        void onError(String error);
    }
    
    public interface CartDataCallback {
        void onSuccess(java.util.ArrayList<String> data);
        void onError(String error);
    }
    
    public interface OrderDataCallback {
        void onSuccess(java.util.ArrayList<String> data);
        void onError(String error);
    }
}
