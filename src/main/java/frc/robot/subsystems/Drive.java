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

//LIDAR
import java.nio.ByteBuffer;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.hal.I2CJNI;


public class Drive extends SubsystemBase {
  
  TalonSRX leftTalon, rightTalon;
  TalonSRX rightFollower;
  VictorSPX leftFollower;

  SlewRateLimiter throttleRamp, turnRamp;

  //LIDAR variables
  private static final byte k_deviceAddress = 0x62; //default I2C address
  private final byte m_port;
  private final ByteBuffer m_buffer = ByteBuffer.allocateDirect(2); //ByteBuffer converts byte to char

  /**
   * Constructor
   * 
   * Sets up controllers and configures
   * left and right PID Loops
   */
  public Drive() {

    //LIDAR CREATION
    m_port = (byte) I2C.Port.kOnboard.value;
    I2CJNI.i2CInitialize(m_port);

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
    leftTalon.config_kF(0, leftDriveGains.kF, kTimeoutMs);
    leftTalon.config_kP(0, leftDriveGains.kP, kTimeoutMs);
    leftTalon.config_kI(0, leftDriveGains.kI, kTimeoutMs);
    leftTalon.config_kD(0, leftDriveGains.kD, kTimeoutMs);

    leftFollower.follow(leftTalon);

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

    rightFollower.follow(rightTalon);
    
    //Ramp init 
    throttleRamp = new SlewRateLimiter(kThrottleSlewRate);
    turnRamp = new SlewRateLimiter(kTurnSlewRate);
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

    //call the LIDAR methods (continuous)
    displayLIDARdata();

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

  public void displayLIDARdata() {
    startMeasuring();
    short displayedShort = readShort(0x8f); //distance in centimeters
    double LIDARdistance = displayedShort / 2.54 / 12; //convert from centimeters to feet

    SmartDashboard.putNumber("LIDAR distance", LIDARdistance);
  }

  /**
   * Starts LIDAR measuring.
   */
  public void startMeasuring() {
    writeRegister(0x04, 0x08); //starts measuring with aquisition mode control
    writeRegister(0x11, 0xff); //the number of times the device will retrigger, set to free-run
    writeRegister(0x00, 0x04); //enable reciever bias correction
  }

  /*
  HELPER METHODS
  */

  /**
   * Called by the startMeasuring method above to adjust the values/set up the machine.
   * 
   * @param address The type of control/task?
   * @param value The input to the address.
   * @return ??
   */
  private int writeRegister(int address, int value) {
    m_buffer.put(0, (byte) address); //0 and 1 are index values for ByteBuffer
    m_buffer.put(1, (byte) value);

    return I2CJNI.i2CWrite(m_port, k_deviceAddress, m_buffer, (byte) 2);
  }

  /**
   * Called by the displayLIDARdata method above to get the distance.
   * 
   * @param address The type of control/task?
   * @return Distance in centimeters (stored in a 16-bit short).
   */
  private short readShort(int address) {
    m_buffer.put(0, (byte) address);
    I2CJNI.i2CWrite(m_port, k_deviceAddress, m_buffer, (byte) 1);
    I2CJNI.i2CRead(m_port, k_deviceAddress, m_buffer, (byte) 2);

    return m_buffer.getShort(0);
  }
}
