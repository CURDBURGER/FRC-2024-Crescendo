#include <Adafruit_NeoPixel.h>
#define PIN 6
#define PIN 9
#define Pin 12
#define NUM_LEDS 60
// Parameter 1 = number of pixels in strip
// Parameter 2 = pin number (most are valid)
// Parameter 3 = pixel type flags, add together as needed:
//   NEO_KHZ800  800 KHz bitstream (most NeoPixel products w/WS2812 LEDs)
//   NEO_KHZ400  400 KHz (classic 'v1' (not v2) FLORA pixels, WS2811 drivers)
//   NEO_GRB     Pixels are wired for GRB bitstream (most NeoPixel products)
//   NEO_RGB     Pixels are wired for RGB bitstream (v1 FLORA pixels, not v2)
const int blackButtonPin = 9;  // the number of the pushbutton pin
const int yellowButtonPin = 12;
const int ledPin = 6;  // the number of the LED pin
Adafruit_NeoPixel strip = Adafruit_NeoPixel(NUM_LEDS, ledPin, NEO_GRB + NEO_KHZ800);
// variables will change:
int blackButtonState = 0;   // variable for reading the pushbutton status
int yellowButtonState = 0;  // variable for the yellow button

void setup() {
  strip.begin();
  strip.show();  // Initialize all pixels to 'off'
  strip.setBrightness(134);
  // initialize the LED pin as an output:
  pinMode(ledPin, OUTPUT);
  // initialize the pushbutton pin as an input:
  pinMode(blackButtonPin, INPUT);
  pinMode(ledPin, OUTPUT);
  // initialize the pushbutton pin as an input:
  pinMode(yellowButtonPin, INPUT);
  
}
//start positions of each light
int currentLed = 0;
int currentLedClear = 30;
int currentLedAlliance = 60;
int c
unsigned long lastChange = 0;

void loop() {
  //sets variable to the state of its respective pin
  blackButtonState = digitalRead(blackButtonPin);
  yellowButtonState = digitalRead(yellowButtonPin);

  delay(33);
  //changes position of lights by 1 every second
  if (lastChange < millis() + 1000) {
    currentLed++;
    currentLedClear++;
    currentLedAlliance++;
    lastChange = millis();

  //resets lights to 0 if past a certain value
  if (currentLed >= NUM_LEDS) currentLed = 0;
  if (currentLedClear >= NUM_LEDS) currentLedClear = 0;
  if (currentLedAlliance >= NUM_LEDS * 2) currentLedAlliance = 0;

  if (yellowButtonState == HIGH) {
  // read the state of the pushbutton value:
    // diplays the orange red and blue leds no matter what the blackButtonState variable is
    strip.setPixelColor(29-currentLed, 225, 25, 0);
    strip.setPixelColor(30+currentLed, 225, 25, 0);
    strip.setPixelColor(29-currentLedClear, 0, 0, 0);
    strip.setPixelColor(30+currentLedClear, 0, 0, 0);
    strip.show();
  


  } else {
    // check if the pushbutton is pressed. If it is, the buttonState is HIGH:
    if (blackButtonState == HIGH) {
      // display the alliance led as blue and the clear led
      strip.setPixelColor(currentLedAlliance, 0, 0, 255);
      strip.setPixelColor(currentLedClearAlliance, 0, 0, 0);
      strip.show();
    } else {
        // display the alliance led as red and the clear led
      strip.setPixelColor(currentLedAlliance, 225, 0, 0);
      strip.setPixelColor(currentLedClearAlliance, 0, 0, 0);
      strip.show();
    }
  }
}
}







