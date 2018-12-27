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
     * @param args The exec() arguments.
     * @param callbackContext Contexto de la app web.
     */
    private void sayHello(final JSONArray args, final CallbackContext callbackContext) {
        final Activity context = cordova.getActivity();
        this.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try 
                {
                    String value = getStringSafe(args, 0);
                    String payload = "Hola " +  value;

                    // Guarda la referencia del contexto de la app web.
                    CordovaPluginJavaConnection.sayHelloContext = callbackContext;
        
                    sendResultSuccess(callbackContext, payload);
                } 
                catch (Exception ex) 
                {
                    ex.printStackTrace();
                    if (callbackContext != null) {
                        callbackContext.error(ex.getMessage());
                    }

                    sendResultError(callbackContext, ex.getMessage());
                }
            }
        });
    }
    
    /**
     * Inicia el PermanentService.
     * @param callbackContext Contexto de la app web.
     */
    private void startService(final CallbackContext callbackContext) {
        final Activity context = cordova.getActivity();
        this.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try 
                {
                    // TODO: Iniciar PermanentService.
                    String payload = "Started";
                    
                    // Guarda la referencia del contexto de la app web.
                    CordovaPluginJavaConnection.startServiceContext = callbackContext;
                    sendResultSuccess(callbackContext, payload);
                } 
                catch (Exception ex) 
                {
                    ex.printStackTrace();
                    if (callbackContext != null) {
                        callbackContext.error(ex.getMessage());
                    }

                    sendResultError(callbackContext, ex.getMessage());
                }
            }
        });
    }

    /**
     * Detiene el PermanentService.
     * @param callbackContext Contexto de la app web.
     */
    private void stopService(final CallbackContext callbackContext) {
        final Activity context = cordova.getActivity();
        this.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try 
                {
                    // TODO: Detener PermanentService.
                    // TODO: Limpiar base de datos.
                    String payload = "Stoped";

                    // Guarda la referencia del contexto de la app web.
                    CordovaPluginJavaConnection.stopServiceContext = callbackContext;
                    sendResultSuccess(callbackContext, payload);
                } 
                catch (Exception ex) 
                {
                    ex.printStackTrace();
                    if (callbackContext != null) {
                        callbackContext.error(ex.getMessage());
                    }
                    
                    sendResultError(callbackContext, ex.getMessage());
                }
            }
        });
    }

    /**
     * Obtiene los logs de base de datos.
     * @param callbackContext Contexto de la app web.
     */
    private void getLogs(final CallbackContext callbackContext) {
        final Activity context = cordova.getActivity();
        this.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try 
                {
                    // TODO: Consultar base de datos.
                    String[] payload;
                    payload = new String[]{"TIC 1","TIC 2","TIC 3"};
                    
                    // Convierte el payLoad a JSON.
                    String json = new Gson().toJson(payload);
                    
                    CordovaPluginJavaConnection.getLogsContext = callbackContext;
                    sendResultSuccess(callbackContext, json);
                } 
                catch (Exception ex) 
                {
                    ex.printStackTrace();
                    if (callbackContext != null) {
                        callbackContext.error(ex.getMessage());
                    }

                    sendResultError(callbackContext, ex.getMessage());
                }
            }
        });
    }

    /**
     * Envia la respuesta a la aplicacion javascript.
     * @param callbackContext Contexto de la app web.
     * @param payload Respuesta.
     */
    private void sendResultSuccess(CallbackContext callbackContext, String payload) {
        
        if (callbackContext != null) {
            // Compone el mensaje de respuesta. 
            PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, payload);
            pluginResult.setKeepCallback(true);

            // Envia la respuesta a la llamada.
            callbackContext.sendPluginResult(pluginResult);
        } 
    }

    /**
     * Envia el error a la aplicacion javascript.
     * @param callbackContext Contexto de la app web.
     * @param payload Respuesta.
     */
    private void sendResultError(CallbackContext callbackContext, String payload) {
        // Compone el mensaje de respuesta. 
        PluginResult pluginResult = new PluginResult(PluginResult.Status.ERROR, payload);

        // Envia la respuesta a la llamada.
        callbackContext.sendPluginResult(pluginResult);
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
