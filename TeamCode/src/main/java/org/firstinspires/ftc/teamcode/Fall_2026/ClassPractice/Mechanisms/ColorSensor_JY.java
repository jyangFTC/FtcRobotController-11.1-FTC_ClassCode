package org.firstinspires.ftc.teamcode.Fall_2026.ClassPractice.Mechanisms;

import android.graphics.Color;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.robotcore.external.Telemetry;


public class ColorSensor_JY {
    Telemetry telemetry;
    public NormalizedColorSensor color_sensor;
    //public DistanceSensor distanceSensor;
    public double distanceMm;

    public void init(HardwareMap hardwareMap, Telemetry telemetry) {
        color_sensor = hardwareMap.get(NormalizedColorSensor.class, "CS1"); //ON PORT 1
        color_sensor.setGain(1);
        this.telemetry = telemetry;
        //distanceSensor = hwMap.get(DistanceSensor.class, "color_sensor");
    }

    public enum DetectedColor{
        RED,
        BLUE,
        YELLOW,
        UNKNOWN
    }

    public DetectedColor getDetectedColor(){
        NormalizedRGBA colors = color_sensor.getNormalizedColors();  // return 4 values
        float sum = colors.red + colors.blue + colors.green;
        float normRed, normGreen, normBlue;
        normRed = colors.red/sum;
        normGreen = colors.green/sum;
        normBlue = colors.blue/sum;

        telemetry.addData("red", normRed);
        telemetry.addData("green", normGreen);
        telemetry.addData("blue", normBlue);

        float[] hsv = new float[3];
        Color.colorToHSV(colors.toColor(), hsv);
        float hue = hsv[0];
        float saturation = hsv[1];
        float value = hsv[2];
        telemetry.addData("hue", hue);
        telemetry.addData("saturation", saturation);
        telemetry.addData("value", value);

        if(hue >= 20 && hue <= 40){
            return DetectedColor.RED;
        } else if(hue >=70 && hue <= 80){
            return DetectedColor.YELLOW;
        } else if(hue >= 210 && hue <=240){
            return DetectedColor.BLUE;
        } else {
            return DetectedColor.UNKNOWN;
        }

    }


}
