/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.colorwheel;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.*;

public class ColorControl extends CommandBase {

  ColorWheel colorWheel;

  /**
   * Add requirements
   */
  public ColorControl(ColorWheel colorWheelSystem) {
    colorWheel = colorWheelSystem;
    addRequirements(colorWheel);
  }

  @Override
  public void initialize() {
    colorWheel.getColorCommand();
    colorWheel.startMotor();
  }

  @Override
  public void execute() {}

  @Override
  public void end(boolean interrupted) {
    colorWheel.stopMotor();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {

    //commented out because we don't have a switch
    // if(!colorWheel.getSwitch()) { //end if we're not touching the wheel
    //   return true;
    // }
    //end as soon as we've reached the right color
    if(colorWheel.readColor() == colorWheel.sensorColorCommand) {
      return true;
    }
    else return false;  

  }
}
