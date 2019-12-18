//**************************************************************************************
//* Funzione dei pulsanti
/// btnS1 Bottone frontale singolo (pin 4)   btn1 , endstop
/// btnS2 Bottone dietro sinistra (pin 2)    btn2 , UP both
/// btnS3 Bottone dietro destra (pin 3)      btn3 , DW both
//*
//**************************************************************************************


//#include <SPI.h>
#include <Trinamic_TMC2130.h>
#include <elapsedMillis.h>

// pin configuration

#define CS_PIN1 A1   //19  //A1
#define z1step 7
#define z1dir 6

#define CS_PIN2 A2         //20  //A2
#define z2step 10
#define z2dir 9
#define steppersEnable 8

#define z1Endstop 11
#define z2Endstop 12

#define ldrPin A0

#define btnS1 4   
#define btnS2 3
#define btnS3 2

//#define ledR 3
//#define ledG 5
//#define ledB 6

Trinamic_TMC2130 myStepper2(CS_PIN1);
Trinamic_TMC2130 myStepper1(CS_PIN2);

#define nMicrosteps 2

// Movimenti singoli motore 1
//----------------------------------------------------------------------
void stepDW1(int steps)
{  digitalWrite(z1dir, LOW); 
   for(int a=0; a<steps; a++) {//if (digitalRead(zMinSensor))
   { digitalWrite(z1step, HIGH);   
     delayMicroseconds(150);               
     digitalWrite(z1step, LOW); 
     delayMicroseconds(900);
     }}
}  

void stepUP1(int steps)
{  digitalWrite(z1dir, HIGH); 
   for(int a=0; a<steps; a++) {//if (digitalRead(zMinSensor))
   { digitalWrite(z1step, HIGH);   
     delayMicroseconds(150);               
     digitalWrite(z1step, LOW); 
     delayMicroseconds(900);
     }}
}  

// Movimenti singoli motore 2
//----------------------------------------------------------------------
void stepDW2(int steps)
{  digitalWrite(z2dir, LOW); 
   for(int a=0; a<steps; a++) {//if (digitalRead(zMinSensor))
   { digitalWrite(z2step, HIGH);   
     delayMicroseconds(150);               
     digitalWrite(z2step, LOW); 
     delayMicroseconds(900);
     }}
}  

void stepUP2(int steps)
{  digitalWrite(z2dir, HIGH); 
   for(int a=0; a<steps; a++) {//if (digitalRead(zMinSensor))
   { digitalWrite(z2step, HIGH);   
     delayMicroseconds(150);               
     digitalWrite(z2step, LOW); 
     delayMicroseconds(900);
     }}
   }  

// Movimenti combinati motore 1 e 2 insieme
//----------------------------------------------------------------------
void stepDWboth(int steps)
{  digitalWrite(z2dir, HIGH); 
   digitalWrite(z1dir, HIGH); 
   for(int a=0; a<steps; a++) {//if (digitalRead(zMinSensor))
   { digitalWrite(z1step, HIGH);   digitalWrite(z2step, HIGH);   
     delayMicroseconds(150);               
     digitalWrite(z1step, LOW); digitalWrite(z2step, LOW); 
     delayMicroseconds(900);
     }}
}  

void stepUPboth(int steps)
{  digitalWrite(z2dir, LOW); 
   digitalWrite(z1dir, LOW); 
   for(int a=0; a<steps; a++) 
   { if ( (digitalRead(z1Endstop)==LOW) && (digitalRead(z2Endstop)==LOW) )
     { digitalWrite(z1step, HIGH);   digitalWrite(z2step, HIGH);   
     delayMicroseconds(150);               
     digitalWrite(z1step, LOW); digitalWrite(z2step, LOW); 
     delayMicroseconds(900);
     }
   }
}  



void stepToEndstop()
{  digitalWrite(z2dir, LOW); 
   digitalWrite(z1dir, LOW); 
   // Prima insieme
   while ( (digitalRead(z1Endstop)==LOW) && (digitalRead(z2Endstop)==LOW) )
   { digitalWrite(z1step, HIGH);   digitalWrite(z2step, HIGH);   
     delayMicroseconds(150);               
     digitalWrite(z1step, LOW); digitalWrite(z2step, LOW); 
     delayMicroseconds(900);
     }  delay(500); 
// Poi livella singolarmente: motore 1
   while (digitalRead(z1Endstop)==LOW)
   { digitalWrite(z1step, HIGH);     delayMicroseconds(150);               
     digitalWrite(z1step, LOW);      delayMicroseconds(900);
     }  delay(500);        
//Poi livella singolarmente: motore 2
   while (digitalRead(z2Endstop)==LOW)
   { digitalWrite(z2step, HIGH);     delayMicroseconds(150);               
     digitalWrite(z2step, LOW);      delayMicroseconds(900);
     }   delay(500);     
// colpetto finale a entrambi di 1mm su
   digitalWrite(z2dir, LOW); 
   digitalWrite(z1dir, LOW); 
   for(int a=0; a<200; a++) 
   { digitalWrite(z1step, HIGH);   digitalWrite(z2step, HIGH);   
     delayMicroseconds(150);               
     digitalWrite(z1step, LOW); digitalWrite(z2step, LOW); 
     delayMicroseconds(900);
    } 

    
} 

void stepToEndstopEgo()
{  stepToEndstop();
// ora giu' pronto a stampare
  stepDWboth(16250);
    
}  
   

