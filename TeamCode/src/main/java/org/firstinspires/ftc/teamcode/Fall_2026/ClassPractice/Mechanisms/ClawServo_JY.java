package org.firstinspires.ftc.teamcode.Fall_2026.ClassPractice.Mechanisms;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class ClawServo_JY {

    private Servo clawServo;
    // Claw position from ServoCalibration
    private Telemetry telemetry;

    //Use ServoCalibration to find the OPEN and CLOSE position
    private final double CLAW_OPEN_POS = 0.43;
    //RC2: 0.47, (CLOSE: 0.69) Change to the position you identified
    //RC3: 0.43, (CLOSE: 0.23) !!! OPEN > CLOSE. The other two Open < Close
    //RC4: 0.29 (need to change) (CLOSE: 0.50)
    private final double CLAW_CLOSE_POS = 0.23;
    //RC2: 0.69, Change to the position you identified
    //RC3: 0.23
    //RC4:  0.50 (need to change)

    private double claw_cur_pos;
    public boolean isOpen;
    boolean prevtogglebuttonpressed = false;

    //For mannually adjust slide using dpad_up/down
    boolean prev_dpad_left = false;
    boolean prev_dpad_right = false;

    //private final ElapsedTime clawTimer = new ElapsedTime();

    public void init(HardwareMap hwMap, Telemetry telemetry){
        clawServo = hwMap.get(Servo.class, "claw_servo");
        //Set initial claw position to the Open position
        claw_cur_pos = CLAW_OPEN_POS;
        clawServo.setPosition(claw_cur_pos);
        this.telemetry = telemetry;
        isOpen = true;
        //clawstate = CLAWSTATE.IDLE;
    }

    public double getClawPos(){
        return claw_cur_pos;
    }
    public void setClawPos(double pos){
        //Limit the position to be between Open and Close position
        pos = Math.max(CLAW_CLOSE_POS, Math.min(pos, CLAW_OPEN_POS));
        clawServo.setPosition(pos);
        claw_cur_pos = pos;
    }

    public void manuallyAdjustClaw(Gamepad gamepad1, int RCNumber){
        //Manually adjust claw position
        //dpad_left/right: close/open claw at 0.02 each step
        boolean dpad_left = gamepad1.dpad_left;
        boolean dpad_right = gamepad1.dpad_right;

        if (dpad_left && (dpad_left != prev_dpad_left)) {
            if(RCNumber == 3){
                claw_cur_pos -= 0.02;
            } else if (RCNumber == 2 || RCNumber == 4){
                claw_cur_pos += 0.02;
            }
            telemetry.addData("Closing claw to position ", getClawPos());
        } else if (dpad_right && (dpad_right!= prev_dpad_right)){
            if(RCNumber ==3){
                claw_cur_pos += 0.02;;
            }else if (RCNumber == 2 || RCNumber == 4){
                claw_cur_pos -= 0.02;
            }
            telemetry.addData("Opening claw to position ", getClawPos());
        }
        claw_cur_pos = Range.clip(claw_cur_pos, CLAW_CLOSE_POS, CLAW_OPEN_POS);
        // RC2 AND RC4, Close > Open. Uncomment the following
        //claw_cur_pos = Range.clip(claw_cur_pos, CLAW_OPEN_POS, CLAW_CLOSE_POS);
        clawServo.setPosition(claw_cur_pos);
        prev_dpad_left = dpad_left;
        prev_dpad_right = dpad_right;
    }

    public void closeClaw_v1(){
        clawServo.setPosition(CLAW_CLOSE_POS);
        claw_cur_pos = CLAW_CLOSE_POS;
    }

    public void openClaw_v1(){
        clawServo.setPosition(CLAW_OPEN_POS);
        claw_cur_pos = CLAW_OPEN_POS;
    }

    public void updateTelemetry(){
        telemetry.addData("Claw current pos ", claw_cur_pos);
    }

    // Press a button on the gamepad to switch claw postion from close to open and vice versa
    public void toggleClaw(boolean toggleButtonPressed){
        if(toggleButtonPressed && !prevtogglebuttonpressed){
            isOpen = !isOpen;
            prevtogglebuttonpressed = true;
        } else if (!toggleButtonPressed){
            prevtogglebuttonpressed = false;
        }
        if(prevtogglebuttonpressed){
            if (isOpen){
                openClaw_v1();
            } else {
                closeClaw_v1();
            }
        }
        telemetry.addData("isOpen =", isOpen);
    }

}
