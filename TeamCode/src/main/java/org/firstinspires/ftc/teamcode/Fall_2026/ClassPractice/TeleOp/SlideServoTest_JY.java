package org.firstinspires.ftc.teamcode.Fall_2026.ClassPractice.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Camp_2026.Mechanisms.SlideServo_JY;

@Disabled
@TeleOp(name="SlideServoTest_JY", group="CampTest")
public class SlideServoTest_JY extends OpMode {
    SlideServo_JY slideServo;
    double slide_pos;
    double looptime = 0;
    double lastlooptime = 0;

    // Gamepad button variables
    boolean crossPressed;
    boolean trianglePressed;

    /*
    The following fields are inherited from OpMode and can be used directly
    - hardwarMap
    - telemetry
    - gamepad1, gamepad2
     */
    public void init(){
        slideServo = new SlideServo_JY();
        slideServo.init(hardwareMap, telemetry);
        // Newly added
        crossPressed = false;
        trianglePressed = false;
    }

    public void loop(){
        getLoopTime();
        control_v2(); //the State Machine
        //control_v1(); //non State-Machine

        telemetry.update();
    }

    // The same as we did during camp
    public void control_v1(){
        // gamepad triangle/Y is pressed, raise slide to HIGH pos
        // gamepad cross/A: lower slide to LOW pos
        if(gamepad1.triangle){
            slideServo.setHighPos_v1();
        }

        if (gamepad1.cross){
            //slideServo.setLowPos_v1();
            slideServo.setLowPosSlow_v1();
            //slideServo.setLowPosSlow_v2();
        }
        slideServo.updateTelemetry();
    }

    //State Machine control
    public void control_v2(){
        // Changed the code below so the Slide State only is changed once.
        if (gamepad1.cross && !crossPressed){
            slideServo.resetTimer();
            slideServo.slideState = SlideServo_JY.SLIDESTATE.LOW;
            crossPressed = true;
        } else if (!gamepad1.cross) { crossPressed = false;} //the STATE is restored to IDLE in the hardware class
        if (gamepad1.triangle && !trianglePressed){
            slideServo.slideState = SlideServo_JY.SLIDESTATE.HIGH;
            slideServo.resetTimer();
            trianglePressed = true;
        } else if (!gamepad1.triangle){ trianglePressed = false;}
        slideServo.update();
        slideServo.updateTelemetry();
    }

    public void getLoopTime(){
        looptime = time - lastlooptime;
        lastlooptime = time;
        telemetry.addData("looptime mm ", looptime*1000);
    }
}
