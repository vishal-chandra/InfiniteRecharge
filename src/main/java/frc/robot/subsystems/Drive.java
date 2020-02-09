package frc.robot.subsystems;

//WPI
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.SlewRateLimiter;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

//CTRE
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.*; 

//3205
import static frc.robot.Constants.*;


public class Drive extends SubsystemBase {
  
  TalonSRX leftTalon, rightTalon;
  VictorSPX leftVictor, rightVictor;
  SlewRateLimiter throttleRamp;

  /**
   * Constructor
   * 
   * Sets up controllers and configures
   * left and right PID Loops
   */
  public Drive() {

    /**
     * Controller Setup
     */
    leftTalon = new TalonSRX(kLeftTalonID);
    leftTalon.configFactoryDefault();

    leftVictor = new VictorSPX(kLeftVictorID);
    leftVictor.configFactoryDefault();

    rightTalon = new TalonSRX(kRightTalonID);
    rightTalon.configFactoryDefault();

    rightVictor = new VictorSPX(kRightVictorID);
    rightVictor.configFactoryDefault();

    /**
     * LEFT DRIVE PID SETUP ------------------------------------------------------------------------
     */
    leftTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, kTimeoutMs);
    leftTalon.setSensorPhase(true);

    leftTalon.setInverted(true); //make it spin the right way
    leftVictor.setInverted(InvertType.FollowMaster); //make the follower spin the same way


    //minimum percent output
    leftTalon.configNominalOutputForward(0, kTimeoutMs);
    leftTalon.configNominalOutputReverse(0, kTimeoutMs);

    //maximum percent output
    leftTalon.configPeakOutputForward(1, kTimeoutMs);
    leftTalon.configPeakOutputReverse(-1, kTimeoutMs);

    //gains
    leftTalon.config_kF(0, leftDriveGains.kF, kTimeoutMs);
    leftTalon.config_kP(0, leftDriveGains.kP, kTimeoutMs);
    leftTalon.config_kI(0, leftDriveGains.kI, kTimeoutMs);
    leftTalon.config_kD(0, leftDriveGains.kD, kTimeoutMs);

    leftVictor.follow(leftTalon);

    /**
     * RIGHT DRIVE PID SETUP -----------------------------------------------------------------------
     */
    rightTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, kTimeoutMs);
    rightTalon.setSensorPhase(true); 

    //minimum percent output
    rightTalon.configNominalOutputForward(0, kTimeoutMs);
    rightTalon.configNominalOutputReverse(0, kTimeoutMs);

    //maximum percent output
    rightTalon.configPeakOutputForward(1, kTimeoutMs);
    rightTalon.configPeakOutputReverse(-1, kTimeoutMs);

    //gains
    rightTalon.config_kF(0, rightDriveGains.kF, kTimeoutMs);
    rightTalon.config_kP(0, rightDriveGains.kP, kTimeoutMs);
    rightTalon.config_kI(0, rightDriveGains.kI, kTimeoutMs);
    rightTalon.config_kD(0, rightDriveGains.kD, kTimeoutMs);

    rightVictor.follow(rightTalon);
    
    //Ramp init 
    throttleRamp = new SlewRateLimiter(kThrottleSlewRate);
  }

  /**
   * A reimplementation of WPI's DifferentialDrive.curvatureDrive()
   * method written for compatibility with closed-loop control.
   * Quick Turn is assumed.  
   * 
   * @param power desired speed as a number [-1.0, 1.0]
   * @param turn  desired curvature as a number [-1.0, 1.0]
   */
  public void curvatureDrive(double power, double turn) {

    //adjust inputs
    double curvedPower = throttleRamp.calculate(curve(power));
    double curvedTurn  = curve(turn);

    //a positive turn command should speed up the left side
    double leftCommand  = curvedPower + curvedTurn;
    //a negative turn command should speed up the right side
    double rightCommand = curvedPower - curvedTurn;

    //if a command is outside the domain [-1.0, 1.0]
    //bring that command down/up to +-1.0 and bring
    //the other command down/up by the same amount
    if(leftCommand > 1.0) {
      double difference = leftCommand - 1.0;
      leftCommand = 1.0;
      rightCommand -= difference;
    }
    else if(rightCommand > 1.0) {
      double difference = rightCommand - 1.0;
      leftCommand -= difference;
      rightCommand = 1.0;
    }
    else if(leftCommand < -1.0) {
      double difference = leftCommand + 1.0; 
      leftCommand = -1.0; 
      rightCommand -= difference;
    }
    else if(rightCommand < -1.0) {
      double difference = rightCommand + 1.0;
      leftCommand -= difference;
      rightCommand = -1.0;
    }

    //send commands
    leftTalon.set(ControlMode.Velocity, commandToTargetVelocity(leftCommand));
    rightTalon.set(ControlMode.Velocity, commandToTargetVelocity(rightCommand));
  }

  /**
   * f(x) = x|x|
   * curves input so that sensitivity increases 
   * at higher values
   * 
   * @param input the current value of a joystick
   * @return the curved output of the function
   */
  double curve(double input) {
    return Math.abs(input) * input;
  }

  /**
   * takes a joystick command between -1.0 and 1.0
   * and returns a target velocity value in 
   * native units per 100 milliseconds
   * 
   * @param command
   * @return target velocity
   */
  double commandToTargetVelocity(double command) {

    //convert command to a percentage of maxRPM, 
    //then convert revolutions to units
    //and minutes to 100ms
    return command * maxDriveRPM * 4096 / 600;
  }

  public void updateShuffleboard() {
    SmartDashboard.putNumber("leftDriveError", leftTalon.getClosedLoopError());
    SmartDashboard.putNumber("rightDriveError", rightTalon.getClosedLoopError());

    SmartDashboard.putNumber("leftVel", leftTalon.getSelectedSensorVelocity());
    SmartDashboard.putNumber("rightVel", rightTalon.getSelectedSensorVelocity());

    SmartDashboard.putNumber("leftTarget", leftTalon.getClosedLoopTarget());
    SmartDashboard.putNumber("rightTarget", rightTalon.getClosedLoopTarget());
  }

}
