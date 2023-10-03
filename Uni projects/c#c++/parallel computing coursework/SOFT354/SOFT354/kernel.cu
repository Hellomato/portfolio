
#include "cuda_runtime.h"
#include "device_launch_parameters.h"
#include <vector>
#include <stdio.h>
#include <stdlib.h>
#include <time.h>;


const int hiddenNodes = 40;
const int cycles = 5000;
const float lr = 0.02f;
const int datapoints = 2000;
const float length = 0.4;




#define PI 3.14159265358979323846

struct point {
	float x;
	float y;
};

typedef struct {
	int width;
	int height;
	float* elements; // Matrices are stored in row - major order
} Matrix;

Matrix trainingElbows,trainingEndPoints,trainingAngles;
Matrix testingElbows, testingEndPoints, testingAngles;

float
randomf(const float min, const float max) {

	float num = rand() / (float)RAND_MAX;
	return min + num * (max - min);
}

//adaptation of code by Dr. Ian Howard
//howardlab.com
point* kinematics2D(float l1, float l2, float a1, float a2, float ox, float oy) {
	point points[2];

	point p1;
	p1.x = l1 * cos(a1) + 1.0f * ox;
	p1.y = l1 * sin(a1) + 1.0f * oy;

	point p2;
	p2.x = l1 * cos(a1) + l2 * cos(a1 + a2) + 1 * ox;
	p2.y = l1 * cos(a1) + l2 * cos(a1 + a2) + 1 * oy;

	points[0] = p1;
	points[1] = p2;

	return points;
}

void generatePoints() {
	time_t t;

	srand((unsigned)time(&t));
	//setup training matrices
	trainingAngles.height = 2;
	trainingAngles.width = datapoints;
	trainingAngles.elements = (float*)malloc(trainingAngles.width * trainingAngles.height * sizeof(float));
	
	trainingElbows.height = 2;
	trainingElbows.width = datapoints;
	trainingElbows.elements = (float*)malloc(trainingAngles.width * trainingAngles.height * sizeof(float));

	trainingEndPoints.height = 2;
	trainingEndPoints.width = datapoints;
	trainingEndPoints.elements = (float*)malloc(trainingAngles.width * trainingAngles.height * sizeof(float));

	//setup testing Matricies
	testingAngles.height = 2;
	testingAngles.width = datapoints;
	testingAngles.elements = (float*)malloc(trainingAngles.width * trainingAngles.height * sizeof(float));
	testingElbows.height = 2;
	testingElbows.width = datapoints;
	testingElbows.elements = (float*)malloc(trainingAngles.width * trainingAngles.height * sizeof(float));
	testingEndPoints.height = 2;
	testingEndPoints.width = datapoints;
	testingEndPoints.elements = (float*)malloc(trainingAngles.width * trainingAngles.height * sizeof(float));

	for (int i = 0; i < datapoints; i++) {
		

		float a1 = randomf(0.0, PI);
		float a2 = randomf(0.0, PI);
		
		trainingAngles.elements[i] = a1;
		trainingAngles.elements[i + trainingAngles.width] = a2;

		float a3 = randomf(0.0, PI);
		float a4 = randomf(0.0, PI);

		testingAngles.elements[i] = a3;
		testingAngles.elements[i + testingAngles.width] = a4;

		point* data = kinematics2D(length, length, a1, a2, 0, 0);

		trainingEndPoints.elements[i] = data[0].x;
		trainingEndPoints.elements[i + trainingEndPoints.width] = data[0].y;
		
		trainingElbows.elements[i] = data[1].x;
		trainingElbows.elements[i + trainingElbows.width] = data[1].y;
		
		data = kinematics2D(length, length, a3, a4, 0, 0);

		testingEndPoints.elements[i] = data[0].x;
		testingEndPoints.elements[i + testingEndPoints.width] = data[0].y;

		testingEndPoints.elements[i] = data[1].x;
		testingEndPoints.elements[i + testingEndPoints.width] = data[1].y;
	}
	
}

//adapted from practical 6
Matrix MatrixMultiplicationLinear(Matrix A,Matrix B) {
	Matrix C;
	C.height = A.height;
	C.width = B.width;
	C.elements = (float*)malloc(C.width * C.height * sizeof(float));
	for (int i = 0; i < C.height; i++) {
		for (int j = 0; j < C.width; j++) {
			C.elements[j + i * C.width] = 0;

			for (int k = 0; k < A.width; k++) {
				C.elements[j + i * C.width] += A.elements[k + i * A.width] * B.elements[j + k * B.width];
			}
		}

	}

	return C;
}

Matrix transpose(Matrix A) {
	Matrix B;
	B.height = A.width;
	B.width = A.height;
	B.elements = (float*)malloc(B.width * B.height * sizeof(float));
	for (int i = 0; i < B.width; ++i)
		for (int j = 0; j < B.height; ++j) {
			B.elements[i + j * B.width] = A.elements[j + i * A.width];
		}
	return B;
}
float Average(Matrix A) {
	float sum = 0;
	float average;
	for (int i = 0; i < A.height; i++) {
		for (int j = 0; j < A.width; j++) {
			sum += A.elements[j + i * A.width];
		}
	}
	average = sum / (float)(A.height * A.width);
	return average;
}

