package frc.robot.subsystems;

//WPI
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.MedianFilter;
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
  TalonSRX rightFollower;
  VictorSPX leftFollower;

  AnalogInput ultrasonic;
  MedianFilter ultraFilter;

  SlewRateLimiter throttleRamp, turnRamp;

  public boolean posMode;

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

    leftFollower = new VictorSPX(kLeftFollowerID);
    leftFollower.configFactoryDefault();

    rightTalon = new TalonSRX(kRightTalonID);
    rightTalon.configFactoryDefault();

    rightFollower = new TalonSRX(kRightFollowerID);
    rightFollower.configFactoryDefault();

    /**
     * LEFT DRIVE PID SETUP ------------------------------------------------------------------------
     */
    leftTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, kTimeoutMs);
    leftTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 1, kTimeoutMs);
    leftTalon.setSensorPhase(true);

    leftTalon.setInverted(true); //make it spin the right way
    leftFollower.setInverted(InvertType.FollowMaster); //make the follower spin the same way


    //minimum percent output
    leftTalon.configNominalOutputForward(0, kTimeoutMs);
    leftTalon.configNominalOutputReverse(0, kTimeoutMs);

    //maximum percent output
    leftTalon.configPeakOutputForward(1, kTimeoutMs);
    leftTalon.configPeakOutputReverse(-1, kTimeoutMs);

    //gains
    //vel
    leftTalon.config_kF(0, leftDriveGains.kF, kTimeoutMs);
    leftTalon.config_kP(0, leftDriveGains.kP, kTimeoutMs);
    leftTalon.config_kI(0, leftDriveGains.kI, kTimeoutMs);
    leftTalon.config_kD(0, leftDriveGains.kD, kTimeoutMs);
    //pos
    leftTalon.config_kF(1, leftPosGains.kF, kTimeoutMs);
    leftTalon.config_kP(1, leftPosGains.kP, kTimeoutMs);
    leftTalon.config_kI(1, leftPosGains.kI, kTimeoutMs);
    leftTalon.config_kD(1, leftPosGains.kD, kTimeoutMs);

    leftFollower.follow(leftTalon);

    /**
     * RIGHT DRIVE PID SETUP -----------------------------------------------------------------------
     */
    rightTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, kTimeoutMs);
    rightTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 1, kTimeoutMs);
    rightTalon.setSensorPhase(true); 

    //minimum percent output
    rightTalon.configNominalOutputForward(0, kTimeoutMs);
    rightTalon.configNominalOutputReverse(0, kTimeoutMs);

    //maximum percent output
    rightTalon.configPeakOutputForward(1, kTimeoutMs);
    rightTalon.configPeakOutputReverse(-1, kTimeoutMs);

    //gains
    //vel
    rightTalon.config_kF(0, rightDriveGains.kF, kTimeoutMs);
    rightTalon.config_kP(0, rightDriveGains.kP, kTimeoutMs);
    rightTalon.config_kI(0, rightDriveGains.kI, kTimeoutMs);
    rightTalon.config_kD(0, rightDriveGains.kD, kTimeoutMs);
    //pos
    rightTalon.config_kF(1, rightPosGains.kF, kTimeoutMs);
    rightTalon.config_kP(1, rightPosGains.kP, kTimeoutMs);
    rightTalon.config_kI(1, rightPosGains.kI, kTimeoutMs);
    rightTalon.config_kD(1, rightPosGains.kD, kTimeoutMs);

    rightFollower.follow(rightTalon);
    
    //Ramp init 
    throttleRamp = new SlewRateLimiter(kThrottleSlewRate);
    turnRamp = new SlewRateLimiter(kTurnSlewRate);

    //ultrasonic
    ultrasonic = new AnalogInput(0);
    ultraFilter = new MedianFilter(50);

    posMode = false;
  }

  /**
   * A reimplementation of WPI's DifferentialDrive.curvatureDrive()
   * method written for compatibility with closed-loop control.
   * Quick Turn is assumed.  
   * 
   * @param power desired speed as a number [-1.0, 1.0]
   * @param turn  desired curvature as a number [-1.0, 1.0]
   */
  public void curvatureDrive(double power, double turn, boolean allowed) {

    //adjust inputs
    double curvedPower = throttleRamp.calculate(curve(power));
    double curvedTurn  = turnRamp.calculate(curve(turn));

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

    leftCommand = commandToTargetVelocity(leftCommand);
    rightCommand = commandToTargetVelocity(rightCommand);

    //send commands
    if(!posMode) {
      if(!allowed) {
        if(leftCommand > 0 && rightCommand > 0) {
          leftTalon.set(ControlMode.Velocity, leftCommand);
          rightTalon.set(ControlMode.Velocity, rightCommand);
        }
        else {
          leftTalon.set(ControlMode.Velocity, commandToTargetVelocity(0.05));
          rightTalon.set(ControlMode.Velocity, commandToTargetVelocity(0.05));
        }
      }
      else {
        leftTalon.set(ControlMode.Velocity, leftCommand);
        rightTalon.set(ControlMode.Velocity, rightCommand);
      }
    }
  }

  public void driveToDist(double dist) {
    double rotations = dist / (Math.PI * 0.5);
    double rotationsInTicks = rotations * 4096;

    leftTalon.setSelectedSensorPosition(0);
    rightTalon.setSelectedSensorPosition(0);

    leftTalon.selectProfileSlot(1, 0);
    rightTalon.selectProfileSlot(1, 0);

    leftTalon.set(ControlMode.Position, rotationsInTicks);
    rightTalon.set(ControlMode.Position, rotationsInTicks);
  }

  public void getUltra() {
    double voltageReading = ultraFilter.calculate(ultrasonic.getVoltage());
    double voltsPerMM = 5.0 / 5120;

    double rangeMM = voltageReading / voltsPerMM;
    double rangeFt = rangeMM / 304.8;

    System.out.println(rangeFt);
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
