/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

//CTRE
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.*;

//3205
import static frc.robot.Constants.*;

public class Winch extends SubsystemBase {
  
  VictorSPX winchElevatorVictor; //motor that raises elevator
  VictorSPX winchHookVictor; //motor that pulls robot up
  
  /**
   * Creates a new Winch.
   */
  public Winch() {
    //init hardware
    winchElevatorVictor = new VictorSPX(kWinchElevatorID);
    winchHookVictor = new VictorSPX(kWinchHookID);

  }

  public void startElevatorMotor() {
    winchElevatorVictor.set(ControlMode.PercentOutput, 0.2);
  }

  public void startHookMotor() {
    winchHookVictor.set(ControlMode.PercentOutput, 0.2);
  }

  public void stopElevatorMotor() {
    winchElevatorVictor.set(ControlMode.PercentOutput, 0);
  }

  public void stopHookMotor() {
    winchHookVictor.set(ControlMode.PercentOutput, 0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
