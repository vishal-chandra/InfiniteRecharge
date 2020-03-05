/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.*;
import frc.robot.subsystems.*;
import frc.robot.commands.intake.*;



public class AutoCommand extends SequentialCommandGroup {
  /**
   * Creates a new AutoCommand.
   */
  public AutoCommand(Drive drive, Intake intake, Shooter shooter) {
    super(
      new InstantCommand(() -> drive.posMode = true),
      new InstantCommand(() -> drive.driveToDist(2)),
      new WaitCommand(1),
      new InstantCommand(() -> drive.driveToDist(-2)),
      new InstantCommand(() -> drive.posMode = false),
      new WaitCommand(1),
      new ShootAll(shooter, intake) 
    );
  }
}
