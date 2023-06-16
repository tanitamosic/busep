@echo off
set dur=%1
set DURATION=%dur%

start python thermometer.py     %DURATION%
start python camera.py          %DURATION%
start python smoke_detector.py  %DURATION%
start python light.py           %DURATION%
start python lock.py            %DURATION%

exit /b