package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.*;

public class Shooter extends SubsystemBase {
  
  TalonSRX shooterTalon, shooterFollower;

  public Shooter() {
    shooterTalon = new TalonSRX(kShooterTalonID);
    shooterTalon.configFactoryDefault();
    shooterTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, kTimeoutMs);
    shooterTalon.setSensorPhase(true);

    shooterTalon.config_kF(0, shooterGains.kF, kTimeoutMs);
    shooterTalon.config_kP(0, shooterGains.kP, kTimeoutMs);
    shooterTalon.config_kI(0, shooterGains.kI, kTimeoutMs);
    shooterTalon.config_kD(0, shooterGains.kD, kTimeoutMs);
    //shooterTalon.configAllowableClosedloopError(0, shooterTolerance, kTimeoutMs);

    shooterFollower = new TalonSRX(kShooterFollowerID);
    shooterFollower.configFactoryDefault();
    shooterFollower.setInverted(InvertType.FollowMaster);
    shooterFollower.follow(shooterTalon); 
  }

  public void stopFlywheels() {
    shooterTalon.set(ControlMode.PercentOutput, 0.0);
  }

  public void startFlywheels() {
    shooterTalon.set(ControlMode.Velocity, commandToTargetVelocity(0.5));
  }

  public void flywheelsPct() {
    shooterTalon.set(ControlMode.PercentOutput, 0.85);
  }

  public double getError() {
    return shooterTalon.getClosedLoopError(0);
  }

  private double commandToTargetVelocity(double command) {
    //see drive subsys for explanation
    return command * maxShootRPM * 4096 / 600;
  }
}
