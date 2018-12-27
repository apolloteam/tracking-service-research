package com.prueba.conex;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import com.google.gson.Gson;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PermissionHelper;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Main plugin class.
 */
public class CordovaPluginJavaConnection extends CordovaPlugin {

    private static CallbackContext sayHelloContext = null;
    private static CallbackContext startServiceContext = null;
    private static CallbackContext stopServiceContext = null;
    private static CallbackContext getLogsContext = null;

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
        CordovaPluginJavaConnection.startServiceContext = null;
        CordovaPluginJavaConnection.stopServiceContext = null;
        CordovaPluginJavaConnection.getLogsContext = null;
    }

    /**
     * Javascript callbacks execution context.
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
            if (action.equals("startService")) {
                startService(callbackContext);
                ret = true;
            }
            if (action.equals("stopService")) {
                stopService(callbackContext);
                ret = true;
            }
            if (action.equals("getLogs")) {
                getLogs(callbackContext);
                ret = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return ret;
    }

    /**
     * Say Hello.
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
     * @param callbackContext Contexto de la app web.
     */
    private void sayHelloContext(CallbackContext callbackContext, String msg) {
        CordovaPluginJavaConnection.sayHelloContext = callbackContext;

        if (CordovaPluginJavaConnection.sayHelloContext != null) {
            // Compone el mensaje de respuesta. 
            PluginResult r = new PluginResult(PluginResult.Status.OK, msg);
            r.setKeepCallback(true);

            // Envia la respuesta a la llamada.
            CordovaPluginJavaConnection.sayHelloContext.sendPluginResult(r);
        }
    }
    
    /**
     * startService.
     * @param callbackContext
     */
    private void startService(final CallbackContext callbackContext) {
        final Activity context = cordova.getActivity();
        this.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {

                    String msg = "Started";
                    startServiceContext(callbackContext, msg);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    if (callbackContext != null) {
                        callbackContext.error(ex.getMessage());
                    }

                    startServiceContext(null,"");
                }
            }
        });
    }

    /**
     * Guarda la referencia del contexto de la app web para enviar la respuesta a la llamada.
     * @param callbackContext Contexto de la app web.
     */
    private void startServiceContext(CallbackContext callbackContext, String msg) {
        CordovaPluginJavaConnection.startServiceContext = callbackContext;

        if (CordovaPluginJavaConnection.startServiceContext != null) {
            // Compone el mensaje de respuesta. 
            PluginResult r = new PluginResult(PluginResult.Status.OK, msg);
            r.setKeepCallback(true);

            // Envia la respuesta a la llamada.
            CordovaPluginJavaConnection.startServiceContext.sendPluginResult(r);
        }
    }

    /**
     * stopService.
     * @param callbackContext
     */
    private void stopService(final CallbackContext callbackContext) {
        final Activity context = cordova.getActivity();
        this.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {

                    String msg = "Stoped";
                    stopServiceContext(callbackContext,msg);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    if (callbackContext != null) {
                        callbackContext.error(ex.getMessage());
                    }

                    stopServiceContext(null,"");
                }
            }
        });
    }

    /**
     * Guarda la referencia del contexto de la app web para enviar la respuesta a la llamada.
     * @param callbackContext Contexto de la app web.
     */
    private void stopServiceContext(CallbackContext callbackContext, String msg) {
        CordovaPluginJavaConnection.stopServiceContext = callbackContext;

        if (CordovaPluginJavaConnection.stopServiceContext != null) {
            // Compone el mensaje de respuesta. 
            PluginResult r = new PluginResult(PluginResult.Status.OK, msg);
            r.setKeepCallback(true);

            // Envia la respuesta a la llamada.
            CordovaPluginJavaConnection.stopServiceContext.sendPluginResult(r);
        }
    }

    /**
     * Guarda la referencia del contexto de la app web para enviar la respuesta a la llamada.
     * @param callbackContext Contexto de la app web.
     */
    private void getLogs(final CallbackContext callbackContext) {
        final Activity context = cordova.getActivity();
        this.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {

                    String[] payload;
                    payload = new String[]{"log 1","log 2","log 3"};
                    String json = new Gson().toJson(payload);
                    getLogsContext(callbackContext, json);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    if (callbackContext != null) {
                        callbackContext.error(ex.getMessage());
                    }

                    getLogsContext(null,null);
                }
            }
        });
    }

    /**
     * Guarda la referencia del contexto de la app web para enviar la respuesta a la llamada.
     * @param callbackContext Contexto de la app web.
     */
    private void getLogsContext(CallbackContext callbackContext, String payload) {
        CordovaPluginJavaConnection.getLogsContext = callbackContext;

        if (CordovaPluginJavaConnection.getLogsContext != null) {
            // Compone el mensaje de respuesta. 
            PluginResult r = new PluginResult(PluginResult.Status.OK, payload);
            r.setKeepCallback(true);

            // Envia la respuesta a la llamada.
            CordovaPluginJavaConnection.getLogsContext.sendPluginResult(r);
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
