/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.drive;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drive;

public class AutoRotate extends CommandBase {

  Drive drive;
  double kP = 0.0039;
  double errorThreshold = 2;

  /**
   * Creates a new AutoRotate.
   */
  public AutoRotate(Drive drive) {
    this.drive = drive;
    addRequirements(drive);
    // Use addRequirements() here to declare subsystem dependencies.
  }
  
  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double error = getError();
    double turnPower = kP * error;
    drive.curvatureDrive(0, turnPower);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if(getError() < errorThreshold) return true;
    return false;
  }

  public double getError() {
    return -drive.getAngle();
  }
}
