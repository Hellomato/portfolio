using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using SOFT152SteeringLibrary;

namespace SOFT152Steering
{
    class Food
    {
        /// <summary>
        /// controls how many times a food point can provide food
        /// </summary>
        public int Uses { get; set; }

        /// <summary>
        /// the vector position of the food in the world
        /// </summary>
        public SOFT152Vector Position { get; set; }

        /// <summary>
        /// Default constructor to create a food at a given location with a certain amount of uses
        /// </summary>
        /// <param name="v"> vector position to create food at</param>
        /// <param name="uses"> nuber of uses the food has before it is removed</param>
        public Food(SOFT152Vector v, int uses)
        {
            Uses = uses;
            Position = v;
        }

        /// <summary>
        /// decreases the number of uses by one
        /// to be called whenever this object 'gives' out food
        /// </summary>
        public void Decrease()
        {
            Uses -= 1;

        }
    }
}
