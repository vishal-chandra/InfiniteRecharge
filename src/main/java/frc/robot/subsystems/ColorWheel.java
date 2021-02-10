/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

//WPI
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.util.Color;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
//CTRE
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

//REV
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorSensorV3;
import com.revrobotics.ColorMatchResult;

//3205
import static frc.robot.Constants.*;

public class ColorWheel extends SubsystemBase {
  
  TalonSRX colorWheelTalon; //motor

  //this switch is responsible for telling us if we're @ the wheel
  DigitalInput positionSwitch;

  ColorSensorV3 colorSensor; //reads color
  ColorMatch colorMatcher;
  ColorMatchResult matchResult;

  //stores the color displayed by the DriverStation
  public char sensorColorCommand = ' ';
  
  //colors on the wheel
  Color redTarget = ColorMatch.makeColor(0.53, 0.34, 0.13);
  Color blueTarget = ColorMatch.makeColor(0.12, 0.43, 0.45);
  Color greenTarget = ColorMatch.makeColor(0.17, 0.58, 0.25);
  Color yellowTarget = ColorMatch.makeColor(0.31, 0.57, 0.12);

  public ColorWheel() {
    //init hardware
    colorWheelTalon = new TalonSRX(kColorWheelTalonID);
    colorWheelTalon.configFactoryDefault();
    colorWheelTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
    colorWheelTalon.setSensorPhase(false);

    positionSwitch = new DigitalInput(kColorWheelSwitchPort);

    colorSensor = new ColorSensorV3(Port.kOnboard);

    //init matching
    colorMatcher = new ColorMatch();
    colorMatcher.addColorMatch(redTarget);
    colorMatcher.addColorMatch(blueTarget);
    colorMatcher.addColorMatch(greenTarget);
    colorMatcher.addColorMatch(yellowTarget);
  }

  /**
   * gets the color command from the Dashboard
   * see https://docs.wpilib.org/en/latest/docs/software/wpilib-overview/2020-Game-Data.html
   */
  public void getColorCommand() {
    String fieldColor = "G"; //DriverStation.getInstance().getGameSpecificMessage();

    if(fieldColor.length() > 0) {
      switch(fieldColor.charAt(0)) {
        case 'R':
          sensorColorCommand = 'B';
          break;
        case 'G':
          sensorColorCommand = 'Y';
          break;
        case 'B':
          sensorColorCommand = 'R';
          break;
        case 'Y':
          sensorColorCommand = 'G';
          break;
      }
    }
  }

  public char readColor() {
    ColorMatchResult match = colorMatcher.matchClosestColor(colorSensor.getColor());
    if(match.color == redTarget) return 'R';
    else if(match.color == blueTarget) return 'B';
    else if(match.color == greenTarget) return 'G';
    else if(match.color == yellowTarget) return 'Y';
    else return ' ';
  }

  public void startMotor() {
    if(positionSwitch.get()) colorWheelTalon.set(ControlMode.PercentOutput, 0.2);
  }

  public void stopMotor() {
    colorWheelTalon.set(ControlMode.PercentOutput, 0);
  }

  public boolean getSwitch() {
    return positionSwitch.get(); //the swtich needs to be flipped -> !
                                 //the switch was removed and is reading true ->!
                                 //overal, no !
  }
}
