using System;

namespace Mastermind
{
    class Queue {
        public int front = -1;
        public int back = -1;
        public int[] data;
        public int len;

        public Queue(int length)
        {
            data = new int[length];
            len = length;
        }


        
    }
    class Program
    {
        /// <summary>
        /// Turns the first character of input into number
        /// could improve to include any lengths, not needed unless pegs/colours > 10
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        static int stringToInt(string input)
        {
            
            char character = input[0];

            int number = character - '0';

            return number;
        }

        /// <summary>
        /// add data to back of queue 
        /// taken from pracical work
        /// </summary>
        /// <param name="q"></param>
        /// <param name="i"></param>
        static void add(Queue q, int i)
        {

            if (isFull(q))
            {

                Console.WriteLine("Error");
                // better error handling?
            }
            else
            {

                if (isEmpty(q))
                {

                    q.front = q.back = 0; // or wherever one wants to start
                    q.data[q.front] = i;

                }
                else
                { // somewhat filled 

                    if (q.back == (q.len -1)) // no place left at end 
                        q.back = -1; // cycle around

                    q.back = q.back + 1;
                    q.data[q.back] = i;
                }
            }
        }

        /// <summary>
        /// remove front item from queue
        /// taken from pracical work
        /// </summary>
        /// <param name="q"></param>
        /// <returns></returns>
        static int remove(Queue q)
        {
            if (isEmpty(q))
            {
                Console.WriteLine("ERROR - queue is empty");
                return (-1);
            }
            else
            {

                int temp = q.data[q.front];

                q.front = q.front + 1;
                if (q.front == q.len)
                    q.front = 0;

                // Note: No data shuffling necessary here
                //   in contrast to array_queue.cs

                // have we emptied the queue?     
                if (size(q) == q.len)
                    q.front = q.back = -1;

                return (temp);
            }
        }
        /// <summary>
        /// return true if queue is empty
        /// taken from pracical work
        /// </summary>
        /// <param name="q"></param>
        /// <returns></returns>
        static bool isEmpty(Queue q)
        {
            return q.front == -1;
        }

        /// <summary>
        /// size of the data from first to last not including empty fields
        /// taken from pracical work
        /// </summary>
        /// <param name="q"></param>
        /// <returns></returns>
        static int size(Queue q)
        {
            if (q.front == -1)  // empty
                return 0;
            else
                return (1 + (q.len + q.back - q.front) % q.len); // nasty trick ... 
        }

        /// <summary>
        /// return true if queue is full
        /// taken from pracical work
        /// </summary>
        /// <param name="q"></param>
        /// <returns></returns>
        static bool isFull(Queue q)
        {
            return size(q) >= q.len;
        }


        static void Main(string[] args)
        {
            //create variables
            int pegs;
            int colours;
            int[] codeArray;
            int[] guessArray;
            bool correctGuess = false;
            int guesses = 10;

            //create random number generator to make code
            Random numberGen = new Random();

            //create a new queue to store guesses
            Queue history = new Queue(200); 

            //ask and save number of pegs, asuumes user behaves
            Console.WriteLine("How many pegs?(max 9)");
            pegs = stringToInt(Console.ReadLine()); ;

            //asks and stores number of colours wanted, asumes user behaves
            Console.WriteLine("How many colours?(max 9)");
            colours = stringToInt( Console.ReadLine());


            //create arrays to store the code and guess
            codeArray = new int[pegs];
            guessArray = new int[pegs];

            //create code
            for(int i = 0; i < pegs; i++)
            {
                codeArray[i] = numberGen.Next(1, colours);
            }

            //main game loop
            while (!correctGuess)
            {
                int blackPegs = 0 ;
                int whitePegs = 0 ;

                //get guess
                for (int i = 0; i < pegs; i++)
                {

                    Console.WriteLine("Guess peg " + (i + 1));

                    guessArray[i] = stringToInt(Console.ReadLine());

                    //add the input to queue to store whole guess to print later
                    add(history, guessArray[i]);

                    //if history is full break (some error occured)
                    if (isFull(history))
                    {
                        break;
                    }

                }
                //same again but informs user
                if (isFull(history))
                {
                    Console.WriteLine("You ran out of guesses");
                    break;
                }

                //check guess
                //create a temporary array to store a modified version of the secret code
                int[] checkCode = new int[pegs];

                //firstly check if any are in same place and same colour
                for (int i = 0; i < pegs; i++)
                {
                    //copy code into temporary code;
                    checkCode[i] = codeArray[i];
                    if (guessArray[i] == codeArray[i])
                    {
                        //if match add one black peg, remove corrisponding code peice and guess to remove 
                        //unnesssary checks and errors
                        blackPegs += 1;
                        checkCode[i] = 0;
                        guessArray[i] = 0;
                    }
                }

                //check for white pegs
                for (int i = 0; i < pegs; i++)
                {

                    {
                        for (int j = 0; j < pegs; j++)
                        {
                            //if colour exists in the temp code add a white peg and remove the guess from the checks
                            if (guessArray[i] == checkCode[j] && guessArray[i] != 0)
                            {
                                whitePegs += 1;
                                checkCode[i] = 0;
                                break;
                                
                            }
                        }
                    }

                    
                }
                //add a spacer (used in printing)
                add(history, -2);
                //store white and black pegs
                add(history, whitePegs);
                add(history, blackPegs);
                //another spacer to make things look nice on the other end
                add(history, -1);

                //print guess

                if (isEmpty(history))
                {
                    Console.WriteLine();
                }
                else
                {
                    Console.WriteLine("Previous guesses");
                    int len = size(history);
                    //loop through queue print all values
                    for (int i = 0; i < len; i++)
                    {
                        int currentNumber = history.data[(history.front + i)] % history.len;


                        //make things look nice
                        if(currentNumber == -1)
                        {
                            Console.WriteLine();
                        }
                        else if(currentNumber == -2)
                        {
                            Console.Write("| ");
                        }else
                        {
                            Console.Write(currentNumber + "  ");
                        }
                        
                       
                 
                    }
                }

                // if the code is correct end the game
                if (blackPegs == pegs)
                {
                    correctGuess = true;
                    Console.WriteLine("You Cracked the code!");
                    break;
                }

                //remove one guess if they run out end game
                guesses -= 1;
                if (guesses == 0)
                {
                    Console.WriteLine("Out of Guesses");
                    break;
                }


               

            }
            Console.WriteLine("Game over");
            Console.Read();
        }
    }
}