# Proyecto de investigación para la conexion entre una aplicacion basica de angular javascript con un plugin de cordova en Java.

## Para el levantamiento del ambiente, ingresar en el folder /cordova-java-connection:

1. Ejecutar *init.cmd* para la instalacion de librerias via npm y bower, al finalizar levantara un servidor local con la aplicacion.

2. Instalar Android Studio con sus respectivos SDK Platforms y controladores USB (Dependiendo de el device). (Importante realizarlo antes del paso 3)

3. Ejecutar *install-plugin-and-platform.cmd* para la instalacion del plugin en java ubicado en el folder */plugin* al finalizar, creara respectivamente la plataforma para la ejecucion en el sistema operativo Android.

4. Conectar el dispositivo via cable USB (Recordar habilitar la configuracion usb desarrollador.)

5. Ejecutar *run-on-device.cmd* para realizar la compilacion y ejecucion de la aplicacion en el dispositivo.