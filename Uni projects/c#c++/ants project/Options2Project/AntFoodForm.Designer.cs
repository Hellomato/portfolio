namespace SOFT152Steering
{
    partial class AntFoodForm
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.components = new System.ComponentModel.Container();
            this.drawingPanel = new System.Windows.Forms.Panel();
            this.startButton = new System.Windows.Forms.Button();
            this.stopButton = new System.Windows.Forms.Button();
            this.timer = new System.Windows.Forms.Timer(this.components);
            this.label1 = new System.Windows.Forms.Label();
            this.textAnts = new System.Windows.Forms.TextBox();
            this.groupBox1 = new System.Windows.Forms.GroupBox();
            this.radioNest = new System.Windows.Forms.RadioButton();
            this.RadioEvilNest = new System.Windows.Forms.RadioButton();
            this.radioFood = new System.Windows.Forms.RadioButton();
            this.resetButton = new System.Windows.Forms.Button();
            this.groupBox1.SuspendLayout();
            this.SuspendLayout();
            // 
            // drawingPanel
            // 
            this.drawingPanel.BackColor = System.Drawing.Color.White;
            this.drawingPanel.Location = new System.Drawing.Point(42, 93);
            this.drawingPanel.Name = "drawingPanel";
            this.drawingPanel.Size = new System.Drawing.Size(373, 320);
            this.drawingPanel.TabIndex = 0;
            this.drawingPanel.MouseDown += new System.Windows.Forms.MouseEventHandler(this.drawingPanel_MouseDown);
            // 
            // startButton
            // 
            this.startButton.Location = new System.Drawing.Point(243, 6);
            this.startButton.Name = "startButton";
            this.startButton.Size = new System.Drawing.Size(75, 23);
            this.startButton.TabIndex = 1;
            this.startButton.Text = "Start";
            this.startButton.UseVisualStyleBackColor = true;
            this.startButton.Click += new System.EventHandler(this.startButton_Click);
            // 
            // stopButton
            // 
            this.stopButton.Location = new System.Drawing.Point(324, 6);
            this.stopButton.Name = "stopButton";
            this.stopButton.Size = new System.Drawing.Size(75, 23);
            this.stopButton.TabIndex = 2;
            this.stopButton.Text = "Stop";
            this.stopButton.UseVisualStyleBackColor = true;
            this.stopButton.Click += new System.EventHandler(this.stopButton_Click);
            // 
            // timer
            // 
            this.timer.Interval = 20;
            this.timer.Tick += new System.EventHandler(this.timer_Tick);
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(51, 9);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(80, 13);
            this.label1.TabIndex = 3;
            this.label1.Text = "Number of Ants";
            // 
            // textAnts
            // 
            this.textAnts.Location = new System.Drawing.Point(137, 6);
            this.textAnts.Name = "textAnts";
            this.textAnts.Size = new System.Drawing.Size(100, 20);
            this.textAnts.TabIndex = 4;
            this.textAnts.Text = "0";
            // 
            // groupBox1
            // 
            this.groupBox1.Controls.Add(this.radioNest);
            this.groupBox1.Controls.Add(this.RadioEvilNest);
            this.groupBox1.Controls.Add(this.radioFood);
            this.groupBox1.Location = new System.Drawing.Point(84, 35);
            this.groupBox1.Name = "groupBox1";
            this.groupBox1.Size = new System.Drawing.Size(288, 52);
            this.groupBox1.TabIndex = 5;
            this.groupBox1.TabStop = false;
            this.groupBox1.Text = "Object to place";
            // 
            // radioNest
            // 
            this.radioNest.AutoSize = true;
            this.radioNest.Location = new System.Drawing.Point(7, 20);
            this.radioNest.Name = "radioNest";
            this.radioNest.Size = new System.Drawing.Size(47, 17);
            this.radioNest.TabIndex = 3;
            this.radioNest.Text = "Nest";
            this.radioNest.UseVisualStyleBackColor = true;
            // 
            // RadioEvilNest
            // 
            this.RadioEvilNest.AutoSize = true;
            this.RadioEvilNest.Enabled = false;
            this.RadioEvilNest.Location = new System.Drawing.Point(122, 19);
            this.RadioEvilNest.Name = "RadioEvilNest";
            this.RadioEvilNest.Size = new System.Drawing.Size(67, 17);
            this.RadioEvilNest.TabIndex = 2;
            this.RadioEvilNest.Text = "Evil Nest";
            this.RadioEvilNest.UseVisualStyleBackColor = true;
            // 
            // radioFood
            // 
            this.radioFood.AutoSize = true;
            this.radioFood.Location = new System.Drawing.Point(67, 20);
            this.radioFood.Name = "radioFood";
            this.radioFood.Size = new System.Drawing.Size(49, 17);
            this.radioFood.TabIndex = 1;
            this.radioFood.Text = "Food";
            this.radioFood.UseVisualStyleBackColor = true;
            // 
            // resetButton
            // 
            this.resetButton.Location = new System.Drawing.Point(378, 35);
            this.resetButton.Name = "resetButton";
            this.resetButton.Size = new System.Drawing.Size(75, 23);
            this.resetButton.TabIndex = 6;
            this.resetButton.Text = "Reset";
            this.resetButton.UseVisualStyleBackColor = true;
            this.resetButton.Click += new System.EventHandler(this.resetButton_Click);
            // 
            // AntFoodForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(457, 454);
            this.Controls.Add(this.resetButton);
            this.Controls.Add(this.groupBox1);
            this.Controls.Add(this.textAnts);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.stopButton);
            this.Controls.Add(this.startButton);
            this.Controls.Add(this.drawingPanel);
            this.Name = "AntFoodForm";
            this.Text = "Steering";
            this.groupBox1.ResumeLayout(false);
            this.groupBox1.PerformLayout();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Panel drawingPanel;
        private System.Windows.Forms.Button startButton;
        private System.Windows.Forms.Button stopButton;
        private System.Windows.Forms.Timer timer;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.TextBox textAnts;
        private System.Windows.Forms.GroupBox groupBox1;
        private System.Windows.Forms.RadioButton RadioEvilNest;
        private System.Windows.Forms.RadioButton radioFood;
        private System.Windows.Forms.RadioButton radioNest;
        private System.Windows.Forms.Button resetButton;
    }
}

