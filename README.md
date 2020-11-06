# AI-Project
For a computer to play Snake automatically there are three
searching algorithms related to artiﬁcial intelligence, Best First Search, A* Search and improved A* Search with forward checking.
## Characteristics 
In the most general way, our implementation consists of the snake moving on a square board, trying to eat  
as many apples as possible without biting itself.  
Once the snake eats an apple, a new apple is placed in a free position on the board and the snake length grows by one unit.   
When the snake has no choice other than biting itself, the game is over and a ﬁnal score is returned.  
In our implementation, we simply calculate the score as the number of apples the snake has eaten or equivalently, the  
length increased by the snake.  
The following are some basic rules followed by our implementation:  

1. Goal - the snake tries to eat as many apples as possible, within ﬁnite steps.   
    The ﬁrst priority for the snake is to not bite itself while the second is to increase the score.  
2. There are four possible directions the snake can move: north, south, east, and west.   
    However, because of the placement of its tail some directions may not be available.   
    The most clear example is that the snake can never swap to an opposite direction i.e. north to south, east to west,    etc.
3. The snake grows by one unit when eating an apple.   
    The growth is immediately reﬂected by the gained length of the tail, i.e. the tip of the tail occupies the square on which the apple       was.
4. The board size is ﬁxed to square.  
5. After an apple is eaten by the snake, another apple is placed randomly with uniform probability on one available squares of the board.     Here the availability of a square is denoted by the fact that it is not occupied by the snake.
