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
  char initColor;
  int colorPasses = 0;

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
    System.out.println("start motor");
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    char currentColor = colorWheel.readColor();
    if(currentColor == initColor) colorPasses++;
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    System.out.println("stop motor");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if(colorPasses == 6)
      return true;
    else
      return false;
  }
}
