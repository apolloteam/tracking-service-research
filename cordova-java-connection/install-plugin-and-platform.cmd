@ECHO OFF
@ECHO OFF
REM cordova plugin ls | find "Cordova-plugin-mobile-suplliers" /c>nul 2>&1
REM IF [%Errorlevel%] EQU [0] (
REM     CALL cordova plugin rm cordova-plugin-mobile-suplliers --save
REM )
REM IF EXIST .\plugins\cordova-plugin-mobile-suplliers\NUL (
REM     CALL DEL /S /F /Q .\plugins\cordova-plugin-mobile-suplliers
REM )

cordova plugin ls | find "cordova-plugin-mobile-suplliers" /c>nul 2>&1
IF [%Errorlevel%] EQU [0] (
    CALL cordova plugin rm cordova-plugin-mobile-suppliers --save
)
IF EXIST .\plugins\cordova-plugin-mobile-suppliers\NUL (
    CALL DEL /S /F /Q .\plugins\cordova-plugin-mobile-suppliers
)
IF EXIST .\plugins\Cordova-plugin-mobile-suppliers\NUL (
    CALL RD /S /Q .\plugins\cordova-plugin-mobile-suppliers
)

REM npm ls -ll | find "Cordova-plugin-mobile-suplliers" /c>nul 2>&1
REM IF [%Errorlevel%] EQU [0] (
REM     CALL npm uninstall cordova-plugin-mobile-suplliers -P
REM )

CALL cordova platform rm android --save
REM CALL cordova plugin add .\..\plugin --save
CALL npm i cordova-android apolloteam/angular-ts-decorators --save
CALL gulp
CALL cordova platform add android --save
CALL cordova build android