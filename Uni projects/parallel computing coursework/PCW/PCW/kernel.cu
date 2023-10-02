
#include "cuda_runtime.h"
#include "device_launch_parameters.h"
#include <time.h>;
#include <stdlib.h>
#include <stdio.h>
#include"win-gettimeofday.h" //taken from practical work, all yours Dr Gianni


#define BLOCK_SIZE 32
#define DIMX 1000
#define DIMY 1000
#define DIMZ 1000

typedef struct {
	int width;
	int height;
	float* elements; 
} Matrix;



Matrix MatrixMultiplicationLinear(Matrix A, Matrix B) {
	Matrix C;
	C.height = A.height;
	C.width = B.width;
	//allocate memory
	C.elements = (float*)malloc(C.width * C.height * sizeof(float));
	//for each point in matrix C 
	for (int i = 0; i < C.height; i++) {
		for (int j = 0; j < C.width; j++) {
			C.elements[j + i * C.width] = 0;
			
			//calculate value
			for (int k = 0; k < A.width; k++) {
				C.elements[j + i * C.width] += A.elements[k + i * A.width] * B.elements[j + k * B.width];
			}
		}

	}

	return C;
}

__global__ void matrixMult(Matrix A, Matrix B, Matrix C) {

	//variables for shared memory
	__shared__ float As[BLOCK_SIZE][BLOCK_SIZE];
	__shared__ float Bs[BLOCK_SIZE][BLOCK_SIZE];

	//block ids
	int blockY = blockIdx.y;
	int blockX = blockIdx.x;

	float sum = 0.f;
	//thread ids
	int threadY = threadIdx.y;
	int threadX = threadIdx.x;

	//fill As and Bs
	for (int i = 0; i < (BLOCK_SIZE + A.width - 1) / BLOCK_SIZE; i++) {
		//if thread should have a value (if matrix dimentions dont match blocksize)
		if (i * BLOCK_SIZE + threadX < A.width && blockY * BLOCK_SIZE + threadY < A.height) {
			As[threadY][threadX] = A.elements[(blockY * BLOCK_SIZE + threadY) * A.width + i * BLOCK_SIZE + threadX];
		}
		else
		{
			As[threadY][threadX] = 0.f;
		}
		//same for Bs
		if (i * BLOCK_SIZE + threadY < B.height && blockX * BLOCK_SIZE + threadX < B.width) {
			Bs[threadY][threadX] = B.elements[(i * BLOCK_SIZE + threadY) * B.width + blockX * BLOCK_SIZE + threadX];
		}
		else
		{
			Bs[threadY][threadX] = 0.f;
		}
		//make sure everything is done
		__syncthreads();

		//work out partial sums
		for (int j = 0; j < BLOCK_SIZE; j++)
			sum += As[threadY][j] * Bs[j][threadX];

		__syncthreads();
	}
	//check if threadX/y is a position in C.elements (same as before)
	if (blockY * BLOCK_SIZE + threadY < C.height && blockX * BLOCK_SIZE + threadX < C.width) {
		
		C.elements[((blockY * BLOCK_SIZE + threadY) * C.width) + (blockX * BLOCK_SIZE) + threadX] = sum;
	}

}

