package org.firstinspires.ftc.teamcode.Fall_2026.ClassPractice.Mechanisms;


import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class test {
    private Servo clawServo;
    // Claw position from ServoCalibration
    private Telemetry telemetry;
    private final double CLAW_OPEN_POS = 0.47;
    //RC2: 0.47, Change to the position you identified
    //RC3: 0.43
    //RC4:
    private final double CLAW_CLOSE_POS = 0.69;
    //RC2: 0.67, Change to the position you identified
    //RC3: 0.27
    //RC4:

    private double claw_cur_pos;
    public boolean isOpen;
    boolean prevtogglebuttonpressed = false;

    private ElapsedTime clawTimer;

    public enum CLAWSTATE{CLOSE, OPEN, IDLE}

    private CLAWSTATE clawstate = CLAWSTATE.IDLE;

    public void init(HardwareMap hwMap, Telemetry telemetry){
        clawServo = hwMap.get(Servo.class, "claw_servo");
        //Set initial claw position to the Open position
        claw_cur_pos = CLAW_OPEN_POS;
        clawServo.setPosition(claw_cur_pos);
        this.telemetry = telemetry;
        isOpen = true;
        clawTimer = new ElapsedTime();
        clawstate = CLAWSTATE.IDLE;
    }

    public void update(){
        switch(clawstate){
            case CLOSE:
                closeClaw_v2();
                break;

            case OPEN:
                openClaw_v2();
                break;

            case IDLE:
                telemetry.addLine("Claw is IDLE");
                break;
        }
    }
    public double getClawPos(Telemetry telemetry){
        return claw_cur_pos;
    }

    //Manually close more or open more of the claw
    public void adjustClawPos(int close) {
        if (close == 1) {
            claw_cur_pos += 0.02;
        } else if (close == -1) {
            claw_cur_pos -= 0.02;
        }
    }
    public void setClawPos(double pos){
        //Limit the position to be between Open and Close position
        pos = Math.max(CLAW_CLOSE_POS, Math.min(pos, CLAW_OPEN_POS));
        clawServo.setPosition(pos);
        claw_cur_pos = pos;
    }

    public void closeClaw_v1(){
        clawServo.setPosition(CLAW_CLOSE_POS);
        claw_cur_pos = CLAW_CLOSE_POS;
    }

    // With State Machine control
    public void closeClaw_v2(){
        if(clawstate == CLAWSTATE.IDLE ) {
            clawTimer.reset();  // reset timer when just start closing
        }
        clawServo.setPosition(CLAW_CLOSE_POS);
        claw_cur_pos = CLAW_CLOSE_POS;
        // after 500 ms, the claw fully closed. Set state to IDLE
        if(clawTimer.milliseconds() > 500){
            clawstate =CLAWSTATE.IDLE;
        }
    }

    public void openClaw_v1(){
        clawServo.setPosition(CLAW_OPEN_POS);
        claw_cur_pos = CLAW_OPEN_POS;
    }

    public void openClaw_v2(){
        if(clawstate == CLAWSTATE.IDLE){
            clawTimer.reset();
        }
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

    public void toggleClaw_v2(){
        if (isOpen){
            openClaw_v1();
        } else {
            closeClaw_v1();
        }

    }
}

