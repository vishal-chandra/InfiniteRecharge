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

//CTRE
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

//REV
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorSensorV3;
import com.revrobotics.ColorMatchResult;

//3205
import static frc.robot.Constants.*;

public class ColorWheel extends SubsystemBase {
  
  TalonSRX colorWheelTalon;
  DigitalInput colorSwitch;

  ColorSensorV3 colorSensor;
  ColorMatch colorMatcher;
  ColorMatchResult matchResult;
  
  Color redTarget = ColorMatch.makeColor(0, 0, 0);
  Color blueTarget = ColorMatch.makeColor(0, 0, 0);
  Color greenTarget = ColorMatch.makeColor(0, 0, 0);
  Color yellowTarget = ColorMatch.makeColor(0, 0, 0);

  public ColorWheel() {

    //init hardware
    colorWheelTalon = new TalonSRX(kColorWheelTalonID);
    colorSwitch = new DigitalInput(kColorWheelSwitchPort);
    colorSensor = new ColorSensorV3(Port.kOnboard);

    //init matching
    colorMatcher = new ColorMatch();
    colorMatcher.addColorMatch(redTarget);
    colorMatcher.addColorMatch(blueTarget);
    colorMatcher.addColorMatch(greenTarget);
    colorMatcher.addColorMatch(yellowTarget);
  }

  public void colorControl() {

  }

  public void rotationControl() {

  }

  /**
   * gets the color command from the Dashboard
   * see https://docs.wpilib.org/en/latest/docs/software/wpilib-overview/2020-Game-Data.html
   */
  private char getColorCommand() {
    String fieldColor = DriverStation.getInstance().getGameSpecificMessage();
    char sensorColor = ' ';

    if(fieldColor.length() > 0) {
      switch(fieldColor.charAt(0)) {
        case 'R':
          sensorColor = 'B';
          break;
        case 'G':
          sensorColor = 'Y';
          break;
        case 'B':
          sensorColor = 'R';
          break;
        case 'Y':
          sensorColor = 'G';
          break;
        
      }
    }
    return sensorColor;
  }
}