Matrix cudaSolution(const Matrix A, const Matrix B) {

	//create and allocate memeory for devices
	Matrix DeviceA;
	Matrix DeviceB;
	Matrix DeviceC;
	Matrix C;
	DeviceC.width = C.width = B.width;
	DeviceC.height = C.height = A.height;

	C.elements = (float*)malloc(C.width * C.height * sizeof(float));

	DeviceA.height = A.height;
	DeviceA.width = A.width;

	cudaMalloc(&DeviceA.elements, A.width * A.height * sizeof(float));
	//copy A to deviceA
	cudaMemcpy(DeviceA.elements, A.elements, A.width * A.height * sizeof(float), cudaMemcpyHostToDevice);

	DeviceB.width = B.width;
	DeviceB.height = B.height;
	cudaMalloc(&DeviceB.elements, A.width * A.height * sizeof(float));
	//copy B to deviceB
	cudaMemcpy(DeviceB.elements, B.elements, B.width * B.height * sizeof(float), cudaMemcpyHostToDevice);

	cudaMalloc(&DeviceC.elements, C.width * C.height * sizeof(float));
	
	
	dim3 dimBlock(BLOCK_SIZE, BLOCK_SIZE);
	//set gridsize
	dim3 dimGrid((B.width + dimBlock.x - 1) / dimBlock.x, (A.height + dimBlock.y - 1) / dimBlock.y);

	matrixMult << <dimGrid, dimBlock >> > (DeviceA, DeviceB, DeviceC);
	//make sure everything finished
	cudaThreadSynchronize();

	///copy finished matrix to output matrix
	cudaMemcpy(C.elements, DeviceC.elements, C.width * C.height * sizeof(float), cudaMemcpyDeviceToHost);
	
	
	cudaFree(DeviceA.elements);
	cudaFree(DeviceB.elements);
	cudaFree(DeviceC.elements);
	
	return C;
}

void initialiseMatrices(Matrix &A, Matrix &B) {
	//stuff for random
	time_t t;

	srand((unsigned)time(&t));
	A.elements = (float*)malloc(A.width * A.height * sizeof(float));
	B.elements = (float*)malloc(B.width * B.height * sizeof(float));

	//fill both matrices with random numbers 0-10
	for (int i = 0; i < A.height; i++) {
		for (int j = 0; j < A.width; j++) {
			A.elements[j + i * A.width] = rand() % 10;
		}
	}
	for (int i = 0; i < B.height; i++) {
		for (int j = 0; j < B.width; j++) {
			B.elements[j + i * B.width] = rand() % 10;
		}
	}

}

int main(int argc,char*argv[])
{
	Matrix A;
	Matrix B;
	//get matrix dimensions
	printf("Enter height for Matrix 1 \n");
	scanf("%d",&A.height);
	printf("Enter width for Matrix 1 \n");
	scanf("%d", &A.width);

	printf("Enter height for Matrix 2 \n");
	scanf("%d", &B.height);
	printf("Enter width for Matrix 2 \n");
	scanf("%d", &B.width);

	while (A.width != B.height) {
		printf("invalid dimentions \n");
		printf("Enter height for Matrix 1 \n");
		scanf("%d", &A.height);
		printf("Enter width for Matrix 1 \n");
		scanf("%d", &A.width);

		printf("Enter height for Matrix 2 \n");
		scanf("%d", &B.height);
		printf("Enter width for Matrix 2 \n");
		scanf("%d", &B.width);
	}

	initialiseMatrices(A,B);
	//get time
	double start = 0.0;
	start = get_current_time();
	//do linear multiplication
	Matrix C = MatrixMultiplicationLinear(A, B);
	//get time again
	double  end = 0.0;
	end = get_current_time();
	//work out how much time passed
	double t = (end - start);
	
	printf("Time Taken for linear:  %lf seconds \n", t);

	start = get_current_time();
	//do paralel multiplication
	Matrix PC = cudaSolution(A,B);
	
	end = get_current_time();

	t = (end - start);
	printf("Time Taken for paralel:  %lf seconds \n", t);


	
	bool isSame = true;
	for (int i = 0; i < PC.height; i++) {
		for (int j = 0; j < PC.width; j++) {

			
			if (PC.elements[i * PC.width + j] != C.elements[i * PC.width + j]) {
				isSame = false;
				printf("%d, %f \n", i * PC.width + j,PC.elements[i * PC.width + j] - C.elements[i * PC.width + j]);
			}

		}
	}

	if (isSame) {
		printf("Both Matrices are the same");
	}
	else {
		printf("Matrices do not match");
	}

	free(A.elements);
	free(B.elements);
	free(C.elements);
	free(PC.elements);
    return 0;
}


