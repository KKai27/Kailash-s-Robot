package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ShooterConstants;

import com.revrobotics.SparkMaxAbsoluteEncoder.Type;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;

import com.revrobotics.AbsoluteEncoder;


public class Flywheel extends SubsystemBase {
    private CANSparkMax flywheelMotor1;
    private CANSparkMax flywheelMotor2;
    private PIDController flywheelController;
    private AbsoluteEncoder flywheelEncoder;
    private SimpleMotorFeedforward feedforward;

    public Flywheel() {
        flywheelMotor1 = new CANSparkMax(11, CANSparkMaxLowLevel.MotorType.kBrushless);
        flywheelMotor2 = new CANSparkMax(12, CANSparkMaxLowLevel.MotorType.kBrushless);
        flywheelMotor2.follow(flywheelMotor1, true);
        flywheelController = new PIDController(ShooterConstants.kSparkMaxP, 0, 0); //tune
        flywheelEncoder = flywheelMotor1.getAbsoluteEncoder(Type.kDutyCycle);
        feedforward = new SimpleMotorFeedforward(ShooterConstants.kSparkMaxFeedforward, 0);

    }

    public void setMotorSpeed(double velocity) {
        flywheelController.setSetpoint(velocity);
    }


    @Override
    public void periodic() {
        flywheelMotor1.setVoltage(flywheelController.calculate(flywheelEncoder.getPosition()) + feedforward.calculate(ShooterConstants.kSparkMaxFeedforward));
    }
    
    @Override
    public void simulationPeriodic() {
        // This method will be called once per scheduler run during simulation
    }
}
