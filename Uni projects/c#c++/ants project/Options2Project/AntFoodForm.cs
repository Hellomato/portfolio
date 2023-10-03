using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

using SOFT152SteeringLibrary;

namespace SOFT152Steering
{
    public partial class AntFoodForm : Form
    {

        // decalare an Ant list
        private List<AntAgent> antList;

        // Declare a nest list
        private List<SOFT152Vector> nestList;

        //Declare a Food list
        private List<Food> foodList;

        //Declare an Ant List
        private List<AntAgent> robberAntList;

        //Declare a nest list for the robber ants
        private List<SOFT152Vector> robberAntNestList;

        // the random object given to each Ant agent
        private Random randomGenerator;

        // A bitmap image used for double buffering
        private Bitmap backgroundImage;

        //Declare a variable for the size of the food when drawn
        private float foodSize;

        //Declare a variable for the size of a nest when drawn
        private float nestSize;

        //declare a variable for the size of an ant when drawn
        private float antSize;

        //Declare a variable for the vision range of ants
        private int antVision;

        //Declare a variable for the collision radius of ants
        private int antCollision;


        public AntFoodForm()
        {        
            InitializeComponent();

            CreateBackgroundImage();
            
            //assign the default values of sizes for objects
            antSize = 5.0f;
            foodSize = 15.0f;
            nestSize = 20.0f;

            //assign the default values for vision and collision
            antCollision = 3;
            antVision = 15;
            
            //create all the lists
            foodList = new List<Food>();
            nestList = new List<SOFT152Vector>();
            robberAntList = new List<AntAgent>();
            robberAntNestList = new List<SOFT152Vector>();
            antList = new List<AntAgent>();

        }

        /// <summary>
        /// Creates ants equal to the number from the form
        /// </summary>
        private void CreateAnts()
        {
            
            Rectangle worldLimits;

            // create a random object to pass to the ants
            randomGenerator = new Random();

            // define some world size for the ants to move around on
            // assume the size of the world is the same size as the panel
            // on which they are displayed
            worldLimits = new Rectangle(0, 0, drawingPanel.Width, drawingPanel.Height);
            
            // Declare ant number variable
            int numberOfAnts;

            //try get the number of ants from the form
            try {
                numberOfAnts = Convert.ToInt32(textAnts.Text);

                //throw an exception if the number of ants is negative
                if(numberOfAnts < 0)
                {
                    throw new FormatException("Number of ants cannot be negative");
                };

                //throw an exception if the number of ants is not whole
                if(numberOfAnts - Convert.ToDouble(textAnts.Text) != 0)
                {
                    throw new FormatException("Number of ants must be a whole number");
                };

                //if the number of ants that exsist currently is not the same as the number of ants wanted create new ants
                if (antList.Count != numberOfAnts)
                {
                    antList.Clear();
                    //create ants and add them to list;
                    for (int i = 0; i < numberOfAnts; i++)
                    {
                        AntAgent tempAnt;

                        SOFT152Vector startingPoint;

                        //get a random point for the ant to be created at
                        startingPoint = GetRandomPos(antSize);

                        //create the ant with the new point
                        tempAnt = new AntAgent(startingPoint, randomGenerator, worldLimits);

                        antList.Add(tempAnt);
                    }
                }

            }
            catch (FormatException ex)
            {
                //show a message to inform the user of the error
                MessageBox.Show(ex.Message);
            }
            //catch any unknown errors
            catch (Exception ex) {
                MessageBox.Show("Please enter a valid number of ants");
            };
             
            //enable the ability to create evil nests if there are more than 0 ants
            if(antList.Count > 0)
            {
                RadioEvilNest.Enabled = true;
            }
            
        }
   
        /// <summary>
        ///  Creates the background image to be used in double buffering 
        /// </summary>
        private void CreateBackgroundImage()
        {
            int imageWidth;
            int imageHeight;

            // the backgroundImage  can be any size
            // assume it is the same size as the panel 
            // on which the Ants are drawn
            imageWidth = drawingPanel.Width;
            imageHeight = drawingPanel.Height;

            backgroundImage = new Bitmap(drawingPanel.Width, drawingPanel.Height);
        }
        
