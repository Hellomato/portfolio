﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;


using System.Threading;
using System.Drawing;

using SOFT152SteeringLibrary;

namespace SOFT152Steering
{
    class AntAgent
    {

        /// <summary>
        /// The speed of the agent as used in all three movment methods 
        /// Ideal value depends on timer tick interval and realistic motion of
        /// agents needed. Suggest though in range 0 ... 2
        /// </summary>
        public double AgentSpeed { set; get; }  


        /// <summary>
        /// If the agent is using the the ApproachAgent() method, this property defines
        /// at what point the agent will reduce the speed of approach to miminic a 
        /// more relistic approach behaviour
        /// </summary>
        public double ApproachRadius { set; get; }    
        
        public double AvoidDistance { set; get; }      

        /// <summary>
        /// Property defines how 'random' the agent movement is whilst 
        /// the agent is using the Wander() method
        /// Suggest range of WanderLimits is 0 ... 1
        /// </summary>
        public double WanderLimits { set; get; }


        /// <summary>
        /// Used in conjunction worldBounds to determine if
        /// the agents position will stay within the world bounds 
        /// </summary>
        public bool ShouldStayInWorldBounds { set; get; }


        /// <summary>
        /// property that defines whether the ant has food or not
        /// used to control interaction and movement logic
        /// </summary>
        public bool HasFood { get; set; }

        /// <summary>
        /// the position of the food in the ants memory (null if unknown)
        /// used to control interaction and movement logic
        /// </summary>
        public SOFT152Vector FoodPos { get; set; }

        /// <summary>
        /// the position of the nest in the ants memory (null if unknown)
        /// used to control interaction and movement logic
        /// </summary>
        public SOFT152Vector NestPos { set; get; }

        // --------------------------------------------
        // Private fields 

        /// <summary>
        /// Current postion of the agent, updated by the three
        /// movment methods
        /// </summary>
        private SOFT152Vector agentPosition;  

        /// <summary>
        /// used in conjunction with the Wander() method
        /// to detemin the next position an agent should be in 
        /// Should remain a private field and do not edit within this class
        /// </summary>
        private SOFT152Vector wanderPosition;


        /// <summary>
        /// The size of the world the agent lives on as a Rectangle object.
        /// Used in conjunction with ShouldStayInWorldBounds, which if true
        /// will mean the agents position will be kept within the world bounds 
        /// (i.e. the  world width or the world height)
        /// </summary>
        private Rectangle worldBounds;   // To keep track of the obejcts bounds i.e. ViewPort dimensions

        /// <summary>
        /// The random object passed to the agent. 
        /// Used only in the Wander() method to generate a 
        /// random direction to move in
        /// </summary>
        private Random randomNumberGenerator;              // random number used for wandering


        /// <summary>
        /// default constructor for an ant
        /// </summary>
        /// <param name="position"> position for ant to be created at</param>
        /// <param name="random">random number generator as one cant be created within this method</param>
        public AntAgent(SOFT152Vector position, Random random)
        {
           agentPosition = new SOFT152Vector(position.X, position.Y);

           randomNumberGenerator = random;

            
            InitialiseAgent();
        }

        /// <summary>
        /// Main constructor for an ant
        /// </summary>
        /// <param name="position">posistion for ant to be created at</param>
        /// <param name="random">random number generator as one cant be created within this method</param>
        /// <param name="bounds"> the area in which the ant considers a world</param>
        public AntAgent(SOFT152Vector position, Random random, Rectangle bounds )
        {
            agentPosition = new SOFT152Vector(position.X, position.Y);

            worldBounds = new Rectangle(bounds.X, bounds.Y, bounds.Width, bounds.Height);

            randomNumberGenerator = random;

            InitialiseAgent();
        }


        /// <summary>
        /// Initialises the Agents various fields
        /// with default values
        /// </summary>
        private void InitialiseAgent()
        {
            wanderPosition = new SOFT152Vector();
            wanderPosition.X = randomNumberGenerator.Next();
            wanderPosition.Y = randomNumberGenerator.Next();

            ApproachRadius = 10;

            AvoidDistance = 25;

            AgentSpeed = 1.0;

            ShouldStayInWorldBounds = true;

            WanderLimits = 0.5;

            HasFood = false;
        }

        /// <summary>
        /// Causes the agent to make one step towards the object at objectPosition
        /// The speed of approach will reduce one this agent is within
        /// an ApproachRadius of the objectPosition
        /// </summary>
        /// <param name="agentToApproach"></param>
        public void Approach(SOFT152Vector objectPosition)
        {

           Steering.MoveTo(agentPosition, objectPosition, AgentSpeed, ApproachRadius);

            StayInWorld();
            ChanceToForget();          
        }

        /// <summary>
        /// Causes the agent to make one step away from  the objectPosition
        /// The speed of avoid is goverened by this agents speed
        /// </summary>
        public void FleeFrom(SOFT152Vector objectPosition)
        {

            Steering.MoveFrom(agentPosition, objectPosition, AgentSpeed, AvoidDistance);

            StayInWorld();
            ChanceToForget();
        }

        /// <summary>
        /// Causes the agent to make one random step.
        /// The size of the step determined by the value of WanderLimits
        /// and the agents speed
        /// </summary>
        public void Wander()
        {
            Steering.Wander(agentPosition, wanderPosition, WanderLimits, AgentSpeed, randomNumberGenerator);

           StayInWorld();
           ChanceToForget();
        }


        /// <summary>
        /// if the ant is supposed to stay within its bounds
        /// prevent it from moving forward until it choses a different direction
        /// </summary>
        private void StayInWorld()
        {
            // if the agent should stay with in the world
            if (ShouldStayInWorldBounds == true)
            {
                // and the world has a positive width and height
                if (worldBounds.Width >= 0 && worldBounds.Height >= 0)
                {
                    // now adjust the agents position if outside the limits of the world
                    if (agentPosition.X < 0)
                        agentPosition.X = 0;

                    else if (agentPosition.X > worldBounds.Width)
                        agentPosition.X = worldBounds.Width;

                    if (agentPosition.Y < 0)
                        agentPosition.Y = 0;

                    else if (AgentPosition.Y > worldBounds.Height)
                        agentPosition.Y = worldBounds.Height;
                }
            }
        }

        /// <summary>
        ///  give the ant a very small chance to forget its nest or food
        /// </summary>
        private void ChanceToForget()
        {
            int forgetChance = randomNumberGenerator.Next(0, 100);

            if (forgetChance > 96)
            {
                if (forgetChance > 98)
                {
                    NestPos = null;
                }
                else
                {
                    FoodPos = null;
                }
            }
        }



        /// <summary>
        /// getter and setter methods for agent position 
        /// given by lecturer probably could remove
        /// </summary>
        public SOFT152Vector AgentPosition
        {
            set
            {
                agentPosition = value;
            }

            get
            {
                return agentPosition;
            }
        }

    }  // end class AntAgent
}
