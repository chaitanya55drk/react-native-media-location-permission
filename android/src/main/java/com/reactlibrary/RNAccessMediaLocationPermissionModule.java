
package com.reactlibrary;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.SparseArray;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.modules.core.PermissionAwareActivity;
import com.facebook.react.modules.core.PermissionListener;

public class RNAccessMediaLocationPermissionModule extends ReactContextBaseJavaModule implements PermissionListener {

  private final ReactApplicationContext reactContext;

  private final String GRANTED = "granted";
  private final String DENIED = "denied";
  private final String UNAVAILABLE = "unavailable";
  private final String BLOCKED = "blocked";
  private static final String ERROR_INVALID_ACTIVITY = "E_INVALID_ACTIVITY";

  private final SparseArray<Request> mRequests;
  private int mRequestCode = 0;

  public RNAccessMediaLocationPermissionModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
    mRequests = new SparseArray<Request>();
  }

  private class Request {

    public boolean[] rationaleStatuses;
    public Callback callback;

    public Request(boolean[] rationaleStatuses, Callback callback) {
      this.rationaleStatuses = rationaleStatuses;
      this.callback = callback;
    }
  }

  private boolean hasMediaLocationPermission(Context context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      // this condition is not true for below Android Q
      return context.checkSelfPermission(Manifest.permission.ACCESS_MEDIA_LOCATION) == PERMISSION_GRANTED;
    }else return false;
  }

  @ReactMethod
  public void requestMediaLocationPermission(final Promise promise) {
    final String permission = Manifest.permission.ACCESS_MEDIA_LOCATION;
    if (permission == null) {
      promise.resolve(UNAVAILABLE);
      return;
    }

    Context context = getReactApplicationContext().getBaseContext();

    if(hasMediaLocationPermission(context)) {
      promise.resolve(GRANTED);
      return;
    }

    try {
      PermissionAwareActivity activity = getPermissionAwareActivity();
      boolean[] rationaleStatuses = new boolean[1];
      rationaleStatuses[0] = activity.shouldShowRequestPermissionRationale(permission);

      mRequests.put(mRequestCode, new Request(
              rationaleStatuses,
              new Callback() {
                @SuppressLint("ApplySharedPref")
                @Override
                public void invoke(Object... args) {
                  int[] results = (int[]) args[0];

                  if (results.length > 0 && results[0] == PackageManager.PERMISSION_GRANTED) {
                    promise.resolve(GRANTED);
                  } else {
                    PermissionAwareActivity activity = (PermissionAwareActivity) args[1];
                    boolean[] rationaleStatuses = (boolean[]) args[2];

                    if (rationaleStatuses[0] && !activity.shouldShowRequestPermissionRationale(permission)) {
                      promise.resolve(BLOCKED);
                    } else {
                      promise.resolve(DENIED);
                    }
                  }
                }
              }));

      activity.requestPermissions(new String[] {permission}, mRequestCode, this);
      mRequestCode++;
    } catch (IllegalStateException e) {
      promise.reject(ERROR_INVALID_ACTIVITY, e);
    }
  }

  private PermissionAwareActivity getPermissionAwareActivity() {
    Activity activity = getCurrentActivity();
    if (activity == null) {
      throw new IllegalStateException("Tried to use permissions API while not attached to an " + "Activity.");
    } else if (!(activity instanceof PermissionAwareActivity)) {
      throw new IllegalStateException("Tried to use permissions API but the host Activity doesn't"  + " implement PermissionAwareActivity.");
    }
    return (PermissionAwareActivity) activity;
  }

  @Override
  public boolean onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    Request request = mRequests.get(requestCode);
    request.callback.invoke(grantResults, getPermissionAwareActivity(), request.rationaleStatuses);
    mRequests.remove(requestCode);
    return mRequests.size() == 0;
  }

  @Override
  public String getName() {
    return "RNAccessMediaLocationPermission";
  }
}