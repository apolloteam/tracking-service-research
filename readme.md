## Realice los siguientes pasos para levantar el ambiente de prueba:

1. Instalar Android Studio con sus respectivos SDK Platforms y controladores USB del dispositivo en que que se va a ejecutar.

2. Ingresar en la folder /cordova-java-connection.

3. Ejecutar **init.cmd**: Instala librerias via npm y bower, al finalizar levantara un servidor local con la aplicacion.

4. Ejecutar **install-plugin-and-platform.cmd**: Instala el plugin en java ubicado en el folder */plugin* al finalizar, crea la plataforma para la ejecucion en el sistema operativo Android.

5. Conectar el dispositivo via USB (previamente habilitar la configuracion usb desarrollador y tener drivers sel dispositivo instalados en la PC).

6. Ejecutar **run-on-device.cmd**: Compila y ejecuta la aplicacion en el dispositivo.