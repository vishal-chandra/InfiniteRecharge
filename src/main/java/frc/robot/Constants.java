package frc.robot;

import frc.robot.Gains;


/**
 * Container for robot-wide constants
 *
 * Wherever needed, import the constants with
 * import static frc.robot.Constants.*
 */
public final class Constants {

    //OI
    public static int kXboxPort = 0;

    //Motor Controller CAN IDs
    public static int kColorWheelTalonID = 7;
    public static int kLeftTalonID   = 9;
    public static int kLeftFollowerID  = 2;
    public static int kRightTalonID  = 6;
    public static int kRightFollowerID = 1;

    //switch ports
    public static int kColorWheelSwitchPort = 0;

    //window to check for the completion of ctre methods
    public static int kTimeoutMs = 30;

    //Drive Characteristics 
    public static int maxDriveRPM = 567;
    //max throttle change in a second
    public static double kThrottleSlewRate = 1.3;
    public static double kTurnSlewRate = 0.9;
    public static double kFlywheelRatio = 1 / 8;

    //PID Gains (to be tuned)
    //gearing on new robot is 8.46:1
    public static Gains leftDriveGains  = new Gains(0.2643, 0, 0, 0); 
    public static Gains rightDriveGains = new Gains(0.2643, 0, 0, 0);
}
