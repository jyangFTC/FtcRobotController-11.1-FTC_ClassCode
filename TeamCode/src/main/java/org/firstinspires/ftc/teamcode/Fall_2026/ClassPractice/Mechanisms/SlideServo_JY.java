package org.firstinspires.ftc.teamcode.Fall_2026.ClassPractice.Mechanisms;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

// Use configuration "2026CampMiniBot2"
public class SlideServo_JY {

    private Servo slideServo;
    private Telemetry telemetry;
    private double slide_cur_pos;

    //Run ServoCalibration to find the high and low position of the slide
    private final double HIGH_POSITION = 0.345;
    //RC2:0.64 (LOW: 0.46)
    //RC4:0.64 OK (LOW 0.45)
    //RC3:0.345 (LOW 0.565) !!! RC3 HIGH < LOW.
    private final double LOW_POSITION = 0.55;
    //RC2: 0.50 old: 0.46 From servoCalibration
    //RC4: 0.45 MID: 0.51,
    //RC3: 0.55

    private boolean prev_dpad_up = false;
    private boolean prev_dpad_down = false;

    private final ElapsedTime slideTimer = new ElapsedTime();
    double last_time = 0;


    public void init(HardwareMap hwMap, Telemetry telemetry){
        slideServo = hwMap.get(Servo.class, "slide_servo");
        slideServo.setPosition(LOW_POSITION);
        this.telemetry = telemetry;
        slide_cur_pos = LOW_POSITION;
        slideTimer.reset();
    }

    public void manuallyAdjustSlide(Gamepad gamepad1, int RCNumber){
        //Manually adjust slide position
        //dpad_up/down: raise/lower slide
        boolean dpad_up = gamepad1.dpad_up;
        boolean dpad_down = gamepad1.dpad_down;
        if (dpad_up && (dpad_up != prev_dpad_up)) {
            if(RCNumber == 2 || RCNumber == 4){
                slide_cur_pos += 0.02;
            } else if (RCNumber == 3){
                slide_cur_pos -= 0.02;
            }
            telemetry.addData("Raising slide to position ", getSlidePos());
        } else if (dpad_down && (dpad_down!= prev_dpad_down)){
            if(RCNumber == 2 || RCNumber == 4){
                slide_cur_pos -= 0.02;
            } else if (RCNumber == 3){
                slide_cur_pos += 0.02;
            }
            telemetry.addData("Lowering slide to position ", getSlidePos());
        }

        if(RCNumber == 3){  //R3: LOW > HIGH
            slide_cur_pos = Range.clip(slide_cur_pos, HIGH_POSITION, LOW_POSITION);
        } else if(RCNumber == 2 || RCNumber == 4){ //R2, R4: HIGH > LOW
            slide_cur_pos = Range.clip(slide_cur_pos, LOW_POSITION, HIGH_POSITION);
        }
        slideServo.setPosition(slide_cur_pos);
        prev_dpad_up = dpad_up;
        prev_dpad_down = dpad_down;
    }

    //Only call the timer reset when the slide movement is triggered.
    public void resetTimer(){
        slideTimer.reset();
    }




    public void setSlidePosition(double position){
        position = Range.clip(position, LOW_POSITION, HIGH_POSITION); //Range.clip(n1, min, max)
        slideServo.setPosition(position);
        slide_cur_pos = position;
    }

    public void setHighPos_v1(){
        slideServo.setPosition(HIGH_POSITION);
        slide_cur_pos = HIGH_POSITION;

    }
    public void setHighPos_v2(){
        if(slideTimer.milliseconds()<300){
            slideServo.setPosition(HIGH_POSITION);
        } else {
            slide_cur_pos = HIGH_POSITION;
        }
    }

    public void setLowPos_v1(){
        slideServo.setPosition(LOW_POSITION);
        slide_cur_pos = LOW_POSITION;
    }

    //The current linkage design is a slider-crank mechanism. The crank angle at the low position
    // is almost parallel to the horizontal, making the vertical component minimal. Therefore, the
    // gravity pulls down the slide very fast near the low position, with significant impact at the
    // low position. This could damage the servo gear. Use the "slowDecendToLow()" instead
    public void setLowPos_v2(){
        if(slideTimer.milliseconds()<300){
            slideServo.setPosition(LOW_POSITION);
        } else {
            slide_cur_pos = LOW_POSITION;
        }

    }

   //Slowly drop the slide with elapsedtime interval. But have to reset slidetime at each drop step.
    public void setLowPosSlow_v2(int RCNumber){
        double step = 0.01;
        double interval = 10; // interval of each step: ms.
        if(RCNumber == 3){ //HIGH < LOW
            if (slide_cur_pos < LOW_POSITION && slideTimer.milliseconds() > interval) {
                slide_cur_pos += step;
                slideServo.setPosition(slide_cur_pos);
                telemetry.addData("Drop to slide position", slide_cur_pos);
                slideTimer.reset();
            }
            if (slide_cur_pos >= LOW_POSITION){
                slideServo.setPosition(LOW_POSITION); //Prevent from dropping below LOW POSITION
                //slideState = SLIDESTATE.IDLE; // Uncomment if using STATE MACHINE
            }
        } else if (RCNumber == 2 || RCNumber == 4){  // LOW < HIGH
            if (slide_cur_pos > LOW_POSITION && slideTimer.milliseconds() > interval) {
                slide_cur_pos -= step;
                slideServo.setPosition(slide_cur_pos);
                telemetry.addData("Drop to slide position", slide_cur_pos);
                slideTimer.reset();
            }
            if (slide_cur_pos <= LOW_POSITION){
                slideServo.setPosition(LOW_POSITION); //Prevent from dropping below LOW POSITION
                //slideState = SLIDESTATE.IDLE;
            }
        }
    }


    // Only position control, but no time control. Took about 20 cycles, at 1.7 ms/cycle, still very fast.
    public void setLowPosSlow_v1(){
        double step = 0.01;
        if (slide_cur_pos > LOW_POSITION) {
            slide_cur_pos -= step;
            //slide_cur_pos = slide_cur_pos - step;
            slideServo.setPosition(slide_cur_pos);
        }
    }

    public void updateTelemetry(){
        telemetry.addData("Slide cur pos ", slide_cur_pos);
    }

    public double getSlidePos(){
        return slide_cur_pos;
    }
}
