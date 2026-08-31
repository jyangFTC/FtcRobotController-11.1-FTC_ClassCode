package org.firstinspires.ftc.teamcode.Fall_2026.ClassPractice.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Fall_2026.ClassPractice.Mechanisms.ColorSensor_JY;

//@Disabled
@TeleOp(name="ColorSensorTest_JY", group="CampTest")
public class ColorSensorTest_JY extends OpMode {

    //Must do the "new" to allocate memory (although not initiated it). Otherwise, null pointer error.
    ColorSensor_JY colorSensor = new ColorSensor_JY();


    // Must be classname.enum, can't be instance name
    ColorSensor_JY.DetectedColor detectedColor;

    public void init() {
        colorSensor.init(hardwareMap, telemetry);

    }

    public void loop() {
        telemetry.addData("Detected color: ", colorSensor.getDetectedColor());
        //colorSensor.getDistance(telemetry);

    }

}