        /// <summary>
        /// Calls logic and drawing methods each tick of the timer
        /// </summary>
        private void timer_Tick(object sender, EventArgs e)
        {

            AntLogic();

            RobberAntLogic();
                        
            DrawThingsDoubleBuffering();
         }
        

        /// <summary>
        /// Draws everything that needs to be drawn
        /// </summary>
        private void DrawThingsDoubleBuffering()
        {
            Brush solidBrush;

            // get the graphics context of the background image
            using (Graphics backgroundGraphics =  Graphics.FromImage(backgroundImage))
            {
                // create a brush to draw ants
                solidBrush = new SolidBrush(Color.Red);
                
                //Clear graphics 
                backgroundGraphics.Clear(Color.White);

                //Draw all ants
                drawAnts(backgroundGraphics, solidBrush, antList);

                //Change brush colour to draw next item
                solidBrush = new SolidBrush(Color.Brown);

                //draw the nests from nestlist
                foreach (var nest in nestList)
                {
                    drawItem(backgroundGraphics, solidBrush, nest, nestSize);
                }
                
                //change the colour of the brush            
                solidBrush = new SolidBrush(Color.Green);

                //draw all the food from foodlist
                foreach(var food in foodList)
                {
                    drawItem(backgroundGraphics, solidBrush, food.Position, foodSize);
                }
                
                //change the colour of the brush
                solidBrush = new SolidBrush(Color.Blue);

                //draw the ants in the robber list
                drawAnts(backgroundGraphics, solidBrush, robberAntList);
                
                //change the colour of the brush
                solidBrush = new SolidBrush(Color.Black);

                //draw the nests in the robber ant nest list
                foreach(var nest in robberAntNestList)
                {
                    drawItem(backgroundGraphics, solidBrush, nest, nestSize);
                }


            }

            // now draw the image on the panel
            using (Graphics g = drawingPanel.CreateGraphics())
            {
                g.DrawImage(backgroundImage, 0, 0, drawingPanel.Width, drawingPanel.Height);
            }

                // dispose of resources
                solidBrush.Dispose();
        }

        /// <summary>
        /// Draws all the ants from the given list to the given graphics
        /// </summary>
        /// <param name="g"> The graphics to draw onto </param>
        /// <param name="b"> the brush used to draw </param>
        /// <param name="data"> list of ants to draw from </param>
        private void drawAnts(Graphics g, Brush b, List<AntAgent> data)
        {
            for (int i = 0; i < data.Count; i++)
            {
                //create a reference for the current ant
                AntAgent currentAnt = data.ElementAt(i);

                float tempXPosition;
                float tempYPosition;

                //get the current position of the ant as a float point
                tempXPosition = (float)currentAnt.AgentPosition.X;
                tempYPosition = (float)currentAnt.AgentPosition.Y;

                

                // draw the agent on the backgroundImage at the given position
                g.FillEllipse(b, tempXPosition, tempYPosition, antSize, antSize);

                //draw food on the ant if it is carrying food
                if (currentAnt.HasFood)
                {
                    Brush foodBrush = new SolidBrush(Color.Green);
                    g.FillEllipse(foodBrush, (float)currentAnt.AgentPosition.X, (float)currentAnt.AgentPosition.Y, 4, 4);

                    foodBrush.Dispose();
                }


            }
        }

        /// <summary>
        /// Draw a vector object with size provided to the given graphics
        /// </summary>
        /// <param name="g"> graphics to draw onto</param>
        /// <param name="b"> the brush used to draw with</param>
        /// <param name="data"> the vector posistion of the object to be drawn </param>
        /// <param name="size"> the size of the object </param>
        private void drawItem(Graphics g, Brush b, SOFT152Vector data, float size)
        {
           float tempXPosition;
           float tempYPosition;

           //get the position of the object as a float point
           tempXPosition = (float)data.X;
           tempYPosition = (float)data.Y;

           //draw the object
           g.FillEllipse(b, tempXPosition, tempYPosition, size, size);
            
        }

