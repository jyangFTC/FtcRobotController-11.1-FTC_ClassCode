package org.firstinspires.ftc.teamcode.Fall_2026.ClassPractice.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Fall_2026.ClassPractice.Mechanisms.DriveTrain_JY;

@Disabled
@TeleOp(name="DriveTrainTest_JY", group="Fall_2026")
public class DriveTrainTest_JY extends OpMode {

    private DriveTrain_JY driveTrain;
    int driveType = 0; //default - normal RC driving
    boolean StrafingOnly = false;
    boolean prevDpad_down = false;

    public void init(){
        driveTrain = new DriveTrain_JY();
        //For RC-3 and RC-4 robot, use init()
        driveTrain.init(hardwareMap, telemetry, gamepad1);

        //--------!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!-------
        // For RC-2, use init_RC2(), uncomment the next line

        //driveTrain.init_RC2(hardwareMap, telemetry, gamepad1);
        //-------------------------------------------------
    }

    public void loop(){

        driveTrain.checkPort();

        //Toggle strafing only and normal RC when dpad_down is pressed
        if (gamepad1.dpad_down && !prevDpad_down) {
            StrafingOnly = !StrafingOnly;
            prevDpad_down = true;
            telemetry.addLine("Toggled Strafing");
        }  else if (!gamepad1.dpad_down && prevDpad_down){
            prevDpad_down = false;
            telemetry.addLine("set prevDpad_down to false");
        }
        if (StrafingOnly){
            driveTrain.RCDrive(0, gamepad1.left_stick_x, gamepad1.right_stick_x);
            telemetry.addLine("Strafing only");
        } else {
            driveTrain.RCDrive(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
            telemetry.addLine("normal RC drive");
        }
        //driveTrain.tankdriveRC(gamepad1.left_stick_y, gamepad1.right_stick_x);
        driveTrain.RCDrive(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);

        driveTrain.updateTelemetry();
        telemetry.update();
    }
}
