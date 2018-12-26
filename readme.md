## Realice los siguientes pasos para levantar el ambiente de prueba:

1. Instalar Android Studio con sus respectivos SDK Platforms y controladores USB del dispositivo en que que se va a ejecutar.

2. Instalar la última versión de node (https://nodejs.org/es/, versión LTE).

3. Ingresar en la folder /cordova-java-connection.

4. Ejecutar **init.cmd**: Instala librerias via npm y bower, al finalizar levantara un servidor local con la aplicacion.

5. Ejecutar **install-plugin-and-platform.cmd**: Instala el plugin en java ubicado en el folder */plugin* al finalizar, crea la plataforma para la ejecucion en el sistema operativo Android.

6. Conectar el dispositivo via USB (previamente habilitar la configuracion usb desarrollador y tener drivers sel dispositivo instalados en la PC).

7. Ejecutar **run-on-device.cmd**: Compila y ejecuta la aplicacion en el dispositivo.