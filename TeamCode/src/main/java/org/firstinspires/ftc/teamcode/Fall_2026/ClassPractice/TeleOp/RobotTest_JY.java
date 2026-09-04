package org.firstinspires.ftc.teamcode.Fall_2026.ClassPractice.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Fall_2026.ClassPractice.Mechanisms.ClawServo_JY;
import org.firstinspires.ftc.teamcode.Fall_2026.ClassPractice.Mechanisms.ColorSensor_JY;
import org.firstinspires.ftc.teamcode.Fall_2026.ClassPractice.Mechanisms.DriveTrain_JY;
import org.firstinspires.ftc.teamcode.Fall_2026.ClassPractice.Mechanisms.SlideServo_JY;

//@Disabled
@TeleOp (name="RobotTest_JY", group="CampTest")
public class RobotTest_JY extends OpMode {

    // Define all hardware components as "private"
    private ClawServo_JY clawServo;
    private SlideServo_JY slideServo;
    private DriveTrain_JY driveTrain;
    private ColorSensor_JY colorSensor;

    // Define all control variables
    private final ElapsedTime clawTimer = new ElapsedTime();
    private final ElapsedTime timer1 = new ElapsedTime();
    private final ElapsedTime timer2 = new ElapsedTime();
    boolean squarePressed = false; // for sequence_1

    ColorSensor_JY.DetectedColor color = ColorSensor_JY.DetectedColor.UNKNOWN;
    boolean colorDetected = false;
    boolean doSequence2 = false;
    boolean forward = false;
    boolean backward = false;

    //Check looptime
    double lastlooptime =0;
    double looptime = 0;

    public void init() {
        clawServo = new ClawServo_JY();
        clawServo.init(hardwareMap, telemetry);
        slideServo = new SlideServo_JY();
        slideServo.init(hardwareMap, telemetry);
        driveTrain = new DriveTrain_JY();
        driveTrain.init(hardwareMap, telemetry);
        //For RC2, uncomment the following and comment the above
        //driveTrain.init_RC2(hardwareMap, telemetry, gamepad1);
        colorSensor = new ColorSensor_JY();
        colorSensor.init(hardwareMap, telemetry);

        lastlooptime = time;
        telemetry.addLine("Init is completed");
    }

    public void loop() {
        driveTrain.FCDrive(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
        //driveTrain.updateTelemetry();
        //Sequence_1();
        telemetry.addData("Detected color is ", colorSensor.getDetectedColor());
        clawServo.manuallyAdjustClaw(gamepad1, 3); //dpad_left and right
        slideServo.manuallyAdjustSlide(gamepad1, 3);  //dpad_up and down
        //driveTrain.checkPort(gamepad1); //USE all 4 buttons on the top right.
        //clawServo.setClawPos(0.26); //RC3, almost close
        //slideServo.setSlidePosition(0.50); //RC3 almost at the bottom
        //nonSequntial(); //triangle, cross, square, circle
        Sequence_2();
        Sequence_1();

        clawServo.toggleClaw(gamepad1.right_bumper);

        if(gamepad1.left_bumper){
            driveTrain.resetYaw();
            telemetry.addLine("IMU is reset to 0");
        }
        updateTelemetry();
    }


    // Close claw -> if (red), forward 2000 ms ; else if (blue), backward 2000 ms -> open claw
    private void Sequence_2() {
        if (gamepad1.cross && !doSequence2) {
            timer2.reset();
            doSequence2 = true;
        }
        if (doSequence2) {
            // Step 1: detect color
            if (timer2.milliseconds() < 500) {
                color = colorSensor.getDetectedColor();
                if (color == ColorSensor_JY.DetectedColor.RED) {
                    forward = true;
                    backward = false;
                } else if (color == ColorSensor_JY.DetectedColor.BLUE) {
                    forward = false;
                    backward = true;
                } else if(color == ColorSensor_JY.DetectedColor.UNKNOWN){
                    forward = false;
                    backward = false;
                    timer2.reset();
                }
            } else if (timer2.milliseconds()<2000){
                clawServo.closeClaw_v1();
            } else if (timer2.milliseconds()<2300){
                if(forward){
                    //driveTrain.turnRight(90);
                    driveTrain.setPowerDirect(0.3);
                    telemetry.addData("detected color is ", color);
                    telemetry.addLine("Robot goes FORWARD");
                } else if (backward){
                    telemetry.addData("detected color is ", color);
                    telemetry.addLine("Robot goes BACKWARD");
                    driveTrain.setPowerDirect(-0.3);
                }
            } else if (timer2.milliseconds() < 3000){
                //clawServo.openClaw_v1();
                driveTrain.stopAll();
                slideServo.setHighPos_v1();
            } else if (timer2.milliseconds() < 3500){
                clawServo.openClaw_v1();
            }else {
                //timer2.reset();
                doSequence2 = false;
            }
        }
    }

        // Close claw -> raise slide -> open claw -> lower slide
    private void Sequence_1 () {
        if (gamepad1.square && !squarePressed) {
            squarePressed = true;
            timer1.reset();
        } else if (timer1.milliseconds() < 200) {
            clawServo.closeClaw_v1();
        } else if (timer1.milliseconds() < 400) {
            slideServo.setHighPos_v1();
        } else if (timer1.milliseconds() < 600) {
            clawServo.openClaw_v1();
        } else if (timer1.milliseconds() < 800) {
            slideServo.setLowPos_v1();
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
            //slideServo.setLowPos_v1();
            slideServo.setLowPosSlow_v2(3); //change to your RCNumber: 2,3, 4. Hold cross long
        }
    }

    public void updateTelemetry () {
        clawServo.updateTelemetry();
        slideServo.updateTelemetry();
        driveTrain.updateTelemetry();
    }
}