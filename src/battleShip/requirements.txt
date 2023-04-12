Requirements and Planning

10x10 grid playing board
A player has 2 grids in view: their defensive and offensive grid.

Ships are:
Carrier		5
Battleship	4
Destroyer	3
Submarine	3
Patrol Boat	2

Game Phases
1. Start Page
2. Placing ships (ends when both players have placed each ship)
3. Game play
	- p1 begins by selecting empty location on offensive grid
	- game returns "miss", "hit", or "sunk" and updates view
	- turn flips into p2 and repeat
	
4. End of Game (victory occurs for the player who has sunk all 5 of the opponent ships)

MVC


Things that the Grid could do before getting jobs split up
	Defensive grid
	- Store locations of defensive boats
	- Display defensive boats
	
	offensive grid
	- Store locations of offensive attacks
	- Display either empty, hit, sunk
	- Track location of each attack to prevent a repeat location

offensiveGrid[10][10] = ENUM 'empty' 'hit' 'miss'
boat[10][10] pointing to a boat object

offensive player chooses grid location (0,0)
	if model.isValidMove()
	{
		if boat[0][0] is empty
			offensiveGrid[0][0] = 'miss'
		else
			boat[0][0].hit();
			offensiveGrid[0][0] = 'hit'
	}

Boat Class:
A boat must track
	- length of boat
	- how many hits it has taken
	- sunk boolean
		make true when length = hits

