package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController.Button;

import edu.wpi.first.wpilibj2.command.*;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

//3205
import frc.robot.commands.*;
import frc.robot.subsystems.*;
import static frc.robot.Constants.*;

/**
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {

  XboxController xbox = new XboxController(kXboxPort);

  // The robot's subsystems and commands are defined here...
  public final Drive drive = new Drive();
  public final Shooter shooter = new Shooter();
  public final Intake intake = new Intake();

  IntakeBall intakeBall = new IntakeBall(intake);
  IndexEmpty indexEmpty = new IndexEmpty(intake);
  BringUp bringUp = new BringUp(intake);
  BringDown bringDown = new BringDown(intake);
  Index index = new Index(intake);
  FeedBall feedBall = new FeedBall(intake);

  StartFlywheels startFlywheels = new StartFlywheels(shooter);

  private final Command m_autoCommand = new WaitCommand(0);


  /**
   * The container for the robot.  Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();

    //this block sets up the background driving function
    //constantly look at the sticks and pass them to drivebase
    drive.setDefaultCommand(
      new RunCommand(
      () -> drive.curvatureDrive(
          xbox.getY(Hand.kLeft),
          -xbox.getX(Hand.kRight) //for some reason this needs to be reversed
      ),
      drive)
    );

    // intake.setDefaultCommand(
    //   new RunCommand(() -> intake.getTowerState(), intake)
    // );
  }

  /**
   * Use this method to define your button -> command mappings.  Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
   * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {

    new JoystickButton(xbox, Button.kStart.value).whenPressed(startFlywheels);
    new JoystickButton(xbox, Button.kBack.value).whenPressed(new InstantCommand(() -> shooter.stopFlywheels()));
    
    new JoystickButton(xbox, Button.kA.value).whenPressed(intakeBall);
    new JoystickButton(xbox, Button.kB.value).whenPressed(index);
    new JoystickButton(xbox, Button.kY.value).whenPressed(bringUp);
    new JoystickButton(xbox, Button.kX.value).whenPressed(feedBall);
  }


  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return m_autoCommand;
  }
}
