package org.firstinspires.ftc.teamcode.Fall_2026.ClassPractice.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.ServoImplEx;

//@Disabled
@TeleOp(name="ServoCalibration_jy", group="CampTest")

// Using "2026CampMiniBot" configuration on Blue DS
/*
 Find the servo positions for claw and slide. Everytime when the slide and claw are removed, need
 to find the open/close for claw, top/bottom for slide again
 - Use dpad/up and down to riase and lower the slide
 - Use dpad/left and right to
 */
public class ServoCalibration_JY extends OpMode {

    private ServoImplEx slideServo;
    private ServoImplEx clawServo;

    private final double CLAW_OPEN_POSITION = 0.29; //Find the exact position
    private final double CLAW_CLOSE_POSITION = 0.50; //0.485, 0.457, VERY TIGHT
    private final double SLIDE_HIGH_POSITION = 0.65; //Find the exact position
    private final double SLIDE_LOW_POSITION = 0.47; //Find the exact position

    double slideCurrentPosition = 0.5;
    double clawCurrentPosition = 0.4;// 0.4

    boolean isdpad_up = false;
    boolean isdpad_down = false;
    boolean isdpad_left = false;
    boolean isdpad_right = false;

    public void init() {
        // REV controller default servo PWM: 1000 - 2000 us.
        // REV Smart Robotic Servo PWM: 500 - 2500 us from spec sheet.
        // SWYFT servo is the same, see noted on https://github.com/SWYFT-Robotics/swyft-servo-programmer-v2
        PwmControl.PwmRange range = new PwmControl.PwmRange(500, 2500);
        //Cut range: 1000-2000: Slide LOW: 1000+(2000-1000)*0.196=1196; HIGH: 1000+0.02*1000=1020
        // Actual: 500-2500: Slide LOW: 2000*0.196+500=892. HIGH: 500+2000*0.02=540
        // 1196 > 892, push slide lower than its physical limit
        // 1020 > 892, still below the lower limit. So the slide will not move at all
        // So must set the servo PWM to its actual range: 500, 2500
        slideServo = hardwareMap.get(ServoImplEx.class, "slide_servo");
        slideServo.setPwmRange(range);
        slideServo.setPosition(slideCurrentPosition);
        clawServo = hardwareMap.get(ServoImplEx.class, "claw_servo");
        clawServo.setPwmRange(range);
        clawServo.setPosition(clawCurrentPosition);
    }

    // This run only once when the INIT button is pressed

    public void init_loop() {
        PwmControl.PwmRange range = slideServo.getPwmRange();
        telemetry.addData("Slide Servo PWM range", "%.0f - %.0f us", range.usPulseLower, range.usPulseUpper);
        telemetry.addData("Slide servo init position ", slideServo.getPosition());
        telemetry.addData("Claw servo init position ", clawServo.getPosition());

    }

    // This run repeatedly when the PLAY button is pressed
    public void loop() {
        if (gamepad1.dpad_up && !isdpad_up) {
            slideCurrentPosition += 0.01;
            //slideCurrentPosition = Range.clip(slideCurrentPosition, SLIDE_HIGH_POSITION,SLIDE_LOW_POSITION);
            //slideServo.setPosition(slideCurrentPosition);
            isdpad_up = true;
        } else if (!gamepad1.dpad_up) {
            isdpad_up = false;
        }
        if (gamepad1.dpad_down && !isdpad_down) {
            slideCurrentPosition -= 0.01;
            //slideCurrentPosition = Range.clip(slideCurrentPosition, SLIDE_HIGH_POSITION,SLIDE_LOW_POSITION);
            //slideServo.setPosition(slideCurrentPosition);
            isdpad_down = true;
        } else if (!gamepad1.dpad_down) {
            isdpad_down = false;
        }
        if (gamepad1.dpad_left && !isdpad_left) {
            clawCurrentPosition += 0.01;
            //clawCurrentPosition = Range.clip(clawCurrentPosition, CLAW_CLOSE_POSITION,CLAW_OPEN_POSITION);
            //clawServo.setPosition(clawCurrentPosition);
            isdpad_left = true;
        } else if (!gamepad1.dpad_left) {
            isdpad_left = false;
        }
        if (gamepad1.dpad_right && !isdpad_right) {
            clawCurrentPosition -= 0.01;
            //clawCurrentPosition = Range.clip(clawCurrentPosition, CLAW_CLOSE_POSITION, CLAW_OPEN_POSITION);
            //clawServo.setPosition(clawCurrentPosition);
            isdpad_right = true;
        } else if (!gamepad1.dpad_right) {
            isdpad_right = false;
        }

        //clawCurrentPosition = Range.clip(clawCurrentPosition, CLAW_CLOSE_POSITION,CLAW_OPEN_POSITION);
        //slideCurrentPosition = Range.clip(slideCurrentPosition, SLIDE_HIGH_POSITION, SLIDE_LOW_POSITION);
        slideServo.setPosition(slideCurrentPosition);
        clawServo.setPosition(clawCurrentPosition);
        telemetry.addData("Slide servo position ", slideCurrentPosition);
        telemetry.addData("Claw servo position ", clawCurrentPosition);
        telemetry.update();
    }
}
