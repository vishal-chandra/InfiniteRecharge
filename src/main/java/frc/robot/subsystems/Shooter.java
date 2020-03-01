package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.SlewRateLimiter;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.*;

public class Shooter extends SubsystemBase {
  
  TalonSRX shooterTalon, shooterFollower;

  public SlewRateLimiter shooterRamp;
  public double shootCommand;

  int shooterTolerance = 5500; //this is in ticks per 100ms

  public Shooter() {
    shooterTalon = new TalonSRX(kShooterTalonID);
    shooterTalon.configFactoryDefault();

    shooterTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 
                                              0, kTimeoutMs);
    shooterTalon.setSensorPhase(true);

    shooterTalon.config_kF(0, shooterGains.kF, kTimeoutMs);
    shooterTalon.config_kP(0, shooterGains.kP, kTimeoutMs);
    shooterTalon.config_kI(0, shooterGains.kI, kTimeoutMs);
    shooterTalon.config_kD(0, shooterGains.kD, kTimeoutMs);

    shooterFollower = new TalonSRX(kShooterFollowerID);
    shooterFollower.configFactoryDefault();
    shooterFollower.setInverted(InvertType.FollowMaster);
    shooterFollower.follow(shooterTalon); 

    shooterRamp = new SlewRateLimiter(kShooterSlewRate);

    shootCommand = commandToTargetVelocity(shootPower);
  }

  public void setFlywheels(double power) {
    shooterTalon.set(ControlMode.Velocity, commandToTargetVelocity(shooterRamp.calculate(power)));
  }

  public void stopFlywheels() {
    shooterTalon.set(ControlMode.PercentOutput, 0.0);
  }

  public boolean checkRPM() {
    if(Math.abs(shooterTalon.getSelectedSensorVelocity() - shootCommand) < shooterTolerance)
      return true;
    else return false;
  }

  /**
   * takes a command between -1.0 and 1.0
   * and returns a target velocity value in 
   * native units per 100 milliseconds
   * 
   * @param command
   * @return target velocity
   */
  double commandToTargetVelocity(double command) {

    //convert command to a percentage of maxRPM, 
    //then convert revolutions to units
    //and minutes to 100ms
    return command * maxShooterRPM * 4096 / 600;
  }

  public void updateShuffleboard() {
    SmartDashboard.putNumber("shooterCommand", shooterTalon.getClosedLoopTarget());
    SmartDashboard.putNumber("shooterVel", shooterTalon.getSelectedSensorVelocity());
    SmartDashboard.putNumber("shooterError", shooterTalon.getClosedLoopError());
  }

}
