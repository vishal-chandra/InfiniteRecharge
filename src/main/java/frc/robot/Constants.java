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

    //Limit Switch Ports
    public static int kIntakeSwitchPort = 1;
    public static int kTowerTopSwitchPort  = 2;
    public static int kTowerBottomSwitchPort = 3;

    //Motor Controller CAN IDs
    public static int kShooterTalonID = 8;
    public static int kShooterFollowerID = 4;
    public static int kIntakeTalonPort = 2;
    public static int kTowerTalonID  = 3;
    
    public static int kLeftTalonID   = 9;
    public static int kLeftFollowerID  = 2;
    public static int kRightTalonID  = 6;
    public static int kRightFollowerID = 1;

    //window to check for the completion of ctre methods
    public static int kTimeoutMs = 30;

    //Motor Characteristics
    public static int maxDriveRPM = 567;

    //max throttle change in a second
    public static double kThrottleSlewRate = 1.1;
    public static double kTurnSlewRate = 0.9;
    public static double kShooterSlewRate = 1.0;

    //PID Gains (to be tuned)
    //gearing on new robot is 8.46:1
    public static Gains leftDriveGains  = new Gains(0.2643, 0, 0, 0); 
    public static Gains rightDriveGains = new Gains(0.2643, 0, 0, 0);

    public static Gains leftPosGains = new Gains(0, 0.07, 0, 0);
    public static Gains rightPosGains = new Gains(0, 0.07, 0, 0);
}
