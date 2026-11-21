package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Sample TeleOp that drives a mecanum robot with gamepad 1.
 *
 * Controls:
 *   Left stick  - forward/back and strafe
 *   Right stick - rotate
 *
 * Robot configuration (Driver Station) must contain four DC motors named:
 *   "leftFront", "rightFront", "leftBack", "rightBack"
 */
@TeleOp(name = "Sample: Move Robot", group = "Sample")
public class SampleMoveRobot extends LinearOpMode {

    private DcMotor leftFront;
    private DcMotor rightFront;
    private DcMotor leftBack;
    private DcMotor rightBack;

    @Override
    public void runOpMode() {
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");

        // Mirror the motors on one side so positive power drives forward.
        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);

        telemetry.addLine("Ready. Press START.");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            double drive = -gamepad1.left_stick_y;   // forward is negative on the stick
            double strafe = gamepad1.left_stick_x;
            double turn = gamepad1.right_stick_x;

            double frontLeft = drive + strafe + turn;
            double frontRight = drive - strafe - turn;
            double backLeft = drive - strafe + turn;
            double backRight = drive + strafe - turn;

            // Normalize so no power exceeds 1.0 while keeping the ratios.
            double max = Math.max(1.0, Math.max(
                    Math.max(Math.abs(frontLeft), Math.abs(frontRight)),
                    Math.max(Math.abs(backLeft), Math.abs(backRight))));

            leftFront.setPower(frontLeft / max);
            rightFront.setPower(frontRight / max);
            leftBack.setPower(backLeft / max);
            rightBack.setPower(backRight / max);

            telemetry.addData("Drive", "%.2f", drive);
            telemetry.addData("Strafe", "%.2f", strafe);
            telemetry.addData("Turn", "%.2f", turn);
            telemetry.update();
        }

        leftFront.setPower(0);
        rightFront.setPower(0);
        leftBack.setPower(0);
        rightBack.setPower(0);
    }
}
