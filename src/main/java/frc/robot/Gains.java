package frc.robot;

/**
 * A simple container class for PID Gains
 */
public class Gains{

    public double kF, kP, kI, kD;

    public Gains(double kF, double kP, double kI, double kD) {
        this.kF = kF;
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
    }
}