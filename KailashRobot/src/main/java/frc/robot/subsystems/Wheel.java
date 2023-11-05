// package frc.robot.subsystems;
// import com.revrobotics.CANSparkMax;
// import com.revrobotics.CANSparkMaxLowLevel;
// import com.revrobotics.CANSparkMax.IdleMode;
// import edu.wpi.first.wpilibj2.command.SubsystemBase;

// public class Wheel extends SubsystemBase {
//     private CANSparkMax leftWheel; 
//     private CANSparkMax rightWheel;
//     public Wheel() {
//         leftWheel = new CANSparkMax(11, CANSparkMaxLowLevel.MotorType.kBrushless);
//         rightWheel = new CANSparkMax(12, CANSparkMaxLowLevel.MotorType.kBrushless);
//         rightWheel.follow(leftWheel, true);
//         leftWheel.setIdleMode(IdleMode.kCoast);
//         rightWheel.setIdleMode(IdleMode.kCoast);
        
//     }

//     public void moveForward(){
//         leftWheel.set(1);
//     }
//     public void moveBackward() {
//         leftWheel.set(-1);
//     }
//     public void stop() {
//         leftWheel.set(0);
//     }
    
//     @Override
//     public void periodic() {
//         // This method will be called once per scheduler run
//     }
    
//     @Override
//     public void simulationPeriodic() {
//         // This method will be called once per scheduler run during simulation
//     }

// }
