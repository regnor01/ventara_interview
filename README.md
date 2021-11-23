# ventara_interview
This is a basic simulation of a card game tournament, where participants are randomly assigned what place (1-8) they are given for a game, and there are seven games processed per simulation. Currently, it uses the JOption pane as a GUI, and does support changing the number of simulations and players. 

UPDATE: The user can change all variables related to the game. This includes point distribution, number of players, number of rounds per simulation, number of players per individual game, number of players who can advance to stage 2 and number of simulations. Negative points can be declared, which will act as a penalty for scoring certain places.

KNOWN ISSUES: The program does not give information to user about the values of the default players, simulations etc. as the text boxes are too small to render them in a user-friendly way. The program cannot handle a situation where a game requires more players than are in the tournament, and will ask the user to resubmit their value. Similarly, the game cannot handle more players advancing to the next round than players in the tournament.
