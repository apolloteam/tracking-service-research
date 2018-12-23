package com.prueba.conex;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PermissionHelper;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Main plugin class
 */
public class CordovaPluginJavaConnection extends CordovaPlugin {

    private static CallbackContext sayHelloContext = null;

    /**
     * Plugin initialization
     * - Creates configuration
     * - Register Receiver to communicate Service with Cordova Plugin
     */
    @Override
    protected void pluginInitialize() {
        // init();
    }

    @Override
    public void onReset() {
        CordovaPluginJavaConnection.sayHelloContext = null;
    }

    /**
     * Javascript callbacks execution context
     * @param action          The action to execute.
     * @param args            The exec() arguments.
     * @param callbackContext The callback context used when calling back into JavaScript.
     * @return
     * @throws JSONException
     */
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
        boolean ret = false;
        try {
            if (action.equals("sayHello")) {
                sayHello(args, callbackContext);
                ret = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return ret;
    }

    /**
     * Notifica a la app que se completo correctamente la acción sayHello en el servicio.
     */
    public static void sayHelloComplete() {
        final CallbackContext callbackContext = CordovaPluginJavaConnection.sayHelloContext;
        if (callbackContext != null) {
            PluginResult r = new PluginResult(PluginResult.Status.OK, Boolean.TRUE);
            callbackContext.sendPluginResult(r);
        }
    }
    
    /**
     * Notifica a la app que ocurrio un error.
     */
    public static void sayHelloFail(String message) {
        final CallbackContext callbackContext = CordovaPluginJavaConnection.sayHelloContext;
        if (callbackContext != null) {
            PluginResult r = new PluginResult(PluginResult.Status.ERROR, message);
            callbackContext.sendPluginResult(r);
        }
    }

    /**
     * Say Hello.
     *
     * @param args
     * @param callbackContext
     */
    private void sayHello(final JSONArray args, final CallbackContext callbackContext) {
        final Activity context = cordova.getActivity();
        this.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {

                    String value = getStringSafe(args, 0);
                    String msg = "Hola " +  value;
                    sayHelloContext(callbackContext, msg);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    if (callbackContext != null) {
                        callbackContext.error(ex.getMessage());
                    }

                    sayHelloContext(null, "");
                }
            }
        });
    }

    /**
     * Guarda la referencia del contexto de la app web para enviar la respuesta a la llamada.
     * @param callbackContext COntexto de la app web.
     */
    private void sayHelloContext(CallbackContext callbackContext, String msg) {
        // Log.d(TAG, "sayHelloContext TH: " + Thread.currentThread().getName() + " PC: " + this.getCurrentProcessName());
        CordovaPluginJavaConnection.sayHelloContext = callbackContext;

        if (CordovaPluginJavaConnection.sayHelloContext != null) {
            PluginResult r = new PluginResult(PluginResult.Status.OK, msg);
            r.setKeepCallback(true);
            CordovaPluginJavaConnection.sayHelloContext.sendPluginResult(r);
        }
    }
    
    /**
     * Devuelve una cadena del JSONArray o una cadena vacia.
     * @param args  Array de datos en formato JSON.
     * @param index Posición que se esta buscando.
     * @return Una cadena del JSONArray o una cadena vacia.
     */
    private String getStringSafe(final JSONArray args, final int index) {
        try {
            return args.getString(index);
        } catch (JSONException e) {
            return "";
        }
    }
}
