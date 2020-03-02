/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.*;

public class ClearBalls extends CommandBase {
  
  Intake intake;
  Shooter shooter;
  int cycleCount = 0;

  public ClearBalls(Intake intakeSystem, Shooter shooterSystem) {
    intake = intakeSystem;
    shooter = shooterSystem;
    addRequirements(intake, shooter);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    shooter.startFlywheels();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    cycleCount++;
    if(cycleCount == 100) {
      intake.runTower();
      intake.runIntake();
    }
    if(cycleCount == 400) {
      intake.stopTower();
      intake.stopIntake();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if(cycleCount == 500) {
      cycleCount = 0;
      shooter.stopFlywheels();
      return true;
    }
    return false;
  }
}
