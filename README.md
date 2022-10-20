# CSCI-4448-Project 4.2  

## Team Members:  
Cesser Jackson  
Ricardo Gonzalez  

## Java Version:  
openjdk version "17.0.4" 2022-07-19  
OpenJDK Runtime Environment (build 17.0.4+8-Ubuntu-120.04)  
OpenJDK 64-Bit Server VM (build 17.0.4+8-Ubuntu-120.04, mixed mode, sharing)  

## Assumptions:  
- Search is available in all rooms that contain a treasure, but an Adventurer can still only hold one of each treasure. Because of this, searching a room that contains a treasure that's owned essentially wastes the turn.  
- The game currently ends immediately if the player chooses not to move on their first turn.  
- Runners will still take escape damage on their first movement if applicable. Their second movement in the same turn is guaranteed to avoid all escape damage.  

## RotLA UML Diagram update:  
![RotLA UML diagram v4.2](RotLA_UML_v4.2.png)  
command changes, factory changes, adventurer attributes trimmed, methods changed to fit new adventurer behavior (expand on this before turning in)