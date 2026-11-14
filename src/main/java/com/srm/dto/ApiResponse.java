package com.srm.dto;

/**
 * Generic API response wrapper.
 * Every REST endpoint returns this structure so the
 * frontend always knows what to expect.
 *
 * Example success:
 *   { "success": true,  "message": "Login successful", "data": { ... } }
 *
 * Example error:
 *   { "success": false, "message": "Invalid credentials", "data": null }
 */
public class ApiResponse<T> {

    private boolean success;
    private String  message;
    private T       data;

    // ── Constructors ────────────────────────────────────────
    public ApiResponse() {}

    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data    = data;
    }

    // ── Static factory methods ──────────────────────────────
    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, "Success", data);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }

    // ── Getters & Setters ───────────────────────────────────
    public boolean isSuccess()              { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage()              { return message; }
    public void setMessage(String message)  { this.message = message; }

    public T getData()                      { return data; }
    public void setData(T data)             { this.data = data; }
}
