using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace TrafficLight
{
    static class Program
    {
        /// <summary>
        /// The main entry point for the application.
        /// </summary>
        [STAThread]
        static void Main()
        {
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            
            //create 2 forms;
            for (int i = 1; i < 2; i++)
            {
                FormTrafficLight currentLight = new FormTrafficLight();
                currentLight.Show();
                currentLight.ClientID = i;
                 
                Thread.Sleep(100);
            }
            Application.Run(new FormTrafficLight());
            
        }
    }
}