void printMatrix(Matrix A) {
	for (int k = 0; k < A.height; k++) {
		for (int l = 0; l < A.width; l++) {
			printf("%f ", A.elements[k * A.width + l]);
		}
		printf(";\n");
	}
}
void linearSolution() {

	//number of inputs and outputs for network
	const int inputs = 3;
	const int outputs = 2;

	//setup network arrays
	Matrix w1;
	w1.height = hiddenNodes;
	w1.width = inputs;
	w1.elements = (float*)malloc(w1.width * w1.height * sizeof(float));
	Matrix w2;
	w2.height = outputs;
	w2.width = hiddenNodes + 1;
	w2.elements = (float*)malloc(w2.width * w2.height * sizeof(float));
	
	//initalise weight matrix
	for (int i = 0; i < w1.height; i++) {
		for (int j = 0; j < w1.width; j++) {
			w1.elements[j + i * w1.width]  = randomf(0.f, 1.0f);

		}

	}
	for (int k = 0; k < w2.height; k++) {
		for (int i = 0; i < w2.width; i++) {
			w2.elements[i + k * w2.width] = randomf(0.f, 1.0f);
		}
		
	}



	//train network
	//for n cycles
	Matrix error{1,cycles};
	error.elements = (float*)malloc(error.width * error.height * sizeof(float));
	for (int i = 0; i < cycles; i++) {
		//for each datapoint
		for (int j = 0; j < datapoints; j++) {
			
			//initialise variables for feed Forward
			Matrix l1activation;
			Matrix activation;

			//get current data and augment
			Matrix currentData = { 1,inputs };
			currentData.elements = (float*)malloc(currentData.width * currentData.height * sizeof(float));
			currentData.elements[0] = trainingEndPoints.elements[i];
			currentData.elements[1] = trainingEndPoints.elements[i + trainingEndPoints.width];
			currentData.elements[2] = 1;
			//activations for layer 1
			l1activation = MatrixMultiplicationLinear(w1, currentData);

			//sigmoid and augment agian
			Matrix l1Augment = { l1activation.width, l1activation.height + 1};
			
			l1Augment.elements = (float*)malloc(l1Augment.width * l1Augment.height * sizeof(float));
			
			for (int k = 0; k < l1Augment.height * l1Augment.width; k++) {
				if (k < l1activation.height * l1activation.width) {
					
					l1Augment.elements[k] = 1.0f / (float) (1 + exp(-l1activation.elements[k]));
				}
				else {
					l1Augment.elements[k] = 1;
				}
				
			}

			//get final activations
			activation = MatrixMultiplicationLinear(w2, l1Augment);
			///////////////////////////////////////////////////////////
			
			//calculate delta values
			Matrix d2 = {1,hiddenNodes};
			d2.elements = (float*)malloc(d2.width * d2.height * sizeof(float));
			Matrix d3 = { 1,outputs };
			d3.elements = (float*)malloc(d3.width * d3.height * sizeof(float));
			//get current targets
			float t[2] = { trainingAngles.elements[j],trainingAngles.elements[j + trainingAngles.width] };
			
			for (int k = 0; k < outputs; k++) {
				d3.elements[k] = -(t[k] - activation.elements[k]);
				
			}
			Matrix w2t = transpose(w2);
			for (int k = 0; k < hiddenNodes; k++) {
				Matrix temp = MatrixMultiplicationLinear(w2t, d3);
				d2.elements[k] = temp.elements[k] * (l1Augment.elements[k] * (1 - l1Augment.elements[k]));
				free(temp.elements);
			}
			//////////////////////////////////////////////////////////////
			//update weights

			//update w1
			for (int k = 0; k < w1.height; k++) {
				for (int l = 0; l < w1.width; l++) {
					Matrix inputDataT = transpose(currentData);
					
					Matrix d2xInputT = MatrixMultiplicationLinear(d2, inputDataT);
					w1.elements[l + k * w1.width] = w1.elements[l + k * w1.width] - lr * d2xInputT.elements[l + k * d2xInputT.width];
					free(inputDataT.elements);
					free(d2xInputT.elements);
				}
			}
			//update w2
			for (int k = 0; k < w2.height; k++) {
				for (int l = 0; l < w2.width; l++) {
					Matrix a2T = transpose(l1Augment);
					Matrix d3xa2t = MatrixMultiplicationLinear(d3, a2T);
					w2.elements[l + k * w2.width] = w2.elements[l + k * w2.width] - lr * d3xa2t.elements[l + k * w2.width];
					free(a2T.elements);
					free(d3xa2t.elements);
				}
			}
			free(currentData.elements);
			free(activation.elements);
			free(d2.elements);
			free(d3.elements);
			free(w2t.elements);
			free(l1Augment.elements);
			free(l1activation.elements);
			
		}


		Matrix activationCycle;
			//initialise variables for feed Forward
			Matrix l1activation;

			//get current data and augment
			Matrix currentData = trainingEndPoints;
			
			//activations for layer 1
			l1activation = MatrixMultiplicationLinear(w1, currentData);


			//sigmoid and augment agian
			Matrix l1Augment = { l1activation.width, l1activation.height + 1};
			
			l1Augment.elements = (float*)malloc(l1Augment.width * l1Augment.height * sizeof(float));
			
			for (int k = 0; k < l1Augment.height * l1Augment.width; k++) {
				if (k < l1activation.height * l1activation.width) {
					
					l1Augment.elements[k] = 1.0f / (1 + exp(-l1activation.elements[k]));
				}
				else {
					l1Augment.elements[k] = 1;
				}
				
			}
			//get final activations
			activationCycle = MatrixMultiplicationLinear(w2, l1Augment);
			
			for (int k = 0; k < activationCycle.width * activationCycle.height; k++) {
				activationCycle.elements[k] = (trainingAngles.elements[k] - activationCycle.elements[k]) * (trainingAngles.elements[k] - activationCycle.elements[k]);
			}

		float average = Average(activationCycle);
		error.elements[i] = average;
		free(activationCycle.elements);
		free(l1Augment.elements);
		free(l1activation.elements);
		printf("e(%d): %f \n",i, error.elements[i]);
	}

}


int main()
{
	generatePoints();

	linearSolution();
	


}
