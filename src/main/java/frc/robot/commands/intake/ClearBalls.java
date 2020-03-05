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
  int stopCycle = 0;
  int ballsShot = 0;

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
    if(cycleCount == 50) {
      intake.runTower();
      intake.runIntake();
    }
    if(cycleCount > 70 && intake.ballAtTowerTop()) {
      intake.stopTower();
      intake.stopIntake();
      stopCycle = cycleCount;
    }
    if(cycleCount == stopCycle + 30) {
      intake.runTower();
      intake.runIntake();
      stopCycle = 0;
      ballsShot++;
    }

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    shooter.stopFlywheels();
    intake.stopIntake();
    intake.stopTower();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if(ballsShot == 3) {
      ballsShot = 0;
      cycleCount = 0;
      return true;
    }
    return false;
  }
}
