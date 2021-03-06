package GameOfLife

/*
Game of Life - a Groovy implementation of John H. Conway's Game of Life
Copyright (C) 2011 Adrian McMenamin

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*/

class LifeGame {
	/* Controls the game */
	def grid = []
	def gridWidth
	def gridHeight
	def widthRange
	def widthRangeEx
	def heightRange
	def heightRangeEx
	
	void run() {
		welcome()
		printGrid()
		setupBeasties()
		def i = 0
		while (true) {
			evolve(i)
			Thread.sleep(500)
			i++
		}
	}
	
	void welcome() {
		println "Welcome to the Game of Life"
		println "Based on John Conway's famous article for the Scientific American."
		println "To begin you must specify a grid size. Height and width are limited"
		println "to 40 x 40 max."
		println()
		println "Please specify the width and height of your grid in the format"
		println "width,height"
		getGridDimensions()
		
		(heightRangeEx).each {
			def row = []
			(widthRangeEx).each {row[it] = 0}
			grid.add(row)
		}
	}
	
	void setupBeasties() {
		println()
		println "You now need to specify the cells in which the automata live."
		println "Please enter in the format (x, y). Enter -1, -1 to end this phase."
		getBeastiePlaces()
		println "Game will now begin..."
	}
	
	void evolve(def generation)
	{
		println "Generation $generation"
		printGrid()
		def newGrid = []
		(heightRangeEx).each {
			def row = []
			def line = it
			(widthRangeEx).each {row[it] = liveOrDie(it, line)}
			newGrid.add(row)
		}
		grid = newGrid
		
	}
	
	def liveOrDie(def x, def y)
	{
		return score(-1, x, y, 0)
	}
	
	def score(def index, def x, def y, def mates)
	{
		if (mates > 3)
			return 0
		if (index > 1) {
			if (mates == 3)
				return 1
			else if (grid[y][x] == 1 && mates == 2)
				return 1
			else
				return 0
		}
		if (!((y + index) in heightRangeEx))
			return score(++index, x, y, mates)
		if ((x - 1) >= 0)
			mates += grid[y + index][x - 1]
		if (index != 0)
			mates += grid[y + index][x]
		if ((x + 1) < gridWidth)
			mates += grid[y + index][x + 1]
		return score(++index, x, y, mates)
	}
	
	
	void getBeastiePlaces()
	{
		String gridPoint = new Scanner(System.in).nextLine()
		def comma = ","
		def gridBits = gridPoint.tokenize(comma)
		if (gridBits.isEmpty()) {
			errorXY(gridPoint + " does not evaluate")
			getBeastiePlaces()
			return
		}
		if (gridBits[1] == null) {
			errorXY(gridPoint + " badly formed")
			getBeastiePlaces()
			return
		}
		if (!gridBits[0].isInteger() || !gridBits[1].isInteger()) {
			errorXY(gridPoint + " not numerical")
			getBeastiePlaces()
			return
		}
		def gridW = gridBits[0].toInteger()
		def gridH = gridBits[1].toInteger()
		if ((gridW == -1) && (gridH == -1))
			return
		if (!(gridW in widthRange)||!(gridH in heightRange)){
			errorXY(gridPoint + " out of range")
			getBeastiePlaces()
			return
		}
		grid[gridH - 1][gridW - 1] = 1;
		printGrid()
		println "Next place x,y... or -1,-1 to finish"
		getBeastiePlaces()
		return
	}
	
	void getGridDimensions()
	{
		String gridSize = new Scanner(System.in).nextLine()
		def comma = ","
		def gridBits = gridSize.tokenize(comma)
		if (gridBits.isEmpty()) {
			errorXY(gridSize + " does not evaluate")
			getGridDimensions()
			return
		}
		if (gridBits[1] == null) {
			errorXY(gridSize + " badly formed")
			getGridDimensions()
			return
		}
		if (!gridBits[0].isInteger() || !gridBits[1].isInteger()) {
			errorXY(gridSize + " not numerical")
			getGridDimensions()
			return
		}
		gridWidth = gridBits[0].toInteger()
		gridHeight = gridBits[1].toInteger()
		if (!(gridWidth in 1..40)||!(gridHeight in 1..40)) {
			errorXY(gridSize + "out of range")
			getGridDimensions()
			}
		heightRange = 1..gridHeight
		widthRange = 1..gridWidth
		heightRangeEx = 0..<gridHeight
		widthRangeEx = 0..<gridWidth
	}
	
	void errorXY(String strMsg)
	{
		println strMsg
	}
	
	void printGrid() {
		(0..gridWidth + 1).each {
			print "*"
		}
		println()
		(heightRangeEx).each {
			def line = it
			print "|"
			(widthRangeEx).each {
				if (grid[line][it] == 0)
					print " "
				else
					print "X"
			}
			print "|"
			println()
		}
		(0..gridWidth + 1).each {
			print "*"
		}
		println()
	}
}

def lifer = new LifeGame()
lifer.run()
