/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.colorwheel;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.*;

public class RotationControl extends CommandBase {

  ColorWheel colorWheel;
  char initColor, lastColor, currentColor;
  int colorPasses = 0;
  boolean hasMoved = false;

  /**
   * Add Requirements
   */
  public RotationControl(ColorWheel colorWheelSystem) {
    colorWheel = colorWheelSystem;
    addRequirements(colorWheel);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    initColor = colorWheel.readColor();
    lastColor = initColor;
    colorWheel.startMotor();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    currentColor = colorWheel.readColor();

    if(currentColor != lastColor) hasMoved = true;

    if(currentColor == initColor && hasMoved) {
      colorPasses++;
      System.out.println(colorPasses);
      hasMoved = false;
    }

    lastColor = currentColor;
  }
 
  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    colorWheel.stopMotor();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if(!colorWheel.getSwitch()) {
      colorPasses = 0;
      return true;
    }
    if(colorPasses == 6) {
      colorPasses = 0; //reset to original state
      return true;
    }
    else
      return false;
  }
}
