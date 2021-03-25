package frc.robot.custom;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.SpeedController;

import static frc.robot.Constants.*;

/**
 * A DifferentialDrive compatible TalonSRX Wrapper allowing velocity control
 */
public class WPI_VeloTalon extends TalonSRX implements SpeedController {

    public WPI_VeloTalon(int deviceNumber, Gains gains) {
        super(deviceNumber);
        configFactoryDefault();
        configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, kTimeoutMs);
        //setSensorPhase(false); //for sensor alignments

        //set gains
        config_kF(0, gains.kF, kTimeoutMs);
        config_kP(0, gains.kP, kTimeoutMs);
        config_kI(0, gains.kI, kTimeoutMs);
        config_kD(0, gains.kD, kTimeoutMs);
    }

    @Override //required implementation
    public void pidWrite(double output) {}

    @Override
    public void set(double pctCommand) {
        double targetVelocity = commandToTargetVelocity(pctCommand);
        super.set(ControlMode.Velocity, targetVelocity);
    }

    @Override
    public double get() {
        return getSelectedSensorVelocity(0);
    }

    @Override
    public void setInverted(boolean isInverted) {
        super.setInverted(isInverted);
    }

    @Override
    public boolean getInverted() {
        return super.getInverted();
    }

    @Override
    public void disable() {
        neutralOutput();
    }

    @Override
    public void stopMotor() {
        neutralOutput();
    }

    private double commandToTargetVelocity(double command) {
        //convert command to a percentage of maxRPM, 
        //then convert revolutions to units
        //and minutes to 100ms
        return command * MAX_RPM * 4096 / 600;
    }
}
