package org.firstinspires.ftc.teamcode.Fall_2026.ClassPractice.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Fall_2026.ClassPractice.Mechanisms.ClawServo_JY;
import org.firstinspires.ftc.teamcode.Fall_2026.ClassPractice.Mechanisms.ColorSensor_JY;
import org.firstinspires.ftc.teamcode.Fall_2026.ClassPractice.Mechanisms.DriveTrain_JY;
import org.firstinspires.ftc.teamcode.Fall_2026.ClassPractice.Mechanisms.SlideServo_JY;

@Disabled
@TeleOp (name="RobotTest_JY", group="Fall_2026")
public class RobotTest_JY extends OpMode {

    private ClawServo_JY clawServo;
    private SlideServo_JY slideServo;
    private DriveTrain_JY driveTrain;
    private ColorSensor_JY colorSensor;
    private final ElapsedTime timer = new ElapsedTime();
    boolean squarePressed = false;

    ColorSensor_JY.DetectedColor color = ColorSensor_JY.DetectedColor.UNKNOWN;
    boolean colorDetected = false;
    boolean doSequence2 = false;
    boolean forward = false;
    boolean backward = false;

    public void init() {
        clawServo = new ClawServo_JY();
        clawServo.init(hardwareMap, telemetry);
        slideServo = new SlideServo_JY();
        slideServo.init(hardwareMap, telemetry);
        driveTrain = new DriveTrain_JY();
        driveTrain.init_RC2(hardwareMap, telemetry, gamepad1);
        colorSensor = new ColorSensor_JY();
        colorSensor.init(hardwareMap, telemetry);

        telemetry.addLine("Init is completed");
    }

    public void loop() {
        driveTrain.RCDrive(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
        Sequential_1();
        telemetry.update();
    }

    // Close claw -> if (red), turn right 90 degree ; else if (blue), turn left 90 -> open claw


        // Close claw -> raise slide -> open claw -> lower slide
    private void Sequential_1 () {
        if (gamepad1.square && !squarePressed) {
            squarePressed = true;
            timer.reset();
        } else if (timer.milliseconds() < 200) {
            clawServo.closeClaw_v1();
        } else if (timer.milliseconds() < 400) {
            slideServo.setHighPos_v1();
        } else if (timer.milliseconds() < 600) {
            clawServo.openClaw_v1();
        } else if (timer.milliseconds() < 800) {
            slideServo.setLowPosSlow_v1();
        } else {
            //timer1.reset(); with this reset, the sequence will repeat on its own
            // because the timer will proceed without pressing the square button
            squarePressed = false;
        }
    }
    private void nonSequntial () {
        if (gamepad1.square) {
            clawServo.closeClaw_v1();
        }
        if (gamepad1.circle) {
            clawServo.openClaw_v1();
        }
        if (gamepad1.triangle) {
            slideServo.setHighPos_v1();
        }
        if (gamepad1.cross) {
            slideServo.setLowPos_v1();
        }
    }

    public void updateTelemetry () {
        clawServo.updateTelemetry();
        slideServo.updateTelemetry();
    }

}