package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.SparkMaxAbsoluteEncoder.Type;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.ShooterPreset;
import frc.robot.Constants.ShooterConstants;

public class Shooter extends SubsystemBase {
  private CANSparkMax kicker;
  private CANSparkMax pivot;
  private AbsoluteEncoder pivotEncoder;
  private RelativeEncoder flywheelEncoder;
  private CANSparkMax topFlywheel;
  private CANSparkMax bottomFlywheel;
  private PIDController pivotController;
  private PIDController flywheelController;

   /** Creates a new Shooter. */
  public Shooter() {
     kicker = new CANSparkMax(ShooterConstants.kKickerMotorCanId, CANSparkMaxLowLevel.MotorType.kBrushless);
     kicker.enableVoltageCompensation(ShooterConstants.kKickerNominalVoltage);
     pivot = new CANSparkMax(ShooterConstants.kPivotMotorCanId, CANSparkMaxLowLevel.MotorType.kBrushless);
     topFlywheel = new CANSparkMax(ShooterConstants.kTopFlywheelMotorCanId, CANSparkMaxLowLevel.MotorType.kBrushless);
     bottomFlywheel = new CANSparkMax(ShooterConstants.kBottomFlywheelMotorCanId, CANSparkMaxLowLevel.MotorType.kBrushless);
     bottomFlywheel.follow(topFlywheel, true);

     pivotEncoder = pivot.getAbsoluteEncoder(Type.kDutyCycle);
     pivotEncoder.setPositionConversionFactor(ShooterConstants.kPivotPositionConversionFactor);
     pivotController = new PIDController(ShooterConstants.kPivotP, 0, 0);
     flywheelEncoder = topFlywheel.getEncoder();
     flywheelEncoder.setVelocityConversionFactor(ShooterConstants.kFlywheelVelocityConversionFactor);
     flywheelController = new PIDController(0, 0, 0);

     kicker.setIdleMode(IdleMode.kCoast);
     pivot.setIdleMode(IdleMode.kBrake);
     topFlywheel.setIdleMode(IdleMode.kCoast);
     bottomFlywheel.setIdleMode(IdleMode.kCoast);
  }

  public void setPivotTarget(double degrees) {
    pivotController.setSetpoint(Units.degreesToRadians(degrees));
  }

  public double getPivotTarget() {
    return pivotController.getSetpoint();
  }

  public void setFlywheelTarget(double rpm) {
    flywheelController.setSetpoint(Units.rotationsPerMinuteToRadiansPerSecond(rpm));
  }

  public double getFlywheelTarget() {
    return pivotController.getSetpoint();
  }

  public double getPivotAngleRadians() {
    return pivotEncoder.getPosition() / ShooterConstants.kPivotGearRatio;
  }

  public double getFlywheelVelocity() {
    return flywheelEncoder.getVelocity() / 1.0;
  }

  public void setKickerIntake(double power) {
    kicker.setVoltage(power * ShooterConstants.kKickerNominalVoltage);
  }

  private void setKickerOuttake(double outtakeSpeed) {
    kicker.set(outtakeSpeed);
  }

  public Command intakeSequence() {
    return new ParallelCommandGroup(
      new InstantCommand(() -> setKickerIntake(ShooterConstants.kKickerIntakeMotorSpeed)),
      setPreset(ShooterConstants.kIntakePreset));
  }

  public Command outtakeSequence() {
    return new SequentialCommandGroup(
      setPreset(ShooterConstants.kOuttakePreset),
      new InstantCommand(() -> setKickerOuttake(ShooterConstants.kKickerOuttakeMotorSpeed)));
  }

  public Command setPreset(ShooterPreset shooterPreset) {
    return new SequentialCommandGroup(
      new InstantCommand(() -> {
        setFlywheelTarget(shooterPreset.FlywheelRPM);
        setPivotTarget(shooterPreset.PivotDegrees);
      })
    );
  }

  @Override
  public void periodic() {
    pivot.setVoltage(pivotController.calculate(getPivotAngleRadians()));
    topFlywheel.setVoltage(flywheelController.calculate(getFlywheelVelocity()));
  }
}
