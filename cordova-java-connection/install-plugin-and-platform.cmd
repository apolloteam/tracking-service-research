@ECHO OFF
@ECHO OFF
cordova plugin ls | find "Cordova-plugin-java-connection" /c>nul 2>&1
IF [%Errorlevel%] EQU [0] (
    CALL cordova plugin rm cordova-plugin-java-connection --save
)
IF EXIST .\plugins\cordova-plugin-java-connection\NUL (
    CALL DEL /S /F /Q .\plugins\cordova-plugin-java-connection
)
IF EXIST .\plugins\com-zwitcher-cordova-wsocket\NUL (
    CALL RD /S /Q .\plugins\com-zwitcher-cordova-wsocket
)

cordova plugin ls | find "cordova-plugin-java-connection" /c>nul 2>&1
IF [%Errorlevel%] EQU [0] (
    CALL cordova plugin rm cordova-plugin-java-connection--save
)
IF EXIST .\plugins\cordova-plugin-java-connection\NUL (
    CALL DEL /S /F /Q .\plugins\cordova-plugin-java-connection
)
IF EXIST .\plugins\Cordova-plugin-java-connection\NUL (
    CALL RD /S /Q .\plugins\cordova-plugin-java-connection
)

npm ls -ll | find "com-zwitcher-cordova-wsocket" /c>nul 2>&1
IF [%Errorlevel%] EQU [0] (
    CALL npm uninstall com-zwitcher-cordova-wsocket -P
)

npm ls -ll | find "Cordova-plugin-java-connection" /c>nul 2>&1
IF [%Errorlevel%] EQU [0] (
    CALL npm uninstall cordova-plugin-java-connection -P
)

CALL cordova platform rm android --save
CALL cordova plugin add .\..\plugin --save
CALL npm i cordova-android@6.4.0 apolloteam/angular-ts-decorators --save
CALL gulp
CALL cordova platform add android --save
CALL cordova build android