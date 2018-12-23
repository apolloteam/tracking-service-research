@ECHO OFF
SET env=%1
IF [%env%] EQU [] SET env=./www
http-server %env% -p8282 --o --wait=2000