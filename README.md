LcdScreenTest
=============

A small LCD screen testing program using jMonkeyEngine.

The purpose of this program is to implement a testing suite for displays,
notably LCD displays. A variety of tests are currently implemented by pressing
the appropriate number keys:

1: Solid Red
2: Solid Green
3: Solid Blue
4: "Seizure mode," which flips rapidly between the above three colors
5: Solid White
6: Solid Black
7: Moving checkerboard pattern
8: Frozen checkerboard pattern

Tests 1, 2, 3, 5 and 6 are most useful for finding dead or stuck pixels. Tests 4
and 7 are useful for visualizing the response time for a flatpanel display (more
gray might mean slower response time).

This program should be run at the native resolution with v-sync enabled.

JScreenFix has this to say in their FAQ secion:

  "Other applications and videos which turn the entire screen on and off rapidly
  cause a significant strain on the power circuitry and could damage your
  screen."

Whether this is true or not, you use this software at your own risk. Consult the
LICENSE file for more information.