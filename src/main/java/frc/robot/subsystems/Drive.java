package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.custom.WPI_VeloTalon;
import edu.wpi.first.wpilibj.SlewRateLimiter;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix.sensors.PigeonIMU;

import static frc.robot.Constants.*;

/**
 * Basic tank drive with WPI-wrapped motor controllers
 * see https://docs.ctre-phoenix.com/en/stable/ch15_WPIDrive.html
 */
public class Drive extends SubsystemBase {

  WPI_VeloTalon leftTalon, rightTalon;
  WPI_TalonSRX rightTalonFollower;
  WPI_VictorSPX leftVictor;
  DifferentialDrive drivetrain;

  PigeonIMU gyro;
  double[] ypr = {0, 0, 0};

  SlewRateLimiter throttleRamp, turnRamp;

  /** Creates a new Drive. */
  public Drive() {

    leftTalon  = new WPI_VeloTalon(leftTalonPort, leftDriveGains);
    leftTalon.setSensorPhase(true);

    rightTalon = new WPI_VeloTalon(rightTalonPort, rightDriveGains);
    rightTalon.setSensorPhase(true);
    rightTalon.setInverted(true);
    
    drivetrain = new DifferentialDrive(leftTalon, rightTalon);
    drivetrain.setRightSideInverted(false);

    //follower setup

    leftVictor  = new WPI_VictorSPX(leftVictorPort);
    leftVictor.configFactoryDefault();
    leftVictor.follow(leftTalon);
    leftVictor.setInverted(InvertType.FollowMaster);

    rightTalonFollower = new WPI_TalonSRX(rightTalonFollowerPort);
    rightTalonFollower.configFactoryDefault();
    rightTalonFollower.follow(rightTalon);
    rightTalonFollower.setInverted(InvertType.FollowMaster);

    //slew rate setup
    throttleRamp = new SlewRateLimiter(kThrottleSlewRate);
    turnRamp = new SlewRateLimiter(kTurnSlewRate);

    //gyro setup
    gyro = new PigeonIMU(rightTalonFollower);
    gyro.setYaw(0);
    gyro.setFusedHeading(0);

  }

  public void curvatureDrive(double power, double turn) {
    double adjPower = throttleRamp.calculate(curve(power) / 2.5);
    double adjTurn = turnRamp.calculate(curve(turn) / 4);

    //send adjusted values to motors
    drivetrain.curvatureDrive(adjPower, adjTurn, true);
  }

  /**
   * f(x) = |x|x
   */
  double curve(double input) {
    return Math.abs(input) * input;
  }

  public double getAngle() {
    gyro.getYawPitchRoll(ypr);
    return ypr[0];
  }

  public void periodic() {
    // System.out.println("L vel " + leftTalon.getSelectedSensorVelocity() + "  R vel: " + rightTalon.getSelectedSensorVelocity() +
    //                    "L targ " + leftTalon.getClosedLoopTarget(0) + " R targ: " + rightTalon.getClosedLoopTarget());

    System.out.println(moddedAngle(getAngle()));
    
  }

  public double moddedAngle(double angle) {
    
    int rotations;
    if(angle < 0) rotations = 1 + Math.abs((int) (angle / 360));
    else rotations = (int) (angle / 360);
    
    if(angle < 0) angle += rotations * 360;
    else angle -= rotations * 360;

    if(angle > 180) angle -= 360;
    return angle;
  }


}
