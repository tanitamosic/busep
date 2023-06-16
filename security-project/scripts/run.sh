#!/bin/bash
dur=$${1:-30}
DURATION=$((dur))
python thermometer.py     $DURATION &
python camera.py          $DURATION &
python smoke_detector.py  $DURATION &
python light.py           $DURATION &
python lock.py            $DURATION &

wait