        /// <summary>
        /// Helper method for timer tick
        /// Logic for movement of ants and interactions
        /// </summary>
        private void AntLogic()
        {
            for (int i = 0; i < antList.Count; i++)
            {
                //create a reference for the current ant
                AntAgent currentAnt = antList.ElementAt(i);

                //for each nest in nestlist do the following
                for (int j = 0; j < nestList.Count; j++)
                {
                    //create a reference for the current nest
                    SOFT152Vector currentNest = nestList.ElementAt(j);

                    //check to see if the current ant can see the current nest
                    //if it can update the ants knowledge
                    if (currentAnt.AgentPosition.Distance(currentNest) < antVision)
                    {
                        currentAnt.NestPos = currentNest;
                    }

                    //check to see if the current ant is within collision radius of the current nest
                    //if it is interact with it and stay outside of collision radius
                    if (currentAnt.AgentPosition.Distance(currentNest) < antCollision)
                    {
                        currentAnt.FleeFrom(currentNest);
                        if (currentAnt.HasFood)
                        {
                            currentAnt.HasFood = false;

                            currentAnt.AgentSpeed = 1;
                        }

                    }
                }

                //for each ant after this one in antlist do the following
                for (int j = i + 1; j < antList.Count; j++)
                {
                    //create a reference for the other ant
                    AntAgent anotherAnt = antList.ElementAt(j);

                    //check to see if the ants are with collision radius
                    //if they are move away from eachother
                    if (currentAnt.AgentPosition.Distance(anotherAnt.AgentPosition) < antCollision)
                    {
                        currentAnt.FleeFrom(anotherAnt.AgentPosition);

                    }
                    
                    //check to see if the current ant can see the other ant
                    //if it can interact with it
                    if (currentAnt.AgentPosition.Distance(anotherAnt.AgentPosition) < antVision)
                    {
                        if (anotherAnt.FoodPos != null)
                        {
                            currentAnt.FoodPos = anotherAnt.FoodPos;
                        }
                        if (anotherAnt.NestPos != null)
                        {

                            currentAnt.NestPos = anotherAnt.NestPos;
                        }
                    }
                }

                //for each food in foodlist do the following
                for (int j = 0; j < foodList.Count; j++)
                {
                    //create a reference for the current food
                    Food currentFood = foodList.ElementAt(j);

                    //check if current ant can see current food
                    //if it can update ant knowledge
                    if (currentAnt.AgentPosition.Distance(currentFood.Position) < antVision)
                    {
                        currentAnt.FoodPos = currentFood.Position;
                    }

                    //check if current ant is within collision radius of current food
                    //if it is interact and move away
                    if (currentAnt.AgentPosition.Distance(currentFood.Position) < antCollision)
                    {
                        currentAnt.FleeFrom(currentFood.Position);
                        if (currentAnt.HasFood == false)
                        {
                            currentAnt.HasFood = true;
                            currentAnt.AgentSpeed = 0.7;
                            currentFood.Decrease();

                        }

                        //if the current food has no uses left remove it
                        if (currentFood.Uses == 0)
                        {
                            foodList.RemoveAt(j);
                        }
                     
                    }
                }
                
                //movement logic
                //if the ant has food and knows where a nest is go to the nest
                if (currentAnt.HasFood == true && currentAnt.NestPos != null)
                {
                    currentAnt.Approach(currentAnt.NestPos);
                }
                //if the ant does not have food and does know where food is go to food
                else if (currentAnt.HasFood == false && currentAnt.FoodPos != null)
                {
                    currentAnt.Approach(currentAnt.FoodPos);

                    //if the ant knows where food is and the food is within vision forget where the food is (incase food is gone)
                    if(currentAnt.FoodPos != null && currentAnt.AgentPosition.Distance(currentAnt.FoodPos) < antVision)
                    {
                        currentAnt.FoodPos = null;
                    }
                }
                //if the other conditions are not met wander until they are
                else
                {
                    currentAnt.Wander();
                }
            };
        }

