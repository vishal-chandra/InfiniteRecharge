package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.*;

public class Shooter extends SubsystemBase {
  
  TalonSRX shooterTalon, shooterFollower;

  public Shooter() {
    shooterTalon = new TalonSRX(kShooterTalonID);
    shooterTalon.configFactoryDefault();

    shooterFollower = new TalonSRX(kShooterFollowerID);
    shooterFollower.configFactoryDefault();
    shooterFollower.setInverted(InvertType.FollowMaster);
    shooterFollower.follow(shooterTalon); 
  }

  public void stopFlywheels() {
    shooterTalon.set(ControlMode.PercentOutput, 0.0);
  }

  public void startFlywheels() {
    shooterTalon.set(ControlMode.PercentOutput, 1.0);
  }
}
