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

  char towerState = ' ';

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

  /*Motor Methods*/
  public void runIntake() {
    intakeMotor.set(ControlMode.PercentOutput, -1.0);
  }

  public void stopIntake() {
    intakeMotor.set(ControlMode.PercentOutput, 0.0);
  }

  public void runTower() {
    towerMotor.set(ControlMode.PercentOutput, 0.4);
  }

  public void stopTower() {
    towerMotor.set(ControlMode.PercentOutput, 0.0);
  }
  
  public void reverseTower() {
    towerMotor.set(ControlMode.PercentOutput, -0.4);
  }

  /*Switch Methods*/
  public boolean ballInIntake() {
    return intakeSwitch.get();
  }

  public boolean ballAtTowerBottom() {
    return towerBottomSwitch.get();
  }

  public boolean ballAtTowerTop() {
    return towerTopSwitch.get();
  }

  public char getTowerState() {
    
    //case 1: empty (can't shoot, can intake freely)
    if(!ballAtTowerBottom() && !ballAtTowerTop()) {
      towerState = 'E';
      return towerState;
    }

    //case 2: balls at top but not full (can shoot freely, must bring down to intake)
    else if(ballAtTowerTop() && !ballAtTowerBottom()) {
      towerState = 'T';
      return towerState;
    }

    //case 3: balls at bottom but not full (must bring up to shoot, can intake freely)
    else if(ballAtTowerBottom() && !ballAtTowerTop()) {
      towerState = 'B';
      return towerState;
    }

    //case 4: full (can shoot freely, can't intake)
    else if(ballAtTowerBottom() && ballAtTowerTop() && ballInIntake()) {
      towerState = 'F';
      return towerState;
    }

    else {
      towerState = 'U';
      return towerState;
    }
  }

}
