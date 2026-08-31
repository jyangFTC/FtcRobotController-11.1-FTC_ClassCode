package org.firstinspires.ftc.teamcode.Fall_2026.ClassPractice.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Fall_2026.ClassPractice.Mechanisms.ClawServo_JY;

@Disabled
@TeleOp(name="ClawServoTest_JY", group="CampTest")
public class ClawServoTest_JY extends OpMode {
    ClawServo_JY clawServo;
    double claw_pos;
    boolean clawClosed = false;

    double looptime = 0;
    double lastlooptime = 0;

    boolean circlePressed = false;
    boolean crossPressed = false;

    /*
    The following fields are inherited from OpMode and can be used directly
    - hardwarMap
    - telemetry
    - gamepad1, gamepad2
     */
    public void init(){
        clawServo = new ClawServo_JY();
        clawServo.init(hardwareMap, telemetry);
    }

    public void loop(){
        control_v1();

        clawServo.updateTelemetry();
        telemetry.addData("looptime mm ", looptime*1000);
        telemetry.update();


    }


    private void toggleClaw(){
        // Press the circle button onetime to open or close the claw
        if (gamepad1.circle && !circlePressed){
            clawClosed = !clawClosed;
            circlePressed = true;
        } else if (!gamepad1.circle){
            circlePressed = false;
        }
        if(clawClosed){
            clawServo.closeClaw_v1();
        } else {
            clawServo.openClaw_v1();
        }
        telemetry.addData("Claw status", clawClosed);
        telemetry.addData("CirclePresses is", circlePressed);
    }

    private void control_v1(){
        // Move the current gamepad control code in loop() to here.
        // gamepad square/X is pressed, open the claw
        // gamepad circle/B: close the claw
        if(gamepad1.square){
            clawServo.closeClaw_v1();
        }
        if (gamepad1.circle){
            clawServo.openClaw_v1();
        }
        if (gamepad1.cross && !crossPressed){
            clawServo.isOpen = ! clawServo.isOpen;
            crossPressed = true;

        } else if (!gamepad1.cross){
            crossPressed = false;
        }
        if (crossPressed){
            clawServo.toggleClaw_v2();
        }
        //clawServo.toggleClaw(gamepad1.cross);
    }

    public void getLoopTime(){
        looptime = time - lastlooptime;
        lastlooptime = time;
    }
}
