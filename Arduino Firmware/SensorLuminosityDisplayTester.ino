/* Photocell simple testing sketch. 
 
Connect one end of the photocell to 5V, the other end to Analog 0.
Then connect one end of a 10K resistor from Analog 0 to ground 
Connect LED from pin 11 through a resistor to ground 
For more information see http://learn.adafruit.com/photocells */

//#include <U8g2lib.h>
//#include <SPI.h>
//#include <Wire.h>

//#include <Adafruit_GFX.h>
#include <Adafruit_SSD1306.h>

Adafruit_SSD1306 display(4);
 
int photocellPin1 = 0;     // the cell and 10K pulldown are connected to a0
int photocellPin2 = 1;     // the cell and 10K pulldown are connected to a0
int photocellPin3 = 2;     // the cell and 10K pulldown are connected to a0

int photocellReading1;     // the analog reading from the sensor divider
int photocellReading2;     // the analog reading from the sensor divider
int photocellReading3;     // the analog reading from the sensor divider



void printLabel(boolean doClear, String txt, int value, int y)
{
  //if (doClear == true) display.clearDisplay();
  display.setCursor(0, y);
  String thisString = String(value);
  display.setTextSize(1);
  display.setTextColor(WHITE);
  display.print(thisString); Serial.print(thisString);
  display.print(txt);        Serial.println(txt);
  
}



void setup(void) {
  // We'll send debugging information via the Serial monitor
  Serial.begin(115200);   
  

  display.begin(SSD1306_SWITCHCAPVCC, 0x3C);  // initialize with the I2C addr 0x3C (for the 128x32)
  // Clear the buffer.
  display.clearDisplay();

}


 
void loop(void) {
  photocellReading1 = analogRead(photocellPin1);  
  photocellReading2 = analogRead(photocellPin2);  
  photocellReading3 = analogRead(photocellPin3);    
 
 // Serial.print("Analog reading = ");
//  Serial.print(photocellReading1); Serial.print(' ');    // the raw analog reading
//  Serial.print(photocellReading2); Serial.print(' ');    // the raw analog reading
//  Serial.println(photocellReading3);     // the raw analog reading  

//Serial.println(photocellReading1);
display.clearDisplay();
printLabel(true," lum. sens.1 ",photocellReading1,0);
printLabel(true," lum. sens.2 ",photocellReading2,10);
printLabel(true," lum. sens.3 ",photocellReading3,20);
display.display();


 /*  String str[10] = "a";//(String)photocellReading1;
   u8g2.clearBuffer();          // clear the internal memory
   u8g2.setFont(u8g2_font_logisoso28_tr);  // choose a suitable font at https://github.com/olikraus/u8g2/wiki/fntlistall
  // u8g2.drawStr(0,0,str);  // write something to the internal memory
 
 u8g2.drawStr(8,29,"MYBOTIC");  // write something to the internal memory
   u8g2.sendBuffer();         // transfer internal memory to the display
   */
//Serial.print(" ");
//Serial.println(photocellReading2);
//Serial.print(" ");
//Serial.println(photocellReading3);
//Serial.print(" ");
 
  // LED gets brighter the darker it is at the sensor
  // that means we have to -invert- the reading from 0-1023 back to 1023-0
//  photocellReading = 1023 - photocellReading;
  //now we have to map 0-1023 to 0-255 since thats the range analogWrite uses
//  LEDbrightness = map(photocellReading, 0, 1023, 0, 255);
//  analogWrite(LEDpin, LEDbrightness);
 
  delay(100);


 
}
