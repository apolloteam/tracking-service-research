package com.prueba.conex;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.io.StringWriter;
import java.util.List;

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
    private static CallbackContext initParametersContext = null;
    private static CallbackContext setParametersContext = null;
    private static CallbackContext getParametersContext = null;

    public static final int REQUEST_LOCATION_PERMISSION_START = 369;
    public static final int REQUEST_LOCATION_PERMISSION_STOP = 963;

    /**
     * Plugin initialization - Creates configuration - Register Receiver to
     * communicate Service with Cordova Plugin
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
        CordovaPluginJavaConnection.initParametersContext = null;
        CordovaPluginJavaConnection.setParametersContext = null;
        CordovaPluginJavaConnection.getParametersContext = null;
    }

    /**
     * Javascript callbacks execution context.
     * 
     * @param action          The action to execute.
     * @param args            The exec() arguments.
     * @param callbackContext The callback context used when calling back into
     *                        JavaScript.
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
            if (action.equals("initParameters")) {
                initParameters(args, callbackContext);
                ret = true;
            }
            if (action.equals("setParameters")) {
                setParameters(args, callbackContext);
                ret = true;
            }
            if (action.equals("getParameters")) {
                getParameters(callbackContext);
                ret = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return ret;
    }

    /**
     * Say Hello.
     * 
     * @param args            The exec() arguments.
     * @param callbackContext Contexto de la app web.
     */
    private void sayHello(final JSONArray args, final CallbackContext callbackContext) {
        final Activity context = cordova.getActivity();
        this.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    String value = getStringSafe(args, 0);
                    String payload = "Hola " + value;

                    // Guarda la referencia del contexto de la app web.
                    CordovaPluginJavaConnection.sayHelloContext = callbackContext;

                    sendResultSuccess(callbackContext, payload);
                } catch (Exception ex) {
                    errorProcess(callbackContext, ex);
                }
            }
        });
    }

    private void startMyForegroundService(Context context) {

        this.cordova.getThreadPool().execute(new Runnable() {
            public void run() {

                try {

                    Intent serviceIntent = new Intent(context, MyForegroundService.class);

                    context.startService(serviceIntent);

                    AppPreferences preferences = new AppPreferences(context);

                    preferences.setServiceRunning(true);
                    
                    sendResultSuccess(CordovaPluginJavaConnection.startServiceContext, "");
                } catch (Exception ex) {
                    errorProcess(CordovaPluginJavaConnection.startServiceContext, ex);
                }
            }
        });
    }

    private void stopMyForegroundService(Context context) {

        this.cordova.getThreadPool().execute(new Runnable() {
            public void run() {

                try {

                    Intent serviceIntent = new Intent(context, MyForegroundService.class);

                    context.stopService(serviceIntent);

                    AppPreferences preferences = new AppPreferences(context);
                    preferences.setServiceRunning(false);

                    // new AppDatabase(context).deleteAll();

                    sendResultSuccess(CordovaPluginJavaConnection.stopServiceContext, "");
                } catch (Exception ex) {
                    errorProcess(CordovaPluginJavaConnection.stopServiceContext, ex);
                }
            }
        });
    }

    /**
     * Inicia el PermanentService.
     * 
     * @param callbackContext Contexto de la app web.
     */
    private void startService(final CallbackContext callbackContext) {
        final Activity context = cordova.getActivity();
        
        if ( this.cordova.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION) ) {

            startMyForegroundService(context);
        } else {

            CordovaPluginJavaConnection.startServiceContext = callbackContext;
            sendResultSuccess(callbackContext, "");

            this.cordova.requestPermission(this, REQUEST_LOCATION_PERMISSION_START, Manifest.permission.ACCESS_FINE_LOCATION );
        }
    }

    /**
     * Detiene el PermanentService.
     * 
     * @param callbackContext Contexto de la app web.
     */
    private void stopService(final CallbackContext callbackContext) {
        final Activity context = cordova.getActivity();

        if ( this.cordova.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION) ) {

            stopMyForegroundService(context);
        } else {

            CordovaPluginJavaConnection.stopServiceContext = callbackContext;
            sendResultSuccess(callbackContext, "");

            this.cordova.requestPermission(this, REQUEST_LOCATION_PERMISSION_STOP, Manifest.permission.ACCESS_FINE_LOCATION );
        }
    }

    /**
     * Callback de la solicitud de permisos.
     *
     * @param requestCode  Codigo solicitado.
     * @param permissions  Permisos.
     * @param grantResults
     * @throws JSONException
     */
    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {

        Log.v("cordova"," 1 requestCode = "+requestCode);

        final Activity context = cordova.getActivity();
        CallbackContext callbackContext = null;
        
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION_START:

                callbackContext = CordovaPluginJavaConnection.startServiceContext;
                break;
            case REQUEST_LOCATION_PERMISSION_STOP:

                callbackContext = CordovaPluginJavaConnection.stopServiceContext;
                break;
        }

        Log.v("cordova"," 2 requestCode = "+requestCode);

        if( grantResults[0] == PackageManager.PERMISSION_DENIED ) {
            
            if (callbackContext != null)
                errorProcess(callbackContext, new Exception("Es necesario el permiso de GPS."));

            return;
        }

        Log.v("cordova"," 3 requestCode = "+requestCode);

        //Uses switch in order to be able to add another permissions
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION_START:
                startMyForegroundService(context);
                break;
            case REQUEST_LOCATION_PERMISSION_STOP:
                stopMyForegroundService(context);
                break;
        }
    }

    /**
     * Obtiene los logs de base de datos.
     * 
     * @param callbackContext Contexto de la app web.
     */
    private void getLogs(final CallbackContext callbackContext) {
        final Activity context = cordova.getActivity();
        this.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    List<String> messages = new AppDatabase(context).getMessages();

                    Log.v("Cordova", "messages count: " + messages.size());

                    String[] payload = messages.toArray(new String[0]);

                    // Convierte el payLoad a JSON.
                    String json = new Gson().toJson(payload);

                    CordovaPluginJavaConnection.getLogsContext = callbackContext;
                    sendResultSuccess(callbackContext, json);
                } catch (Exception ex) {
                    errorProcess(callbackContext, ex);
                }
            }
        });
    }

    /**
     * Inicializa los parametros del plugin.
     * 
     * @param pluginParameters Parametros de la aplicación para transferir al
     *                         plugin.
     * @param callbackContext  Contexto de la app web.
     */
    private void initParameters(final JSONArray pluginParameters, final CallbackContext callbackContext) {
        final Activity context = cordova.getActivity();
        this.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {

                    JSONObject parameters = pluginParameters.getJSONObject(0);

                    AppPreferences preferences = new AppPreferences(context);

                    try {
                        String trackingApiBaseUrl = parameters.getString(AppPreferences.TRACKING_API_BASE_URL);
                        preferences.setTrackingApiBaseUrl(trackingApiBaseUrl);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        String logApiBaseUrl = parameters.getString(AppPreferences.LOG_API_BASE_URL);
                        preferences.setLogApiBaseUrl(logApiBaseUrl);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        Integer gpsInterval = parameters.getInt(AppPreferences.GPS_INTERVAL);
                        preferences.setGspInterval(gpsInterval);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // Guarda la referencia del contexto de la app web.
                    CordovaPluginJavaConnection.initParametersContext = callbackContext;
                    callbackContext.success();
                } catch (Exception ex) {
                    errorProcess(callbackContext, ex);
                }
            }
        });
    }

    /**
     * Establece los parametros del plugin.
     * 
     * @param pluginParameters Parametros de la aplicación para transferir al
     *                         plugin.
     * @param callbackContext  Contexto de la app web.
     */
    private void setParameters(final JSONArray pluginParameters, final CallbackContext callbackContext) {
        final Activity context = cordova.getActivity();
        this.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {

                    JSONObject parameters = pluginParameters.getJSONObject(0);

                    AppPreferences preferences = new AppPreferences(context);

                    try {
                        Integer holderId = parameters.getInt(AppPreferences.HOLDER_ID);
                        preferences.setHolderId(holderId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        Integer activityId = parameters.getInt(AppPreferences.ACTIVITY_ID);
                        preferences.setActivityId(activityId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        Integer ownerId = parameters.getInt(AppPreferences.OWNER_ID);
                        preferences.setOwnerId(ownerId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        Integer holderStatus = parameters.getInt(AppPreferences.HOLDER_STATUS);
                        preferences.setHolderStatus(holderStatus);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        Integer activityStatus = parameters.getInt(AppPreferences.ACTIVITY_STATUS);
                        preferences.setActivityStatus(activityStatus);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // Guarda la referencia del contexto de la app web.
                    CordovaPluginJavaConnection.setParametersContext = callbackContext;
                    callbackContext.success();
                } catch (Exception ex) {
                    errorProcess(callbackContext, ex);
                }
            }
        });
    }

    /**
     * Obtiene los parametros establecidos en el plugin.
     * 
     * @param callbackContext Contexto de la app web.
     */
    private void getParameters(final CallbackContext callbackContext) {
        final Activity context = cordova.getActivity();
        this.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {

                    AppPreferences preferences = new AppPreferences(context);

                    String trackingApiBaseUrl = preferences.getTrackingApiBaseUrl();
                    String logApiBaseUrl = preferences.getLogApiBaseUrl();
                    Integer gpsInterval = preferences.getGspInterval();
                    Integer serviceRunning = preferences.getServiceRunning();
                    Integer holderId = preferences.getHolderId();
                    Integer activityId = preferences.getActivityId();
                    Integer ownerId = preferences.getOwnerId();
                    Integer holderStatus = preferences.getHolderStatus();
                    Integer activityStatus = preferences.getActivityStatus();
                    String lastPositionDate = preferences.getLastPositionDate();
                    String lastPosition = preferences.getLastPosition();

                    String pluginParametersJson = "{'trackingApiBaseUrl': '"+trackingApiBaseUrl+"', 'logApiBaseUrl': '"+logApiBaseUrl+"', 'gpsInterval': "+gpsInterval+
                            ", 'serviceRunning': "+serviceRunning+", 'holderId': "+holderId+", 'activityId': "+activityId+", 'ownerId': "+ownerId+", 'holderStatus': "+holderStatus+
                            ", 'activityStatus': "+activityStatus+", 'lastPositionDate': '"+lastPositionDate+"', 'lastPosition': '"+lastPosition+"'}";

                    // Guarda la referencia del contexto de la app web.
                    CordovaPluginJavaConnection.getParametersContext = callbackContext;

                    sendResultSuccess(callbackContext, pluginParametersJson);
                } catch (Exception ex) {
                    errorProcess(callbackContext, ex);
                }
            }
        });
    }

    /**
     * Envia la respuesta a la aplicacion javascript.
     * 
     * @param callbackContext Contexto de la app web.
     * @param payload         Respuesta.
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
     * 
     * @param callbackContext Contexto de la app web.
     * @param payload         Respuesta.
     */
    private void sendResultError(CallbackContext callbackContext, String payload) {
        // Compone el mensaje de respuesta.
        PluginResult pluginResult = new PluginResult(PluginResult.Status.ERROR, payload);

        // Envia la respuesta a la llamada.
        callbackContext.sendPluginResult(pluginResult);
    }

    /**
     * Envia el error a la aplicacion javascript.
     * 
     * @param callbackContext Contexto de la app web.
     * @param payload         Exception.
     */
    private void errorProcess(CallbackContext callbackContext, Exception ex) {
        // Convierte el payLoad a JSON.
        Gson gson = new Gson();
        Map<String, String> exc_map = new HashMap<String, String>();
        exc_map.put("message", ex.toString());
        exc_map.put("stacktrace", getStackTrace(ex));
        // Convierte el payLoad a JSON.
        String jsonError = new Gson().toJson(exc_map);

        sendResultError(callbackContext, jsonError);
        ex.printStackTrace();
        if (callbackContext != null) {
            callbackContext.error(jsonError);
        }
    }

    /**
     * Construye el formato del stacktrace.
     */
    public static String getStackTrace(final Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }

    /**
     * Devuelve una cadena del JSONArray o una cadena vacia.
     * 
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
