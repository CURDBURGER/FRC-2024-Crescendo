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
int currentLedBlue = 0;
int currentLedRed = 20;
int currentLedOrange = 40;
int currentLedAlliance = 0;
int currentLedClear = 60;
unsigned long lastChange = 0;

void loop() {
  blackButtonState = digitalRead(blackButtonPin);
  yellowButtonState = digitalRead(yellowButtonPin);

  delay(33);
  if (lastChange < millis() + 1000) {
    currentLedBlue++;
    currentLedRed++;
    currentLedOrange++;
    currentLedAlliance++;
    currentLedClear++;
    lastChange = millis();

  if (currentLedBlue >= NUM_LEDS) currentLedBlue = 0;
  if (currentLedRed >= NUM_LEDS) currentLedRed = 0;
  if (currentLedOrange >= NUM_LEDS) currentLedOrange = 0;
  if (currentLedAlliance >= NUM_LEDS * 2) currentLedAlliance = 0;
  if (currentLedClear >= NUM_LEDS * 2) currentLedClear = 0;

  if (yellowButtonState == HIGH) {
  // read the state of the pushbutton value:
    // turn LED to orange no matter what the blackButtonState variable is
    strip.setPixelColor(currentLedBlue, 0, 0, 255);
    strip.setPixelColor(currentLedRed, 225, 0, 0);
    strip.setPixelColor(currentLedOrange, 225, 25, 0);
    strip.show();
  


  } else {
    // check if the pushbutton is pressed. If it is, the buttonState is HIGH:
    if (blackButtonState == HIGH) {
      // turn LED to blue:
      strip.setPixelColor(currentLedAlliance, 0, 0, 255);
      strip.setPixelColor(currentLedClear, 0, 0, 0);
      strip.show();
    } else {
        // turn LED to red
      strip.setPixelColor(currentLedAlliance, 225, 0, 0);
      strip.setPixelColor(currentLedClear, 0, 0, 0);
      strip.show();
    }
  }
}
}







