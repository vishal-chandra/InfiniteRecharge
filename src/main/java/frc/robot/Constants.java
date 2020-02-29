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
    public static int kLeftTalonID   = 1;
    public static int kLeftVictorID  = 2;
    public static int kRightTalonID  = 4;
    public static int kRightVictorID = 3;
<<<<<<< HEAD
    public static int kShooterTalonID = 4;
    public static int kShooterTalonFollowerID = 8;
=======
    public static int kShooterTalonID = 10;
>>>>>>> e479fbee1539bfcb7c71b4fd107e7f91108642b1

    //window to check for the completion of ctre methods
    public static int kTimeoutMs = 30;

    //Motor Characteristics
    public static int maxDriveRPM = 495;
    public static int maxShooterRPM = 4000; 

    //max throttle change in a second
    public static double kThrottleSlewRate = 1.1;
    public static double kTurnSlewRate = 0.9;

    //PID Gains (to be tuned)
    //gearing on new robot is 8.46:1
    public static Gains leftDriveGains  = new Gains(0.3053, 0.3, 0, 0); 
    public static Gains rightDriveGains = new Gains(0.3053, 0.3, 0, 0);
    public static Gains shooterGains    = new Gains(0, 0, 0, 0);
}
