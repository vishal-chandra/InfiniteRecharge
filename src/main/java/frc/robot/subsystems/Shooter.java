package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.*;

public class Shooter extends SubsystemBase {
  
  TalonSRX shooterTalon, shooterFollower;
  double shootRPM = commandToTargetVelocity(1);
  int shooterTolerance = 300; //this is in ticks per 100ms

  public Shooter() {
    shooterTalon = new TalonSRX(kShooterTalonID);
    shooterTalon.configFactoryDefault();

    shooterTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 
                                              0, kTimeoutMs);
    shooterTalon.setSensorPhase(false);

    //minimum
    shooterTalon.configNominalOutputForward(0, kTimeoutMs);
    shooterTalon.configNominalOutputReverse(0, kTimeoutMs);
    //maximum
    shooterTalon.configPeakOutputForward(1, kTimeoutMs);
    shooterTalon.configPeakOutputReverse(-1, kTimeoutMs);

    shooterTalon.config_kF(0, shooterGains.kF, kTimeoutMs);
    shooterTalon.config_kP(0, shooterGains.kP, kTimeoutMs);
    shooterTalon.config_kI(0, shooterGains.kI, kTimeoutMs);
    shooterTalon.config_kD(0, shooterGains.kD, kTimeoutMs);
  }

  public void startFlywheels() {
    shooterTalon.set(ControlMode.Velocity, shootRPM);
  }

  public void stopFlywheels() {
    shooterTalon.set(ControlMode.Velocity, commandToTargetVelocity(0));
  }

  public boolean checkRPM() {
    if(Math.abs(shooterTalon.getSelectedSensorVelocity() - shootRPM) < shooterTolerance)
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
    return command * maxDriveRPM * 4096 / 600;
  }

}