// Funzioni di stampa 3d
//----------------------------------------------------------------------

// passo elica leadscrew 3d printed:  2 mm = 1 giro
// stepper da 0.9° = 400 step / giro
// con microstep a 2,  800 step per fare 2mm
//                     80 step per fare 0.2mm
//                     40 step per 0.1mm

void zLayerUp() // su di 1 layer con stacco, stampa a 100um
{ //  su di 10 (ex 5) mm
  stepUPboth(4000);
  delay(500); 
  //// giù di 4mm meno 0.1mm
  // giù di 4mm meno 0.05mm
  stepDWboth(4000-20);             
}  


void zLayerUpShort() // su di 1 layer con stacco, stampa a 100um
{ //  su di 10 (ex 5) mm
  stepUPboth(1000);
  delay(200); 
  //// giù di 4mm meno 0.1mm
  // giù di 4mm meno 0.05mm
  stepDWboth(1000-20);             
}  


void blinks(int times)
{ for (int i=0; i<times; i++)
  { digitalWrite(13, HIGH);
    delay(150); 
   digitalWrite(13, LOW);
    delay(150);
  }
}

void setup() {
    // inizializzazione stepper motors per pompa e asse Z


 pinMode(CS_PIN1, OUTPUT);
 pinMode(CS_PIN2, OUTPUT); 

  pinMode(13, OUTPUT); 
 
   pinMode(z1step, OUTPUT);
   pinMode(z1dir, OUTPUT);      
   pinMode(z2step, OUTPUT);
   pinMode(z2dir, OUTPUT);      
 pinMode(steppersEnable, OUTPUT); 

   pinMode(z1Endstop, INPUT);      
   pinMode(z2Endstop, INPUT);         
    
   pinMode(btnS1, INPUT);     
   pinMode(btnS1, INPUT); 
   pinMode(btnS3, INPUT);   

     digitalWrite(steppersEnable, LOW); 

 // stepper 1
  myStepper1.init();
  myStepper1.set_mres(nMicrosteps); // ({1,2,4,8,16,32,64,128,256}) number of microsteps
  myStepper1.set_IHOLD_IRUN(12,12,5); // ([0-31],[0-31],[0-5]) sets all currents to maximum
  myStepper1.set_I_scale_analog(1); // ({0,1}) 0: I_REF internal, 1: sets I_REF to AIN
  myStepper1.set_tbl(1); // ([0-3]) set comparator blank time to 16, 24, 36 or 54 clocks, 1 or 2 is recommended
  myStepper1.set_toff(8); // ([0-15]) 0: driver disable, 1: use only with TBL>2, 2-15: off time setting during slow decay phase

// stepper 2
  myStepper2.init();
  myStepper2.set_mres(nMicrosteps); // ({1,2,4,8,16,32,64,128,256}) number of microsteps
  myStepper2.set_IHOLD_IRUN(12,12,5); // ([0-31],[0-31],[0-5]) sets all currents to maximum
  myStepper2.set_I_scale_analog(1); // ({0,1}) 0: I_REF internal, 1: sets I_REF to AIN
  myStepper2.set_tbl(1); // ([0-3]) set comparator blank time to 16, 24, 36 or 54 clocks, 1 or 2 is recommended
  myStepper2.set_toff(8); // ([0-15]) 0: driver disable, 1: use only with TBL>2, 2-15: off time setting during slow decay phase  
       

//stepDW1(200);
//stepDW2(200);
//stepUP1(200);
//stepUP2(200);

   
    Serial.begin(9600);    
    
}

void loop() {
   // per dubug mode
   int incomingByte = 0; // for incoming serial data
   if (Serial.available() > 0)
     { incomingByte = Serial.read();
         if (incomingByte=='a') // test layer 50um
          { for (int k=0; k<50; k++) zLayerUpShort(); } // 0.05 x 50 = 2.5mm
     }


 // Serial.println(analogRead(ldrPin));

  elapsedMillis timeElapsed;
  boolean isoff=false;
  boolean isfin=false;
  if (analogRead(ldrPin)>850)
  { 
    
    /////delay(250);
    timeElapsed = 0;  
    do 
    { if (analogRead(ldrPin)<300) isoff=true;
      if ((isoff==true)&&(analogRead(ldrPin)>850)) { isfin=true; stepToEndstop(); blinks(2); }
    } while (timeElapsed < 300);
    
    if (isfin==false) { zLayerUp();  blinks(4); }


    
  }

   if (digitalRead(z1Endstop)==HIGH) {Serial.println("Endstop 1 high"); delay(50);}// else {Serial.println("Endsotp low"); delay(50);}
   if (digitalRead(z2Endstop)==HIGH) {Serial.println("Endstop 2 high"); delay(50);}// else {Serial.println("Endsotp low"); delay(50);}   
   if (digitalRead(btnS2)==HIGH) {blinks(1);  Serial.println("btns2 , UP both"); delay(150); stepUPboth(200); } //else {Serial.println("btn1 low"); delay(50);}
   if (digitalRead(btnS3)==HIGH) {blinks(2); Serial.println("btns3 , DW both"); delay(150); stepDWboth(200); } //
   if (digitalRead(btnS1)==HIGH) {blinks(3); Serial.println("btns1 to endstop"); delay(150); Serial.println(analogRead(ldrPin)); stepToEndstopEgo(); } //   

   
}
