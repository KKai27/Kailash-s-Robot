package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxAbsoluteEncoder.Type;
import com.revrobotics.AbsoluteEncoder;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ShooterConstants;

public class Shooter extends SubsystemBase {
  private CANSparkMax pivot;
  private CANSparkMax kicker;
  private CANSparkMax topFlywheel;
  private CANSparkMax bottomFlywheel;
  private PIDController pivotController;
  private PIDController flywheelController;
  private RelativeEncoder flywheelEncoder;
  private AbsoluteEncoder pivotEncoder;


   /** Creates a new Shooter. */
  public Shooter() {
     pivot = new CANSparkMax(ShooterConstants.kPivotMotorCanId, CANSparkMaxLowLevel.MotorType.kBrushless);
     kicker = new CANSparkMax(ShooterConstants.kKickerMotorCanId, CANSparkMaxLowLevel.MotorType.kBrushless);
     topFlywheel = new CANSparkMax(11, CANSparkMaxLowLevel.MotorType.kBrushless);
     bottomFlywheel = new CANSparkMax(12, CANSparkMaxLowLevel.MotorType.kBrushless);
     bottomFlywheel.follow(topFlywheel, true);
     pivotController = new PIDController(ShooterConstants.kSparkMaxP, 0, 0);
     flywheelController = new PIDController(ShooterConstants.kSparkMaxP, 0, 0);
     flywheelEncoder = topFlywheel.getEncoder();
     pivotEncoder = pivot.getAbsoluteEncoder(Type.kDutyCycle);
     pivotEncoder.setPositionConversionFactor(360 * ShooterConstants.kPivotGearRatio);
  }

  public void setPivot(double degrees) {
    pivotController.setSetpoint(Units.degreesToRadians(degrees));
  }                                                                           

  public void setFlywheel(double velocity) {
    flywheelController.setSetpoint(velocity);
  }

  public double getPivot() {
    return pivotEncoder.getPosition() / ShooterConstants.kPivotGearRatio;
  }

  public void intake() {
    setFlywheel(1);
    setPivot(1);
    kicker.setVoltage(2);
  }

  public void outtake() {
    setFlywheel(-1);
    setPivot(-1);
    kicker.setVoltage(-2);
  }
  
  @Override
  public void periodic() {
    pivot.setVoltage(pivotController.calculate(getPivot()));
    topFlywheel.setVoltage(flywheelController.calculate(flywheelEncoder.getVelocity()));
    
  }
      
  @Override
  public void simulationPeriodic() {
      // This method will be called once per scheduler run during simulation
  }

}
