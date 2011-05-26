package GameOfLife

class LifeGame {
	/* Controls the game */
	def grid
	def gridWidth
	def gridHeight
	
	void run() {
		welcome()
		printGrid()
		setupBeasties()
		evolve(0)
		
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
		
		grid = new Object[gridWidth][gridHeight]
		(0..<gridHeight).each {
			def line = it
			(0..<gridWidth).each{
				grid[it][line] = 0
			}
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
		def newGrid = new Object[gridWidth][gridHeight]
		(0..<gridHeight).each { 
			def line = it
			(0..<gridWidth).each {
				newGrid[it][line] = liveOrDie(it, line)
			}
		}
		grid = newGrid
		
		new Timer().runAfter(500, {evolve(++generation)})
		
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
			else if (grid[x][y] == 1 && mates == 2)
				return 1
			else
				return 0
		}
		if (!((y + index) in 0..<gridHeight))
			return score(++index, x, y, mates)
		if ((x - 1) >= 0)
			mates += grid[x - 1][y + index]
		if (index != 0)
			mates += grid[x][y + index]
		if ((x + 1) < gridWidth)
			mates += grid[x + 1][y + index]
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
		if (!(gridW in 1..gridWidth)||!(gridH in 1..gridHeight)){
			errorXY(gridPoint + " out of range")
			getBeastiePlaces()
			return
		}
		grid[gridW - 1][gridH - 1] = 1;
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
		(0..<gridHeight).each {
			def line = it
			print "|"
			(0..<gridWidth).each {
				if (grid[it][line] == 0)
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