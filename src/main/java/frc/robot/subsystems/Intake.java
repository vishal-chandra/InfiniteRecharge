/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.*;

public class Intake extends SubsystemBase {
  
  DigitalInput intakeSwitch, towerBottomSwitch, towerTopSwitch;
  TalonSRX intakeMotor, towerMotor;

  public Intake() {

    //motor setup
    intakeMotor = new TalonSRX(kIntakeTalonID);
    intakeMotor.configFactoryDefault();

    towerMotor = new TalonSRX(kTowerTalonID);
    towerMotor.configFactoryDefault();

    //switch setup
    intakeSwitch = new DigitalInput(kIntakeSwitchPort);
    towerBottomSwitch = new DigitalInput(kTowerBottomSwitchPort);
    towerTopSwitch = new DigitalInput(kTowerTopSwitchPort);

  }

  public void runIntake() {
    intakeMotor.set(ControlMode.PercentOutput, 0.3);
  }

  public void stopIntake() {
    intakeMotor.set(ControlMode.PercentOutput, 0.0);
  }

  public void runTower() {
    towerMotor.set(ControlMode.PercentOutput, 0.0);
  }

  public void stopTower() {
    towerMotor.set(ControlMode.PercentOutput, 0.3);
  }

}
