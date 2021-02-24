package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj2.command.*;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj.XboxController.Button;

//3205
import frc.robot.commands.intake.*;
import frc.robot.commands.shooter.*;
import frc.robot.commands.auto.*;
import frc.robot.commands.colorwheel.ColorControl;
import frc.robot.commands.colorwheel.RotationControl;
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
  Joystick monkey = new Joystick(kMonkeyPort);

  // The robot's subsystems and commands are defined here...
  public final Drive drive = new Drive();
  public final ColorWheel colorWheel = new ColorWheel();
  public final Shooter shooter = new Shooter();
  public final Intake intake = new Intake();
  public final Vision vision = new Vision(); 

  IntakeBall intakeBall = new IntakeBall(intake);
  Index index = new Index(intake);

  RotationControl rotationControl = new RotationControl(colorWheel);
  ColorControl colorControl = new ColorControl(colorWheel);

  ShootAll shootAll1 = new ShootAll(shooter, intake, 0.1);
  ShootAll shootAll2 = new ShootAll(shooter, intake, 0.2);
  ShootAll shootAll3 = new ShootAll(shooter, intake, 0.3);
  ShootAll shootAll4 = new ShootAll(shooter, intake, 0.4);

  StopAll stopAll = new StopAll(shooter, intake);

  private final AutoCommand m_autoCommand = new AutoCommand(drive, intake, shooter);


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
          -xbox.getX(Hand.kRight)
      ),
      drive)
    );
  }

  /**
   * Use this method to define your button -> command mappings.  Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
   * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {

    new JoystickButton(xbox, Button.kBumperRight.value)
      .whenPressed(intakeBall);

    new JoystickButton(xbox, Button.kBumperLeft.value)
      .whenPressed(index);

    new JoystickButton(xbox, Button.kBack.value)
      .whenPressed(stopAll);

    //shooting commands
    //zone 1
    new JoystickButton(xbox, Button.kB.value)
      .whenPressed(shootAll1);

    //2
    new JoystickButton(xbox, Button.kA.value)
      .whenPressed(shootAll2);

    //3
    new JoystickButton(xbox, Button.kX.value)
      .whenPressed(shootAll3);

    //4
    new JoystickButton(xbox, Button.kY.value)
      .whenPressed(shootAll4);

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
