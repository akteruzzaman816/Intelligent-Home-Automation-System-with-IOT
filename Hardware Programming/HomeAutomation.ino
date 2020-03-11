#include <ESP8266WiFi.h>
#include<FirebaseArduino.h>
#define FIREBASE_HOST "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"                     
#define FIREBASE_AUTH "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"       
#define WIFI_SSID "Wifi_Name"                                               
#define WIFI_PASSWORD "wifi_password" 
const int sensor =A0;


  #define pinNumber0 D0 
  #define pinNumber1 D1 
  #define pinNumber2 D2  
  #define pinNumber3 D3  
  #define pinNumber4 D4
  #define pinNumber5 D5 
  #define pinNumber6 D6  
  #define pinNumber7 D7
  #define pinNumber8 D8

  int value0; 
  int value1; 
  int value2; 
  int value3;
  int value4; 
  int value5; 
  int value6;
  int value7;
  int value8;
  int mapValue;
  

void setup() 
{
  
  Serial.begin(74880); 
                                                    
  pinMode(pinNumber0,OUTPUT);
  pinMode(pinNumber1,OUTPUT);
  pinMode(pinNumber2,OUTPUT);
  pinMode(pinNumber3,OUTPUT);
  pinMode(pinNumber4,OUTPUT);
  pinMode(pinNumber5,OUTPUT);
  pinMode(pinNumber6,OUTPUT);
  pinMode(pinNumber7,OUTPUT);
  pinMode(pinNumber8,OUTPUT);

  pinMode(sensor,INPUT);



  WiFi.begin(WIFI_SSID,WIFI_PASSWORD);
  Serial.print("connecting");
  while (WiFi.status()!=WL_CONNECTED){
    Serial.print(".");
    delay(500);
  }
  
  Serial.println();
  Serial.print("connected:");
  Serial.println(WiFi.localIP());

  Firebase.begin(FIREBASE_HOST,FIREBASE_AUTH);
  
  }

  
  void firebasereconnect()
  {
    Serial.println("Trying to reconnect");
      Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  }
  

  void loop() 
  {

    //check firebase connection
    if (Firebase.failed())
        {
        Serial.print("setting number failed:");
        Serial.println(Firebase.error());
        firebasereconnect();
        return;
        }

        //for getting and send server  temperature data
        int analogValue = analogRead(sensor);
        float millivolts = (analogValue/1024.0) * 3300; //3300 is the voltage provided by NodeMCU
        float celsius = millivolts/10;
        Serial.print("in DegreeC=   ");
        Serial.println(celsius);
        
        //---------- Here is the calculation for Fahrenheit ----------//
        
        float fahrenheit = ((celsius * 9)/5 + 32);
        Serial.print(" in Farenheit=   ");
        Serial.println(fahrenheit);

        Firebase.setInt("/users/dC0GPGEnFAQY7dATjgebzi5WnPg2/temperature/fahrenheit",fahrenheit);    
        Firebase.setInt("/users/dC0GPGEnFAQY7dATjgebzi5WnPg2/temperature/celsius",celsius);


         
    //Event handle for  D0..........................
    value0=Firebase.getString("/users/dC0GPGEnFAQY7dATjgebzi5WnPg2/Buttons/D0/buttonState").toInt();  
    if(value0==0)                                                            
      {
        digitalWrite(pinNumber0,LOW);
      }
      else if(value0==1)                                                      
      {                                      
        digitalWrite(pinNumber0,HIGH);
      }


    //Event handle for  D1..........................
    value1=Firebase.getString("/users/dC0GPGEnFAQY7dATjgebzi5WnPg2/Buttons/D1/buttonState").toInt();  
    if(value1==0)                                                            
      {
        digitalWrite(pinNumber1,LOW);
      }
      else if(value1==1)                                                      
      {                                      
        digitalWrite(pinNumber1,HIGH);
      }


    //Event handle for  D2..........................
    value2=Firebase.getString("/users/dC0GPGEnFAQY7dATjgebzi5WnPg2/Buttons/D2/buttonState").toInt();  
    if(value2==0)                                                            
      {
        digitalWrite(pinNumber2,LOW);
      }
      else if(value2==1)                                                      
      {                                      
        digitalWrite(pinNumber2,HIGH);
      }


    //Event handle for  D3..........................
    value3=Firebase.getString("/users/dC0GPGEnFAQY7dATjgebzi5WnPg2/Buttons/D3/buttonState").toInt();  
    if(value3==0)                                                            
      {
        digitalWrite(pinNumber3,LOW);
      }
      else if(value3==1)                                                      
      {                                      
        digitalWrite(pinNumber3,HIGH);
      }


    //Event handle for  D4..........................
    value4=Firebase.getString("/users/dC0GPGEnFAQY7dATjgebzi5WnPg2/Buttons/D4/buttonState").toInt();  
    if(value4==0)                                                            
      {
        digitalWrite(pinNumber4,LOW);
      }
      else if(value4==1)                                                      
      {                                      
        digitalWrite(pinNumber4,HIGH);
      }

    //Event handle for  D5..........................
    value5=Firebase.getString("/users/dC0GPGEnFAQY7dATjgebzi5WnPg2/Buttons/D5/buttonState").toInt();
    mapValue=Firebase.getString("/users/dC0GPGEnFAQY7dATjgebzi5WnPg2/Locations/info/status").toInt();
    Serial.println(mapValue);
    //Serial.println(value5);
    if(mapValue==0)                                                            
      {
        digitalWrite(pinNumber5,LOW);
      }
      else if(mapValue==1)                                                      
      {                                      
        digitalWrite(pinNumber5,HIGH);
      } 


    //Event handle for  D6..........................
    value6=Firebase.getString("/users/dC0GPGEnFAQY7dATjgebzi5WnPg2/Buttons/D6/buttonState").toInt();  
    if(value6==0)                                                            
      {
        digitalWrite(pinNumber6,LOW);
      }
      else if(value6==1)                                                      
      {                                      
        digitalWrite(pinNumber6,HIGH);
      }           

  
    //Event handle for  D7..........................
    value7=Firebase.getString("/users/dC0GPGEnFAQY7dATjgebzi5WnPg2/Buttons/D7/buttonState").toInt();  
    if(value7==0)                                                            
      {
        digitalWrite(pinNumber7,LOW);
      }
      else if(value7==1)                                                      
      {                                      
        digitalWrite(pinNumber7,HIGH);
      }   


    //Event handle for  D8..........................
    value8=Firebase.getString("/users/dC0GPGEnFAQY7dATjgebzi5WnPg2/Buttons/D8/buttonState").toInt();  
    if(value8==0)                                                            
      {
        digitalWrite(pinNumber8,LOW);
      }
      else if(value8==1)                                                      
      {                                      
        digitalWrite(pinNumber8,HIGH);
      }           


  
     
  }
