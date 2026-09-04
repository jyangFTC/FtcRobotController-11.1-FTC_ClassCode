package org.firstinspires.ftc.teamcode.Fall_2026.ClassPractice.Mechanisms;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
public class DriveTrain_JY {
    private DcMotor frontLeft, frontRight, backLeft, backRight;
    private double frontleftPower, frontrightPower, backleftPower, backrightPower;
    private IMU imu;
    double heading;
    private final double maxPower = 0.6; // small motors, 6-9 V, so max power is 40%

    private Telemetry telemetry;
    private Gamepad gamepad1;


    public void init(HardwareMap hardwareMap, Telemetry telemetry){
        frontLeft = hardwareMap.get(DcMotor.class, "front_left"); //port 0
        frontRight = hardwareMap.get(DcMotor.class, "front_right"); // port 2
        backLeft = hardwareMap.get(DcMotor.class, "back_left"); // port 1
        backRight = hardwareMap.get(DcMotor.class, "back_right"); // port 3
        this.telemetry = telemetry;
        //this.gamepad1 = gamepad1;

        // reverse the mirror side (left) of motors
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);

        stopAll();

        // Initialize IMU (gyro sensor)
        imu = hardwareMap.get(IMU.class,"imu");
        // Define the hub's orientation relative to the robot based on how the hub is mounted
        RevHubOrientationOnRobot.LogoFacingDirection logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.UP;
        RevHubOrientationOnRobot.UsbFacingDirection usbDirection = RevHubOrientationOnRobot.UsbFacingDirection.LEFT;
        RevHubOrientationOnRobot orientation = new RevHubOrientationOnRobot(logoDirection, usbDirection);
        // Initialize the IMU with the orientation
        imu.initialize(new IMU.Parameters(orientation));
        imu.resetYaw();
        heading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
    }

    public void init_RC2(HardwareMap hardwareMap, Telemetry telemetry){
        frontLeft = hardwareMap.get(DcMotor.class, "front_left"); //port 0
        frontRight = hardwareMap.get(DcMotor.class, "front_right"); // port 2
        backLeft = hardwareMap.get(DcMotor.class, "back_left"); // port 1
        backRight = hardwareMap.get(DcMotor.class, "back_right"); // port 3
        this.telemetry = telemetry;
        //this.gamepad1 = gamepad1;

        // reverse the pairing side of motor
        //frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);

        stopAll();

        // Initialize IMU (gyro sensor)
        imu = hardwareMap.get(IMU.class,"imu");
        // Define the hub's orientation relative to the robot based on how the hub is mounted
        RevHubOrientationOnRobot.LogoFacingDirection logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.UP;
        RevHubOrientationOnRobot.UsbFacingDirection usbDirection = RevHubOrientationOnRobot.UsbFacingDirection.LEFT;
        RevHubOrientationOnRobot orientation = new RevHubOrientationOnRobot(logoDirection, usbDirection);
        // Initialize the IMU with the orientation
        imu.initialize(new IMU.Parameters(orientation));
        imu.resetYaw();
        heading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
    }

    public void stopAll(){
        frontLeft.setPower(0.0);
        backLeft.setPower(0.0);
        frontRight.setPower(0.0);
        backRight.setPower(0.0);
    }

    public void resetYaw(){
        imu.resetYaw();
    }
    //Robot centric drive
    public void RCDrive(double y, double x, double t){
        /* Manul drive with gamepad
        y: left joystick y: forward/backward;
        x: left joystick x: strafing left/right
        t: right joystick x: turning
         */
        frontleftPower = -y+x+t;
        backleftPower = -y-x+t;
        frontrightPower = -y-x-t;
        backrightPower = -y+x-t;

        normalizePower();
        setPower();
    }

    // Field centric drive
    public void FCDrive(double y, double x, double t){
    /* Field centric drive: movement is relative to the field, not the robot
    y: left joystick y: forward/backward;
    x: left joystick x: strafing left/right
    t: right joystick x: turning
     */

        // Get the current heading (yaw angle) from the IMU in radians
        heading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

        // Rotate the joystick inputs based on current heading to correct for robot rotation
        double adjustedY = -y * Math.cos(heading) + x * Math.sin(heading);
        double adjustedX = y * Math.sin(heading) + x * Math.cos(heading);

        // Apply the same mecanum wheel drive equations as RCDrive
        frontleftPower = adjustedY + adjustedX + t;
        backleftPower = adjustedY - adjustedX + t;
        frontrightPower = adjustedY - adjustedX - t;
        backrightPower = adjustedY + adjustedX - t;

        normalizePower();
        setPower();
    }

    //Tank drive: no strafing, same as FLL drive
    public void tankdriveRC(double y, double t){
        /* Manul drive with gamepad
        y: left joystick y: forward/backward; no strafing
        t: right joystick x: turning
         */
        frontleftPower = -y+t;
        backleftPower = - y+t;
        frontrightPower = -y-t;
        backrightPower = -y-t;

        normalizePower();
        setPower();
    }

    private void normalizePower(){
        // normalize the power to be between -1 and 1
        double max = Math.max(Math.abs(frontleftPower), Math.abs(backleftPower));
        max = Math.max(max, Math.abs(frontrightPower));
        max = Math.max(max, Math.abs(backrightPower));
        if (max > 1.0) {
            frontleftPower = frontleftPower /max;
            backleftPower = backleftPower / max;
            frontrightPower = frontrightPower /max;
            backrightPower = backrightPower / max;
        }
        // Scale the power so the max is "maxPower"
        frontleftPower = frontleftPower * maxPower;
        backleftPower = backleftPower *maxPower;
        frontrightPower = frontrightPower *maxPower;
        backrightPower = backrightPower *maxPower;
    }

    private void setPower(){
        frontLeft.setPower(frontleftPower);
        backLeft.setPower(backleftPower);
        frontRight.setPower(frontrightPower);
        backRight.setPower(backrightPower);
    }

    //Directly set power to the given values
    public void setPowerDirect(double power){
        //Power cannot exceed 0.4. Small motors
        power = Math.min(Math.max(-0.4, power), 0.4);
        frontLeft.setPower(power);
        backLeft.setPower(power);
        frontRight.setPower(power);
        backRight.setPower(power);
    }
    public void updateTelemetry(){
        telemetry.addData("IMU degree", heading*180/Math.PI);
        telemetry.addData("frontleftPower", frontleftPower);
        telemetry.addData("frontrightPower", frontrightPower);
        telemetry.addData("backleftPower", backleftPower);
        telemetry.addData("backrightPower", backrightPower);
    }

    // Run motor one by one to check the port match
    // Port 0- front left; Port 2: front-right
    // Port 1 - back left; Port 3: back-right
    public void checkPort(Gamepad gamepad1){
        if (gamepad1.square){
            frontLeft.setPower(0.5);
        } else if (!gamepad1.square){
            frontLeft.setPower(0.0);
        }
        if (gamepad1.cross){
            backLeft.setPower(0.5);
        } else if (!gamepad1.square){
            backLeft.setPower(0.0);
        }
        if (gamepad1.triangle){
            frontRight.setPower(0.5);
        } else if (!gamepad1.triangle){
            frontRight.setPower(0.0);
        }
        if (gamepad1.circle){
            backRight.setPower(0.5);
        } else if(!gamepad1.circle){
            backLeft.setPower(0.0);
        }
    }
}
