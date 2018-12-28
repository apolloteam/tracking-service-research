@ECHO OFF
ECHO Compilando proyecto web.
CALL gulp clean
CALL gulp
ECHO Iniciando el servidor local
server