        /// <summary>
        /// Helper method for timer tick
        /// Logic for movment of robberants and interactions
        /// </summary>
        private void RobberAntLogic()
        {
            //for each robber ant in the robber ant list do the following
            for (int i = 0; i < robberAntList.Count; i++)
            {
                //create a reference for the current robber
                AntAgent currentRobber = robberAntList.ElementAt(i);

                
                //if the current robber doesnt have food
                if (currentRobber.HasFood == false)
                {
                    //look through all ants 
                    for (int j = 0; j < antList.Count; j++)
                    {
                        //create a reference for the ant
                        AntAgent anotherAnt = antList.ElementAt(j);

                        //check if the robber can see the ant and if the ant has food
                        //if both are true robber approach ant and ant flee from robber
                        if (currentRobber.AgentPosition.Distance(anotherAnt.AgentPosition) < antVision && anotherAnt.HasFood)
                        {
                            currentRobber.Approach(anotherAnt.AgentPosition);

                            anotherAnt.FleeFrom(currentRobber.AgentPosition);
                        }

                        //check if the robber has 'caught' the ant'
                        //if it has steal the food
                        if (currentRobber.AgentPosition.Distance(anotherAnt.AgentPosition) < antCollision && anotherAnt.HasFood)
                        {
                            currentRobber.HasFood = true;
                            anotherAnt.HasFood = false;
                            currentRobber.FoodPos = new SOFT152Vector(anotherAnt.AgentPosition);


                            currentRobber.AgentSpeed = 0.7;
                        }
                    }
                }
                

                for (int j = i + 1; j < robberAntList.Count; j++)
                {
                    //create a reference for another robber
                    AntAgent anotherrobber = robberAntList.ElementAt(j);

                    //if the ants can see eachother exchange information
                    if (currentRobber.AgentPosition.Distance(anotherrobber.AgentPosition) < antVision)
                    {
                        if (anotherrobber.FoodPos != null)
                        {
                            currentRobber.FoodPos = anotherrobber.FoodPos;
                        }
                        if (anotherrobber.NestPos != null)
                        {

                            currentRobber.NestPos = anotherrobber.NestPos;
                        }
                    }

                    //if the ants are within collision radius flee from eachother
                    if (currentRobber.AgentPosition.Distance(anotherrobber.AgentPosition) < antCollision)
                    {
                        currentRobber.FleeFrom(anotherrobber.AgentPosition);
                    }
                }
                
                for (int j = 0; j < robberAntNestList.Count; j++)
                {
                    //create reference for the current nest
                    SOFT152Vector currentnest = robberAntNestList.ElementAt(j);

                    //if the robber can see the nest update the ant knowledge
                    if (currentRobber.AgentPosition.Distance(currentnest) < antVision)
                    {
                        currentRobber.NestPos = currentnest;
                    }
                    
                    //if the robber is within collision radius interact with nest
                    if (currentRobber.AgentPosition.Distance(currentnest) < antCollision)
                    {
                        if (currentRobber.HasFood)
                        {
                            currentRobber.HasFood = false;

                            currentRobber.AgentSpeed = 1;
                        }

                    }
                }

                //movement logic for robberant
                //if the current robber has food and knows where a nest is go to nest
                if (currentRobber.NestPos != null && currentRobber.HasFood)
                {
                    currentRobber.Approach(currentRobber.NestPos);
                }
                //if the current robber knows where food is and does not have food go to where food was last
                else if (currentRobber.FoodPos != null && currentRobber.HasFood == false)
                {
                    currentRobber.Approach(currentRobber.FoodPos);
                    //if the current robber can see where food was before forget where food was (incase food is no longer there)
                    if (currentRobber.FoodPos != null && currentRobber.AgentPosition.Distance(currentRobber.FoodPos) < antVision)
                    {
                        currentRobber.FoodPos = null;
                    }
                }
                //if no other conditions are met wander until they are
                else
                {
                    currentRobber.Wander();
                }

            }
        }

        /// <summary>
        /// Creates a valid random position within the world
        /// </summary>
        /// <param name="sizeOfObject"> size of the object so to be drawn so its not drawn off screen</param>
        /// <returns> a random valid vector posistion </returns>
        private SOFT152Vector GetRandomPos(float sizeOfObject)
        {
            //create the vectore to pass back
            SOFT152Vector pos;

            //intalise it
            pos = new SOFT152Vector();

            //set the x position to be a random number between the width of the world minus the size of the object
            pos.X = randomGenerator.Next(0, drawingPanel.Width - (int)sizeOfObject);
            
            //set the y position to be a random number between the height of the world minus the size of the object
            pos.Y = randomGenerator.Next(0, drawingPanel.Height -(int)sizeOfObject);

            //return the vector
            return pos;
        }

        /// <summary>
        /// Stops the timer
        /// </summary>
        private void stopButton_Click(object sender, EventArgs e)
        {
            //stop the timer
            timer.Stop();
            
        }

        /// <summary>
        /// Starts the timer and creates ants
        /// </summary>
        private void startButton_Click(object sender, EventArgs e)
        {
            //create the ants
            CreateAnts();
            
            //start the timer
            timer.Start();
        }

        /// <summary>
        /// Gets the mouse location and creates objects based on what is selected on the form
        /// </summary>
        private void drawingPanel_MouseDown(object sender, MouseEventArgs e)
        {
            //create a point to store mouse location
            Point mouseLocation;
            //set point to be mouse current location
            mouseLocation = e.Location;

            SOFT152Vector locationAsVector;
            //convert mouse location to a vector
            locationAsVector = new SOFT152Vector(mouseLocation.X,mouseLocation.Y);

            //create an object based on what is checked on the radio buttons
            if (radioNest.Checked)
            {
                CreateNest(locationAsVector);
                
            } else if (radioFood.Checked)
            {
                CreateFood(locationAsVector);

            } else if (RadioEvilNest.Checked)
            {
                CreateRobberAntNest(locationAsVector);
                CreateRobberAnts(locationAsVector);
            }

            //update display
            DrawThingsDoubleBuffering();
        }

        /// <summary>
        /// Creates nests at a given vector
        /// </summary>
        /// <param name="v"> the vector position for the nest to be created at</param>
        private void CreateNest(SOFT152Vector v)
        {

            SOFT152Vector tempVector = new SOFT152Vector(v);

            nestList.Add(tempVector);

        }

        /// <summary>
        /// Creates food at a given vector
        /// </summary>
        /// <param name="v">the vector position to create the food at </param>
        private void CreateFood(SOFT152Vector v)
        {
            //create a food with a reasonable amount of uses
            int numberOfUses = 50;

            Food tempFood = new Food(v, numberOfUses);

            foodList.Add(tempFood);
        }

        /// <summary>
        /// Creates a robberAnt nest at a given vector
        /// </summary>
        /// <param name="v">the vector position to create the robber nest at</param>
        private void CreateRobberAntNest(SOFT152Vector v)
        {
            SOFT152Vector tempVector = new SOFT152Vector(v);

            robberAntNestList.Add(tempVector);
        }

        /// <summary>
        /// Creates a number of robber ants based on the number of ants at a given nest
        /// </summary>
        /// <param name="nest"> vector posistion of the nest to create ants at </param>
        private void CreateRobberAnts(SOFT152Vector nest)
        {
            //check to see that a nest exsists
            if (robberAntNestList.Count > 0)
            {
                int robberAnts;
                //create robber ants as a fraction of the total amount of ants
                robberAnts = antList.Count / 5;

                for (int i = 0; i < robberAnts; i++)
                {
                    Rectangle worldLimits;
                    worldLimits = new Rectangle(0, 0, drawingPanel.Width, drawingPanel.Height);

                    //create a robber ant at the nest
                    AntAgent robberAnt = new AntAgent(nest, randomGenerator, worldLimits);

                    //add that ant to the robber ant list
                    robberAntList.Add(robberAnt);
                }
            }
        }

        /// <summary>
        /// Clears all lists and resets the drawing
        /// </summary>
        private void resetButton_Click(object sender, EventArgs e)
        {
            //clear all lists to reset program
            antList.Clear();
            robberAntList.Clear();
            foodList.Clear();
            robberAntNestList.Clear();
            nestList.Clear();

            //disable the evil ant nest button
            RadioEvilNest.Enabled = false;

            //update the display
            DrawThingsDoubleBuffering();
        }
    }
}
